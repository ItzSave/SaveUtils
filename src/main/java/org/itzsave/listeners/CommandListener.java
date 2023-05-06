package org.itzsave.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {

    @EventHandler
    public void commandEvent(PlayerCommandPreprocessEvent event){
        Player player = event.getPlayer();
        String command = event.getMessage().split(" ")[0].toLowerCase();

        if (command.contains(":") && !player.isOp()) {
            event.setCancelled(true);
        }
    }
}
