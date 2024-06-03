package com.stevenpaw.fightvalley.common.listener;

import com.stevenpaw.fightvalley.common.database.SQL_Player;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class ItemFrameListener implements Listener {

    /**
     * Verhindert die Rotation des Itemframes
     *
     * @param event (Event) = PlayerInteractEntityEvent
     */
    @EventHandler
    public void onPlayerEntityInteract(PlayerInteractEntityEvent event) {
        Player p = event.getPlayer();

        if(SQL_Player.getString(p.getUniqueId(), "CurrentArena") != null) {
            event.setCancelled(true);
        }
    }

    /**
     * Verhindert das Zerstören der Itemframes
     *
     * @param event (Event) = HangingBreakByEntityEvent
     */
    @EventHandler
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
        if(event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            if (SQL_Player.getString(p.getUniqueId(), "CurrentArena") != null) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Verhindert, dass items aus Itemframes entfernt werden können
     *
     * @param event (Event) = EntityDamageByEntityEvent
     */
    @EventHandler
    public void itemFrameItemRemoval(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();

            if (SQL_Player.getString(p.getUniqueId(), "CurrentArena") != null) {
                event.setCancelled(true);
            }
        }
    }

    /**
     * Verwandelt bestimmte ItemFrames in unsichtbare ItemFrames
     *
     * @param event (Event) = HangingPlaceEvent
     */
    @EventHandler
    public void placeItemFrame(HangingPlaceEvent event) {
        if(event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();

            if (SQL_Player.getString(p.getUniqueId(), "CurrentArena") != null) {
                event.setCancelled(true);
            }
        }
    }
}
