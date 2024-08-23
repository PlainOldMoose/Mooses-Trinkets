package me.plainoldmoose.trinkets.gui.interactions;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EcoHookListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

//        PlayerDataLoader.getInstance().hookTrinketsDataOntoEco(player, true);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

//        PlayerDataLoader.getInstance().hookTrinketsDataOntoEco(player, false);
    }
}
