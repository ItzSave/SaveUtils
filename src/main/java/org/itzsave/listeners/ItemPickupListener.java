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
import org.jetbrains.annotations.NotNull;


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

            if (plugin.getAutoTrashHandler().getTrashItems(p) != null && plugin.getAutoTrashHandler().getTrashItems(p).contains(item)){
                e.getItem().remove();
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventory(@NotNull InventoryPickupItemEvent e) {
        for (HumanEntity he : e.getInventory().getViewers()) {
            if (he instanceof Player p && plugin.getAutoTrashHandler().getTrashItemsMat(p).contains(e.getItem().getItemStack().getType())) {
                e.getItem().remove();
            }
        }
    }
}

