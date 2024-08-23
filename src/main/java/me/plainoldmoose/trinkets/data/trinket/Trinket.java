package me.plainoldmoose.trinkets.data.trinket;

import me.plainoldmoose.trinkets.gui.builders.IconBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a trinket in the game with its associated properties, such as the item, display name, stats, and type.
 * A trinket is an in-game item with specific attributes and metadata.
 */
public class Trinket {
    private ItemStack item;
    private final String name;
    private final String displayName;
    private final Map<String, Integer> stats;
    private final Map<String, Integer> formattedStats;
    private final TrinketType type;

    /**
     * Constructs a Trinket with the specified properties.
     *
     * @param material The material type of the trinket.
     * @param identifier The unique identifier for the trinket.
     * @param displayName The display name of the trinket.
     * @param stats A map of stat names to their values for the trinket.
     * @param formattedStats A map of formatted stat names to their values.
     * @param type The type of the trinket, indicating its usage or category.
     * @param modelID The custom model data ID for the trinket's item.
     */
    public Trinket(Material material, String identifier, String displayName, Map<String, Integer> stats, Map<String, Integer> formattedStats, TrinketType type, int modelID) {
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

    /**
     * Generates a list of lore lines for the trinket's item based on its formatted stats.
     *
     * @param formattedStats A map of formatted stat names to their values.
     * @return A list of lore lines to be displayed on the trinket's item.
     */
    private List<String> generateLore(Map<String, Integer> formattedStats) {
        List<String> lore = new ArrayList<>();
        lore.add(" ");

        formattedStats.forEach((statName, statValue) -> {
            String convertedName = IconBuilder.convertToBukkitColor(statName);
            lore.add(String.format("%s §8»§f %d", convertedName, statValue));
        });

        return lore;
    }

    /**
     * Applies metadata to the trinket's item, including display name, lore, and custom model data.
     *
     * @param displayName The display name of the trinket.
     * @param modelID The custom model data ID for the item.
     * @param lore A list of lore lines to be displayed on the item.
     */
    private void applyItemMeta(String displayName, int modelID, List<String> lore) {
        ItemMeta meta = this.item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(lore);
            meta.setCustomModelData(modelID);
            this.item.setItemMeta(meta);
        }
    }

    /**
     * Retrieves a clone of the trinket's ItemStack.
     *
     * @return A clone of the trinket's ItemStack.
     */
    public ItemStack getTrinketItem() {
        return item.clone();
    }

    /**
     * Gets the unique name identifier of the trinket.
     *
     * @return The name of the trinket.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the display name of the trinket.
     *
     * @return The display name of the trinket.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the map of stats associated with the trinket.
     *
     * @return A map of stat names to their values for the trinket.
     */
    public Map<String, Integer> getStats() {
        return stats;
    }

    /**
     * Sets the ItemStack for the trinket.
     *
     * @param item The new ItemStack for the trinket.
     */
    public void setItem(ItemStack item) {
        this.item = item;
    }

    /**
     * Gets the type of the trinket.
     *
     * @return The type of the trinket.
     */
    public TrinketType getType() {
        return type;
    }
}
