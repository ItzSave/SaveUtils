package org.itzsave.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.itzsave.SaveUtils;

import java.util.Objects;

@SuppressWarnings("ConstantConditions")
public class PlayerLeaveListener implements Listener {

    private final SaveUtils plugin;

    public PlayerLeaveListener(SaveUtils plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (plugin.getConfig().getBoolean("Settings.leave-message-enabled")) {
            if (e.getPlayer().hasPermission("saveutil.slient")) {
                e.quitMessage(null);
            } else {
                e.quitMessage(SaveUtils.color(Objects.requireNonNull(plugin.getLangFile().getString("Event-Messages.leave-message").replace("%player%", e.getPlayer().getName()))));
            }
        } else {
            e.quitMessage(null);
        }
        plugin.getAutoTrashHandler().resetTrashItems(e.getPlayer());
    }
}
