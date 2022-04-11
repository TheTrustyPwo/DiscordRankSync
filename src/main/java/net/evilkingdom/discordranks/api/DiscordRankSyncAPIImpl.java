package net.evilkingdom.discordranks.api;

import net.evilkingdom.discordranks.DiscordRankSync;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

public class DiscordRankSyncAPIImpl implements DiscordRankSyncAPI {
    private final DiscordRankSync plugin;

    public DiscordRankSyncAPIImpl(DiscordRankSync plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isLinked(OfflinePlayer player) {
        return this.plugin.getPlayerManager().getPlayerUserCache().containsKey(player.getUniqueId());
    }

    @Nullable
    @Override
    public String getDiscordId(OfflinePlayer player) {
        return this.plugin.getPlayerManager().getPlayerUserCache().getOrDefault(player.getUniqueId(), null);
    }
}
