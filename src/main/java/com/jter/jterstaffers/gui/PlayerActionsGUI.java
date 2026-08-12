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
import java.util.UUID;

public class PlayerActionsGUI {

    private final JterStaffers plugin;
    private final Player viewer;
    private final Player target;
    private final int returnToPage;

    public PlayerActionsGUI(JterStaffers plugin, Player viewer, Player target, int returnToPage) {
        this.plugin = plugin;
        this.viewer = viewer;
        this.target = target;
        this.returnToPage = returnToPage;
    }

    public void open() {
        Map<String, String> ph = new HashMap<>();
        ph.put("player", target.getName());
        String title = plugin.messages().get("playerlist.actions-gui-title", ph);
        String stripped = ChatColor.stripColor(title);
        if (stripped.length() > 32) title = stripped.substring(0, 32);

        PlayerActionsHolder holder = new PlayerActionsHolder(target.getUniqueId(), returnToPage);
        Inventory inv = plugin.getServer().createInventory(holder, 27, title);
        holder.setInventory(inv);

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        if (headMeta != null) {
            headMeta.setOwningPlayer(target);
            headMeta.setDisplayName(ChatColor.YELLOW + target.getName());
            head.setItemMeta(headMeta);
        }
        inv.setItem(4, head);

        inv.setItem(10, namedItem(Material.SPYGLASS, ChatColor.YELLOW + "Info",
                "&7Show detailed player information"));
        inv.setItem(12, namedItem(Material.ENDER_PEARL, ChatColor.LIGHT_PURPLE + "Teleport",
                "&7Teleport to this player"));
        inv.setItem(14, namedItem(Material.CHEST, ChatColor.GOLD + "Invsee",
                "&7Open/edit this player's inventory"));
        inv.setItem(16, namedItem(Material.BLAZE_ROD,
                plugin.freeze().isFrozen(target.getUniqueId()) ? ChatColor.GREEN + "Unfreeze" : ChatColor.RED + "Freeze",
                "&7Toggle freeze for this player"));
        inv.setItem(20, namedItem(Material.ELYTRA,
                target.getGameMode() == org.bukkit.GameMode.SPECTATOR ? ChatColor.GREEN + "Exit Spectator" : ChatColor.GRAY + "Spectator",
                "&7Toggle spectator mode for this player"));
        inv.setItem(22, namedItem(Material.ARROW, ChatColor.WHITE + "Back",
                "&7Return to the player list"));

        viewer.openInventory(inv);
    }

    private ItemStack namedItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            List<String> loreList = new ArrayList<>();
            for (String line : lore) {
                loreList.add(ChatColor.translateAlternateColorCodes('&', line));
            }
            meta.setLore(loreList);
            item.setItemMeta(meta);
        }
        return item;
    }
}
