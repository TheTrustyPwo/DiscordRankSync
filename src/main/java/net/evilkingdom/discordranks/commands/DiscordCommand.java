package net.evilkingdom.discordranks.commands;

import net.evilkingdom.discordranks.DiscordRankSync;
import net.evilkingdom.discordranks.utils.StringUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class DiscordCommand implements CommandExecutor {
    private final DiscordRankSync plugin;
    private Map<UUID, String> secretCodes;

    public DiscordCommand(DiscordRankSync plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return false;

        if (args.length == 0) {
            List<String> message = this.plugin.getConfig().getStringList("messages.discord");
            sender.sendMessage(StringUtilities.colorize(message));
        }

        switch (args[0].toUpperCase(Locale.ROOT)) {
            case "LINK": {

            } case "UNLINK": {

            }
        }

        return false;
    }
}
