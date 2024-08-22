package me.plainoldmoose.trinkets.GUI.components;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class Button {
    private final int inventoryIndex;
    private boolean enabled;
    private ItemStack displayItem;

    /**
     * Defines the action to be taken when the button is clicked.
     * Must be implemented by subclasses to define specific click behavior.
     *
     * @param player The player who clicked the button.
     */
    public abstract void onClick(Player player);

    public Button(ItemStack displayItem, int inventoryIndex) {
        this.inventoryIndex = inventoryIndex;
        this.displayItem = displayItem;
        this.enabled = true;
    }

    public ItemStack getDisplayItem() {
        return displayItem;
    }

    public void setDisplayItem(ItemStack item) {
        this.displayItem = item;
    }

    public int getIndex() {
        return inventoryIndex;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setVisibility(boolean visible) {
        this.enabled = visible;
    }
}
