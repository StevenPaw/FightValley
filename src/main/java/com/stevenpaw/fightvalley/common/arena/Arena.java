package com.stevenpaw.fightvalley.common.arena;

import com.stevenpaw.fightvalley.common.database.SQL_Arena;
import com.stevenpaw.fightvalley.common.database.SQL_Player;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Arena {
    private String name;
    private int maxPlayers;
    private int minPlayers;
    private int startTime;
    private int curTime;
    private ArenaStates state;
    private List<Player> players;

    public Arena(String name, int maxPlayers, int minPlayers, int startTime) {
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
        this.startTime = startTime;
        this.curTime = startTime;
        this.state = ArenaStates.WAITING;

        players = new ArrayList<>();
    }

    public void Tick() {
        if (state == ArenaStates.RUNNING) {
            curTime--;
            players.forEach(p -> {
                ArenaSidebar.updateScore(p, this);
            });
            if (curTime <= 0) {
                EndGame();
            }
        }
    }

    public void Start() {
        state = ArenaStates.RUNNING;
        Bukkit.broadcastMessage("The game " + name + " has started!");
    }

    public void EndGame() {
        Bukkit.broadcastMessage("The game " + name + " is ending!");
        // Reset all players
        if(players != null) {
            for (Player p : players) {
                p.teleport(SQL_Arena.getExit(name));
                players.remove(p);
            }
        }
        //Change Gamestate back to waiting
        state = ArenaStates.WAITING;
        curTime = startTime;
    }

    public ArenaStates getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    public void addPlayer(Player p) {
        players.add(p);
        ArenaSidebar.setScoreBoard(p, this);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public Location getLobby() {
        return SQL_Arena.getLobby(name);
    }

    public Location getExit() {
        return SQL_Arena.getExit(name);
    }

    public Integer getCurTime() {
        return curTime;
    }

    public String timeLeft() {
        Integer secondsLeft = curTime;
        Integer minutes = secondsLeft / 60;
        Integer seconds = secondsLeft % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
