package com.mineplayers.alararshardcore.listeners;

import com.mineplayers.alararshardcore.AlararsHardcore;
import com.mineplayers.alararshardcore.restrictions.BlockRestrictions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Prevents disallowed blocks from being placed in the Aether.
 */
public class BlockPlaceListener implements Listener {

    private final AlararsHardcore plugin;
    private final BlockRestrictions restrictions;

    public BlockPlaceListener(AlararsHardcore plugin, BlockRestrictions restrictions) {
        this.plugin = plugin;
        this.restrictions = restrictions;
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        var player = event.getPlayer();
        if (!player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("worldName", "aether"))) {
            return;
        }

        var material = event.getBlockPlaced().getType();
        if (restrictions.isSpawner(material) || restrictions.isRedstoneBlocked(material)) {
            event.setCancelled(true);
            player.sendMessage("You cannot place that block in the Aether.");
        }
    }
}
