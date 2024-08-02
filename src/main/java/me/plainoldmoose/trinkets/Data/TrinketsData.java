package me.plainoldmoose.moosestrinkets.Data;

import me.plainoldmoose.moosestrinkets.MoosesTrinkets;
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
        configFile = new File(MoosesTrinkets.getInstance().getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            MoosesTrinkets.getInstance().saveResource("config.yml", false);
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