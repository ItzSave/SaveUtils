package org.itzsave.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.itzsave.SaveCore;
import org.jetbrains.annotations.NotNull;

public class MentionCommand implements CommandExecutor {

    private final SaveCore plugin;

    public MentionCommand(SaveCore plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return false;
    }
}
