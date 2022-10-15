package org.itzsave;

import me.mattstudios.mf.base.CommandManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.itzsave.commands.DonationCommand;
import org.itzsave.commands.NightvisionCommand;
import org.itzsave.commands.SaveUtilCommand;
import org.itzsave.commands.autotrash.AutoTrash;
import org.itzsave.handlers.AutoTrashHandler;
import org.itzsave.handlers.CustomCommandHandler;
import org.itzsave.handlers.PlaceholderHandler;
import org.itzsave.listeners.*;
import org.itzsave.tasks.Announcement;
import org.itzsave.tasks.AntiRaidFarm;
import org.itzsave.utils.ConfigManager;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
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
        getLogger().warning("--------- [SaveUtils] ---------");
        getLogger().info("Loading SaveUtils... v" + this.getDescription().getVersion());


        // Load luckperms
        this.luckPerms = getServer().getServicesManager().load(LuckPerms.class);

        saveDefaultConfig();

        // Checking if purpur is installed.
        purpurCheck();

        // Load all events.
        loadModules();

        this.playerItems = new HashMap<>();
        this.autoTrashHandler = new AutoTrashHandler(this);

        // Checking if PlaceholderAPI is installed.
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            getLogger().warning("PlaceholderAPI has located enabling placeholders...");
            new PlaceholderHandler().register();
            getLogger().info("Loaded PlaceholderAPI placeholders.");
        } else {
            getLogger().severe("--------- [SaveUtils] ---------");
            getLogger().severe("PlaceholderAPI was not found support has not been enabled.");
            getLogger().severe("--------- [SaveUtils] ---------");
        }

        if (this.getConfig().getBoolean("Settings.enable-announcer")) {
            loadAnnouncer();
        }

        langfile = new ConfigManager(this, "lang");
        langfile.saveDefaultConfig();

        CommandManager cm = new CommandManager(this);
        cm.register(new AutoTrash(this),
                new SaveUtilCommand(this),
                new NightvisionCommand(this),
                new DonationCommand(this));

        if (this.getConfig().getBoolean("Purpur-Settings.give-books-when-grindstone-disenchant", false)) {
            Bukkit.getPluginManager().registerEvents(new GrindstoneEnchantListener(), this);
            getLogger().info("[SaveUtils] Loading GrindstoneDisenchantmentListener.");
        }

        if (this.getConfig().getBoolean("Settings.custom-commands-enabled")) {
            Bukkit.getPluginManager().registerEvents(new CustomCommandHandler(this), this);
            getLogger().info("Enabling custom command handler!");
        }

        Stream.of(
                new IllegalBookCreationListener(this),
                new ItemPickupListener(this),
                new WitherSpawnListener(this),
                new AntiRaidFarm(this),
                new BedInteractEvent(this),
                new ChatListener(),
                new PlayerListener()
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
        getLogger().info(" ");
        getLogger().warning("--------- [SaveUtils] ---------");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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

    private void loadModules() {
        if (this.getConfig().getBoolean("Modules.enable-sleep-listener", false)) {
            getLogger().info("[SaveUtils] Loading Sleep Percentage Listener...");
            Bukkit.getPluginManager().registerEvents(new SleepPercentageListener(this), this);
        }
    }
}
