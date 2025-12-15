package com.mineplayers.alararshardcore.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * Basic JSON-backed storage for player data.
 */
public class PlayerDataStore {

    private final JavaPlugin plugin;
    private final Gson gson;
    private final Path playersDirectory;

    public PlayerDataStore(JavaPlugin plugin) {
        this.plugin = plugin;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.playersDirectory = plugin.getDataFolder().toPath().resolve("players");
        ensureFolders();
    }

    private void ensureFolders() {
        try {
            Files.createDirectories(playersDirectory);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to create player data directory: " + e.getMessage());
        }
    }

    public PlayerData load(UUID uuid) {
        Path file = playersDirectory.resolve(uuid.toString() + ".json");
        if (!Files.exists(file)) {
            double baseMaxHealth = plugin.getConfig().getDouble("baseMaxHealth", 40.0);
            return new PlayerData(uuid, baseMaxHealth);
        }

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            PlayerData data = gson.fromJson(reader, PlayerData.class);
            if (data.getUuid() == null) {
                data.setUuid(uuid);
            }
            return data;
        } catch (IOException ex) {
            plugin.getLogger().warning("Could not load data for " + uuid + ": " + ex.getMessage());
            double baseMaxHealth = plugin.getConfig().getDouble("baseMaxHealth", 40.0);
            return new PlayerData(uuid, baseMaxHealth);
        }
    }

    public void save(PlayerData data) {
        Path file = playersDirectory.resolve(data.getUuid().toString() + ".json");
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            gson.toJson(data, writer);
        } catch (IOException ex) {
            plugin.getLogger().warning("Could not save data for " + data.getUuid() + ": " + ex.getMessage());
        }
    }

    public PlayerData load(Player player) {
        return load(player.getUniqueId());
    }

    public void save(Player player, PlayerData data) {
        save(data);
    }
}
