package me.plainoldmoose.trinkets.gui.builders;

import me.plainoldmoose.trinkets.data.loaders.config.gui.TrinketSlotsConfigLoader;
import me.plainoldmoose.trinkets.gui.components.TrinketSlot;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Manages the creation and storage of TrinketSlot buttons for the GUI.
 * This class handles the setup of TrinketSlot buttons based on configuration settings and maintains a map of these buttons for easy access.
 */
public class TrinketSlotBuilder {
    private static final Map<Integer, TrinketSlot> trinketSlotMap = new HashMap<>();

    /**
     * Creates TrinketSlot buttons based on configuration settings and adds them to the internal map.
     * This method retrieves TrinketSlot configurations, initializes them, and stores them in a map where the key is the slot index.
     */
    public static void createSlotButtons() {
        trinketSlotMap.clear();
        Set<TrinketSlot> trinketSlotSet = TrinketSlotsConfigLoader.getInstance().getTrinketSlotSet();

        for (TrinketSlot ts : trinketSlotSet) {
            ts.pop(); // Reset the contained item when constructing
            trinketSlotMap.put(ts.getIndex(), ts);
        }
    }

    /**
     * Retrieves the map of TrinketSlot buttons.
     * The map associates each TrinketSlot index with its corresponding TrinketSlot object.
     *
     * @return A map where the key is the TrinketSlot index and the value is the TrinketSlot object.
     */
    public static Map<Integer, TrinketSlot> getTrinketSlotMap() {
        return trinketSlotMap;
    }
}
