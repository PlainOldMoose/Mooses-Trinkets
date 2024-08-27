package me.plainoldmoose.trinkets.gui.components;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a stats icon button in a GUI that displays player statistics.
 * This button includes a list of raw stat names and can update its display
 * item lore to show formatted statistics.
 */
public class StatsIcon extends Button implements Cloneable{
    private List<String> rawStatNames = new ArrayList<>();

    /**
     * Constructs a new StatsIcon instance with the specified item, index, and list of raw stat names.
     *
     * @param displayItem The {@link ItemStack} to be displayed as the stats icon.
     * @param index The slot index in the inventory where the stats icon is placed.
     * @param rawStatNames The list of raw stat names to be displayed on the stats icon.
     */
    public StatsIcon(ItemStack displayItem, int index, List<String> rawStatNames) {
        super(displayItem, index);
        this.rawStatNames = rawStatNames;
    }

    /**
     * Gets the list of statistics currently displayed in the item's lore.
     *
     * @return The list of statistics displayed on the stats icon.
     */
    public List<String> getStatsList() {
        ItemMeta meta = this.getDisplayItem().getItemMeta();
        return meta != null ? meta.getLore() : new ArrayList<>();
    }

    /**
     * Sets the list of statistics to be displayed in the item's lore.
     *
     * @param statsList The list of statistics to display on the stats icon.
     */
    public void setListOfStats(List<String> statsList) {
        ItemMeta meta = this.getDisplayItem().getItemMeta();
        if (meta != null) {
            meta.setLore(statsList);
            this.getDisplayItem().setItemMeta(meta);
        }
    }

    /**
     * Gets the list of raw stat names associated with this stats icon.
     *
     * @return The list of raw stat names.
     */
    public List<String> getRawStatNames() {
        return rawStatNames;
    }

    /**
     * Defines the action to be taken when the stats icon is clicked.
     * This method can be overridden by subclasses to define specific click behavior.
     *
     * @param player The {@link Player} who clicked the stats icon.
     */
    @Override
    public void onClick(Player player) {
        // Define specific behavior on click if needed
    }

    @Override
    public StatsIcon clone() {
        try {
            StatsIcon cloned = (StatsIcon) super.clone(); // Perform a shallow copy

            // Deep copy the mutable fields
            cloned.rawStatNames = new ArrayList<>(this.rawStatNames);

            // Create a new ItemStack for displayItem to avoid shared references
            ItemStack originalItem = this.getDisplayItem();
            ItemStack clonedItem = originalItem.clone();
            cloned.setDisplayItem(clonedItem);

            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Can't happen, since we are Cloneable
        }
    }

}
