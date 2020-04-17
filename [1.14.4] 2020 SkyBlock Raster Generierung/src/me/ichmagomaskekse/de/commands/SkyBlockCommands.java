package me.ichmagomaskekse.de.commands;

import org.bukkit.FireworkEffect.Type;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ichmagomaskekse.de.Prefixes;
import me.ichmagomaskekse.de.SkyBlock;
import me.ichmagomaskekse.de.SkyBlockGenerator;

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
					}else sendCommandInfo(p, "is");
					break;
				case 2:
					if(args[0].equalsIgnoreCase("visit") && SkyBlock.hasPermission(p, "skyblock.island.visit")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						SkyBlockCommandFunction.requestVisit(p, args);
					}else sendCommandInfo(p, "is");
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
		}else sender.sendMessage("§cDer Befehl ist nur für Spieler!");
		return false;
	}
	
	/*
	 * TODO: sendCommandInfo() sendet Information über einen command an einen Spieler
	 */
	public void sendCommandInfo(Player p, String cmd) {
		if(cmd.equalsIgnoreCase("is")) {
			p.sendMessage("");
			p.sendMessage(" §7» §b/is §fTeleport zur Insel");
			p.sendMessage(" §7» §b/is help §fHilfe zu SkyBlock Commands");
			p.sendMessage(" §7» §b/is create §fInsel erstellen");
			p.sendMessage(" §7» §b/is delete §fInsel löschen");
			p.sendMessage(" §7» §b/is visit <Player> §fAndere Inseln besuchen");
			p.sendMessage(" §7» §b/is kick <Player> §fSpieler von der Insel schmeißen");
			p.sendMessage(" §7» §b/is ban <Player> §fSpieler von der Insel verbannen");
			p.sendMessage(" §7» §b/is pardon <Player> §fHebe eine Verbannung auf");
			p.sendMessage(" §7» §b/is addfriend <Player> §fSpieler zur Insel einladen");
		}
	}

}
