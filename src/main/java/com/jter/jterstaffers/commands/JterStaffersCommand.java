package com.jter.jterstaffers.commands;

import com.jter.jterstaffers.JterStaffers;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JterStaffersCommand implements CommandExecutor, TabCompleter {

    private final JterStaffers plugin;

    public JterStaffersCommand(JterStaffers plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("jterstaffers.admin")) {
            plugin.messages().send(sender, "general.no-permission");
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            plugin.messages().load();
            plugin.notes().load();
            plugin.messages().send(sender, "admin.reloaded");
            return true;
        }

        sendHelp(sender);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("jterstaffers.admin")) {
            return Collections.emptyList();
        }
        if (args.length == 1) {
            List<String> options = new ArrayList<>();
            if ("reload".startsWith(args[0].toLowerCase())) {
                options.add("reload");
            }
            return options;
        }
        return Collections.emptyList();
    }

    private void sendHelp(CommandSender sender) {
        String[] lines = {
                "&8&m----------&r &c&lJterStaffers by jter &7v1.0 &8&m----------",
                "&c/sc <message> &7- Send a message to staff chat",
                "&c/staffmode &7- Toggle staff mode",
                "&c/vanish &7- Toggle vanish",
                "&c/invsee <player> &7- View/edit a player's inventory",
                "&c/freeze <player> &7- Freeze/unfreeze a player",
                "&c/note &7- List players with notes",
                "&c/note <player> &7- View a player's notes",
                "&c/note <player> <text> &7- Add a note to a player",
                "&c/note remove <player> <id> &7- Remove a note",
                "&c/js reload &7- Reload config.yml and messages.yml",
                "&8&m-------------------------------------------------"
        };
        for (String line : lines) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
        }
    }
}
