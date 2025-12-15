package com.mineplayers.alararshardcore.listeners;

import com.mineplayers.alararshardcore.AlararsHardcore;
import com.mineplayers.alararshardcore.managers.HeartManager;
import com.mineplayers.alararshardcore.managers.SpawnManager;
import com.mineplayers.alararshardcore.storage.PlayerData;
import com.mineplayers.alararshardcore.storage.PlayerDataStore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Handles join logic for new players in the Aether world.
 */
public class PlayerJoinListener implements Listener {

    private final AlararsHardcore plugin;
    private final PlayerDataStore dataStore;
    private final HeartManager heartManager;
    private final SpawnManager spawnManager;

    public PlayerJoinListener(AlararsHardcore plugin, PlayerDataStore dataStore, HeartManager heartManager, SpawnManager spawnManager) {
        this.plugin = plugin;
        this.dataStore = dataStore;
        this.heartManager = heartManager;
        this.spawnManager = spawnManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        PlayerData data = dataStore.load(player);
        heartManager.resetIfNeeded(data);
        dataStore.save(data);

        if (!player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("worldName", "aether"))) {
            return;
        }

        if (data.isSeasonDead()) {
            player.kickPlayer("You are out of lives for this season.");
            // TODO: hook into Velocity messenger to connect to lobby instead of kicking.
            return;
        }

        heartManager.applyMaxHealth(player, data);
        giveStarterKit(player);
        spawnManager.teleportPlayerRandomly(player);
    }

    private void giveStarterKit(org.bukkit.entity.Player player) {
        // TODO: pull items from configuration instead of hard-coded air.
        player.sendMessage("Welcome to the Aether! Stay alive.");
    }
}
