package me.plainoldmoose.trinkets.Data.handlers;

import me.plainoldmoose.trinkets.GUI.components.TrinketSlot;
import me.plainoldmoose.trinkets.Trinkets;
import me.plainoldmoose.trinkets.utils.ConfigUtils;
import me.plainoldmoose.trinkets.utils.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ConfigHandler {

    private static final ConfigHandler instance = new ConfigHandler();

    public static ConfigHandler getInstance() {
        return instance;
    }

    private File configFile;
    private FileConfiguration fileConfig;

    private final Set<TrinketSlot> trinketSlotSet = new HashSet<TrinketSlot>();
    private final HashMap<String, Integer> defaultTrinketSlots = new HashMap<>();
    private Material backgroundMaterial;
    private Material secondaryBackgroundMaterial;

    /**
     * Loads the configuration file and initializes default values.
     */
    public void loadConfig() {
        configFile = new File(Trinkets.getInstance().getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            Trinkets.getInstance().saveResource("config.yml", false);
        }

        fileConfig = YamlConfiguration.loadConfiguration(configFile);
        fileConfig.options().parseComments(true);
        ConfigUtils.colorizeConfig(fileConfig);

        try {
            setUpDefaults();
            loadTrinketSlots();
            loadBackgroundMaterials();
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getServer().getLogger().severe("[Mooses-Trinkets] Something went wrong when loading config.yml, please check the configuration.");
        }
    }

    /**
     * Loads the background materials from the configuration file.
     */
    private void loadBackgroundMaterials() {
        backgroundMaterial = Material.valueOf(fileConfig.getString("background-material"));
        secondaryBackgroundMaterial = Material.valueOf(fileConfig.getString("secondary-background-material"));
    }

    /**
     * Loads a specific trinket slot from the configuration file.
     *
     * @param key The key of the trinket slot to load i.e. HEAD.
     */
    private void loadTrinketSlot(String key) {
        System.out.println(key);
        ConfigurationSection section = fileConfig.getConfigurationSection("trinket_slots." + key);

        if (section == null) {
            return;
        }

        String type = section.getString("type");
        String name = section.getString("name");
        int slot = section.getInt("slot");
        boolean isEnabled = section.getBoolean("enabled");
        Material material = Material.valueOf(section.getString("material"));
        ItemStack trinketSlotItem = ItemFactory.createItemStack(material, name + " " + type);
        TrinketSlot trinketSlot = new TrinketSlot(type, slot, trinketSlotItem, isEnabled);
        trinketSlotSet.add(trinketSlot);
    }

    /**
     * Loads all trinket slots defined in the configuration file.
     */
    private void loadTrinketSlots() {
        trinketSlotSet.clear();
        Set<String> slots = (fileConfig.getConfigurationSection("trinket_slots").getKeys(false));

        for (String s : slots) {
            loadTrinketSlot(s);
        }
//
//        SlotTypesHandler slotTypesHandler = SlotTypesHandler.getInstance();
//
//        for (String slot : slotTypesHandler.getSlotSet()) {
//            loadTrinketSlot(slot);
//        }
    }

    public Set<TrinketSlot> getTrinketSlotSet() {
        return trinketSlotSet;
    }

    /**
     * Sets up the default index for trinket slots.
     */
    private void setUpDefaults() {
        defaultTrinketSlots.put("HEAD", 13);
        defaultTrinketSlots.put("NECK", 22);
        defaultTrinketSlots.put("LEFT_ARM", 21);
        defaultTrinketSlots.put("RIGHT_ARM", 23);
        defaultTrinketSlots.put("LEG", 31);
        defaultTrinketSlots.put("FEET", 40);
    }

    /**
     * Sets the display name of an ItemMeta from the configuration file.
     *
     * @param itemMeta The ItemMeta to modify.
     */
    private void setItemMetaName(ItemMeta itemMeta, String name) {
        itemMeta.setDisplayName(name);
    }

    // Getter methods
    public Material getBackgroundMaterial() {
        return backgroundMaterial;
    }

    public Material getSecondaryBackgroundMaterial() {
        return secondaryBackgroundMaterial;
    }
}
