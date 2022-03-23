package org.itzsave.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.itzsave.SaveCore;
import org.jetbrains.annotations.NotNull;

public class NightvisionCommand implements CommandExecutor {


    SaveCore plugin = SaveCore.getPlugin(SaveCore.class);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(SaveCore.color("&cThs command can only be run from in-game"));
        } else {
            if (command.getName().equalsIgnoreCase("nightvision")) {
                if (player.hasPermission("savecore.nightvision")) {
                    if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                        player.sendMessage(SaveCore.color(plugin.getLangFile().getString("Messages.nightvision-disabled")));
                        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                        return false;
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, true, false));
                    player.sendMessage(SaveCore.color(plugin.getLangFile().getString("Messages.nightvision-enabled")));
                    return true;
                }
                player.sendMessage(SaveCore.color(plugin.getLangFile().getString("Messages.no-permission")));
            }
        }
        return true;
    }
}
