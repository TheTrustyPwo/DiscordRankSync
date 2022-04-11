package net.evilkingdom.discordranksync.player;

import net.evilkingdom.discordranksync.DiscordRankSync;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerManager {
    private final DiscordRankSync plugin;
    private final Map<UUID, String> playerUserCache;
    private final Map<String, UUID> secretCodes;
    private final long ROLE_UPDATE_PERIOD;
    private final long CODE_EXPIRY_TIME;

    public PlayerManager(DiscordRankSync plugin) {
        this.plugin = plugin;
        this.playerUserCache = new HashMap<>();
        this.secretCodes = new HashMap<>();
        this.ROLE_UPDATE_PERIOD = this.plugin.getConfig().getLong("role_update_period") * 60L;
        this.CODE_EXPIRY_TIME = this.plugin.getConfig().getLong("code_expiry_time") * 60L;
        startUpdateRoleTask();
    }

    private void startUpdateRoleTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, this::updateRole, 0L, this.ROLE_UPDATE_PERIOD);
    }

    public void updateRole() {
        String guildId = this.plugin.getConfig().getString("guild_id");

        this.playerUserCache.forEach((uuid, userId) -> {
            Player player = Bukkit.getPlayer(uuid);

            this.plugin.getRanks().forEach((permission, role) -> {
                if (player.hasPermission(permission)) {
                    this.plugin.getJda().getGuildCache().getElementById(guildId)
                            .addRoleToMember(this.playerUserCache.get(player.getUniqueId()), role).queue();
                }
            });
        });
    }

    public void link(UUID uuid, String discordId) {
        this.plugin.getDatabase().linkPlayer(uuid, discordId);
        this.playerUserCache.put(uuid, discordId);
    }

    public void unlink(UUID uuid) {
        this.plugin.getDatabase().unlinkPlayer(uuid);
        this.playerUserCache.remove(uuid);
    }

    public String giveCode(UUID uuid) {
        if (this.secretCodes.containsValue(uuid)) {
            this.secretCodes.values().remove(uuid);
        }

        String code = String.valueOf(ThreadLocalRandom.current().nextInt(10000000, 99999999));
        this.secretCodes.put(code, uuid);
        Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin,
                () -> this.secretCodes.remove(code), this.CODE_EXPIRY_TIME);

        return code;
    }

    public Map<UUID, String> getPlayerUserCache() {
        return playerUserCache;
    }

    public Map<String, UUID> getSecretCodes() {
        return secretCodes;
    }
}
