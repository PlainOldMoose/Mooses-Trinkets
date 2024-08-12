package me.plainoldmoose.trinkets.GUI.components;

import org.bukkit.inventory.ItemStack;

/**
 * Represents different trinket slots in the GUI.
 * Each enum constant corresponds to a specific trinket slot in the inventory.
 */
public class TrinketSlot {
    private final String key;
    private int slot;
    private ItemStack background;
    private boolean enabled;

    public String getKey() {
        return key;
    }

    /**
     * Constructs a TrinketSlot with the specified key.
     *
     * @param key the key associated with the trinket slot
     */
    public TrinketSlot(String key, int slot, ItemStack background, boolean enabled) {
        this.key = key;
        this.slot = slot;
        this.background = background;
        this.enabled = enabled;
    }

    /**
     * Gets the inventory slot index for this trinket slot.
     *
     * @return the inventory slot index
     */
    public int getSlot() {
        return slot;
    }

    /**
     * Gets the background {@link ItemStack} for this trinket slot.
     *
     * @return the background ItemStack
     */
    public ItemStack getBackground() {
        return background;
    }

    /**
     * Checks if this trinket slot is enabled.
     *
     * @return true if the trinket slot is enabled, false otherwise
     */
    public boolean isEnabled() {
        return enabled;
    }
}
