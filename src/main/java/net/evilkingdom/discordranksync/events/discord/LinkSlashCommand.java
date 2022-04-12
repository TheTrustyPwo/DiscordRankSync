package net.evilkingdom.discordranksync.events.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.evilkingdom.discordranksync.DiscordRankSync;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

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

        if (this.plugin.getPlayerManager().getPlayerUUIDFromCode(code) == null) {
            event.replyEmbeds(this.plugin.getEmbed("invalid_code")).queue();
            return;
        }

        UUID uuid = this.plugin.getPlayerManager().getPlayerUUIDFromCode(code);
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

        if (this.plugin.getPlayerManager().getPlayerUUID(event.getUser().getId()) != null) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.copyFrom(this.plugin.getEmbed("already_linked"));
            embedBuilder.setDescription(embedBuilder.getDescriptionBuilder().toString().replace("%player-name%", player.getName()));
            event.replyEmbeds(embedBuilder.build()).queue();
            return;
        }

        this.plugin.getPlayerManager().removeCode(code);
        this.plugin.getPlayerManager().link(uuid, event.getUser().getId());

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.copyFrom(this.plugin.getEmbed("link_success"));
        embedBuilder.setDescription(embedBuilder.getDescriptionBuilder().toString().replace("%player-name%", player.getName()));
        embedBuilder.setThumbnail(this.plugin.getConfig().getString("messages.discord.link_success.thumbnail")
                .replace("%player-name%", player.getName()));

        event.replyEmbeds(embedBuilder.build()).queue();

        if (player.isOnline()) {
            player.getPlayer().sendMessage(this.plugin.getMessage("link_success")
                    .replace("%name%", event.getUser().getName())
                    .replace("%discriminator%", event.getUser().getDiscriminator())
                    .replace("%id%", event.getUser().getId()));
        }
    }
}
