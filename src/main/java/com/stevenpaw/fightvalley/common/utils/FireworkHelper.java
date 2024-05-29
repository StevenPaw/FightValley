package com.stevenpaw.fightvalley.common.utils;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkHelper {
    public static void spawnFireworks(Location location, int amount, int power, boolean flicker) {
        Location loc = location;
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK_ROCKET);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(power);
        fwm.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(flicker).build());

        fw.setFireworkMeta(fwm);
        fw.detonate();

        for(int i = 0;i<amount; i++){
            Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK_ROCKET);
            fw2.setFireworkMeta(fwm);
        }
    }
}
