package me.dreamisland.de.commands;

import org.bukkit.FireworkEffect.Type;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.dreamisland.de.Prefixes;
import me.dreamisland.de.SkyBlock;
import me.dreamisland.de.SkyBlockGenerator;

public class SkyBlockCommands implements CommandExecutor {
	
	public SkyBlockCommands() {
		//Registriere Commands
		SkyBlock.getInstance().getCommand("simulate").setExecutor(this);
		SkyBlock.getInstance().getCommand("undo").setExecutor(this);
		SkyBlock.getInstance().getCommand("is").setExecutor(this);
		SkyBlock.getInstance().getCommand("spawn").setExecutor(this);
		SkyBlock.getInstance().getCommand("accept").setExecutor(this);
		SkyBlock.getInstance().getCommand("deny").setExecutor(this);
	}
	
	/*
	 * TODO: Command-Execution
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("simulate")) {
				if(SkyBlock.hasPermission(p, "skyblock.simulate")) {	//Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
					
					switch(args.length) {
					case 0:
						//Starte den Simulation-Vorgang
						
						if(SkyBlockGenerator.generateIfReady()) {
							p.sendMessage(">>> §bDer Generier-Vorgang für die Simulation wird ausgeführt...");
						}else p.sendMessage(">>> §cDer Generator ist nicht bereit für die Simulation. Probiere es gleich erneut.");
						break;
					case 1:
						//Starte den Simulation-Vorgang
						
						if(SkyBlockGenerator.generateIfReady(Integer.valueOf(args[0]))) {
							p.sendMessage(">>> §bDer Generier-Vorgang für die Simulation wird ausgeführt...");
							p.sendMessage(">>> §eParameter: [§fAmountOfIsland=§7"+args[0]+"§f, IslandSize=§7Previous§f, SpaceBetweenIslands=§7Previous§e]");
						}else p.sendMessage(">>> §cDer Generator ist nicht bereit für die Simulation. Probiere es gleich erneut.");
						break;
					case 2:
						//Starte den Simulation-Vorgang
						
						if(SkyBlockGenerator.generateIfReady(Integer.valueOf(args[0]), Integer.valueOf(args[1]))) {
							p.sendMessage(">>> §bDer Generier-Vorgang für die Simulation wird ausgeführt...");
							p.sendMessage(">>> §eParameter: [§fAmountOfIsland=§7"+args[0]+"§f, IslandSize=§7"+args[1]+"§f, SpaceBetweenIslands=§7Previous§e]");
						}else p.sendMessage(">>> §cDer Generator ist nicht bereit für die Simulation. Probiere es gleich erneut.");
						break;
					case 3:
						//Starte den Simulation-Vorgang
						
						if(SkyBlockGenerator.generateIfReady(Integer.valueOf(args[0]), Integer.valueOf(args[1]), Integer.valueOf(args[2]))) {
							p.sendMessage(">>> §bDer Generier-Vorgang für die Simulation wird ausgeführt...");
							p.sendMessage(">>> §eParameter: [§fAmountOfIsland=§7"+args[0]+"§f, IslandSize=§7"+args[1]+"§f, SpaceBetweenIslands=§7"+args[2]+"§e]");
						}else p.sendMessage(">>> §cDer Generator ist nicht bereit für die Simulation. Probiere es gleich erneut.");
						break;
					}
				}
			}else if(cmd.getName().equalsIgnoreCase("undo")) {
				if(SkyBlock.hasPermission(p, "skyblock.undo")) {	//Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
					SkyBlockGenerator.undo();
					p.sendMessage(">>> §aDie Simulationen wurden rückgängig gemacht");
				}
			}else if(cmd.getName().equalsIgnoreCase("is")) {
				int size = 0;
				int amount = 0;
				int space = 0;
				switch(args.length) {
				case 0:
					SkyBlockCommandFunction.teleportToIsland(p);
					break;
				case 1:
					if(args[0].equalsIgnoreCase("help") && SkyBlock.hasPermission(p, "skyblock.island.help")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						sendCommandInfo(p, "is");
					}else if(args[0].equalsIgnoreCase("create") && SkyBlock.hasPermission(p, "skyblock.island.create")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						SkyBlockCommandFunction.createIsland(p);
					}else if(args[0].equalsIgnoreCase("delworld") && SkyBlock.hasPermission(p, "skyblock.island.delworld")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						SkyBlockCommandFunction.deleteWorld(p);
					}else if(args[0].equalsIgnoreCase("delete") && SkyBlock.hasPermission(p, "skyblock.island.delete")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						SkyBlockCommandFunction.deleteIsland(p);
					}else if(args[0].equalsIgnoreCase("kick") && SkyBlock.hasPermission(p, "skyblock.island.kick")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						sendCommandInfo(p, "is");
					}else if(args[0].equalsIgnoreCase("generatefile") && SkyBlock.hasPermission(p, "skyblock.*")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						p.sendMessage(Prefixes.SERVER.getPrefix()+"Generiere §eInsel-Index-File.yml §7mit folgenden Spezifikationen:");
						p.sendMessage(Prefixes.SERVER.getPrefix()+" §7» §fIsland-Size: §e"+SkyBlockGenerator.amountOfIslands);
						p.sendMessage(Prefixes.SERVER.getPrefix()+" §7» §dIsland-Amount: §e"+SkyBlockGenerator.issize);
						p.sendMessage(Prefixes.SERVER.getPrefix()+" §7» §fSpace-Between-Islands: §e"+SkyBlockGenerator.spaceBetweenIslands);
						SkyBlock.generateNewIndexFile(true);
					}else sendCommandInfo(p, "is");
					break;
				case 2:
					if(args[0].equalsIgnoreCase("visit") && SkyBlock.hasPermission(p, "skyblock.island.visit")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						SkyBlockCommandFunction.requestVisit(p, args);
					}else if(args[0].equalsIgnoreCase("kick") && SkyBlock.hasPermission(p, "skyblock.island.kick")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						SkyBlockCommandFunction.kickPlayer(p, args[1]);
					}else if(args[0].equalsIgnoreCase("generatefile") && SkyBlock.hasPermission(p, "skyblock.*")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						try{
							size = Integer.parseInt(args[1]);
							SkyBlock.generateNewIndexFile(true);
							p.sendMessage(Prefixes.SERVER.getPrefix()+"Generiere §eInsel-Index-File.yml §7mit folgenden Spezifikationen:");
							p.sendMessage(Prefixes.SERVER.getPrefix()+" §7» §fIsland-Size: §e"+size+" Blöcken");
							p.sendMessage(Prefixes.SERVER.getPrefix()+" §7» §dIsland-Amount: §e"+SkyBlockGenerator.issize+" Blöcken");
							p.sendMessage(Prefixes.SERVER.getPrefix()+" §7» §fSpace-Between-Islands: §e"+SkyBlockGenerator.spaceBetweenIslands+" Blöcken");
						}catch(NumberFormatException ex) {
							p.sendMessage(Prefixes.SERVER.getPrefix()+"§cEs dürfen nur reine Numerische Ziffern als Island-Size angegeben werden");
						}
					}else sendCommandInfo(p, "is");
					break;
				case 3:
					if(args[0].equalsIgnoreCase("generatefile") && SkyBlock.hasPermission(p, "skyblock.*")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						try{
							size = Integer.parseInt(args[1]);
						}catch(NumberFormatException ex) {
							p.sendMessage(Prefixes.SERVER.getPrefix()+"§cEs dürfen nur reine Numerische Ziffern als Island-Size angegeben werden");
							break;
						}
						try{
							amount = Integer.parseInt(args[2]);
							SkyBlock.generateNewIndexFile(true, size, amount);
							p.sendMessage(Prefixes.SERVER.getPrefix()+"Generiere §eInsel-Index-File.yml §7mit folgenden Spezifikationen:");
							p.sendMessage(Prefixes.SERVER.getPrefix()+" §7» §fIsland-Size: §e"+size+" Blöcken");
							p.sendMessage(Prefixes.SERVER.getPrefix()+" §7» §dIsland-Amount: §e"+amount+" Blöcken");
							p.sendMessage(Prefixes.SERVER.getPrefix()+" §7» §fSpace-Between-Islands: §e"+SkyBlockGenerator.spaceBetweenIslands+" Blöcken");
						}catch(NumberFormatException ex) {
							p.sendMessage(Prefixes.SERVER.getPrefix()+"§cEs dürfen nur reine Numerische Ziffern als Island-Amount angegeben werden");
						}
					}
					break;
				case 4:
					if(args[0].equalsIgnoreCase("generatefile") && SkyBlock.hasPermission(p, "skyblock.*")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						try{
							size = Integer.parseInt(args[1]);
						}catch(NumberFormatException ex) {
							p.sendMessage(Prefixes.SERVER.getPrefix()+"§cEs dürfen nur reine Numerische Ziffern als Island-Size angegeben werden");
							break;
						}
						try{
							amount = Integer.parseInt(args[2]);
						}catch(NumberFormatException ex) {
							p.sendMessage(Prefixes.SERVER.getPrefix()+"§cEs dürfen nur reine Numerische Ziffern als Island-Amount angegeben werden");
						}
						try{
							space = Integer.parseInt(args[3]);
							SkyBlock.generateNewIndexFile(true, size, amount, space);
							p.sendMessage(Prefixes.SERVER.getPrefix()+"Generiere §eInsel-Index-File.yml §7mit folgenden Spezifikationen:");
							p.sendMessage(Prefixes.SERVER.getPrefix()+" §7» §fIsland-Size: §e"+size+" Blöcken");
							p.sendMessage(Prefixes.SERVER.getPrefix()+" §7» §dIsland-Amount: §e"+amount+" Blöcken");
							p.sendMessage(Prefixes.SERVER.getPrefix()+" §7» §fSpace-Between-Islands: §e"+space+" Blöcken");
						}catch(NumberFormatException ex) {
							p.sendMessage(Prefixes.SERVER.getPrefix()+"§cEs dürfen nur reine Numerische Ziffern als Space-Between-Islands angegeben werden");
						}
					}
					break;
					default:
						sendCommandInfo(p, "is");
						break;
				}
			}else if(cmd.getName().equalsIgnoreCase("spawn")) {
				if(SkyBlock.hasPermission(p, "skyblock.spawn")) {
					p.teleport(SkyBlock.spawn.clone().add(0,0.5,0));
					SkyBlock.spawnFireworks(p.getLocation().clone(), 1, true, true, Type.BALL_LARGE);
				}
			}else if(cmd.getName().equalsIgnoreCase("accept")) {
				if(SkyBlock.hasPermission(p, "skyblock.accept")) {
					SkyBlockCommandFunction.acceptRequest(p);
				}
			}else if(cmd.getName().equalsIgnoreCase("deny")) {
				if(SkyBlock.hasPermission(p, "skyblock.deny")) {
					SkyBlockCommandFunction.denyRequest(p);
				}
			}
		}else sender.sendMessage("§cDer Befehl ist nur  Spieler!");
		return false;
	}
	
	/*
	 * TODO: sendCommandInfo() sendet Information §ber einen command an einen Spieler
	 */
	public void sendCommandInfo(Player p, String cmd) {
		if(cmd.equalsIgnoreCase("is")) {
			p.sendMessage("");
			if(SkyBlock.hasPermission(p, "skyblock.island")) {
				p.sendMessage(" §7» §b/is §fTeleport zur Insel");
				if(p.hasPermission("skyblock.island")) p.sendMessage(" §7» §b/is help §fHilfe zu SkyBlock Commands");
				if(p.hasPermission("skyblock.island")) p.sendMessage(" §7» §b/is create §fInsel erstellen");
				if(p.hasPermission("skyblock.island")) p.sendMessage(" §7» §b/is delete §fInsel löschen");
				if(p.hasPermission("skyblock.island")) p.sendMessage(" §7» §b/is visit <Player> §fAndere Inseln besuchen");
				if(p.hasPermission("skyblock.island")) p.sendMessage(" §7» §b/is kick <Player> §fSpieler von der Insel schmeißen");
				if(p.hasPermission("skyblock.island")) p.sendMessage(" §7» §b/is ban <Player> §fSpieler von der Insel verbannen");
				if(p.hasPermission("skyblock.island")) p.sendMessage(" §7» §b/is pardon <Player> §fHebe eine Verbannung auf");
				if(p.hasPermission("skyblock.island")) p.sendMessage(" §7» §b/is addfriend <Player> §fSpieler zur Insel einladen");
			}
			if(p.hasPermission("skyblock.*")) p.sendMessage(" §cDEV-ONLY §7» §b/is generatefile [§eIsland-Amount§b] [§eIsland-Size§b] [§eSpace-Between-Islands§b] §fGeneriere eine §eInsel-Index-File.yml §7Datei."
					+ "Aber Achtung: Das Generieren dieser File öffnet ein Windows-Fenster welches nur sichtbar ist, wenn du dieses Plugin auf einem Localhost betreibst!");
		}
	}

}
