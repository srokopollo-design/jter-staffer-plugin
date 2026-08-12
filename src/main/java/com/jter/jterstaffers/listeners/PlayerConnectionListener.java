package com.jter.jterstaffers.listeners;

import com.jter.jterstaffers.JterStaffers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnectionListener implements Listener {

    private final JterStaffers plugin;

    public PlayerConnectionListener(JterStaffers plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.vanish().applyVisibility(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.vanish().clear(event.getPlayer().getUniqueId());
        plugin.freeze().clear(event.getPlayer().getUniqueId());
        plugin.flightVision().clear(event.getPlayer().getUniqueId());

        if (plugin.staffMode().isInStaffMode(event.getPlayer().getUniqueId())) {
            plugin.staffMode().disable(event.getPlayer());
        }
    }
}
