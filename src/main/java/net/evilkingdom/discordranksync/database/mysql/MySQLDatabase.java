package net.evilkingdom.discordranksync.database.mysql;

import net.evilkingdom.discordranksync.DiscordRankSync;
import net.evilkingdom.discordranksync.database.Database;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.UUID;

public class MySQLDatabase extends Database {
    private Connection connection;

    public MySQLDatabase(DiscordRankSync plugin) {
        super(plugin);
    }

    @Override
    public boolean connect() {
        String host = this.plugin.getConfig().getString("database.mysql.host");
        int port = this.plugin.getConfig().getInt("database.mysql.port", 3306);
        String database = this.plugin.getConfig().getString("database.mysql.database");
        String username = this.plugin.getConfig().getString("database.mysql.username");
        String password = this.plugin.getConfig().getString("database.mysql.password");
        boolean useSSL = this.plugin.getConfig().getBoolean("database.mysql.use_ssl", false);

        try {
            this.connection = DriverManager.getConnection(
                    String.format("jdbc:mysql://%s:%d/%s?useSSL=%b", host, port, database, useSSL),
                    username,
                    password
            );

            createTables();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized void execute(String statement, Object... args) {
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(statement)) {
            if (args != null) {
                for (byte b = 0; b < args.length; b++) {
                    preparedStatement.setObject(b + 1, args[b]);
                }
            }

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void executeAsync(String statement, Object... args) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> execute(statement, args));
    }

    private void createTables() {
        executeAsync("CREATE TABLE IF NOT EXISTS DiscordRankSync(UUID varchar(36) NOT NULL UNIQUE, DiscordID varchar(18), PRIMARY KEY (UUID))");
    }

    @Override
    public void close() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDiscordId(UUID uuid) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT DiscordID FROM DiscordRankSync WHERE UUID=?");
            preparedStatement.setString(1, uuid.toString());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("DiscordId");
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return null;
    }

    @Override
    public void linkPlayer(UUID uuid, String discordId) {
        executeAsync("INSERT INTO DiscordRankSync VALUES (?, ?)", uuid.toString(), discordId);
    }

    @Override
    public void unlinkPlayer(UUID uuid) {
        executeAsync("DELETE FROM DiscordRankSync WHERE UUID=?", uuid.toString());
    }
}
