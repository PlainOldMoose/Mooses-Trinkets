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

public class TrinketsGUI {
    private final int size = 54; // Inventory size
    // TODO - setup config for this
    private final String title = "               Trinkets"; // GUI title
    private Inventory inventory;

    /**
     * Displays the Trinkets GUI to the specified player. Sets up the inventory, background, buttons, and updates the GUI.
     * If the player already has a TrinketsGUI metadata, their current inventory is closed.
     *
     * @param player The player to whom the GUI will be displayed.
     */
    public void displayTo(Player player) {
        inventory = Bukkit.createInventory(player, this.size, this.title);
        BackgroundBuilder.createBackgroundTiles();
        TrinketSlotBuilder.createSlotButtons();
        IconBuilder.createStatsIcons(player);
        PlayerDataLoader.loadPlayerTrinkets(player);

        updateGUI(player);

        if (player.hasMetadata("TrinketsGUI")) {
            player.closeInventory();
        }

        player.setMetadata("TrinketsGUI", new FixedMetadataValue(Trinkets.getInstance(), this));
        player.openInventory(inventory);
    }

    private void renderBackgrounds() {
        for (Background background : BackgroundBuilder.getBackgroundList()) {
            inventory.setItem(background.getIndex(), background.getDisplayItem());
        }
    }

    private void renderStatIcons(Player player) {
        IconBuilder.createStatsIcons(player);
        for (StatsIcon icon : IconBuilder.getIconList()) {
            inventory.setItem(icon.getIndex(), icon.getDisplayItem());
        }
    }
    
    private void renderButtons() {
        for (Map.Entry<Integer, TrinketSlot> entry : TrinketSlotBuilder.getTrinketSlotMap().entrySet()) {
            TrinketSlot trinketSlot = entry.getValue();
            if (trinketSlot.isEnabled()) {
                inventory.setItem(trinketSlot.getIndex(), trinketSlot.getContainedTrinket() != null ? trinketSlot.getContainedTrinket() : trinketSlot.getDisplayItem());
            }
        }
    }

    public void updateGUI(Player player) {
        renderBackgrounds();
        renderStatIcons(player);
        renderButtons();
    }
}
