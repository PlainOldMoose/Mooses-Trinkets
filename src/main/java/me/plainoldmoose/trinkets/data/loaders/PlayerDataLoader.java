package me.plainoldmoose.trinkets.data.loaders;

import me.plainoldmoose.trinkets.Trinkets;
import me.plainoldmoose.trinkets.data.trinket.SerializedTrinketSlot;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerDataLoader {
    private static final PlayerDataLoader instance = new PlayerDataLoader();
    private File configFile;
    private FileConfiguration fileConfig;

//    private final Map<UUID, List<ItemStack>> equippedTrinkets = new HashMap<>();
    private final Map<UUID, List<SerializedTrinketSlot>> serialisedSlots = new HashMap<>();

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
            List<SerializedTrinketSlot> serializedTrinketSlotList = (List<SerializedTrinketSlot>) fileConfig.get(playerUUID.toString());
            serialisedSlots.put(playerUUID, serializedTrinketSlotList);
        }
    }

    public void saveData() {
        for (Map.Entry<UUID, List<SerializedTrinketSlot>> playerData : serialisedSlots.entrySet()) {
            fileConfig.set(playerData.getKey().toString(), playerData.getValue());
        }

        try {
            fileConfig.save(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void hookTrinketsDataOntoEco(Player player, boolean add) {
//        List<ItemStack> playerTrinkets = equippedTrinkets.get(player.getUniqueId());
//
//        if (playerTrinkets == null) {
//            return;
//        }
//
//        TrinketManager trinketManager = TrinketManager.getInstance();
//
//        for (ItemStack item : playerTrinkets) {
//            Trinket trinket = trinketManager.getTrinket(item);
//            Map<String, Integer> trinketStats = trinket.getStats();
//            TrinketInteractionHandler.updatePlayerStats(player, trinketStats, add);
//        }
//    }

    public Map<UUID, List<SerializedTrinketSlot>> getSerialisedSlots() {
        return serialisedSlots;
    }

//    public Map<UUID, List<ItemStack>> getEquippedTrinkets() {
//        return equippedTrinkets;
//    }

    public static PlayerDataLoader getInstance() {
        return instance;
    }
}
