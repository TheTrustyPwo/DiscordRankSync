package net.evilkingdom.discordranksync.api;

import net.evilkingdom.discordranksync.DiscordRankSync;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

public class DiscordRankSyncAPIImpl implements DiscordRankSyncAPI {
    private final DiscordRankSync plugin;

    public DiscordRankSyncAPIImpl(DiscordRankSync plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isLinked(OfflinePlayer player) {
        return this.plugin.getPlayerManager().getDiscordId(player.getUniqueId()) != null;
    }

    @Nullable
    @Override
    public String getDiscordId(OfflinePlayer player) {
        return this.plugin.getPlayerManager().getDiscordId(player.getUniqueId());
    }
}
