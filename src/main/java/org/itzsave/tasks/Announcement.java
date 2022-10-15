package org.itzsave.tasks;

import com.google.common.collect.Maps;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.itzsave.SaveUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Announcement implements Listener {

    SaveUtils plugin = SaveUtils.getPlugin(SaveUtils.class);

    private List<String> msg;

    private Map<Integer, Announcement> announcementIntegerMap;
    private int announcement = 0;


    public Announcement() {
        announcementIntegerMap = Maps.newHashMap();
        int index = 0;
        for (String a : Objects.requireNonNull(plugin.getConfig().getConfigurationSection("Announcements")).getKeys(false)) {
            announcementIntegerMap.put(index, new Announcement(a));
            index++;
        }
    }

    private BukkitTask task;


    public void register() {
        task = new BukkitRunnable() {


            @Override
            public void run() {
                if (announcement > announcementIntegerMap.size() - 1) {
                    announcement = 0;
                }
                announcementIntegerMap.get(announcement).displayMessage();
                announcement++;


            }
        }.runTaskTimerAsynchronously(plugin, plugin.getConfig().getInt("Settings.announcer-interval") * 20L,
                plugin.getConfig().getInt("Settings.announcer-interval") * 20L);
    }


    public void cancel() {
        if (task == null) return;
        task.cancel();
        announcementIntegerMap.clear();
        int index = 0;
        for (String a : Objects.requireNonNull(plugin.getConfig().getConfigurationSection("Announcements")).getKeys(false)) {
            announcementIntegerMap.put(index, new Announcement(a));
            index++;
        }
    }

    public Announcement(String key) {
        ConfigurationSection sec = plugin.getConfig().getConfigurationSection("Announcements." + key);
        msg = sec.getStringList("Message");
    }


    public void displayMessage() {
        msg.forEach(msg -> Bukkit.getOnlinePlayers().forEach(players -> players.sendMessage(SaveUtils.color(PlaceholderAPI.setPlaceholders(players, msg)))));
    }
}