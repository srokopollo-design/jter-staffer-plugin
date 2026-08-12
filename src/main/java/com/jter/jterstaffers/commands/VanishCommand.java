package com.jter.jterstaffers.commands;

import com.jter.jterstaffers.JterStaffers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand implements CommandExecutor {

    private final JterStaffers plugin;

    public VanishCommand(JterStaffers plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            plugin.messages().send(sender, "general.player-only");
            return true;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("jterstaffers.staffmode")) {
            plugin.messages().send(player, "general.no-permission");
            return true;
        }

        boolean nowVanished = plugin.vanish().toggle(player);
        plugin.messages().send(player, nowVanished ? "vanish.enabled" : "vanish.disabled");
        return true;
    }
}
