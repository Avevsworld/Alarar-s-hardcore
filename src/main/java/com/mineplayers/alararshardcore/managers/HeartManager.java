package com.mineplayers.alararshardcore.managers;

import com.mineplayers.alararshardcore.AlararsHardcore;
import com.mineplayers.alararshardcore.storage.PlayerData;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

/**
 * Handles max health logic and season death rules.
 */
public class HeartManager {

    private final AlararsHardcore plugin;
    private final double baseMaxHealth;
    private final double healthLossPerDeath;

    public HeartManager(AlararsHardcore plugin) {
        this.plugin = plugin;
        this.baseMaxHealth = plugin.getConfig().getDouble("baseMaxHealth", 40.0);
        this.healthLossPerDeath = plugin.getConfig().getDouble("healthLossPerDeath", 2.0);
    }

    public void applyMaxHealth(Player player, PlayerData data) {
        var attribute = player.getAttribute(Attribute.MAX_HEALTH);
        if (attribute == null) {
            return;
        }

        attribute.setBaseValue(data.getCurrentMaxHealth());
        if (player.getHealth() > data.getCurrentMaxHealth()) {
            player.setHealth(data.getCurrentMaxHealth());
        }
    }

    public boolean handleDeath(Player player, PlayerData data) {
        double newMax = Math.max(2.0, data.getCurrentMaxHealth() - healthLossPerDeath);
        data.setCurrentMaxHealth(newMax);
        applyMaxHealth(player, data);

        // If the player's health has hit the minimum, mark them as season dead.
        if (newMax <= 2.0) {
            data.setSeasonDead(true);
            return true;
        }
        return false;
    }

    public void ensureInitialized(PlayerData data) {
        if (data.getCurrentMaxHealth() <= 0) {
            data.setCurrentMaxHealth(baseMaxHealth);
        }
    }

}
