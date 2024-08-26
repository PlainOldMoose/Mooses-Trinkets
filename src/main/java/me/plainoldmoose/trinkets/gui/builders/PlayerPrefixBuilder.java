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

    /**
     * Retrieves the singleton instance of the PlayerPrefixBuilder.
     *
     * @return The single instance of PlayerPrefixBuilder.
     */
    public static PlayerPrefixBuilder getInstance() {
        return instance;
    }

    private static Chat chat = null;

    /**
     * Retrieves the current Vault Chat API instance.
     *
     * @return The current chat instance, or {@code null} if not initialized.
     */
    public Chat getChat() {
        return chat;
    }

    /**
     * Constructs a new PlayerPrefixBuilder and sets up the chat service.
     */
    public PlayerPrefixBuilder() {
        setupChat();
    }

    /**
     * Sets up the chat service by initializing the Vault Chat API provider.
     * If Vault is not available, this method will not perform any actions.
     */
    private void setupChat() {
        if (!isVaultAvailable()) {
            return;
        }

        RegisteredServiceProvider<Chat> rsp = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);

        if (rsp == null) {
            return;
        }

        chat = rsp.getProvider();
        Bukkit.getLogger().info("[Mooses-Trinkets] Registered Vault chat hook.");
    }

    /**
     * Checks if the Vault plugin and its Chat service are available on the server.
     *
     * @return {@code true} if Vault Chat service is available, {@code false} otherwise.
     */
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
