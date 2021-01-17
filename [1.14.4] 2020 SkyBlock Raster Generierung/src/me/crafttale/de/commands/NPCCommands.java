package me.crafttale.de.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.crafttale.de.Chat.MessageType;
import me.crafttale.de.log.XLogger;
import me.crafttale.de.log.XLogger.LogType;
import me.crafttale.de.SkyBlock;
import me.crafttale.de.npc.NPCManager;
import me.crafttale.de.npc.NPCType;

public class NPCCommands implements CommandExecutor {
	
	private String[] command = null; //Ein Array mit allen Commands, die in dieser Klasse verwendet werden.
	
	public NPCCommands() {
		//Initialisiere Commands
		command = new String[1];
		command[0] = "npc";
		
		//Registriere Commands
		XLogger.log(LogType.PluginInternProcess, "§eRegistriere NPC Commands");
		for(int i = 0; i != command.length; i++) {
			SkyBlock.getSB().getCommand(command[i]).setExecutor(this);
			XLogger.log(LogType.PluginInternProcess, "§aRegistriere NPC Befehl: §2"+command[i]);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		
		if(cmd.getName().equals("npc") && SkyBlock.hasPermission(sender, "skyblock.npc")) {			
			if(sender instanceof Player) {
				Player p = (Player) sender;
				switch(args.length) {
				case 0:
					sendCommandInfo(sender, "npc");
					break;
				case 1:
					if(args[0].equalsIgnoreCase("citizen")) NPCManager.registerNewNPC(NPCType.NORMAL_CITIZEN, p.getLocation(), p);
					else if(args[0].equalsIgnoreCase("casino_suchti")) NPCManager.registerNewNPC(NPCType.CASINO_SUCHTI, p.getLocation(), p);
					else p.sendMessage(" §7» §6/npc <citizen : casino_suchti>");
					break;
				}
			}else SkyBlock.sendConsoleMessage(MessageType.ERROR, "Dieser Befehl ist nur für Spieler.");
		}
		
		return false;
	}
	
	public void sendCommandInfo(CommandSender sender, String cmd) {
		if(cmd.equals("npc")) {
			if(SkyBlock.hasPermission(sender, "skyblock.npc")) {
				sender.sendMessage(" §6NPC-Befehle");
				sender.sendMessage(" §7» §6/npc §fErhalte Hilfe zum NPC Command");
			}
		}
	}
	
}
