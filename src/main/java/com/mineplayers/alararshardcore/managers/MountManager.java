package com.mineplayers.alararshardcore.managers;

import com.mineplayers.alararshardcore.AlararsHardcore;
import com.mineplayers.alararshardcore.storage.PlayerData;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * Handles mount spawning with simple cast time and cooldown logic.
 */
public class MountManager {

    private final AlararsHardcore plugin;
    private final long castTicks;
    private final long cooldownMillis;

    public MountManager(AlararsHardcore plugin) {
        this.plugin = plugin;
        long castSeconds = plugin.getConfig().getLong("mount.castSeconds", 3);
        this.castTicks = Math.max(1L, castSeconds * 20L);
        this.cooldownMillis = TimeUnit.SECONDS.toMillis(plugin.getConfig().getLong("mount.cooldownSeconds", 900));
    }

    public boolean canUseMount(PlayerData data) {
        return Instant.now().toEpochMilli() >= data.getMountCooldownUntil();
    }

    public void triggerCooldown(PlayerData data) {
        data.setMountCooldownUntil(Instant.now().toEpochMilli() + cooldownMillis);
    }

    public void castAndSummon(Player player, PlayerData data) {
        if (!canUseMount(data)) {
            long remaining = Math.max(0, data.getMountCooldownUntil() - Instant.now().toEpochMilli());
            player.sendMessage("Your mount is resting for another " + (remaining / 1000) + "s.");
            return;
        }

        // Basic cast time using the region scheduler so Folia remains happy.
        plugin.getServer().getRegionScheduler().runDelayed(plugin, player.getLocation(), task -> {
            // TODO: add cancelation if player moves or takes damage during cast time.
            summonHorse(player);
            triggerCooldown(data);
        }, castTicks);
    }

    private void summonHorse(Player player) {
        var location = player.getLocation();
        var world = location.getWorld();
        if (world == null) {
            player.sendMessage("You cannot summon a mount here.");
            return;
        }

        world.spawn(location, Horse.class, spawned -> {
            spawned.setAdult();
            spawned.setTamed(true);
            spawned.setOwner(player);
            spawned.getInventory().setSaddle(new ItemStack(Material.SADDLE));
            spawned.setCustomName(player.getName() + "'s Mount");
        });

        player.sendMessage("Your mount appears from the sky!");
        // TODO: add particle/effect support and prevent duplicate mounts per player.
    }
}
