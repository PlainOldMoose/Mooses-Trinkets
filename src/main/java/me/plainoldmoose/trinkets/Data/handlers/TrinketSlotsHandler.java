package me.plainoldmoose.trinkets.Data.handlers;

import me.plainoldmoose.trinkets.GUI.components.TrinketSlot;
import me.plainoldmoose.trinkets.trinket.TrinketType;
import me.plainoldmoose.trinkets.utils.ItemFactory;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class TrinketSlotsHandler {
    private final Set<TrinketSlot> trinketSlotSet = new HashSet<TrinketSlot>();
    private static final TrinketSlotsHandler instance = new TrinketSlotsHandler();

    public Set<TrinketSlot> getTrinketSlotSet() {
        return trinketSlotSet;
    }

    public static TrinketSlotsHandler getInstance() {
        return instance;
    }

    public void loadTrinketSlots(FileConfiguration fileConfig) {
        trinketSlotSet.clear();
        System.out.println("RELOADING >>>>>>>>>>>>>>>>>>>");
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
