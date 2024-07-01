package com.stevenpaw.fightvalley.common.database;

import com.stevenpaw.fightvalley.common.arena.Arena;
import com.stevenpaw.fightvalley.common.arena.ArenaSign;
import com.stevenpaw.fightvalley.main.Main;
import com.stevenpaw.fightvalley.main.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SQL_Sign {

    /**
     * Erstellt die Tabelle FightValley_Sign
     */
    public static void createSignTable() {
        if (MySQL.isConnected()) {
            try {
                MySQL.con.createStatement().executeUpdate(
                    "CREATE TABLE IF NOT EXISTS FightValley_Sign (" +
                            "ID INT AUTO_INCREMENT PRIMARY KEY, " +
                            "PosX DOUBLE, " +
                            "PosY DOUBLE, " +
                            "PosZ DOUBLE, " +
                            "PosWorld VARCHAR(255), " +
                            "Arena VARCHAR(255))"
                );
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Erstellt das Schild, wenn es noch nicht existiert
     * @param loc (Location) = Die zu nutzende Location
     * @param arena (String) = Die zu verknüpfende Arena
     */
    public static void createSign(Location loc, String arena) {
        if (!signExists(loc)) {
            Main.signs.put(loc, new ArenaSign(loc, Main.arenas.get(arena)));
            MySQL.update("INSERT INTO FightValley_Sign (PosX, PosY, PosZ, PosWorld, Arena) VALUES ('" + loc.getBlockX() + "','" + loc.getBlockY() + "','" + loc.getBlockZ() + "','" + loc.getBlock().getWorld().getName() + "','" + arena + "')");
        }
    }

    /**
     * Löscht die Arena
     * @param loc (Location) = Die zu löschende Arena
     */
    public static void deleteSign(Location loc) {
        if (signExists(loc)) {
            MySQL.update("DELETE FROM FightValley_Sign WHERE PosX= '" + loc.getBlockX() + "' AND PosY= '" + loc.getBlockY() + "' AND PosZ= '" + loc.getBlockZ() + "' AND PosWorld= '" + loc.getWorld().getName() + "'");
        }
    }

    /**
     * Überprüft, ob die UUID bereits existiert
     * @param loc (Location) = Die zu überprüfende ID
     * @return Boolean = True/False
     */
    public static boolean signExists(Location loc) {
        try {
            final ResultSet res = MySQL.getResult("SELECT * FROM FightValley_Sign WHERE PosX= '" + loc.getBlockX() + "' AND PosY= '" + loc.getBlockY() + "' AND PosZ= '" + loc.getBlockZ() + "' AND PosWorld= '" + loc.getWorld().getName() + "'");
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
     * @param loc (Location) = Die ID einer Arena
     * @param column (String) = Die zu bearbeitende Spalte
     * @param d (Double) = Der zu setzende Double
     */
    public static void setDouble(Location loc, final String column, final Double d) {
        if (signExists(loc)) {
            MySQL.update("UPDATE FightValley_Sign SET " + column + "= '" + d + "' WHERE PosX= '" + loc.getBlockX() + "' AND PosY= '" + loc.getBlockY() + "' AND PosZ= '" + loc.getBlockZ() + "' AND PosWorld= '" + loc.getWorld().getName() + "'");
        }
    }

    /**
     * Gibt den Double eines Spielers in einer Spalte wieder
     * @param loc (Location) = Die ID einer Arena
     * @param column (String) = Die zu überprüfende Spalte
     * @return Double = Double des Spielers aus der Spalte
     */
    public static Double getDouble(Location loc, final String column) {
        final ResultSet rs = MySQL.getResult("SELECT " + column + " FROM FightValley_Sign WHERE PosX= '" + loc.getBlockX() + "' AND PosY= '" + loc.getBlockY() + "' AND PosZ= '" + loc.getBlockZ() + "' AND PosWorld= '" + loc.getWorld().getName() + "'");
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
     * @param loc (Location) = Die Location des Schildes
     */
    public static void setString(Location loc, final String column, final String s) {
        if (signExists(loc)) {
            MySQL.update("UPDATE FightValley_Sign SET " + column + "= '" + s + "' WHERE PosX= '" + loc.getBlockX() + "' AND PosY= '" + loc.getBlockY() + "' AND PosZ= '" + loc.getBlockZ() + "' AND PosWorld= '" + loc.getWorld().getName() + "'");
        }
    }

    /**
     * Gibt den String einer UUID in einer Spalte wieder
     * @param loc (Location) = Die ID einer Arena
     * @param column (String) = Die zu überprüfende Spalte
     * @return String = String der UUID aus der Spalte
     */
    public static String getString(Location loc, final String column) {
        if (signExists(loc)) {
            final ResultSet rs = MySQL.getResult("SELECT "+column+" FROM FightValley_Sign WHERE PosX= '" + loc.getBlockX() + "' AND PosY= '" + loc.getBlockY() + "' AND PosZ= '" + loc.getBlockZ() + "' AND PosWorld= '" + loc.getWorld().getName() + "'");
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

    public static HashMap<Location, ArenaSign> getSigns() {
        HashMap<Location, ArenaSign> signs = new HashMap<>();
        try {
            final ResultSet res = MySQL.getResult("SELECT * FROM FightValley_Sign");
            assert res != null;
            while (res.next()) {
                Location loc = new Location(Bukkit.getWorld(res.getString("PosWorld")), res.getDouble("PosX"), res.getDouble("PosY"), res.getDouble("PosZ"));
                Arena arena = Main.arenas.get(res.getString("Arena"));
                signs.put(loc, new ArenaSign(loc, arena));
            }
            return signs;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
