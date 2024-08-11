package me.plainoldmoose.trinkets.Data;

import me.plainoldmoose.trinkets.Trinkets;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

/**
 * Manages the loading and storing of trinket data from a configuration file.
 */
public class TrinketsData {

    private static final TrinketsData instance = new TrinketsData();
    private final ConfigHandler configHandler = new ConfigHandler();
    private final MessageHandler messageHandler = new MessageHandler();
    private final SkillsHandler skillsHandler = new SkillsHandler();

    public SkillsHandler getSkillsHandler() {
        return skillsHandler;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public TrinketsHandler getTrinketsHandler() {
        return trinketsHandler;
    }

    private final TrinketsHandler trinketsHandler = new TrinketsHandler();

    private File configFile;
    private FileConfiguration fileConfig;

    private TrinketsData() {
        loadConfig();
    }

    public void loadConfig() {
        configHandler.loadConfig();
        trinketsHandler.loadConfig();
        messageHandler.loadConfig();
        skillsHandler.loadConfig();
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
