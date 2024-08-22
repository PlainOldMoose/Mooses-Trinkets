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
 * Manages the loading and storing of trinket data from a configuration file.
 */
public class TrinketsConfigHandler {
    private static TrinketsConfigHandler instance = new TrinketsConfigHandler();
    private FileConfiguration fileConfig = loadConfigFile();

    public static TrinketsConfigHandler getInstance() {
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
     */
    public void reloadConfig() {
        loadConfigFile();
        loadConfig();
        Trinkets.getInstance().getCommandExecutor().update();
    }
}
