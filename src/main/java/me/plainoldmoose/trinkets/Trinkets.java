package me.plainoldmoose.trinkets;

import me.plainoldmoose.trinkets.Command.TrinketsCommand;
import me.plainoldmoose.trinkets.Data.TrinketManager;
import me.plainoldmoose.trinkets.Data.TrinketsData;
import me.plainoldmoose.trinkets.Data.handlers.DataHandler;
import me.plainoldmoose.trinkets.GUI.GUIListener;
import me.plainoldmoose.trinkets.GUI.fetchers.ChatServiceFetcher;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Main class for the Trinkets plugin.
 * Extends JavaPlugin to provide the core functionality and lifecycle methods for the plugin.
 */
public final class Trinkets extends JavaPlugin {

    private TrinketManager manager = new TrinketManager();
    private TrinketsCommand commandExecutor = new TrinketsCommand();
    private File ecoFile;

    @Override
    public void onEnable() {
        ecoFile = new File("plugins/EcoSkills/stats");
        getServer().getPluginManager().registerEvents(new GUIListener(), this);
        getCommand("trinkets").setExecutor(commandExecutor);
        TrinketsData.getInstance().loadConfig();

        // Update command configurations and load data
        commandExecutor.update();

        DataHandler.hookTrinketsDataOntoEco(false);

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
    }

    @Override
    public void onDisable() {
        getLogger().info("Starting to disable Trinkets...");
        DataHandler.hookTrinketsDataOntoEco(false);
        getLogger().info("Data hooked onto Eco.");
        DataHandler.getInstance().saveData();
        getLogger().info("Data saved.");
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