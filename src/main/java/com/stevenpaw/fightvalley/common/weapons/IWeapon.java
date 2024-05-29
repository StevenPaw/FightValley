package com.stevenpaw.fightvalley.common.weapons;

import com.stevenpaw.fightvalley.common.arena.ArenaPlayer;
import org.bukkit.inventory.ItemStack;

public interface IWeapon {
    String getName();
    void activate(ArenaPlayer ap);
    void attack(ArenaPlayer ap);
    ItemStack[] getItems();
    String getWeaponDescription();
    String getWeaponShortDescription();
}
