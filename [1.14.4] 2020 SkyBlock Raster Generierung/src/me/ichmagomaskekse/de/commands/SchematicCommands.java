package me.ichmagomaskekse.de.commands;

import java.io.File;

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
					if(args[0].equalsIgnoreCase("list") && SkyBlock.hasPermission(p, "skyblock.schematics.list")) {
						File fi = new File("plugins/SkyBlock/Schematics/Saved/");
						p.sendMessage("§7Alle verfügbaren Schematics:");
						if(fi.listFiles().length == 0) {
							p.sendMessage(" §c§oKeine Schematics vorhanden!");							
						}else {							
							for(File f : fi.listFiles()) {
								if(f.getAbsoluteFile().getName().endsWith(".yml")) {
									p.sendMessage(" §7- §6"+f.getAbsoluteFile().getName());
								}
							}
						}
					}if(args[0].equalsIgnoreCase("copy") && SkyBlock.hasPermission(p, "skyblock.schematics.copy")) {
						SchematicProfile profile = SchematicManager.getProfile(p);
						if(profile.readyToCopy()) {
							p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Die Region wird kopiert..");
							if(profile.getSchematic().copy(true)) {
								p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Die Region wurde kopiert");
							}else p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"§cDie Region konnte nicht kopiert werden");
						}else {
//							p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Ich habe die Copy Funktion erstmal deaktiviert. Sie bringt den Server zum Crashen hehe");
							p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Du musst §7§n2§e Location auswählen");
						}
					}else if(args[0].equalsIgnoreCase("save") && SkyBlock.hasPermission(p, "skyblock.schematics.save")) {
						p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"/s save <Dateiname>");
					}else if(args[0].equalsIgnoreCase("paste") && SkyBlock.hasPermission(p, "skyblock.schematics.paste")) {
						p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"/s paste <Dateifile>");
					}
					break;
				case 2:
					if(args[0].equalsIgnoreCase("copy") && SkyBlock.hasPermission(p, "skyblock.schematics.copy")) {
						if(args[1].equalsIgnoreCase("-a")) {							
							SchematicProfile profile = SchematicManager.getProfile(p);
							if(profile.readyToCopy()) {
								p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Die Region wird kopiert..");
								if(profile.getSchematic().copy(false)) {
									p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Die Region wurde kopiert");
								}else p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"§cDie Region konnte nicht kopiert werden");
							}else {
//								p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Ich habe die Copy Funktion erstmal deaktiviert. Sie bringt den Server zum Crashen hehe");
								p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Du musst §7§n2§e Location auswählen");
							}
						}
					}else if(args[0].equalsIgnoreCase("save") && SkyBlock.hasPermission(p, "skyblock.schematics.save")) {
						SchematicProfile profile = SchematicManager.getProfile(p);
						if(profile.readyToSave()) {
							profile.setSavedSchematicName(args[1]);
							p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"'§e"+args[1]+"§e' wird gespeichert...");
							if(profile.getSchematic().save(false)) {
								p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Schematic '§e"+args[1]+"§e'  wurde gespeichert");								
							}
						}else p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Eine Schematic kann aktuell nicht gespeichert werden");
					}else if(args[0].equalsIgnoreCase("paste") && SkyBlock.hasPermission(p, "skyblock.schematics.paste")) {
						if(SchematicManager.pasteSchematic(p, args[1], false)) {
						}else p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Die Schematic existiert nicht");
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
			p.sendMessage(" §7» §6/s list §fListe aller Schematics");
			p.sendMessage(" §7» §6/s save <Name> §fSpeichert ausgewählte Schematic");
			p.sendMessage(" §7» §6/s copy [-a] §fKopiert Bereich zur Schematic");
			p.sendMessage(" §7» §6/s paste <Name> §fPaste eine Schematic");
		}
	}
}
