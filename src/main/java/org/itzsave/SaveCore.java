package org.itzsave;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.itzsave.commands.SaveCoreCommand;
import org.itzsave.listeners.PlayerJoinListener;
import org.itzsave.listeners.CustomCommandListener;
import org.itzsave.listeners.PlayerLeaveListener;
import org.itzsave.tasks.AnnouncerTask;
import org.itzsave.utils.ConfigManager;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SaveCore extends JavaPlugin implements Listener {

    private ConfigManager langfile;
    private AnnouncerTask announcements;

    @Override
    public void onEnable() {
        // Plugin startup logic

        saveDefaultConfig();

        if (this.getConfig().getBoolean("Settings.enable-announcer")) {
            loadAnnouncer();
        }

        langfile = new ConfigManager(this, "lang");
        langfile.saveDefaultConfig();


        this.registerCommand("savecore", new SaveCoreCommand(this));

        if (this.getConfig().getBoolean(("Settings.custom-commands-enabled"))) {
            this.registerEvents(new CustomCommandListener(this));
        }
        this.registerEvents(new PlayerJoinListener(this));
        this.registerEvents(new PlayerLeaveListener(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    @SuppressWarnings("unused")
    private void registerCommand(String name, CommandExecutor executor) {
        Objects.requireNonNull(this.getCommand(name)).setExecutor(executor);
    }

    private void registerEvents(Listener... listeners) {
        PluginManager pm = Bukkit.getPluginManager();
        for (Listener listener : listeners) {
            pm.registerEvents(listener, this);
        }
    }

    public FileConfiguration getLangFile() {
        return langfile.getConfig();
    }

    public final static Pattern HEX_PATTERN = Pattern.compile("#[a-fA-F0-9]{6}");

    @SuppressWarnings("deprecation")
    public static String color(String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuilder buffer = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(buffer, net.md_5.bungee.api.ChatColor.of(matcher.group()).toString());
        }
        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }

    public void loadAnnouncer() {
        announcements = new AnnouncerTask();
        announcements.register();
    }


    //Reload handler
    public void onReload(){
        reloadConfig();
        langfile.reload();
        if (this.getConfig().getBoolean("Settings.enable-announcer")) {
            announcements.cancel();
            announcements.register();
        }
    }
}
