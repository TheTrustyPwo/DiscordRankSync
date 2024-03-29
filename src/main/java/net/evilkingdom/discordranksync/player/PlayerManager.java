package net.evilkingdom.discordranksync.player;

import net.evilkingdom.discordranksync.DiscordRankSync;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerManager {
    private final DiscordRankSync plugin;
    private final Map<UUID, String> playerUserCache;
    private final Map<String, UUID> secretCodes;
    private final Set<UUID> chatToggled;
    private final long ROLE_UPDATE_PERIOD;
    private final long CODE_EXPIRY_TIME;

    public PlayerManager(DiscordRankSync plugin) {
        this.plugin = plugin;
        this.playerUserCache = new HashMap<>();
        this.secretCodes = new HashMap<>();
        this.chatToggled = new HashSet<>();
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
                    this.plugin.getJda().getGuildById(guildId)
                            .addRoleToMember(this.playerUserCache.get(player.getUniqueId()), role).queue();
                }
            });
        });
    }

    public void link(UUID uuid, String discordId) {
        this.plugin.getPluginDatabase().linkPlayer(uuid, discordId);
        this.playerUserCache.put(uuid, discordId);
    }

    public void unlink(UUID uuid) {
        this.plugin.getPluginDatabase().unlinkPlayer(uuid);
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

    public String getDiscordId(UUID uuid) {
        return this.playerUserCache.get(uuid);
    }

    public UUID getPlayerUUID(String discordId) {
        for (Map.Entry<UUID, String> entry : this.playerUserCache.entrySet()) {
            if (entry.getValue().equals(discordId)) {
                return entry.getKey();
            }
        }

        return null;
    }

    public void loadPlayer(Player player) {
        String discordId = this.plugin.getPluginDatabase().getDiscordId(player.getUniqueId());
        if (discordId != null) {
            this.playerUserCache.put(player.getUniqueId(), discordId);
        }
    }

    public void unloadPlayer(Player player) {
        this.playerUserCache.remove(player.getUniqueId());
    }

    public UUID getPlayerUUIDFromCode(String code) {
        return this.secretCodes.get(code);
    }

    public void removeCode(String code) {
        this.secretCodes.remove(code);
    }

    public void toggleChat(Player player) {
        this.chatToggled.add(player.getUniqueId());
    }

    public void untoggleChat(Player player) {
        this.chatToggled.remove(player.getUniqueId());
    }

    public boolean isChatToggled(Player player) {
        return this.chatToggled.contains(player.getUniqueId());
    }
}
