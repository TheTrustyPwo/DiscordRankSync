package net.evilkingdom.discordranksync;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import net.evilkingdom.discordranksync.api.DiscordRankSyncAPI;
import net.evilkingdom.discordranksync.api.DiscordRankSyncAPIImpl;
import net.evilkingdom.discordranksync.commands.DiscordCommand;
import net.evilkingdom.discordranksync.database.Database;
import net.evilkingdom.discordranksync.database.mongodb.Mongo;
import net.evilkingdom.discordranksync.events.discord.LinkSlashCommand;
import net.evilkingdom.discordranksync.events.minecraft.OnPlayerJoin;
import net.evilkingdom.discordranksync.events.minecraft.OnPlayerQuit;
import net.evilkingdom.discordranksync.player.PlayerManager;
import net.evilkingdom.discordranksync.utils.StringUtilities;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.util.*;

@SuppressWarnings("ConstantConditions")
public final class DiscordRankSync extends JavaPlugin {
    private static DiscordRankSync instance;
    private Database database;
    private JDA jda;
    private Map<String, Role> ranks;
    private Map<String, String> messages;
    private Map<String, MessageEmbed> embeds;
    private PlayerManager playerManager;
    private DiscordRankSyncAPI api;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        loadAll();
        registerCommands();
        registerEvents();
        this.playerManager = new PlayerManager(this);
        this.api = new DiscordRankSyncAPIImpl(this);
    }

    private void loadAll() {
        loadDatabase();
        loadJDA();
        loadSlashCommands();
        loadRanks();
        loadMessages();
        loadEmbeds();
    }

    private void loadDatabase() {
        String databaseType = getConfig().getString("database.type");

        switch (databaseType.toUpperCase(Locale.ROOT)) {
            case "MONGODB" -> this.database = new Mongo(this);
        }
    }

    private void loadJDA() {
        JDABuilder builder = JDABuilder.createDefault(getConfig().getString("bot_token"));

        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        builder.setBulkDeleteSplittingEnabled(false);
        builder.setActivity(Activity.playing("Evil Kingdom"));

        try {
            this.jda = builder.build();
            this.jda.awaitReady();
            getLogger().info("Logged into discord bot");
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void loadSlashCommands() {
        CommandData linkCommandData = new CommandDataImpl("link", "Link your Minecraft and Discord accounts!")
                .addOption(OptionType.STRING, "code", "Secret code what was provided to you", true);
        this.jda.getGuildById(getConfig().getString("guild_id")).upsertCommand(linkCommandData).queue();
        this.jda.addEventListener(new LinkSlashCommand(this));
    }

    private void loadRanks() {
        this.ranks = new HashMap<>();

        ConfigurationSection section = getConfig().getConfigurationSection("ranks");
        for (String permission : section.getKeys(false)) {
            this.ranks.put(permission.replace('_', '.'), this.jda.getGuildById(getConfig().getString("guild_id"))
                    .getRoleById(section.getString(permission)));
        }

        getLogger().info(String.format("Loaded %d ranks", this.ranks.size()));
    }

    private void loadMessages() {
        this.messages = new HashMap<>();

        ConfigurationSection section = getConfig().getConfigurationSection("messages.minecraft");
        for (String identifier : section.getKeys(false)) {
            String message = StringUtilities.colorize(section.getStringList(identifier));
            this.messages.put(identifier, message);
        }

        getLogger().info("Loaded messages");
    }

    private void loadEmbeds() {
        this.embeds = new HashMap<>();

        ConfigurationSection section = getConfig().getConfigurationSection("messages.discord");
        for (String identifier : section.getKeys(false)) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(section.getString(identifier + ".title"));
            embedBuilder.setDescription(section.getString(identifier + ".description"));
            embedBuilder.setColor(Integer.parseInt(section.getString(identifier + ".color", "000000"), 16));
            this.embeds.put(identifier, embedBuilder.build());
        }

        getLogger().info("Loaded embeds");
    }

    private void registerCommands() {
        getCommand("discord").setExecutor(new DiscordCommand(this));
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new OnPlayerJoin(this), this);
        Bukkit.getPluginManager().registerEvents(new OnPlayerQuit(this), this);
    }

    public void reload() {
        reloadConfig();
        loadRanks();
        loadMessages();
        loadEmbeds();
    }

    @Override
    public void onDisable() {
        this.database.close();
        this.jda.shutdown();
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

    public String getMessage(String identifier) {
        return this.messages.get(identifier);
    }

    public MessageEmbed getEmbed(String identifier) {
        return this.embeds.get(identifier);
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public DiscordRankSyncAPI getApi() {
        return api;
    }
}