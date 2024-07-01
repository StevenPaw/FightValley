package com.stevenpaw.fightvalley.common.arena;

import com.stevenpaw.fightvalley.common.database.SQL_Sign;
import com.stevenpaw.fightvalley.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;

import java.util.HashMap;

public class ArenaSign {
    private Location location;
    private Arena arena;

    public ArenaSign(Location location, Arena arena) {
        this.location = location;
        this.arena = arena;
    }

    public static void UpdateSigns() {
        HashMap<Location, ArenaSign> signs = Main.signs;
        // loop through all signs
        signs.forEach((k, v) -> {
            if(k.getBlock().getState() instanceof Sign) {
                //get the sign at the location and change the text
                Sign targetSign = (Sign) k.getBlock().getState();
                targetSign.setLine(0, ChatColor.GRAY + "Join Arena");
                targetSign.setLine(1, ChatColor.BLACK + v.arena.getName());
                targetSign.setLine(2, getStateString(v.arena.getState()));
                targetSign.setLine(3, ChatColor.GRAY + (v.arena.getPlayers().size() + "/" + v.arena.getMaxPlayers()));
                targetSign.update(true);
            } else {
                //remove the sign from the list if it is not a sign
                SQL_Sign.deleteSign(k);
                signs.remove(k);
            }
        });
    }

    public static String getStateString(ArenaStates state) {
        switch(state) {
            case WAITING:
                return ChatColor.GREEN + "Waiting";
            case STARTING:
                return ChatColor.GREEN + "Starting";
            case RUNNING:
                return ChatColor.YELLOW + "Ingame";
            case ENDING:
                return ChatColor.YELLOW + "Ending";
            case DISABLED:
                return ChatColor.RED + "Disabled";
            default:
                return ChatColor.GRAY + "Unknown";
        }
    }

    public String getArenaName() {
        return arena.getName();
    }
}
