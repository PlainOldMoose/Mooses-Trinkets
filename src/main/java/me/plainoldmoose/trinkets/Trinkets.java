package me.plainoldmoose.trinkets;

import com.willfp.eco.core.data.PlayerProfile;
import me.plainoldmoose.trinkets.Command.TrinketsCommand;
import me.plainoldmoose.trinkets.Data.Trinket;
import me.plainoldmoose.trinkets.Data.TrinketManager;
import me.plainoldmoose.trinkets.Data.TrinketsData;
import me.plainoldmoose.trinkets.Data.handlers.DataHandler;
import me.plainoldmoose.trinkets.GUI.GUIListener;
import me.plainoldmoose.trinkets.GUI.fetchers.ChatServiceFetcher;
import me.plainoldmoose.trinkets.GUI.interactions.TrinketInteractionHandler;
import org.bukkit.Bukkit;
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
        getServer().getPluginManager().registerEvents(new GUIListener(), this);
        getCommand("trinkets").setExecutor(commandExecutor);
        TrinketsData.getInstance().loadConfig();

        // Update command configurations and load data
        commandExecutor.update();

        Plugin vaultPlugin = Bukkit.getServer().getPluginManager().getPlugin("Vault");
        if (vaultPlugin == null || !vaultPlugin.isEnabled()) {
            // Vault is not installed or not enabled
            getLogger().info("Vault is not installed or not enabled. Proceeding without Vault dependency.");
            return;
        }

        try {
            Class<?> chatClass = Class.forName("net.milkbowl.vault.chat.Chat");
            ChatServiceFetcher.getInstance();
            getLogger().info("Vault's Chat class loaded successfully.");
        } catch (ClassNotFoundException e) {
            getLogger().warning("Vault is present, but the Chat class could not be found. Make sure Vault is correctly installed.");
        }

//        hookTrinketsDataOntoEco(true);
    }

    @Override
    public void onDisable() {
//        hookTrinketsDataOntoEco(false);
        DataHandler.getInstance().saveData();
    }

    public static Trinkets getInstance() {
        return getPlugin(Trinkets.class);
    }

    public void hookTrinketsDataOntoEco(boolean add) {
        Map<UUID, List<ItemStack>> data = DataHandler.getInstance().getEquippedTrinkets();
        for (Map.Entry<UUID, List<ItemStack>> entry : data.entrySet()) {
            PlayerProfile pp = PlayerProfile.load(entry.getKey());

            for (ItemStack item : entry.getValue()) {
                Trinket t = manager.getTrinketByDisplayName(item.getItemMeta().getDisplayName());
                System.out.println(">>> Editing stats for " + t.getName() + " from player " + entry.getKey());
                TrinketInteractionHandler.updatePlayerStats(pp, t.getStats(), add);
            }
        }
    }

    public TrinketManager getManager() {
        return this.manager;
    }

    public TrinketsCommand getCommandExecutor() {
        return commandExecutor;
    }
}