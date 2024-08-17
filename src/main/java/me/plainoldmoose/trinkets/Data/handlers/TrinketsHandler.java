package me.plainoldmoose.trinkets.Data.handlers;

import me.plainoldmoose.trinkets.Data.Trinket;
import me.plainoldmoose.trinkets.Trinkets;
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

public class TrinketsHandler {
    private static final TrinketsHandler instance = new TrinketsHandler();

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

        // Get all .yml files in the directory that don't start with "_"
        File[] trinketFiles = configDir.listFiles((dir, name) -> name.endsWith(".yml") && !name.startsWith("_"));

        if (trinketFiles == null || trinketFiles.length == 0) {
            Bukkit.getServer().getLogger().warning("[Mooses-Trinkets] No valid trinket configuration files found in the /trinkets/ folder.");
            return;
        }

        Trinkets.getInstance().getManager().getTrinketList().clear();

        // Loop through each file and load the configuration
        for (File file : trinketFiles) {
            try {
                loadTrinketsFile(file); // Pass the configuration to your trinket loading method
            } catch (Exception e) {
                Bukkit.getServer().getLogger().severe("[Mooses-Trinkets] Something went wrong when loading " + file.getName() + ", please check the configuration.");
            }
        }
    }

    private void loadTrinketsFile(File file) {
        FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
        fileConfig.options().parseComments(true);
        ConfigUtils.colorizeConfig(fileConfig);

        Set<String> keys = fileConfig.getKeys(false);
        String key = keys.iterator().next();
        Trinkets.getInstance().getManager().addTrinket(loadTrinket(key, fileConfig));
    }

    private Trinket loadTrinket(String identifier, FileConfiguration fileConfig) {
        String displayName = fileConfig.getString(identifier + ".name");
        String materialName = fileConfig.getString(identifier + ".material");
        String slotName = fileConfig.getString(identifier + ".type");
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

        return new Trinket(Material.valueOf(materialName), identifier, displayName, statsMap, formattedStatsMap, slotName, modelID);
    }

    public static TrinketsHandler getInstance() {
        return instance;
    }
}
