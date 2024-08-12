package me.plainoldmoose.trinkets.GUI;

import me.plainoldmoose.trinkets.Data.Trinket;
import me.plainoldmoose.trinkets.Data.TrinketsData;
import me.plainoldmoose.trinkets.Data.handlers.ConfigHandler;
import me.plainoldmoose.trinkets.GUI.components.Background;
import me.plainoldmoose.trinkets.GUI.components.Button;
import me.plainoldmoose.trinkets.GUI.components.StatsIcon;
import me.plainoldmoose.trinkets.GUI.components.TrinketSlot;
import me.plainoldmoose.trinkets.GUI.fetchers.BackgroundFetcher;
import me.plainoldmoose.trinkets.GUI.fetchers.ChatServiceFetcher;
import me.plainoldmoose.trinkets.GUI.fetchers.PlayerStatsFetcher;
import me.plainoldmoose.trinkets.GUI.interactions.TrinketInteractionHandler;
import me.plainoldmoose.trinkets.Trinkets;
import me.plainoldmoose.trinkets.utils.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;

public class TrinketsGUI {
    private final int size = 54; // Inventory size
    // TODO - setup config for this
    private final String title = "               Trinkets"; // GUI title
    private Inventory inventory;

    private final HashMap<Integer, Button> buttonMap = new HashMap<>();

    private final ChatServiceFetcher chatSetup = new ChatServiceFetcher();
    private final BackgroundFetcher BackgroundFetcher = new BackgroundFetcher();
    private final TrinketInteractionHandler trinketInteractionHandler = new TrinketInteractionHandler();


    /**
     * Displays the Trinkets GUI to the specified player. Sets up the inventory, background, buttons, and updates the GUI.
     * If the player already has a TrinketsGUI metadata, their current inventory is closed.
     *
     * @param player The player to whom the GUI will be displayed.
     */
    public void displayTo(Player player) {
        if (!chatSetup.setupChat()) {
            return; // Cannot proceed without chat setup failed
        }

        inventory = Bukkit.createInventory(player, this.size, this.title);
        BackgroundFetcher.createBackgroundTiles();
        createSlotButtons();
        loadTrinketSaveData(player);
        updateGUI(player);

        if (player.hasMetadata("TrinketsGUI")) {
            player.closeInventory();
        }

        player.setMetadata("TrinketsGUI", new FixedMetadataValue(Trinkets.getInstance(), this));
        player.openInventory(inventory);
    }

    private void renderBackgrounds() {
        for (Background background : BackgroundFetcher.getBackgroundList()) {
            inventory.setItem(background.getSlot(), background.getItem());
        }
    }

    private void renderStatIcons(Player player) {
        PlayerStatsFetcher.createStatsIcons(player);

        for (StatsIcon icon : PlayerStatsFetcher.getIconList()) {
            String iconItemName = icon.getItem().getItemMeta().getDisplayName();
            List<String> stats = icon.getStatsList();

            // This is some fancy shenanigans because ItemStack is bad and I hate bukkit.
            if (iconItemName.contains("%playername%")) {
                if (icon.getItem().getType() == Material.PLAYER_HEAD) {
                    icon.setItem(ItemFactory.createPlayerHead(player.getUniqueId()));
                }

                iconItemName = iconItemName.replace("%playername%", player.getDisplayName());
                icon.setItem(ItemFactory.changeItemStackName(icon.getItem(), iconItemName));
                icon.setItem(ItemFactory.changeItemStackLore(icon.getItem(), stats));
            }

            inventory.setItem(icon.getSlot(), icon.getItem());
        }
    }

    private void renderButtons() {
        for (Map.Entry<Integer, Button> entry : buttonMap.entrySet()) {
            Button button = entry.getValue();
            if (button.isEnabled()) {
                inventory.setItem(button.getSlot(), button.getContainedItem() != null ? button.getContainedItem() : button.getItem());
            }
        }
    }

    /**
     * Updates the GUI for the specified player. Sets the background items, button items, and player skull.
     *
     * @param player The player whose GUI will be updated.
     */
    public void updateGUI(Player player) {
        renderBackgrounds();
        renderStatIcons(player);
        renderButtons();
    }

    /**
     * Creates buttons for each TrinketSlot and adds them to the button map.
     */
    private void createSlotButtons() {
        ConfigHandler configHandler = TrinketsData.getInstance().getConfigHandler();
        Set<TrinketSlot> trinketSlotSet = configHandler.getTrinketSlotSet();

        for (TrinketSlot ts : trinketSlotSet) {
            createButton(ts);
        }
//
//        Map<String, ItemStack> trinketSlotsMap = configHandler.getTrinketSlotMap();
//
//        for (Map.Entry<String, ItemStack> e : trinketSlotsMap.entrySet()) {
//            String name = e.getKey();
//            ItemStack item = e.getValue();
//            ItemMeta meta = item.getItemMeta();
//
//            TrinketSlot slot = new TrinketSlot(meta.getDisplayName(), )
//        }
    }

    private void loadTrinketSaveData(Player player) {
        Map<UUID, List<ItemStack>> savedTrinkets = TrinketsData.getInstance().getDataHandler().getEquippedTrinkets();
        List<ItemStack> playerTrinkets = savedTrinkets.get(player.getUniqueId());

        if (playerTrinkets == null) {
            return;
        }

        for (ItemStack t : playerTrinkets) {
            Trinket trinket = Trinkets.getInstance().getManager().getTrinketByDisplayName(t.getItemMeta().getDisplayName());
            player.sendMessage("Loading from players trinkets > " + trinket.getDisplayName());
        }

        for (int i = 0; i < playerTrinkets.size(); i++) {
            Button b = buttonMap.get(22 );
            if (b.getContainedItem() == null) {
                b.push(playerTrinkets.get(i));
            }
        }
    }

    /**
     * Creates a button for a given TrinketSlot and adds it to the button map.
     *
     * @param slot The TrinketSlot for which the button is created.
     */
    private void createButton(TrinketSlot slot) {
        Button button = new Button(slot.getSlot(), slot.getBackground()) {
            @Override
            public void onClick(Player player) {
                buttonOnClickHandler(player, slot.getSlot());
            }
        };
        button.setVisibility(slot.isEnabled());
        buttonMap.put(slot.getSlot(), button);
    }

    private void buttonOnClickHandler(Player player, int slot) {
        Button button = buttonMap.get(slot);
        trinketInteractionHandler.handleButtonClick(player, slot, button);
    }

    public HashMap<Integer, Button> getButtonMap() {
        return buttonMap;
    }
}
