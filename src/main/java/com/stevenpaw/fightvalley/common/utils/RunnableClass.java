package com.stevenpaw.fightvalley.common.utils;

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
}
