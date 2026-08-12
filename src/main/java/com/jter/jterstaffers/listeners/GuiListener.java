package com.jter.jterstaffers.listeners;

import com.jter.jterstaffers.JterStaffers;
import com.jter.jterstaffers.gui.PlayerActionsGUI;
import com.jter.jterstaffers.gui.PlayerActionsHolder;
import com.jter.jterstaffers.gui.PlayerListGUI;
import com.jter.jterstaffers.gui.PlayerListHolder;
import com.jter.jterstaffers.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class GuiListener implements Listener {

    private final JterStaffers plugin;

    public GuiListener(JterStaffers plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player viewer = (Player) event.getWhoClicked();

        if (event.getInventory().getHolder() instanceof PlayerListHolder) {
            event.setCancelled(true);
            handlePlayerListClick(event, viewer, (PlayerListHolder) event.getInventory().getHolder());
        } else if (event.getInventory().getHolder() instanceof PlayerActionsHolder) {
            event.setCancelled(true);
            handleActionsClick(event, viewer, (PlayerActionsHolder) event.getInventory().getHolder());
        }
    }

    private void handlePlayerListClick(InventoryClickEvent event, Player viewer, PlayerListHolder holder) {
        int slot = event.getRawSlot();
        if (slot < 0 || slot >= event.getInventory().getSize()) return;

        if (slot == 45) {
            new PlayerListGUI(plugin, viewer, holder.getPage() - 1).open();
            return;
        }
        if (slot == 53) {
            new PlayerListGUI(plugin, viewer, holder.getPage() + 1).open();
            return;
        }

        UUID targetUuid = holder.getTarget(slot);
        if (targetUuid == null) return;

        Player target = Bukkit.getPlayer(targetUuid);
        if (target == null) {
            plugin.messages().send(viewer, "general.player-not-online",
                    java.util.Collections.singletonMap("player", "that player"));
            return;
        }

        new PlayerActionsGUI(plugin, viewer, target, holder.getPage()).open();
    }

    private void handleActionsClick(InventoryClickEvent event, Player viewer, PlayerActionsHolder holder) {
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;

        Player target = Bukkit.getPlayer(holder.getTarget());
        if (target == null) {
            plugin.messages().send(viewer, "general.player-not-online",
                    java.util.Collections.singletonMap("player", "that player"));
            viewer.closeInventory();
            return;
        }

        ItemMeta meta = clicked.getItemMeta();
        String name = ChatColor.stripColor(meta.getDisplayName());

        switch (name) {
            case "Info":
                viewer.closeInventory();
                Util.sendInspectInfo(viewer, target, plugin);
                break;
            case "Teleport":
                viewer.closeInventory();
                Util.teleportToPlayer(viewer, target, plugin);
                break;
            case "Invsee":
                Util.openInvsee(viewer, target, plugin);
                break;
            case "Freeze":
            case "Unfreeze":
                Util.freezeToggle(viewer, target, plugin);
                new PlayerActionsGUI(plugin, viewer, target, holder.getReturnToPage()).open();
                break;
            case "Spectator":
            case "Exit Spectator":
                Util.spectatorToggle(target, plugin);
                new PlayerActionsGUI(plugin, viewer, target, holder.getReturnToPage()).open();
                break;
            case "Back":
                new PlayerListGUI(plugin, viewer, holder.getReturnToPage()).open();
                break;
            default:
                break;
        }
    }
}
