package me.plainoldmoose.trinkets.Data;

import me.plainoldmoose.trinkets.Trinkets;
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

/**
 * Manages the loading and storing of trinket data from a configuration file.
 */
public class TrinketsData {
    private static final TrinketsData instance = new TrinketsData();

    private File configFile;
    private FileConfiguration fileConfig;

    private final HashMap<String, String> messagesMap = new HashMap<>();
    private final HashMap<String, ItemStack> trinketSlotMap = new HashMap<>();

    private final HashMap<String, Integer> defaultTrinketSlots = new HashMap<String, Integer>();

    private TrinketsData() {
        loadConfig();
    }

    /**
     * Main load method, calls sub-methods to load individual components of the configuration.
     */
    public void loadConfig() {
        configFile = new File(Trinkets.getInstance().getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            Trinkets.getInstance().saveResource("config.yml", false);
        }

        fileConfig = YamlConfiguration.loadConfiguration(configFile);
        fileConfig.options().parseComments(true);

        // TODO - setup proper error logging for this
        try {
            setUpDefaults();
            loadTrinketSlots();
            loadMessages();
        } catch (Exception e) {
            Bukkit.getServer().getLogger().severe("Something went wrong loading YML config");
        }
    }

    public void reloadConfig() {
        trinketSlotMap.clear();
        messagesMap.clear();
        loadConfig();
        Trinkets.getInstance().getCommandExecutor().update();
    }


    private void loadMessages() {
        String prefix = fileConfig.getString("prefix");
        messagesMap.put("prefix", prefix);
    }

    public HashMap<String, String> getMessagesMap() {
        return messagesMap;
    }

    private void loadTrinketSlots() {
        loadTrinketSlot("HEAD");
        loadTrinketSlot("NECK");
        loadTrinketSlot("LEFT_ARM");
        loadTrinketSlot("RIGHT_ARM");
        loadTrinketSlot("LEG");
        loadTrinketSlot("FEET");
    }

    public HashMap<String, ItemStack> getTrinketSlotMap() {
        return trinketSlotMap;
    }

    private void setUpDefaults() {
        defaultTrinketSlots.put("HEAD", 13);
        defaultTrinketSlots.put("NECK", 22);
        defaultTrinketSlots.put("LEFT_ARM", 21);
        defaultTrinketSlots.put("RIGHT_ARM", 23);
        defaultTrinketSlots.put("LEG", 31);
        defaultTrinketSlots.put("FEET", 40);
    }

    private void loadTrinketSlot(String slotKey) {
        String materialPath = slotKey + ".material";
        String namePath = slotKey + ".name";
        String enabledPath = slotKey + ".enabled";
        String slotPath = slotKey + ".slot";

        ItemStack trinketSlot = new ItemStack(Material.valueOf(fileConfig.getString(materialPath)));
        ItemMeta trinketSlotMeta = trinketSlot.getItemMeta();
        trinketSlotMeta.setDisplayName(fileConfig.getString(namePath));

        // Use PersistentDataContainer to store the enabled state
        PersistentDataContainer dataContainer = trinketSlotMeta.getPersistentDataContainer();
        NamespacedKey enabledKey = new NamespacedKey(Trinkets.getInstance(), "enabled");
        NamespacedKey slotKeyNamespace = new NamespacedKey(Trinkets.getInstance(), "slot");

        boolean isEnabled = fileConfig.getBoolean(enabledPath, true);
        dataContainer.set(enabledKey, PersistentDataType.INTEGER, isEnabled ? 1 : 0);

        int slot;
        try {
            String slotAsString = fileConfig.getString(slotPath);
            slot = Integer.parseInt(slotAsString);
        } catch (NumberFormatException e) {
            slot = defaultTrinketSlots.get(slotKey);
        }

        dataContainer.set(slotKeyNamespace, PersistentDataType.INTEGER, slot);

        trinketSlot.setItemMeta(trinketSlotMeta);
        trinketSlotMap.put(slotKey, trinketSlot);
    }

    public static TrinketsData getInstance() {
        return instance;
    }
}