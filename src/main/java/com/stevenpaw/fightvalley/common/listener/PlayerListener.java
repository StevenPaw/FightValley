package com.stevenpaw.fightvalley.common.listener;

import com.stevenpaw.fightvalley.common.arena.Arena;
import com.stevenpaw.fightvalley.common.arena.ArenaPlayer;
import com.stevenpaw.fightvalley.common.database.SQL_ArenaSpawn;
import com.stevenpaw.fightvalley.common.database.SQL_Player;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.List;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if(ArenaPlayer.isInArena(event.getPlayer())) {
            Player victim = event.getPlayer();
            if (victim.getKiller() != null) {
                ArenaPlayer player = ArenaPlayer.GetArenaPlayer(victim);
                player.addDeath();
                event.setDeathMessage(null);

                Player killer = victim.getKiller();
                if (killer != null) {
                    ArenaPlayer killerPlayer = ArenaPlayer.GetArenaPlayer(killer);
                    killerPlayer.addKill();
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if(ArenaPlayer.isInArena(event.getPlayer())){
            Player p = event.getPlayer();
            ArenaPlayer player = ArenaPlayer.GetArenaPlayer(p);
            List<Location> allSpawns = SQL_ArenaSpawn.getSpawnLocations(player.getCurrentArena().getName());
            Location randomSpawn = allSpawns.get((int) (Math.random() * allSpawns.size()));
            event.setRespawnLocation(randomSpawn);
            player.getCurrentArena().setRandomWeapon(player);
        }
    }

    @EventHandler
    public void OnInteract(PlayerInteractEvent event) {
        if(ArenaPlayer.isInArena(event.getPlayer())){
            Player p = event.getPlayer();
            ArenaPlayer player = ArenaPlayer.GetArenaPlayer(p);

            if(event.getAction() == Action.LEFT_CLICK_AIR) {
                player.getCurrentWeapon().attack(player);
            }

            if(event.getAction() == Action.RIGHT_CLICK_AIR) {
                player.getCurrentWeapon().activate(player);
            }
        }
    }

    @EventHandler
    public void OnPlayerQuit(PlayerQuitEvent event) {
        if(ArenaPlayer.isInArena(event.getPlayer())){
            Player p = event.getPlayer();
            ArenaPlayer player = ArenaPlayer.GetArenaPlayer(p);
            player.getCurrentArena().leaveArena(player);
        }
    }

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event) {
        if(SQL_Player.getString(event.getPlayer().getUniqueId(), "CurrentArena") != null){
            ArenaPlayer ap = ArenaPlayer.GetArenaPlayer(event.getPlayer());
            if(ap.getCurrentArena() != null){
                event.getPlayer().teleport(ap.getCurrentArena().getExit());
                ap.getCurrentArena().leaveArena(ap);
            }
            SQL_Player.setString(event.getPlayer().getUniqueId(), "CurrentArena", "");
        }
    }
}
