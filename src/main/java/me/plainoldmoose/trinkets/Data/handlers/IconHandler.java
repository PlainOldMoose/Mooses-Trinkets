package me.plainoldmoose.trinkets.Data.handlers;

import me.plainoldmoose.trinkets.Data.TrinketsData;
import me.plainoldmoose.trinkets.GUI.components.StatsIcon;
import me.plainoldmoose.trinkets.Trinkets;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
        colorizeConfig(fileConfig);

        try {
            loadIcons();
        } catch (Exception e) {
            Bukkit.getServer().getLogger().severe("[Mooses - Trinkets] Something went wrong when loading gui_icons.yml, please check the configuration.");
        }
    }

    private void loadIcons() {
        iconList.clear();
        Set<String> keys = fileConfig.getKeys(false);

        for (String key : keys) {
            String materialName = fileConfig.getString(key + ".material");
            String name = fileConfig.getString(key + ".name");
            int slot = fileConfig.getInt(key + ".slot");
            List<String> stats = fileConfig.getStringList(key + ".stats");

            Material material = Material.getMaterial(materialName.toUpperCase());

            for (String stat : stats) {
                if (!TrinketsData.getInstance().getSkillsHandler().getSkillFileNames().contains(stat)) {
                    stats.remove(stat);
                }
            }

            StatsIcon icon = new StatsIcon(createItemStack(material, name), slot);
            icon.setRawStatNames(stats);
            iconList.add(icon);
        }
    }

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

    /**
     * Creates an ItemStack with the specified material and name.
     *
     * @param material The material of the item.
     * @param name     The display name of the item.
     * @return The created ItemStack.
     */
    private ItemStack createItemStack(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        return item;
    }
}
