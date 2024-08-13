package me.plainoldmoose.trinkets.GUI.fetchers;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Manages interactions with the Vault chat service. Provides functionality to set up chat integration
 * and retrieve player prefixes using the Vault Chat API.
 */
public class ChatServiceFetcher {
    private static final ChatServiceFetcher instance = new ChatServiceFetcher();

    public static ChatServiceFetcher getInstance() {
        return instance;
    }

    private Chat chat = null;

    public Chat getChat() {
        return chat;
    }

    public ChatServiceFetcher() {
        setupChat();
    }

    /**
     * Sets up the chat service by initializing the Vault Chat API provider.
     *
     * @return True if the chat service is successfully set up, false otherwise.
     */
    public boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp == null) {
            Bukkit.getLogger().severe("[Mooses-Trinkets] Vault Chat service provider not found!");
            return false;
        }
        chat = rsp.getProvider();
        boolean setupSuccessful = chat != null;
        if (setupSuccessful) {
            Bukkit.getLogger().info("[Mooses-Trinkets] Successfully initialized Vault Chat provider.");
        } else {
            Bukkit.getLogger().severe("[Mooses-Trinkets] Failed to initialize Vault Chat provider.");
        }

        // This is an info log, not a severe error
        Bukkit.getLogger().info("[Mooses-Trinkets] Registered Vault chat hook.");

        // Optional: Force flush logs for debugging purposes
        System.out.flush();

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
