package org.itzsave.utils;

import org.bukkit.command.CommandSender;
import org.itzsave.SaveUtils;

import java.util.List;

@SuppressWarnings("unused")
public enum Messages {

    RELOADED("reloaded", "<green>SaveUtils has been reloaded!"),
    RULES("rules", "<red>ERROR: This message is invalid please check your config. <gray>[rules]"),
    NO_PERMISSION("no-permission", ""),
    NO_CONSOLE("no-console", ""),
    HELP_MESSAGE("help-message", "<red>ERROR: This message is invalid please check your config. <gray>[help-message]"),
    ILLEGAL_BOOK("illegal-book", ""),
    NIGHTVISION_ENABLED("nightvision-enabled", ""),
    NIGHTVISION_DISABLED("nightvision-disabled", ""),
    VOID_DAMAGE("void-damage", ""),
    AUTO_TRASH_NULL_ITEM("null-item", "<red><bold>(!)</bold> <red>The item you input was invalid.");


    final SaveUtils plugin = SaveUtils.getPlugin(SaveUtils.class);

    private final String path;
    private final String def;


    Messages(String path, String def) {
        this.path = path;
        this.def = def;
    }

    public void send(CommandSender receiver, Object... replacements) {
        Object value = plugin.getLangFile().get("Messages." + this.path, this.def);
        String configMessage;

        if (value == null) {
            configMessage = "Warning: message not found (" + this.path + ")";
        } else {
            configMessage = value instanceof List ? TextUtils.fromList((List<?>) value) : value.toString();
        }

        if (!configMessage.isEmpty()) {
            receiver.sendMessage(SaveUtils.color(replace(configMessage, replacements)));
        }

    }

    private String replace(String message, Object... replacements) {
        for (int i = 0; i < replacements.length; i += 2) {
            if (i + 1 >= replacements.length) break;
            message = message.replace(String.valueOf(replacements[i]), String.valueOf(replacements[i + 1]));
        }

        return message;
    }

    public String getPath() {
        return this.path;
    }


}
