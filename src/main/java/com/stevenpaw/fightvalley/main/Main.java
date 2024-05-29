package com.stevenpaw.fightvalley.main;

import com.stevenpaw.fightvalley.common.arena.Arena;
import com.stevenpaw.fightvalley.common.arena.ArenaPlayer;
import com.stevenpaw.fightvalley.common.commands.Command;
import com.stevenpaw.fightvalley.common.database.SQL_Arena;
import com.stevenpaw.fightvalley.common.database.SQL_Player;
import com.stevenpaw.fightvalley.common.listener.PlayerListener;
import com.stevenpaw.fightvalley.common.utils.RunnableClass;
import com.stevenpaw.fightvalley.common.weapons.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import java.io.File;
import java.util.*;

public class Main extends JavaPlugin {

    private static Main plugin;

    private Economy eco = null;
    private Permission perms = null;
    private Chat chat = null;

    public static String prefix = "§e[FightValley] ";
    public static File file;
    public static FileConfiguration cfg;

    public static Main getPlugin() {
        return plugin;
    }

    public static HashMap<String, Arena> arenas;
    public static HashMap<UUID, ArenaPlayer> arenaPlayers;
    public static List<Class<? extends IWeapon>> weapons;
    public static int defaultArenaTime = 120;

    @Override
    public void onDisable() {
        arenas.forEach((k, v) -> {
            v.EndGame();
        });
        MySQL.disconnect();
        getLogger().info(String.format(this.prefix + "Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
    }

    @Override
    public void onEnable() {
        plugin = this;

        if (!setupEconomy() ) {
            getLogger().severe(String.format("Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
        setupChat();
        setupConfig();
        MySQL.setupMySQL();
        registerEvents();

        arenas = new HashMap<>();
        arenas = SQL_Arena.getArenas();
        arenaPlayers = new HashMap<>();
        arenaPlayers = SQL_Player.getAllPlayers();
        weapons = new ArrayList<>();
        weapons.add(Fighter.class);
        weapons.add(FireBender.class);
        weapons.add(FrostWalker.class);
        weapons.add(SpeedWalker.class);
        weapons.add(Archer.class);

        Bukkit.getScheduler().runTaskTimer(this, RunnableClass::runSecond, 20, 20);
        Bukkit.getScheduler().runTaskTimer(this, RunnableClass::runMinute, 20, 20*60);

        Objects.requireNonNull(getCommand("fightvalley")).setExecutor(new Command());
        Objects.requireNonNull(getCommand("fv")).setExecutor(new Command());

        getLogger().info(String.format(this.prefix + "Enabled Version %s", getDescription().getName(), getDescription().getVersion()));
    }

    /**
     * Erstelle und lade die Config
     */
    private void setupConfig(){
        saveDefaultConfig();
        file = new File(Main.getPlugin().getDataFolder().getAbsolutePath()+ "/config.yml");
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().severe(String.format("[%s] Error (1)!", getDescription().getName()));
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            getLogger().severe(String.format("[%s] Error (2)!", getDescription().getName()));
            return false;
        }
        eco = rsp.getProvider();
        return eco != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {

        ServicesManager sm = getServer().getServicesManager();
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    public Economy getEconomy() {
        return eco;
    }

    public Permission getPermissions() {
        return perms;
    }

    public Chat getChat() {
        return chat;
    }

    private void registerEvents(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(), this);
    }
}