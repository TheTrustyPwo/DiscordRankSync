package net.evilkingdom.discordranks.events.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.evilkingdom.discordranks.DiscordRankSync;
import org.bukkit.Bukkit;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
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
            event.replyEmbeds(this.plugin.getEmbed("invalid_code")).queue();
            return;
        }

        UUID uuid = this.plugin.getPlayerManager().getSecretCodes().get(code);
        String playerName = Bukkit.getOfflinePlayer(uuid).getName();

        if (this.plugin.getPlayerManager().getPlayerUserCache().containsValue(event.getUser().getId())) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.copyFrom(this.plugin.getEmbed("already_linked"));
            embedBuilder.setDescription(embedBuilder.getDescriptionBuilder().toString().replace("%player-name%", playerName));
            event.replyEmbeds(embedBuilder.build()).queue();
            return;
        }

        this.plugin.getPlayerManager().getSecretCodes().remove(code);
        this.plugin.getPlayerManager().link(uuid, event.getUser().getId());

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.copyFrom(this.plugin.getEmbed("link_success"));
        embedBuilder.setDescription(embedBuilder.getDescriptionBuilder().toString().replace("%player-name%", playerName));
        embedBuilder.setThumbnail(this.plugin.getConfig().getString("messages.discord.link_success.thumbnail")
                .replace("%player-name%", playerName));

        event.replyEmbeds(embedBuilder.build()).queue();
    }
}
