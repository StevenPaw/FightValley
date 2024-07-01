package com.stevenpaw.fightvalley.common.utils;

import com.stevenpaw.fightvalley.common.arena.ArenaSign;
import com.stevenpaw.fightvalley.common.arena.ArenaStates;
import com.stevenpaw.fightvalley.main.Main;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RunnableClass {

    public static void runMinute() {
        if (Bukkit.getOnlinePlayers().size() > 0) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                //Executes every minute
            }
        }
    }

    public static void runSecond() {
        if(Main.arenas.size() > 0) {
            //loop through all arenas
            Main.arenas.forEach((k, v) -> {
                v.Tick();
            });
        }
    }

    public static void runFiveSecond() {
        if(Main.arenas.size() > 0) {
            ArenaSign.UpdateSigns();
        }
    }
}
