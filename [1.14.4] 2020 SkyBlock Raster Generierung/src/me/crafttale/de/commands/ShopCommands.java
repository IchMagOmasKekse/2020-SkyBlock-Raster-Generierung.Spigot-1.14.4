package me.crafttale.de.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.crafttale.de.Chat.MessageType;
import me.crafttale.de.SkyBlock;
import me.crafttale.de.economy.ShopCreatorItem;
import me.crafttale.de.log.XLogger;
import me.crafttale.de.log.XLogger.LogType;

public class ShopCommands implements CommandExecutor {
	
	private String[] command = null; //Ein Array mit allen Commands, die in dieser Klasse verwendet werden.
	
	public ShopCommands() {
		//Initialisiere Commands
		command = new String[1];
		command[0] = "shop";
		
		//Registriere Commands
		XLogger.log(LogType.PluginInternProcess, "§eRegistriere Shop Commands");
		for(int i = 0; i != command.length; i++) {
			SkyBlock.getSB().getCommand(command[i]).setExecutor(this);
			XLogger.log(LogType.PluginInternProcess, "§aRegistriere Shop Befehl: §2"+command[i]);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		
		if(cmd.getName().equals("shop") && SkyBlock.hasPermission(sender, "skyblock.shop")) {			
			if(sender instanceof Player) {
				Player p = (Player) sender;
				switch(args.length) {
				case 0:
					sendCommandInfo(sender, "shop");
					break;
				case 1:
					if(args[0].equalsIgnoreCase("spawner")) p.getInventory().addItem(ShopCreatorItem.getCreatorItem());
					else if(args[0].equalsIgnoreCase("ore")) p.getInventory().addItem(ShopCreatorItem.getCreatorItem("ore-shop"));
					else if(args[0].equalsIgnoreCase("crops")) p.getInventory().addItem(ShopCreatorItem.getCreatorItem("garden-shop"));
					break;
				}
			}else SkyBlock.sendConsoleMessage(MessageType.ERROR, "Dieser Befehl ist nur für Spieler.");
		}
		
		return false;
	}
	
	public void sendCommandInfo(CommandSender sender, String cmd) {
		if(cmd.equals("shop")) {
			if(SkyBlock.hasPermission(sender, "skyblock.shop")) {
				sender.sendMessage(" §9Shop-Befehle");
				sender.sendMessage(" §7» §9/shop §fErhalte Hilfe zum Shop Command");
				sender.sendMessage(" §7» §9/shop spawner§fErhalte Spawner");
			}
		}
	}
	
}
