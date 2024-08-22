package me.plainoldmoose.trinkets.data.trinket;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SerializedTrinketSlot implements ConfigurationSerializable {
    private final Map<String, Object> map;

    public SerializedTrinketSlot(Map<String, Object> map) {
        this.map = map;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        // Return the internal map, which contains the serialized data
        return new HashMap<>(map);
    }
}
