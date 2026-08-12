package com.jter.jterstaffers.commands;

import com.jter.jterstaffers.JterStaffers;
import com.jter.jterstaffers.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InvseeCommand implements CommandExecutor {

    private final JterStaffers plugin;

    public InvseeCommand(JterStaffers plugin) {
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

        if (args.length < 1) {
            plugin.messages().send(player, "general.invalid-usage",
                    java.util.Collections.singletonMap("usage", "/invsee <player>"));
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            plugin.messages().send(player, "general.player-not-online",
                    java.util.Collections.singletonMap("player", args[0]));
            return true;
        }

        if (target.equals(player)) {
            plugin.messages().send(player, "invsee.opened-own");
            return true;
        }

        Util.openInvsee(player, target, plugin);
        return true;
    }
}
