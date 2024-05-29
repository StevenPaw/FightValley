package com.stevenpaw.fightvalley.common.database;

import com.stevenpaw.fightvalley.common.arena.Arena;
import com.stevenpaw.fightvalley.common.arena.ArenaPlayer;
import com.stevenpaw.fightvalley.main.Main;
import com.stevenpaw.fightvalley.main.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class SQL_Player {

    /**
     * Erstellt die Tabelle McValley_Player
     */
    public static void createPlayerTable() {
        if (MySQL.isConnected()) {
            try {
                MySQL.con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS FightValley_Player (" +
                        "UUID VARCHAR(100) PRIMARY KEY, " +
                        "HighestStreak INT, " +
                        "CurrentArena VARCHAR(255), " +
                        "GameModeOutside VARCHAR(255), " +
                        "InventoryOutside TEXT" +
                        ")");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Erstellt den Spieler, wenn er noch nicht existiert
     * @param uuid (UUID) = Der zu erstellende Spieler
     */
    public static void createPlayer(final UUID uuid) {
        if (!playerExists(uuid)) {
            MySQL.update("INSERT INTO FightValley_Player (UUID, HighestStreak, CurrentArena, GameModeOutside) VALUES ('" + uuid + "','0','','" + Bukkit.getPlayer(uuid).getGameMode().toString() + "')");
        }
    }

    /**
     * Überprüft, ob die UUID bereits existiert
     * @param uuid (UUID) = Die zu überprüfende UUID
     * @return Boolean = True/False
     */
    public static boolean playerExists(final UUID uuid) {
        try {
            final ResultSet res = MySQL.getResult("SELECT * FROM FightValley_Player WHERE UUID= '" + uuid + "'");
            assert res != null;
            return res.next();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Setzt den Double eines Spielers in einer Spalte
     * @param uuid (UUID) = Der Spieler
     * @param column (String) = Die zu bearbeitende Spalte
     * @param d (Double) = Der zu setzende Double
     */
    public static void setDouble(final UUID uuid, final String column, final Double d) {
        if (!playerExists(uuid)) {
            createPlayer(uuid);
        }
        MySQL.update("UPDATE FightValley_Player SET " + column + "= '" + d + "' WHERE UUID= '"+uuid+"';");
    }

    /**
     * Gibt den Double eines Spielers in einer Spalte wieder
     * @param uuid (UUID) = Der Spieler
     * @param column (String) = Die zu überprüfende Spalte
     * @return Double = Double des Spielers aus der Spalte
     */
    public static Double getDouble(final UUID uuid, final String column) {
        try {
            MySQL.con.createStatement().executeUpdate("UPDATE FightValley_Player SET "+column+" = 0 WHERE "+column+" IS NULL");
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        final ResultSet rs = MySQL.getResult("SELECT "+column+" FROM FightValley_Player WHERE UUID='"+uuid+"'");
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
     * @param uuid (UUID) = Der Spieler
     * @param column (String) = Die zu bearbeitende Spalte
     * @param string (string) = Der zu setzende String
     */
    public static void setString(final UUID uuid, final String column, final String string) {
        if (!playerExists(uuid)) {
            createPlayer(uuid);
        }
        MySQL.update("UPDATE FightValley_Player SET "+column+"= '"+string+"' WHERE UUID= '"+uuid+"';");
    }

    /**
     * Gibt den String einer UUID in einer Spalte wieder
     * @param uuid (UUID) = Die UUID
     * @param column (String) = Die zu überprüfende Spalte
     * @return String = String der UUID aus der Spalte
     */
    public static String getString(final UUID uuid, final String column) {
        if (playerExists(uuid)) {
            final ResultSet rs = MySQL.getResult("SELECT "+column+" FROM FightValley_Player WHERE UUID='"+uuid+"'");
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

    public static HashMap<UUID, ArenaPlayer> getAllPlayers() {
        HashMap<UUID, ArenaPlayer> players = new HashMap<>();
        try {
            ResultSet rs = MySQL.getResult("SELECT * FROM FightValley_Player");
            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("UUID"));
                int highestStreak = rs.getInt("HighestStreak");
                Arena currentArena = Main.arenas.get(rs.getString("CurrentArena"));
                GameMode gameModeOutside = GameMode.valueOf(rs.getString("GameModeOutside"));

                ArenaPlayer ap = new ArenaPlayer(uuid, currentArena, gameModeOutside, highestStreak);
                players.put(uuid, ap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }
}
