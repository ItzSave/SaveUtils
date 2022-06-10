package org.itzsave.events;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.itzsave.SaveUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class IllegalBookCreationEvent implements Listener {

    private final SaveUtils plugin;

    public IllegalBookCreationEvent(SaveUtils plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onBookEdit(PlayerEditBookEvent e) {
        for (String bookPage : e.getNewBookMeta().getPages()) {
            if (!StandardCharsets.US_ASCII.newEncoder().canEncode(bookPage)) {
                e.getPlayer().sendMessage(Component.text(Objects.requireNonNull(SaveUtils.color(plugin.getLangFile().getString("Messages.illegal-book")))));
                e.setCancelled(true);
            }
        }
    }
}
