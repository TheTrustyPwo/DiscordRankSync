package net.evilkingdom.discordranksync.database.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import net.evilkingdom.discordranksync.DiscordRankSync;
import net.evilkingdom.discordranksync.database.Database;
import org.bson.Document;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public class Mongo extends Database {
    private final DiscordRankSync plugin;
    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public Mongo(DiscordRankSync plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public boolean connect() {
        String connString = this.plugin.getConfig().getString("database.mongo.connection_string");
        String databaseName = this.plugin.getConfig().getString("database.mongo.database");
        ConnectionString connectionString = new ConnectionString(connString);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .build();
        this.client = MongoClients.create(settings);
        this.database = this.client.getDatabase(databaseName);
        this.collection = this.database.getCollection("players");
        return true;
    }

    @Override
    public void close() {
        this.client.close();
    }

    @Override
    public String getDiscordId(UUID uuid) {
        Document document = this.collection.find(Filters.eq("_id", uuid.toString())).first();
        if (document == null) return null;
        return document.getString("discordId");
    }

    @Override
    public void linkPlayer(UUID uuid, String discordId) {
        Document document = new Document()
                .append("_id", uuid.toString())
                .append("discordId", discordId);
        this.collection.insertOne(document);
    }

    @Override
    public void unlinkPlayer(UUID uuid) {
        MongoCollection<Document> collection = this.database.getCollection("players");
        collection.deleteOne(Filters.eq("_id", uuid.toString()));
    }
}
