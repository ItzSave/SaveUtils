package org.itzsave.utils;

import org.bukkit.command.CommandSender;
import org.itzsave.SaveUtils;

import java.util.List;

@SuppressWarnings("unused")
public enum Messages {

    RELOADED("reloaded"),
    RULES("rules"),
    NO_PERMISSION("no-permission"),
    NO_CONSOLE("no-console"),
    HELP_MESSAGE("help-message"),
    ILLEGAL_BOOK("illegal-book"),
    NIGHTVISION_ENABLED("nightvision-enabled"),
    NIGHTVISION_DISABLED("nightvision-disabled"),
    BED_WARNING("bed-warning-message"),
    VOID_DAMAGE("void-damage");


    final SaveUtils plugin = SaveUtils.getPlugin(SaveUtils.class);

    private final String path;

    Messages(String path) {
        this.path = path;
    }

    public void send(CommandSender receiver, Object... replacements) {
        Object value = plugin.getLangFile().get("Messages." + this.path);
        String message;

        if (value == null) {
            message = "Warning: message not found (" + this.path + ")";
        } else {
            message = value instanceof List ? TextUtils.fromList((List<?>) value) : value.toString();
        }

        if (!message.isEmpty()) {
            receiver.sendMessage(SaveUtils.color(replace(message, replacements)));
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
