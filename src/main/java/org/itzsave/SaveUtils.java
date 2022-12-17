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
import org.itzsave.commands.*;
import org.itzsave.handlers.AutoTrashHandler;
import org.itzsave.handlers.CustomCommandHandler;
import org.itzsave.handlers.PlaceholderHandler;
import org.itzsave.listeners.*;
import org.itzsave.tasks.Announcement;
import org.itzsave.tasks.AntiRaidFarm;
import org.itzsave.utils.ConfigManager;
import org.itzsave.utils.Messages;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public final class SaveUtils extends JavaPlugin implements Listener {

    private ConfigManager langFile;
    private Announcement announcements;
    public LuckPerms luckPerms;

    public HashMap<UUID, List<Material>> playerItems;

    private AutoTrashHandler autoTrashHandler;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().warning("--------- [SaveUtils] ---------");

        saveDefaultConfig();

        // Checking if purpur is installed.
        purpurCheck();

        this.playerItems = new HashMap<>();
        this.autoTrashHandler = new AutoTrashHandler(this);

        langFile = new ConfigManager(this, "lang");
        langFile.saveDefaultConfig();

        // Loading all modules.
        loadModules();

        Bukkit.getPluginManager().registerEvents(new EntityListener(this), this);

        CommandManager cm = new CommandManager(this);
        cm.getMessageHandler().register("cmd.no.permission", Messages.NO_PERMISSION::send);
        cm.getMessageHandler().register("cmd.no.console", Messages.NO_CONSOLE::send);
        cm.register(new AutoTrashCommand(this),
                new SaveUtilCommand(this),
                new NightvisionCommand(),
                new RuleCommand(),
                new DonationCommand(this));

        Stream.of(
                new IllegalBookCreationListener(),
                new ItemPickupListener(this),
                new AntiRaidFarm(this),
                new BedInteractEvent(),
                new PlayerListener()
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));

        // Checking if luckperms is installed before loading the chat module
        if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
            if (this.getConfig().getBoolean("Modules.enable-chat-formatting")) {
                Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
                this.luckPerms = getServer().getServicesManager().load(LuckPerms.class);
                getLogger().info("[Module] Loading chat formatting module.");
            }
        } else {
            getLogger().severe("WARNING: LuckPerms has not found! Please install it to enable chat formatting.");
            getConfig().set("Modules.enable-chat-formatting", false);
            saveConfig();
        }

        // Checking if PlaceholderAPI is installed.
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderHandler().register();
            getLogger().info("[Module] Loading internal PlaceholderAPI placeholders.");
            getLogger().info("[Module] Loading PlaceholderAPI message support.");
        } else {
            getLogger().severe("WARNING: PlaceholderAPI was not found support has not been enabled.");
        }
        getLogger().warning("--------- [SaveUtils] ---------");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public FileConfiguration getLangFile() {
        return langFile.getConfig();
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
        langFile.reload();

        loadModules();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderHandler().register();
        }

        if (this.getConfig().getBoolean("Modules.enable-announcer")) {
            getLogger().warning("Announcer is being reloaded.");
            announcements.cancel();
            loadAnnouncer();
            getLogger().info("Announcer has been reloaded");
        }
    }

    private void purpurCheck() {
        if (getConfig().getBoolean("Modules.enable-purpur-settings", false)) {
            try {
                Class.forName("org.purpurmc.purpur.PurpurConfig");
                getLogger().info("--------- [SaveUtils] ---------");
                getLogger().info("Detected Purpur now enabling all purpur features");
                getLogger().info("--------- [SaveUtils] ---------");
            } catch (ClassNotFoundException e) {
                getLogger().warning("--------- [SaveUtils] ---------");
                getLogger().warning("You are not running Purpur and have options enabled");
                getLogger().warning("that require it. Forcefully disabling them now.");
                getLogger().warning("--------- [SaveUtils] ---------");
                getConfig().set("Modules.enable-purpur-settings", false);
                saveConfig();
            }
        }
    }


    private void loadModules() {
        if (this.getConfig().getBoolean("Modules.enable-sleep-listener", false)) {
            getLogger().info("[Module] Loading sleep listener module.");
            Bukkit.getPluginManager().registerEvents(new SleepPercentageListener(this), this);
        }

        if (this.getConfig().getBoolean("Purpur-Settings.give-books-when-grindstone-disenchant", false)) {
            Bukkit.getPluginManager().registerEvents(new GrindstoneEnchantListener(), this);
            getLogger().info("[Module] Enabling grindstone disenchantment module.");
        }

        if (this.getConfig().getBoolean("Modules.custom-commands-enabled", true)) {
            Bukkit.getPluginManager().registerEvents(new CustomCommandHandler(), this);
            getLogger().info("[Module] Loading custom commands module.");
        }

        if (this.getConfig().getBoolean("Modules.enable-announcer", true)) {
            loadAnnouncer();
            getLogger().info("[Module] Loading auto announcer module.");
        }

        if (getConfig().getBoolean("Modules.enable-anti-void-death", true)) {
            Bukkit.getPluginManager().registerEvents(new VoidDamageListener(), this);
            getLogger().info("[Module] Loading anti void death module.");
        }

    }
}
