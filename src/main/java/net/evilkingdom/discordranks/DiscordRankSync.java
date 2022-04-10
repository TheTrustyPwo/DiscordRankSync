package net.evilkingdom.discordranks;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.evilkingdom.discordranks.database.Database;
import net.evilkingdom.discordranks.database.mongodb.Mongo;
import net.evilkingdom.discordranks.events.OnPlayerJoin;
import net.evilkingdom.discordranks.events.OnPlayerQuit;
import net.evilkingdom.discordranks.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class DiscordRankSync extends JavaPlugin {
    private static DiscordRankSync instance;
    private Database database;
    private JDA jda;
    private Map<String, Role> ranks;
    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadDatabase();
        loadJDA();
        loadRanks();
        this.playerManager = new PlayerManager(this);
        registerEvents();
    }

    private void loadDatabase() {
        String databaseType = getConfig().getString("database.type");
        switch (databaseType.toUpperCase(Locale.ROOT)) {
            case "MONGODB": {
                this.database = new Mongo(this);
                break;
            }
        }
    }

    private void loadJDA() {
        JDABuilder builder = JDABuilder.createDefault(getConfig().getString("bot_token"));
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        builder.setBulkDeleteSplittingEnabled(false);
        builder.setActivity(Activity.playing("Evil Kingdom"));
        try {
            this.jda = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    private void loadRanks() {
        this.ranks = new HashMap<>();
        ConfigurationSection section = getConfig().getConfigurationSection("ranks");
        for (String permission : section.getKeys(false)) {
            this.ranks.put(permission, this.jda.getRoleById(section.getString(permission)));
        }
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new OnPlayerJoin(this), this);
        Bukkit.getPluginManager().registerEvents(new OnPlayerQuit(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static DiscordRankSync getInstance() {
        return instance;
    }

    public Database getDatabase() {
        return database;
    }

    public JDA getJda() {
        return jda;
    }

    public Map<String, Role> getRanks() {
        return ranks;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}
