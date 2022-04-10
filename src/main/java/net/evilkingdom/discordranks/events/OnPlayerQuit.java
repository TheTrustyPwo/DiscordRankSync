package net.evilkingdom.discordranks.events;

import net.evilkingdom.discordranks.DiscordRankSync;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerQuit implements Listener {
    private final DiscordRankSync plugin;

    public OnPlayerQuit(DiscordRankSync plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        this.plugin.getPlayerManager().getPlayerUserCache().remove(e.getPlayer().getUniqueId());
    }
}
