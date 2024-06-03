package com.stevenpaw.fightvalley.common.arena;

import com.stevenpaw.fightvalley.common.items.IArenaItem;
import org.bukkit.World;

public class ArenaItemSpawn {
    private World world;
    private int x;
    private int y;
    private int z;
    private IArenaItem item;
    private int curTime = 0;
    boolean isRespawned = false;

    public ArenaItemSpawn(World world, int x, int y, int z, IArenaItem item) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.item = item;

        curTime = item.respawnTime();
    }

    public void Tick() {
        if(curTime >= 0) {
            curTime--;
        } else if (!isRespawned) {
            //Drop Item in world
            world.dropItem(world.getBlockAt(x, y, z).getLocation(), item.getItem());
            isRespawned = true;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public IArenaItem getItem() {
        return item;
    }

    public boolean isOnCoordinates(World world, int x, int y, int z) {
        return this.world == world && this.x == x && this.y == y && this.z == z;
    }

    public void PickUp(ArenaPlayer ap) {
        item.PickupItem(ap);
        curTime = item.respawnTime();
        isRespawned = false;
    }
}
