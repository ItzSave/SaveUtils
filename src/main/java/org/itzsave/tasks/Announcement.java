package org.itzsave.tasks;

import com.google.common.collect.Maps;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.itzsave.SaveUtils;
import org.itzsave.utils.DefaultFontInfo;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

public class Announcement implements Listener {

    SaveUtils plugin = SaveUtils.getPlugin(SaveUtils.class);

    private List<String> msg;

    private Map<Integer, Announcement> announcementIntegerMap;
    private int announcement = 0;
    private static final int CENTER_PX = 154;

    private static final int MAX_PX = 250;


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
        //noinspection ConstantConditions
        msg = sec.getStringList("Message");
    }


    public void displayMessage() {
        if (plugin.getConfig().getBoolean("Settings.center-announcements")) {
            msg.forEach(msg -> {
                Bukkit.getOnlinePlayers().forEach(players -> plugin.getChatCenter().sendCenteredMessage(players, msg));
                Bukkit.getLogger().log(Level.INFO, "Sending a centered announcement!");
            });
        } else {
            msg.forEach(msg -> {
                Bukkit.getOnlinePlayers().forEach(players -> players.sendMessage(SaveUtils.color(PlaceholderAPI.setPlaceholders(players, msg))));
            });
        }
    }

    private static void sendCenteredMessage(Player player, String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;
        int charIndex = 0;
        int lastSpaceIndex = 0;
        String toSendAfter = null;
        String recentColorCode = "";
        char[] arrayOfChar = message.toCharArray();
        int i = arrayOfChar.length;
        byte b = 0;
        while (true) {
            if (b < i) {
                char c = arrayOfChar[b];
                if (c == '§') {
                    previousCode = true;
                } else if (previousCode) {
                    previousCode = false;
                    recentColorCode = "§" + c;
                    if (c == 'l' || c == 'L') {
                        isBold = true;
                    } else {
                        isBold = false;
                        if (messagePxSize >= 250) {
                            toSendAfter = recentColorCode + message.substring(lastSpaceIndex + 1, message.length());
                            message = message.substring(0, lastSpaceIndex + 1);
                            break;
                        }
                    }
                } else {
                    if (c == ' ') {
                        lastSpaceIndex = charIndex;
                    } else {
                        DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                        messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                        messagePxSize++;
                    }
                    if (messagePxSize >= 250) {
                        toSendAfter = recentColorCode + message.substring(lastSpaceIndex + 1, message.length());
                        message = message.substring(0, lastSpaceIndex + 1);
                        break;
                    }
                }
            } else {
                break;
            }
            b++;
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = 154 - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        player.sendMessage(sb.toString() + message);
        if (toSendAfter != null)

            sendCenteredMessage(player, toSendAfter);
    }
}