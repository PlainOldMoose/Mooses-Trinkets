package me.plainoldmoose.trinkets.gui.components;

import me.plainoldmoose.trinkets.data.trinket.TrinketType;
import me.plainoldmoose.trinkets.gui.interactions.TrinketInteractionHandler;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a slot in a GUI that can contain a trinket.
 * This class handles the interaction with the trinket slot and manages
 * the contained trinket. It also supports serialization and deserialization
 * for configuration purposes.
 */
public class TrinketSlot extends Button implements ConfigurationSerializable {
    private ItemStack containedTrinket;
    private final TrinketType type;

    /**
     * Constructs a new TrinketSlot with the specified display item, trinket type, and inventory index.
     *
     * @param displayItem The {@link ItemStack} to be displayed in the trinket slot.
     * @param type The {@link TrinketType} associated with this slot.
     * @param inventoryIndex The slot index in the inventory where the trinket slot is placed.
     */
    public TrinketSlot(ItemStack displayItem, TrinketType type, int inventoryIndex) {
        super(displayItem, inventoryIndex);
        this.type = type;
    }

    /**
     * Defines the action to be taken when the trinket slot is clicked.
     * This method delegates the handling of the click to {@link TrinketInteractionHandler}.
     *
     * @param player The {@link Player} who clicked the trinket slot.
     */
    @Override
    public void onClick(Player player) {
        TrinketInteractionHandler.handleButtonClick(player, this);
    }

    /**
     * Pushes a trinket into this slot.
     *
     * @param trinket The {@link ItemStack} representing the trinket to be added to the slot.
     */
    public void push(ItemStack trinket) {
        containedTrinket = trinket;
    }

    /**
     * Pops the trinket out of this slot.
     *
     * @return The {@link ItemStack} representing the trinket that was in the slot, or an air item if the slot was empty.
     */
    public ItemStack pop() {
        if (containedTrinket == null) {
            return new ItemStack(Material.AIR);
        }
        ItemStack item = containedTrinket;
        containedTrinket = null;
        return item;
    }

    /**
     * Gets the trinket currently contained in this slot.
     *
     * @return The {@link ItemStack} representing the contained trinket, or null if the slot is empty.
     */
    public ItemStack getContainedTrinket() {
        return containedTrinket;
    }

    /**
     * Gets the {@link TrinketType} associated with this slot.
     *
     * @return The {@link TrinketType} for this slot.
     */
    public TrinketType getType() {
        return type;
    }

    /**
     * Serializes this TrinketSlot into a map for configuration storage.
     * This map includes the contained trinket, display item, index, and trinket type.
     *
     * @return A map representing the serialized state of this TrinketSlot.
     */
    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("containedTrinket", containedTrinket); // Ensure containedTrinket is serializable
        map.put("displayItem", this.getDisplayItem()); // Ensure this.getDisplayItem() is serializable
        map.put("index", this.getIndex());
        map.put("type", type.serialize()); // Ensure type is serializable, or convert it to a serializable format
        return map;
    }

    /**
     * Deserializes a TrinketSlot from a map.
     * The map should include the contained trinket, display item, index, and trinket type.
     *
     * @param map The map containing the serialized state of a TrinketSlot.
     * @return A new TrinketSlot instance created from the map data.
     */
    public static TrinketSlot deserialize(Map<String, Object> map) {
        ItemStack containedTrinket = (ItemStack) map.get("containedTrinket");
        TrinketType type = TrinketType.deserialize((Map<String, Object>) map.get("type")); // Ensure that type is correctly cast
        int index = (int) map.get("index");
        ItemStack displayItem = (ItemStack) map.get("displayItem"); // Use the correct key here

        TrinketSlot newSlot = new TrinketSlot(displayItem, type, index);
        newSlot.push(containedTrinket);
        return newSlot;
    }
}
