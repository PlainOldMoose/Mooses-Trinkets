package me.plainoldmoose.trinkets.gui.builders;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import static org.bukkit.Bukkit.getLogger;

/**
 * Manages interactions with the Vault chat service. Provides functionality to set up chat integration
 * and retrieve player prefixes using the Vault Chat API.
 */
public class PlayerPrefixBuilder {
    private static final PlayerPrefixBuilder instance = new PlayerPrefixBuilder();

    public static PlayerPrefixBuilder getInstance() {
        return instance;
    }

    private static Chat chat = null;

    public Chat getChat() {
        return chat;
    }

    public PlayerPrefixBuilder() {
        setupChat();
    }

    /**
     * Sets up the chat service by initializing the Vault Chat API provider.
     */
    private void setupChat() {
        if (!isVaultAvailable()) {
            return;
        }

        RegisteredServiceProvider<Chat> rsp = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);

        chat = rsp.getProvider();
        Bukkit.getLogger().info("[Mooses-Trinkets] Registered Vault chat hook.");
    }

    private boolean isVaultAvailable() {
        try {
            Class.forName("net.milkbowl.vault.chat.Chat");
            return true;
        } catch (ClassNotFoundException e) {
            getLogger().warning("[Mooses-Trinkets] Vault Chat service provider not found!");
            return false;
        }
    }

    /**
     * Retrieves the prefix of the specified player using the Vault Chat API.
     *
     * @param player The player whose prefix is to be retrieved.
     * @return The prefix of the player, or an empty string if chat is not set up.
     */
    public static String getPlayerPrefix(Player player) {
        return chat != null ? chat.getPlayerPrefix(player) : "";
    }
}
