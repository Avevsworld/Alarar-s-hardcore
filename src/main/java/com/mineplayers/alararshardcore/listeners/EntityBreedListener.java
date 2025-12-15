package com.mineplayers.alararshardcore.listeners;

import com.mineplayers.alararshardcore.AlararsHardcore;
import org.bukkit.Chunk;
import org.bukkit.entity.Animals;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;

/**
 * Limits breeding in the Aether world.
 */
public class EntityBreedListener implements Listener {

    private final AlararsHardcore plugin;
    private final int maxAnimalsPerChunk;

    public EntityBreedListener(AlararsHardcore plugin) {
        this.plugin = plugin;
        this.maxAnimalsPerChunk = plugin.getConfig().getInt("breeding.maxAnimalsPerChunk", 20);
    }

    @EventHandler
    public void onBreed(EntityBreedEvent event) {
        if (!(event.getEntity() instanceof Animals animals)) {
            return;
        }

        if (!animals.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("worldName", "aether"))) {
            return;
        }

        Chunk chunk = animals.getLocation().getChunk();
        long count = chunk.getEntities().length;
        // TODO: make this radius-based instead of chunk-based.
        if (count >= maxAnimalsPerChunk) {
            event.setCancelled(true);
        }
    }
}
