package me.plainoldmoose.trinkets.GUI;

import me.plainoldmoose.trinkets.Data.Trinket;
import me.plainoldmoose.trinkets.Data.handlers.ConfigHandler;
import me.plainoldmoose.trinkets.Data.handlers.DataHandler;
import me.plainoldmoose.trinkets.GUI.components.Background;
import me.plainoldmoose.trinkets.GUI.components.StatsIcon;
import me.plainoldmoose.trinkets.GUI.components.TrinketSlot;
import me.plainoldmoose.trinkets.GUI.components.TrinketSlotButton;
import me.plainoldmoose.trinkets.GUI.fetchers.BackgroundFetcher;
import me.plainoldmoose.trinkets.GUI.fetchers.ChatServiceFetcher;
import me.plainoldmoose.trinkets.GUI.fetchers.PlayerStatsFetcher;
import me.plainoldmoose.trinkets.GUI.interactions.TrinketInteractionHandler;
import me.plainoldmoose.trinkets.Trinkets;
import me.plainoldmoose.trinkets.utils.ConfigUtils;
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

    private final HashMap<Integer, TrinketSlotButton> buttonMap = new HashMap<>();

    private final BackgroundFetcher BackgroundFetcher = new BackgroundFetcher();
    private final TrinketInteractionHandler trinketInteractionHandler = new TrinketInteractionHandler();


    /**
     * Displays the Trinkets GUI to the specified player. Sets up the inventory, background, buttons, and updates the GUI.
     * If the player already has a TrinketsGUI metadata, their current inventory is closed.
     *
     * @param player The player to whom the GUI will be displayed.
     */
    public void displayTo(Player player) {
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

                if (ChatServiceFetcher.getInstance().getChat() != null) {
                    iconItemName = ConfigUtils.colorizeString(ChatServiceFetcher.getInstance().getPlayerPrefix(player)) + iconItemName;
                }
                icon.setItem(ItemFactory.changeItemStackName(icon.getItem(), iconItemName));
                icon.setItem(ItemFactory.changeItemStackLore(icon.getItem(), stats));
            }

            inventory.setItem(icon.getSlot(), icon.getItem());
        }
    }

    private void renderButtons() {
        for (Map.Entry<Integer, TrinketSlotButton> entry : buttonMap.entrySet()) {
            TrinketSlotButton trinketSlotButton = entry.getValue();
            if (trinketSlotButton.isEnabled()) {
                inventory.setItem(trinketSlotButton.getSlot(), trinketSlotButton.getContainedItem() != null ? trinketSlotButton.getContainedItem() : trinketSlotButton.getItem());
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
        Set<TrinketSlot> trinketSlotSet = ConfigHandler.getInstance().getTrinketSlotSet();;

        for (TrinketSlot ts : trinketSlotSet) {
            createTrinketSlotButton(ts);
        }
    }

    private void loadTrinketSaveData(Player player) {
        Map<UUID, List<ItemStack>> savedTrinkets = DataHandler.getInstance().getEquippedTrinkets();;
        List<ItemStack> playerTrinkets = savedTrinkets.get(player.getUniqueId());

        if (playerTrinkets == null) {
            return;
        }

        for (TrinketSlotButton b : buttonMap.values()) {
            for (ItemStack t : playerTrinkets) {
                Trinket trinket = Trinkets.getInstance().getManager().getTrinketByDisplayName(t.getItemMeta().getDisplayName());
                if (b.getKey().equals(trinket.getSlot())) {
                    b.push(t);
                }
            }
        }
    }

    /**
     * Creates a button for a given TrinketSlot and adds it to the button map.
     *
     * @param slot The TrinketSlot for which the button is created.
     */
    private void createTrinketSlotButton(TrinketSlot slot) {
        TrinketSlotButton trinketSlotButton = new TrinketSlotButton(slot.getSlot(), slot.getBackground(), slot.getKey()) {
            @Override
            public void onClick(Player player) {
                trinketButtonClickHandler(player, slot.getSlot());
            }
        };
        trinketSlotButton.setVisibility(slot.isEnabled());
        buttonMap.put(slot.getSlot(), trinketSlotButton);
    }

    private void trinketButtonClickHandler(Player player, int slot) {
        TrinketSlotButton trinketSlotButton = buttonMap.get(slot);
        trinketInteractionHandler.handleButtonClick(player, slot, trinketSlotButton);
    }

    public HashMap<Integer, TrinketSlotButton> getButtonMap() {
        return buttonMap;
    }
}
