package me.plainoldmoose.trinkets.GUI.components;

import org.bukkit.inventory.ItemStack;

/**
 * Represents a background item in a GUI with a specific slot.
 */
public abstract class Background {
    private ItemStack item;
    private final int slot;

    /**
     * Constructs a new Background.
     *
     * @param item The ItemStack representing the background item
     * @param slot The slot where the background item is placed
     */
    public Background(ItemStack item, int slot) {
        this.item = item.clone();
        this.slot = slot;
    }

    public ItemStack getItem() {
        return item.clone();
    }

    public void setItem(ItemStack item) {
        this.item = item.clone();
    }

    public int getSlot() {
        return slot;
    }
}
