package com.stevenpaw.fightvalley.common.arena;

import com.stevenpaw.fightvalley.common.database.SQL_Arena;
import com.stevenpaw.fightvalley.common.database.SQL_ArenaSpawn;
import com.stevenpaw.fightvalley.common.utils.FireworkHelper;
import com.stevenpaw.fightvalley.common.weapons.IWeapon;
import com.stevenpaw.fightvalley.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Arena {
    private String name;
    private int maxPlayers;
    private int minPlayers;
    private int startTime = Main.defaultArenaTime;
    private int startingTime = 30;
    private int endingTime = 10;
    private int curTime;
    private ArenaStates state;
    private List<ArenaPlayer> players;
    private List<ArenaItemSpawn> itemSpawns;

    /**
     * Arena
     * @param name (String) = The name of the arena
     * @param maxPlayers (int) = The maximum amount of players
     * @param minPlayers (int) = The minimum amount of players
     * @param startTime (int) = The time the game starts
     */
    public Arena(String name, int maxPlayers, int minPlayers, int startTime) {
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
        this.startTime = startTime;
        this.curTime = startTime;

        if(SQL_Arena.hasLobby(name) && SQL_Arena.hasExit(name) && SQL_ArenaSpawn.getSpawnLocations(name).size() > 0) {
            this.state = ArenaStates.WAITING;
        } else {
            this.state = ArenaStates.DISABLED;
        }

        players = new ArrayList<>();
    }

    /**
     * Updates the game state every second
     */
    public void Tick() {
        switch (state) {
            default:
                break;
            case ArenaStates.WAITING:
                players.forEach(p -> {
                    ArenaSidebar.updateScoreboard(p.getPlayer(), this);
                });
                break;
            case ArenaStates.RUNNING:
                curTime--;
                players.forEach(p -> {
                    ArenaSidebar.updateScoreboard(p.getPlayer(), this);
                    p.getPlayer().setFoodLevel(20);
                });
                if (curTime <= 0) {
                    FinishGame();
                }
                break;
            case ArenaStates.STARTING:
                curTime--;
                players.forEach(p -> {
                    ArenaSidebar.updateScoreboard(p.getPlayer(), this);
                });
                if (curTime <= 0) {
                    Start2();
                }
                break;
            case ArenaStates.ENDING:
                curTime--;
                players.forEach(p -> {
                    ArenaSidebar.updateScoreboard(p.getPlayer(), this);
                });
                if (curTime <= 0) {
                    EndGame();
                }
                break;
        }
    }

    /**
     * Starts the game
     */
    public void Start() {
        curTime = startingTime;
        state = ArenaStates.STARTING;
        curTime = startingTime;
        Bukkit.broadcastMessage("The game " + name + " has started!");
    }

    /**
     * Starts the game after the Starting State
     */
    public void Start2(){
        curTime = startTime;
        state = ArenaStates.RUNNING;
        List<Location> arenaSpawns = SQL_ArenaSpawn.getSpawnLocations(name);
        //Randomize the order of arena spawns
        for (int i = 0; i < arenaSpawns.size(); i++) {
            int randomIndexToSwap = (int) (Math.random() * arenaSpawns.size());
            Location temp = arenaSpawns.get(randomIndexToSwap);
            arenaSpawns.set(randomIndexToSwap, arenaSpawns.get(i));
            arenaSpawns.set(i, temp);
        }
        for (ArenaPlayer p : players) {
            FireworkHelper.spawnFireworks(p.getPlayer().getLocation().add(0, 10, 0), 1, 2, true);
            p.getPlayer().setFoodLevel(20);

            //Give random spawns to different random players but only the same when there are more players than spawns
            if (players.size() > arenaSpawns.size()) {
                p.getPlayer().teleport(arenaSpawns.get((int) (Math.random() * arenaSpawns.size())));
            } else {
                p.getPlayer().teleport(arenaSpawns.get(players.indexOf(p)));
            }
            setRandomWeapon(p);
        }
    }

    /**
     * Ends the game immediately without points
     */
    public void EndGame() {
        // Ends the game immediately
        state = ArenaStates.WAITING;
        if(players != null) {
            List<ArenaPlayer> tempPlayers = new ArrayList<>(players);
            tempPlayers.forEach(p -> {
                p.getPlayer().setHealth(20);
                p.getPlayer().setFoodLevel(20);
                p.getPlayer().setFireTicks(0);
                leaveArena(p);
            });
        }
        state = ArenaStates.WAITING;
    }

    public ArenaPlayer[] getScoreboard() {
        // Sort the players by kills
        players.sort((p1, p2) -> p2.getKills() - p1.getKills());
        return players.toArray(new ArenaPlayer[0]);
    }

    /**
     * Ends the game and displays the winner
     */
    public void FinishGame() {
        // Reset all players
        if(players != null) {
            List<ArenaPlayer> tempPlayers = new ArrayList<>(players);
            ArenaPlayer[] scoreboard = getScoreboard();

            int money = 20;
            for (int i = 0; i < 2; i++) {
                Main.getEconomy().depositPlayer(scoreboard[i].getPlayer(), money / (i + 1));
            }

            tempPlayers.forEach(p -> {
                p.getPlayer().sendTitle("Game Over", scoreboard[0].getPlayer().getName() + " won!", 10, 70, 20);
                p.getPlayer().sendMessage("§aGame Over! These are the final scores:");
                for (int i = 0; i < scoreboard.length; i++) {
                    p.getPlayer().sendMessage((i + 1) + ". " + scoreboard[i].getPlayer().getName() + " - " + scoreboard[i].getKills() + " kills");
                }
                p.getPlayer().setHealth(20);
                p.getPlayer().setFoodLevel(20);
                p.getPlayer().setFireTicks(0);
                p.clearInventory();
            });
            tempPlayers.forEach(p -> {
                p.resetScores();
            });
            curTime = endingTime;
        }
        state = ArenaStates.ENDING;
    }

    /**
     * Gets the current state of the game
     */
    public ArenaStates getState() {
        return state;
    }

    /**
     * Gets the name of the arena
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a player to the game
     * @param p (Player) = The player to add
     */
    public void addPlayer(ArenaPlayer p) {
        players.add(p);
        p.setCurrentArena(Main.arenas.get(name));
        p.setGameModeOutsideOfArena(p.getGameMode());
        ArenaSidebar.setScoreBoard(p.getPlayer(), this);
        p.setGameMode(GameMode.ADVENTURE);
        p.setCurrentArena(this);
        p.saveInventory();
        p.clearInventory();
        Main.arenaPlayers.put(p.getPlayer().getUniqueId(), p);
    }

    /**
     * Gets the players in the game
     * @return List<Player> = The players in the game
     */
    public List<ArenaPlayer> getPlayers() {
        return players;
    }

    /**
     * Gets the minimum amount of players
     * @return int = The minimum amount of players
     */
    public int getMinPlayers() {
        return minPlayers;
    }

    /**
     * Gets the maximum amount of players
     * @return int = The maximum amount of players
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Gets the lobby location
     * @return Location = The lobby location
     */
    public Location getLobby() {
        return SQL_Arena.getLobby(name);
    }

    /**
     * Gets the exit location
     * @return Location = The exit location
     */
    public Location getExit() {
        return SQL_Arena.getExit(name);
    }

    /**
     * Gets the current time of the countdown
     * @return Integer = The current time
     */
    public Integer getCurTime() {
        return curTime;
    }

    /**
     * Gets the time left of the current Countdown
     * @return String = The time left in the current Countdown
     */
    public String timeLeft() {
        Integer secondsLeft = curTime;
        Integer minutes = secondsLeft / 60;
        Integer seconds = secondsLeft % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Joins the arena
     * @param p (Player) = The player to join
     */
    public void joinArena(ArenaPlayer p) {
        if (state == ArenaStates.WAITING || state == ArenaStates.STARTING) {
            if (players.size() < maxPlayers) {
                addPlayer(p);
                p.teleportToLobby();
                if (players.size() >= minPlayers) {
                    if(state == ArenaStates.STARTING){
                        curTime = startingTime;
                    } else {
                        Start();
                    }
                }
            } else {
                p.getPlayer().sendMessage("The game is full!");
            }
        } else {
            p.getPlayer().sendMessage("The game is already running!");
        }
    }

    /**
     * Leaves the arena
     * @param p (Player) = The player to leave
     */
    public void leaveArena(ArenaPlayer p) {
        if (players.contains(p)) {
            p.getPlayer().sendMessage("Leaving the arena...");
            p.setStreak(0);
            p.teleportToExit();
            p.setGameMode(p.getGameModeOutsideOfArena());
            ArenaSidebar.removeScoreboard(p.getPlayer(), this);
            p.restoreInventory();
            players.remove(p);

            if(state == ArenaStates.RUNNING){
                if (players.size() == 0) {
                    EndGame(); //End the game immediately if no players are left
                } else if (players.size() < minPlayers) {
                    FinishGame(); //End the game if there are not enough players to play and announce winner
                }
            } else if (state == ArenaStates.STARTING){
                if (players.size() < minPlayers) {
                    state = ArenaStates.WAITING;
                }
            }
            p.emptyCurrentArena(); //Do this last to prevent errors
            p.emptyCurrentWeapon();
        }
    }

    public void setState(ArenaStates state) {
        this.state = state;
    }

    public void setRandomWeapon(ArenaPlayer p) {
        Class<?> randomWeaponClass = Main.weapons.get((int) (Math.random() * Main.weapons.size()));
        IWeapon weapon = null;
        try {
            weapon = (IWeapon) randomWeaponClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        if (weapon != null) {
            ItemStack[] items = weapon.getItems();
            p.clearInventory();
            for (ItemStack item : items) {
                p.getPlayer().getInventory().addItem(item);
            }
            p.setCurrentWeapon(weapon);
            p.getPlayer().sendTitle(weapon.getName(), weapon.getWeaponShortDescription(), 10, 70, 20);
            p.getPlayer().sendMessage(weapon.getWeaponDescription());
        }
    }
}
