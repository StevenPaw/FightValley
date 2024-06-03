package com.stevenpaw.fightvalley.common.items;

import com.stevenpaw.fightvalley.common.arena.ArenaPlayer;
import org.bukkit.inventory.ItemStack;

public interface IArenaItem {
    String getName();
    void PickupItem(ArenaPlayer ap);
    int respawnTime();
    ItemStack getItem();
}
