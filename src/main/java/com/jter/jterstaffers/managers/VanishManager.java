package com.jter.jterstaffers.managers;

import com.jter.jterstaffers.JterStaffers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class VanishManager {

    private final JterStaffers plugin;
    private final Set<UUID> vanished = new HashSet<>();

    public VanishManager(JterStaffers plugin) {
        this.plugin = plugin;
    }

    public boolean isVanished(UUID uuid) {
        return vanished.contains(uuid);
    }

    public boolean toggle(Player player) {
        boolean newState = !isVanished(player.getUniqueId());
        setVanished(player, newState);
        return newState;
    }

    public void setVanished(Player player, boolean state) {
        UUID uuid = player.getUniqueId();
        if (state) {
            vanished.add(uuid);
        } else {
            vanished.remove(uuid);
        }

        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.getUniqueId().equals(uuid)) continue;

            boolean canSee = online.hasPermission("jterstaffers.staffmode") || online.isOp();

            if (state && !canSee) {
                online.hidePlayer(plugin, player);
            } else {
                online.showPlayer(plugin, player);
            }
        }

        if (state) {
            player.setPlayerListName(null);
        }

        plugin.flightVision().refresh(player);

        if (plugin.getConfig().getBoolean("vanish.notify-console", true)) {
            String path = state ? "vanish.now-vanished-console" : "vanish.now-visible-console";
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("player", player.getName());
            plugin.getLogger().info(stripColor(plugin.messages().get(path, placeholders)));
        }
    }

    //VANISH VISIBILITI
    public void applyVisibility(Player viewer) {
        boolean canSeeVanished = viewer.hasPermission("jterstaffers.staffmode") || viewer.isOp();
        for (UUID uuid : vanished) {
            Player vanishedPlayer = Bukkit.getPlayer(uuid);
            if (vanishedPlayer == null || vanishedPlayer.equals(viewer)) continue;
            if (canSeeVanished) {
                viewer.showPlayer(plugin, vanishedPlayer);
            } else {
                viewer.hidePlayer(plugin, vanishedPlayer);
            }
        }
    }

    public void clear(UUID uuid) {
        vanished.remove(uuid);
    }

    private String stripColor(String input) {
        return org.bukkit.ChatColor.stripColor(input);
    }
}
