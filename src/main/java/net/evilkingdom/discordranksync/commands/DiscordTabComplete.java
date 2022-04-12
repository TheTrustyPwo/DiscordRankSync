package net.evilkingdom.discordranksync.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class DiscordTabComplete implements TabCompleter {
    private static final List<String> subCommands = Arrays.asList("help", "link", "unlink", "whois", "reload");

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return subCommands;
        }

        return null;
    }
}
