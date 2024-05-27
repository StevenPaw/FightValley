package com.stevenpaw.fightvalley.common.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CMDfightvalley implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (sender.hasPermission("FightValley.*") || sender.hasPermission("FightValley.JoinArena")) {
                sender.sendMessage("§6Jetzt solltest du einer Arena joinen...");
                sender.sendMessage("§6----------------------------");
            } else
                sender.sendMessage("§cDu hast keine Berechtigung für diesen Befehl!");
        }
        return false;
    }
}