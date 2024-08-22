package me.plainoldmoose.trinkets.gui.builders;

import me.plainoldmoose.trinkets.data.loaders.config.GUILoaders.TrinketSlotsConfigLoader;
import me.plainoldmoose.trinkets.gui.components.TrinketSlot;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TrinketSlotBuilder {
    private static final Map<Integer, TrinketSlot> trinketSlotMap = new HashMap<>();

    /**
     * Creates buttons for each TrinketSlot and adds them to the button map.
     */
    public static void createSlotButtons() {
        Set<TrinketSlot> trinketSlotSet = TrinketSlotsConfigLoader.getInstance().getTrinketSlotSet();

        for (TrinketSlot ts : trinketSlotSet) {
            trinketSlotMap.put(ts.getIndex(), ts);
        }
    }

    public static Map<Integer, TrinketSlot> getTrinketSlotMap() {
        return trinketSlotMap;
    }
}
