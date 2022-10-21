package org.itzsave.handlers;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.itzsave.SaveUtils;
import org.itzsave.utils.TextUtils;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class PlaceholderHandler extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "SaveUtils";
    }

    @Override
    public @NotNull String getAuthor() {
        return SaveUtils.getPlugin(SaveUtils.class).getDescription().getVersion();
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }


    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        Player p = player.getPlayer();
        if (params.equalsIgnoreCase("name")) {
            return player == null ? null : player.getName();
        }

        if (params.equalsIgnoreCase("exp")) {
            return TextUtils.numberFormat(p.getTotalExperience());
        }

        return null;
    }
}
