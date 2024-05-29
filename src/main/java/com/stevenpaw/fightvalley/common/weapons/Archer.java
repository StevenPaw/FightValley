package com.stevenpaw.fightvalley.common.weapons;

import com.stevenpaw.fightvalley.common.arena.ArenaPlayer;
import com.stevenpaw.fightvalley.common.utils.Util_ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Archer implements IWeapon {

    @Override
    public String getName() {
        return "Archer";
    }

    @Override
    public void activate(ArenaPlayer ap) {
        ap.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 10, 10, false, false));
    }

    @Override
    public void attack(ArenaPlayer ap) {

    }

    @Override
    public ItemStack[] getItems() {
        ItemStack[] items = new ItemStack[2];
        items[0] = new Util_ItemBuilder(Material.BOW)
                .setDisplayName("Archer Bow")
                .setEnchantment(Enchantment.INFINITY, 1)
                .setLore("A bow for the arena")
                .build();
        items[1] = new Util_ItemBuilder(Material.ARROW)
                .setDisplayName("Archer Arrow")
                .setLore("An arrow for the arena")
                .build();
        return items;
    }

    @Override
    public String getWeaponDescription() {
        return "You can now shoot arrows! And focus with right click!";
    }

    @Override
    public String getWeaponShortDescription() {
        return "Bow & Arrow";
    }
}
