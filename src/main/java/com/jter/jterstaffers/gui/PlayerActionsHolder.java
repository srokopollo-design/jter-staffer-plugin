package com.jter.jterstaffers.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.UUID;

public class PlayerActionsHolder implements InventoryHolder {

    private Inventory inventory;
    private final UUID target;
    private final int returnToPage;

    public PlayerActionsHolder(UUID target, int returnToPage) {
        this.target = target;
        this.returnToPage = returnToPage;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public UUID getTarget() {
        return target;
    }

    public int getReturnToPage() {
        return returnToPage;
    }
}
