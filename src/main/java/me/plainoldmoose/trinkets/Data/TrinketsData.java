package me.plainoldmoose.trinkets.Data;

import me.plainoldmoose.trinkets.Data.handlers.*;
import me.plainoldmoose.trinkets.Trinkets;
import me.plainoldmoose.trinkets.utils.ConfigUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Manages the loading and storing of trinket data from a configuration file.
 */
public class TrinketsData {
    private static TrinketsData instance = new TrinketsData();
    private FileConfiguration fileConfig = loadConfigFile();

    public static TrinketsData getInstance() {
        return instance;
    }

    private FileConfiguration loadConfigFile() {
        File configFile = new File(Trinkets.getInstance().getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            Trinkets.getInstance().saveResource("config.yml", false);
        }

        fileConfig = YamlConfiguration.loadConfiguration(configFile);
        fileConfig.options().parseComments(true);
        ConfigUtils.colorizeConfig(fileConfig);
        return fileConfig;
    }

    public void loadConfig() {
        DataHandler.getInstance().loadData(); // Loads player data
        SkillsHandler.getInstance().loadConfig(); // Loads skills from eco
        MessageHandler.getInstance().loadConfig(); // Loads message.yml
        TrinketsHandler.getInstance().loadYMLs(); // Loads all trinkets from /trinkets/
        TrinketSlotsHandler.getInstance().loadTrinketSlots(fileConfig); // Load TrinketSlots from config.yml
        GUIBackgroundHandler.getInstance().loadBackgroundMaterials(fileConfig); // Load background materials from config.yml
        IconHandler.getInstance().loadIcons(fileConfig); // Load stats icons from config.yml
    }

    /**
     * Reloads the configuration file and updates the in-memory data.
     */
    public void reloadConfig() {
        loadConfig();
        Trinkets.getInstance().getCommandExecutor().update();
    }
}
