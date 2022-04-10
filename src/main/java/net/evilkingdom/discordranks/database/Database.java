package net.evilkingdom.discordranks.database;

import net.evilkingdom.discordranks.DiscordRankSync;
import org.bukkit.Bukkit;

import java.util.UUID;

public abstract class Database {
    protected final DiscordRankSync plugin;

    public Database(DiscordRankSync plugin) {
        this.plugin = plugin;
        if (connect()) Bukkit.getLogger().info("Successfully connected to database!");
        else Bukkit.getLogger().warning("Failed to connect to database!");
    }

    public abstract boolean connect();

    public abstract String getUserID(UUID uuid);
}
