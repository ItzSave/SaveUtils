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

public class AutoTrash implements CommandExecutor {
    private final String EMPTY_TRASH_LIST;

    private final String TRASH_LIST_FORMAT;

    private final List<String> HELP_PAGE;

    private final String ITEM_ADD_SUCCESS;

    private final String ITEM_REM_SUCCESS;

    private final String NOT_ENOUGH_ARGS;

    private final String NOT_VALID_ITEM;

    private final String TRASH_LIST_RESET;

    private final String NO_PERMISSION;

    private final String RELOAD_MESSAGE;

    private final String NOT_VALID_ARG;

    private final SaveUtils plugin;

    public AutoTrash(SaveUtils plugin) {
        this.plugin = plugin;
        this.HELP_PAGE = this.plugin.getLangFile().getStringList("Messages.HELP_PAGE");
        this.ITEM_ADD_SUCCESS = this.plugin.getLangFile().getString("Messages.ITEM_ADD_SUCCESS");
        this.ITEM_REM_SUCCESS = this.plugin.getLangFile().getString("Messages.ITEM_REM_SUCCESS");
        this.NOT_ENOUGH_ARGS = this.plugin.getLangFile().getString("Messages.NOT_ENOUGH_ARGS");
        this.NOT_VALID_ITEM = this.plugin.getLangFile().getString("Messages.NOT_VALID_ITEM");
        this.EMPTY_TRASH_LIST = this.plugin.getLangFile().getString("Messages.TRASH_LIST_EMPTY");
        this.TRASH_LIST_FORMAT = this.plugin.getLangFile().getString("Messages.TRASH_LIST_FORMAT");
        this.TRASH_LIST_RESET = this.plugin.getLangFile().getString("Messages.TRASH_LIST_RESET");
        this.NO_PERMISSION = this.plugin.getLangFile().getString("Messages.no-permission");
        this.RELOAD_MESSAGE = this.plugin.getLangFile().getString("Messages.RELOAD_MESSAGE");
        this.NOT_VALID_ARG = this.plugin.getLangFile().getString("Messages.NOT_VALID_ARG");
    }

    public boolean onCommand(@NotNull CommandSender cs, @NotNull Command c, @NotNull String s, String[] args) {
        if (cs instanceof Player p) {
            if (p.hasPermission("autotrash.use") || p.hasPermission("autotrash.admin"))
                if (args.length == 0) {
                    for (String line : this.HELP_PAGE)
                        p.sendMessage(SaveUtils.color(line).replace("%label%", c.getLabel()));
                } else if (args[0].equalsIgnoreCase("add")) {
                    if (args.length == 2) {
                        Material item = Material.getMaterial(args[1].toUpperCase());
                        if (item == null) {
                            p.sendMessage(SaveUtils.color(this.NOT_VALID_ITEM));
                        } else {
                            this.plugin.addAutoTrashItem(p, item);
                            p.sendMessage(SaveUtils.color(this.ITEM_ADD_SUCCESS.replace("%item%", item.name())));
                        }
                    } else {
                        p.sendMessage(SaveUtils.color(this.NOT_ENOUGH_ARGS));
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (args.length == 2) {
                        Material item = Material.getMaterial(args[1].toUpperCase());
                        if (item == null) {
                            p.sendMessage(SaveUtils.color(this.NOT_VALID_ITEM));
                        } else {
                            this.plugin.remAutoTrashItem(p, item);
                            p.sendMessage(SaveUtils.color(this.ITEM_REM_SUCCESS.replace("%item%", item.name())));
                        }
                    } else {
                        p.sendMessage(SaveUtils.color(this.NOT_ENOUGH_ARGS));
                    }
                } else if (args[0].equalsIgnoreCase("list")) {
                    if (this.plugin.getTrashItems(p) == null) {
                        p.sendMessage(SaveUtils.color(this.EMPTY_TRASH_LIST));
                        return false;
                    }
                    List<String> items = new ArrayList<>(Objects.requireNonNull(this.plugin.getTrashItems(p)));
                    String pl = String.join(", ", items);
                    p.sendMessage(SaveUtils.color(this.TRASH_LIST_FORMAT.replace("%trashlist%", pl)));
                } else if (args[0].equalsIgnoreCase("reset")) {
                    this.plugin.resetTrashItems(p);
                    p.sendMessage(SaveUtils.color(this.TRASH_LIST_RESET));
                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (p.hasPermission("autotrash.admin")) {
                        this.plugin.reloadConfig();
                        p.sendMessage(SaveUtils.color(this.RELOAD_MESSAGE));
                    } else {
                        p.sendMessage(SaveUtils.color(this.NO_PERMISSION));
                    }
                } else {
                    p.sendMessage(SaveUtils.color(this.NOT_VALID_ARG));
                }
            return false;
        }
        cs.sendMessage("[AutoTrash] You cannot use this command from the console.");
        return false;
    }
}
