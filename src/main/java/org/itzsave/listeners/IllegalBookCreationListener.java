package org.itzsave.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.itzsave.utils.Messages;

import java.nio.charset.StandardCharsets;

public class IllegalBookCreationListener implements Listener {

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onBookEdit(PlayerEditBookEvent e) {
        for (String bookPage : e.getNewBookMeta().getPages()) {
            if (!StandardCharsets.US_ASCII.newEncoder().canEncode(bookPage)) {
                Messages.ILLEGAL_BOOK.send(e.getPlayer());
                e.setCancelled(true);
            }
        }
    }
}
