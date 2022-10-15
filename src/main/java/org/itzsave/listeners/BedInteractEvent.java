package org.itzsave.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.itzsave.SaveUtils;

public class BedInteractEvent implements Listener {

    private final SaveUtils plugin;

    public BedInteractEvent(SaveUtils plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent e) {
        if (plugin.getConfig().getBoolean("Settings.warning-message-for-beds", false)) {
            e.getPlayer().sendMessage(SaveUtils.color(plugin.getLangFile().getString("Messages.bed-warning-message")));

        }

    }

}
