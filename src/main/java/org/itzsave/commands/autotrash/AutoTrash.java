package org.itzsave.commands.autotrash;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.itzsave.SaveUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("ConstantConditions")
public class AutoTrash implements CommandExecutor {

    private final SaveUtils plugin;


    public AutoTrash(SaveUtils plugin) {
        this.plugin = plugin;

    }

    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command c, @NotNull String s, String[] args) {
        if (cs instanceof Player p) {
            if (p.hasPermission("autotrash.use") || p.hasPermission("autotrash.admin"))
                if (args.length == 0) {
                    for (String line : this.plugin.getLangFile().getStringList("Messages.HELP_PAGE"))
                        p.sendMessage(SaveUtils.color(line));
                } else if (args[0].equalsIgnoreCase("add")) {
                    if (args.length == 2) {
                        Material item = Material.getMaterial(args[1].toUpperCase());
                        if (item == null) {
                            p.sendMessage(SaveUtils.color(this.plugin.getLangFile().getString("Messages.NOT_VALID_ITEM")));
                        } else {
                            plugin.getAutoTrashHandler().addAutoTrashItem(p, item);
                            p.sendMessage(SaveUtils.color(this.plugin.getLangFile().getString("Messages.ITEM_ADD_SUCCESS").replace("%item%", item.name())));
                        }
                    } else {
                        p.sendMessage(SaveUtils.color(this.plugin.getLangFile().getString("Messages.NOT_ENOUGH_ARGS")));
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (args.length == 2) {
                        Material item = Material.getMaterial(args[1].toUpperCase());
                        if (item == null) {
                            p.sendMessage(SaveUtils.color(this.plugin.getLangFile().getString("Messages.NOT_VALID_ITEM")));
                        } else {
                            plugin.getAutoTrashHandler().remAutoTrashItem(p, item);
                            p.sendMessage(SaveUtils.color(this.plugin.getLangFile().getString("Messages.ITEM_REM_SUCCESS").replace("%item%", item.name())));
                        }
                    } else {
                        p.sendMessage(SaveUtils.color(this.plugin.getLangFile().getString("Messages.NOT_ENOUGH_ARGS")));
                    }
                } else if (args[0].equalsIgnoreCase("list")) {
                    if (plugin.getAutoTrashHandler().getTrashItems(p) == null) {
                        p.sendMessage(SaveUtils.color(this.plugin.getLangFile().getString("Messages.TRASH_LIST_EMPTY")));
                        return false;
                    }
                    List<String> items = new ArrayList<>(Objects.requireNonNull(plugin.getAutoTrashHandler().getTrashItems(p)));
                    String pl = String.join(", ", items);
                    p.sendMessage(SaveUtils.color(this.plugin.getLangFile().getString("Messages.TRASH_LIST_FORMAT").replace("%trashlist%", pl)));
                } else if (args[0].equalsIgnoreCase("reset")) {
                    plugin.getAutoTrashHandler().resetTrashItems(p);
                    p.sendMessage(SaveUtils.color(this.plugin.getLangFile().getString("Messages.TRASH_LIST_RESET")));
                } else {
                    p.sendMessage(SaveUtils.color(this.plugin.getLangFile().getString("Messages.NOT_VALID_ARG")));
                }
            return false;
        }
        cs.sendMessage("[AutoTrash] You cannot use this command from the console.");
        return false;
    }
}
