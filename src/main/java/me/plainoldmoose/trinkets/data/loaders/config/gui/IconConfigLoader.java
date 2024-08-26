package me.plainoldmoose.trinkets.data.loaders.config.gui;

import me.plainoldmoose.trinkets.data.loaders.eco.SkillsHandler;
import me.plainoldmoose.trinkets.gui.components.StatsIcon;
import me.plainoldmoose.trinkets.utils.ItemFactory;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handles the loading of stat icons from a configuration file.
 * This class provides functionality to retrieve, construct and store a list of stat icons
 * used in the GUI.
 */
public class IconConfigLoader {
    private static final IconConfigLoader instance = new IconConfigLoader();
    private final List<StatsIcon> iconList = new ArrayList<>();

    public void loadIcons(FileConfiguration fileConfig) {
        iconList.clear();
        ConfigurationSection section = fileConfig.getConfigurationSection("stat_icons");

        if (section == null) {
            return;
        }

        Set<String> keys = (section.getKeys(false));

        for (String key : keys) {
            loadIcon(key, section);
        }
    }

    private void loadIcon(String key, ConfigurationSection section) {
        String materialName = section.getString(key + ".material");
        String name = section.getString(key + ".name");
        int slot = section.getInt(key + ".slot");
        List<String> stats = section.getStringList(key + ".stats");

        Material material = Material.getMaterial(materialName.toUpperCase());

        stats.removeIf(stat -> !SkillsHandler.getInstance().getSkillFileNames().contains(stat));

        StatsIcon icon = new StatsIcon(ItemFactory.createItemStack(material, name), slot, stats);
        iconList.add(icon);
    }

    public static IconConfigLoader getInstance() {
        return instance;
    }

    public List<StatsIcon> getIconList() {
        return new ArrayList<>(iconList);
    }
}
