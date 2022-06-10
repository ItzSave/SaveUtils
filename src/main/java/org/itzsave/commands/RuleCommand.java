package org.itzsave.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.itzsave.SaveUtils;
import org.jetbrains.annotations.NotNull;

public class RuleCommand implements CommandExecutor {

    SaveUtils plugin = SaveUtils.getPlugin(SaveUtils.class);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(SaveUtils.color("&cThs command can only be run from in-game"));
        } else {
            if (command.getName().equalsIgnoreCase("rules")) {
                for (String msg : plugin.getLangFile().getStringList("Messages.rules")) {
                    sender.sendMessage(SaveUtils.color(msg));
                }
            }
        }
        return false;
    }
}
