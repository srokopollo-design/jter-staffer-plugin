package com.jter.jterstaffers.managers;

import com.jter.jterstaffers.JterStaffers;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NoteManager {

    private final JterStaffers plugin;
    private File file;
    private FileConfiguration data;

    // UIDI -> NAME
    private final Map<UUID, String> nameCache = new LinkedHashMap<>();
    private final Map<UUID, List<Note>> notesByPlayer = new LinkedHashMap<>();

    public NoteManager(JterStaffers plugin) {
        this.plugin = plugin;
    }

    public void load() {
        file = new File(plugin.getDataFolder(), "notes.yml");
        if (!file.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().warning("Could not create notes.yml: " + e.getMessage());
            }
        }
        data = YamlConfiguration.loadConfiguration(file);

        nameCache.clear();
        notesByPlayer.clear();

        ConfigurationSection players = data.getConfigurationSection("players");
        if (players == null) return;

        for (String uuidStr : players.getKeys(false)) {
            UUID uuid;
            try {
                uuid = UUID.fromString(uuidStr);
            } catch (IllegalArgumentException e) {
                continue;
            }
            ConfigurationSection playerSection = players.getConfigurationSection(uuidStr);
            if (playerSection == null) continue;

            String name = playerSection.getString("name", uuidStr);
            nameCache.put(uuid, name);

            List<Note> notes = new ArrayList<>();
            ConfigurationSection notesSection = playerSection.getConfigurationSection("notes");
            if (notesSection != null) {
                for (String idStr : notesSection.getKeys(false)) {
                    ConfigurationSection noteSection = notesSection.getConfigurationSection(idStr);
                    if (noteSection == null) continue;
                    try {
                        int id = Integer.parseInt(idStr);
                        String text = noteSection.getString("text", "");
                        String author = noteSection.getString("author", "unknown");
                        long timestamp = noteSection.getLong("timestamp", 0L);
                        notes.add(new Note(id, text, author, timestamp));
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
            notes.sort((a, b) -> Integer.compare(a.getId(), b.getId()));
            notesByPlayer.put(uuid, notes);
        }
    }

    public void save() {
        data = new YamlConfiguration();
        for (Map.Entry<UUID, List<Note>> entry : notesByPlayer.entrySet()) {
            UUID uuid = entry.getKey();
            String base = "players." + uuid;
            data.set(base + ".name", nameCache.getOrDefault(uuid, uuid.toString()));
            for (Note note : entry.getValue()) {
                String noteBase = base + ".notes." + note.getId();
                data.set(noteBase + ".text", note.getText());
                data.set(noteBase + ".author", note.getAuthor());
                data.set(noteBase + ".timestamp", note.getTimestamp());
            }
        }
        try {
            data.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save notes.yml: " + e.getMessage());
        }
    }

    public int addNote(UUID target, String targetName, String text, String author) {
        nameCache.put(target, targetName);
        List<Note> notes = notesByPlayer.computeIfAbsent(target, k -> new ArrayList<>());

        int id = 1;
        idSearch:
        while (true) {
            for (Note note : notes) {
                if (note.getId() == id) {
                    id++;
                    continue idSearch;
                }
            }
            break;
        }

        notes.add(new Note(id, text, author, System.currentTimeMillis()));
        notes.sort((a, b) -> Integer.compare(a.getId(), b.getId()));
        save();
        return id;
    }

    public boolean removeNote(UUID target, int id) {
        List<Note> notes = notesByPlayer.get(target);
        if (notes == null) return false;
        boolean removed = notes.removeIf(n -> n.getId() == id);
        if (removed) {
            if (notes.isEmpty()) {
                notesByPlayer.remove(target);
            }
            save();
        }
        return removed;
    }

    public List<Note> getNotes(UUID target) {
        return notesByPlayer.getOrDefault(target, new ArrayList<>());
    }

    public Map<UUID, String> getAllPlayersWithNotes() {
        Map<UUID, String> result = new LinkedHashMap<>();
        for (UUID uuid : notesByPlayer.keySet()) {
            if (!notesByPlayer.get(uuid).isEmpty()) {
                result.put(uuid, nameCache.getOrDefault(uuid, uuid.toString()));
            }
        }
        return result;
    }
}
