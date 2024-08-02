package me.plainoldmoose.trinkets;

import me.plainoldmoose.trinkets.GUI.TrinketsListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Trinkets extends JavaPlugin {

    private me.plainoldmoose.moosestrinkets.Data.TrinketManager manager = new me.plainoldmoose.moosestrinkets.Data.TrinketManager();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new TrinketsListener(), this);
        getCommand("trinkets").setExecutor(new me.plainoldmoose.moosestrinkets.Command.TrinketsCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Trinkets getInstance() {
        return getPlugin(Trinkets.class);
    }

    public me.plainoldmoose.moosestrinkets.Data.TrinketManager getManager() {
        return this.manager;
    }
}