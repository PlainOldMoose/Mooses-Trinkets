package me.plainoldmoose.trinkets;

import me.plainoldmoose.trinkets.Command.TrinketsCommand;
import me.plainoldmoose.trinkets.data.TrinketsConfigHandler;
import me.plainoldmoose.trinkets.data.loaders.PlayerDataLoader;
import me.plainoldmoose.trinkets.data.trinket.SerializedTrinketSlot;
import me.plainoldmoose.trinkets.gui.GUIListener;
import me.plainoldmoose.trinkets.gui.builders.PlayerPrefixBuilder;
import me.plainoldmoose.trinkets.gui.interactions.EcoHookListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class for the Trinkets plugin.
 * Extends JavaPlugin to provide the core functionality and lifecycle methods for the plugin.
 */
public final class Trinkets extends JavaPlugin {
    private TrinketsCommand commandExecutor = new TrinketsCommand();

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(SerializedTrinketSlot.class, "SerializedTrinketSlot");
        getServer().getPluginManager().registerEvents(new GUIListener(), this);
        getServer().getPluginManager().registerEvents(new EcoHookListener(), this);
        getCommand("trinkets").setExecutor(commandExecutor);
        TrinketsConfigHandler.getInstance().loadConfig();

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
            PlayerPrefixBuilder.getInstance();
            getLogger().info("Vault's Chat class loaded successfully.");
        } catch (ClassNotFoundException e) {
            getLogger().warning("Vault is present, but the Chat class could not be found. Make sure Vault is correctly installed.");
        }

    }

    @Override
    public void onDisable() {
        PlayerDataLoader.getInstance().saveData();
    }

    public static Trinkets getInstance() {
        return getPlugin(Trinkets.class);
    }

    public TrinketsCommand getCommandExecutor() {
        return commandExecutor;
    }
}