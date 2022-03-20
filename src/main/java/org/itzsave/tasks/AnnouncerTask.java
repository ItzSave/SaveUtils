package org.itzsave.tasks;


import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.itzsave.SaveCore;

import java.util.List;
import java.util.Map;

public class AnnouncerTask implements Listener {

    SaveCore plugin = SaveCore.getPlugin(SaveCore.class);

    private final Map<Integer, Announcement> announcementIntegerMap;
    private int announcement = 0;

    public AnnouncerTask() {
        announcementIntegerMap = Maps.newHashMap();
        int index = 0;
        for (String a : plugin.getConfig().getConfigurationSection("Announcements").getKeys(false)) {
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
        }.runTaskTimerAsynchronously(plugin, plugin.getConfig().getInt("Settoings.announcer-interval") * 20L,
                plugin.getConfig().getInt("Settings.announcer-interval") * 20L);
    }


    public void cancel() {
        if (task == null) return;
        task.cancel();
        announcementIntegerMap.clear();
        int index = 0;
        for (String a : plugin.getConfig().getConfigurationSection("Announcements").getKeys(false)) {
            announcementIntegerMap.put(index, new Announcement(a));
            index++;
        }
    }

    private String key;
    private List<String> msg;
    private ConfigurationSection sec;

    public void Announcement(String key) {
        this.key = key;
        sec = plugin.getConfig().getConfigurationSection("Announcements." + key);
        msg = sec.getStringList("Message");
    }


    public void displayMessage() {
        msg.forEach(msg -> {
            msg = SaveCore.color(msg);
            String finalMsg = msg;

            Bukkit.getOnlinePlayers().forEach(players -> players.sendMessage(finalMsg));
        });
    }


}
