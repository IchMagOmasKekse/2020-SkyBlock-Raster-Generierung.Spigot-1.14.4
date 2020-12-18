package me.dreamisland.de.profiles;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.dreamisland.de.Chat.MessageType;
import me.dreamisland.de.Cuboid;
import me.dreamisland.de.SkyBlock;
import me.dreamisland.de.SkyBlockGenerator;
import me.dreamisland.de.filemanagement.SkyFileManager;

public class IslandManager {
	
	public static HashMap<Player, IslandProfile> profiles = new HashMap<Player, IslandProfile>();
	
	public IslandManager() {
		for(Player p : Bukkit.getOnlinePlayers()) loadProfile(p);
		
		for(int i : SkyFileManager.getRemovers()) {
			clearIsland(i);
		}
	}
	
	/*
	 * TODO: Gibt zurück, ob ein Spieler sich auf einem Insel Grundstück befindet
	 */
	public static boolean isInIslandRegion(String uuid_target, Player p) {
		for(Player t : profiles.keySet()) {
			profiles.get(p).cuboid.isIn(t); //<-- Das kann Fehler verursachen. 'p' 't' und 'uuid_target' könnten vertauscht sein
		}
		return false;
	}
	
	/*
	 * TODO: Gibt das Insel Profil zurück
	 */
	public static IslandProfile getProfile(Player p) {
		if(profiles.containsKey(p)) return profiles.get(p);
		else {
			loadProfile(p);
			return profiles.get(p);
		}
	}
	
	/*
	 * TODO: Meldet alle Insel Profile ab
	 */
	public static boolean unloadAll() {
		profiles.clear();
		return true;
	}
	
	/*
	 * TODO: Meldet das Profil einer Insel ab
	 */
	public static boolean unloadProfile(Player p) {
		if(profiles.containsKey(p)) {
			profiles.remove(p);
			return true;
		}else return false;
	}
	
	/*
	 * TODO: Registriert das Profil einer Insel 
	 */
	public static boolean loadProfile(Player p) {
		if(profiles.containsKey(p)) return false;
		else {
			if(SkyFileManager.hasIsland(p)) {
				IslandProfile profile = new IslandProfile(SkyFileManager.getIslandID(p.getUniqueId().toString()), p.getUniqueId());
				profiles.put(p, profile);				
			}else if(SkyFileManager.isMemberOfAnIsland(p.getUniqueId())) {
				IslandProfile profile = new IslandProfile(SkyFileManager.getIslandWhereHeIsMemberOf(p.getUniqueId()), p.getUniqueId());
				profiles.put(p, profile);	
			}
			return true;
		}
	}
	
	/**
	 * Gibt den Cuboid einer Insel zurück
	 * @param island_id
	 * @return
	 */
	public static Cuboid getIslandCuboid(int island_id) {
		File index = new File("plugins/SkyBlock/Insel-Index-File.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(index);
		
		int x1 = (int)(cfg.getDouble("Islands.ID-"+island_id+".LocX")-(SkyBlockGenerator.issize/2));
		int z1 = (int)(cfg.getDouble("Islands.ID-"+island_id+".LocZ")-(SkyBlockGenerator.issize/2));
		int x2 = x1+(SkyBlockGenerator.issize);
		int z2 = z1+(SkyBlockGenerator.issize);
		Location pos1 = new Location(Bukkit.getWorld(cfg.getString("Islands.ID-"+island_id+".World")), x1, 0, z1);
		Location pos2 = new Location(Bukkit.getWorld(cfg.getString("Islands.ID-"+island_id+".World")), x2, 256, z2);
		return new Cuboid(pos1, pos2);
	}
	
	/*
	 * TODO: Beseitigt die Insel
	 */
	public static boolean clearIsland(final HashMap<Player, BukkitRunnable> runs, final Player p, final int island_id) {
		
		MessageType type = MessageType.WARNING;
		SkyBlock.sendConsoleMessage(type, "Die Insel mit der ID §f"+island_id+type.getSuffix()+" wurde vom Besitzer befreit.",
				"Diese Insel kann dennoch nicht neu geclaimt werden, weil sie noch von einem Admin gereinigt und anschließend mit §f/claimable "+island_id+type.getSuffix()+" freigegeben werden muss.");
		
//		IslandPaster.removeLiquidAndTileEntities(getProfile(p).cuboid);
//		SkyFileManager.saveIslandRemover(island_id);
		
//		runs.put(p,  new BukkitRunnable() {			
//			ArrayList<Block> blocks = getProfile(p).cuboid.blockList();
//			int a = 0;
//			int max = getProfile(p).cuboid.getTotalBlockSize();
//			@Override
//			public void run() {
//				for(int i = 0; i != 1; i++) {					
//					if(blocks.size() > a) {
//						final Block b = blocks.get(a);
//						if(b.getType() != Material.AIR && b.getType() != Material.CAVE_AIR) {
//							b.setType(Material.AIR);
//						}
//						a++;
//						if(a == max) {
//							SkyFileManager.markIslandAsClaimable(island_id);
//							SkyFileManager.removeIslandRemover(island_id);
//							SkyBlock.sendDeveloperMessage("Die Insel "+island_id+" ist nun wieder claimable");
//							runs.get(p).cancel();
//							runs.remove(p);
//						}
//					}else {
//						SkyFileManager.markIslandAsClaimable(island_id);
//						SkyFileManager.removeIslandRemover(island_id);
//						SkyBlock.sendDeveloperMessage("Die Insel "+island_id+" ist nun wieder claimable");
//						runs.get(p).cancel();
//						runs.remove(p);
//					}
//				}
//			}
//		});
//		runs.get(p).runTaskTimer(SkyBlock.getInstance(), 0l, 0l);
		return true;
	}
	public static boolean clearIsland(final int island_id) {
		MessageType type = MessageType.WARNING;
		SkyBlock.sendConsoleMessage(type, "Die Insel mit der ID §f"+island_id+type.getSuffix()+" wurde vom Besitzer befreit.",
				"Diese Insel kann dennoch nicht neu geclaimt werden, weil sie noch von einem Admin gereinigt und anschließend mit §f/claimable "+island_id+type.getSuffix()+" freigegeben werden muss.");
		
//		IslandPaster.removeLiquidAndTileEntities(getIslandCuboid(island_id));
		
//		final BukkitRunnable runner = new BukkitRunnable() {
//			ArrayList<Block> blocks = getIslandCuboid(island_id).blockList();
//			int a = 0;
//			int max = getIslandCuboid(island_id).getTotalBlockSize();
//			@Override
//			public void run() {
//				for(int i = 0; i != 1; i++) {					
//					if(blocks.size() > a) {
//						final Block b = blocks.get(a);
//						if(b.getType() != Material.AIR && b.getType() != Material.CAVE_AIR) {
//							b.setType(Material.AIR);
//						}
//						a++;
//						if(a == max) {
//							SkyFileManager.markIslandAsClaimable(island_id);
//							SkyFileManager.removeIslandRemover(island_id);
//							SkyBlock.sendDeveloperMessage(MessageType.INFO, "Die Insel Nummer "+island_id+" ist nun wieder claimable");
//							cancel();
//						}
//					}else {
//						SkyFileManager.markIslandAsClaimable(island_id);
//						SkyFileManager.removeIslandRemover(island_id);
//						SkyBlock.sendDeveloperMessage(MessageType.INFO, "Die Insel Nummer "+island_id+" ist nun wieder claimable");
//						cancel();
//					}
//				}
//			}
//		};
//		runner.runTaskTimer(SkyBlock.getInstance(), 0l, 0l);
		return true;
	}
	
	public static class IslandProfile {
		
		private int id = 0;
		private UUID uuid = null;
		private Location spawnpoint = null;
		private Cuboid cuboid = null;
		private ArrayList<String> members = new ArrayList<String>();
		
		public IslandProfile(int island_id, UUID owner) {
			this.id = island_id;
			this.uuid = owner;
			
			if(SkyFileManager.hasIsland(owner.toString())) {
				spawnpoint = SkyFileManager.getIslandPlayerSpawn(uuid.toString());
				
				int x1 = SkyFileManager.getLocationX(owner.toString())-(SkyBlockGenerator.issize/2);
				int z1 = SkyFileManager.getLocationZ(owner.toString())-(SkyBlockGenerator.issize/2);
				int x2 = x1+(SkyBlockGenerator.issize);
				int z2 = z1+(SkyBlockGenerator.issize);
				if(SkyFileManager.getWorldName(owner.toString()) == null) Bukkit.broadcastMessage("getWorldName == null");
				if(Bukkit.getWorld(SkyFileManager.getWorldName(owner.toString())) == null) Bukkit.broadcastMessage("getWorld == null");
				Location pos1 = new Location(Bukkit.getWorld(SkyFileManager.getWorldName(owner.toString())), x1, 0, z1);
				Location pos2 = new Location(Bukkit.getWorld(SkyFileManager.getWorldName(owner.toString())), x2, 256, z2);
				this.cuboid = new Cuboid(pos1, pos2);	
				members = getMembers();
			}
		}
		
		/**
		 * Lädt Einstellungen und Daten einer Insel neu
		 * @param
		 */
		public void reload() {
			if(SkyFileManager.hasIsland(uuid.toString())) {
				spawnpoint = SkyFileManager.getIslandPlayerSpawn(uuid.toString());
				
				int x1 = SkyFileManager.getLocationX(uuid.toString())-(SkyBlockGenerator.issize/2);
				int z1 = SkyFileManager.getLocationZ(uuid.toString())-(SkyBlockGenerator.issize/2);
				int x2 = x1+(SkyBlockGenerator.issize);
				int z2 = z1+(SkyBlockGenerator.issize);
				if(SkyFileManager.getWorldName(uuid.toString()) == null) Bukkit.broadcastMessage("getWorldName == null");
				if(Bukkit.getWorld(SkyFileManager.getWorldName(uuid.toString())) == null) Bukkit.broadcastMessage("getWorld == null");
				Location pos1 = new Location(Bukkit.getWorld(SkyFileManager.getWorldName(uuid.toString())), x1, 0, z1);
				Location pos2 = new Location(Bukkit.getWorld(SkyFileManager.getWorldName(uuid.toString())), x2, 256, z2);
				this.cuboid = new Cuboid(pos1, pos2);	
				members = getMembers();
			}
		}
		
		/*
		 * TODO: Gibt den Spawnpunkt einer Insel zurück
		 */
		public Location getSpawnpoint() {
			return spawnpoint;
		}
		
		/*
		 * TODO: Gibt die ID von der Insel zurück
		 */
		public int getIslandID() {
			return id;
		}
		
		/*
		 * TODO: Gibt die UUID des Owners zurück
		 */
		public UUID getOwnerUUID() {
			return uuid;
		}
		
		/*
		 * TODO: Gibt zurück, ob der Spieler p sich auf dem Grundstück der Insel befindet
		 */
		public boolean isInIslandregion(Player p) {
			return cuboid.isIn(p);
		}
		
		/**
		 * Gibt eine ArrayList<UUID> mit den Insel-Members zurück.
		 * @return
		 */
		public ArrayList<String> getMembers() {
			return SkyFileManager.getMembers(uuid.toString());
		}
		
		/**
		 * Fügt einen neuen Member hinzu.
		 * @param uuid
		 * @return
		 */
		public boolean addMember(Player new_member) {
			if(members.contains(new_member.getUniqueId().toString())) return false;
			if(SkyFileManager.addMember(Bukkit.getPlayer(uuid), new_member)) {
				members.add(new_member.getUniqueId().toString());
				return true;
			}else return false;
		}
		
		/**
		 * Entfernt ein Member von der Insel. Er hat nun keine Rechte mehr.
		 * @param member
		 * @return
		 */
		public boolean removeMember(Player member) {
			if(members.contains(member.getUniqueId().toString()) == false) return false;
			if(SkyFileManager.removeMember(uuid.toString(), member.getUniqueId().toString())) {
				members.remove(member.getUniqueId().toString());
				return true;
			}else return false;
		}
	}
	
}
