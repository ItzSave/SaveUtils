package org.itzsave.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.itzsave.SaveCore;
import org.jetbrains.annotations.NotNull;

import java.io.ObjectInputFilter;

public class SaveCoreCommand implements CommandExecutor {

    private final SaveCore plugin;

    public SaveCoreCommand(SaveCore plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {

        if (command.getName().equalsIgnoreCase("savecore")) {
            if (args.length == 0) {
                for (String msg : plugin.getLangFile().getStringList("Messages.help-message")) {
                    sender.sendMessage(SaveCore.color(msg).replace("%version%", plugin.getDescription().getVersion()));
                }
            } else {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (sender.hasPermission("SaveCore.Admin")) {
                        sender.sendMessage(SaveCore.color(plugin.getLangFile().getString("Messages.reloaded")));
                        plugin.onReload();
                    }
                }
            }
        }
        return true;
    }
}
