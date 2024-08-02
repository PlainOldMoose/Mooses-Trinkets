package me.plainoldmoose.trinkets.GUI;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Map;

public class TrinketsListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        final Player eventPlayer = (Player) event.getWhoClicked();

        if (eventPlayer.hasMetadata("TrinketsGUI")) {
            final TrinketsGUI menu = (TrinketsGUI) eventPlayer.getMetadata("TrinketsGUI").get(0).value();

            for (final Background background : menu.getBackgrounds()) {
                if (background.getSlot() == event.getSlot()) {
                    event.setCancelled(true);
                }
            }

            for (Map.Entry<Integer, Button> entry : menu.getButtonMap().entrySet()) {
                Button button = entry.getValue();
                if (button.getSlot() == event.getSlot()) {
                    button.onClick(eventPlayer);
                    event.setCancelled(true);
                }
            }

            menu.updateGUI();
        }
    }
}