package me.plainoldmoose.trinkets.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

/**
 * Utility class for handling configuration-related tasks.
 * Provides methods to create {@link ItemStack}s from configuration values and to colorize configuration values.
 */
public class ConfigUtils {

    /**
     * Creates an {@link ItemStack} from a configuration file based on the material path.
     * The material is specified by a path in the configuration file.
     *
     * @param fileConfig The configuration file containing the material path.
     * @param materialPath The path to the material in the configuration file.
     * @return The created {@link ItemStack}.
     */
    public static ItemStack createItemStack(FileConfiguration fileConfig, String materialPath) {
        Material material = Material.valueOf(fileConfig.getString(materialPath));
        return new ItemStack(material);
    }

    /**
     * Creates an {@link ItemStack} from a configuration section based on the material name.
     * The material is specified directly as a string.
     *
     * @param section The configuration section containing the material name.
     * @param materialName The name of the material.
     * @return The created {@link ItemStack}.
     */
    public static ItemStack createItemStack(ConfigurationSection section, String materialName) {
        Material material = Material.valueOf(materialName);
        return new ItemStack(material);
    }

    /**
     * Colorizes all string values in the configuration file by replacing color codes with the corresponding colors.
     * The color codes are in the format '&' followed by a color character.
     *
     * @param config The configuration file to be colorized.
     */
    public static void colorizeConfig(FileConfiguration config) {
        colorizeSection(config);
    }

    /**
     * Recursively colorizes all string values in the configuration section.
     * This method replaces color codes with the corresponding colors.
     *
     * @param section The configuration section to be colorized.
     */
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
     * Colorizes a string by replacing color codes with the corresponding colors.
     * The color codes are in the format '&' followed by a color character.
     *
     * @param string The string to be colorized.
     * @return The colorized string.
     */
    public static String colorizeString(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
