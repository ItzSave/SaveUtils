package org.itzsave.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.itzsave.SaveCore;

import java.util.Objects;

public class PlayerLeaveListener implements Listener {

    private final SaveCore plugin;

    public PlayerLeaveListener(SaveCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (plugin.getConfig().getBoolean("Settings.leave-message-enabled")) {
            if (e.getPlayer().hasPermission("savecore.slient")) {
                e.quitMessage(null);
            } else {
                e.quitMessage(Component.text(Objects.requireNonNull(SaveCore.color(plugin.getLangFile().getString("Event-Messages.leave-message")).replace("%player%", e.getPlayer().getName()))));
            }
        } else {
            e.quitMessage(null);
        }
        plugin.resetTrashItems(e.getPlayer());
    }
}
