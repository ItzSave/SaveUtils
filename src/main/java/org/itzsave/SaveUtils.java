package org.itzsave;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.itzsave.commands.*;
import org.itzsave.commands.autotrash.AutoTrash;
import org.itzsave.events.IllegalBookCreationEvent;
import org.itzsave.events.ItemPickupEvent;
import org.itzsave.events.SpawnerBreakEvent;
import org.itzsave.listeners.CustomCommandListener;
import org.itzsave.listeners.PlayerJoinListener;
import org.itzsave.listeners.PlayerLeaveListener;
import org.itzsave.listeners.WitherSpawnListener;
import org.itzsave.tasks.Announcement;
import org.itzsave.utils.ConfigManager;

import java.util.*;

public final class SaveUtils extends JavaPlugin implements Listener {

    private ConfigManager langfile;
    private Announcement announcements;
    private static SaveUtils instance;

    private HashMap<UUID, List<Material>> playerItems;

    @Override
    public void onEnable() {
        // Plugin startup logic

        saveDefaultConfig();

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
        //this.registerCommand("givespawner", new GiveSpawnerCommand(this));

        if (this.getConfig().getBoolean(("Settings.custom-commands-enabled"))) {
            this.registerEvents(new CustomCommandListener(this));
        }
        this.registerEvents(new PlayerJoinListener(this));
        this.registerEvents(new PlayerLeaveListener(this));
        this.registerEvents(new IllegalBookCreationEvent(this));
        this.registerEvents(new ItemPickupEvent(this));
        this.registerEvents(new WitherSpawnListener(this));
        this.registerEvents(new SpawnerBreakEvent(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    private void registerCommand(String name, CommandExecutor executor) {
        //noinspection ConstantConditions
        (this.getCommand(name)).setExecutor(executor);
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

    public static Component color(String message) {
        return MiniMessage.miniMessage().deserialize(message);
    }

    public void loadAnnouncer() {
        announcements = new Announcement();
        announcements.register();
    }


    //Reload handler
    public void onReload() {
        reloadConfig();
        langfile.reload();
        if (this.getConfig().getBoolean("Settings.enable-announcer")) {
            announcements.cancel();
            announcements.register();
        }
    }

    public void addAutoTrashItem(Player p, Material item) {
        if (!this.playerItems.containsKey(p.getUniqueId())) {
            List<Material> temp = new ArrayList<>();
            temp.add(item);
            this.playerItems.put(p.getUniqueId(), temp);
        } else {
            List<Material> temp = new ArrayList<>(this.playerItems.get(p.getUniqueId()));
            temp.add(item);
            this.playerItems.remove(p.getUniqueId());
            this.playerItems.put(p.getUniqueId(), temp);
        }
    }

    public void remAutoTrashItem(Player p, Material item) {
        if (this.playerItems.containsKey(p.getUniqueId()) && this.playerItems.get(p.getUniqueId()).contains(item)) {
            List<Material> temp = new ArrayList<>(this.playerItems.get(p.getUniqueId()));
            temp.remove(item);
            this.playerItems.remove(p.getUniqueId());
            this.playerItems.put(p.getUniqueId(), temp);
        }
    }

    public List<String> getTrashItems(Player p) {
        if (!this.playerItems.containsKey(p.getUniqueId()))
            return null;
        List<String> ret = new ArrayList<>();
        for (Material item : this.playerItems.get(p.getUniqueId()))
            ret.add(item.name());
        return ret;
    }

    public List<Material> getTrashItemsMat(Player p) {
        if (!this.playerItems.containsKey(p.getUniqueId()))
            return null;
        return new ArrayList<>(this.playerItems.get(p.getUniqueId()));
    }

    public void resetTrashItems(Player p) {
        //noinspection RedundantCollectionOperation
        if (this.playerItems.containsKey(p.getUniqueId()))
            this.playerItems.remove(p.getUniqueId());
    }
}
