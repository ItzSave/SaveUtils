package org.itzsave;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.itzsave.commands.DonationCommand;
import org.itzsave.commands.NightvisionCommand;
import org.itzsave.commands.RuleCommand;
import org.itzsave.commands.SaveUtilCommand;
import org.itzsave.commands.autotrash.AutoTrash;
import org.itzsave.listeners.*;
import org.itzsave.tasks.Announcement;
import org.itzsave.utils.ConfigManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public final class SaveUtils extends JavaPlugin implements Listener {

    private ConfigManager langfile;
    private Announcement announcements;

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

        Stream.of(
                new PlayerJoinListener(this),
                new IllegalBookCreationListener(this),
                new ItemPickupListener(this),
                new PlayerJoinListener(this),
                new PlayerLeaveListener(this),
                new WitherSpawnListener(this),
                new CustomCommandListener(this)
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