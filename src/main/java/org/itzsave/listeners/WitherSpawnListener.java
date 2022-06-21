package org.itzsave.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.itzsave.SaveUtils;

public record WitherSpawnListener(SaveUtils plugin) implements Listener {


    @EventHandler
    public void witherSpawn(EntitySpawnEvent e) {
        if (plugin.getConfig().getBoolean("Settings.disable-wither-spawns")) {
            if (e.getEntityType().equals(EntityType.WITHER)) {
                e.setCancelled(true);
            }
        }

    }
}
