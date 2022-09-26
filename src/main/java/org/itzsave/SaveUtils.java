package org.itzsave;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
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
    public LuckPerms luckPerms;

    public HashMap<UUID, List<Material>> playerItems;

    private AutoTrashHandler autoTrashHandler;

    @Override
    public void onEnable() {
        // Plugin startup logic

        // Load luckperms
        this.luckPerms = getServer().getServicesManager().load(LuckPerms.class);

        saveDefaultConfig();

        // Checking if purpur is installed.
        purpurCheck();

        this.playerItems = new HashMap<>();
        this.autoTrashHandler = new AutoTrashHandler(this);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.getLogger().log(Level.INFO, "PlaceholderAPI has been loaded support has been enabled.");
        } else {
            this.getLogger().log(Level.SEVERE, "--------- [SaveUtils] ---------");
            this.getLogger().log(Level.SEVERE, "PlaceholderAPI was not found support has not been enabled.");
            this.getLogger().log(Level.SEVERE, "--------- [SaveUtils] ---------");
        }

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

        if (this.getConfig().getBoolean("Purpur-Settings.give-books-when-grindstone-disenchant")) {
            Bukkit.getPluginManager().registerEvents(new GrindstoneEnchantListener(), this);
            Bukkit.getLogger().log(Level.INFO, "[SaveUtils] Loading GrindstoneDisenchantmentListener.");
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
                new BedInteractEvent(this),
                new SleepPercentageListener(this),
                new ChatListener()
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
            getLogger().info("Announcer is being reloaded.");
            announcements.cancel();
            loadAnnouncer();
        }
    }

    private void purpurCheck() {
        if (this.getConfig().getBoolean("Settings.enable-purpur-settings", false)) {
            try {
                Class.forName("org.purpurmc.purpur.PurpurConfig");
                getLogger().info("--------- [SaveUtils] ---------");
                getLogger().info("Detected Purpur now enabling all purpur features...");
                getLogger().info("--------- [SaveUtils] ---------");
            } catch (ClassNotFoundException e) {
                getLogger().warning("--------- [SaveUtils] ---------");
                getLogger().warning("You are not running Purpur and have options enabled");
                getLogger().warning("that require it. Forcefully disabling them now.");
                getLogger().warning("--------- [SaveUtils] ---------");
                this.getConfig().set("Settings.enable-purpur-settings", false);
                saveConfig();
            }
        }


    }
}
