package net.evilkingdom.discordranksync.events.minecraft;

import net.evilkingdom.discordranksync.DiscordRankSync;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class OnPlayerChat implements Listener {
    private final DiscordRankSync plugin;

    public OnPlayerChat(DiscordRankSync plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (!this.plugin.getPlayerManager().isChatToggled(e.getPlayer())) return;

        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            this.plugin.getChatChannel().sendMessage(this.plugin.getConfig().getString("messages.discord.chat")
                    .replace("%player%", e.getPlayer().getName())
                    .replace("%message%", e.getMessage())).queue();
        });
    }
}
