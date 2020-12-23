package me.skytale.de.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import me.skytale.de.SkyBlock;
import me.skytale.de.Chat.MessageType;

public class BroadcasterCommands implements CommandExecutor {

	public BroadcasterCommands() {
		SkyBlock.getSB().getCommand("broadcaster").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("broadcaster")) {
			if(SkyBlock.hasPermission(sender, "skyblock.broadcaster")) {	//Benutzerdifinierte Permissionabfrage
				switch(args.length) {
				case 0:
					if(sender instanceof Player) {
						Player p = (Player) sender;
						SkyBlock.sendMessage(MessageType.INFO, p, " §7» §9/broadcaster <§fstart§9:§fstop§9:§freload§9>");
					}else if(sender instanceof ConsoleCommandSender) SkyBlock.sendConsoleMessage(MessageType.INFO, " §7» §9/broadcaster <§fstart§9:§fstop§9:§freload§9>");
					break;
				case 1:
					if(args[0].equalsIgnoreCase("start")) {
						if(sender instanceof Player) {
							Player p = (Player) sender;
							if(SkyBlock.getBroadcaster().startTimer()) {
								SkyBlock.sendMessage(MessageType.INFO, p, "Der Broadcaster wurde gestartet");
							}else SkyBlock.sendMessage(MessageType.ERROR, p, "Der Broadcaster läuft bereits");
						}else if(sender instanceof ConsoleCommandSender) {
							if(SkyBlock.getBroadcaster().startTimer()) {
								SkyBlock.sendConsoleMessage(MessageType.INFO, "Der Broadcaster wurde gestartet");
							}else SkyBlock.sendConsoleMessage(MessageType.ERROR, "Der Broadcaster ist bereits deaktiviert");
						}
					}else if(args[0].equalsIgnoreCase("stop")) {
						if(sender instanceof Player) {
							Player p = (Player) sender;
							if(SkyBlock.getBroadcaster().stop()) {
								SkyBlock.sendMessage(MessageType.INFO, p, "Der Broadcaster wurde gestoppt");
							}else SkyBlock.sendMessage(MessageType.ERROR, p, "Der Broadcaster war nicht aktiviert");
						}else if(sender instanceof ConsoleCommandSender) {
							if(SkyBlock.getBroadcaster().stop()) {
								SkyBlock.sendConsoleMessage(MessageType.INFO, "Der Broadcaster wurde gestoppt");
							}else SkyBlock.sendConsoleMessage(MessageType.ERROR, "Der Broadcaster war nicht aktiviert");
						}
					}else if(args[0].equalsIgnoreCase("reload")) {
						if(sender instanceof Player) {
							Player p = (Player) sender;
							SkyBlock.getBroadcaster().stop();
							SkyBlock.getBroadcaster().loadSettings();
							SkyBlock.sendMessage(MessageType.INFO, p, "Der Broadcaster wurde neugeladen. Starte ihne mit §9/broadcaster start");
						}else if(sender instanceof ConsoleCommandSender) {
								SkyBlock.getBroadcaster().stop();
								SkyBlock.getBroadcaster().loadSettings();
								SkyBlock.sendConsoleMessage(MessageType.INFO, "Der Broadcaster wurde neugeladen. Starte ihne mit §9/broadcaster start");
						}
					}
					break;
					default:
						if(sender instanceof Player) {
							Player p = (Player) sender;
							SkyBlock.sendMessage(MessageType.INFO, p, " §7» §9/broadcaster <§fstart§9:§fstop§9:§freload§9>");
						}else if(sender instanceof ConsoleCommandSender) SkyBlock.sendConsoleMessage(MessageType.INFO, " §7» §9/broadcaster <§fstart§9:§fstop§9:§freload§9>");
						break;
				}
			}
		}
		return false;
	}
	
	
	
}
