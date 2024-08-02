package me.plainoldmoose.trinkets.GUI;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Abstract class representing a button in a GUI.
 * Each button has a specific slot and defines behavior for when it is clicked.
 */
public abstract class Button {

    private ItemStack item;
    private final int slot;
    private boolean enabled;

    /**
     * Constructor to create a Button at a specified slot.
     *
     * @param slot      The slot number where the button will be placed.
     * @param itemStack
     */
    public Button(int slot, ItemStack itemStack) {
        this.slot = slot;
        this.item = itemStack;
        this.enabled = false;
    }

    /**
     * Gets the slot number of the button.
     *
     * @return The slot number.
     */
    public final int getSlot() {
        return slot;
    }

    /**
     * Gets the ItemStack representing the button.
     * Must be implemented by subclasses to define the button's appearance.
     *
     * @return The ItemStack representing the button.
     */
    public ItemStack getItem() {
        return this.item;
    }


    public boolean isEnabled() {
        return this.enabled;
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public void setItemStack(ItemStack item) {
        this.item = item;
    }

    /**
     * Defines the action to be taken when the button is clicked.
     * Must be implemented by subclasses to define specific click behavior.
     *
     * @param player The player who clicked the button.
     */
    public abstract void onClick(Player player);
}