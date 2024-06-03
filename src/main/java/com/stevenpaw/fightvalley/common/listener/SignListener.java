package com.stevenpaw.fightvalley.common.listener;

import com.stevenpaw.fightvalley.common.arena.Arena;
import com.stevenpaw.fightvalley.common.arena.ArenaPlayer;
import com.stevenpaw.fightvalley.common.arena.ArenaStates;
import com.stevenpaw.fightvalley.common.database.SQL_Arena;
import com.stevenpaw.fightvalley.main.Main;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class SignListener implements Listener {

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        event.getPlayer().sendMessage("SignChangeEvent");
        if (!event.getPlayer().hasPermission("FightValley.*") || !event.getPlayer().hasPermission("FightValley.Sign")) {
            return;
        }
        if ((event.getLine(0) == null) && (event.getLine(1) == null)) {
            return;
        }
        if (Objects.equals(event.getLine(0), "") && Objects.equals(event.getLine(1), "")) {
            return;
        }
        switch (event.getLine(0)) {
            case "[FightValleyJoin]":
                event.getPlayer().sendMessage("Is FightValley Sign!");
                Sign sign = (Sign) event.getBlock().getState()  ;

                if (SQL_Arena.arenaExists(event.getLine(1))) {
                    Arena arena = SQL_Arena.getArenas().get(event.getLine(1));
                    sign.getPersistentDataContainer().set(new NamespacedKey(Main.getPlugin(), "arena"), PersistentDataType.STRING, arena.getName());
                    sign.setLine(0, "FightValley");
                    sign.setLine(1, "Join");
                    sign.setLine(2, arena.getName());
                    sign.setColor(DyeColor.GREEN);
                    sign.update();
                } else {
                    sign.setColor(DyeColor.RED);
                    event.getPlayer().sendMessage("Arena " + event.getLine(1) + " existiert nicht!");
                }
                break;
            default:
                break;
        }
    }

    @EventHandler
    void OnPlayerClickSign(PlayerInteractEvent event) {
        if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR) {
            return;
        }
        if (event.getClickedBlock().getState() instanceof Sign) {
            Sign sign = (Sign) event.getClickedBlock().getState();
            if (sign.getPersistentDataContainer().has(new NamespacedKey(Main.getPlugin(), "arena"), PersistentDataType.STRING)) {
                Arena arena = Main.arenas.get(sign.getPersistentDataContainer().get(new NamespacedKey(Main.getPlugin(), "arena"), PersistentDataType.STRING));
                if (arena != null) {
                    ArenaPlayer ap = ArenaPlayer.GetArenaPlayer(event.getPlayer().getUniqueId());
                    if (ap != null) {
                        if (ap.getCurrentArena() != null) {
                            ap.getCurrentArena().leaveArena(ap);
                        } else {
                            if(arena.getPlayers().size() >= arena.getMaxPlayers()) {
                                ap.getPlayer().sendMessage("§cThis Arena is full!");
                            }
                            if (arena.getState() == ArenaStates.WAITING || arena.getState() == ArenaStates.STARTING) {
                                arena.joinArena(ap);
                                ap.getPlayer().sendMessage("§6You joined Arena §c" + arena.getName() + "§6...");
                            } else if (arena.getState() == ArenaStates.RUNNING) {
                                ap.getPlayer().sendMessage("§cThis Arena is already running!");
                            } else {
                                ap.getPlayer().sendMessage("§cThis Arena not ready!");
                            }
                        }
                    }
                }
                event.setCancelled(true);
            }
        }
    }
}
