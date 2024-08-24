package me.plainoldmoose.trinkets;

import me.plainoldmoose.trinkets.command.TrinketsCommand;
import me.plainoldmoose.trinkets.data.TrinketsConfigHandler;
import me.plainoldmoose.trinkets.data.loaders.PlayerDataLoader;
import me.plainoldmoose.trinkets.data.trinket.SerializedTrinketSlot;
import me.plainoldmoose.trinkets.data.trinket.TrinketManager;
import me.plainoldmoose.trinkets.listeners.GUIListener;
import me.plainoldmoose.trinkets.gui.builders.PlayerPrefixBuilder;
import me.plainoldmoose.trinkets.gui.interactions.EcoHookListener;
import me.plainoldmoose.trinkets.listeners.TrinketStackListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
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
        // Register SerializedTrinketSlot class for configuration serialization
        ConfigurationSerialization.registerClass(SerializedTrinketSlot.class, "SerializedTrinketSlot");

        // Register event listeners
        getServer().getPluginManager().registerEvents(new GUIListener(), this);
        getServer().getPluginManager().registerEvents(new EcoHookListener(), this);
        getServer().getPluginManager().registerEvents(new TrinketStackListener(), this);

        // Set up the Trinkets command executor
        getCommand("trinkets").setExecutor(commandExecutor);

        // Load the plugin's configuration
        TrinketsConfigHandler.getInstance().loadConfig();

        // Update command configurations and load data
        commandExecutor.update();

        createDiamondSwordRecipe();

        // Check if Vault is installed and enabled
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

    private void createDiamondSwordRecipe() {
        // Create a new ItemStack (the result of the recipe)
        ItemStack item = TrinketManager.getInstance().getTrinketItemStack("anvil_of_defense");
        // Create a NamespacedKey for the recipe
        NamespacedKey key = new NamespacedKey(this, "trinket");
        // Create a ShapedRecipe with the NamespacedKey and the result ItemStack
        ShapedRecipe recipe = new ShapedRecipe(key, item);

        // Define the shape of the recipe (3x3 grid)
        recipe.shape(" D ", " S ", " D ");

        // Set the ingredients ('D' = Diamond, 'S' = Stick)
        recipe.setIngredient('D', Material.DIAMOND);
        recipe.setIngredient('S', Material.STICK);

        // Add the recipe to the Bukkit server
        Bukkit.addRecipe(recipe);
    }

    public static Trinkets getInstance() {
        return getPlugin(Trinkets.class);
    }

    public TrinketsCommand getCommandExecutor() {
        return commandExecutor;
    }
}