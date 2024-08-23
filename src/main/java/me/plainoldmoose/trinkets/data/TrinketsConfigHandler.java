package me.plainoldmoose.trinkets.data;

import me.plainoldmoose.trinkets.Trinkets;
import me.plainoldmoose.trinkets.data.loaders.PlayerDataLoader;
import me.plainoldmoose.trinkets.data.loaders.TrinketsLoader;
import me.plainoldmoose.trinkets.data.loaders.config.GUILoaders.BackgroundItemConfigLoader;
import me.plainoldmoose.trinkets.data.loaders.config.GUILoaders.IconConfigLoader;
import me.plainoldmoose.trinkets.data.loaders.config.GUILoaders.TrinketSlotsConfigLoader;
import me.plainoldmoose.trinkets.data.loaders.config.MessageConfigLoader;
import me.plainoldmoose.trinkets.data.loaders.eco.SkillsHandler;
import me.plainoldmoose.trinkets.utils.ConfigUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Handles the configuration loading and management for the Trinkets plugin.
 * This includes loading player data, trinket definitions, and various configuration settings.
 */
public class TrinketsConfigHandler {
    private static TrinketsConfigHandler instance = new TrinketsConfigHandler();
    private FileConfiguration fileConfig = loadConfigFile();

    /**
     * Gets the singleton instance of the TrinketsConfigHandler.
     *
     * @return The singleton instance of TrinketsConfigHandler.
     */
    public static TrinketsConfigHandler getInstance() {
        return instance;
    }

    /**
     * Loads the configuration file for the plugin. If the file does not exist,
     * it will be created from the default resource.
     *
     * @return The loaded FileConfiguration.
     */
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

    /**
     * Loads all configurations and data necessary for the plugin.
     * This includes player data, skills, messages, trinkets, and GUI configurations.
     */
    public void loadConfig() {
        PlayerDataLoader.getInstance().loadData(); // Loads player data
        SkillsHandler.getInstance().loadConfig(); // Loads skills from eco
        MessageConfigLoader.getInstance().loadConfig(); // Loads message.yml
        TrinketsLoader.getInstance().loadYMLs(); // Loads all trinkets from /trinkets/
        TrinketSlotsConfigLoader.getInstance().loadTrinketSlots(fileConfig); // Load TrinketSlots from config.yml
        BackgroundItemConfigLoader.getInstance().loadBackgroundMaterials(fileConfig); // Load background materials from config.yml
        IconConfigLoader.getInstance().loadIcons(fileConfig); // Load stats icons from config.yml
    }

    /**
     * Reloads the configuration file and updates the in-memory data.
     * This method also triggers a command executor update.
     */
    public void reloadConfig() {
        loadConfigFile();
        loadConfig();
        Trinkets.getInstance().getCommandExecutor().update();
    }
}
