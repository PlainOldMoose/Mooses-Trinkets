package me.plainoldmoose.trinkets.data.trinket;

public class TrinketType {
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
}
