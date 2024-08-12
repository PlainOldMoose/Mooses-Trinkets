package me.plainoldmoose.trinkets.Data.handlers;

import me.plainoldmoose.trinkets.Data.TrinketsData;
import me.plainoldmoose.trinkets.GUI.components.StatsIcon;
import me.plainoldmoose.trinkets.Trinkets;
import me.plainoldmoose.trinkets.utils.ConfigUtils;
import me.plainoldmoose.trinkets.utils.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class IconHandler {
    private File configFile;
    private FileConfiguration fileConfig;

    public List<StatsIcon> getIconList() {
        return iconList;
    }

    private final List<StatsIcon> iconList = new ArrayList<>();

    public void loadConfig() {
        configFile = new File(Trinkets.getInstance().getDataFolder(), "gui_icons.yml");

        if (!configFile.exists()) {
            Trinkets.getInstance().saveResource("gui_icons.yml", false);
        }

        fileConfig = YamlConfiguration.loadConfiguration(configFile);
        fileConfig.options().parseComments(true);
        ConfigUtils.colorizeConfig(fileConfig);

        try {
            loadIcons();
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getServer().getLogger().severe("[Mooses - Trinkets] Something went wrong when loading gui_icons.yml, please check the configuration.");
        }
    }

    private void loadIcons() {
        TrinketsData data = TrinketsData.getInstance();
        if (data == null) {
            return;
        }

        iconList.clear();
        Set<String> keys = fileConfig.getKeys(false);

        for (String key : keys) {
            String materialName = fileConfig.getString(key + ".material");
            String name = fileConfig.getString(key + ".name");
            int slot = fileConfig.getInt(key + ".slot");
            List<String> stats = fileConfig.getStringList(key + ".stats");

            Material material = Material.getMaterial(materialName.toUpperCase());

            stats.removeIf(stat -> !data.getSkillsHandler().getSkillFileNames().contains(stat));

            StatsIcon icon = new StatsIcon(ItemFactory.createItemStack(material, name), slot);
            icon.setRawStatNames(stats);
            iconList.add(icon);
        }
    }
}
