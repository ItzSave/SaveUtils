package org.itzsave.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.itzsave.SaveUtils;

public class PlayerListener implements Listener {

    final SaveUtils plugin = SaveUtils.getPlugin(SaveUtils.class);

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (plugin.getConfig().getBoolean("Settings.disable-join-messages")) {
            e.joinMessage(null);
            return;
        }
        if (e.getPlayer().hasPermission("saveutils.silent")) {
            e.joinMessage(null);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {

        if (plugin.getConfig().getBoolean("Settings.disable-quit-messages")) {
            e.quitMessage(null);
            return;
        }

        if (e.getPlayer().hasPermission("saveutils.silent")) {
            e.quitMessage(null);
        }
    }

}
