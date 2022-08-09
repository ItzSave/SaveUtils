package org.itzsave;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.itzsave.commands.DonationCommand;
import org.itzsave.commands.NightvisionCommand;
import org.itzsave.commands.RuleCommand;
import org.itzsave.commands.SaveUtilCommand;
import org.itzsave.commands.autotrash.AutoTrash;
import org.itzsave.handlers.AutoTrashHandler;
import org.itzsave.listeners.*;
import org.itzsave.tasks.Announcement;
import org.itzsave.tasks.AntiRaidFarm;
import org.itzsave.utils.ConfigManager;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Stream;

public final class SaveUtils extends JavaPlugin implements Listener {

    private ConfigManager langfile;
    private Announcement announcements;

    public HashMap<UUID, List<Material>> playerItems;

    private AutoTrashHandler autoTrashHandler;

    @Override
    public void onEnable() {
        // Plugin startup logic


        saveDefaultConfig();

        this.playerItems = new HashMap<>();
        this.autoTrashHandler = new AutoTrashHandler(this);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.getLogger().log(Level.INFO, "PlaceholderAPI has been loaded support has been enabled.");
        } else {
            this.getLogger().log(Level.SEVERE, "--------- [SaveUtils] ---------");
            this.getLogger().log(Level.SEVERE, "PlaceholderAPI was not found support has not been enabled.");
            this.getLogger().log(Level.SEVERE, "--------- [SaveUtils] ---------");
        }


        try{
            Class.forName("org.purpurmc.purpur.PurpurConfig");
        } catch (ClassNotFoundException e) {
            getLogger().warning("--------- [SaveUtils] ---------");
            getLogger().warning("You are not running Purpur! Be sure to disable any config options that");
            getLogger().warning("require purpur or you will encounter errors and or crashes!");
            getLogger().warning("--------- [SaveUtils] ---------");
        }



        this.playerItems = new HashMap<>();

        if (this.getConfig().getBoolean("Settings.enable-announcer")) {
            loadAnnouncer();
        }

        langfile = new ConfigManager(this, "lang");
        langfile.saveDefaultConfig();

        this.registerCommand("saveutil", new SaveUtilCommand(this));
        this.registerCommand("rules", new RuleCommand());
        this.registerCommand("nightvision", new NightvisionCommand());
        this.registerCommand("donation", new DonationCommand(this));
        this.registerCommand("autotrash", new AutoTrash(this));

        if (this.getConfig().getBoolean("Purpur-Settings.give-books-when-grindstone-disenchant")){
            Bukkit.getPluginManager().registerEvents(new GrindstoneEnchantListener(), this);
            Bukkit.getLogger().log(Level.INFO, "[SaveUtils] Loading Purpur settings.");
        }

        Stream.of(
                new PlayerJoinListener(this),
                new IllegalBookCreationListener(this),
                new ItemPickupListener(this),
                new PlayerJoinListener(this),
                new PlayerLeaveListener(this),
                new WitherSpawnListener(this),
                new CustomCommandListener(this),
                new AntiRaidFarm(this),
                new BedInteractEvent(this)
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    private void registerCommand(String name, CommandExecutor executor) {
        //noinspection ConstantConditions
        (this.getCommand(name)).setExecutor(executor);
    }

    public FileConfiguration getLangFile() {
        return langfile.getConfig();
    }

    public static Component color(String message) {
        return MiniMessage.miniMessage().deserialize(message);
    }

    public void loadAnnouncer() {
        announcements = new Announcement();
        announcements.register();
    }

    public AutoTrashHandler getAutoTrashHandler() {
        return this.autoTrashHandler;
    }

    //Reload handler
    public void onReload() {
        reloadConfig();
        langfile.reload();
        if (this.getConfig().getBoolean("Settings.enable-announcer")) {
            Bukkit.getLogger().log(Level.INFO, "Announcer is being reloaded.");
            announcements.cancel();
            loadAnnouncer();
        }
    }
}
