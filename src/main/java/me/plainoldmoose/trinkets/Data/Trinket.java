package me.plainoldmoose.trinkets.Data;

import me.plainoldmoose.trinkets.GUI.fetchers.PlayerStatsFetcher;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
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

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public String getType() {
        return type;
    }

    public Trinket(Material material, String identifier, String displayName, Map<String, Integer> stats, Map<String, Integer> formattedStats, String type, int modelID) {
        this.item = new ItemStack(material);
        this.name = identifier;
        this.displayName = displayName;
        this.stats = stats;
        this.formattedStats = formattedStats;
        this.type = type;

        // Generate the lore based on formatted stats
        List<String> lore = generateLore(formattedStats);

        // Apply metadata to the item
        applyItemMeta(displayName, modelID, lore);
    }

    private List<String> generateLore(Map<String, Integer> formattedStats) {
        List<String> lore = new ArrayList<>();
        lore.add(" ");

        formattedStats.forEach((statName, statValue) -> {
            String convertedName = PlayerStatsFetcher.convertToBukkitColor(statName);
            lore.add(String.format("%s §8»§f %d", convertedName, statValue));
        });

        return lore;
    }

    private void applyItemMeta(String displayName, int modelID, List<String> lore) {
        ItemMeta meta = this.item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(lore);
            meta.setCustomModelData(modelID);
            this.item.setItemMeta(meta);
        }
    }

    public ItemStack getTrinketItem() {
        return item.clone();
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Map<String, Integer> getStats() {
        return stats;
    }
}
