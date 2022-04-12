package net.evilkingdom.discordranksync.events.minecraft;

import net.evilkingdom.discordranksync.DiscordRankSync;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerQuit implements Listener {
    private final DiscordRankSync plugin;

    public OnPlayerQuit(DiscordRankSync plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        this.plugin.getPlayerManager().unloadPlayer(e.getPlayer());
    }
}
