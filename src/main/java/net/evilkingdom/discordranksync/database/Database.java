package net.evilkingdom.discordranksync.database;

import net.evilkingdom.discordranksync.DiscordRankSync;

import java.util.UUID;

public abstract class Database {
    protected final DiscordRankSync plugin;

    public Database(DiscordRankSync plugin) {
        this.plugin = plugin;
    }

    public abstract boolean connect();

    public abstract void close();

    public abstract String getDiscordId(UUID uuid);

    public abstract void linkPlayer(UUID uuid, String discordId);

    public abstract void unlinkPlayer(UUID uuid);
}
