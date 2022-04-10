package net.evilkingdom.discordranks.player;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.evilkingdom.discordranks.DiscordRankSync;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {
    private final DiscordRankSync plugin;
    private Map<UUID, String> playerUserCache;
    private BukkitTask updateRoleTask;

    public PlayerManager(DiscordRankSync plugin) {
        this.plugin = plugin;
        this.playerUserCache = new HashMap<>();
        startUpdateRoleTask();
    }

    private void startUpdateRoleTask() {
        String guildId = this.plugin.getConfig().getString("guild_id");
        this.updateRoleTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, () -> {
            this.playerUserCache.forEach((uuid, userId) -> {
                Player player = Bukkit.getPlayer(uuid);
                this.plugin.getRanks().forEach((permission, role) -> {
                    if (player.hasPermission(permission)) {
                        this.plugin.getJda().getGuildById(guildId).addRoleToMember(this.playerUserCache.get(player.getUniqueId()), role).queue();
                    }
                });
            });
        }, 0L, 0L);
    }

    public Map<UUID, String> getPlayerUserCache() {
        return playerUserCache;
    }
}
