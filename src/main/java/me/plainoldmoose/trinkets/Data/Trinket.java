package me.plainoldmoose.trinkets.Data;

import me.plainoldmoose.trinkets.GUI.fetchers.PlayerStatsFetcher;
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
    private final String name;
    private final String displayName;
    private final Map<String, Integer> stats;
    private final Map<String, Integer> formattedStats;
    private final String type;
    private final int modelID;

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public String getType() {
        return type;
    }

    /**
     * Constructs a new Trinket.
     *
     * @param material The material representing the trinket item
     * @param name     The display name of the trinket
     */
    public Trinket(Material material, String name, String displayName, Map<String, Integer> stats, Map<String, Integer> formattedStats, String type, int modelID) {
        this.item = new ItemStack(material);
        this.name = name;
        this.displayName = displayName;
        this.stats = stats;
        this.formattedStats = formattedStats;
        this.type = type;
        this.modelID = modelID;

        // TODO - find a better way to display stats that trinkets give as lore
        ArrayList<String> list = new ArrayList<>();
        list.add(" ");

        for (Map.Entry<String, Integer> e : formattedStats.entrySet()) {

            String statName = (String) e.getKey();
            int statValue = (int) e.getValue();
            String convertedName = PlayerStatsFetcher.convertToBukkitColor(statName);
            list.add(convertedName + " §8»§f " + statValue);
        }

        ItemMeta meta = this.item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            ;
            meta.setLore(list);
            meta.setCustomModelData(modelID);
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

    public Map<String, Integer> getStats() {
        return stats;
    }
}
