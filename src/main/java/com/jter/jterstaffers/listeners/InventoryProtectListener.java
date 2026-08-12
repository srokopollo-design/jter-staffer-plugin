package com.jter.jterstaffers.listeners;

import com.jter.jterstaffers.JterStaffers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryProtectListener implements Listener {

    private final JterStaffers plugin;

    public InventoryProtectListener(JterStaffers plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!plugin.staffMode().isInStaffMode(player.getUniqueId())) return;

        if (plugin.staffItems().isStaffTool(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
            plugin.messages().send(player, "staffmode.cannot-drop");
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (!plugin.staffMode().isInStaffMode(player.getUniqueId())) return;

        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getHolder() != null
                && !event.getClickedInventory().equals(player.getInventory())) {
            return;
        }

        ItemStack current = event.getCurrentItem();
        ItemStack cursor = event.getCursor();

        boolean touchesTool = (current != null && plugin.staffItems().isStaffTool(current))
                || (cursor != null && plugin.staffItems().isStaffTool(cursor));

        if (touchesTool && event.getClickedInventory().equals(player.getInventory())) {
            event.setCancelled(true);
            plugin.messages().send(player, "staffmode.cannot-move");
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (!plugin.staffMode().isInStaffMode(player.getUniqueId())) return;

        if (plugin.staffItems().isStaffTool(event.getOldCursor())) {
            event.setCancelled(true);
            plugin.messages().send(player, "staffmode.cannot-move");
        }
    }

    @EventHandler
    public void onSwapHands(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (!plugin.staffMode().isInStaffMode(player.getUniqueId())) return;

        if (plugin.staffItems().isStaffTool(event.getMainHandItem())
                || plugin.staffItems().isStaffTool(event.getOffHandItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!plugin.staffMode().isInStaffMode(player.getUniqueId())) return;

        if (plugin.staffItems().isStaffTool(event.getItemInHand())) {
            event.setCancelled(true);
            plugin.messages().send(player, "staffmode.cannot-place");
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (plugin.staffMode().isInStaffMode(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
