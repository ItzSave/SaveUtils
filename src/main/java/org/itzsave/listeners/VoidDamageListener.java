package org.itzsave.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.itzsave.utils.Messages;

public class VoidDamageListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player player && e.getCause() == EntityDamageEvent.DamageCause.VOID) {
            player.teleport(player.getWorld().getSpawnLocation());
            Messages.VOID_DAMAGE.send(player);
            e.setCancelled(true);
        }
    }
}
