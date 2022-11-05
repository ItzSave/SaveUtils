package org.itzsave.commands;

import me.mattstudios.mf.annotations.Alias;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;
import org.itzsave.utils.Messages;


@Command("rules")
@Alias("rule")
@SuppressWarnings("unused")
public class RuleCommand extends CommandBase {
    
    @Default
    public void execute(CommandSender sender) {
        Messages.RULES.send(sender);
    }
}
