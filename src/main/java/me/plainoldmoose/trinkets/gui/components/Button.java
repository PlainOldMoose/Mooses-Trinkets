package me.plainoldmoose.trinkets.gui.components;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a clickable button in a GUI with an associated inventory slot and item display.
 * This abstract class defines the structure for GUI buttons and requires subclasses to specify
 * the action to be taken when the button is clicked.
 */
public abstract class Button {
    private final int inventoryIndex;
    private boolean enabled;
    private ItemStack displayItem;

    /**
     * Constructs a new Button instance with the specified item and inventory index.
     *
     * @param displayItem The {@link ItemStack} to be displayed as the button.
     * @param inventoryIndex The slot index in the inventory where the button is placed.
     */
    public Button(ItemStack displayItem, int inventoryIndex) {
        this.inventoryIndex = inventoryIndex;
        this.displayItem = displayItem;
        this.enabled = true;
    }

    /**
     * Defines the action to be taken when the button is clicked.
     * This method must be implemented by subclasses to define specific click behavior.
     *
     * @param player The {@link Player} who clicked the button.
     */
    public abstract void onClick(Player player);

    /**
     * Gets the {@link ItemStack} that is displayed as the button.
     *
     * @return The display item of the button.
     */
    public ItemStack getDisplayItem() {
        return displayItem;
    }

    /**
     * Sets the {@link ItemStack} to be displayed as the button.
     *
     * @param item The new display item for the button.
     */
    public void setDisplayItem(ItemStack item) {
        this.displayItem = item;
    }

    /**
     * Gets the slot index in the inventory where the button is placed.
     *
     * @return The inventory slot index of the button.
     */
    public int getIndex() {
        return inventoryIndex;
    }

    /**
     * Checks whether the button is enabled and visible in the GUI.
     *
     * @return {@code true} if the button is enabled, {@code false} otherwise.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the visibility of the button.
     *
     * @param visible {@code true} to make the button visible, {@code false} to hide it.
     */
    public void setVisibility(boolean visible) {
        this.enabled = visible;
    }
}
