package com.mineplayers.alararshardcore.listeners;

import com.mineplayers.alararshardcore.AlararsHardcore;
import com.mineplayers.alararshardcore.restrictions.BlockRestrictions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

/**
 * Blocks certain spawn reasons in the Aether.
 */
public class EntitySpawnListener implements Listener {

    private final AlararsHardcore plugin;
    private final BlockRestrictions restrictions;

    public EntitySpawnListener(AlararsHardcore plugin, BlockRestrictions restrictions) {
        this.plugin = plugin;
        this.restrictions = restrictions;
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        var entity = event.getEntity();
        if (!entity.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("worldName", "aether"))) {
            return;
        }

        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER &&
                restrictions.isSpawner(org.bukkit.Material.SPAWNER)) {
            event.setCancelled(true);
        }
    }
}
