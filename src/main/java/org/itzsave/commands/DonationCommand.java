package org.itzsave.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.itzsave.SaveUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DonationCommand implements CommandExecutor {

    private final SaveUtils plugin;

    public DonationCommand(SaveUtils plugin){
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("donation")) {
            if (sender.hasPermission("savecore.admin")) {
                if (args.length == 0) {
                    sender.sendMessage(SaveUtils.color("Usage: /donate <player>"));
                    return true;
                } else {
                    Player target = Bukkit.getOnlinePlayers().stream().filter(p -> p.getName().equalsIgnoreCase(args[0])).findFirst().orElse(null);
                    if (target == null) {
                        sender.sendMessage(SaveUtils.color("&cWe could not find that user please try again.")); //.replace("%player%", target.getPlayer().getName()));
                    } else {
                        Bukkit.getOnlinePlayers().forEach(p -> {
                            for (String msg : plugin.getConfig().getStringList("Donation-Announcement")) {
                                p.sendMessage(SaveUtils.color(msg.replace("%player%", Objects.requireNonNull(target.getPlayer()).getName())));
                            }
                        });

                    }
                }

            } else {
                sender.sendMessage(SaveUtils.color(plugin.getLangFile().getString(("messages.no-permission"))));
            }
        }
        return false;
    }
}
