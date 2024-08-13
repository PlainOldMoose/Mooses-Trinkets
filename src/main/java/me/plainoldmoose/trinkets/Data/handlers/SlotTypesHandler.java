package me.plainoldmoose.trinkets.Data.handlers;

import me.plainoldmoose.trinkets.Trinkets;
import me.plainoldmoose.trinkets.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class SlotTypesHandler {
    private static final SlotTypesHandler instance = new SlotTypesHandler();

    public static SlotTypesHandler getInstance() {
        return instance;
    }

    private File configFile;
    private FileConfiguration fileConfig;
    private Set<String> slotSet = new HashSet<String>();

    /**
     * Loads the configuration file and initializes default values.
     */
    public void loadConfig() {
        configFile = new File(Trinkets.getInstance().getDataFolder(), "trinket_types.yml");

        if (!configFile.exists()) {
            Trinkets.getInstance().saveResource("trinket_types.yml", false);
        }

        fileConfig = YamlConfiguration.loadConfiguration(configFile);
        fileConfig.options().parseComments(true);
        ConfigUtils.colorizeConfig(fileConfig);

        try {
            loadSlots();
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getServer().getLogger().severe("[Mooses-Trinkets] Something went wrong when loading trinket_types.yml, please check the configuration.");
        }
    }

    private void loadSlots() {
        slotSet = new HashSet<String>(fileConfig.getStringList("types"));
        for (String slot : slotSet) {
        }
    }

    public Set<String> getSlotSet() {
        return slotSet;
    }
}
