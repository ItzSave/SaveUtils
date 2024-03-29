package org.itzsave.commands;


import me.mattstudios.mf.annotations.Alias;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;
import org.itzsave.SaveUtils;
import org.itzsave.utils.Messages;
import org.jetbrains.annotations.NotNull;

@me.mattstudios.mf.annotations.Command("saveutil")
@Alias("saveutils")
@SuppressWarnings("unused")
public class SaveUtilCommand extends CommandBase {

    private final SaveUtils plugin;

    public SaveUtilCommand(SaveUtils plugin) {
        this.plugin = plugin;
    }


    @Default
    public void onCommand(@NotNull CommandSender sender, String[] args) {
        Messages.HELP_MESSAGE.send(sender, "%version%", plugin.getDescription().getVersion());
    }

    @SubCommand("reload")
    @Permission("saveutils.admin")
    public void reloadCommand(CommandSender sender) {
        if (sender.hasPermission("saveutil.admin")) {
            Messages.RELOADED.send(sender);
            plugin.onReload();
        }
    }
}
