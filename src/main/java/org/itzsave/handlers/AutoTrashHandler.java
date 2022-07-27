package org.itzsave.handlers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.itzsave.SaveUtils;

import java.util.ArrayList;
import java.util.List;


public class AutoTrashHandler {
    private SaveUtils plugin;

    public AutoTrashHandler(SaveUtils plugin) {
        this.plugin = plugin;
    }

    public void addAutoTrashItem(Player p, Material item) {
        if (!plugin.playerItems.containsKey(p.getUniqueId())) {
            List<Material> temp = new ArrayList<>();
            temp.add(item);
            plugin.playerItems.put(p.getUniqueId(), temp);
        } else {
            List<Material> temp = new ArrayList<>(plugin.playerItems.get(p.getUniqueId()));
            temp.add(item);
            plugin.playerItems.remove(p.getUniqueId());
            plugin.playerItems.put(p.getUniqueId(), temp);
        }
    }

    public void remAutoTrashItem(Player p, Material item) {
        if (plugin.playerItems.containsKey(p.getUniqueId()) && plugin.playerItems.get(p.getUniqueId()).contains(item)) {
            List<Material> temp = new ArrayList<>(plugin.playerItems.get(p.getUniqueId()));
            temp.remove(item);
            plugin.playerItems.remove(p.getUniqueId());
            plugin.playerItems.put(p.getUniqueId(), temp);
        }
    }

    public List<String> getTrashItems(Player p) {
        if (!plugin.playerItems.containsKey(p.getUniqueId()))
            return null;
        List<String> ret = new ArrayList<>();
        for (Material item : plugin.playerItems.get(p.getUniqueId()))
            ret.add(item.name());
        return ret;
    }

    public List<Material> getTrashItemsMat(Player p) {
        if (!plugin.playerItems.containsKey(p.getUniqueId()))
            return null;
        return new ArrayList<>(plugin.playerItems.get(p.getUniqueId()));
    }

    public void resetTrashItems(Player p) {
        //noinspection RedundantCollectionOperation
        if (plugin.playerItems.containsKey(p.getUniqueId()))
            plugin.playerItems.remove(p.getUniqueId());
    }
}
