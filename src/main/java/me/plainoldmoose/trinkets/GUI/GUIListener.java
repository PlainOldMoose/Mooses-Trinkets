package me.plainoldmoose.trinkets.GUI;

import me.plainoldmoose.trinkets.Data.TrinketManager;
import me.plainoldmoose.trinkets.Data.handlers.DataHandler;
import me.plainoldmoose.trinkets.Data.handlers.Keys;
import me.plainoldmoose.trinkets.GUI.components.Background;
import me.plainoldmoose.trinkets.GUI.components.TrinketSlotButton;
import me.plainoldmoose.trinkets.GUI.fetchers.BackgroundFetcher;
import me.plainoldmoose.trinkets.Trinkets;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

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
            for (final Background background : BackgroundFetcher.getBackgroundList()) {
                if (background.getSlot() == event.getSlot()) {
                    event.setCancelled(true);
                }
            }

            // Handle interactions with buttons
            for (Map.Entry<Integer, TrinketSlotButton> entry : menu.getButtonMap().entrySet()) {
                TrinketSlotButton trinketSlotButton = entry.getValue();
                if (trinketSlotButton.getSlot() == event.getSlot()) {
                    trinketSlotButton.onClick(eventPlayer);
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

        Inventory inventory = event.getInventory();
        List<ItemStack> trinketList = new ArrayList<ItemStack>();

        for (ItemStack item : inventory.getContents()) {
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer itemContainer = meta.getPersistentDataContainer();
            if (itemContainer.has(Keys.TRINKET)) {
                TrinketManager manager = Trinkets.getInstance().getManager();
                trinketList.add(item);
            }
        }

        UUID playerUUID = eventPlayer.getUniqueId();

        DataHandler.getInstance().getEquippedTrinkets().put(playerUUID, trinketList);
        DataHandler.getInstance().saveData();

        eventPlayer.removeMetadata("TrinketsGUI", Trinkets.getInstance());
    }
}