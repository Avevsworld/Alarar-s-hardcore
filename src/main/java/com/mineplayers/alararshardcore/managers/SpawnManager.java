package com.mineplayers.alararshardcore.managers;

import com.mineplayers.alararshardcore.AlararsHardcore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Handles random safe teleports into the Aether world.
 */
public class SpawnManager {

    private final AlararsHardcore plugin;
    private final String targetWorldName;
    private final int radiusMin;
    private final int radiusMax;
    private final int minDistanceToPlayers;
    private final int maxAttempts;

    public SpawnManager(AlararsHardcore plugin) {
        this.plugin = plugin;
        this.targetWorldName = plugin.getConfig().getString("worldName", "aether");
        this.radiusMin = plugin.getConfig().getInt("spawn.radiusMin", 2000);
        this.radiusMax = plugin.getConfig().getInt("spawn.radiusMax", 20000);
        this.minDistanceToPlayers = plugin.getConfig().getInt("spawn.minDistanceToPlayers", 250);
        this.maxAttempts = plugin.getConfig().getInt("spawn.maxAttempts", 40);
    }

    public World getTargetWorld() {
        return Bukkit.getWorld(targetWorldName);
    }

    public void teleportPlayerRandomly(Player player) {
        World world = getTargetWorld();
        if (world == null) {
            plugin.getLogger().warning("Aether world not loaded; cannot teleport " + player.getName());
            return;
        }

        for (int i = 0; i < maxAttempts; i++) {
            Location attempt = randomLocation(world);
            if (isFarFromPlayers(attempt) && isSafe(attempt)) {
                player.teleportAsync(attempt);
                return;
            }
        }

        plugin.getLogger().warning("Failed to find a safe spawn for " + player.getName() + " after " + maxAttempts + " attempts");
    }

    private Location randomLocation(World world) {
        double distance = ThreadLocalRandom.current().nextDouble(radiusMin, radiusMax);
        double angle = ThreadLocalRandom.current().nextDouble(0, 2 * Math.PI);
        double x = Math.cos(angle) * distance;
        double z = Math.sin(angle) * distance;
        int y = world.getHighestBlockYAt((int) x, (int) z) + 1;
        // TODO: add settlement avoidance logic when structures are defined.
        return new Location(world, x + 0.5, y, z + 0.5);
    }

    private boolean isFarFromPlayers(Location location) {
        return Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.getWorld().equals(location.getWorld()))
                .map(Player::getLocation)
                .map(loc -> loc.toVector().distance(location.toVector()))
                .noneMatch(distance -> distance < minDistanceToPlayers);
    }

    private boolean isSafe(Location location) {
        var below = location.clone().subtract(new Vector(0, 1, 0)).getBlock();
        var at = location.getBlock();
        var above = location.clone().add(new Vector(0, 1, 0)).getBlock();
        return below.getType().isSolid() && !at.getType().isSolid() && !above.getType().isSolid();
    }
}
