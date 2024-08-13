package me.plainoldmoose.trinkets.Data.handlers;

import me.plainoldmoose.trinkets.Data.TrinketManager;
import me.plainoldmoose.trinkets.Trinkets;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

public class DataHandler {
    private static final DataHandler instance = new DataHandler();

    public static DataHandler getInstance() {
        return instance;
    }

    private File configFile;
    private FileConfiguration fileConfig;

    private final Map<UUID, List<ItemStack>> equippedTrinkets = new HashMap<UUID, List<ItemStack>>();
    private TrinketManager manager = Trinkets.getInstance().getManager();


    public void loadData() {
        configFile = new File(Trinkets.getInstance().getDataFolder(), "data.yml");

        if (!configFile.exists()) {
            Trinkets.getInstance().saveResource("data.yml", false);
        }

        fileConfig = YamlConfiguration.loadConfiguration(configFile);
        fileConfig.options().parseComments(true);

        try {
            fileConfig.load(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String key : fileConfig.getKeys(false)) {
            UUID playerUUID = UUID.fromString(key);
            List<ItemStack> trinketsList = new ArrayList<ItemStack>();

            ItemStack[] items = ((List<ItemStack>) fileConfig.get(key)).toArray(new ItemStack[0]);
            trinketsList = List.of(items);
            equippedTrinkets.put(playerUUID, trinketsList);
        }
    }

    public void saveData() {
        for (Map.Entry<UUID, List<ItemStack>> playerData : equippedTrinkets.entrySet()) {
            fileConfig.set(playerData.getKey().toString(), playerData.getValue());
        }

        try {
            fileConfig.save(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<UUID, List<ItemStack>> getEquippedTrinkets() {
        return equippedTrinkets;
    }
}
