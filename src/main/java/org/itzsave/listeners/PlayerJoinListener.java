package org.itzsave.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.itzsave.SaveUtils;

import java.util.Objects;

public record PlayerJoinListener(SaveUtils plugin) implements Listener {


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (plugin.getConfig().getBoolean("Settings.join-message-enabled")) {
            if (e.getPlayer().hasPermission("savecore.slient")) {
                e.joinMessage(null);
            } else {
                e.joinMessage(SaveUtils.color(Objects.requireNonNull(plugin.getLangFile().getString("Event-Messages.join-message")).replace("player", e.getPlayer().getName())));
            }
        } else {
            e.joinMessage(null);
        }

    }

}
