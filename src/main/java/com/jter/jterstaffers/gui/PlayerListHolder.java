package com.jter.jterstaffers.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerListHolder implements InventoryHolder {

    private Inventory inventory;
    private final int page;
    private final Map<Integer, UUID> slotTargets = new HashMap<>();

    public PlayerListHolder(int page) {
        this.page = page;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public int getPage() {
        return page;
    }

    public void putTarget(int slot, UUID uuid) {
        slotTargets.put(slot, uuid);
    }

    public UUID getTarget(int slot) {
        return slotTargets.get(slot);
    }
}
