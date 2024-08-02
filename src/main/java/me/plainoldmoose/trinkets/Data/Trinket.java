package me.plainoldmoose.trinkets.Data;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Represents a trinket in the game with an ID, an item, a name, and an enchantment status.
 */
public class Trinket {
    private final int trinketID;
    private final ItemStack item;
    private final boolean enchanted;
    private final String name;

    /**
     * Constructs a new Trinket.
     *
     * @param trinketID The unique ID of the trinket
     * @param item The ItemStack representing the trinket item
     * @param name The display name of the trinket
     * @param enchanted Whether the trinket is enchanted
     */
    public Trinket(int trinketID, ItemStack item, String name, boolean enchanted) {
        this.trinketID = trinketID;
        this.item = item.clone();
        this.name = name;
        this.enchanted = enchanted;

        ItemMeta meta = this.item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            this.item.setItemMeta(meta);
        }
    }

    public ItemStack getTrinketItem() {
        return item.clone();
    }

    public int getTrinketID() {
        return trinketID;
    }

    public String getName() {
        return name;
    }

    public boolean isEnchanted() {
        return enchanted;
    }
}
