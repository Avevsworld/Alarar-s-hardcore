package com.mineplayers.alararshardcore.listeners;

import com.mineplayers.alararshardcore.AlararsHardcore;
import com.mineplayers.alararshardcore.managers.HeartManager;
import com.mineplayers.alararshardcore.proxy.VelocityMessenger;
import com.mineplayers.alararshardcore.storage.PlayerData;
import com.mineplayers.alararshardcore.storage.PlayerDataStore;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Handles player death events in the Aether.
 */
public class PlayerDeathListener implements Listener {

    private final AlararsHardcore plugin;
    private final PlayerDataStore dataStore;
    private final HeartManager heartManager;
    private final VelocityMessenger velocityMessenger;

    public PlayerDeathListener(AlararsHardcore plugin, PlayerDataStore dataStore, HeartManager heartManager, VelocityMessenger velocityMessenger) {
        this.plugin = plugin;
        this.dataStore = dataStore;
        this.heartManager = heartManager;
        this.velocityMessenger = velocityMessenger;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        var player = event.getEntity();
        if (!player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("worldName", "aether"))) {
            return;
        }

        event.getDrops().clear();
        player.getInventory().clear();

        PlayerData data = dataStore.load(player);
        boolean seasonDead = heartManager.handleDeath(player, data);
        dataStore.save(data);

        if (seasonDead) {
            player.sendMessage("You have exhausted your hearts for this season.");
        }

        // Teleport away a short moment after death to give time for death screen.
        plugin.getServer().getGlobalRegionScheduler().runDelayed(plugin, task -> {
            // TODO: hook into Velocity to redirect to the lobby instead of respawning here.
            velocityMessenger.sendToLobby(player);
        }, 20L);
    }
}
