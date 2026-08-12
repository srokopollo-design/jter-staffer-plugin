package com.jter.jterstaffers.commands;

import com.jter.jterstaffers.JterStaffers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffModeCommand implements CommandExecutor {

    private final JterStaffers plugin;

    public StaffModeCommand(JterStaffers plugin) {
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

        boolean nowIn = plugin.staffMode().toggle(player);
        plugin.messages().send(player, nowIn ? "staffmode.enabled" : "staffmode.disabled");
        return true;
    }
}
