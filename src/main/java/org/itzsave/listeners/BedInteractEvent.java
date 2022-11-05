package org.itzsave.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.itzsave.SaveUtils;
import org.itzsave.utils.Messages;

public class BedInteractEvent implements Listener {

    SaveUtils plugin = SaveUtils.getPlugin(SaveUtils.class);

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent e) {
        if (plugin.getConfig().getBoolean("Settings.warning-message-for-beds", false)) {
            Messages.BED_WARNING.send(e.getPlayer());

        }

    }

}
