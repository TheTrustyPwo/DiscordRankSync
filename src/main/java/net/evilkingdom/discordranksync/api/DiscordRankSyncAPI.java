package net.evilkingdom.discordranksync.api;

import org.bukkit.OfflinePlayer;

import javax.annotation.Nullable;

public interface DiscordRankSyncAPI {

    /**
     * Returns whether player is linked with a discord user
     *
     * @param player ~ The player to check if its linked
     * @return ~ Returns true if the player is linked with a discord user else false
     */
     boolean isLinked(OfflinePlayer player);

    /**
     * Get the discord ID of a player, if the player is linked
     *
     * @param player ~ The player to query for its discord ID
     * @return ~ Returns the discord ID as a string if the player is linked, else null
     */
    @Nullable
    String getDiscordId(OfflinePlayer player);
}
