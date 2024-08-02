package me.plainoldmoose.moosestrinkets.Data;

import org.bukkit.inventory.ItemStack;

public class Trinket {
    private int trinketID;
    private ItemStack item;
    private boolean enchanted = false;

    public Trinket(int trinketID, ItemStack item, boolean enchanted) {
        this.trinketID = trinketID;
        this.item = item;
        this.enchanted = enchanted;
    }

    public ItemStack getTrinketItem() {
        return item;
    }

    public int getTrinketID() {
        return this.trinketID;
    }
}