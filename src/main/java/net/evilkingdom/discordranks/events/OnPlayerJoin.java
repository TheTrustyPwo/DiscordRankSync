package net.evilkingdom.discordranks.events;

import net.evilkingdom.discordranks.DiscordRankSync;
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
        String userId = this.plugin.getDatabase().getUserID(player.getUniqueId());
        if (userId == null) {
            return;
        }
        this.plugin.getPlayerManager().getPlayerUserCache().put(player.getUniqueId(), userId);
    }
}
