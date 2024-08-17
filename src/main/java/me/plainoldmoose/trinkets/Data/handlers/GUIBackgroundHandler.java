package me.plainoldmoose.trinkets.Data.handlers;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

public class GUIBackgroundHandler {
    private static final GUIBackgroundHandler instance = new GUIBackgroundHandler();
    private Material backgroundMaterial;
    private Material secondaryBackgroundMaterial;

    public void loadBackgroundMaterials(FileConfiguration fileConfig) {
        backgroundMaterial = Material.valueOf(fileConfig.getString("background-material"));
        secondaryBackgroundMaterial = Material.valueOf(fileConfig.getString("secondary-background-material"));
    }

    public static GUIBackgroundHandler getInstance() {
        return instance;
    }

    public Material getBackgroundMaterial() {
        return backgroundMaterial;
    }

    public Material getSecondaryBackgroundMaterial() {
        return secondaryBackgroundMaterial;
    }
}
