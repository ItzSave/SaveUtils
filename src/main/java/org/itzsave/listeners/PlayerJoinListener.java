package org.itzsave.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.itzsave.SaveUtils;

@SuppressWarnings("ConstantConditions")
public record PlayerJoinListener(SaveUtils plugin) implements Listener {


    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent e) {
        if (plugin.getConfig().getBoolean("Settings.join-message-enabled")) {
            if (e.getPlayer().hasPermission("saveutil.silent")) {
                e.joinMessage(null);
            } else {
                e.joinMessage(SaveUtils.color(plugin.getLangFile().getString("Event-Messages.join-message").replace("player", e.getPlayer().getName())));
            }
        } else {
            e.joinMessage(null);
        }
    }


}