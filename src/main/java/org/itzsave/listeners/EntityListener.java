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
        if (plugin.getConfig().getBoolean("Settings.disable-wither-spawning", true)) {
            if (e.getEntityType().equals(EntityType.WITHER)) {
                plugin.getLogger().info("[Module] Disabling wither spawning.");
                e.setCancelled(true);
            }
        }

        if (plugin.getConfig().getBoolean("Modules.disable-phantom-spawning", true)) {
            if (e.getEntityType().equals(EntityType.PHANTOM)) {
                plugin.getLogger().info("[Module] Disabling phantom spawning.");
                e.setCancelled(true);

            }
        }

    }
}
