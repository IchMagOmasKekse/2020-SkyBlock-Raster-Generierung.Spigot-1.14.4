package me.ichmagomaskekse.de.commands;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.ichmagomaskekse.de.Prefixes;
import me.ichmagomaskekse.de.SkyBlock;
import me.ichmagomaskekse.de.SkyBlockGenerator;
import me.ichmagomaskekse.de.filemanagement.SkyFileManager;
import me.ichmagomaskekse.de.profiles.IslandManager;
import me.ichmagomaskekse.de.requests.Request;
import me.ichmagomaskekse.de.requests.Request.RequestManager;
import me.ichmagomaskekse.de.requests.VisitRequest;

public class CommandFunction {
	
	/*
	 * TODO: Nehme eine anfrage an
	 */
	public static boolean denyRequest(Player p) {
		if(RequestManager.hasReceivedRequest(p)) {
			Request r = RequestManager.getReceivedRequest(p);
			r.deny();
		}else {
			p.sendMessage(Prefixes.REQUEST.getPrefix()+"§cDu hast keine Anfrage erhalten");
			return false;
		}
		return true;
	}
	
	
	/*
	 * TODO: Nehme eine anfrage an
	 */
	public static boolean acceptRequest(Player p) {
		if(RequestManager.hasReceivedRequest(p)) {
			Request r = RequestManager.getReceivedRequest(p);
			r.accept();
		}else {
			p.sendMessage(Prefixes.REQUEST.getPrefix()+"§cDu hast keine Anfrage erhalten");
			return false;
		}
		return true;
	}
	
	/*
	 * TODO: Sendet eine Anfrage zum Besuchen einer Insel
	 */
	public static boolean requestVisit(Player p, String[] args) {
		if(args[1].equalsIgnoreCase(p.getName())) {
			p.sendMessage(Prefixes.REQUEST.getPrefix()+"Nutze §7/is §bwenn du deine Insel besuchen möchtest :)");
			return false;
		}
		if(RequestManager.hasSendRequest(p)) {
			p.sendMessage(Prefixes.REQUEST.getPrefix()+"Du kannst nur §7eine §bAnfrage gleichzeit schicken");
			return false;
		}
		Player t = Bukkit.getPlayer(args[1]);
		if(t == null) {
			p.sendMessage(Prefixes.REQUEST.getPrefix()+"Dieser Spieler war noch nie auf diesem Server oder ist offline");
			return false;
		}else {
			RequestManager.registerRequest(new VisitRequest(p, t));
		}
		return false;
	}
	
	/*
	 * TODO: Teleportiert den Besucher zum Gastgeber
	 */
	public static boolean visitIsland(Player visitor, Player target) {
		if(SkyFileManager.hasIsland(target) == false) {
			visitor.sendMessage(Prefixes.REQUEST.getPrefix()+"Dieser Spieler hat keine Insel");
			return false;
		}else {
			visitor.teleport(SkyFileManager.getLocationOfIsland(target).add(0,0.5,0));
			return true;
		}
	}
	
	/*
	 * TODO: Löscht die Insel eines Spielers
	 */
	public static boolean deleteIsland(Player p) {
		if(SkyFileManager.hasIsland(p)) return SkyFileManager.deleteIsland(p);
		else {
			p.sendMessage(Prefixes.SERVER.getPrefix()+"§cDu besitzt keine Insel");
			return false;
		}
	}
	
	/*
	 * TODO: Ist eine Abkürzung, um die Welt mit Multiverse zu löschen
	 */
	public static boolean deleteWorld(Player p) {
		p.sendMessage("Lösche skyblockworld");
		p.performCommand("mv delete skyblockworld");
		p.performCommand("mv confirm");
		return true;
	}
	
	/*
	 * TODO: Teleportiert den Spieler zu seiner Insel oder erstellt ihm eine Insel, wenn er keine besitzt
	 */
	public static boolean teleportToIsland(Player p) {
		if(SkyBlockCommands.hasPermission(p, "skyblock.island.tp")) {//Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
			if(SkyFileManager.hasIsland(p)) {
				IslandManager.loadProfile(p);
				p.teleport(SkyFileManager.getLocationOfIsland(p).add(0.5,0.5,0.5));
				SkyBlock.spawnFireworks(p.getLocation(), 1, true, false, Type.BURST, Color.LIME);
			}else p.performCommand("is create");
		}
		return true;
	}
	
	/*
	 * TODO: Erstellt dem Spieler eine Insel oder teleportiert ihn zu ihr, wenn bereit eine hat
	 */
	public static boolean createIsland(Player p) {
		Location is_loc = null;
		if(SkyFileManager.hasIsland(p)) {
			is_loc = new Location(SkyFileManager.getWorld(p.getUniqueId().toString()),
					SkyFileManager.getLocationX(p.getUniqueId().toString()),
					SkyBlockGenerator.islandHeight,
					SkyFileManager.getLocationZ(p.getUniqueId().toString()));
			p.sendMessage(Prefixes.SERVER.getPrefix()+"Du besitzt bereits eine Insel");
			p.sendMessage(Prefixes.SERVER.getPrefix()+"Du wurdest zu deiner Insel teleportiert");
		}else {							
			int id = SkyFileManager.getUnclaimedIslandID(false);
			SkyFileManager.claimIsland(id, p);
			p.sendMessage(Prefixes.SERVER.getPrefix()+"§aDeine Insel wurde erfolgreich erstellt");
			p.sendMessage(Prefixes.SERVER.getPrefix()+"Du hast die Insel Nummer §b"+id+"§7!");
			is_loc = new Location(SkyFileManager.getWorld(p.getUniqueId().toString()),
					SkyFileManager.getLocationX(p.getUniqueId().toString()),
					SkyBlockGenerator.islandHeight,
					SkyFileManager.getLocationZ(p.getUniqueId().toString()));
		}
		IslandManager.loadProfile(p);
		if(is_loc.getBlock().getType().isSolid() == false && is_loc.clone().add(0,-1,0).getBlock().getType().isSolid() == false) is_loc.getBlock().setType(Material.STONE);
		p.teleport(is_loc);
		return true;
	}
	
}
