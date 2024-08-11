package me.plainoldmoose.trinkets.Data;

import me.plainoldmoose.trinkets.Trinkets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class MessageHandler {
    private File configFile;
    private FileConfiguration fileConfig;
    private final HashMap<String, String> messagesMap = new HashMap<>();

    /**
     * Loads the configuration file and initializes default values.
     */
    public void loadConfig() {
        configFile = new File(Trinkets.getInstance().getDataFolder(), "messages.yml");

        if (!configFile.exists()) {
            Trinkets.getInstance().saveResource("messages.yml", false);
        }

        fileConfig = YamlConfiguration.loadConfiguration(configFile);
        fileConfig.options().parseComments(true);
        colorizeConfig(fileConfig);

        try {
            loadMessages();
        } catch (Exception e) {
            Bukkit.getServer().getLogger().severe("Something went wrong loading YML config");
        }
    }

    public HashMap<String, String> getMessagesMap() {
        return messagesMap;
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
     * Loads messages from the configuration file.
     */
    private void loadMessages() {
        // List of keys to fetch from the configuration file
        String[] keys = {"prefix", "invalid_trinket", "trinket_not_found", "unknown_command", "reload", "no_trinkets"};

        // Loop through each key and put it into the messagesMap
        for (String key : keys) {
            String value = fileConfig.getString(key);
            messagesMap.put(key, value);
        }
    }
}
