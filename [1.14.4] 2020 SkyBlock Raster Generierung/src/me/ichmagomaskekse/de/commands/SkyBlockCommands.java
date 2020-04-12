package me.ichmagomaskekse.de.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ichmagomaskekse.de.SkyBlock;
import me.ichmagomaskekse.de.SkyBlockGenerator;

public class SkyBlockCommands implements CommandExecutor {
	
	public SkyBlockCommands() {
		//Registriere Commands
		SkyBlock.getInstance().getCommand("simulate").setExecutor(this);
		SkyBlock.getInstance().getCommand("undo").setExecutor(this);
	}
	
	/*
	 * TODO: Command-Execution
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("simulate")) {
				if(hasPermission(p, "skyblock.simulate")) {	//Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
					
					switch(args.length) {
					case 0:
						//Starte den Simulation-Vorgang
						
						if(SkyBlockGenerator.generateIfReady(p)) {
							p.sendMessage(">>> §bDer Generier-Vorgang für die Simulation wird ausgeführt...");
						}else p.sendMessage(">>> §cDer Generator ist nicht bereit für die Simulation. Probiere es gleich erneut.");
						break;
					case 1:
						//Starte den Simulation-Vorgang
						
						if(SkyBlockGenerator.generateIfReady(p, Integer.valueOf(args[0]))) {
							p.sendMessage(">>> §bDer Generier-Vorgang für die Simulation wird ausgeführt...");
							p.sendMessage(">>> §eParameter: [§fAmountOfIsland=§7"+args[0]+"§f, IslandSize=§7Previous§f, SpaceBetweenIslands=§7Previous§e]");
						}else p.sendMessage(">>> §cDer Generator ist nicht bereit für die Simulation. Probiere es gleich erneut.");
						break;
					case 2:
						//Starte den Simulation-Vorgang
						
						if(SkyBlockGenerator.generateIfReady(p, Integer.valueOf(args[0]), Integer.valueOf(args[1]))) {
							p.sendMessage(">>> §bDer Generier-Vorgang für die Simulation wird ausgeführt...");
							p.sendMessage(">>> §eParameter: [§fAmountOfIsland=§7"+args[0]+"§f, IslandSize=§7"+args[1]+"§f, SpaceBetweenIslands=§7Previous§e]");
						}else p.sendMessage(">>> §cDer Generator ist nicht bereit für die Simulation. Probiere es gleich erneut.");
						break;
					case 3:
						//Starte den Simulation-Vorgang
						
						if(SkyBlockGenerator.generateIfReady(p, Integer.valueOf(args[0]), Integer.valueOf(args[1]), Integer.valueOf(args[2]))) {
							p.sendMessage(">>> §bDer Generier-Vorgang für die Simulation wird ausgeführt...");
							p.sendMessage(">>> §eParameter: [§fAmountOfIsland=§7"+args[0]+"§f, IslandSize=§7"+args[1]+"§f, SpaceBetweenIslands=§7"+args[2]+"§e]");
						}else p.sendMessage(">>> §cDer Generator ist nicht bereit für die Simulation. Probiere es gleich erneut.");
						break;
					}
				}
			}else if(cmd.getName().equalsIgnoreCase("undo")) {
				if(hasPermission(p, "skyblock.undo")) {	//Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
					SkyBlockGenerator.undo();
					p.sendMessage(">>> §aDie Simulationen wurden rückgängig gemacht");
				}
			}
		}else sender.sendMessage("§cDer Befehl ist nur für Spieler!");
		return false;
	}
	
	
	/*
	 * TODO: Benutzerdefinierte Permission-Abfrage
	 * 
	 * - Gibt eine benutzerdefinierte non-permission Message aus.
	 * - Diese Methode spart einige Zeilen an Code.
	 * 
	 */
	public boolean hasPermission(Player p, String permission) {
		if(p.hasPermission(permission)) return true;
		else {
			p.sendMessage("§cDu hast kein Recht dazu!");
			return false;
		}
	}

}
