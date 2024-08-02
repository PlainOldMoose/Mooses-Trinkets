package me.plainoldmoose.trinkets.Data;

import me.plainoldmoose.trinkets.Trinkets;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class TrinketsData {
    private final static TrinketsData instance = new TrinketsData();

    private File configFile;
    private FileConfiguration fileConfig;

    private TrinketsData() {
        loadConfig();
    }

    private void loadConfig() {
        configFile = new File(Trinkets.getInstance().getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            Trinkets.getInstance().saveResource("config.yml", false);
        }

        fileConfig = new YamlConfiguration();
//        fileConfig.options().parseComments(true);

        try {
            fileConfig.load(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TrinketsData getInstance() {
        return instance;
    }
}