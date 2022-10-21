package org.itzsave.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;

public class PhantomListener implements Listener {


    @EventHandler
    private void disable(CreatureSpawnEvent event) {
        @NotNull LivingEntity entity = event.getEntity();

        if (entity.getType() != EntityType.PHANTOM) {
            return;
        }

        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
            event.setCancelled(true);
        }
    }
}
