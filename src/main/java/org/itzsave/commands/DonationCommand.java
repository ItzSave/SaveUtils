package org.itzsave.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.itzsave.SaveCore;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DonationCommand implements CommandExecutor {

    private final SaveCore plugin;

    public DonationCommand(SaveCore plugin){
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("donation")) {
            if (sender.hasPermission("savecore.admin")) {
                if (args.length == 0) {
                    sender.sendMessage(SaveCore.color("Usage: /donate <player>"));
                    return true;
                } else {
                    Player target = Bukkit.getOnlinePlayers().stream().filter(p -> p.getName().equalsIgnoreCase(args[0])).findFirst().orElse(null);
                    if (target == null) {
                        sender.sendMessage(SaveCore.color("&cWe could not find that user please try again.")); //.replace("%player%", target.getPlayer().getName()));
                    } else {
                        Bukkit.getOnlinePlayers().forEach(p -> {
                            for (String msg : plugin.getConfig().getStringList("Donation-Announcement")) {
                                p.sendMessage(SaveCore.color(msg.replace("%player%", target.getPlayer().getName())));
                                //break;
                            }
                        });

                    }
                }

            } else {
                sender.sendMessage(plugin.getLangFile().getString(SaveCore.color("messages.no-permission")));
            }
        }
        return false;
    }
}
