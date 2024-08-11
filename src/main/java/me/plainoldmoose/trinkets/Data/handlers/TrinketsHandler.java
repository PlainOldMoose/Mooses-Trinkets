package me.plainoldmoose.trinkets.Data.handlers;

import me.plainoldmoose.trinkets.Data.Trinket;
import me.plainoldmoose.trinkets.Data.TrinketManager;
import me.plainoldmoose.trinkets.Trinkets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.HashMap;
import java.util.Set;

public class TrinketsHandler {
    private static final TrinketsHandler instance = new TrinketsHandler();

    private File configFile;
    private FileConfiguration fileConfig;

    public TrinketsHandler getInstance() {
        return instance;
    }

    public void loadConfig() {
        configFile = new File(Trinkets.getInstance().getDataFolder(), "trinkets.yml");

        if (!configFile.exists()) {
            Trinkets.getInstance().saveResource("trinkets.yml", false);
        }

        fileConfig = YamlConfiguration.loadConfiguration(configFile);
        fileConfig.options().parseComments(true);
        colorizeConfig(fileConfig);

        try {
            loadTrinkets();
        } catch (Exception e) {
            Bukkit.getServer().getLogger().severe("[Mooses - Trinkets] Something went wrong when loading trinkets.yml, please check the configuration.");
        }
    }

    private void loadTrinkets() {
        TrinketManager manager= Trinkets.getInstance().getManager();
        Set<String> keySet = fileConfig.getKeys(false);

        for (String key : keySet) {
            manager.addTrinket(loadTrinket(key));
        }

    }

    private Trinket loadTrinket(String key) {
        String name = fileConfig.getString(key + ".name");
        String materialName = fileConfig.getString(key + ".material");

        ConfigurationSection statsSection = fileConfig.getConfigurationSection(key + ".stats");
        HashMap<String, Integer> statsMap = new HashMap<>();

        if (statsSection == null) {
            return null;
        }

        Set<String> statKeys = statsSection.getKeys(false);

        for (String stat : statKeys) {
            int value = statsSection.getInt(stat);
            statsMap.put(stat, value);
        }

        return new Trinket(Material.valueOf(materialName), key, name, statsMap, false);
    }

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

    /**
     * Creates an ItemStack with the specified material and name.
     *
     * @param material The material of the item.
     * @param name     The display name of the item.
     * @return The created ItemStack.
     */
    private ItemStack createItemStack(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        return item;
    }
}
