package org.itzsave.commands;

import me.mattstudios.mf.annotations.Completion;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.itzsave.SaveUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@me.mattstudios.mf.annotations.Command("donation")
@SuppressWarnings("unused")
public class DonationCommand extends CommandBase {

    private final SaveUtils plugin;

    public DonationCommand(SaveUtils plugin) {
        this.plugin = plugin;
    }


    @Default
    @Permission("saveutils.admin")
    @Completion("#players")
    public void onCommand(@NotNull CommandSender sender, String[] args) {
                if (args.length == 0) {
                    sender.sendMessage(SaveUtils.color("Usage: /donate <player>"));
                } else {
                    Player target = Bukkit.getOnlinePlayers().stream().filter(p -> p.getName().equalsIgnoreCase(args[0])).findFirst().orElse(null);
                    if (target == null) {
                        sender.sendMessage(SaveUtils.color("<red>We could not find that user please try again."));
                    } else {
                        Bukkit.getOnlinePlayers().forEach(p -> {
                            for (String msg : plugin.getConfig().getStringList("Donation-Announcement")) {
                                p.sendMessage(SaveUtils.color(msg.replace("%player%", Objects.requireNonNull(target.getPlayer()).getName())));
                            }
                        });

                    }
                }

            }
        }
