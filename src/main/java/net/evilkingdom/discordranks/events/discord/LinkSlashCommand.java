package net.evilkingdom.discordranks.events.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.evilkingdom.discordranks.DiscordRankSync;
import org.bukkit.Bukkit;

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
            event.reply("invalid code").setEphemeral(true).queue();
            return;
        }

        UUID uuid = this.plugin.getPlayerManager().getSecretCodes().get(code);
        String playerName = Bukkit.getOfflinePlayer(uuid).getName();

        this.plugin.getPlayerManager().getSecretCodes().remove(code);
        this.plugin.getPlayerManager().link(uuid, event.getUser().getId());

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Success!");
        embedBuilder.setDescription(String.format("You are now linked with %s", playerName));

        event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
    }
}
