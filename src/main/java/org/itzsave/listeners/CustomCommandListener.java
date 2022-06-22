package org.itzsave.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.itzsave.SaveUtils;

import java.util.Objects;
import java.util.logging.Level;

public class CustomCommandListener implements Listener {

    private final SaveUtils plugin;

    public CustomCommandListener(SaveUtils plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCustomCommand(PlayerCommandPreprocessEvent e) {
        try {
            Objects.requireNonNull(plugin.getConfig().getConfigurationSection("commands.")).getKeys(false).forEach(
                    command -> {
                        if (command.toLowerCase().equalsIgnoreCase(e.getMessage().split(" ")[0].replace("/", ""))) {
                            e.setCancelled(true);
                            plugin.getConfig().getStringList("commands." + command + ".message").forEach(line -> e.getPlayer().sendMessage(SaveUtils.color(PlaceholderAPI.setPlaceholders(e.getPlayer(), (line)))));
                        }
                    }
            );
        } catch (NullPointerException ex) {
            plugin.getLogger().log(Level.WARNING, "Configuration path \"commands\" is null ");
        }

    }
}
