package org.itzsave.commands;

import me.mattstudios.mf.annotations.Alias;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.itzsave.SaveUtils;
import org.jetbrains.annotations.NotNull;

@me.mattstudios.mf.annotations.Command("nightvision")
@Alias("nv")
@SuppressWarnings("unused")
public class NightvisionCommand extends CommandBase {

    private final SaveUtils plugin;

    public NightvisionCommand(SaveUtils plugin) {
        this.plugin = plugin;
    }

    @Default
    @Permission("saveutils.nightvision")
    public void onCommand(@NotNull CommandSender sender) {
        Player player = (Player) sender;
        if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
            player.sendMessage(SaveUtils.color(plugin.getLangFile().getString("Messages.nightvision-disabled")));
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        } else {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, true, false));
            player.sendMessage(SaveUtils.color(plugin.getLangFile().getString("Messages.nightvision-enabled")));
        }
    }
}

