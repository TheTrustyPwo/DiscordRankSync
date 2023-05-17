package net.evilkingdom.discordranksync.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscordTabComplete implements TabCompleter {
    private static final List<String> playerCommands = Arrays.asList("help", "link", "unlink", "chat", "whois");

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            final List<String> commands = new ArrayList<>(playerCommands);
            if (sender.hasPermission("drs.reload")) commands.add("reload");
            if (sender.hasPermission("drs.update")) commands.add("update");
            return commands;
        }

        return null;
    }
}
