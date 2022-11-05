package org.itzsave.handlers;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.itzsave.SaveUtils;

public class CustomCommandHandler implements Listener {

    SaveUtils plugin = SaveUtils.getPlugin(SaveUtils.class);

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCustomCommand(PlayerCommandPreprocessEvent event) {
        try {
            plugin.getConfig().getConfigurationSection("Custom-Commands.").getKeys(false).forEach(
                    command -> {
                        if (command.toLowerCase().equalsIgnoreCase(event.getMessage().split(" ")[0].replace("/", ""))) {
                            Player p = event.getPlayer();
                            event.setCancelled(true);
                            plugin.getConfig().getStringList("Custom-Commands." + command + ".message").forEach(line -> p.sendMessage(SaveUtils.color(PlaceholderAPI.setPlaceholders(p, (line)))));
                        }
                    }
            );
        } catch (NullPointerException ex) {
            plugin.getLogger().warning("Configuration path \"commands\" is null ");
        }

    }
}
