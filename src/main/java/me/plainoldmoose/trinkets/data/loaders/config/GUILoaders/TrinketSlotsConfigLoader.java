package me.plainoldmoose.trinkets.data.loaders.config.GUILoaders;

import me.plainoldmoose.trinkets.data.trinket.TrinketType;
import me.plainoldmoose.trinkets.gui.components.TrinketSlot;
import me.plainoldmoose.trinkets.utils.ItemFactory;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

/**
 * Handles the loading of trinket slots from a configuration file.
 * This class is responsible for parsing the configuration to create and manage
 * a set of {@link TrinketSlot} objects used in the GUI.
 */
public class TrinketSlotsConfigLoader {
    private static final TrinketSlotsConfigLoader instance = new TrinketSlotsConfigLoader();
    private final Set<TrinketSlot> trinketSlotSet = new HashSet<>();

    public static TrinketSlotsConfigLoader getInstance() {
        return instance;
    }

    public Set<TrinketSlot> getTrinketSlotSet() {
        return trinketSlotSet;
    }

    /**
     * Loads trinket slots from the specified configuration file.
     * This method reads the "trinket_slots" section of the configuration
     * and populates the set of {@link TrinketSlot} objects.
     *
     * @param fileConfig The configuration file to load from.
     */
    public void loadTrinketSlots(FileConfiguration fileConfig) {
        trinketSlotSet.clear();
        ConfigurationSection section = fileConfig.getConfigurationSection("trinket_slots");

        if (section == null) {
            return;
        }

        Set<String> slots = section.getKeys(false);

        for (String s : slots) {
            loadTrinketSlot(s, section);
        }
    }

    /**
     * Loads a single trinket slot from the configuration section.
     * This method constructs a {@link TrinketSlot} object using the data associated
     * with the provided key in the configuration section.
     *
     * @param key     The key identifying the trinket slot in the configuration.
     * @param section The configuration section containing trinket slot data.
     */
    private void loadTrinketSlot(String key, ConfigurationSection section) {
        // Construct TrinketType from YML value
        String typeString = section.getString(key + ".type");
        TrinketType type = new TrinketType(typeString);

        // Construct ItemStack from YML values
        String name = section.getString(key + ".name");
        Material material = Material.valueOf(section.getString(key + ".material"));
        ItemStack displayItem = ItemFactory.createItemStack(material, name);

        // Load inventory index from YML value
        int inventoryIndex = section.getInt(key + ".slot");

        // Create TrinketSlot
        TrinketSlot trinketSlot = new TrinketSlot(displayItem, type, inventoryIndex);

        // Enable / Disable as necessary
        boolean isEnabled = section.getBoolean(key + ".enabled");
        trinketSlot.setVisibility(isEnabled);

        trinketSlotSet.add(trinketSlot);
    }
}
