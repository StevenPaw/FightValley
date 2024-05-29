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
        updateScoreboard(player, arena);
    }

    public static void updateScoreboard(Player player, Arena arena) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("FightValley", "dummy", "FightValley - " + arena.getName());
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score players = obj.getScore(ChatColor.GRAY + "» Players: " + arena.getPlayers().size() + "/" + arena.getMaxPlayers());
        players.setScore(1);

        Score kills = obj.getScore(ChatColor.GRAY + "» Kills:  " + ArenaPlayer.GetArenaPlayer(player).getKills());
        kills.setScore(1);

        Score streak = obj.getScore(ChatColor.GRAY + "» Streak:  " + ArenaPlayer.GetArenaPlayer(player).getStreak());
        streak.setScore(1);

        Score deaths = obj.getScore(ChatColor.GRAY + "» Deaths: " + ArenaPlayer.GetArenaPlayer(player).getDeaths());
        deaths.setScore(1);

        if(arena.getState() == ArenaStates.RUNNING){
            Score time = obj.getScore(ChatColor.GRAY + "» Time: " + arena.timeLeft());
            time.setScore(0);
        } else if (arena.getState() == ArenaStates.STARTING){
            Score time = obj.getScore(ChatColor.GRAY + "» Start in: " + arena.timeLeft());
            time.setScore(0);
        } else if (arena.getState() == ArenaStates.WAITING){
            Score time = obj.getScore(ChatColor.GRAY + " " + arena.timeLeft());
            time.setScore(0);
        }

        player.setScoreboard(board);
    }

    public static void removeScoreboard(Player player, Arena arena) {
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }
}
