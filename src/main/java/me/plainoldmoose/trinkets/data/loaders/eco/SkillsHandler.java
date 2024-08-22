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

public class SkillsHandler {
    private static final SkillsHandler instance = new SkillsHandler();

    private File pluginFolder;
    private List<String> skillFileNames = new ArrayList<String>();
    private Map<String, String> skillNameFormat = new HashMap<String, String>();

    public void loadConfig() {
        pluginFolder = getEcoSkillsFolder();

        if (pluginFolder.exists() && pluginFolder.isDirectory()) {
            loadFiles();
        }
    }

    private File getEcoSkillsFolder() {
        pluginFolder = new File("plugins/EcoSkills/stats");

        if (pluginFolder.exists() && pluginFolder.isDirectory()) {
            return pluginFolder;
        }

        Bukkit.getServer().getLogger().severe("[Mooses-Trinkets] Something went wrong when loading skills from eco, please check the configuration.");
        return null;
    }

    public void loadFiles() {
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
