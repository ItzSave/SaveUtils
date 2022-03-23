package org.itzsave.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.itzsave.SaveCore;

import java.util.Objects;

public class PlayerJoinListener implements Listener {

    private final SaveCore plugin;

    public PlayerJoinListener(SaveCore plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!plugin.getConfig().getBoolean("Settings.join-message-enabled")) {
            e.joinMessage(Component.text(""));
        }

        if (plugin.getConfig().getBoolean("Settings.replace-join-message")) {

            e.joinMessage(Component.text(Objects.requireNonNull(plugin.getLangFile().getString("")).replace("%player%", e.getPlayer().getName())));
        }

        if (plugin.getConfig().getBoolean("custom-join-messages-enabled")){
            for (String key : Objects.requireNonNull(plugin.getConfig().getConfigurationSection("join-messages")).getKeys(false)) {
                if (e.getPlayer().hasPermission(Objects.requireNonNull(plugin.getConfig().getString("join-messages." + key + ".permission")))){
                    Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(Objects.requireNonNull(plugin.getConfig().getString("join-messages." + key + ".message")).replace("%player%", e.getPlayer().getName())));                    break;
                }
            }
        }


    }

}
