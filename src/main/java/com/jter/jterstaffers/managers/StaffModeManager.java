package com.jter.jterstaffers.managers;

import com.jter.jterstaffers.JterStaffers;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class StaffModeManager {

    private final JterStaffers plugin;
    private final Set<UUID> inStaffMode = new HashSet<>();
    private final Map<UUID, SavedInventory> savedInventories = new HashMap<>();

    public StaffModeManager(JterStaffers plugin) {
        this.plugin = plugin;
    }

    public boolean isInStaffMode(UUID uuid) {
        return inStaffMode.contains(uuid);
    }

    //TOGLE STAFFMODE
    public boolean toggle(Player player) {
        if (isInStaffMode(player.getUniqueId())) {
            disable(player);
            return false;
        } else {
            enable(player);
            return true;
        }
    }

    public void enable(Player player) {
        UUID uuid = player.getUniqueId();
        if (isInStaffMode(uuid)) return;

        if (plugin.getConfig().getBoolean("staffmode.save-inventory", true)) {
            savedInventories.put(uuid, SavedInventory.capture(player));
            PlayerInventory inv = player.getInventory();
            inv.clear();
            inv.setArmorContents(new ItemStack[4]);
            inv.setItemInOffHand(null);
        }

        String gm = plugin.getConfig().getString("staffmode.gamemode-on-enter", "");
        if (gm != null && !gm.isEmpty()) {
            try {
                player.setGameMode(GameMode.valueOf(gm.toUpperCase()));
            } catch (IllegalArgumentException ignored) {
            }
        }

        inStaffMode.add(uuid);
        giveTools(player);
        plugin.flightVision().refresh(player);
    }

    public void disable(Player player) {
        UUID uuid = player.getUniqueId();
        if (!isInStaffMode(uuid)) return;

        inStaffMode.remove(uuid);

        // Leave vanish/spectator state cleanly when leaving staff mode
        if (plugin.vanish().isVanished(uuid)) {
            plugin.vanish().setVanished(player, false);
        }
        if (player.getGameMode() == GameMode.SPECTATOR) {
            String gm = plugin.getConfig().getString("staffmode.gamemode-on-leave", "SURVIVAL");
            try {
                player.setGameMode(GameMode.valueOf(gm.toUpperCase()));
            } catch (IllegalArgumentException e) {
                player.setGameMode(GameMode.SURVIVAL);
            }
        }
        if (plugin.freeze().isFrozen(uuid)) {
            plugin.freeze().setFrozen(player, false);
        }

        SavedInventory saved = savedInventories.remove(uuid);
        if (saved != null) {
            saved.restore(player);
        }

        plugin.flightVision().refresh(player);
    }

    public void giveTools(Player player) {
        Map<Integer, ItemStack> tools = plugin.staffItems().buildToolItems();
        for (Map.Entry<Integer, ItemStack> entry : tools.entrySet()) {
            player.getInventory().setItem(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Called on plugin disable to restore inventories of anyone still in staff mode,
     * so nobody loses their items due to a reload/restart.
     */
    public void disableAllOnShutdown() {
        for (UUID uuid : new HashSet<>(inStaffMode)) {
            Player player = plugin.getServer().getPlayer(uuid);
            if (player != null) {
                disable(player);
            }
        }
    }

    private static class SavedInventory {
        private final ItemStack[] contents;
        private final ItemStack[] armor;
        private final ItemStack offHand;
        private final float exp;
        private final int level;
        private final int foodLevel;

        private SavedInventory(ItemStack[] contents, ItemStack[] armor, ItemStack offHand,
                                float exp, int level, int foodLevel) {
            this.contents = contents;
            this.armor = armor;
            this.offHand = offHand;
            this.exp = exp;
            this.level = level;
            this.foodLevel = foodLevel;
        }

        static SavedInventory capture(Player player) {
            PlayerInventory inv = player.getInventory();
            return new SavedInventory(
                    inv.getContents().clone(),
                    inv.getArmorContents().clone(),
                    inv.getItemInOffHand().clone(),
                    player.getExp(),
                    player.getLevel(),
                    player.getFoodLevel()
            );
        }

        void restore(Player player) {
            PlayerInventory inv = player.getInventory();
            inv.clear();
            inv.setContents(contents);
            inv.setArmorContents(armor);
            inv.setItemInOffHand(offHand);
            player.setExp(exp);
            player.setLevel(level);
            player.setFoodLevel(foodLevel);
            player.updateInventory();
        }
    }
}
