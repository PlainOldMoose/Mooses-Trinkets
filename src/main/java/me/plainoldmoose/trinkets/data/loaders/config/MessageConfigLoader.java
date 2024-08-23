package me.plainoldmoose.trinkets.data.loaders.config;

import me.plainoldmoose.trinkets.Trinkets;
import me.plainoldmoose.trinkets.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

/**
 * Handles the loading of messages from the "messages.yml" configuration file.
 * This class is responsible for managing and providing access to various messages
 * used in the plugin, ensuring they are loaded and colorized appropriately.
 */
public class MessageConfigLoader {
    private static final MessageConfigLoader instance = new MessageConfigLoader();

    private File configFile;
    private FileConfiguration fileConfig;
    private final HashMap<String, String> messagesMap = new HashMap<>();

    /**
     * Loads the configuration file and initializes default values.
     * If the file does not exist, it is copied from the plugin's resources.
     * This method also handles the colorization of the messages.
     */
    public void loadConfig() {
        configFile = new File(Trinkets.getInstance().getDataFolder(), "messages.yml");

        if (!configFile.exists()) {
            Trinkets.getInstance().saveResource("messages.yml", false);
        }

        fileConfig = YamlConfiguration.loadConfiguration(configFile);
        fileConfig.options().parseComments(true);
        ConfigUtils.colorizeConfig(fileConfig);

        try {
            loadMessages();
        } catch (Exception e) {
            Bukkit.getServer().getLogger().severe("[Mooses-Trinkets] Something went wrong when loading messages.yml, please check the configuration.");
        }
    }

    /**
     * Loads messages from the configuration file into the messagesMap.
     * This method reads predefined keys from the configuration and populates the map
     * with the corresponding message strings.
     */
    private void loadMessages() {
        String[] keys = {"prefix", "invalid_trinket", "trinket_not_found", "unknown_command", "reload", "no_trinkets"};

        for (String key : keys) {
            String value = fileConfig.getString(key);
            messagesMap.put(key, value);
        }
    }

    public static MessageConfigLoader getInstance() {
        return instance;
    }

    public HashMap<String, String> getMessagesMap() {
        return messagesMap;
    }
}
