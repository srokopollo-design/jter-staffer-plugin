package com.jter.jterstaffers.gui;

import com.jter.jterstaffers.JterStaffers;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerListGUI {

    private static final int PAGE_SIZE = 45;

    private final JterStaffers plugin;
    private final Player viewer;
    private final int page;

    public PlayerListGUI(JterStaffers plugin, Player viewer, int page) {
        this.plugin = plugin;
        this.viewer = viewer;
        this.page = Math.max(page, 0);
    }

    public void open() {
        List<Player> online = new ArrayList<>(plugin.getServer().getOnlinePlayers());
        online.removeIf(p -> plugin.vanish().isVanished(p.getUniqueId())
                && !viewer.canSee(p) && !p.equals(viewer));

        if (online.isEmpty()) {
            plugin.messages().send(viewer, "playerlist.no-players");
            return;
        }

        int totalPages = Math.max(1, (int) Math.ceil(online.size() / (double) PAGE_SIZE));
        int safePage = Math.min(page, totalPages - 1);

        Map<String, String> ph = new HashMap<>();
        ph.put("page", (safePage + 1) + "/" + totalPages);
        String title = plugin.messages().get("playerlist.gui-title", ph);
        title = ChatColor.stripColor(title).length() > 32
                ? ChatColor.stripColor(title).substring(0, 32) : title;

        PlayerListHolder holder = new PlayerListHolder(safePage);
        Inventory inv = plugin.getServer().createInventory(holder, 54, title);
        holder.setInventory(inv);

        int start = safePage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, online.size());

        for (int i = start; i < end; i++) {
            Player target = online.get(i);
            int slot = i - start;

            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            if (meta != null) {
                meta.setOwningPlayer(target);
                meta.setDisplayName(ChatColor.YELLOW + target.getName());
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Click to view actions");
                meta.setLore(lore);
                head.setItemMeta(meta);
            }
            inv.setItem(slot, head);
            holder.putTarget(slot, target.getUniqueId());
        }

        if (safePage > 0) {
            inv.setItem(45, navItem(Material.ARROW, ChatColor.GREEN + "Previous Page"));
        }
        if (safePage < totalPages - 1) {
            inv.setItem(53, navItem(Material.ARROW, ChatColor.GREEN + "Next Page"));
        }

        viewer.openInventory(inv);
    }

    private ItemStack navItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }
}
