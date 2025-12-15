package com.mineplayers.alararshardcore;

import com.mineplayers.alararshardcore.commands.HardcoreDebugCommand;
import com.mineplayers.alararshardcore.commands.MountCommand;
import com.mineplayers.alararshardcore.listeners.BlockPlaceListener;
import com.mineplayers.alararshardcore.listeners.EntityBreedListener;
import com.mineplayers.alararshardcore.listeners.EntitySpawnListener;
import com.mineplayers.alararshardcore.listeners.PlayerDeathListener;
import com.mineplayers.alararshardcore.listeners.PlayerJoinListener;
import com.mineplayers.alararshardcore.managers.HeartManager;
import com.mineplayers.alararshardcore.managers.MountManager;
import com.mineplayers.alararshardcore.managers.SpawnManager;
import com.mineplayers.alararshardcore.proxy.VelocityMessenger;
import com.mineplayers.alararshardcore.restrictions.BlockRestrictions;
import com.mineplayers.alararshardcore.storage.PlayerDataStore;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Entry point for the Alarar's Hardcore plugin.
 */
public class AlararsHardcore extends JavaPlugin {

    private PlayerDataStore playerDataStore;
    private HeartManager heartManager;
    private SpawnManager spawnManager;
    private MountManager mountManager;
    private BlockRestrictions blockRestrictions;
    private VelocityMessenger velocityMessenger;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.playerDataStore = new PlayerDataStore(this);
        this.heartManager = new HeartManager(this);
        this.spawnManager = new SpawnManager(this);
        this.mountManager = new MountManager(this);
        this.blockRestrictions = new BlockRestrictions();
        this.velocityMessenger = new VelocityMessenger(this);

        registerListeners();
        registerCommands();
    }

    private void registerListeners() {
        var pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(this, playerDataStore, heartManager, spawnManager), this);
        pluginManager.registerEvents(new PlayerDeathListener(this, playerDataStore, heartManager, velocityMessenger), this);
        pluginManager.registerEvents(new BlockPlaceListener(this, blockRestrictions), this);
        pluginManager.registerEvents(new EntitySpawnListener(this, blockRestrictions), this);
        pluginManager.registerEvents(new EntityBreedListener(this), this);
    }

    private void registerCommands() {
        var mountCommand = new MountCommand(mountManager, playerDataStore);
        if (getCommand("mount") != null) {
            getCommand("mount").setExecutor(mountCommand);
            getCommand("mount").setTabCompleter(mountCommand);
        }

        if (getCommand("ahcdebug") != null) {
            getCommand("ahcdebug").setExecutor(new HardcoreDebugCommand(playerDataStore));
        }
    }
}
