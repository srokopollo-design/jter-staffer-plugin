package com.jter.jterstaffers.managers;

import com.jter.jterstaffers.JterStaffers;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class MessagesManager {

    private final JterStaffers plugin;
    private File file;
    private FileConfiguration config;

    public MessagesManager(JterStaffers plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);

        try (InputStream defStream = plugin.getResource("messages.yml")) {
            if (defStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(
                        new InputStreamReader(defStream, StandardCharsets.UTF_8));
                config.setDefaults(defConfig);
                config.options().copyDefaults(true);
                save();
            }
        } catch (IOException ignored) {
        }
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not save messages.yml: " + e.getMessage());
        }
    }

    private String prefix() {
        return color(config.getString("prefix", ""));
    }

    private String color(String input) {
        if (input == null) return "";
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public String get(String path, Map<String, String> placeholders) {
        String raw = config.getString(path);
        if (raw == null) {
            return ChatColor.RED + "Missing message: " + path;
        }
        raw = raw.replace("{prefix}", config.getString("prefix", ""));
        if (placeholders != null) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                raw = raw.replace("{" + entry.getKey() + "}", entry.getValue());
            }
        }
        return color(raw);
    }

    public String get(String path) {
        return get(path, null);
    }

    public void send(CommandSender sender, String path, Map<String, String> placeholders) {
        sender.sendMessage(get(path, placeholders));
    }

    public void send(CommandSender sender, String path) {
        send(sender, path, null);
    }

    public java.util.List<String> getList(String path) {
        java.util.List<String> raw = config.getStringList(path);
        java.util.List<String> result = new java.util.ArrayList<>();
        for (String s : raw) {
            result.add(color(s));
        }
        return result;
    }

    public FileConfiguration raw() {
        return config;
    }
}
