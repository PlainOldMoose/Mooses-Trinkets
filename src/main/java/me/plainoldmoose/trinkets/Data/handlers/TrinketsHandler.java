package me.plainoldmoose.trinkets.Data.handlers;

import me.plainoldmoose.trinkets.Data.Trinket;
import me.plainoldmoose.trinkets.Data.TrinketManager;
import me.plainoldmoose.trinkets.Data.TrinketsData;
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

    private File configFile;
    private FileConfiguration fileConfig;

    public TrinketsHandler getInstance() {
        return instance;
    }

    public void loadConfig() {
        configFile = new File(Trinkets.getInstance().getDataFolder(), "trinkets.yml");

        if (!configFile.exists()) {
            Trinkets.getInstance().saveResource("trinkets.yml", false);
        }

        fileConfig = YamlConfiguration.loadConfiguration(configFile);
        fileConfig.options().parseComments(true);
        ConfigUtils.colorizeConfig(fileConfig);

        try {
            loadTrinkets();
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getServer().getLogger().severe("[Mooses - Trinkets] Something went wrong when loading trinkets.yml, please check the configuration.");
        }
    }

    private void loadTrinkets() {
        TrinketManager manager = Trinkets.getInstance().getManager();
        manager.getTrinketList().clear();

        Set<String> keySet = fileConfig.getKeys(false);

        for (String key : keySet) {
            manager.addTrinket(loadTrinket(key));
        }

    }

    private Trinket loadTrinket(String key) {
        TrinketsData data = TrinketsData.getInstance();
        if (data == null) {
            return null;
        }

        String name = fileConfig.getString(key + ".name");
        String materialName = fileConfig.getString(key + ".material");
        String slotName = fileConfig.getString(key + ".slot");

        ConfigurationSection statsSection = fileConfig.getConfigurationSection(key + ".stats");
        HashMap<String, Integer> statsMap = new HashMap<>();
        HashMap<String, Integer> formattedStatsMap = new HashMap<>();

        Map<String, String> formats = data.getSkillsHandler().getSkillNameFormat();

        if (statsSection == null) {
            return null;
        }

        Set<String> statKeys = statsSection.getKeys(false);

        for (String stat : statKeys) {
            int value = statsSection.getInt(stat);
            statsMap.put(stat, value);
            formattedStatsMap.put(formats.get(stat), value);
        }

        return new Trinket(Material.valueOf(materialName), key, name, statsMap, formattedStatsMap, slotName);
    }
}
