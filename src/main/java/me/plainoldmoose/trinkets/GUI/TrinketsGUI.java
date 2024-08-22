package me.plainoldmoose.trinkets.GUI;

import me.plainoldmoose.trinkets.Data.Trinket;
import me.plainoldmoose.trinkets.Data.handlers.DataHandler;
import me.plainoldmoose.trinkets.Data.handlers.TrinketSlotsHandler;
import me.plainoldmoose.trinkets.GUI.components.Background;
import me.plainoldmoose.trinkets.GUI.components.StatsIcon;
import me.plainoldmoose.trinkets.GUI.components.TrinketSlot;
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

import static org.bukkit.Bukkit.getLogger;

public class TrinketsGUI {
    private final int size = 54; // Inventory size
    // TODO - setup config for this
    private final String title = "               Trinkets"; // GUI title
    private Inventory inventory;

    private final HashMap<Integer, TrinketSlot> buttonMap = new HashMap<>();

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
            inventory.setItem(background.getIndex(), background.getDisplayItem());
        }
    }

    private void renderStatIcons(Player player) {
        PlayerStatsFetcher.createStatsIcons(player);

        // Check if Vault is installed and if its Chat class is available
        boolean vaultChatAvailable = isVaultChatAvailable();

        for (StatsIcon icon : PlayerStatsFetcher.getIconList()) {
            String iconItemName = icon.getDisplayItem().getItemMeta().getDisplayName();
            List<String> stats = icon.getStatsList();

            if (iconItemName.contains("%playername%")) {
                if (icon.getDisplayItem().getType() == Material.PLAYER_HEAD) {
                    icon.setDisplayItem(ItemFactory.createPlayerHead(player.getUniqueId()));
                }

                iconItemName = iconItemName.replace("%playername%", player.getDisplayName());

                if (vaultChatAvailable && ChatServiceFetcher.getInstance().getChat() != null) {
                    iconItemName = ConfigUtils.colorizeString(ChatServiceFetcher.getInstance().getPlayerPrefix(player)) + iconItemName;
                }

                icon.setDisplayItem(ItemFactory.changeItemStackName(icon.getDisplayItem(), iconItemName));
                icon.setDisplayItem(ItemFactory.changeItemStackLore(icon.getDisplayItem(), stats));
            }

            inventory.setItem(icon.getIndex(), icon.getDisplayItem());
        }
    }

    // TODO - refactor this shit it's ugly asf ty
    private boolean isVaultChatAvailable() {
        try {
            Class.forName("net.milkbowl.vault.chat.Chat");
            return true;
        } catch (ClassNotFoundException e) {
            getLogger().warning("Vault's Chat class is not available. Chat functionality will be disabled.");
            return false;
        }
    }


    private void renderButtons() {
        for (Map.Entry<Integer, TrinketSlot> entry : buttonMap.entrySet()) {
            TrinketSlot trinketSlot = entry.getValue();
            if (trinketSlot.isEnabled()) {
                inventory.setItem(trinketSlot.getIndex(), trinketSlot.getContainedTrinket() != null ? trinketSlot.getContainedTrinket() : trinketSlot.getDisplayItem());
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
        Set<TrinketSlot> trinketSlotSet = TrinketSlotsHandler.getInstance().getTrinketSlotSet();;

        for (TrinketSlot ts : trinketSlotSet) {
            buttonMap.put(ts.getIndex(), ts);
        }
    }

    // TODO - re-implement this to fix duplication
    private void loadTrinketSaveData(Player player) {
        Map<UUID, List<ItemStack>> savedTrinkets = DataHandler.getInstance().getEquippedTrinkets();;
        List<ItemStack> playerTrinkets = savedTrinkets.get(player.getUniqueId());

        if (playerTrinkets == null) {
            return;
        }

        for (TrinketSlot b : buttonMap.values()) {
            for (ItemStack t : playerTrinkets) {
                Trinket trinket = Trinkets.getInstance().getManager().getTrinket(t);
                if (b.getType().equals(trinket.getType())) {
                    b.push(t);
                }
            }
        }
    }

    public HashMap<Integer, TrinketSlot> getButtonMap() {
        return buttonMap;
    }
}
