package me.ichmagomaskekse.de.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ichmagomaskekse.de.Prefixes;
import me.ichmagomaskekse.de.SkyBlock;
import me.ichmagomaskekse.de.schematics.SchematicManager;
import me.ichmagomaskekse.de.schematics.SchematicManager.SchematicProfile;

public class SchematicCommands implements CommandExecutor {
	
	public SchematicCommands() {
		SkyBlock.getInstance().getCommand("s").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			
			if(cmd.getName().equalsIgnoreCase("s") && SkyBlock.hasPermission(p, "skyblock.schematics.schem")) {				
				switch(args.length) {
				case 0:
					sendCommandInfo(p, cmd.getName());
					break;
				case 1:
					if(args[0].equalsIgnoreCase("copy") && SkyBlock.hasPermission(p, "skyblock.schematics.copy")) {
						SchematicProfile profile = SchematicManager.getProfile(p);
						if(profile.readyTocopy()) {
							p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Die Region wird kopiert..");
							if(profile.getSchematic().copy(true)) {
								p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Die Region wurde kopiert");
							}else p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"§cDie Region konnte nicht kopiert werden");
						}else {
							p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Ich habe die Copy Funktion erstmal deaktiviert. Sie bringt den Server zum Crashen hehe");
							p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Du musst §7§n2§e Location auswählen");
						}
					}
					break;
				case 2:
					if(args[0].equalsIgnoreCase("copy") && SkyBlock.hasPermission(p, "skyblock.schematics.copy")) {
						if(args[1].equalsIgnoreCase("-a")) {							
							SchematicProfile profile = SchematicManager.getProfile(p);
							if(profile.readyTocopy()) {
								p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Die Region wird kopiert..");
								if(profile.getSchematic().copy(false)) {
									p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Die Region wurde kopiert");
								}else p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"§cDie Region konnte nicht kopiert werden");
							}else {
								p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Ich habe die Copy Funktion erstmal deaktiviert. Sie bringt den Server zum Crashen hehe");
//								p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Du musst §7§n2§e Location auswählen");
							}
						}
					}
					break;
				default:
					break;
				}
			}
		}
		return false;
	}
	
	/*
	 * TODO: sendCommandInfo() sendet Information über einen command an einen Spieler
	 */
	public void sendCommandInfo(Player p, String cmd) {
		if(cmd.equalsIgnoreCase("s")) {
			p.sendMessage("");
			p.sendMessage(" §6Info: §eMit der Gold Axt kann man die region markieren");
			p.sendMessage(" §7» §6/s §fHilfe zum Schematic-System");
			p.sendMessage(" §7» §6/s save <Name> §fSpeichert ausgewählte Schematic");
			p.sendMessage(" §7» §6/s copy [-a] §fKopiert Bereich zur Schematic");
		}
	}
}
