package me.plainoldmoose.trinkets.data.loaders.eco;

import me.plainoldmoose.trinkets.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles the loading and management of skills from the EcoSkills plugin.
 * This class is responsible for reading skill files, formatting their names, and providing access
 * to the loaded skill data.
 */
public class SkillsHandler {
    private static final SkillsHandler instance = new SkillsHandler();

    private File pluginFolder;
    private List<String> skillFileNames = new ArrayList<>();
    private Map<String, String> skillNameFormat = new HashMap<>();

    /**
     * Loads the skill configuration by checking the existence of the EcoSkills folder
     * and then loading the files within it.
     */
    public void loadConfig() {
        pluginFolder = getEcoSkillsFolder();

        if (pluginFolder.exists() && pluginFolder.isDirectory()) {
            loadFiles();
        }
    }

    /**
     * Retrieves the EcoSkills folder where skill files are stored.
     * If the folder does not exist or is not a directory, logs an error.
     *
     * @return The directory containing skill files, or null if the directory is invalid.
     */
    private File getEcoSkillsFolder() {
        pluginFolder = new File("plugins/EcoSkills/stats");

        if (pluginFolder.exists() && pluginFolder.isDirectory()) {
            return pluginFolder;
        }

        Bukkit.getServer().getLogger().severe("[Mooses-Trinkets] Something went wrong when loading skills from eco, please check the configuration.");
        return null;
    }

    /**
     * Loads skill files from the EcoSkills folder, parsing their configurations
     * and storing the skill names and their formatted versions.
     */
    public void loadFiles() {
        skillFileNames.clear();
        skillNameFormat.clear();
        File skillsDirectory = getEcoSkillsFolder();

        File[] files = skillsDirectory.listFiles();

        if (files == null) {
            Bukkit.getServer().getLogger().severe("[Mooses-Trinkets] Something went wrong when loading skills from eco, please check the configuration.");
            return;
        }

        for (File file : files) {
            if (file.getName().contains("example")) {
                continue;
            }

            if (!file.getName().endsWith(".yml")) {
                continue;
            }

            String fileNameWithoutExtension = file.getName().replace(".yml", "");
            skillFileNames.add(fileNameWithoutExtension);

            FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
            fileConfig.options().parseComments(true);
            ConfigUtils.colorizeConfig(fileConfig);

            skillNameFormat.put(fileNameWithoutExtension, fileConfig.getString("name"));
        }
    }

    public Map<String, String> getSkillNameFormat() {
        return skillNameFormat;
    }

    public List<String> getSkillFileNames() {
        return skillFileNames;
    }

    public static SkillsHandler getInstance() {
        return instance;
    }
}
