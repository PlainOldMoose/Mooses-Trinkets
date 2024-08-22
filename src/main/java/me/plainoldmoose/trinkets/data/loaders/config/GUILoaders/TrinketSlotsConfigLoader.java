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

public class TrinketSlotsConfigLoader {
    private static final TrinketSlotsConfigLoader instance = new TrinketSlotsConfigLoader();
    private final Set<TrinketSlot> trinketSlotSet = new HashSet<TrinketSlot>();

    public static TrinketSlotsConfigLoader getInstance() {
        return instance;
    }

    public Set<TrinketSlot> getTrinketSlotSet() {
        return trinketSlotSet;
    }

    public void loadTrinketSlots(FileConfiguration fileConfig) {
        trinketSlotSet.clear();
        ConfigurationSection section = fileConfig.getConfigurationSection("trinket_slots");

        if (section == null) {
            return;
        }

        Set<String> slots = (section.getKeys(false));

        for (String s : slots) {
            loadTrinketSlot(s, section);
        }
    }

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
