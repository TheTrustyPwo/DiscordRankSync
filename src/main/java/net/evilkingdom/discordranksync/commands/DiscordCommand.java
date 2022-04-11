package net.evilkingdom.discordranksync.commands;

import net.dv8tion.jda.api.entities.User;
import net.evilkingdom.discordranksync.DiscordRankSync;
import net.evilkingdom.discordranksync.utils.StringUtilities;
import org.bukkit.Bukkit;
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
                if (args.length != 2) {
                    player.sendMessage(StringUtilities.colorize("&cUsage: /discord whois <player>"));
                    return false;
                }

                Player target = Bukkit.getPlayer(args[1]);

                if (target == null) {
                    player.sendMessage(this.plugin.getMessage("invalid_player"));
                    return false;
                }

                String userId = this.plugin.getPlayerManager().getPlayerUserCache().get(target.getUniqueId());
                if (userId == null) {
                    player.sendMessage(this.plugin.getMessage("whois_not_linked")
                            .replace("%player%", target.getName()));
                    return false;
                }

                User user = this.plugin.getJda().getUserById(userId);
                player.sendMessage(this.plugin.getMessage("whois_linked")
                        .replace("%player%", target.getName())
                        .replace("%name%", user.getName())
                        .replace("%discriminator%", user.getDiscriminator())
                        .replace("%id%", user.getId()));
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
