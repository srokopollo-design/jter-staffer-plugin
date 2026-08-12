package com.jter.jterstaffers.commands;

import com.jter.jterstaffers.JterStaffers;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class StaffChatCommand implements CommandExecutor {

    private final JterStaffers plugin;

    public StaffChatCommand(JterStaffers plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("jterstaffers.staffchat")) {
            plugin.messages().send(sender, "general.no-permission");
            return true;
        }

        if (args.length == 0) {
            plugin.messages().send(sender, "general.invalid-usage",
                    java.util.Collections.singletonMap("usage", "/sc <message>"));
            return true;
        }

        String message = String.join(" ", args);
        if (message.trim().isEmpty()) {
            plugin.messages().send(sender, "staffchat.empty-message");
            return true;
        }

        String senderName = sender instanceof Player ? sender.getName() : "Console";

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("player", senderName);
        placeholders.put("message", message);
        String formatted = plugin.messages().get("staffchat.format", placeholders);

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.hasPermission("jterstaffers.staffchat")) {
                online.sendMessage(formatted);
            }
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(formatted);
        }

        return true;
    }
}
