package com.stevenpaw.fightvalley.common.weapons;

import com.stevenpaw.fightvalley.common.arena.ArenaPlayer;
import com.stevenpaw.fightvalley.common.utils.Util_ItemBuilder;
import com.stevenpaw.fightvalley.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class Siren implements IWeapon {

    @Override
    public String getName() {
        return "Siren";
    }

    @Override
    public void activate(ArenaPlayer ap) {

    }

    @Override
    public void attack(ArenaPlayer ap) {

    }

    @Override
    public ItemStack[] getItems() {
        ItemStack[] items = new ItemStack[2];
        items[0] = new Util_ItemBuilder(Material.TRIDENT)
                .setDisplayName("Siren Trident")
                .setEnchantment(Enchantment.CHANNELING, 1)
                .setEnchantment(Enchantment.LOYALTY, 3)
                .setLore("A Trident for the arena")
                .build();
        items[1] = new Util_ItemBuilder(Material.TURTLE_HELMET)
                .setDisplayName("Siren Helmet")
                .setLore("A Helmet for the arena")
                .build();
        return items;
    }

    @Override
    public String getWeaponDescription() {
        return "You can now throw tridents on people and summon thunder wherever a trident lands!";
    }

    @Override
    public String getWeaponShortDescription() {
        return "Throw Tridents with thunder!";
    }
}
