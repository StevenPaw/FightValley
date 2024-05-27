package com.stevenpaw.fightvalley.main;

import com.stevenpaw.fightvalley.common.database.SQL_Player;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL {

    public static Connection con;
    public static String Host= Main.cfg.getString("MySQL.host");
    public static String Port = Main.cfg.getString("MySQL.port");
    public static String Database = Main.cfg.getString("MySQL.database");
    public static String User = Main.cfg.getString("MySQL.user");
    public static String Password = Main.cfg.getString("MySQL.password");

    public static void connect() {
        if (!isConnected()) {
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + Host + ":" + Port + "/" + Database+"?autoReconnect=true", User, Password);
                Bukkit.getConsoleSender().sendMessage(Main.prefix + "§aMit MySQL verbunden!");
            }
            catch (SQLException e) {
                Bukkit.getConsoleSender().sendMessage(Main.prefix + "§cKonnte nicht mit MySQL verbinden!");
                e.printStackTrace();
            }
        }
    }

    public static void setupMySQL(){
        MySQL.connect(); //MySQL verbinden
        MySQL.createTable(); //MySQL Tabellen erstellen
    }

    public static void disconnect() {
        if (isConnected()) {
            try {
                con.close();
                Bukkit.getConsoleSender().sendMessage(Main.prefix + "§cMySQL-Verbindung beendet!");
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isConnected() {
        try {
            if (con != null && !con.isClosed()) {
                return true;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void update(final String query) {
        if (isConnected()) {
            try {
                con.createStatement().executeUpdate(query);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ResultSet getResult(final String query) {
        try {
            return con.createStatement().executeQuery(query);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void createTable() {
        SQL_Player.createPlayerTable();
    }
}
