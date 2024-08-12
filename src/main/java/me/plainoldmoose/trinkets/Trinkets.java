package me.plainoldmoose.trinkets;

import me.plainoldmoose.trinkets.Command.TrinketsCommand;
import me.plainoldmoose.trinkets.Data.TrinketManager;
import me.plainoldmoose.trinkets.Data.TrinketsData;
import me.plainoldmoose.trinkets.GUI.GUIListener;
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

        getServer().getPluginManager().registerEvents(new GUIListener(), this);
        getCommand("trinkets").setExecutor(commandExecutor);
        TrinketsData.getInstance().loadConfig();

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