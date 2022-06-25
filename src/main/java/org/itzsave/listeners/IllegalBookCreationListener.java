package org.itzsave.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.itzsave.SaveUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class IllegalBookCreationListener implements Listener {

    private final SaveUtils plugin;

    public IllegalBookCreationListener(SaveUtils plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onBookEdit(PlayerEditBookEvent e) {
        for (String bookPage : e.getNewBookMeta().getPages()) {
            if (!StandardCharsets.US_ASCII.newEncoder().canEncode(bookPage)) {
                e.getPlayer().sendMessage(SaveUtils.color(Objects.requireNonNull(plugin.getLangFile().getString("Messages.illegal-book"))));
                e.setCancelled(true);
            }
        }
    }
}
