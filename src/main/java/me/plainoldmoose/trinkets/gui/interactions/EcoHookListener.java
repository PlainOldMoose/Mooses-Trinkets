package me.plainoldmoose.trinkets.gui.interactions;

import me.plainoldmoose.trinkets.data.loaders.PlayerDataLoader;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Listener class that handles events related to player connections and disconnections.
 * It hooks or unhooks trinket data to the EcoSkills API when players join or leave the game.
 */
public class EcoHookListener implements Listener {

    /**
     * Handles the event when a player joins the game.
     * This method hooks the player's trinket data onto the EcoSkills API.
     *
     * @param event The {@link PlayerJoinEvent} that is triggered when a player joins the game.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Hook the player's trinket data onto the EcoSkills API
        PlayerDataLoader.getInstance().hookTrinketsDataOntoEco(player, true);
    }

    /**
     * Handles the event when a player leaves the game.
     * This method unhooks the player's trinket data from the EcoSkills API.
     *
     * @param event The {@link PlayerQuitEvent} that is triggered when a player leaves the game.
     */
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Unhook the player's trinket data from the EcoSkills API
        PlayerDataLoader.getInstance().hookTrinketsDataOntoEco(player, false);
    }
}
