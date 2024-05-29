package com.stevenpaw.fightvalley.common.weapons;

import com.stevenpaw.fightvalley.common.arena.ArenaPlayer;
import com.stevenpaw.fightvalley.common.utils.Util_ItemBuilder;
import com.stevenpaw.fightvalley.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FireBender implements IWeapon {
    @Override
    public String getName() {
        return "Fire Bender";
    }

    @Override
    public void activate(ArenaPlayer ap) {
        ap.getPlayer().spawnParticle(Particle.FLAME, ap.getPlayer().getLocation(), 50);
        //set fire to the block in front of the player
        ap.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 100, 1, false, true));

        //Set Fire if air block
        Location loc = ap.getPlayer().getLocation();
        if(loc.getBlock().getType() == Material.AIR) {
            loc.getBlock().setBlockData(Material.FIRE.createBlockData());
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> loc.getBlock().setBlockData(Material.AIR.createBlockData()), 20L * 5);
        }
    }

    @Override
    public void attack(ArenaPlayer ap) {
    }

    @Override
    public ItemStack[] getItems() {
        ItemStack[] items = new ItemStack[1];
        items[0] = new ItemStack(new Util_ItemBuilder(Material.IRON_SWORD)
                .setDisplayName(getName())
                .setEnchanted()
                .setEnchantment(Enchantment.FIRE_ASPECT, 1)
                .setLore("A fire sword for the arena")
                .build());
        return items;
    }

    @Override
    public String getWeaponDescription() {
        return "You are now a fire bender! Use right click to spawn fire and get resistance to fire!";
    }

    @Override
    public String getWeaponShortDescription() {
        return "Right click for fire!";
    }
}
