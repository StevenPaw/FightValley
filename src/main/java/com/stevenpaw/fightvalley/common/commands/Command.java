package com.stevenpaw.fightvalley.common.commands;

import com.stevenpaw.fightvalley.common.arena.Arena;
import com.stevenpaw.fightvalley.common.arena.ArenaPlayer;
import com.stevenpaw.fightvalley.common.arena.ArenaStates;
import com.stevenpaw.fightvalley.common.database.SQL_Arena;
import com.stevenpaw.fightvalley.common.database.SQL_ArenaSpawn;
import com.stevenpaw.fightvalley.main.Main;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;

public class Command implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (sender.hasPermission("FightValley.*") || sender.hasPermission("FightValley.JoinArena")) {
                sender.sendMessage("§6FightValley is active!");
            } else {
                sender.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
            }
        } else {
            switch(args[0]){
                case "create":
                    if (sender.hasPermission("FightValley.*") || sender.hasPermission("FightValley.CreateArena")) {
                        createArena(sender, args);
                    } else {
                        sender.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
                    }
                    break;
                case "delete":
                    if (sender.hasPermission("FightValley.*") || sender.hasPermission("FightValley.DeleteArena")) {
                        deleteArena(sender, args);
                    } else {
                        sender.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
                    }
                    break;
                case "addspawn":
                    if (sender.hasPermission("FightValley.*") || sender.hasPermission("FightValley.AddSpawn")) {
                        addSpawn(sender, args);
                    } else {
                        sender.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
                    }
                    break;
                case "highlightSpawns":
                    if (sender.hasPermission("FightValley.*") || sender.hasPermission("FightValley.HighlightSpawns")) {
                        highlightSpawns(sender, args);
                    } else {
                        sender.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
                    }
                    break;
                case "setlobby":
                    if (sender.hasPermission("FightValley.*") || sender.hasPermission("FightValley.SetLobby")) {
                        setLobby(sender, args);
                    } else {
                        sender.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
                    }
                    break;
                case "setexit":
                    if (sender.hasPermission("FightValley.*") || sender.hasPermission("FightValley.SetExit")) {
                        setExit(sender, args);
                    } else {
                        sender.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
                    }
                    break;
                case "removespawn":
                    if (sender.hasPermission("FightValley.*") || sender.hasPermission("FightValley.RemoveSpawn")) {
                        removeSpawn(sender, args);
                    } else {
                        sender.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
                    }
                    break;
                case "join":
                    if (sender.hasPermission("FightValley.*") || sender.hasPermission("FightValley.JoinArena")) {
                        joinArena(sender, args);
                    } else {
                        sender.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
                    }
                    break;
                case "start":
                    if (sender.hasPermission("FightValley.*") || sender.hasPermission("FightValley.StartArena")) {
                        startArena(sender, args);
                    } else {
                        sender.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
                    }
                    break;
                case "stop":
                    if (sender.hasPermission("FightValley.*") || sender.hasPermission("FightValley.StopArena")) {
                        stopArena(sender, args);
                    } else {
                        sender.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
                    }
                    break;
                case "list":
                    if (sender.hasPermission("FightValley.*") || sender.hasPermission("FightValley.List")) {
                        list(sender, args);
                    } else {
                        sender.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
                    }
                    break;
                case "leave":
                    if (sender.hasPermission("FightValley.*") || sender.hasPermission("FightValley.LeaveArena")) {
                        leaveArena(sender, args);
                    } else {
                        sender.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
                    }
                    break;
                case "players":
                    if (sender.hasPermission("FightValley.*") || sender.hasPermission("FightValley.ListPlayers")) {
                        listPlayers(sender, args);
                    } else {
                        sender.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
                    }
                    break;
                default:
                    sender.sendMessage("§cUnknown command!");
                    break;
            }
        }
        return false;
    }

    private void createArena(CommandSender sender, String[] args) {
        //fightvalley createArena <name> <minPlayers> <maxPlayers>
        if(args.length != 4) {
            sender.sendMessage("§cUsage: /fightvalley createarena <name> <minPlayers> <maxPlayers>");
        } else {
            String name = args[1];

            if(SQL_Arena.arenaExists(args[1])) {
                sender.sendMessage("§cArena " + name + " already exists!");
                return;
            }

            int minPlayers = Integer.parseInt(args[2]);
            int maxPlayers = Integer.parseInt(args[3]);
            SQL_Arena.createArena(name, minPlayers, maxPlayers);
            Main.arenas.put(name, new Arena(name, maxPlayers, minPlayers, Main.defaultArenaTime));
            sender.sendMessage("§6Arena §c" + name + "§6 was created...");
        }
    }

    private void deleteArena(CommandSender sender, String[] args) {
        //fightvalley deleteArena <name>
        if(args.length < 2 || args.length > 2) {
            sender.sendMessage("§cUsage: /fightvalley deletearena <name>");
        } else {
            String name = args[1];

            if(!SQL_Arena.arenaExists(args[1])) {
                sender.sendMessage("§cArena " + name + " does not exist!");
                return;
            }

            SQL_Arena.deleteArena(name); //Deletes the arena
            SQL_ArenaSpawn.deleteAllArenaSpawns(name); //Deletes all spawns of this arena
            sender.sendMessage("§6Arena §c" + name + "§6 was deleted...");
        }
    }

    private void setLobby (CommandSender sender, String[] args) {
        //fightvalley setLobby <name>
        if(args.length < 2 || args.length > 2) {
            sender.sendMessage("§cUsage: /fightvalley setlobby <name>");
        } else {
            String name = args[1];

            if(!SQL_Arena.arenaExists(args[1])) {
                sender.sendMessage("§cArena " + name + " does not exist!");
                return;
            }

            Location loc = ((Player) sender).getLocation().toBlockLocation();
            SQL_Arena.setDouble(name, "LobbyX", loc.x());
            SQL_Arena.setDouble(name, "LobbyY", loc.y());
            SQL_Arena.setDouble(name, "LobbyZ", loc.z());
            SQL_Arena.setString(name, "LobbyWorld", loc.getWorld().getName());

            if(SQL_Arena.hasLobby(name) && SQL_Arena.hasExit(name) && SQL_ArenaSpawn.getSpawnLocations(name).size() > 0) {
                Main.arenas.get(name).setState(ArenaStates.WAITING);
            } else {
                Main.arenas.get(name).setState(ArenaStates.DISABLED);
            }

            sender.sendMessage("§6Lobby for Arena §c" + name + "§6 was set...");
        }
    }

    private void setExit (CommandSender sender, String[] args) {
        //fightvalley setExit <name>
        if(args.length < 2 || args.length > 2) {
            sender.sendMessage("§cUsage: /fightvalley setexit <name>");
        } else {
            String name = args[1];

            if(!SQL_Arena.arenaExists(args[1])) {
                sender.sendMessage("§cArena " + name + " does not exist!");
                return;
            }

            Location loc = ((Player) sender).getLocation().toBlockLocation();
            SQL_Arena.setDouble(name, "ExitX", loc.x());
            SQL_Arena.setDouble(name, "ExitY", loc.y());
            SQL_Arena.setDouble(name, "ExitZ", loc.z());
            SQL_Arena.setString(name, "ExitWorld", loc.getWorld().getName());

            if(SQL_Arena.hasLobby(name) && SQL_Arena.hasExit(name) && SQL_ArenaSpawn.getSpawnLocations(name).size() > 0) {
                Main.arenas.get(name).setState(ArenaStates.WAITING);
            } else {
                Main.arenas.get(name).setState(ArenaStates.DISABLED);
            }

            sender.sendMessage("§6Exit for Arena §c" + name + "§6 was set...");
        }
    }

    private void addSpawn(CommandSender sender, String[] args) {
        //fightvalley addSpawn <name>
        if(args.length < 2 || args.length > 2) {
            sender.sendMessage("§cUsage: /fightvalley addspawn <arena>");
        } else {
            String name = args[1];

            if(!SQL_Arena.arenaExists(args[1])) {
                sender.sendMessage("§cArena " + name + " does not exist!");
                return;
            }

            if(SQL_ArenaSpawn.arenaSpawnExists(name, ((Player) sender).getLocation())) {
                sender.sendMessage("§cSpawn already exists here for this arena!");
                return;
            }

            Location loc = ((Player) sender).getLocation();
            SQL_ArenaSpawn.createArenaSpawn(name, loc);

            if(SQL_Arena.hasLobby(name) && SQL_Arena.hasExit(name) && SQL_ArenaSpawn.getSpawnLocations(name).size() > 0) {
                Main.arenas.get(name).setState(ArenaStates.WAITING);
            } else {
                Main.arenas.get(name).setState(ArenaStates.DISABLED);
            }

            sender.sendMessage("§6Spawn for Arena §c" + name + "§6 was added...");
        }
    }

    private void removeSpawn (CommandSender sender, String[] args) {
        //fightvalley removeSpawn <name>
        if (args.length < 2 || args.length > 3) {
            sender.sendMessage("§cUsage: /fightvalley removespawn <arena> [id]");
        } else if (args.length == 2) {
            String name = args[1];

            if(!SQL_Arena.arenaExists(args[1])) {
                sender.sendMessage("§cArena " + name + " does not exist!");
                return;
            }

            if(!SQL_ArenaSpawn.arenaSpawnExists(name, ((Player) sender).getLocation())) {
                sender.sendMessage("§cNo spawn exists here for this arena!");
                return;
            }

            Location loc = ((Player) sender).getLocation();
            SQL_ArenaSpawn.deleteArenaSpawn(name, loc);

            if(SQL_Arena.hasLobby(name) && SQL_Arena.hasExit(name) && SQL_ArenaSpawn.getSpawnLocations(name).size() > 0) {
                Main.arenas.get(name).setState(ArenaStates.WAITING);
            } else {
                Main.arenas.get(name).setState(ArenaStates.DISABLED);
            }

            sender.sendMessage("§6Spawn for Arena §c" + name + "§6 was removed...");
        } else {
            sender.sendMessage("§6Removing Spawn...");
            String name = args[1];
            int spawnId = Integer.parseInt(args[2]);

            if(!SQL_Arena.arenaExists(args[1])) {
                sender.sendMessage("§cArena " + name + " does not exist!");
                return;
            }

            SQL_ArenaSpawn.deleteArenaSpawn(spawnId);

            if(SQL_Arena.hasLobby(name) && SQL_Arena.hasExit(name) && SQL_ArenaSpawn.getSpawnLocations(name).size() > 0) {
                Main.arenas.get(name).setState(ArenaStates.WAITING);
            } else {
                Main.arenas.get(name).setState(ArenaStates.DISABLED);
            }

            sender.sendMessage("§6Spawn for Arena §c" + name + "§6 was removed...");
        }
    }

    private void highlightSpawns(CommandSender sender, String[] args) {
        if(args.length < 2 || args.length > 2) {
            sender.sendMessage("§cUsage: /fightvalley highlightSpawns <name>");
        } else {
            String arena = args[1];
            sender.sendMessage("§6Highlighting Spawns for Arena §c" + arena + "§6...");
            List<Location> locations = SQL_ArenaSpawn.getSpawnLocations(arena);
            World world = Bukkit.getWorld(SQL_Arena.getString(arena, "LobbyWorld"));
            for (Location loc : locations) {
                //Highlight the spawn with particles
                world.spawnParticle(Particle.DUST_PILLAR, loc.add(0, 1, 0), 50, 0, 0.5, 0, Bukkit.createBlockData(Material.DIAMOND_BLOCK));
            }
        }
    }

    private void joinArena(CommandSender sender, String[] args) {
        //fightvalley joinArena <name>
        if (args.length < 2 || args.length > 2) {
            sender.sendMessage("§cUsage: /fightvalley joinarena <name>");
        } else {
            String name = args[1];
            Player p = (Player) sender;

            for (Arena arena : Main.arenas.values()) {
                if (arena.getPlayers().contains(p)) {
                    sender.sendMessage("§6You are already in the arena §c" + arena.getName() + "§6!");
                    sender.sendMessage("§6Use §c/fightvalley leavearena §6to leave the current arena!");
                    return;
                }
            }

            if (!SQL_Arena.arenaExists(args[1])) {
                sender.sendMessage("§cArena " + name + " does not exist!");
                return;
            }

            if(Main.arenas.get(name).getPlayers().size() >= Main.arenas.get(name).getMaxPlayers()) {
                sender.sendMessage("§cArena " + name + " is full!");
                return;
            }

            ArenaPlayer ap = ArenaPlayer.GetArenaPlayer(p.getUniqueId());

            if (Main.arenas.get(name).getState() == ArenaStates.WAITING || Main.arenas.get(name).getState() == ArenaStates.STARTING) {
                Main.arenas.get(name).joinArena(ap);
                sender.sendMessage("§6You joined Arena §c" + name + "§6...");
            } else if (Main.arenas.get(name).getState() == ArenaStates.DISABLED){
                sender.sendMessage("§6Arena §c" + name + "§6 is not ready yet...");
            }else {
                sender.sendMessage("§cArena " + name + " is already running!");
            }
        }
    }

    private void startArena(CommandSender sender, String[] args) {
        //fightvalley startArena <name>
        if (args.length < 2 || args.length > 2) {
            sender.sendMessage("§cUsage: /fightvalley startarena <name>");
        } else {
            String arenaName = args[1];

            if (!SQL_Arena.arenaExists(args[1])) {
                sender.sendMessage("§cArena " + arenaName + " does not exist!");
                return;
            }

            if (Main.arenas.get(arenaName).getState() == ArenaStates.WAITING) {
                if(Main.arenas.get(arenaName).getPlayers().size() < Main.arenas.get(arenaName).getMinPlayers()) {
                    sender.sendMessage("§cNot enough players to start the game!");
                    return;
                }
                Main.arenas.get(arenaName).Start();
                sender.sendMessage("§6Arena §c" + arenaName + "§6 started...");
            } else {
                sender.sendMessage("§cArena " + arenaName + " is already running!");
            }
        }
    }

    private void list(CommandSender sender, String[] args) {
        //fightvalley list
        HashMap<String, Arena> arenas = Main.arenas;
        sender.sendMessage("--------------------");
        sender.sendMessage("§6Listing " + arenas.size() + " Arenas...");
        for (Map.Entry<String, Arena> entry : arenas.entrySet()) {
            String k = entry.getKey();
            Arena v = entry.getValue();
            sender.sendMessage(k + " | " + v.getState().toString() + " | Players: " + v.getPlayers().size() + "/" + v.getMaxPlayers());
        }
        sender.sendMessage("--------------------");
    }

    private void listPlayers(CommandSender sender, String[] args) {
        //fightvalley list
        HashMap<UUID, ArenaPlayer> players = Main.arenaPlayers;
        sender.sendMessage("--------------------");
        sender.sendMessage("§6Listing " + players.size() + " Players...");
        for (Map.Entry<UUID, ArenaPlayer> entry : players.entrySet()) {
            ArenaPlayer v = entry.getValue();
            if(v.getCurrentArena() != null) {
                sender.sendMessage(v.getOfflinePlayer().getName() + " | In Arena: " + v.getCurrentArena().getName() + " | HighestStreak: " + v.getHighestStreak());
            } else {
                sender.sendMessage(v.getOfflinePlayer().getName() + " | Not in an Arena" + " | HighestStreak: " + v.getHighestStreak());
            }
        }
        sender.sendMessage("--------------------");
    }

    private void stopArena(CommandSender sender, String[] args){
        //fightvalley stopArena <name>
        if (args.length < 2 || args.length > 2) {
            sender.sendMessage("§cUsage: /fightvalley stoparena <name>");
        } else {
            sender.sendMessage("§6Stopping Arena...");
            String arenaName = args[1];

            if (!SQL_Arena.arenaExists(args[1])) {
                sender.sendMessage("§cArena " + arenaName + " does not exist!");
                return;
            }

            ArenaStates arenaState = Main.arenas.get(arenaName).getState();
            if (arenaState == ArenaStates.RUNNING || arenaState == ArenaStates.STARTING) {
                Main.arenas.get(arenaName).EndGame();
                sender.sendMessage("§6Arena §c" + arenaName + "§6 stopped...");
            } else {
                sender.sendMessage("§cArena " + arenaName + " is not running!");
            }
        }
    }

    private void leaveArena(CommandSender sender, String[] args) {
        //fightvalley leaveArena
        Player player = (Player) sender;
        ArenaPlayer p = Main.arenaPlayers.get(player.getUniqueId());
        if(p.getCurrentArena() != null) {
            sender.sendMessage("§6You left Arena §c" + p.getCurrentArena().getName() + "§6...");
            p.getCurrentArena().leaveArena(p);
            return;
        }
        sender.sendMessage("§cYou are not in an arena!");
    }

    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        List<String> arenaNames = SQL_Arena.getArenaNames();
        if (args.length == 1) {
            return Arrays.asList("create", "delete", "addspawn", "highlightSpawns", "setlobby", "setexit", "removespawn", "join", "start", "list", "players", "stop", "leave");
            /*List<String> options = Arrays.asList("create", "delete", "addspawn", "highlightSpawns", "setlobby", "setexit", "removespawn", "join", "start", "list", "players", "stop", "leave");
            for (String option : options) {
                if (!option.toLowerCase().startsWith(args[0].toLowerCase())) {
                    options.remove(option);
                }
            }
            return options;*/
        } else if (args.length == 2) {
            switch (args[0]) {
                case "create":
                    return Arrays.asList("<name>");
                case "delete":
                    return arenaNames;
                case "setlobby":
                    return arenaNames;
                case "setexit":
                    return arenaNames;
                case "addspawn":
                    return arenaNames;
                case "highlightSpawns":
                    return arenaNames;
                case "removespawn":
                    return arenaNames;
                case "join":
                    return arenaNames;
                case "start":
                    return arenaNames;
            }
        } else if (args.length == 3) {
            switch (args[0]) {
                case "create":
                    return Arrays.asList("<minPlayers>");
                case "removespawn":
                    return SQL_ArenaSpawn.getSpawnIDs(args[1]);
            }
        } else if (args.length == 4) {
            switch (args[0]) {
                case "create":
                    return Arrays.asList("<maxPlayers>");
            }
        }
        return null;
    }
}