package com.jter.jterstaffers.util;

import com.jter.jterstaffers.JterStaffers;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Util {

    public static void openInvsee(Player staff, Player target, JterStaffers plugin) {
        staff.openInventory(target.getInventory());
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("player", target.getName());
        plugin.messages().send(staff, "invsee.opened", placeholders);
    }

    public static void sendInspectInfo(Player staff, Player target, JterStaffers plugin) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");

        Map<String, String> ph = new HashMap<>();
        ph.put("player", target.getName());
        staff.sendMessage(plugin.messages().get("inspect.header", ph));

        Map<String, String> p1 = new HashMap<>();
        p1.put("uuid", target.getUniqueId().toString());
        staff.sendMessage(plugin.messages().get("inspect.line-uuid", p1));

        Map<String, String> p2 = new HashMap<>();
        p2.put("gamemode", target.getGameMode().name());
        staff.sendMessage(plugin.messages().get("inspect.line-gamemode", p2));

        Map<String, String> p3 = new HashMap<>();
        p3.put("health", String.valueOf((int) target.getHealth()));
        p3.put("maxhealth", String.valueOf((int) target.getAttribute(
                org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue()));
        staff.sendMessage(plugin.messages().get("inspect.line-health", p3));

        Map<String, String> p4 = new HashMap<>();
        p4.put("food", String.valueOf(target.getFoodLevel()));
        staff.sendMessage(plugin.messages().get("inspect.line-food", p4));

        Map<String, String> p5 = new HashMap<>();
        p5.put("level", String.valueOf(target.getLevel()));
        staff.sendMessage(plugin.messages().get("inspect.line-level", p5));

        Map<String, String> p6 = new HashMap<>();
        p6.put("world", target.getWorld().getName());
        staff.sendMessage(plugin.messages().get("inspect.line-world", p6));

        Map<String, String> p7 = new HashMap<>();
        p7.put("x", String.valueOf(target.getLocation().getBlockX()));
        p7.put("y", String.valueOf(target.getLocation().getBlockY()));
        p7.put("z", String.valueOf(target.getLocation().getBlockZ()));
        staff.sendMessage(plugin.messages().get("inspect.line-location", p7));

        Map<String, String> p8 = new HashMap<>();
        String ip = "unknown";
        try {
            if (target.getAddress() != null) {
                ip = target.getAddress().getAddress().getHostAddress();
            }
        } catch (Exception ignored) {
        }
        p8.put("ip", ip);
        staff.sendMessage(plugin.messages().get("inspect.line-ip", p8));

        Map<String, String> p9 = new HashMap<>();
        p9.put("ping", String.valueOf(target.getPing()));
        staff.sendMessage(plugin.messages().get("inspect.line-ping", p9));

        Map<String, String> p10 = new HashMap<>();
        p10.put("date", sdf.format(new java.util.Date(target.getFirstPlayed())));
        staff.sendMessage(plugin.messages().get("inspect.line-first-join", p10));

        Map<String, String> p11 = new HashMap<>();
        p11.put("vanished", plugin.vanish().isVanished(target.getUniqueId()) ? "Yes" : "No");
        staff.sendMessage(plugin.messages().get("inspect.line-vanished", p11));

        Map<String, String> p12 = new HashMap<>();
        p12.put("frozen", plugin.freeze().isFrozen(target.getUniqueId()) ? "Yes" : "No");
        staff.sendMessage(plugin.messages().get("inspect.line-frozen", p12));

        staff.sendMessage(plugin.messages().get("inspect.footer"));
    }

    public static void freezeToggle(Player staff, Player target, JterStaffers plugin) {
        if (target.hasPermission("jterstaffers.staffmode") && !staff.getUniqueId().equals(target.getUniqueId())) {
            // Allow freezing other staff optionally disabled - kept permissive but informative
        }
        boolean nowFrozen = plugin.freeze().toggle(target);
        Map<String, String> ph = new HashMap<>();
        ph.put("player", target.getName());
        plugin.messages().send(staff, nowFrozen ? "freeze.froze" : "freeze.unfroze", ph);

        if (nowFrozen) {
            plugin.messages().send(target, "freeze.you-were-frozen");
        } else {
            plugin.messages().send(target, "freeze.you-were-unfrozen");
        }
    }

    public static void teleportToPlayer(Player staff, Player target, JterStaffers plugin) {
        staff.teleport(target.getLocation());
        Map<String, String> ph = new HashMap<>();
        ph.put("player", target.getName());
        plugin.messages().send(staff, "teleport.teleported-to", ph);
    }

    public static void spectatorToggle(Player player, JterStaffers plugin) {
        if (player.getGameMode() == org.bukkit.GameMode.SPECTATOR) {
            player.setGameMode(org.bukkit.GameMode.SURVIVAL);
            plugin.messages().send(player, "spectator.disabled");
        } else {
            player.setGameMode(org.bukkit.GameMode.SPECTATOR);
            plugin.messages().send(player, "spectator.enabled");
        }
    }
}
