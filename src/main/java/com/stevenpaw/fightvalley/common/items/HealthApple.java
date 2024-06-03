package com.stevenpaw.fightvalley.common.items;

import com.stevenpaw.fightvalley.common.arena.ArenaPlayer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class HealthApple implements IArenaItem {
    @Override
    public String getName() {
        return "Health Apple";
    }

    @Override
    public void PickupItem(ArenaPlayer ap) {
        ap.getPlayer().setHealth(ap.getPlayer().getHealth() + 4);
    }

    @Override
    public int respawnTime() {
        return 30;
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(Material.APPLE);
    }
}
