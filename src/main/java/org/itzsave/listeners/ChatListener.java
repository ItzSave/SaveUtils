package org.itzsave.listeners;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// Yoinked from https://github.com/wikmor/LPC
// Help from https://gist.github.com/ItsSimplyLeo/e4eca09ba499b54f906428df33054113#file-basicchatrender-java
@SuppressWarnings("FieldCanBeLocal")
public class ChatListener implements Listener {

    private final SaveUtils plugin;

    public ChatListener(SaveUtils plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.renderer(new SaveUtilChatRenderer());
    }

    @SuppressWarnings({"CollectionAddAllCanBeReplacedWithConstructor", "ConstantConditions"})
    static class SaveUtilChatRenderer implements ChatRenderer {

        SaveUtils plugin = SaveUtils.getPlugin(SaveUtils.class);

        @Override
        public @NotNull
        Component render(@NotNull Player source, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
            List<TagResolver> resolverList = new ArrayList<>();

            final CachedMetaData metaData = plugin.luckPerms.getPlayerAdapter(Player.class).getMetaData(source);
            final String group = metaData.getPrimaryGroup();

            @Nullable String format = plugin.getConfig().getString(plugin.getConfig().getString("group-formats." + group) != null ? "group-formats." + group : "chat-format")
                    .replace("{prefix}", metaData.getPrefix() != null ? metaData.getPrefix() : "")
                    .replace("{suffix}", metaData.getSuffix() != null ? metaData.getSuffix() : "")
                    .replace("{name}", source.getName());

            resolverList.addAll(List.of(Placeholder.component("message", message)));

            return MiniMessage.miniMessage().deserialize(format + "<message>", TagResolver.resolver(resolverList));
        }
    }


}
