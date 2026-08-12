package com.jter.jterstaffers.managers;

import com.jter.jterstaffers.JterStaffers;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class FreezeManager {

    private final JterStaffers plugin;
    private final Set<UUID> frozen = new HashSet<>();
    private final Map<UUID, Location> frozenLocation = new HashMap<>();

    public FreezeManager(JterStaffers plugin) {
        this.plugin = plugin;
    }

    public boolean isFrozen(UUID uuid) {
        return frozen.contains(uuid);
    }

    public boolean toggle(Player target) {
        boolean newState = !isFrozen(target.getUniqueId());
        setFrozen(target, newState);
        return newState;
    }

    public void setFrozen(Player target, boolean state) {
        UUID uuid = target.getUniqueId();
        if (state) {
            frozen.add(uuid);
            frozenLocation.put(uuid, target.getLocation().clone());
        } else {
            frozen.remove(uuid);
            frozenLocation.remove(uuid);
        }
    }

    public Location getFrozenLocation(UUID uuid) {
        return frozenLocation.get(uuid);
    }

    public void updateFrozenLocation(UUID uuid, Location location) {
        frozenLocation.put(uuid, location);
    }

    public void clear(UUID uuid) {
        frozen.remove(uuid);
        frozenLocation.remove(uuid);
    }
}
