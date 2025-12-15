package com.mineplayers.alararshardcore.restrictions;

import org.bukkit.Material;

import java.util.EnumSet;
import java.util.Set;

/**
 * Central list of restricted blocks.
 */
public class BlockRestrictions {

    private final Set<Material> redstoneBlocked = EnumSet.of(
            Material.REDSTONE, Material.REDSTONE_BLOCK, Material.REDSTONE_WIRE,
            Material.REPEATER, Material.COMPARATOR,
            Material.REDSTONE_TORCH, Material.REDSTONE_WALL_TORCH,
            Material.OBSERVER, Material.STICKY_PISTON, Material.PISTON,
            Material.NOTE_BLOCK, Material.DISPENSER, Material.DROPPER,
            Material.TARGET
    );

    public boolean isRedstoneBlocked(Material material) {
        return redstoneBlocked.contains(material);
    }

    public boolean isSpawner(Material material) {
        return material == Material.SPAWNER;
    }
}
