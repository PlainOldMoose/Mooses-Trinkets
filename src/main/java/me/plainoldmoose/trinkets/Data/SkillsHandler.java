package me.plainoldmoose.trinkets.Data;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillsHandler {
    private File pluginFolder;
    private List<String> skillFileNames = new ArrayList<String>();
    private Map<String, String> skillNameFormat = new HashMap<String, String>();
    private FileConfiguration fileConfig;

    private List<String> skills = new ArrayList<String>();

    public List<String> getSkillFileNames() {
        return skillFileNames;
    }

    public void loadConfig() {
        pluginFolder = getEcoSkillsFolder();


        if (pluginFolder.exists() && pluginFolder.isDirectory()) {
            loadSkillFileNames();
            loadSkillFormats();
        }
    }

    private File getEcoSkillsFolder() {
        pluginFolder = new File("plugins/EcoSkills/stats");

        if (pluginFolder.exists() && pluginFolder.isDirectory()) {
            return pluginFolder;
        }
        return null;
    }

    private void loadSkillFormats() {
        skillNameFormat.clear();
        File skillDirectory = getEcoSkillsFolder();

        File[] files = skillDirectory.listFiles();

        for (File file : files) {
            // Check if the file has a .yml extension
            if (file.isFile() && file.getName().endsWith(".yml")) {
                FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
                fileConfig.options().parseComments(true);
                colorizeConfig(fileConfig);

                String fileNameWithoutExtension = file.getName().replace(".yml", "");

                skillNameFormat.put(fileNameWithoutExtension, fileConfig.getString("name"));
            }
        }

    }

    private void loadSkillFileNames() {
        skillFileNames.clear();
        File skillDirectory = getEcoSkillsFolder();

        File[] files = skillDirectory.listFiles();

        for (File file : files) {
            // Check if the file has a .yml extension
            if (file.isFile() && file.getName().endsWith(".yml")) {
                String fileNameWithoutExtension = file.getName().replace(".yml", "");
                if (!fileNameWithoutExtension.contains("example")) {
                    skillFileNames.add(fileNameWithoutExtension);
                }
            }
        }
    }

    /**
     * Colorsizes the configuration file's values.
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

    public Map<String, String> getSkillNameFormat() {
        return skillNameFormat;
    }
}
