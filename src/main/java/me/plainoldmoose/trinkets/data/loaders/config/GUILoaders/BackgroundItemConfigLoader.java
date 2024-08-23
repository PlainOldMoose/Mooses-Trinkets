package me.plainoldmoose.trinkets.data.loaders.config.GUILoaders;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Handles the loading of background materials from a configuration file.
 * This class provides functionality to retrieve the primary and secondary background materials
 * used in the GUI.
 */
public class BackgroundItemConfigLoader {
    private static final BackgroundItemConfigLoader instance = new BackgroundItemConfigLoader();
    private Material backgroundMaterial;
    private Material secondaryBackgroundMaterial;

    public void loadBackgroundMaterials(FileConfiguration fileConfig) {
        backgroundMaterial = Material.valueOf(fileConfig.getString("background-material"));
        secondaryBackgroundMaterial = Material.valueOf(fileConfig.getString("secondary-background-material"));
    }

    public static BackgroundItemConfigLoader getInstance() {
        return instance;
    }

    public Material getBackgroundMaterial() {
        return backgroundMaterial;
    }

    public Material getSecondaryBackgroundMaterial() {
        return secondaryBackgroundMaterial;
    }
}
