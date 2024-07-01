package com.stevenpaw.fightvalley.common.database;

import com.stevenpaw.fightvalley.common.arena.ArenaItemSpawn;
import com.stevenpaw.fightvalley.common.items.HealthApple;
import com.stevenpaw.fightvalley.common.items.IArenaItem;
import com.stevenpaw.fightvalley.main.MySQL;
import org.bukkit.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQL_ArenaItemSpawn {

    /**
     * Erstellt die Tabelle FightValley_ArenaSpawn
     */
    public static void createArenaSpawnTable() {
        if (MySQL.isConnected()) {
            try {
                MySQL.con.createStatement().executeUpdate(
                        "CREATE TABLE IF NOT EXISTS FightValley_ArenaItemSpawn (" +
                                "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                                "Arena VARCHAR(255), " +
                                "Item VARCHAR(255), " +
                                "World VARCHAR(255), " +
                                "PosX DOUBLE, " +
                                "PosY DOUBLE, " +
                                "PosZ DOUBLE" +
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
    public static void createArenaItemSpawn(String arena, IArenaItem arenaItem, Location loc) {
        if(SQL_Arena.arenaExists(arena)) {
            MySQL.update("INSERT INTO FightValley_ArenaItemSpawn (Arena, Item, PosX, PosY, PosZ) VALUES ('" + arena + "'," + "'" + arenaItem.getName() + "'," + loc.getBlock().getX() + "," + loc.getBlock().getY() + "," + loc.getBlock().getZ() + ")");
        }
    }

    /**
     * Löscht einen Spawn
     * @param arena (String) = Die anhängende Arena
     * @param loc (Location) = Der Ort des Spawns
     */
    public static void deleteArenaItemSpawn(String arena, Location loc) {
        if(SQL_Arena.arenaExists(arena)) {
            MySQL.update("DELETE FROM FightValley_ArenaItemSpawn WHERE PosX= '" + loc.getBlock().getX() + "' AND PosY= '" + loc.getBlock().getY() + "' AND PosZ= '" + loc.getBlock().getZ() + "'");
        }
    }

    /**
     * Löscht einen Spawn per ID
     * @param id (Integer) = ID des zu löschenden Spawns
     */
    public static void deleteArenaItemSpawn(Integer id) {
        MySQL.update("DELETE FROM FightValley_ArenaItemSpawn WHERE ID= '" + id + "'");
    }

    /**
     * Löscht alle Spawns einer Arena
     * @param arena (String) = Die Arena dessen Spawns gelöscht werden sollen
     */
    public static void deleteAllArenaItemSpawns(String arena) {
        if(SQL_Arena.arenaExists(arena)) {
            MySQL.update("DELETE FROM FightValley_ArenaItemSpawn WHERE Arena= '" + arena + "'");
        }
    }

    public static boolean arenaItemSpawnExists(String arena, Location loc) {
        try {
            final ResultSet res = MySQL.getResult("SELECT * FROM FightValley_ArenaItemSpawn WHERE Arena= '" + arena + "' AND PosX= '" + loc.getBlock().getX() + "' AND PosY= '" + loc.getBlock().getY() + "' AND PosZ= '" + loc.getBlock().getZ() + "'");
            assert res != null;
            return res.next();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> getItemSpawnIDs(String arena) {
        List<String> ids = new ArrayList<>();
        try {
            final ResultSet res = MySQL.getResult("SELECT ID FROM FightValley_ArenaItemSpawn WHERE Arena= '" + arena + "'");
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

    public static List<Location> getItemSpawnLocations(String arena) {
        List<Location> locs = new ArrayList<>();
        try {
            final ResultSet res = MySQL.getResult("SELECT * FROM FightValley_ArenaItemSpawn WHERE Arena= '" + arena + "'");
            assert res != null;
            while (res.next()) {
                locs.add(new Location(SQL_Arena.getLobby(arena).getWorld(), res.getDouble("PosX"), res.getDouble("PosY"), res.getDouble("PosZ")).add(0.5, 0, 0.5));
            }
            return locs;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static List<ArenaItemSpawn> getItemSpawns(String arena) {
        List<ArenaItemSpawn> spawns = new ArrayList<>();
        try {
            final ResultSet res = MySQL.getResult("SELECT * FROM FightValley_ArenaItemSpawn WHERE Arena= '" + arena + "'");
            assert res != null;
            IArenaItem item = null;
            switch (res.getString("Item")) {
                case "HealthApple":
                    item = new HealthApple();
                    break;
            }
            while (res.next()) {
                spawns.add(new ArenaItemSpawn(
                        SQL_Arena.getLobby(arena).getWorld(),
                        res.getInt("PosX"),
                        res.getInt("PosY"),
                        res.getInt("PosZ"),
                        item
                ));
            }
            return spawns;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
