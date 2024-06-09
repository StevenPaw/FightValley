package com.stevenpaw.fightvalley.common.listener;

import com.stevenpaw.fightvalley.common.arena.Arena;
import com.stevenpaw.fightvalley.common.arena.ArenaPlayer;
import com.stevenpaw.fightvalley.common.database.SQL_ArenaSpawn;
import com.stevenpaw.fightvalley.common.database.SQL_Player;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

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

            switch(event.getAction()) {
                case LEFT_CLICK_AIR, LEFT_CLICK_BLOCK:
                    player.getCurrentWeapon().attack(player);
                    break;
                case RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK:
                    player.getCurrentWeapon().activate(player);
                    break;
                case PHYSICAL:
                    break;
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
        if(ArenaPlayer.isInArena(event.getPlayer())){
            ArenaPlayer ap = ArenaPlayer.GetArenaPlayer(event.getPlayer());
            if(ap.getCurrentArena() != null){
                event.getPlayer().teleport(ap.getCurrentArena().getExit());
                ap.getCurrentArena().leaveArena(ap);
            }
            SQL_Player.setString(event.getPlayer().getUniqueId(), "CurrentArena", "");
        }
    }

    /**
     * Führt Aktionen aus, wenn ein Spieler ein Entity anklickt
     * @param event (Event) = PlayerInteractEntityEvent
     */
    @EventHandler
    public void onPlayerEntityInteract(PlayerInteractEntityEvent event)	{
        if(ArenaPlayer.isInArena(event.getPlayer())){
            Player p = event.getPlayer();
            ArenaPlayer player = ArenaPlayer.GetArenaPlayer(p);

            if(event.getRightClicked() instanceof Player) {
                player.getCurrentWeapon().activate(player);
            } else if (event.getRightClicked() instanceof ArmorStand) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Führt Aktionen aus, wenn ein Spieler ein Entity anklickt
     * @param event (Event) = PlayerInteractAtEntityEvent
     */
    @EventHandler
    public void onPlayerInteractAtEntityEvent(PlayerInteractAtEntityEvent event){
        if(ArenaPlayer.isInArena(event.getPlayer())){
            Player p = event.getPlayer();
            ArenaPlayer player = ArenaPlayer.GetArenaPlayer(p);

            if (event.getRightClicked() instanceof Player) {
                player.getCurrentWeapon().activate(player);
            } else if (event.getRightClicked() instanceof ArmorStand) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Hunger erhöhen
     * @param event (Event) = FoodLevelChangeEvent
     */
    @EventHandler
    public void onHungerDepletion(FoodLevelChangeEvent event) {
        Player p = (Player) event.getEntity();
        if(ArenaPlayer.isInArena(p)){
            event.setCancelled(true);
        }
    }

    /**
     * Verhindert, dass Spieler Items droppen können, wenn sie nicht Builder sind
     * @param event (Event) = PlayerDropItemEvent
     */
    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if(ArenaPlayer.isInArena(event.getPlayer())){
            event.setCancelled(true);
        }
    }

    /**
     * Bei der Aufnahme von Gold wird Geld gegeben
     * @param event (Event) = EntityPickupItemEvent
     */
    @EventHandler
    public void onPickupItem(EntityPickupItemEvent event) {
        if(event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();

            if(ArenaPlayer.isInArena(p)){
                if (event.getItem().getItemStack().equals(new ItemStack(Material.APPLE)))
                {
                    event.setCancelled(true);
                    p.setHealth(p.getHealth()+4);
                }
                //TODO: Add Pickup Items
            }
        }
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        if(ArenaPlayer.isInArena(event.getPlayer())){
            String[] splittedMessage = event.getMessage().split(" ");
            if(!splittedMessage[0].equalsIgnoreCase("/fv") && !splittedMessage[0].equalsIgnoreCase("/fightvalley")) {
                event.getPlayer().sendMessage(ChatColor.RED + "Du kannst in der Arena keine Befehle ausführen!" + ChatColor.GRAY + " (Außer /fv oder /fightvalley)");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {

        Player p = (Player) event.getEntity();

        if(ArenaPlayer.isInArena(p)) {
            if(ArenaPlayer.isInLobby(p)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
        if(event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            if(ArenaPlayer.isInArena(p)){
                event.setCancelled(true);
            }
        }
    }
}
