package org.itzsave.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.itzsave.SaveUtils;
import org.jetbrains.annotations.NotNull;

public class SaveUtilCommand implements CommandExecutor {

    private final SaveUtils plugin;

    public SaveUtilCommand(SaveUtils plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {

        if (command.getName().equalsIgnoreCase("saveutil")) {
            if (args.length == 0) {
                for (String msg : plugin.getLangFile().getStringList("Messages.help-message")) {
                    sender.sendMessage(SaveUtils.color(msg.replace("%version%", plugin.getDescription().getVersion())));
                }
            } else {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (sender.hasPermission("saveutil.admin")) {
                        sender.sendMessage(SaveUtils.color(plugin.getLangFile().getString("Messages.reloaded")));
                        plugin.onReload();

                    }
                }
            }
        }
        return true;
    }
}
