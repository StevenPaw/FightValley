package com.stevenpaw.fightvalley.common.arena;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class ArenaSidebar {

    public static void setScoreBoard(Player player, Arena arena) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("FightValley", "dummy", arena.getName());
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(board);

        Score kills = obj.getScore(ChatColor.GRAY + "» Kills");
        kills.setScore(0);

        Score deaths = obj.getScore(ChatColor.GRAY + "» Deaths");
        deaths.setScore(0);

        if(arena.getState() == ArenaStates.RUNNING) {
            Score time = obj.getScore("Time: " + arena.timeLeft());
            time.setScore(0);
        }
    }

    public static void updateScore(Player player, Arena arena) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("FightValley", "dummy", arena.getName());
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score kills = obj.getScore(ChatColor.GRAY + "» Kills: 0");
        kills.setScore(0);

        Score deaths = obj.getScore(ChatColor.GRAY + "» Deaths: 0");
        deaths.setScore(0);

        if(arena.getState() == ArenaStates.RUNNING) {
            Score time = obj.getScore(ChatColor.GRAY + "» Time: " + arena.timeLeft());
            time.setScore(0);
        }

        player.setScoreboard(board);
    }

    public static void removeScoreboard(Player player, Arena arena) {
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }
}
