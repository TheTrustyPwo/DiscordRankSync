package net.evilkingdom.discordranksync.events.minecraft;

import net.evilkingdom.discordranksync.DiscordRankSync;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoin implements Listener {
    private final DiscordRankSync plugin;

    public OnPlayerJoin(DiscordRankSync plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String discordId = this.plugin.getDatabase().getDiscordId(player.getUniqueId());
        if (discordId != null) {
            this.plugin.getPlayerManager().getPlayerUserCache().put(player.getUniqueId(), discordId);
        }
    }
}
