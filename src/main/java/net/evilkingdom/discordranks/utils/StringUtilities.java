package net.evilkingdom.discordranks.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtilities {

    /**
     * Allows you to colorize a string.
     *
     * @param string ~ The string that needs to be colorized.
     * @return The colorized string.
     */
    public static String colorize(String string) {
        final Pattern pattern = Pattern.compile("&#(\\w{5}[0-9a-f])");
        final Matcher matcher = pattern.matcher(string);
        final StringBuilder buffer = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(buffer, ChatColor.of("#" + matcher.group(1)).toString());
        }
        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }

    /**
     * Allows you to colorize a list of strings.
     *
     * @param list ~ The list of strings that needs to be colorized.
     * @return The colorized string list joined, each in a new line.
     */
    public static String colorize(List<String> list) {
        List<String> colorizedList = new ArrayList<>();
        list.forEach(string -> colorizedList.add(colorize(string)));
        return String.join("\n", colorizedList);
    }

}
