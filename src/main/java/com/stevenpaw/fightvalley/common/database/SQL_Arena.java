package com.stevenpaw.fightvalley.common.database;

import com.stevenpaw.fightvalley.common.arena.Arena;
import com.stevenpaw.fightvalley.common.arena.ArenaPlayer;
import com.stevenpaw.fightvalley.main.Main;
import com.stevenpaw.fightvalley.main.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SQL_Arena {

    private static int defaultMaxPlayers = 12;
    private static int defaultMinPlayers = 2;

    /**
     * Erstellt die Tabelle FightValley_Arena
     */
    public static void createArenaTable() {
        if (MySQL.isConnected()) {
            try {
                MySQL.con.createStatement().executeUpdate(
                        "CREATE TABLE IF NOT EXISTS FightValley_Arena (" +
                                "Name VARCHAR(255) PRIMARY KEY, " +
                                "MinPlayers INT, " +
                                "MaxPlayers INT, " +
                                "LobbyX DOUBLE, " +
                                "LobbyY DOUBLE, " +
                                "LobbyZ DOUBLE, " +
                                "LobbyWorld VARCHAR(255), " +
                                "ExitX DOUBLE, " +
                                "ExitY DOUBLE, " +
                                "ExitZ DOUBLE, " +
                                "ExitWorld VARCHAR(255))"
                );
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Erstellt die Arena, wenn er noch nicht existiert
     * @param name (String) = Die zu erstellende Arena
     */
    public static void createArena(String name, int minPlayers, int maxPlayers) {
        if (!arenaExists(name)) {
            MySQL.update("INSERT INTO FightValley_Arena (Name, MinPlayers, MaxPlayers) VALUES ('" + name + "'," + minPlayers + "," + maxPlayers + ")");
        }
    }

    /**
     * Löscht die Arena
     * @param name (String) = Die zu löschende Arena
     */
    public static void deleteArena(String name) {
        if (arenaExists(name)) {
            MySQL.update("DELETE FROM FightValley_Arena WHERE Name= '" + name + "'");
            SQL_ArenaSpawn.deleteAllArenaSpawns(name);
        }
    }

    /**
     * Überprüft, ob die UUID bereits existiert
     * @param name (String) = Die zu überprüfende ID
     * @return Boolean = True/False
     */
    public static boolean arenaExists(String name) {
        try {
            final ResultSet res = MySQL.getResult("SELECT * FROM FightValley_Arena WHERE Name= '" + name + "'");
            assert res != null;
            return res.next();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Setzt den Double einer Arena in einer Spalte
     * @param name (String) = Die ID einer Arena
     * @param column (String) = Die zu bearbeitende Spalte
     * @param d (Double) = Der zu setzende Double
     */
    public static void setDouble(String name, final String column, final Double d) {
        if (!arenaExists(name)) {
            createArena(name, defaultMinPlayers, defaultMaxPlayers);
        }
        MySQL.update("UPDATE FightValley_Arena SET " + column + "= '" + d + "' WHERE Name= '" + name + "';");
    }

    /**
     * Gibt den Double eines Spielers in einer Spalte wieder
     * @param name (String) = Die ID einer Arena
     * @param column (String) = Die zu überprüfende Spalte
     * @return Double = Double des Spielers aus der Spalte
     */
    public static Double getDouble(String name, final String column) {
        try {
            MySQL.con.createStatement().executeUpdate("UPDATE FightValley_Arena SET " + column + " = 0 WHERE " + column + " IS NULL");
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        final ResultSet rs = MySQL.getResult("SELECT "+column+" FROM FightValley_Arena WHERE Name='" + name + "'");
        try {
            assert rs != null;
            if (rs.next()) {
                return rs.getDouble(column);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Setzt den String eines Spielers in einer Spalte
     * @param name (String) = Die ID einer Arena
     * @param column (String) = Die zu bearbeitende Spalte
     * @param string (string) = Der zu setzende String
     */
    public static void setString(String name, final String column, final String string) {
        if (!arenaExists(name)) {
            createArena(name, defaultMinPlayers, defaultMaxPlayers);
        }
        MySQL.update("UPDATE FightValley_Arena SET "+column+"= '"+string+"' WHERE Name= '" + name + "';");
    }

    /**
     * Gibt den String einer UUID in einer Spalte wieder
     * @param name (String) = Die ID einer Arena
     * @param column (String) = Die zu überprüfende Spalte
     * @return String = String der UUID aus der Spalte
     */
    public static String getString(String name, final String column) {
        if (arenaExists(name)) {
            final ResultSet rs = MySQL.getResult("SELECT "+column+" FROM FightValley_Arena WHERE Name='" + name + "'");
            try {
                assert rs != null;
                if (rs.next()) {
                    return rs.getString(column);
                }
                return null;
            }
            catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static List<String> getArenaNames() {
        List<String> names = new ArrayList<>();
        try {
            final ResultSet res = MySQL.getResult("SELECT Name FROM FightValley_Arena");
            assert res != null;
            int i = 0;
            while (res.next()) {
                names.add(res.getString("Name"));
                i++;
            }
            return names;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HashMap<String, Arena> getArenas() {
        HashMap<String, Arena> arenas = new HashMap<>();
        try {
            final ResultSet res = MySQL.getResult("SELECT * FROM FightValley_Arena");
            assert res != null;
            while (res.next()) {
                arenas.put(res.getString("Name"), new Arena(res.getString("Name"), res.getInt("MaxPlayers"), res.getInt("MinPlayers"), Main.defaultArenaTime));
            }
            return arenas;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Boolean hasLobby(String name) {
        return getString(name, "LobbyWorld") != null;
    }

    public static Boolean hasExit(String name) {
        return getString(name, "ExitWorld") != null;
    }

    public static Location getLobby(String name) {
        return new Location(Bukkit.getWorld(getString(name, "LobbyWorld")), getDouble(name, "LobbyX"), getDouble(name, "LobbyY"), getDouble(name, "LobbyZ")).add(0.5, 0, 0.5);
    }

    public static Location getExit(String name) {
        return new Location(Bukkit.getWorld(getString(name, "ExitWorld")), getDouble(name, "ExitX"), getDouble(name, "ExitY"), getDouble(name, "ExitZ")).add(0.5, 0, 0.5);
    }

    public static HashMap<UUID, ArenaPlayer> getArenaPlayers() {
        HashMap<UUID, ArenaPlayer> players = new HashMap<>();
        try {
            final ResultSet res = MySQL.getResult("SELECT * FROM FightValley_Player");
            assert res != null;
            while (res.next()) {
                Arena playerArena = Main.arenas.get(res.getString("CurrentArena"));
                GameMode playerGamemode = GameMode.SURVIVAL;
                try{
                    playerGamemode = GameMode.valueOf(res.getString("GamemodeOutside"));
                } catch (IllegalArgumentException e) {
                    playerGamemode = GameMode.SURVIVAL;
                }
                ArenaPlayer player = new ArenaPlayer(UUID.fromString(res.getString("UUID")), playerArena, playerGamemode);
                players.put(player.getUUID(), player);
            }
            return players;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
