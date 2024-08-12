package me.plainoldmoose.trinkets.GUI.fetchers;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import java.util.logging.Logger;

/**
 * Manages interactions with the Vault chat service. Provides functionality to setup chat integration
 * and retrieve player prefixes using the Vault Chat API.
 */
public class ChatServiceFetcher {
    private static Chat chat = null;
    private static final Logger LOGGER = Bukkit.getLogger();

    public static Chat getChat() {
        return chat;
    }

    /**
     * Sets up the chat service by initializing the Vault Chat API provider.
     *
     * @return True if the chat service is successfully set up, false otherwise.
     */
    public boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp == null) {
            LOGGER.severe("Vault Chat service provider not found!");
            return false;
        }
        chat = rsp.getProvider();
        boolean setupSuccessful = chat != null;
        if (!setupSuccessful) {
            LOGGER.severe("Failed to initialize Vault Chat provider.");
        }
        return setupSuccessful;
    }

    /**
     * Retrieves the prefix of the specified player using the Vault Chat API.
     *
     * @param player The player whose prefix is to be retrieved.
     * @return The prefix of the player, or an empty string if chat is not set up.
     */
    public String getPlayerPrefix(Player player) {
        return chat != null ? chat.getPlayerPrefix(player) : "";
    }
}
