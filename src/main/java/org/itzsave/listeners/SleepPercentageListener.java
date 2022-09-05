package org.itzsave.listeners;


import io.papermc.paper.event.player.PlayerDeepSleepEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.TimeSkipEvent;
import org.itzsave.SaveUtils;

import java.util.List;

// Borrowed from https://github.com/RhythmicSys/PurpurExtras/blob/sleep-message/src/main/java/org/purpurmc/purpurextras/modules/SleepPercentageMessageModule.java
@SuppressWarnings({"SingleStatementInBlock", "FieldCanBeLocal", "ControlFlowStatementWithoutBraces"})
public class SleepPercentageListener implements Listener {
    private final String playerSleepMessage;
    private final String nightSkipMessage;
    private final String sleepMessageBypass = "saveutil.sleepmessagebypass";

    public SleepPercentageListener(SaveUtils plugin) {
        this.playerSleepMessage = plugin.getLangFile().getString("Messages.send-sleep-percentage-message.player-sleeping", "<grey><playername> has fallen asleep. <sleeping> out of <needed> required players in <worldname> are sleeping.");
        this.nightSkipMessage = plugin.getLangFile().getString("Messages.send-sleep-percentage-message.skipping-night", "<grey>Enough players have slept! Skipping through the night in <worldname>.");
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerDeepSleep(PlayerDeepSleepEvent event) {
        if (playerSleepMessage == null || playerSleepMessage.isBlank()) return;
        if (event.getPlayer().hasPermission(sleepMessageBypass)) return;
        World world = event.getPlayer().getWorld();
        String playerName = event.getPlayer().getName();
        String worldName = world.getName();
        int currentSleepCount = 0;
        int worldOnlineTotal = world.getPlayerCount();
        Integer worldSleepPercent = world.getGameRuleValue(GameRule.PLAYERS_SLEEPING_PERCENTAGE);
        Integer neededSleepers = (int) Math.ceil((worldSleepPercent / 100.0) * worldOnlineTotal);
        List<Player> playerList = world.getPlayers();
        for (Player player : playerList) {
            if (player.isDeeplySleeping()) currentSleepCount += 1;
        }
        world.sendMessage(MiniMessage.miniMessage().deserialize(playerSleepMessage,
                Placeholder.unparsed("playername", playerName),
                Placeholder.unparsed("sleeping", String.valueOf(currentSleepCount)),
                Placeholder.unparsed("needed", String.valueOf(neededSleepers)),
                Placeholder.unparsed("worldname", worldName)));
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void nightSkip(TimeSkipEvent event) {
        if (!event.getSkipReason().equals(TimeSkipEvent.SkipReason.NIGHT_SKIP)) return;
        if (nightSkipMessage == null || nightSkipMessage.isBlank()) return;
        String worldName = event.getWorld().getName();
        event.getWorld().sendMessage(MiniMessage.miniMessage().deserialize(nightSkipMessage, Placeholder.unparsed("worldname", worldName)));
    }
}
