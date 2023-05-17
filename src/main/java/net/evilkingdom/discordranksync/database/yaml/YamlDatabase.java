package net.evilkingdom.discordranksync.database.yaml;

import net.evilkingdom.discordranksync.DiscordRankSync;
import net.evilkingdom.discordranksync.database.Database;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class YamlDatabase extends Database {
    private String name;
    private File file;
    private YamlConfiguration data;

    public YamlDatabase(DiscordRankSync plugin) {
        super(plugin);
        this.name = this.plugin.getConfig().getString("database.yaml.file_name");
    }

    @Override
    public boolean connect() {
        this.file = new File(this.plugin.getDataFolder(), this.name);
        this.data = YamlConfiguration.loadConfiguration(this.file);
        if (file.exists()) return true;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void close() {
        save();
    }

    @Override
    public String getDiscordId(UUID uuid) {
        return this.data.getString(uuid.toString());
    }

    @Override
    public void linkPlayer(UUID uuid, String discordId) {
        this.data.set(uuid.toString(), discordId);
        save();
    }

    @Override
    public void unlinkPlayer(UUID uuid) {
        this.data.set(uuid.toString(), null);
        save();
    }

    private void save() {
        try {
            this.data.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
