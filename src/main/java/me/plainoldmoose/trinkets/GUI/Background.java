package me.plainoldmoose.moosestrinkets.GUI;

import org.bukkit.inventory.ItemStack;

public abstract class Background {
    private ItemStack item;
    private final int slot;

    public Background(ItemStack item, int slot) {
        this.item = item;
        this.slot = slot;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public int getSlot() {
        return slot;
    }
}