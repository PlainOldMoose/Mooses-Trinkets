package me.plainoldmoose.trinkets.Data;

import me.plainoldmoose.trinkets.Trinkets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.HashMap;

public class ConfigHandler {

    private File configFile;
    private FileConfiguration fileConfig;

    private final HashMap<String, ItemStack> trinketSlotMap = new HashMap<>();
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
        colorizeConfig(fileConfig);

        try {
            setUpDefaults();
            loadTrinketSlots();
            loadBackgroundMaterials();
        } catch (Exception e) {
            Bukkit.getServer().getLogger().severe("Something went wrong loading YML config");
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
     * Creates an ItemStack from the configuration file.
     *
     * @param materialPath The path to the material in the configuration file.
     * @return The created ItemStack.
     */
    private ItemStack createItemStack(String materialPath) {
        Material material = Material.valueOf(fileConfig.getString(materialPath));
        return new ItemStack(material);
    }

    /**
     * Loads a specific trinket slot from the configuration file.
     *
     * @param key The key of the trinket slot to load i.e. HEAD.
     */
    private void loadTrinketSlot(String key) {
        String materialPath = key + ".material";
        String namePath = key + ".name";

        ItemStack trinketSlotItem = createItemStack(materialPath);
        ItemMeta trinketSlotMeta = trinketSlotItem.getItemMeta();

        setItemMetaName(trinketSlotMeta, namePath);
        setPersistentData(trinketSlotMeta, key);

        trinketSlotItem.setItemMeta(trinketSlotMeta);
        trinketSlotMap.put(key, trinketSlotItem);
    }

    /**
     * Loads all trinket slots defined in the configuration file.
     */
    private void loadTrinketSlots() {
        trinketSlotMap.clear();
        loadTrinketSlot("HEAD");
        loadTrinketSlot("NECK");
        loadTrinketSlot("LEFT_ARM");
        loadTrinketSlot("RIGHT_ARM");
        loadTrinketSlot("LEG");
        loadTrinketSlot("FEET");
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
     * @param namePath The path to the display name in the configuration file.
     */
    private void setItemMetaName(ItemMeta itemMeta, String namePath) {
        itemMeta.setDisplayName(fileConfig.getString(namePath));
    }

    /**
     * Sets persistent data on an ItemMeta.
     *
     * @param itemMeta The ItemMeta to modify.
     * @param key      The key of the trinket slot.
     */
    private void setPersistentData(ItemMeta itemMeta, String key) {
        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        NamespacedKey enabledKey = new NamespacedKey(Trinkets.getInstance(), "enabled");
        NamespacedKey slotKeyNamespace = new NamespacedKey(Trinkets.getInstance(), "slot");

        boolean isEnabled = fileConfig.getBoolean(key + ".enabled", true);
        dataContainer.set(enabledKey, PersistentDataType.INTEGER, isEnabled ? 1 : 0);

        int slot = parseSlot(key);
        dataContainer.set(slotKeyNamespace, PersistentDataType.INTEGER, slot);
    }

    /**
     * Parses the slot index from the configuration file.
     *
     * @param key The key of the trinket slot.
     * @return The slot index.
     */
    private int parseSlot(String key) {
        String slotAsString = fileConfig.getString(key + ".slot");
        try {
            return Integer.parseInt(slotAsString);
        } catch (NumberFormatException e) {
            return defaultTrinketSlots.get(key);
        }
    }

    /**
     * Colorsize the configuration file's values.
     *
     * @param config The configuration file to colorize.
     */
    public static void colorizeConfig(FileConfiguration config) {
        colorizeSection(config);
    }

    private static void colorizeSection(ConfigurationSection section) {
        for (String key : section.getKeys(false)) {
            Object value = section.get(key);
            if (value instanceof String) {
                section.set(key, ChatColor.translateAlternateColorCodes('&', (String) value));
            } else if (value instanceof ConfigurationSection) {
                colorizeSection((ConfigurationSection) value);
            }
        }
    }

    // Getter methods
    public Material getBackgroundMaterial() {
        return backgroundMaterial;
    }

    public Material getSecondaryBackgroundMaterial() {
        return secondaryBackgroundMaterial;
    }

    public HashMap<String, ItemStack> getTrinketIndexMap() {
        return trinketSlotMap;
    }
}
