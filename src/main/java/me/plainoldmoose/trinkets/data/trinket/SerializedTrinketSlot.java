package me.plainoldmoose.trinkets.data.trinket;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a serialized trinket slot for configuration or storage purposes.
 * This class implements {@link ConfigurationSerializable} to allow serialization
 * and deserialization of trinket slot data for storage in configuration files.
 */
public class SerializedTrinketSlot implements ConfigurationSerializable {
    private final Map<String, Object> map;

    /**
     * Constructs a SerializedTrinketSlot with the specified map of data.
     *
     * @param map A map containing the serialized trinket slot data.
     */
    public SerializedTrinketSlot(Map<String, Object> map) {
        this.map = map;
    }

    /**
     * Retrieves the map of serialized trinket slot data.
     *
     * @return A map containing the serialized data of the trinket slot.
     */
    public Map<String, Object> getMap() {
        return map;
    }

    /**
     * Serializes this SerializedTrinketSlot into a map suitable for configuration storage.
     *
     * @return A map containing the serialized data of this trinket slot.
     */
    @NotNull
    @Override
    public Map<String, Object> serialize() {
        // Return the internal map, which contains the serialized data
        return new HashMap<>(map);
    }
}
