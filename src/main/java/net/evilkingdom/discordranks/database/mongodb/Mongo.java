package net.evilkingdom.discordranks.database.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import net.evilkingdom.discordranks.DiscordRankSync;
import net.evilkingdom.discordranks.database.Database;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.UUID;

@SuppressWarnings("ConstantConditions")
public class Mongo extends Database {
    private final DiscordRankSync plugin;
    private MongoClient client;
    private MongoDatabase database;

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
        return true;
    }

    private void createCollections() {
        this.database.createCollection("players");
    }

    @Override
    public String getUserID(UUID uuid) {
        MongoCollection<Document> collection = this.database.getCollection("players");
        Bson projectionFields = Projections.fields(
                Projections.include("uuid", uuid.toString()),
                Projections.excludeId());
        Document document = collection.find()
                .projection(projectionFields)
                .first();
        return document.getString("userId");
    }
}
