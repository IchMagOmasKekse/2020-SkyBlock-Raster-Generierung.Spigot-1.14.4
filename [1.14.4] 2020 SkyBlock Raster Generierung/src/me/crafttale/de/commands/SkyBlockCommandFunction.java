package me.crafttale.de.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.crafttale.de.Chat;
import me.crafttale.de.Prefixes;
import me.crafttale.de.Settings;
import me.crafttale.de.SkyBlock;
import me.crafttale.de.SkyBlockGenerator;
import me.crafttale.de.Chat.MessageType;
import me.crafttale.de.filemanagement.SkyFileManager;
import me.crafttale.de.filemanagement.SkyFileManager.IslandStatus;
import me.crafttale.de.profiles.IslandManager;
import me.crafttale.de.profiles.PlayerProfiler;
import me.crafttale.de.requests.Request;
import me.crafttale.de.requests.VisitRequest;
import me.crafttale.de.requests.Request.RequestManager;
import me.crafttale.de.schematics.SchematicManager;

public class SkyBlockCommandFunction {
	
	public static ArrayList<Player> isCreating = new ArrayList<Player>();
	public static String island_schematic_name = "island";
	
	/**
	 * Kickt einen Spieler von deiner Insel. Dieser wird zum Spawn teleportiert.
	 * @param kicker
	 * @param target_name
	 * @return
	 */
	public static boolean kickPlayer(Player kicker, String target_name) {
		if(Bukkit.getPlayer(target_name) != null) {	
			Player target = Bukkit.getPlayer(target_name);
			if(PlayerProfiler.getUUID(target).toString().equals(PlayerProfiler.getUUID(kicker).toString())) {
				kicker.sendMessage(Prefixes.SERVER.px()+"Du kannst dich nicht selber rausschmeißen!");
				return false;
			}else {
				if(target.hasPermission("skyblock.admin")) {
					kicker.sendMessage(Prefixes.SERVER.px()+"Du kannst keinen Server-Personal von deiner Insel werfen!");
					return false;
				}else {					
					if(IslandManager.getProfile(kicker).isInIslandregion(target)) {
						target.teleport(SkyBlock.spawn);
						target.sendMessage(Prefixes.SERVER.px()+"§e"+kicker.getName()+" §7hat dich von seiner Insel geworfen!");
						kicker.sendMessage(Prefixes.SERVER.px()+"Du hast §e"+target.getName()+" §7deiner Insel geworfen!");
						return true;
					}else {
						kicker.sendMessage(Prefixes.SERVER.px()+"§e"+target_name+" §7ist nicht auf deiner Insel");				
						return false;
					}
				}
			}
		}else {
			kicker.sendMessage(Prefixes.SERVER.px()+"Dieser Spieler ist nicht online");
			return false;
		}
	}
	
	/**
	 * Nehme eine anfrage an.
	 * @param p
	 * @return
	 */
	public static boolean denyRequest(Player p) {
		if(RequestManager.hasReceivedRequest(p)) {
			Request r = RequestManager.getReceivedRequest(p);
			r.deny();
		}else {
			p.sendMessage(Prefixes.REQUEST.px()+"§cDu hast keine Anfrage erhalten");
			return false;
		}
		return true;
	}
	
	
	/**
	 * Nehme eine anfrage an.
	 * @param p
	 * @return
	 */
	public static boolean acceptRequest(Player p) {
		if(RequestManager.hasReceivedRequest(p)) {
			Request r = RequestManager.getReceivedRequest(p);
			r.accept();
		}else {
			p.sendMessage(Prefixes.REQUEST.px()+"§cDu hast keine Anfrage erhalten");
			return false;
		}
		return true;
	}
	

	/**
	 * Sendet eine Anfrage zum Besuchen einer Insel.
	 * @param p
	 * @param args
	 * @return
	 */
	public static boolean requestVisit(Player p, String[] args) {
		if(args[1].equalsIgnoreCase(p.getName())) {
			p.sendMessage(Prefixes.REQUEST.px()+"Nutze §7/is §bwenn du deine Insel besuchen möchtest :)");
			return false;
		}
		if(RequestManager.hasSendRequest(p)) {
			p.sendMessage(Prefixes.REQUEST.px()+"Du kannst nur §7eine §bAnfrage gleichzeit schicken");
			return false;
		}
		Player t = Bukkit.getPlayer(args[1]);
		if(t == null) { 
			p.sendMessage(Prefixes.REQUEST.px()+"Dieser Spieler war noch nie auf diesem Server oder ist offline");
			return false;
		}else {
			RequestManager.registerRequest(new VisitRequest(p, t));
		}
		return false;
	}
	

	/**
	 * Teleportiert den Besucher zum Gastgeber.
	 * @param visitor
	 * @param target
	 * @return
	 */
	public static boolean visitIsland(Player visitor, Player target) {
		if(SkyFileManager.hasIsland(target) == false) {
			visitor.sendMessage(Prefixes.REQUEST.px()+"Dieser Spieler hat keine Insel");
			return false;
		}else {
			visitor.teleport(SkyFileManager.getPlayerDefinedIslandSpawn(target));
			return true;
		}
	}
	

	/**
	 * Löscht die Insel eines Spielers.
	 * @param p
	 * @return
	 */
	public static boolean deleteIsland(Player p) {
		if(isCreating.contains(p)) {
			p.sendMessage(Prefixes.SERVER.px()+"Du kannst deine Insel noch nicht löschen");
			return false;
		}else {
//			p.performCommand("spawn");
			if(SkyFileManager.hasIsland(p)) return SkyFileManager.deleteIsland(p);
			else {
				Chat.sendHoverableMessage(p, "§cDu besitzt keine Insel", "Du kannst keine Insel löschen, wenn du keine hast.\nWenn du Member auf einer anderen Insel bist,\nzählt diese dennoch nicht zu deinem Besitz.", false, true);
				return false;
			}
		}
	}
	

	/**
	 * Ist eine Abkürzung, um die Welt mit Multiverse zu Löschen.
	 * @param p
	 * @return
	 */
	public static boolean deleteWorld(Player p) {
		p.sendMessage("Lösche skyblockworld");
		p.performCommand("mv delete skyblockworld");
		p.performCommand("mv confirm");
		return true;
	}
	
	/**
	 * Teleportiert den Spieler zu seiner Insel oder erstellt ihm eine Insel, wenn er keine besitzt.
	 * @param p
	 * @return
	 */
	public static boolean teleportToIsland(Player p) {
		if(SkyBlock.hasPermission(p, "skyblock.island")) {//Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)	
			if(SkyFileManager.isMemberOfAnIsland(PlayerProfiler.getUUID(p))) {
				IslandManager.loadProfile(p);//TODO: Muss Membering-Insel laden
				if(p.getGameMode() == GameMode.ADVENTURE) p.setGameMode(GameMode.SURVIVAL);
				
				Location loc = null;
				if(SkyFileManager.getPlayerDefinedIslandSpawn(p) != null) loc = SkyFileManager.getPlayerDefinedIslandSpawn(p);
				else loc = SkyFileManager.getLocationOfIsland(p);
				
				p.teleport(loc);
			}else {				
				if(SkyFileManager.hasIsland(p)) {
					IslandManager.loadProfile(p);
					Location loc = null;
					if(SkyFileManager.getPlayerDefinedIslandSpawn(p) != null) loc = SkyFileManager.getPlayerDefinedIslandSpawn(p);
					else loc = SkyFileManager.getLocationOfIsland(p);
					
					if(p.getGameMode() == GameMode.ADVENTURE) p.setGameMode(GameMode.SURVIVAL);
					p.teleport(loc);
					if(p.isOp() == false && p.hasPermission("skyblock.island.gamemode.bypass") == false) p.setGameMode(GameMode.SURVIVAL); 
					SkyBlock.spawnFireworks(p.getLocation(), 1, true, false, Type.BURST, Color.LIME);
				}else p.performCommand("is create");
			}
		}
		return true;
	}
	
	/**
	 * Erstellt dem Spieler eine Insel oder teleportiert ihn zu ihr, wenn bereit eine hat.
	 * @param p
	 * @return
	 */
	public static boolean createIsland(Player p) {
		if(isCreating.contains(p)) {
			p.sendMessage(Prefixes.SERVER.px()+"Du besitzt bereits eine Insel");
			return false;
		}else {
			isCreating.add(p);
			Location is_loc = null;
			if(SkyFileManager.hasIsland(p)) {
				is_loc = new Location(SkyFileManager.getWorld(PlayerProfiler.getUUID(p).toString()),
						SkyFileManager.getLocationX(PlayerProfiler.getUUID(p).toString()),
						SkyBlockGenerator.islandHeight,
						SkyFileManager.getLocationZ(PlayerProfiler.getUUID(p).toString()));
				p.sendMessage(Prefixes.SERVER.px()+"Du besitzt bereits eine Insel");
				p.sendMessage(Prefixes.SERVER.px()+"Du wurdest zu deiner Insel teleportiert");
			}else {
				int id = SkyFileManager.getUnclaimedIslandID(false);
				SkyFileManager.claimIsland(id, p, p);
				p.sendMessage(Prefixes.SERVER.px()+"§aDeine Insel wurde erfolgreich erstellt");
				p.sendMessage(Prefixes.SERVER.px()+"Du hast die Insel Nummer §b"+id+"§7!");
				
				is_loc = new Location(SkyFileManager.getWorld(PlayerProfiler.getUUID(p).toString()),
						SkyFileManager.getLocationX(PlayerProfiler.getUUID(p).toString()),
						SkyBlockGenerator.islandHeight,
						SkyFileManager.getLocationZ(PlayerProfiler.getUUID(p).toString()));
				
				p.sendMessage(Prefixes.SERVER.px()+"§aInsel wird generiert...");
				
				/* PlayerSpawn korrekt setzen */
				Location spawn = is_loc.clone();
				spawn.setY(SkyBlockGenerator.islandHeight+1);
				SkyFileManager.setPlayerDefinedIslandSpawn(p, spawn);
				/* -------------------------- */
				
				SchematicManager.pasteSchematicAt(is_loc, p, island_schematic_name, true);
				
			}
			IslandManager.loadProfile(p);
			
			if(is_loc.getBlock().getType().isSolid() == false && is_loc.clone().add(0,-1,0).getBlock().getType().isSolid() == false) is_loc.getBlock().setType(Material.STONE);
//			p.setGameMode(GameMode.SPECTATOR);
//			p.teleport(is_loc);
		}
		return true;
	}
	
	/**
	 * Sendet dem Spieler alle Insel IDs zu, die verfügbar sind.
	 * Inoffiziell verfügbar bedeutet, dass eine Insel keinen Besitzer mehr hat, aber die Blöcke noch bereinigt werden müssen.
	 * @param p
	 */
	public static void sendAllClaimedIsland(Player p, int page) {
		ArrayList<IslandStatus> statuses = SkyFileManager.getIslandsWithStatus();
		int page_amount = page*Settings.getIslandsPerPage();
		boolean noOne = true;
		int amount = 0;
		int amount_total = 0;
		SkyBlock.sendMessage(MessageType.INFO, p, "Vergebene Inseln, Seite "+page+":");
		for(int i = page_amount; i != page_amount+Settings.getIslandsPerPage(); i++) {
			if(i >= statuses.size()) break;
			
			if(statuses.get(i).isClaimed()) {
				Chat.sendClickableMessage(p, "- §c"+statuses.get(i).getId()+(statuses.get(i).needRelease() == true ? " (§fBraucht Bereinigung§c)" : "")+"", "Braucht Bereinigung: "+(statuses.get(i).needRelease() == true ? "§cJa" : "§fNein")+"\n§aBesitzer: §f"+(statuses.get(i).getOwnerUUID().equals("none") ? "Keinen" : statuses.get(i).getOwnerUUID())+"\n§aKlicke um dich zur Insel §f"+statuses.get(i).getId()+"§a zu teleportieren", "/is tp "+statuses.get(i).getId(), false, false);
				amount++;
				noOne = false;
			}else page_amount+=1;
		}
		
		for(IslandStatus st : statuses) if(st.isClaimed())amount_total++;
		
		if(noOne) {
			Chat.sendHoverableMessage(p, "Auf dieser Seitenzahl gibt es keine vergebenen Inseln.", "Keine vergebene Insel vorhanden", false, true);
		}else {
			SkyBlock.sendMessage(MessageType.INFO, p, amount+" Insel*n vergeben auf dieser Seite(Alle Seiten zsm. beinhalten "+amount_total+")");
			Chat.sendClickableMessage(p, "§aNächste Seite", "Klicke um die Nächste Seite zu öffnen", "/claimed "+(page+1), false, true);
		}
	}
	
	/**
	 * Sendet dem Spieler alle Insel IDs zu, die nicht verfügbar sind.
	 * @param p
	 */
	public static void sendAllUnclaimedIsland(Player p, int page) {
		ArrayList<IslandStatus> statuses = SkyFileManager.getIslandsWithStatus();
		int page_amount = page*Settings.getIslandsPerPage();
		boolean noOne = true;
		int amount = 0;
		int amount_total = 0;
		SkyBlock.sendMessage(MessageType.INFO, p, "Verfügbare Inseln, Seite "+page+":");
		for(int i = page_amount; i != page_amount+Settings.getIslandsPerPage(); i++) {
			if(i >= statuses.size()) break;
			
			if(statuses.get(i).isClaimed() == false) {
				Chat.sendClickableMessage(p, "- §3"+statuses.get(i).getId(), "Klicke um dich zur Insel §f"+statuses.get(i).getId()+"§a zu teleportieren", "/is tp "+statuses.get(i).getId(), false, false);
				amount++;
				noOne = false;
			}else page_amount+=1;
		}
		
		for(IslandStatus st : statuses) if(st.isClaimed() == false)amount_total++;
		
		if(noOne) {
			Chat.sendHoverableMessage(p, "Auf dieser Seitenzahl gibt es keine verfügbaren Inseln.", "Keine verfügbare Insel vorhanden", false, true);
		}else {
			SkyBlock.sendMessage(MessageType.INFO, p, amount+" Insel*n verfügbar auf dieser Seite(Alle Seiten zsm. beinhalten "+amount_total+")");
			Chat.sendClickableMessage(p, "§aNächste Seite", "Klicke um die Nächste Seite zu öffnen", "/unclaimed "+(page+1), false, true);
		}
	}
	
}
