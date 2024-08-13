package me.plainoldmoose.trinkets.Data;

import me.plainoldmoose.trinkets.Data.handlers.*;
import me.plainoldmoose.trinkets.Trinkets;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

/**
 * Manages the loading and storing of trinket data from a configuration file.
 */
public class TrinketsData {
    private static TrinketsData instance = new TrinketsData();

    private File configFile;
    private FileConfiguration fileConfig;

    public void loadConfig() {
        SlotTypesHandler.getInstance().loadConfig();
        ConfigHandler.getInstance().loadConfig();
        SkillsHandler.getInstance().loadConfig();
        TrinketsHandler.getInstance().loadConfig();
        MessageHandler.getInstance().loadConfig();
        IconHandler.getInstance().loadConfig();
        DataHandler.getInstance().loadData();
    }

    /**
     * Reloads the configuration file and updates the in-memory data.
     */
    public void reloadConfig() {
        loadConfig();
        Trinkets.getInstance().getCommandExecutor().update();
    }

    public static TrinketsData getInstance() {
        return instance;
    }
}
