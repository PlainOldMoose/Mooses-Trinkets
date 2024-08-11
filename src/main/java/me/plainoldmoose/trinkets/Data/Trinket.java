package me.plainoldmoose.trinkets.Data;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Map;

/**
 * Represents a trinket in the game with an ID, an item, a name, and an enchantment status.
 */
public class Trinket {
    private ItemStack item;
    private final boolean enchanted;
    private final String name;
    private final String displayName;
    private final Map<String, Integer> stats;

    public void setItem(ItemStack item) {
        this.item = item;
    }

    /**
     * Constructs a new Trinket.
     *
     * @param material The material representing the trinket item
     * @param name The display name of the trinket
     * @param enchanted Whether the trinket is enchanted
     */
    public Trinket(Material material, String name, String displayName, Map<String, Integer> stats, boolean enchanted) {
        this.item = new ItemStack(material);
        this.name = name;
        this.enchanted = enchanted;
        this.displayName = displayName;
        this.stats = stats;


        ArrayList<String> list = new ArrayList<>();

        for (Map.Entry e : stats.entrySet()) {
            list.add((String) e.getKey());
        }

        ItemMeta meta = this.item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);;
            meta.setLore(list);
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

    public Map<String, Integer> getStats() {
        return stats;
    }
}
