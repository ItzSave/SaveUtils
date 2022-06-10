package org.itzsave.tasks;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.itzsave.SaveUtils;

import java.util.List;

public class Announcement {

    SaveUtils plugin = SaveUtils.getPlugin(SaveUtils.class);

    private String key;
    private List<String> msg;
    private ConfigurationSection sec;

    public Announcement(String key) {
        this.key = key;
        sec = plugin.getConfig().getConfigurationSection("Announcements." + key);
        msg = sec.getStringList("Message");
    }


    public void displayMessage() {
        msg.forEach(msg -> {
            msg = SaveUtils.color(msg);
            String finalMsg = msg;

            Bukkit.getOnlinePlayers().forEach(players -> players.sendMessage(PlaceholderAPI.setPlaceholders(players, finalMsg)));
        });
    }
}