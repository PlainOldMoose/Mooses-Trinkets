package me.plainoldmoose.trinkets.data.trinket;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TrinketType implements ConfigurationSerializable {
    private final String value;

    public TrinketType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

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

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("value", value);
        return map;
    }

    public static TrinketType deserialize(Map<String, Object> map) {
        String value = (String) map.get("value");
        return new TrinketType(value);
    }
}
