package org.itzsave.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.itzsave.SaveUtils;
import org.itzsave.utils.SpawnerAPI;

import java.util.Random;

public class SpawnerBreakEvent implements Listener {

    private final SaveUtils plugin;

    public SpawnerBreakEvent(SaveUtils plugin) {
        this.plugin = plugin;
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreakEvent(BlockBreakEvent e) {

        if (!e.isCancelled()) {

            if (e.getBlock().getType() == Material.SPAWNER && e.getPlayer().hasPermission("spawnersilk.minespawner")) {

                Player p = e.getPlayer();

                if (p.getInventory().getItemInMainHand().getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {

                    int random = new Random().nextInt(100);

                    CreatureSpawner spawner = (CreatureSpawner) e.getBlock().getState();
                    EntityType entity = spawner.getSpawnedType();
                    ItemStack spawnerItem = SpawnerAPI.getSpawner(entity);

                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), spawnerItem);
                    e.setExpToDrop(0);
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent e) {

        if (e.getBlockPlaced().getType() == Material.SPAWNER) {
            CreatureSpawner cs = (CreatureSpawner) e.getBlockPlaced().getState();
            cs.setSpawnedType(SpawnerAPI.getEntityType(e.getItemInHand()));
            cs.update();
        }
    }

    @EventHandler
    public void playerRenameItem(InventoryClickEvent event) {

        if (event.getInventory().getType().equals(InventoryType.ANVIL)) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.SPAWNER) {
                event.getWhoClicked().sendMessage(ChatColor.RED + " You can't put that in an anvil");
                event.setResult(Event.Result.DENY);
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e) {

        if (plugin.getConfig().getBoolean("Settings.eggs-change-spawners")) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.SPAWNER) {
                    e.setCancelled(true);
                }
            }
        }
    }
}