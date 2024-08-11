package me.plainoldmoose.trinkets.Data;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class TrinketsHandler {
    private static final TrinketsHandler instance = new TrinketsHandler();

    private File configFile;
    private FileConfiguration fileConfig;

    public TrinketsHandler getInstance() {
        return instance;
    }

    public void loadConfig() {

    }
}
