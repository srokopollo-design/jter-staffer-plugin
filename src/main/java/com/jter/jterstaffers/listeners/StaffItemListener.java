package com.jter.jterstaffers.listeners;

import com.jter.jterstaffers.JterStaffers;
import com.jter.jterstaffers.gui.PlayerListGUI;
import com.jter.jterstaffers.util.Util;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StaffItemListener implements Listener {

    //MINIMUM TIME BETWEN USAGES
    private static final long COOLDOWN_MS = 300L;

    private final JterStaffers plugin;
    private final Map<UUID, Long> lastToolUse = new HashMap<>();

    public StaffItemListener(JterStaffers plugin) {
        this.plugin = plugin;
    }

    //TOOLS THAT NEED TARGET
    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        if (!plugin.staffMode().isInStaffMode(player.getUniqueId())) return;

        Entity clicked = event.getRightClicked();
        if (!(clicked instanceof Player)) return;
        Player target = (Player) clicked;

        ItemStack item = player.getInventory().getItemInMainHand();
        String toolId = plugin.staffItems().getToolId(item);
        if (toolId == null) return;

        event.setCancelled(true);

        switch (toolId) {
            case "freeze":
                if (target.equals(player)) return;
                Util.freezeToggle(player, target, plugin);
                break;
            case "inspect":
                Util.sendInspectInfo(player, target, plugin);
                break;
            case "invsee":
                if (target.equals(player)) {
                    plugin.messages().send(player, "invsee.opened-own");
                    return;
                }
                Util.openInvsee(player, target, plugin);
                break;
            default:
                break;
        }
    }

    // TOLL THAT DONT NEED TARGET
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;

        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        if (!plugin.staffMode().isInStaffMode(player.getUniqueId())) return;

        ItemStack item = event.getItem();
        String toolId = plugin.staffItems().getToolId(item);
        if (toolId == null) return;

        event.setCancelled(true);

        if (!canUse(player.getUniqueId())) return;

        switch (toolId) {
            case "vanish": {
                boolean nowVanished = plugin.vanish().toggle(player);
                plugin.messages().send(player, nowVanished ? "vanish.enabled" : "vanish.disabled");
                break;
            }
            case "teleport": {
                teleportToRandomPlayer(player);
                break;
            }
            case "playerlist": {
                new PlayerListGUI(plugin, player, 0).open();
                break;
            }
            case "spectator": {
                Util.spectatorToggle(player, plugin);
                break;
            }
            default:
                break;
        }
    }

    private void teleportToRandomPlayer(Player player) {
        List<Player> candidates = new ArrayList<>(plugin.getServer().getOnlinePlayers());
        candidates.remove(player);

        if (candidates.isEmpty()) {
            plugin.messages().send(player, "teleport.no-players-available");
            return;
        }

        Player target = candidates.get((int) (Math.random() * candidates.size()));
        Util.teleportToPlayer(player, target, plugin);
    }

    private boolean canUse(UUID uuid) {
        long now = System.currentTimeMillis();
        Long last = lastToolUse.get(uuid);
        if (last != null && now - last < COOLDOWN_MS) {
            return false;
        }
        lastToolUse.put(uuid, now);
        return true;
    }
}
