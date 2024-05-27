package com.stevenpaw.fightvalley.common.arena;

public class ArenaPlayer {
    private String name;
    private int kills;
    private int deaths;
    private int points;
    private int streak;
    private int highestStreak;
    private int arenaID;

    public ArenaPlayer(String name, int kills, int deaths, int points, int streak, int highestStreak, int arenaID) {
        this.name = name;
        this.kills = kills;
        this.deaths = deaths;
        this.points = points;
        this.streak = streak;
        this.highestStreak = highestStreak;
        this.arenaID = arenaID;
    }
}
