package net.evilkingdom.discordranksync.commands;

import net.dv8tion.jda.api.EmbedBuilder;
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
        if (args.length == 0) {
            sender.sendMessage(this.plugin.getMessage("default"));
            return true;
        }

        String arg = args[0].toUpperCase(Locale.ROOT);

        if (arg.equals("HELP")) {
            sender.sendMessage(this.plugin.getMessage("help"));
        } else if (arg.equals("LINK")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(this.plugin.getMessage("only-players"));
                return false;
            }

            Player player = (Player) sender;
            String userId = this.plugin.getPlayerManager().getDiscordId(player.getUniqueId());

            if (userId != null) {
                this.plugin.getJda().retrieveUserById(userId).queue(user -> player.sendMessage(this.plugin.getMessage("already_linked")
                        .replace("%name%", user.getName())
                        .replace("%discriminator%", user.getDiscriminator())
                        .replace("%id%", user.getId())));
                return false;
            }

            String code = this.plugin.getPlayerManager().giveCode(player.getUniqueId());
            player.sendMessage(this.plugin.getMessage("link")
                    .replace("%code%", code));
        } else if (arg.equals("UNLINK")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(this.plugin.getMessage("only-players"));
                return false;
            }

            Player player = (Player) sender;
            String userId = this.plugin.getPlayerManager().getDiscordId(player.getUniqueId());

            if (userId == null) {
                player.sendMessage(this.plugin.getMessage("not_linked"));
                return false;
            }

            this.plugin.getPlayerManager().unlink(player.getUniqueId());

            this.plugin.getJda().retrieveUserById(userId).queue(user -> {
                player.sendMessage(this.plugin.getMessage("unlink")
                        .replace("%name%", user.getName())
                        .replace("%discriminator%", user.getDiscriminator())
                        .replace("%id%", user.getId()));

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.copyFrom(this.plugin.getEmbed("unlink_success"));
                embedBuilder.setDescription(embedBuilder.getDescriptionBuilder().toString().replace("%player-name%", player.getName()));

                user.openPrivateChannel().flatMap(channel -> channel.sendMessageEmbeds(embedBuilder.build())).queue();
            });
        } else if (arg.equals("WHOIS")) {
            if (args.length != 2) {
                sender.sendMessage(StringUtilities.colorize("&cUsage: /discord whois <player>"));
                return false;
            }

            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                sender.sendMessage(this.plugin.getMessage("invalid_player"));
                return false;
            }

            String userId = this.plugin.getPlayerManager().getDiscordId(target.getUniqueId());
            if (userId == null) {
                sender.sendMessage(this.plugin.getMessage("whois_not_linked")
                        .replace("%player%", target.getName()));
                return false;
            }

            this.plugin.getJda().retrieveUserById(userId).queue(user -> sender.sendMessage(this.plugin.getMessage("whois_linked")
                    .replace("%player%", target.getName())
                    .replace("%name%", user.getName())
                    .replace("%discriminator%", user.getDiscriminator())
                    .replace("%id%", user.getId())));
        } else if (arg.equals("CHAT")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(this.plugin.getMessage("only-players"));
                return false;
            }

            Player player = (Player) sender;
            boolean status = this.plugin.getPlayerManager().isChatToggled(player);

            if (status) {
                this.plugin.getPlayerManager().untoggleChat(player);
                player.sendMessage(this.plugin.getMessage("chat-disabled"));
            } else {
                this.plugin.getPlayerManager().toggleChat(player);
                player.sendMessage(this.plugin.getMessage("chat-enabled"));
            }
        } else if (arg.equals("RELOAD")) {
            if (!sender.hasPermission("drs.reload")) {
                sender.sendMessage(this.plugin.getMessage("no_permission"));
                return false;
            }

            this.plugin.reload();
            sender.sendMessage(this.plugin.getMessage("reload"));
        } else if (arg.equals("UPDATE")) {
            if (!sender.hasPermission("drs.update")) {
                sender.sendMessage(this.plugin.getMessage("no_permission"));
                return false;
            }

            this.plugin.getPlayerManager().updateRole();
            sender.sendMessage(this.plugin.getMessage("update"));
        }


        return false;
    }
}
