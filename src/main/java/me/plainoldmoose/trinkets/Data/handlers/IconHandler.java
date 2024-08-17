package me.plainoldmoose.trinkets.Data.handlers;

import me.plainoldmoose.trinkets.GUI.components.StatsIcon;
import me.plainoldmoose.trinkets.utils.ItemFactory;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class IconHandler {
    private static final IconHandler instance = new IconHandler();
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

    public static IconHandler getInstance() {
        return instance;
    }

    public List<StatsIcon> getIconList() {
        return iconList;
    }
}
