package org.itzsave.tasks;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.raid.RaidTriggerEvent;
import org.itzsave.SaveUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/*
CREDIT: https://github.com/jpenilla/AntiRaidFarm
 */

@SuppressWarnings({"FieldMayBeFinal", "FieldCanBeLocal"})
public class AntiRaidFarm implements Listener {

    private Cache<UUID, Long> lastRaidCahce;
    private int raidCooldownSeconds = 180;

    private final SaveUtils plugin;

    public AntiRaidFarm(SaveUtils plugin) {
        this.plugin = plugin;
        this.lastRaidCahce = CacheBuilder.newBuilder().expireAfterAccess(raidCooldownSeconds, TimeUnit.SECONDS)
                .build();
        this.raidCooldownSeconds = plugin.getConfig().getInt("Settings.raid-cooldown-seconds", 180);
    }

    @EventHandler
    public void onRaidTrigger(RaidTriggerEvent event) {
        if (plugin.getConfig().getBoolean("Modules.disable-raid-farms", false)) {
            final Player player = event.getPlayer();
            if (player.hasPermission("saveutil.raidfarmbypass") || player.hasPermission("saveutils.admin")) {
                return;
            }
            final boolean hasCooldown = lastRaidCahce.getIfPresent(player.getUniqueId()) != null;
            if (hasCooldown) {
                event.setCancelled(true);
            } else {
                lastRaidCahce.put(player.getUniqueId(), System.currentTimeMillis());
            }
        }
    }
}
