package me.dreamisland.de.profiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.dreamisland.de.Cuboid;
import me.dreamisland.de.SkyBlock;
import me.dreamisland.de.SkyBlockGenerator;
import me.dreamisland.de.filemanagement.SkyFileManager;

public class IslandManager {
	
	public static HashMap<Player, IslandProfile> profiles = new HashMap<Player, IslandProfile>();
	
	public IslandManager() {
		for(Player p : Bukkit.getOnlinePlayers()) loadProfile(p);
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
			}
			return true;
		}
	}
	
	/*
	 * TODO: Beseitigt die Insel
	 */
	public static boolean clearIsland(final HashMap<Player, BukkitRunnable> runs, final Player p) {
		
		runs.put(p,  new BukkitRunnable() {
			ArrayList<Block> blocks = getProfile(p).cuboid.blockList();
			int a = 0;
			int max = getProfile(p).cuboid.getTotalBlockSize();
			@Override
			public void run() {
				if(blocks.size() > a) {					
					final Block b = blocks.get(a);
					if(b.getType() != Material.AIR && b.getType() != Material.CAVE_AIR) {
						b.setType(Material.AIR);
					}
					a++;
					if(a == max) {
						runs.get(p).cancel();
						runs.remove(p);
					}
				}else {
					runs.get(p).cancel();
					runs.remove(p);
				}
				
			}
		});
		runs.get(p).runTaskTimer(SkyBlock.getInstance(),0l, 0l);
		return true;
	}
	
	public static class IslandProfile {
		
		private int id = 0;
		private UUID uuid = null;
		private Location spawnpoint = null;
		private Cuboid cuboid = null;
		
		public IslandProfile(int island_id, UUID owner) {
			this.id = island_id;
			this.uuid = owner;
			
			if(SkyFileManager.hasIsland(owner)) {
				spawnpoint = SkyFileManager.getSpawn(uuid.toString());
				
				int x1 = SkyFileManager.getLocationX(owner.toString())-(SkyBlockGenerator.issize/2);
				int z1 = SkyFileManager.getLocationZ(owner.toString())-(SkyBlockGenerator.issize/2);
				int x2 = x1+(SkyBlockGenerator.issize);
				int z2 = z1+(SkyBlockGenerator.issize);
				if(SkyFileManager.getWorldName(owner.toString()) == null) Bukkit.broadcastMessage("getWorldName == null");
				if(Bukkit.getWorld(SkyFileManager.getWorldName(owner.toString())) == null) Bukkit.broadcastMessage("getWorld == null");
				Location pos1 = new Location(Bukkit.getWorld(SkyFileManager.getWorldName(owner.toString())), x1, 0, z1);
				Location pos2 = new Location(Bukkit.getWorld(SkyFileManager.getWorldName(owner.toString())), x2, 256, z2);
				this.cuboid = new Cuboid(pos1, pos2);				
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
	}
	
}
