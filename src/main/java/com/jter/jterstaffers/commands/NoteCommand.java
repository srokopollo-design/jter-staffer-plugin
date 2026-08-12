package com.jter.jterstaffers.commands;

import com.jter.jterstaffers.JterStaffers;
import com.jter.jterstaffers.managers.Note;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NoteCommand implements CommandExecutor, TabCompleter {

    private final JterStaffers plugin;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public NoteCommand(JterStaffers plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("jterstaffer.notes")) {
            plugin.messages().send(sender, "general.no-permission");
            return true;
        }

        if (args.length == 0) {
            listPlayersWithNotes(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (args.length < 3) {
                plugin.messages().send(sender, "general.invalid-usage",
                        java.util.Collections.singletonMap("usage", "/note remove <player> <id>"));
                return true;
            }
            removeNote(sender, args[1], args[2]);
            return true;
        }

        if (args.length == 1) {
            showNotes(sender, args[0]);
            return true;
        }

        String text = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
        addNote(sender, args[0], text);
        return true;
    }

    private void addNote(CommandSender sender, String targetName, String text) {
        if (text == null || text.trim().isEmpty()) {
            plugin.messages().send(sender, "notes.empty-text");
            return;
        }

        OfflinePlayer target = resolvePlayer(targetName);
        if (target == null || target.getUniqueId() == null) {
            plugin.messages().send(sender, "general.player-not-found",
                    java.util.Collections.singletonMap("player", targetName));
            return;
        }

        String resolvedName = target.getName() != null ? target.getName() : targetName;
        String author = sender instanceof Player ? sender.getName() : "Console";

        int id = plugin.notes().addNote(target.getUniqueId(), resolvedName, text.trim(), author);

        Map<String, String> ph = new HashMap<>();
        ph.put("id", String.valueOf(id));
        ph.put("player", resolvedName);
        plugin.messages().send(sender, "notes.added", ph);
    }

    private void removeNote(CommandSender sender, String targetName, String idStr) {
        OfflinePlayer target = resolvePlayer(targetName);
        if (target == null || target.getUniqueId() == null) {
            plugin.messages().send(sender, "general.player-not-found",
                    java.util.Collections.singletonMap("player", targetName));
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            plugin.messages().send(sender, "general.invalid-number",
                    java.util.Collections.singletonMap("input", idStr));
            return;
        }

        String resolvedName = target.getName() != null ? target.getName() : targetName;
        boolean removed = plugin.notes().removeNote(target.getUniqueId(), id);

        Map<String, String> ph = new HashMap<>();
        ph.put("id", String.valueOf(id));
        ph.put("player", resolvedName);
        plugin.messages().send(sender, removed ? "notes.removed" : "notes.remove-not-found", ph);
    }

    private void showNotes(CommandSender sender, String targetName) {
        OfflinePlayer target = resolvePlayer(targetName);
        if (target == null || target.getUniqueId() == null) {
            plugin.messages().send(sender, "general.player-not-found",
                    java.util.Collections.singletonMap("player", targetName));
            return;
        }

        String resolvedName = target.getName() != null ? target.getName() : targetName;
        List<Note> notes = plugin.notes().getNotes(target.getUniqueId());

        if (notes.isEmpty()) {
            plugin.messages().send(sender, "notes.no-notes",
                    java.util.Collections.singletonMap("player", resolvedName));
            return;
        }

        Map<String, String> headerPh = new HashMap<>();
        headerPh.put("player", resolvedName);
        sender.sendMessage(plugin.messages().get("notes.list-header", headerPh));

        for (Note note : notes) {
            Map<String, String> ph = new HashMap<>();
            ph.put("id", String.valueOf(note.getId()));
            ph.put("text", note.getText());
            ph.put("author", note.getAuthor());
            ph.put("date", sdf.format(new Date(note.getTimestamp())));
            sender.sendMessage(plugin.messages().get("notes.list-entry", ph));
        }

        sender.sendMessage(plugin.messages().get("notes.list-footer"));
    }

    private void listPlayersWithNotes(CommandSender sender) {
        Map<UUID, String> players = plugin.notes().getAllPlayersWithNotes();
        if (players.isEmpty()) {
            plugin.messages().send(sender, "notes.no-notes-any");
            return;
        }

        sender.sendMessage(plugin.messages().get("notes.players-header"));
        for (Map.Entry<UUID, String> entry : players.entrySet()) {
            int count = plugin.notes().getNotes(entry.getKey()).size();
            Map<String, String> ph = new HashMap<>();
            ph.put("player", entry.getValue());
            ph.put("count", String.valueOf(count));
            sender.sendMessage(plugin.messages().get("notes.players-entry", ph));
        }
    }

    private OfflinePlayer resolvePlayer(String name) {
        Player online = Bukkit.getPlayerExact(name);
        if (online != null) return online;

        for (OfflinePlayer offline : Bukkit.getOfflinePlayers()) {
            if (name.equalsIgnoreCase(offline.getName())) {
                return offline;
            }
        }

        OfflinePlayer fallback = Bukkit.getOfflinePlayer(name);
        if (fallback.hasPlayedBefore() || fallback.isOnline()) {
            return fallback;
        }
        return null;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> options = new ArrayList<>();
        if (args.length == 1) {
            options.add("remove");
            for (Player p : Bukkit.getOnlinePlayers()) {
                options.add(p.getName());
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                options.add(p.getName());
            }
        }
        return options;
    }
}
