package org.itzsave.listeners;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.itzsave.SaveUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// Some methods from https://github.com/wikmor/LPC
// Lots of help from https://gist.github.com/ItsSimplyLeo/e4eca09ba499b54f906428df33054113#file-basicchatrender-java
public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.renderer(new SaveUtilChatRenderer());
    }

    @SuppressWarnings({"CollectionAddAllCanBeReplacedWithConstructor", "ConstantConditions", "RedundantCollectionOperation"})
    static class SaveUtilChatRenderer implements ChatRenderer {

        SaveUtils plugin = SaveUtils.getPlugin(SaveUtils.class);

        @Override
        public @NotNull
        Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
            List<TagResolver> resolverList = new ArrayList<>();

            final CachedMetaData metaData = plugin.luckPerms.getPlayerAdapter(Player.class).getMetaData(source);
            final String group = metaData.getPrimaryGroup();

            @Nullable String format = plugin.getConfig().getString(plugin.getConfig().getString("Chat-Formats.group-formats." + group) != null ? "Chat-Formats.group-formats." + group : "Chat-Formats.default")
                    .replace("{prefix}", metaData.getPrefix() != null ? metaData.getPrefix() : "")
                    .replace("{suffix}", metaData.getSuffix() != null ? metaData.getSuffix() : "")
                    .replace("{name}", source.getName());

            format = plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI") ? PlaceholderAPI.setPlaceholders(source, format) : format;

            resolverList.addAll(List.of(Placeholder.component("message", message)));

            return MiniMessage.miniMessage().deserialize(format + "<message>", TagResolver.resolver(resolverList));
        }
    }
}

