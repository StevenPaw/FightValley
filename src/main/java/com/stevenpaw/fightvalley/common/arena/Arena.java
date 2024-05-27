package com.stevenpaw.fightvalley.common.arena;

public class Arena {
    private int id;
    private String name;
    private int maxPlayers;
    private int minPlayers;
    private int startTime;
    private int curTime;
    private int state; // 0 = waiting, 1 = running, 2 = ending

    public Arena(int id, String name, int maxPlayers, int minPlayers, int startTime) {
        this.id = id;
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
        this.startTime = startTime;
        this.curTime = startTime;
        this.state = 0;
    }

    public void Tick() {
        if (state == 1) {
            curTime--;
            if (curTime <= 0) {
                state = 2;
            }
        }
    }
}
