package me.plainoldmoose.trinkets.gui;

import me.plainoldmoose.trinkets.Trinkets;
import me.plainoldmoose.trinkets.data.loaders.PlayerDataLoader;
import me.plainoldmoose.trinkets.gui.builders.BackgroundBuilder;
import me.plainoldmoose.trinkets.gui.builders.IconBuilder;
import me.plainoldmoose.trinkets.gui.builders.TrinketSlotBuilder;
import me.plainoldmoose.trinkets.gui.components.Background;
import me.plainoldmoose.trinkets.gui.components.StatsIcon;
import me.plainoldmoose.trinkets.gui.components.TrinketSlot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Map;

/**
 * Manages and displays the Trinkets GUI to players.
 * This includes setting up the inventory, rendering background items, stats icons, and TrinketSlots.
 */
public class TrinketsGUI {
    private final int size = 54; // Size of the inventory (54 slots)
    private final String title = "               Trinkets"; // Title of the GUI
    private Inventory inventory;
    /**
     * Displays the Trinkets GUI to the specified player. Sets up the inventory, background, buttons, and updates the GUI.
     * If the player is already viewing a TrinketsGUI, their current inventory is closed.
     *
     * @param player The player to whom the GUI will be displayed.
     */
    public void displayTo(Player player) {
        // Create a new inventory with the specified size and title
        inventory = Bukkit.createInventory(player, this.size, this.title);

        // Build and render the GUI components
        BackgroundBuilder.createBackgroundTiles();
        TrinketSlotBuilder.createSlotButtons();
        IconBuilder.createStatsIcons(player);
        PlayerDataLoader.loadPlayerTrinkets(player);

        // Update the GUI with current data
        updateGUI(player);

        // Close any existing TrinketsGUI for the player
        if (player.hasMetadata("TrinketsGUI")) {
            player.closeInventory();
        }

        // Set metadata to indicate that the player is viewing the TrinketsGUI
        player.setMetadata("TrinketsGUI", new FixedMetadataValue(Trinkets.getInstance(), this));

        // Open the inventory for the player
        player.openInventory(inventory);
    }

    /**
     * Renders the background items onto the inventory.
     */
    private void renderBackgrounds() {
        for (Background background : BackgroundBuilder.getBackgroundList()) {
            inventory.setItem(background.getIndex(), background.getDisplayItem());
        }
    }

    /**
     * Renders the stats icons onto the inventory.
     * Rebuilds the stats icons based on the current player's data.
     *
     * @param player The player whose stats icons are to be displayed.
     */
    private void renderStatIcons(Player player) {
        IconBuilder.createStatsIcons(player);
        for (StatsIcon icon : IconBuilder.getIconList()) {
            inventory.setItem(icon.getIndex(), icon.getDisplayItem());
        }
    }

    /**
     * Renders the TrinketSlots onto the inventory.
     * Updates the slots based on whether they are enabled and whether they contain trinkets.
     */
    private void renderButtons() {
        for (Map.Entry<Integer, TrinketSlot> entry : TrinketSlotBuilder.getTrinketSlotMap().entrySet()) {
            TrinketSlot trinketSlot = entry.getValue();
            if (trinketSlot.isEnabled()) {
                inventory.setItem(trinketSlot.getIndex(), trinketSlot.getContainedTrinket() != null ? trinketSlot.getContainedTrinket() : trinketSlot.getDisplayItem());
            }
        }
    }

    /**
     * Updates the GUI with current data by rendering backgrounds, stats icons, and TrinketSlots.
     *
     * @param player The player whose GUI is being updated.
     */
    public void updateGUI(Player player) {
        renderBackgrounds();
        renderStatIcons(player);
        renderButtons();
    }
}
