package com.jter.jterstaffers.managers;

import com.jter.jterstaffers.JterStaffers;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//STAFFMODE BUILDER
public class StaffItemsManager {

    public static final String[] TOOL_IDS = {
            "freeze", "vanish", "teleport", "inspect", "invsee", "playerlist", "spectator"
    };

    private final JterStaffers plugin;
    private final NamespacedKey toolKey;

    public StaffItemsManager(JterStaffers plugin) {
        this.plugin = plugin;
        this.toolKey = new NamespacedKey(plugin, "jterstaffers_tool");
    }

    public NamespacedKey getToolKey() {
        return toolKey;
    }

    public Map<Integer, ItemStack> buildToolItems() {
        Map<Integer, ItemStack> items = new LinkedHashMap<>();
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("staffmode.items");
        if (section == null) return items;

        for (String toolId : TOOL_IDS) {
            ConfigurationSection tool = section.getConfigurationSection(toolId);
            if (tool == null) continue;
            if (!tool.getBoolean("enabled", true)) continue;

            int slot = tool.getInt("slot", -1);
            if (slot < 0 || slot > 35) continue;

            Material material = Material.matchMaterial(tool.getString("material", "STONE"));
            if (material == null) material = Material.STONE;

            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                String name = tool.getString("name", toolId);
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

                List<String> lore = new ArrayList<>();
                for (String line : tool.getStringList("lore")) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', line));
                }
                meta.setLore(lore);

                meta.getPersistentDataContainer().set(toolKey, PersistentDataType.STRING, toolId);
                item.setItemMeta(meta);
            }
            items.put(slot, item);
        }
        return items;
    }

    //TOOL RETURNER
    public String getToolId(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        if (!meta.getPersistentDataContainer().has(toolKey, PersistentDataType.STRING)) return null;
        return meta.getPersistentDataContainer().get(toolKey, PersistentDataType.STRING);
    }

    public boolean isStaffTool(ItemStack item) {
        return getToolId(item) != null;
    }
}
