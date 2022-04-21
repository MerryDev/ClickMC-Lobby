package de.digitaldevs.lobby.utils;

import org.bukkit.ChatColor;

public class Colorizer {

    public static String colorizeAlternately(final String text, ChatColor first, ChatColor second) {
        final StringBuilder builder = new StringBuilder();
        boolean useFirstColor = true;
        for (char c : text.toCharArray()) {
            if (useFirstColor) {
                builder.append(first).append(c);
                useFirstColor = false;
            } else {
                builder.append(second).append(c);
                useFirstColor = true;
            }
        }
        return builder.toString();
    }

    public static String bold(final String text) {
        return ChatColor.BOLD + text;
    }

}
