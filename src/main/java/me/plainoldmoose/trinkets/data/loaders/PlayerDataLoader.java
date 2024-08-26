package me.plainoldmoose.trinkets.data.loaders;

import me.plainoldmoose.trinkets.Trinkets;
import me.plainoldmoose.trinkets.data.trinket.SerializedTrinketSlot;
import me.plainoldmoose.trinkets.data.trinket.Trinket;
import me.plainoldmoose.trinkets.data.trinket.TrinketManager;
import me.plainoldmoose.trinkets.gui.builders.TrinketSlotBuilder;
import me.plainoldmoose.trinkets.gui.components.TrinketSlot;
import me.plainoldmoose.trinkets.gui.interactions.TrinketInteractionHandler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

/**
 * Manages the loading, saving, and interaction of player-specific trinket data.
 * This class is responsible for handling the serialization and deserialization of trinket slots
 * as well as hooking trinket data onto the EcoSkills plugin.
 */
public class PlayerDataLoader {
    private static final PlayerDataLoader instance = new PlayerDataLoader();
    private File configFile;
    private FileConfiguration fileConfig;

    private static final Map<UUID, List<ItemStack>> equippedTrinkets = new HashMap<>();
    private final Map<UUID, List<SerializedTrinketSlot>> serialisedSlots = new HashMap<>();

    /**
     * Loads player trinket data from the "data.yml" configuration file.
     * This method reads the file, deserializes trinket slot data for each player, and stores it in memory.
     */
    public void loadData() {
        serialisedSlots.clear();
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

    /**
     * Saves the current player trinket data to the "data.yml" configuration file.
     * This method serializes the trinket slots for each player and writes the data to disk.
     */
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

    /**
     * Hooks player trinket data onto the EcoSkills plugin.
     * This method loads the player's equipped trinkets and updates their stats in the EcoSkills plugin.
     *
     * @param player The player whose trinket data is being hooked into EcoSkills.
     * @param add    Whether to add or remove the trinket stats from the player.
     */
    public void hookTrinketsDataOntoEco(Player player, boolean add) {
        loadPlayerTrinkets(player);

        List<ItemStack> playerTrinkets = equippedTrinkets.get(player.getUniqueId());

        if (playerTrinkets == null || playerTrinkets.isEmpty()) {
            return;
        }

        TrinketManager trinketManager = TrinketManager.getInstance();

        for (ItemStack item : playerTrinkets) {
            // TODO - Test removing this if, I don't think it's needed
            if (item == null) {
                player.sendMessage("Null item!");
                return;
            }
            Trinket trinket = trinketManager.getTrinket(item);
            Map<String, Integer> trinketStats = trinket.getStats();
            TrinketInteractionHandler.updatePlayerStats(player, trinketStats, add);
        }
    }

    /**
     * Loads the player's trinkets into the equippedTrinkets map from serialized data.
     * This method deserializes trinket slots, recreates them, and stores the contained trinkets in memory.
     *
     * @param player The player whose trinkets are being loaded.
     */
    // TODO - If trinket is loaded in GUI, but is not in manager, remove trinket
    public static void loadPlayerTrinkets(Player player) {
        equippedTrinkets.clear();
        // Retrieve serialized slots for the player
        Map<UUID, List<SerializedTrinketSlot>> serializedSlots = PlayerDataLoader.getInstance().getSerialisedSlots();
        List<SerializedTrinketSlot> serializedTrinketSlotList = serializedSlots.get(player.getUniqueId());

        if (serializedTrinketSlotList == null) {
            return;
        }

        // Prepare a list to store equipped trinkets
        List<ItemStack> equipped = new ArrayList<>();
        TrinketSlotBuilder.createSlotButtons();

        // Iterate over the serialized slots
        for (SerializedTrinketSlot s : serializedTrinketSlotList) {

            // Deserialize trinket slot
            TrinketSlot trinketSlot = TrinketSlot.deserialize(s.getMap());

            // If player-loaded trinket slot is not found in config-loaded trinket slot map (i.e. the configuration has been changed since the player data was last loaded)
            Map<Integer, TrinketSlot> trinketSlotMap = TrinketSlotBuilder.getTrinketSlotMap();
            if (trinketSlotMap.get(trinketSlot.getIndex()) == null) {
                handlePlayerDataMismatch(player, trinketSlot);
            }

            // Add trinketSlot to map
            trinketSlotMap.put(trinketSlot.getIndex(), trinketSlot);
            // Add the contained trinket to the equipped list
            equipped.add(trinketSlot.getContainedTrinket());
        }

        // Update the equipped trinkets map
        equippedTrinkets.put(player.getUniqueId(), equipped);
    }

    private static void handlePlayerDataMismatch(Player player, TrinketSlot trinketSlot) {
        // Return item into inventory or drop on floor
        if (!player.getInventory().addItem(trinketSlot.getContainedTrinket()).isEmpty()) {
            player.getWorld().dropItem(player.getLocation(), trinketSlot.getContainedTrinket());
        }

        // Handle updating stats and hiding the slot
        TrinketInteractionHandler.updatePlayerStats(player, TrinketManager.getInstance().getTrinket(trinketSlot.getContainedTrinket()).getStats(), false);
        trinketSlot.pop();
        trinketSlot.setVisibility(false);
    }

    public Map<UUID, List<SerializedTrinketSlot>> getSerialisedSlots() {
        return serialisedSlots;
    }

    public static PlayerDataLoader getInstance() {
        return instance;
    }
}
