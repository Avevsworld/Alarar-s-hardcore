package com.mineplayers.alararshardcore.commands;

import com.mineplayers.alararshardcore.storage.PlayerDataStore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HardcoreDebugCommand implements CommandExecutor {

    private final PlayerDataStore dataStore;

    public HardcoreDebugCommand(PlayerDataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can run this command.");
            return true;
        }

        var data = dataStore.load(player);
        sender.sendMessage("Data for " + player.getName() + ":");
        sender.sendMessage("Max Health: " + data.getCurrentMaxHealth());
        sender.sendMessage("Season Dead: " + data.isSeasonDead());
        sender.sendMessage("Mount Cooldown Until: " + data.getMountCooldownUntil());
        return true;
    }
}
