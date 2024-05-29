package com.stevenpaw.fightvalley.common.weapons;

import com.stevenpaw.fightvalley.common.arena.ArenaPlayer;
import com.stevenpaw.fightvalley.common.utils.Util_ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedWalker implements IWeapon{

    @Override
    public String getName() {
        return "Speed Walker";
    }

    @Override
    public void activate(ArenaPlayer ap) {
        ap.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10, 3, false, true));
        ap.getPlayer().spawnParticle(Particle.CLOUD, ap.getPlayer().getLocation(), 50);
    }

    @Override
    public void attack(ArenaPlayer ap) {

    }

    @Override
    public ItemStack[] getItems() {
        ItemStack[] items = new ItemStack[2];
        items[0] = new Util_ItemBuilder(Material.DIAMOND_LEGGINGS)
                .setDisplayName(getName() + " Boots")
                .setEnchantment(Enchantment.SWIFT_SNEAK, 1)
                .setLore("Speed Walker Boots for the arena")
                .build();
        items[1] = new Util_ItemBuilder(Material.STONE_SWORD)
                .setDisplayName(getName() + " Sword")
                .setLore("Speed Walker Sword for the arena")
                .build();
        return items;
    }

    @Override
    public String getWeaponDescription() {
        return "You are now a speed walker! Walk faster with every right click!";
    }

    @Override
    public String getWeaponShortDescription() {
        return "Speed with rightclick!";
    }
}
