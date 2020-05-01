package me.dreamisland.de.commands;

import java.io.File;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.dreamisland.de.Prefixes;
import me.dreamisland.de.SkyBlock;
import me.dreamisland.de.schematics.SchematicManager;
import me.dreamisland.de.schematics.SkyRect;
import me.dreamisland.de.schematics.SchematicManager.SchematicProfile;

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
					if(args[0].equalsIgnoreCase("1")) {
						SchematicProfile profile = SchematicManager.getProfile(p);
						SkyRect sky = new SkyRect(profile.getPos1().clone(), profile.getPos2().clone());
						sky.getCorner1().getBlock().setType(Material.OBSIDIAN);	
						
					}else if(args[0].equalsIgnoreCase("2")) {
						SchematicProfile profile = SchematicManager.getProfile(p);
						SkyRect sky = new SkyRect(profile.getPos1().clone(), profile.getPos2().clone());						
						sky.getCorner2().getBlock().setType(Material.OBSIDIAN);	
						
					}else if(args[0].equalsIgnoreCase("3")) {
						SchematicProfile profile = SchematicManager.getProfile(p);
						SkyRect sky = new SkyRect(profile.getPos1().clone(), profile.getPos2().clone());												
						sky.getCorner3().getBlock().setType(Material.OBSIDIAN);	
						
					}else if(args[0].equalsIgnoreCase("4")) {
						SchematicProfile profile = SchematicManager.getProfile(p);
						SkyRect sky = new SkyRect(profile.getPos1().clone(), profile.getPos2().clone());												
						sky.getCorner4().getBlock().setType(Material.OBSIDIAN);	
						
					}else if(args[0].equalsIgnoreCase("5")) {
						SchematicProfile profile = SchematicManager.getProfile(p);
						SkyRect sky = new SkyRect(profile.getPos1().clone(), profile.getPos2().clone());												
						sky.getCorner5().getBlock().setType(Material.OBSIDIAN);	
						
					}else if(args[0].equalsIgnoreCase("6")) {
						SchematicProfile profile = SchematicManager.getProfile(p);
						SkyRect sky = new SkyRect(profile.getPos1().clone(), profile.getPos2().clone());												
						sky.getCorner6().getBlock().setType(Material.OBSIDIAN);
						
					}else if(args[0].equalsIgnoreCase("7")) {
						SchematicProfile profile = SchematicManager.getProfile(p);
						SkyRect sky = new SkyRect(profile.getPos1().clone(), profile.getPos2().clone());												
						sky.getCorner7().getBlock().setType(Material.OBSIDIAN);	
						
					}else if(args[0].equalsIgnoreCase("8")) {
						SchematicProfile profile = SchematicManager.getProfile(p);
						SkyRect sky = new SkyRect(profile.getPos1().clone(), profile.getPos2().clone());												
						sky.getCorner8().getBlock().setType(Material.OBSIDIAN);
						
					}else if(args[0].equalsIgnoreCase("list") && SkyBlock.hasPermission(p, "skyblock.schematics.list")) {
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
						p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"/s save <Dateiname> [-o]");
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
								p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Du musst §7§n2§e Locations auswählen");
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
							p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Schematic wird platziert...");
						}else p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Die Schematic existiert nicht");
					}
					break;
				case 3:
					if(args[0].equalsIgnoreCase("save") && SkyBlock.hasPermission(p, "skyblock.schematics.save")) {
						if(args[2].equalsIgnoreCase("-o")) {							
							SchematicProfile profile = SchematicManager.getProfile(p);
							if(profile.readyToSave()) {
								profile.setSavedSchematicName(args[1]);
								p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"'§e"+args[1]+"§e' wird gespeichert...");
								if(profile.getSchematic().save(true)) {
									p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Schematic '§e"+args[1]+"§e'  wurde gespeichert");								
								}
							}else p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Eine Schematic kann aktuell nicht gespeichert werden");
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
			p.sendMessage(" §7» §6/s list §fListe aller Schematics");
			p.sendMessage(" §7» §6/s save <Name> §fSpeichert ausgewählte Schematic");
			p.sendMessage(" §7» §6/s copy [-a] §fKopiert Bereich zur Schematic");
			p.sendMessage(" §7» §6/s paste <Name> §fPaste eine Schematic");
		}
	}
}
