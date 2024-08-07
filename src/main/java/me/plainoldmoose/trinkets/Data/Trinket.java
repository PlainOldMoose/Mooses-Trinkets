package me.plainoldmoose.trinkets.Data;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Represents a trinket in the game with an ID, an item, a name, and an enchantment status.
 */
public class Trinket {
    private final ItemStack item;
    private final boolean enchanted;
    private final String name;
    private final String displayName;

    /**
     * Constructs a new Trinket.
     *
     * @param item The ItemStack representing the trinket item
     * @param name The display name of the trinket
     * @param enchanted Whether the trinket is enchanted
     */
    public Trinket(ItemStack item, String name, String displayName, boolean enchanted) {
        this.item = item.clone();
        this.name = name;
        this.enchanted = enchanted;
        this.displayName = displayName;

        ItemMeta meta = this.item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            this.item.setItemMeta(meta);
        }
    }

    public ItemStack getTrinketItem() {
        return item.clone();
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isEnchanted() {
        return enchanted;
    }
}
