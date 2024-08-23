package me.plainoldmoose.trinkets.gui;

import me.plainoldmoose.trinkets.Trinkets;
import me.plainoldmoose.trinkets.data.loaders.PlayerDataLoader;
import me.plainoldmoose.trinkets.data.trinket.SerializedTrinketSlot;
import me.plainoldmoose.trinkets.gui.builders.BackgroundBuilder;
import me.plainoldmoose.trinkets.gui.builders.TrinketSlotBuilder;
import me.plainoldmoose.trinkets.gui.components.Background;
import me.plainoldmoose.trinkets.gui.components.TrinketSlot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        final Player eventPlayer = (Player) event.getWhoClicked();
        Inventory clickedInv = event.getClickedInventory();
        Inventory topInv = eventPlayer.getOpenInventory().getTopInventory();

        // Check if the clicked inventory is the Trinkets GUI

        if (clickedInv == null) {
            return;
        }

        if (!clickedInv.equals(topInv)) {
            return;
        }

        // Check if the player has metadata indicating that they are viewing the Trinkets GUI
        if (eventPlayer.hasMetadata("TrinketsGUI")) {
            final TrinketsGUI menu = (TrinketsGUI) eventPlayer.getMetadata("TrinketsGUI").get(0).value();

            // Cancel interactions with background tiles
            for (final Background background : BackgroundBuilder.getBackgroundList()) {
                if (background.getIndex() == event.getSlot()) {
                    event.setCancelled(true);
                }
            }

            // Handle interactions with buttons
            for (Map.Entry<Integer, TrinketSlot> entry : TrinketSlotBuilder.getTrinketSlotMap().entrySet()) {
                TrinketSlot trinketSlot = entry.getValue();
                if (trinketSlot.getIndex() == event.getSlot()) {
                    trinketSlot.onClick(eventPlayer);
                    event.setCancelled(true);
                }
            }
            menu.updateGUI(eventPlayer);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        final Player eventPlayer = (Player) event.getPlayer();

        if (!eventPlayer.hasMetadata("TrinketsGUI")) {
            return;
        }

        List<SerializedTrinketSlot> serializedTrinketSlotList = new ArrayList<>();
        for (TrinketSlot trinketSlot : TrinketSlotBuilder.getTrinketSlotMap().values()) {
            if (trinketSlot.getContainedTrinket() != null) {
                serializedTrinketSlotList.add(new SerializedTrinketSlot(trinketSlot.serialize()));
            }
        }

        UUID playerUUID = eventPlayer.getUniqueId();
        PlayerDataLoader.getInstance().getSerialisedSlots().put(playerUUID, serializedTrinketSlotList);
        PlayerDataLoader.getInstance().saveData();

        eventPlayer.removeMetadata("TrinketsGUI", Trinkets.getInstance());
    }
}