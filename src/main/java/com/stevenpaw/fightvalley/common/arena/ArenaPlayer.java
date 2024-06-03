package com.stevenpaw.fightvalley.common.arena;

import com.stevenpaw.fightvalley.common.database.SQL_Player;
import com.stevenpaw.fightvalley.common.utils.Util_InventoryEncoder;
import com.stevenpaw.fightvalley.common.weapons.IWeapon;
import com.stevenpaw.fightvalley.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.UUID;

public class ArenaPlayer {
    private UUID uuid;
    private int kills;
    private int deaths;
    private int streak;
    private int highestStreak;
    private Arena currentArena;
    private GameMode gameModeOutsideOfArena;
    private IWeapon currentWeapon;

    public ArenaPlayer(UUID uuid, Arena currentArena, GameMode gameModeOutsideOfArena) {
        this.uuid = uuid;
        this.kills = 0;
        this.deaths = 0;
        this.streak = 0;
        this.highestStreak = 0;
        this.currentArena = currentArena;
        this.gameModeOutsideOfArena = gameModeOutsideOfArena;
    }

    public ArenaPlayer(UUID uuid, Arena currentArena, GameMode gameModeOutsideOfArena, int highestStreak) {
        this.uuid = uuid;
        this.kills = 0;
        this.deaths = 0;
        this.streak = 0;
        this.highestStreak = highestStreak;
        this.currentArena = currentArena;
        this.gameModeOutsideOfArena = gameModeOutsideOfArena;
    }

    public ArenaPlayer(UUID uuid, int kills, int deaths, int points, int streak, int highestStreak, Arena currentArena, GameMode gameModeOutsideOfArena) {
        this.uuid = uuid;
        this.kills = kills;
        this.deaths = deaths;
        this.streak = streak;
        this.highestStreak = highestStreak;
        this.currentArena = currentArena;
        this.gameModeOutsideOfArena = gameModeOutsideOfArena;
    }

    public UUID getUUID() {
        return uuid;
    }

    public GameMode getGameModeOutsideOfArena() {
        return gameModeOutsideOfArena;
    }

    public Arena getCurrentArena() {
        return currentArena;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public static ArenaPlayer GetArenaPlayer(UUID uuid) {
        ArenaPlayer ap;
        Player player = Bukkit.getPlayer(uuid);
        if(Main.arenaPlayers.containsKey(uuid)) {
            ap = Main.arenaPlayers.get(uuid);
        } else {
            ap = new ArenaPlayer(uuid, null, player.getGameMode());
            SQL_Player.setString(player.getUniqueId(), "GameModeOutside", player.getGameMode().toString());
        }
        return ap;
    }

    public static ArenaPlayer GetArenaPlayer(Player player) {
        ArenaPlayer ap;
        if(Main.arenaPlayers.containsKey(player.getUniqueId())) {
            ap = Main.arenaPlayers.get(player.getUniqueId());
        } else {
            ap = new ArenaPlayer(player.getUniqueId(), null, player.getGameMode());
            SQL_Player.createPlayer(player.getUniqueId());
            SQL_Player.setString(player.getUniqueId(), "GameModeOutside", player.getGameMode().toString());
        }
        return ap;
    }

    public void setGameModeOutsideOfArena(GameMode gameModeOutsideOfArena) {
        this.gameModeOutsideOfArena = gameModeOutsideOfArena;
        SQL_Player.setString(uuid, "GameModeOutside", gameModeOutsideOfArena.toString());
    }

    public void setCurrentArena(Arena currentArena) {
        this.currentArena = currentArena;
        SQL_Player.setString(uuid, "CurrentArena", currentArena.getName());
    }

    public void emptyCurrentArena() {
        this.currentArena = null;
        SQL_Player.setString(uuid, "CurrentArena", "");
    }

    public void setGameMode(GameMode gameMode) {
        getPlayer().setGameMode(gameMode);
    }

    public GameMode getGameMode() {
        return getPlayer().getGameMode();
    }

    public void teleportToLobby() {
        getPlayer().teleport(currentArena.getLobby());
    }

    public void teleportToExit() {
        getPlayer().teleport(currentArena.getExit());
    }

    public void saveInventory() {
        if (!SQL_Player.playerExists(uuid)) {
            return;
        }
        Player p = Bukkit.getPlayer(uuid);
        String inv = "";
        inv = Util_InventoryEncoder.playerInventoryToBase64(p.getInventory());
        SQL_Player.setString(uuid, "InventoryOutside", inv);
    }

    public void restoreInventory() {
        if (!SQL_Player.playerExists(uuid)) {
            return;
        }
        Player p = Bukkit.getPlayer(uuid);
        String inv = SQL_Player.getString(uuid, "InventoryOutside");
        p.getInventory().clear();
        try {
            p.getInventory().setContents(Util_InventoryEncoder.itemStackArrayFromBase64(inv));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearInventory() {
        Player p = Bukkit.getPlayer(uuid);
        p.getInventory().clear();
    }

    public void setStreak(int newStreak) {
        streak = newStreak;

        if(streak > highestStreak) {
            highestStreak = streak;
            SQL_Player.setString(uuid, "HighestStreak", String.valueOf(highestStreak));
        }
    }

    public void addKill() {
        kills++;
        streak++;
        if(streak > highestStreak) {
            highestStreak = streak;
            SQL_Player.setString(uuid, "HighestStreak", String.valueOf(highestStreak));
        }
    }

    public void addDeath() {
        deaths++;
        streak = 0;
    }

    public static boolean isInArena(Player player) {
        ArenaPlayer ap = ArenaPlayer.GetArenaPlayer(player);
        return ap.getCurrentArena() != null;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getKills() {
        return kills;
    }

    public int getStreak() {
        return streak;
    }

    public int getHighestStreak() {
        return highestStreak;
    }

    public void setCurrentWeapon(IWeapon weapon) {
        this.currentWeapon = weapon;
    }

    public IWeapon getCurrentWeapon() {
        return currentWeapon;
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    public void resetScores() {
        kills = 0;
        deaths = 0;
        streak = 0;
    }
}
