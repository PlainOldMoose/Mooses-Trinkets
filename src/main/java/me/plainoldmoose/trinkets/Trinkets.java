package me.plainoldmoose.trinkets;

import me.plainoldmoose.trinkets.Command.TrinketsCommand;
import me.plainoldmoose.trinkets.Data.TrinketManager;
import me.plainoldmoose.trinkets.Data.TrinketsData;
import me.plainoldmoose.trinkets.GUI.GUIListener;
import me.plainoldmoose.trinkets.GUI.fetchers.ChatServiceFetcher;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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