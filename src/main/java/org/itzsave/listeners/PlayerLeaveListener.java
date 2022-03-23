package org.itzsave.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.itzsave.SaveCore;

public class PlayerLeaveListener implements Listener {

    private final SaveCore plugin;

    public PlayerLeaveListener(SaveCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (!plugin.getConfig().getBoolean("Settings.leave-message-enabled")) {
            e.quitMessage(null);
        }
    }
}
