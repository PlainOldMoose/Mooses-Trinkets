package me.plainoldmoose.trinkets.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

public class ConfigUtils {
    /**
     * Creates an ItemStack from the configuration file.
     *
     * @param materialPath The path to the material in the configuration file.
     * @return The created ItemStack.
     */
    public static ItemStack createItemStack(FileConfiguration fileConfig, String materialPath) {
        Material material = Material.valueOf(fileConfig.getString(materialPath));
        return new ItemStack(material);
    }

    /**
     * Colorsize the configuration file's values.
     *
     * @param config The configuration file to colorize.
     */
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
}
