package org.itzsave.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.joinMessage(null);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        e.quitMessage(null);
    }

}
