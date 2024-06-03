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

public class FrostWalker implements IWeapon {
    @Override
    public String getName() {
        return "Frost Walker";
    }

    @Override
    public void activate(ArenaPlayer ap) {
        ap.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10, 1, false, true));
        ap.getPlayer().spawnParticle(Particle.SNOWFLAKE, ap.getPlayer().getLocation(), 50);
        //Set Fire if air block
        Location loc = ap.getPlayer().getLocation();
        if(loc.getBlock().getType() == Material.AIR) {
            loc.getBlock().setBlockData(Material.POWDER_SNOW.createBlockData());
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> loc.getBlock().setBlockData(Material.AIR.createBlockData()), 20L * 7);
        }
    }

    @Override
    public void attack(ArenaPlayer ap) {
        //ap.getPlayer().sendMessage("You attacked with a fire sword!");
    }

    @Override
    public ItemStack[] getItems() {
        ItemStack[] items = new ItemStack[2];
        items[0] = new Util_ItemBuilder(Material.DIAMOND_BOOTS)
                .setDisplayName(getName() + " Boots")
                .setEnchantment(Enchantment.FROST_WALKER, 1)
                .setLore("Frost Walker Boots for the arena")
                .build();
        items[1] = new Util_ItemBuilder(Material.STONE_SWORD)
                .setDisplayName(getName() + " Sword")
                .setLore("Frost Walker Sword for the arena")
                .build();
        return items;
    }

    @Override
    public String getWeaponDescription() {
        return "You are now a frost walker! Walk over ice with that boots and freeze the air with right click!";
    }

    @Override
    public String getWeaponShortDescription() {
        return "Freeze Water and Air!";
    }

}
