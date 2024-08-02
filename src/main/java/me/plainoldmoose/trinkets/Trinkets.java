package me.plainoldmoose.trinkets;

import me.plainoldmoose.trinkets.Command.TrinketsCommand;
import me.plainoldmoose.trinkets.Data.TrinketManager;
import me.plainoldmoose.trinkets.Data.TrinketsData;
import me.plainoldmoose.trinkets.GUI.TrinketsListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class for the Trinkets plugin.
 * Extends JavaPlugin to provide the core functionality and lifecycle methods for the plugin.
 */
public final class Trinkets extends JavaPlugin {

    private TrinketManager manager = new TrinketManager();
    private  TrinketsCommand commandExecutor = new TrinketsCommand();

    @Override
    public void onEnable() {
        // Register event listeners
        getServer().getPluginManager().registerEvents(new TrinketsListener(), this);

        // Register commands and their executors
        getCommand("trinkets").setExecutor(commandExecutor);

        // Update command configurations and load data
        commandExecutor.update();
        TrinketsData.getInstance().loadConfig();
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