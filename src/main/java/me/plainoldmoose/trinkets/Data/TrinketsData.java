package me.plainoldmoose.trinkets.Data;

import me.plainoldmoose.trinkets.Trinkets;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
            loadTrinketSlots();
            loadMessages();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load YAML configuration.");
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
        loadHeadTrinketSlot();
    }

    public HashMap<String, ItemStack> getTrinketSlotMap() {
        return trinketSlotMap;
    }


    // TODO - finish rest of the slots, potentially extract to one method
    private void loadHeadTrinketSlot() {
        ItemStack headTrinketSlot = new ItemStack(Material.valueOf(fileConfig.getString("HEAD_SLOT.material")));
        ItemMeta headTrinketSlotMeta = headTrinketSlot.getItemMeta();
        headTrinketSlotMeta.setDisplayName(fileConfig.getString("HEAD_SLOT.name"));

        // Cheap way to pass on a boolean for rendering - if display name is "disabled" it will not render in GUI
        if (fileConfig.getString("HEAD_SLOT.enabled").equalsIgnoreCase("false")) {
            headTrinketSlotMeta.setDisplayName("disabled");
        }

        headTrinketSlot.setItemMeta(headTrinketSlotMeta);
        trinketSlotMap.put("HEAD_SLOT", headTrinketSlot);
    }


    public static TrinketsData getInstance() {
        return instance;
    }
}