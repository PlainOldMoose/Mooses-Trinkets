package me.plainoldmoose.trinkets.Data.handlers;

import me.plainoldmoose.trinkets.Data.Trinket;
import me.plainoldmoose.trinkets.Trinkets;
import me.plainoldmoose.trinkets.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TrinketsHandler {
    private static final TrinketsHandler instance = new TrinketsHandler();

//    private File configFile;
//    private FileConfiguration fileConfig;

    public static TrinketsHandler getInstance() {
        return instance;
    }

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
        YamlConfiguration fileYamlConfig = YamlConfiguration.loadConfiguration(file);
        fileYamlConfig.options().parseComments(true);
        ConfigUtils.colorizeConfig(fileYamlConfig);

        Set<String> keys = fileYamlConfig.getKeys(false);
        String key = keys.iterator().next();
        Trinkets.getInstance().getManager().addTrinket(loadTrinket(fileYamlConfig, key));
    }

    private Trinket loadTrinket(YamlConfiguration config, String key) {
        String name = config.getString(key + ".name");
        String materialName = config.getString(key + ".material");
        String slotName = config.getString(key + ".type");

        ConfigurationSection statsSection = config.getConfigurationSection(key + ".stats");
        HashMap<String, Integer> statsMap = new HashMap<>();

        HashMap<String, Integer> formattedStatsMap = new HashMap<>();

        Map<String, String> formats = SkillsHandler.getInstance().getSkillNameFormat();

        Set<String> statKeys = statsSection.getKeys(false);

        for (String stat : statKeys) {
            int value = statsSection.getInt(stat);
            statsMap.put(stat, value);
            formattedStatsMap.put(formats.get(stat), value);
        }

        return new Trinket(Material.valueOf(materialName), key, name, statsMap, formattedStatsMap, slotName);
    }
}
