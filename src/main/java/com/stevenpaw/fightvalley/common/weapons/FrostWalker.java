package com.stevenpaw.fightvalley.common.weapons;

import com.stevenpaw.fightvalley.common.arena.ArenaPlayer;
import com.stevenpaw.fightvalley.common.utils.Util_ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class FrostWalker implements IWeapon {
    @Override
    public String getName() {
        return "Frost Walker";
    }

    @Override
    public void activate(ArenaPlayer ap) {
        //ap.getPlayer().sendMessage("You are in an arena and using a sword!");
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
        return "You are now a frost walker! Walk over ice with that boots!";
    }

    @Override
    public String getWeaponShortDescription() {
        return "Walk over water!";
    }

}
