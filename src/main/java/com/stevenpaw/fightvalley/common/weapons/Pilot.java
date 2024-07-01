package com.stevenpaw.fightvalley.common.weapons;

import com.stevenpaw.fightvalley.common.arena.ArenaPlayer;
import com.stevenpaw.fightvalley.common.utils.Util_ItemBuilder;
import com.stevenpaw.fightvalley.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class Pilot implements IWeapon {

    @Override
    public String getName() {
        return "Pilot";
    }

    @Override
    public void activate(ArenaPlayer ap) {

    }

    @Override
    public void attack(ArenaPlayer ap) {

    }

    @Override
    public ItemStack[] getItems() {
        ItemStack[] items = new ItemStack[3];
        items[0] = new Util_ItemBuilder(Material.ELYTRA)
                .setDisplayName("Wings")
                .setLore("Wings for the arena")
                .build();
        items[1] = new Util_ItemBuilder(Material.IRON_SWORD)
                .setDisplayName("Pilots Sword")
                .setLore("A wooden sword for the arena")
                .build();
        items[2] = new Util_ItemBuilder(Material.FIREWORK_ROCKET)
                .setDisplayName("Rocket Fuel")
                .setLore("Fuel for your wings")
                .setAmount(64)
                .build();
        return items;
    }

    @Override
    public String getWeaponDescription() {
        return "You can now fly with an elytra and fight with your sword!";
    }

    @Override
    public String getWeaponShortDescription() {
        return "Fly up!";
    }
}
