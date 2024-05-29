package com.stevenpaw.fightvalley.common.weapons;

import com.stevenpaw.fightvalley.common.arena.ArenaPlayer;
import com.stevenpaw.fightvalley.common.utils.Util_ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Fighter implements IWeapon {
    @Override
    public String getName() {
        return "Fighter";
    }

    @Override
    public void activate(ArenaPlayer ap) {
    }

    @Override
    public void attack(ArenaPlayer ap) {
    }

    @Override
    public ItemStack[] getItems() {
        ItemStack[] items = new ItemStack[1];
        items[0] = new Util_ItemBuilder(Material.DIAMOND_SWORD)
                .setDisplayName("Fighter Sword")
                .setLore("A sword for the arena")
                .build();
        return items;
    }

    @Override
    public String getWeaponDescription() {
        return "You are now a fighter!";
    }

    @Override
    public String getWeaponShortDescription() {
        return "Just Smash!";
    }
}
