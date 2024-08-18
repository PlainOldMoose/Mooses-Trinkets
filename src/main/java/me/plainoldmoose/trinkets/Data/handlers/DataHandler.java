package me.plainoldmoose.trinkets.Data.handlers;

import com.willfp.eco.core.data.PlayerProfile;
import me.plainoldmoose.trinkets.Data.Trinket;
import me.plainoldmoose.trinkets.Data.TrinketManager;
import me.plainoldmoose.trinkets.GUI.interactions.TrinketInteractionHandler;
import me.plainoldmoose.trinkets.Trinkets;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DataHandler {
    private static final DataHandler instance = new DataHandler();
    private File configFile;
    private FileConfiguration fileConfig;

    private final Map<UUID, List<ItemStack>> equippedTrinkets = new HashMap<UUID, List<ItemStack>>();

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
            Bukkit.getServer().getLogger().severe("[Mooses-Trinkets] Something went wrong when loading data.yml, data may be corrupted.");
        }

        for (String key : fileConfig.getKeys(false)) {
            UUID playerUUID = UUID.fromString(key);
            List<ItemStack> trinketsList;

            ItemStack[] items = ((List<ItemStack>) fileConfig.get(key)).toArray(new ItemStack[0]);
            trinketsList = List.of(items);
            System.out.println(">>>>> Loaded " + playerUUID + " > " +  trinketsList.size() + " trinkets");
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

    public static void hookTrinketsDataOntoEco(boolean add) {
        Map<UUID, List<ItemStack>> data = DataHandler.getInstance().getEquippedTrinkets();
        for (Map.Entry<UUID, List<ItemStack>> entry : data.entrySet()) {
            PlayerProfile pp = PlayerProfile.load(entry.getKey());

            for (ItemStack item : entry.getValue()) {
                TrinketManager manager = Trinkets.getInstance().getManager();
                Trinket t = manager.getTrinketByDisplayName(item.getItemMeta().getDisplayName());
                System.out.println(">>> Editing stats for " + t.getName() + " on player " + entry.getKey());
                TrinketInteractionHandler.updatePlayerStats(pp, t.getStats(), add);
            }
        }
    }

    public Map<UUID, List<ItemStack>> getEquippedTrinkets() {
        return equippedTrinkets;
    }

    public static DataHandler getInstance() {
        return instance;
    }
}
