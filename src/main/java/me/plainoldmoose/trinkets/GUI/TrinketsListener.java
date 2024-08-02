package me.plainoldmoose.trinkets.GUI;

import me.plainoldmoose.trinkets.Trinkets;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public class TrinketsListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        final Player eventPlayer = (Player) event.getWhoClicked();
        Inventory clickedInv = event.getClickedInventory();
        Inventory topInv = eventPlayer.getOpenInventory().getTopInventory();

        // Check if the clicked inventory is the Trinkets GUI
        if (!clickedInv.equals(topInv)) {
            return;
        }

        // Check if the player has metadata indicating that they are viewing the Trinkets GUI
        if (eventPlayer.hasMetadata("TrinketsGUI")) {
            final TrinketsGUI menu = (TrinketsGUI) eventPlayer.getMetadata("TrinketsGUI").get(0).value();

            // Cancel interactions with background tiles
            for (final Background background : menu.getBackgrounds()) {
                if (background.getSlot() == event.getSlot()) {
                    event.setCancelled(true);
                }
            }

            // Handle interactions with buttons
            for (Map.Entry<Integer, Button> entry : menu.getButtonMap().entrySet()) {
                Button button = entry.getValue();
                if (button.getSlot() == event.getSlot()) {
                    button.onClick(eventPlayer);
                    event.setCancelled(true);
                }
            }

            menu.updateGUI(eventPlayer);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        final Player eventPlayer = (Player) event.getPlayer();

        if (eventPlayer.hasMetadata("TrinketsGUI")) {
            eventPlayer.removeMetadata("TrinketsGUI", Trinkets.getInstance());
        }
    }
}