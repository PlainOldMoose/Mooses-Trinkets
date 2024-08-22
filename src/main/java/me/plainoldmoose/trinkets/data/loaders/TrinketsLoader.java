package me.plainoldmoose.trinkets.data.loaders;

import me.plainoldmoose.trinkets.data.loaders.eco.SkillsHandler;
import me.plainoldmoose.trinkets.Trinkets;
import me.plainoldmoose.trinkets.data.trinket.Trinket;
import me.plainoldmoose.trinkets.data.trinket.TrinketManager;
import me.plainoldmoose.trinkets.data.trinket.TrinketType;
import me.plainoldmoose.trinkets.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TrinketsLoader {
    private static final TrinketsLoader instance = new TrinketsLoader();

    //TODO - refactor this entire class

    public void createTrinketsDir() {
        Trinkets plugin = Trinkets.getInstance();
        File dir = new File(plugin.getDataFolder(), "trinkets");

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File exampleFile = new File(dir, "trinkets/_example.yml");

        // Check if the example file already exists
        if (!exampleFile.exists()) {
            plugin.saveResource("trinkets/_example.yml", false);
        }
    }

    public void loadYMLs() {
        // Get the plugin's data folder (usually plugins/YourPluginName/)
        File dataFolder = Trinkets.getInstance().getDataFolder();
        File configDir = new File(dataFolder, "trinkets");

        // Check if the trinkets directory exists; if not, create it
        if (!configDir.exists()) {
            createTrinketsDir();
        }

        // Clear the current trinket list
        TrinketManager.getInstance().getTrinketList().clear();

        // Recursively search and load all valid .yml files in the directory and subdirectories
        loadYMLFilesRecursively(configDir);
    }

    private void loadYMLFilesRecursively(File directory) {
        // Get all files in the directory
        File[] files = directory.listFiles();

        if (files == null) {
            return;
        }

        // Loop through each file
        for (File file : files) {
            if (file.isDirectory()) {
                // If the file is a directory, recursively search it
                loadYMLFilesRecursively(file);
            } else if (file.isFile() && file.getName().endsWith(".yml") && !file.getName().startsWith("_")) {
                try {
                    // Load the trinket configuration file
                    loadTrinketsFile(file);
                } catch (Exception e) {
                    Bukkit.getServer().getLogger().severe("[Mooses-Trinkets] Something went wrong when loading " + file.getName() + ", please check the configuration.");
                }
            }
        }
    }

    private void loadTrinketsFile(File file) {
        FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
        fileConfig.options().parseComments(true);
        ConfigUtils.colorizeConfig(fileConfig);

        Set<String> keys = fileConfig.getKeys(false);
        String key = keys.iterator().next();
        TrinketManager.getInstance().addTrinket(loadTrinket(key, fileConfig));
    }

    private Trinket loadTrinket(String identifier, FileConfiguration fileConfig) {
        String displayName = fileConfig.getString(identifier + ".name");
        String materialName = fileConfig.getString(identifier + ".material");
        String slotName = fileConfig.getString(identifier + ".type");
        TrinketType type = new TrinketType(slotName);
        int modelID = fileConfig.getInt(identifier + ".model_id");

        ConfigurationSection statsSection = fileConfig.getConfigurationSection(identifier + ".stats");
        HashMap<String, Integer> statsMap = new HashMap<>();
        HashMap<String, Integer> formattedStatsMap = new HashMap<>();

        Map<String, String> formattedSkillNames = SkillsHandler.getInstance().getSkillNameFormat();

        Set<String> statKeys = statsSection.getKeys(false);

        for (String stat : statKeys) {
            int value = statsSection.getInt(stat);
            statsMap.put(stat, value);
            formattedStatsMap.put(formattedSkillNames.get(stat), value);
        }

        return new Trinket(Material.valueOf(materialName), identifier, displayName, statsMap, formattedStatsMap, type, modelID);
    }

    public static TrinketsLoader getInstance() {
        return instance;
    }
}
