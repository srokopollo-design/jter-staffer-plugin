package com.jter.jterstaffers.managers;

import com.jter.jterstaffers.JterStaffers;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

//MANAGES FLY & EFFECTS
public class FlightVisionManager {

    private final JterStaffers plugin;
    private final Set<UUID> boosted = new HashSet<>();
    private final Map<UUID, Boolean> savedAllowFlight = new HashMap<>();
    private final Map<UUID, Boolean> savedFlying = new HashMap<>();

    public FlightVisionManager(JterStaffers plugin) {
        this.plugin = plugin;
    }


    public void refresh(Player player) {
        UUID uuid = player.getUniqueId();
        boolean shouldBoost = plugin.staffMode().isInStaffMode(uuid) || plugin.vanish().isVanished(uuid);
        boolean isBoosted = boosted.contains(uuid);

        if (shouldBoost && !isBoosted) {
            savedAllowFlight.put(uuid, player.getAllowFlight());
            savedFlying.put(uuid, player.isFlying());
            boosted.add(uuid);

            if (plugin.getConfig().getBoolean("staffmode.fly", true)) {
                player.setAllowFlight(true);
            }
            if (plugin.getConfig().getBoolean("staffmode.night-vision", true)) {
                player.addPotionEffect(new PotionEffect(
                        PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false, false));
            }
        } else if (!shouldBoost && isBoosted) {
            boosted.remove(uuid);

            player.removePotionEffect(PotionEffectType.NIGHT_VISION);

            Boolean originalAllowFlight = savedAllowFlight.remove(uuid);
            Boolean originalFlying = savedFlying.remove(uuid);

            boolean allowFlight = originalAllowFlight != null && originalAllowFlight;
            player.setAllowFlight(allowFlight);
            player.setFlying(allowFlight && originalFlying != null && originalFlying);
        }
    }

    public void clear(UUID uuid) {
        boosted.remove(uuid);
        savedAllowFlight.remove(uuid);
        savedFlying.remove(uuid);
    }
}
