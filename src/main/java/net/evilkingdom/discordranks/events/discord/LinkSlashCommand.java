package net.evilkingdom.discordranks.events.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.evilkingdom.discordranks.DiscordRankSync;
import org.bukkit.Bukkit;

import java.util.Date;
import java.util.UUID;

public class LinkSlashCommand extends ListenerAdapter {
    private final DiscordRankSync plugin;

    public LinkSlashCommand(DiscordRankSync plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("link")) return;

        String code = event.getOption("code").getAsString();

        if (!this.plugin.getPlayerManager().getSecretCodes().containsKey(code)) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(this.plugin.getConfig().getString("messages.discord.link_invalid.title"));
            embedBuilder.setDescription(this.plugin.getConfig().getString("messages.discord.link_invalid.description"));
            embedBuilder.setColor(Integer.parseInt(this.plugin.getConfig().getString("messages.discord.link_invalid.color"), 16));
            if (this.plugin.getConfig().getBoolean("messages.discord.link_invalid.timestamp")) {
                embedBuilder.setTimestamp(new Date().toInstant());
            }
            event.replyEmbeds(embedBuilder.build()).queue();
            return;
        }

        UUID uuid = this.plugin.getPlayerManager().getSecretCodes().get(code);
        String playerName = Bukkit.getOfflinePlayer(uuid).getName();

        this.plugin.getPlayerManager().getSecretCodes().remove(code);
        this.plugin.getPlayerManager().link(uuid, event.getUser().getId());

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(this.plugin.getConfig().getString("messages.discord.link_success.title"));
        embedBuilder.setDescription(this.plugin.getConfig().getString("messages.discord.link_success.description")
                .replace("%player-name%", playerName));
        embedBuilder.setColor(Integer.parseInt(this.plugin.getConfig().getString("messages.discord.link_success.color"), 16));
        if (this.plugin.getConfig().getBoolean("messages.discord.link_success.timestamp")) {
            embedBuilder.setTimestamp(new Date().toInstant());
        }

        event.replyEmbeds(embedBuilder.build()).queue();
    }
}
