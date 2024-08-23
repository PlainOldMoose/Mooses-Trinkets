package me.plainoldmoose.trinkets.data.trinket;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the type of a trinket. The type is used to categorize trinkets in the game.
 * This class implements {@link ConfigurationSerializable} to allow serialization and deserialization
 * of TrinketType objects.
 */
public class TrinketType implements ConfigurationSerializable {
    private final String value;

    /**
     * Constructs a TrinketType with the specified value.
     *
     * @param value The type value of the trinket.
     */
    public TrinketType(String value) {
        this.value = value;
    }

    /**
     * Gets the value of this TrinketType.
     *
     * @return The value of the TrinketType.
     */
    public String getValue() {
        return value;
    }

    /**
     * Compares this TrinketType to another object for equality.
     *
     * @param o The object to compare with this TrinketType.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof TrinketType)) {
            return false;
        }

        TrinketType trinketObj = (TrinketType) o;
        return trinketObj.value.equals(value);
    }

    /**
     * Serializes this TrinketType into a map.
     *
     * @return A map containing the serialized representation of this TrinketType.
     */
    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("value", value);
        return map;
    }

    /**
     * Deserializes a TrinketType from the given map.
     *
     * @param map The map containing the serialized representation of a TrinketType.
     * @return A new TrinketType instance based on the data in the map.
     */
    public static TrinketType deserialize(Map<String, Object> map) {
        String value = (String) map.get("value");
        return new TrinketType(value);
    }
}
