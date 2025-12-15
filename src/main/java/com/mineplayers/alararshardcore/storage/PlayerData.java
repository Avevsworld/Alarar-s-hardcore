package com.mineplayers.alararshardcore.storage;

import java.util.UUID;

/**
 * Simple POJO representing persistent data for a player.
 */
public class PlayerData {
    private UUID uuid;
    private double currentMaxHealth;
    private boolean seasonDead;
    private long mountCooldownUntil;

    public PlayerData() {
    }

    public PlayerData(UUID uuid, double currentMaxHealth) {
        this.uuid = uuid;
        this.currentMaxHealth = currentMaxHealth;
        this.seasonDead = false;
        this.mountCooldownUntil = 0L;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public double getCurrentMaxHealth() {
        return currentMaxHealth;
    }

    public void setCurrentMaxHealth(double currentMaxHealth) {
        this.currentMaxHealth = currentMaxHealth;
    }

    public boolean isSeasonDead() {
        return seasonDead;
    }

    public void setSeasonDead(boolean seasonDead) {
        this.seasonDead = seasonDead;
    }

    public long getMountCooldownUntil() {
        return mountCooldownUntil;
    }

    public void setMountCooldownUntil(long mountCooldownUntil) {
        this.mountCooldownUntil = mountCooldownUntil;
    }
}
