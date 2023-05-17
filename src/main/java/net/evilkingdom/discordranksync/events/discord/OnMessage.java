package net.evilkingdom.discordranksync.events.discord;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.evilkingdom.discordranksync.DiscordRankSync;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class OnMessage extends ListenerAdapter {
    private final DiscordRankSync plugin;

    public OnMessage(DiscordRankSync plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!event.getChannel().getId().equals(this.plugin.getConfig().getString("chat_channel_id"))) return;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!this.plugin.getPlayerManager().isChatToggled(player)) return;

            player.sendMessage(this.plugin.getMessage("chat")
                    .replace("%name%", event.getAuthor().getName())
                    .replace("%discriminator%", event.getAuthor().getDiscriminator())
                    .replace("%message%", event.getMessage().getContentStripped()));
        }
    }
}
