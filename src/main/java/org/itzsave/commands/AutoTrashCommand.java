package org.itzsave.commands;

import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.itzsave.SaveUtils;
import org.itzsave.utils.Messages;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings({"FieldCanBeLocal", "unused"})
@Command("autotrash")
@Alias("at")
public class AutoTrashCommand extends CommandBase {

    private final SaveUtils plugin;

    public AutoTrashCommand(SaveUtils plugin) {
        this.plugin = plugin;
    }

    @Default
    @Permission("saveutils.autotrash")
    public void execute(CommandSender sender) {
        Player p = (Player) sender;
        if (sender == null) return;

        for (String line : this.plugin.getLangFile().getStringList("Messages.HELP_PAGE")) {
            p.sendMessage(SaveUtils.color(line));
        }
    }

    @SubCommand("add")
    public void addItem(Player sender, String[] args) {

        if (args[0].equals(null)) {
            Messages.AUTO_TRASH_NULL_ITEM.send(sender);
            return;
        }

        Material item = Material.getMaterial(args[1].toUpperCase());


        // STILL KNOWN TO THROW ERRORS
        if (item == null) {
            Messages.AUTO_TRASH_NULL_ITEM.send(sender);
        } else {
            plugin.getAutoTrashHandler().addAutoTrashItem(sender, item);
            sender.sendMessage(SaveUtils.color(plugin.getLangFile().getString("Messages.ITEM_ADD_SUCCESS").replace("%item%", item.name())));
        }
    }

    @SubCommand("remove")
    public void removeItem(CommandSender sender, String[] args) {
        Material item = Material.getMaterial(args[1].toUpperCase());
        Player player = (Player) sender;
        if (item == null) {
            sender.sendMessage(SaveUtils.color(plugin.getLangFile().getString("Messages.NOT_VALID_ITEM")));
        } else {
            plugin.getAutoTrashHandler().remAutoTrashItem(player, item);
            sender.sendMessage(SaveUtils.color(plugin.getLangFile().getString("Messages.ITEM_REM_SUCCESS").replace("%item%", item.name())));
        }
    }

    @SubCommand("reset")
    public void resetItems(CommandSender sender, String[] args) {
        plugin.getAutoTrashHandler().resetTrashItems((Player) sender);
        sender.sendMessage(SaveUtils.color(plugin.getLangFile().getString("Messages.TRASH_LIST_RESET")));
    }

    @SubCommand("list")
    public void listItems(Player sender, String[] args) {
        if (plugin.getAutoTrashHandler().getTrashItems(sender) == null) {
            sender.sendMessage(SaveUtils.color(this.plugin.getLangFile().getString("Messages.TRASH_LIST_EMPTY")));
        } else {
            List<String> items = new ArrayList<>(plugin.getAutoTrashHandler().getTrashItems(sender));
            String pl = String.join(", ", items);
            sender.sendMessage(SaveUtils.color(plugin.getLangFile().getString("Messages.TRASH_LIST_FORMAT").replace("%trashlist%", pl)));
        }
    }
}