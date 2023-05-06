package org.itzsave.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.itzsave.SaveUtils;

public class EntityListener implements Listener {

    private final SaveUtils plugin;

    public EntityListener(SaveUtils plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void entitySpawn(EntitySpawnEvent e) {
        if (plugin.getConfig().getBoolean("Modules.disable-wither-spawning", true) && e.getEntityType().equals(EntityType.WITHER)) {
            e.setCancelled(true);
        }


        if (plugin.getConfig().getBoolean("Modules.disable-phantom-spawning", true) && e.getEntityType().equals(EntityType.PHANTOM)) {
            e.setCancelled(true);
        }
    }

}
