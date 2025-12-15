package com.mineplayers.alararshardcore.commands;

import com.mineplayers.alararshardcore.managers.MountManager;
import com.mineplayers.alararshardcore.storage.PlayerData;
import com.mineplayers.alararshardcore.storage.PlayerDataStore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class MountCommand implements CommandExecutor, TabCompleter {

    private final MountManager mountManager;
    private final PlayerDataStore dataStore;

    public MountCommand(MountManager mountManager, PlayerDataStore dataStore) {
        this.mountManager = mountManager;
        this.dataStore = dataStore;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        PlayerData data = dataStore.load(player);
        mountManager.castAndSummon(player, data);
        dataStore.save(data);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
