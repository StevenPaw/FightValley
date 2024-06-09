package com.stevenpaw.fightvalley.common.weapons;

import com.stevenpaw.fightvalley.common.arena.ArenaPlayer;
import com.stevenpaw.fightvalley.common.utils.Util_ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Jumper implements IWeapon {

    @Override
    public String getName() {
        return "Jumper";
    }

    @Override
    public void activate(ArenaPlayer ap) {
        ap.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 30, 2, false, false));
    }

    @Override
    public void attack(ArenaPlayer ap) {

    }

    @Override
    public ItemStack[] getItems() {
        ItemStack[] items = new ItemStack[1];
        items[0] = new Util_ItemBuilder(Material.STONE_SHOVEL)
                .setDisplayName("Jumper Axe")
                .setEnchantment(Enchantment.KNOCKBACK, 1)
                .setLore("An Axe for the arena")
                .build();
        return items;
    }

    @Override
    public String getWeaponDescription() {
        return "You can now activate Jump Boost with right click! And slash players away with your axe!";
    }

    @Override
    public String getWeaponShortDescription() {
        return "Jump higher and smash away!";
    }
}
