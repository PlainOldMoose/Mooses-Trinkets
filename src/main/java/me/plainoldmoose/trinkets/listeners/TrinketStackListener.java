package me.plainoldmoose.trinkets.listeners;

import me.plainoldmoose.trinkets.Trinkets;
import me.plainoldmoose.trinkets.data.trinket.Key;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;


// TODO - Fix bug where stats are added even when event is cancelled
public class TrinketStackListener implements Listener {

    private boolean isValidTrinket(ItemStack item) {
        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta()) {
            return false;
        }
        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        return container.has(Key.TRINKET);
    }

    private void handleTrinketStacking(Player player, ItemStack cursorItem, ItemStack inventoryItem, Inventory clickedInv, int slot) {
        if (inventoryItem.isSimilar(cursorItem)) {
            Bukkit.getScheduler().runTaskLater(Trinkets.getInstance(), () -> {
                player.setItemOnCursor(cursorItem);
                if (clickedInv != null) {
                    clickedInv.setItem(slot, inventoryItem);
                }
            }, 1L);
        }
    }

    @EventHandler
    public void onClickEvent(InventoryClickEvent event) {
        Player eventPlayer = (Player) event.getWhoClicked();
        Inventory clickedInv = event.getClickedInventory();
        int slot = event.getSlot();

        if (clickedInv == null || slot < 0) {
            return;
        }

        ItemStack inventoryItem = clickedInv.getItem(slot);
        ItemStack cursorItem = eventPlayer.getItemOnCursor();

        if (isValidTrinket(inventoryItem) && isValidTrinket(cursorItem)) {
            event.setCancelled(true);
            handleTrinketStacking(eventPlayer, cursorItem, inventoryItem, clickedInv, slot);
        }
    }

    @EventHandler
    public void onDoubleClick(InventoryClickEvent event) {
        Player eventPlayer = (Player) event.getWhoClicked();
        ItemStack eventItem = eventPlayer.getItemOnCursor();

        if (isValidTrinket(eventItem)) {
            if (event.getClick() == ClickType.DOUBLE_CLICK) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDragEvent(InventoryDragEvent event) {
        Player eventPlayer = (Player) event.getWhoClicked();
        ItemStack cursorItem = event.getOldCursor();

        if (!isValidTrinket(cursorItem)) {
            return;
        }

        for (int slot : event.getRawSlots()) {
            ItemStack inventoryItem = event.getView().getItem(slot);
            if (isValidTrinket(inventoryItem) && inventoryItem.isSimilar(cursorItem)) {
                event.setCancelled(true);
                break;
            }
        }
    }
}
