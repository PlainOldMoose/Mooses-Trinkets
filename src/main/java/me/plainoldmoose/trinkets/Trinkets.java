package me.plainoldmoose.trinkets;

import me.plainoldmoose.trinkets.Command.TrinketsCommand;
import me.plainoldmoose.trinkets.Data.TrinketManager;
import me.plainoldmoose.trinkets.GUI.TrinketsListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Trinkets extends JavaPlugin {

    private TrinketManager manager = new TrinketManager();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new TrinketsListener(), this);
        getCommand("trinkets").setExecutor(new TrinketsCommand());
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
}