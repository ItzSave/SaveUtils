package org.itzsave.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.itzsave.SaveUtils;

import java.util.Objects;


public class ItemPickupListener implements Listener {

    private final SaveUtils plugin;

    public ItemPickupListener(SaveUtils plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void itemPickup(EntityPickupItemEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Player) {
            Player p = (Player) e.getEntity();

            Material item = e.getItem().getItemStack().getType();

            if (this.plugin.getTrashItems(p) == null) {
                //this.plugin.getServer().getLogger().log(Level.SEVERE, "Trash Items ret null.");
                return;
            }
            if (Objects.requireNonNull(this.plugin.getTrashItemsMat(p)).contains(item)) {
                e.getItem().remove();
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventory(InventoryPickupItemEvent e) {
        for (HumanEntity he : e.getInventory().getViewers()) {
            if (he instanceof Player p) {
                if (Objects.requireNonNull(this.plugin.getTrashItemsMat(p)).contains(e.getItem().getItemStack().getType()))
                    e.getItem().remove();
            }
        }
    }
}
