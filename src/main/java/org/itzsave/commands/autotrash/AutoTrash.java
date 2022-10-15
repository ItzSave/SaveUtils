package org.itzsave.commands.autotrash;

import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.itzsave.SaveUtils;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings({"FieldCanBeLocal", "unused"})
@Command("autotrash")
public class AutoTrash extends CommandBase {

    private final SaveUtils plugin;

    public AutoTrash(SaveUtils plugin) {
        this.plugin = plugin;

    }

    @Default
    @Permission("saveutils.autotrash")
    public void execute(CommandSender sender) {
        Player p = (Player) sender;
        if (sender == null) return;

        for (String line : this.plugin.getLangFile().getStringList("Messages.HELP_AGE")) {
            p.sendMessage(SaveUtils.color(line));
        }
    }

    @SubCommand("add")
    public void addItem(CommandSender sender, String[] args) {
        Material item = Material.getMaterial(args[1].toUpperCase());
        Player player = (Player) sender;
        if (item == null) {
            sender.sendMessage(SaveUtils.color(plugin.getLangFile().getString("Messages.NOT_VALID_ITEM")));
        } else {
            plugin.getAutoTrashHandler().addAutoTrashItem(player, item);
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
    public void listItems(CommandSender sender, String[] args) {
        if (plugin.getAutoTrashHandler().getTrashItems((Player) sender) == null) {
            sender.sendMessage(SaveUtils.color(this.plugin.getLangFile().getString("Messages.TRASH_LIST_EMPTY")));
        } else {
            List<String> items = new ArrayList<>(plugin.getAutoTrashHandler().getTrashItems((Player) sender));
            String pl = String.join(", ", items);
            sender.sendMessage(SaveUtils.color(plugin.getLangFile().getString("Messages.TRASH_LIST_FORMAT").replace("%trashlist%", pl)));
        }
    }
}