package net.evilkingdom.discordranks.commands;

import net.evilkingdom.discordranks.DiscordRankSync;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class DiscordCommand implements CommandExecutor {
    private final DiscordRankSync plugin;

    public DiscordCommand(DiscordRankSync plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return false;

        if (args.length == 0) {
            player.sendMessage(this.plugin.getMessage("default"));
            return true;
        }

        switch (args[0].toUpperCase(Locale.ROOT)) {
            case "LINK" -> {
                if (this.plugin.getPlayerManager().getPlayerUserCache().containsKey(player.getUniqueId())) {
                    player.sendMessage(this.plugin.getMessage("already_linked"));
                    return false;
                }

                String code = this.plugin.getPlayerManager().giveCode(player.getUniqueId());
                player.sendMessage(this.plugin.getMessage("link")
                        .replace("%code%", code));
            }
            case "UNLINK" -> {
                if (!this.plugin.getPlayerManager().getPlayerUserCache().containsKey(player.getUniqueId())) {
                    player.sendMessage(this.plugin.getMessage("not_linked"));
                    return false;
                }

                this.plugin.getPlayerManager().unlink(player.getUniqueId());
                player.sendMessage(this.plugin.getMessage("unlink"));
            }
            case "WHOIS" -> {
                if (!this.plugin.getPlayerManager().getPlayerUserCache().containsKey(player.getUniqueId())) {
                    player.sendMessage(this.plugin.getMessage("whois_not_linked"));
                    return false;
                }

                player.sendMessage(this.plugin.getMessage("whois_linked"));
            }
            case "RELOAD" -> {
                if (!player.hasPermission("drs.reload")) {
                    player.sendMessage(this.plugin.getMessage("no_permission"));
                    return false;
                }

                this.plugin.reload();
                player.sendMessage(this.plugin.getMessage("reload"));
            }
        }

        return false;
    }
}
