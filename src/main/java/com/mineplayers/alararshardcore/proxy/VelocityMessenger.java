package com.mineplayers.alararshardcore.proxy;

import com.mineplayers.alararshardcore.AlararsHardcore;
import org.bukkit.entity.Player;

/**
 * Placeholder for Velocity plugin messaging integration.
 */
public class VelocityMessenger {

    private final AlararsHardcore plugin;

    public VelocityMessenger(AlararsHardcore plugin) {
        this.plugin = plugin;
    }

    public void sendToLobby(Player player) {
        // TODO: Implement Velocity plugin messaging to connect the player to the lobby server.
        // The recommended approach is to register the BungeeCord/Velocity plugin messaging channel,
        // then write the appropriate "connect" message here.
        plugin.getLogger().info("Would send " + player.getName() + " to lobby via Velocity here.");
    }
}
