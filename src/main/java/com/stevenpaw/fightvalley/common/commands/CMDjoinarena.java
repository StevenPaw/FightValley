package com.stevenpaw.fightvalley.common.commands;

import com.stevenpaw.fightvalley.main.Main;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CMDjoinarena implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Economy eco = Main.getPlugin().getEconomy();

        if (args.length == 0) {
            if (sender.hasPermission("McValley.*") || sender.hasPermission("McValley.Moneystats")) {
                Map<String, Double> List = new HashMap<>();
                for(OfflinePlayer all : Bukkit.getOfflinePlayers()){
                    List.put(all.getName(), eco.getBalance(all));
                }
                List = sortByValue(List);
                sender.sendMessage("§6----------------------------\n§aTop Spieler mit dem meisten Geld:");
                int topboardnumber = 1;
                DecimalFormat format = new DecimalFormat("0.00");
                for (Map.Entry<String, Double> entry : List.entrySet()) {
                    if(topboardnumber <=5) {

                        sender.sendMessage("§b"+topboardnumber+". "+entry.getKey()+": "+format.format(entry.getValue())+"€");
                        topboardnumber++;
                    }
                }
                sender.sendMessage("§6----------------------------");
            } else
                sender.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
        }
        return false;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        return map.entrySet().stream().sorted(Map.Entry.comparingByValue(Collections.reverseOrder())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }


}