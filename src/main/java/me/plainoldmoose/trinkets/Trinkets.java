package me.plainoldmoose.trinkets;

import com.willfp.eco.core.data.PlayerProfile;
import com.willfp.eco.core.data.keys.PersistentDataKey;
import com.willfp.eco.core.data.keys.PersistentDataKeyType;
import me.plainoldmoose.trinkets.Command.TrinketsCommand;
import me.plainoldmoose.trinkets.Data.Trinket;
import me.plainoldmoose.trinkets.Data.TrinketManager;
import me.plainoldmoose.trinkets.Data.TrinketsData;
import me.plainoldmoose.trinkets.Data.handlers.DataHandler;
import me.plainoldmoose.trinkets.GUI.GUIListener;
import me.plainoldmoose.trinkets.GUI.fetchers.ChatServiceFetcher;
import me.plainoldmoose.trinkets.GUI.interactions.TrinketInteractionHandler;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Main class for the Trinkets plugin.
 * Extends JavaPlugin to provide the core functionality and lifecycle methods for the plugin.
 */
public final class Trinkets extends JavaPlugin {

    private TrinketManager manager = new TrinketManager();
    private TrinketsCommand commandExecutor = new TrinketsCommand();

    @Override
    public void onEnable() {
        PlayerProfile pp = PlayerProfile.load(UUID.fromString("3fbf679d-5114-45a5-a2c6-db5d4e7a5e80"));
        NamespacedKey statKey = new NamespacedKey("ecoskills", "defense");
        PersistentDataKey<Integer> intKey = new PersistentDataKey<>(statKey, PersistentDataKeyType.INT, 0);
        Integer currentStatValue = pp.read(intKey);
        System.out.println(">>>>> defense is " + currentStatValue + " before any calculations");

        getServer().getPluginManager().registerEvents(new GUIListener(), this);
        getCommand("trinkets").setExecutor(commandExecutor);

        TrinketsData.getInstance().reloadConfig();

        Plugin vaultPlugin = Bukkit.getServer().getPluginManager().getPlugin("Vault");

        if (vaultPlugin != null && vaultPlugin.isEnabled()) {
            try {
                // Attempt to load Vault's Chat class
                Class<?> chatClass = Class.forName("net.milkbowl.vault.chat.Chat");
                // Proceed with using the class, knowing it's available
                ChatServiceFetcher.getInstance();
                getLogger().info("Vault's Chat class loaded successfully.");
            } catch (ClassNotFoundException e) {
                // Handle the case where the class isn't available
                getLogger().warning("Vault is present, but the Chat class could not be found. Make sure Vault is correctly installed.");
            }
        } else {
            // Vault is not installed, force CSF to attempt to load anyway
            getLogger().info("Vault is not installed. Proceeding without Vault dependency.");
        }

        // Update command configurations and load data
        commandExecutor.update();

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! HELLO WORLD 1");
        Map<UUID, List<ItemStack>> data = DataHandler.getInstance().getEquippedTrinkets();
        for (Map.Entry<UUID, List<ItemStack>> entry : data.entrySet()) {
            pp = PlayerProfile.load(entry.getKey());
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! HELLO WORLD 2");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! LENGTH " + entry.getValue().size());

            for (ItemStack item : entry.getValue()) {
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! HELLO WORLD 3");
                Trinket t = manager.getTrinketByDisplayName(item.getItemMeta().getDisplayName());
                System.out.println(">>> Adding stats for " + t.getName() + " to player " + entry.getKey());
                TrinketInteractionHandler.updatePlayerStats(pp, t.getStats(), true);
            }
        }
    }

    @Override
    public void onDisable() {
        Map<UUID, List<ItemStack>> data = DataHandler.getInstance().getEquippedTrinkets();
        for (Map.Entry<UUID, List<ItemStack>> entry : data.entrySet()) {
            PlayerProfile pp = PlayerProfile.load(entry.getKey());

            for (ItemStack item : entry.getValue()) {
                Trinket t = manager.getTrinketByDisplayName(item.getItemMeta().getDisplayName());
                System.out.println(">>> Removing stats for " + t.getName() + " from player " + entry.getKey());
                TrinketInteractionHandler.updatePlayerStats(pp, t.getStats(), false);
            }
        }
        DataHandler.getInstance().saveData();

        PlayerProfile pp = PlayerProfile.load(UUID.fromString("3fbf679d-5114-45a5-a2c6-db5d4e7a5e80"));
        NamespacedKey statKey = new NamespacedKey("ecoskills", "defense");
        PersistentDataKey<Integer> intKey = new PersistentDataKey<>(statKey, PersistentDataKeyType.INT, 0);
        Integer currentStatValue = pp.read(intKey);
        System.out.println(">>>>> defense is now " + currentStatValue);
    }

    public static Trinkets getInstance() {
        return getPlugin(Trinkets.class);
    }

    public TrinketManager getManager() {
        return this.manager;
    }

    public TrinketsCommand getCommandExecutor() {
        return commandExecutor;
    }
}