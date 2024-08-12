package me.plainoldmoose.trinkets.Data.handlers;

import me.plainoldmoose.trinkets.Data.TrinketsData;
import me.plainoldmoose.trinkets.GUI.components.TrinketSlot;
import me.plainoldmoose.trinkets.Trinkets;
import me.plainoldmoose.trinkets.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ConfigHandler {

    private File configFile;
    private FileConfiguration fileConfig;

    private final HashMap<String, ItemStack> trinketSlotMap = new HashMap<>();
    private final Set<TrinketSlot> trinketSlotSet = new HashSet<TrinketSlot>();
    private final HashMap<String, Integer> defaultTrinketSlots = new HashMap<>();
    private Material backgroundMaterial;
    private Material secondaryBackgroundMaterial;

    public HashMap<String, ItemStack> getTrinketSlotMap() {
        return trinketSlotMap;
    }

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
            Bukkit.getServer().getLogger().severe("[Mooses - Trinkets] Something went wrong when loading config.yml, please check the configuration.");
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
        if (!fileConfig.getKeys(false).contains(key)) {
            return;
        }

        String materialPath = key + ".material";
        String namePath = key + ".name";
        String slotPath = key + ".slot";
        String isEnabledPath = key + ".enabled";

        String name = fileConfig.getString(namePath);
        ItemStack trinketSlotItem = ConfigUtils.createItemStack(fileConfig, materialPath);
        ItemMeta trinketSlotMeta = trinketSlotItem.getItemMeta();
        setItemMetaName(trinketSlotMeta, namePath);
        trinketSlotItem.setItemMeta(trinketSlotMeta);
        System.out.println("Setting name to > " + trinketSlotItem.getItemMeta().getDisplayName());
        int slot = fileConfig.getInt(slotPath);
        boolean isEnabled = fileConfig.getBoolean(isEnabledPath);

        TrinketSlot trinketSlot = new TrinketSlot(namePath, slot, trinketSlotItem, isEnabled);
        trinketSlotSet.add(trinketSlot);

//        ItemStack trinketSlotItem = ConfigUtils.createItemStack(fileConfig, materialPath);

//        setPersistentData(trinketSlotMeta, key);
//
//        trinketSlotItem.setItemMeta(trinketSlotMeta);
//        trinketSlotMap.put(key, trinketSlotItem);
    }

    public Set<TrinketSlot> getTrinketSlotSet() {
        return trinketSlotSet;
    }

    /**
     * Loads all trinket slots defined in the configuration file.
     */
    private void loadTrinketSlots() {
        trinketSlotSet.clear();
        TrinketsData data = TrinketsData.getInstance();
        if (data == null) {
            return;
        }

        SlotTypesHandler slotTypesHandler = data.getSlotsHandler();
        System.out.println(slotTypesHandler.getSlotSet().isEmpty());
        trinketSlotMap.clear();
        for (String slot : slotTypesHandler.getSlotSet()) {
            loadTrinketSlot(slot);
            System.out.println("Loaded full slot info > " + slot);
        }
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
