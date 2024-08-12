package me.plainoldmoose.trinkets.GUI.components;

import me.plainoldmoose.trinkets.Data.TrinketsData;
import me.plainoldmoose.trinkets.Trinkets;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;

/**
 * Represents different trinket slots in the GUI.
 * Each enum constant corresponds to a specific trinket slot in the inventory.
 */
public enum TrinketSlot {
    HEAD("HEAD"),
    NECK("NECK"),
    LEFT_ARM("LEFT_ARM"),
    RIGHT_ARM("RIGHT_ARM"),
    LEG("LEG"),
    FEET("FEET");

    private final String key;
    private int slot;
    private ItemStack background;
    private boolean enabled;

    /**
     * Constructs a TrinketSlot with the specified key.
     *
     * @param key the key associated with the trinket slot
     */
    TrinketSlot(String key) {
        this.key = key;
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

    /**
     * Loads the trinket slot configuration from the data source.
     * This method initializes the slot index, background item, and enabled status
     * based on the current configuration.
     */
    public void load() {
        HashMap<String, ItemStack> trinketIndexMap = TrinketsData.getInstance().getConfigHandler().getTrinketIndexMap();
        ItemStack bg = trinketIndexMap.get(key);
        this.slot = getTrinketSlotIndex(bg);
        this.background = bg;
        this.enabled = isTrinketSlotEnabled(bg);
    }

    /**
     * Determines if the given {@link ItemStack} represents an enabled trinket slot.
     *
     * @param item the ItemStack to check
     * @return true if the trinket slot is enabled, false otherwise
     */
    private boolean isTrinketSlotEnabled(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        NamespacedKey enabledKey = new NamespacedKey(Trinkets.getInstance(), "enabled");
        return dataContainer.has(enabledKey, PersistentDataType.INTEGER) && dataContainer.get(enabledKey, PersistentDataType.INTEGER) == 1;
    }

    /**
     * Retrieves the trinket slot index from the given {@link ItemStack}.
     *
     * @param item the ItemStack to retrieve the slot index from
     * @return the trinket slot index
     */
    private int getTrinketSlotIndex(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        NamespacedKey slotKeyNamespace = new NamespacedKey(Trinkets.getInstance(), "slot");
        return dataContainer.get(slotKeyNamespace, PersistentDataType.INTEGER);
    }

    /**
     * Loads all TrinketSlots, initializing their state.
     */
    public static void loadTrinketSlots() {
        for (TrinketSlot slot : TrinketSlot.values()) {
            slot.load();
        }
    }
}
