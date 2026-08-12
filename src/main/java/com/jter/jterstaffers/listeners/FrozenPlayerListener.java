package com.jter.jterstaffers.listeners;

import com.jter.jterstaffers.JterStaffers;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class FrozenPlayerListener implements Listener {

    private final JterStaffers plugin;

    public FrozenPlayerListener(JterStaffers plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!plugin.freeze().isFrozen(player.getUniqueId())) return;
        if (event.getTo() == null) return;

        Location from = event.getFrom();
        Location to = event.getTo();

        // ALOW ONLY MOVE VISUAL
        if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
            Location frozenAt = plugin.freeze().getFrozenLocation(player.getUniqueId());
            if (frozenAt == null) {
                frozenAt = from;
            }
            Location fixed = frozenAt.clone();
            fixed.setYaw(to.getYaw());
            fixed.setPitch(to.getPitch());
            event.setTo(fixed);
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (!plugin.freeze().isFrozen(player.getUniqueId())) return;

        event.setCancelled(true);
        plugin.messages().send(player, "freeze.cannot-use-command");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!plugin.freeze().isFrozen(player.getUniqueId())) return;
        if (!plugin.getConfig().getBoolean("freeze.allow-chat", true)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.freeze().clear(event.getPlayer().getUniqueId());
    }
}
