package com.stevenpaw.fightvalley.common.database;

import com.stevenpaw.fightvalley.main.MySQL;
import org.bukkit.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQL_ArenaSpawn {

    /**
     * Erstellt die Tabelle FightValley_ArenaSpawn
     */
    public static void createArenaSpawnTable() {
        if (MySQL.isConnected()) {
            try {
                MySQL.con.createStatement().executeUpdate(
                        "CREATE TABLE IF NOT EXISTS FightValley_ArenaSpawn (" +
                                "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                                "Arena VARCHAR(255), " +
                                "SpawnX DOUBLE, " +
                                "SpawnY DOUBLE, " +
                                "SpawnZ DOUBLE" +
                                ")"
                );
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Erstellt den Spawn, wenn er noch nicht existiert
     * @param arena (String) = Die anhängende Arena
     * @param loc (Location) = Der Ort des Spawns
     */
    public static void createArenaSpawn(String arena, Location loc) {
        if(SQL_Arena.arenaExists(arena)) {
            MySQL.update("INSERT INTO FightValley_ArenaSpawn (Arena, SpawnX, SpawnY, SpawnZ) VALUES ('" + arena + "'," + loc.getBlock().getX() + "," + loc.getBlock().getY() + "," + loc.getBlock().getZ() + ")");
        }
    }

    /**
     * Löscht einen Spawn
     * @param arena (String) = Die anhängende Arena
     * @param loc (Location) = Der Ort des Spawns
     */
    public static void deleteArenaSpawn(String arena, Location loc) {
        if(SQL_Arena.arenaExists(arena)) {
            MySQL.update("DELETE FROM FightValley_ArenaSpawn WHERE SpawnX= '" + loc.getBlock().getX() + "' AND SpawnY= '" + loc.getBlock().getY() + "' AND SpawnZ= '" + loc.getBlock().getZ() + "'");
        }
    }

    /**
     * Löscht einen Spawn per ID
     * @param id (Integer) = ID des zu löschenden Spawns
     */
    public static void deleteArenaSpawn(Integer id) {
        MySQL.update("DELETE FROM FightValley_ArenaSpawn WHERE ID= '" + id + "'");
    }

    /**
     * Löscht alle Spawns einer Arena
     * @param arena (String) = Die Arena dessen Spawns gelöscht werden sollen
     */
    public static void deleteAllArenaSpawns(String arena) {
        if(SQL_Arena.arenaExists(arena)) {
            MySQL.update("DELETE FROM FightValley_ArenaSpawn WHERE Arena= '" + arena + "'");
        }
    }

    public static boolean arenaSpawnExists(String arena, Location loc) {
        try {
            final ResultSet res = MySQL.getResult("SELECT * FROM FightValley_ArenaSpawn WHERE Arena= '" + arena + "' AND SpawnX= '" + loc.getBlock().getX() + "' AND SpawnY= '" + loc.getBlock().getY() + "' AND SpawnZ= '" + loc.getBlock().getZ() + "'");
            assert res != null;
            return res.next();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> getSpawnIDs(String arena) {
        List<String> ids = new ArrayList<>();
        try {
            final ResultSet res = MySQL.getResult("SELECT ID FROM FightValley_ArenaSpawn WHERE Arena= '" + arena + "'");
            assert res != null;
            int i = 0;
            while (res.next()) {
                ids.add(res.getString("Name"));
                i++;
            }
            return ids;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Location> getSpawnLocations(String arena) {
        List<Location> locs = new ArrayList<>();
        try {
            final ResultSet res = MySQL.getResult("SELECT * FROM FightValley_ArenaSpawn WHERE Arena= '" + arena + "'");
            assert res != null;
            while (res.next()) {
                locs.add(new Location(SQL_Arena.getLobby(arena).getWorld(), res.getDouble("SpawnX"), res.getDouble("SpawnY"), res.getDouble("SpawnZ")).add(0.5, 0, 0.5));
            }
            return locs;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
