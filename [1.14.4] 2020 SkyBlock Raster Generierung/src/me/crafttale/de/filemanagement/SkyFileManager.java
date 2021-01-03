package me.crafttale.de.filemanagement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.crafttale.de.Chat;
import me.crafttale.de.Cuboid;
import me.crafttale.de.PlayerAtlas;
import me.crafttale.de.Prefixes;
import me.crafttale.de.Settings;
import me.crafttale.de.SkyBlock;
import me.crafttale.de.SkyBlockGenerator;
import me.crafttale.de.profiles.IslandManager;
import me.crafttale.de.profiles.IslandManager.IslandData;
import me.crafttale.de.profiles.PlayerProfiler;

public class SkyFileManager {
	
	private static Random ran = new Random();
//	private static File island_index_File = new File("plugins/SkyBlock/Insel-Index-File.yml");
	private static ArrayList<String> emptylist = new ArrayList<String>();

	public SkyFileManager(SkyBlock sb) {
		SkyBlock.getSB().saveResource("Insel-Index-File.yml", false);
		SkyBlock.getSB().saveResource("config.yml", false);
	}

	
	public static void createIslandFile(UUID uuid, UUID owner) {
		
	}
	
	/**
	 * Gibt die ID einer Insel zurück
	 * @param uuid
	 * @return
	 */
	public static int getIslandID(String uuid) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(getIslandFile(uuid));
		return cfg.getInt("Islands.ID");
	}
	
	/**
	 * Gibt den Spawnpunkt einer Insel zurück
	 * @param uuid
	 * @return
	 */
	public static Location getIslandPlayerSpawn(String uuid) {
		if(getIslandFile(uuid).exists()) {			
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(getIslandFile(uuid));
			Location spawn = new Location(
					Bukkit.getWorld(cfg.getString("Islands.Spawnpoint.World")),
					cfg.getInt("Islands.Spawnpoint.LocX"),
					SkyBlockGenerator.islandHeight+1,
					cfg.getInt("Islands.Spawnpoint.LocZ"),
					cfg.getInt("Islands.Spawnpoint.Yaw"),
					cfg.getInt("Islands.Spawnpoint.Pitch"));
			return spawn;
		}else return null;
	}
	
	/**
	 * Gibt alle Insel in einer HashMap<Integer, Boolean> zurück.
	 * Integer = ID
	 * Boolean = isClaimed()
	 * @return
	 */
	public static ArrayList<IslandStatus> getIslandsWithStatus() {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(new File("plugins/SkyBlock/Insel-Index-File.yml"));
		int amount = cfg.getInt("Islands.Amount Generated");
		
		ArrayList<IslandStatus> ids = new ArrayList<IslandStatus>();
		
		for(int id = 0; id != amount; id++) {
			IslandStatus status = new IslandStatus(id, cfg.getBoolean("Islands.ID-"+id+".Claimed"),
					((cfg.getString("Islands.ID-"+id+".Owner UUID") == null || cfg.getString("Islands.ID-"+id+".Owner UUID").equals("none")) ? true : false), cfg.getString("Islands.ID-"+id+".Owner UUID"));
			
			ids.add(status);
		}
		
		return ids;
	}
	/**
	 * Gibt den IslandStatus zurück
	 * @return
	 */
	public static IslandStatus getIslandStatus(int island_id) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(new File("plugins/SkyBlock/Insel-Index-File.yml"));	
		IslandStatus status = new IslandStatus(island_id, cfg.getBoolean("Islands.ID-"+island_id+".Claimed"),
				((cfg.getString("Islands.ID-"+island_id+".Owner UUID") == null || cfg.getString("Islands.ID-"+island_id+".Owner UUID").equals("none")) ? true : false), cfg.getString("Islands.ID-"+island_id+".Owner UUID"));

		
		return status;
	}
	/**
	 * Gibt eine ArrayList<Intger> mit den IDs aller geclaimten Inseln zurück.
	 * @return
	 */
	public static ArrayList<Integer> getClaimedIslands() {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(new File("plugins/SkyBlock/Insel-Index-File.yml"));
		int amount = cfg.getInt("Islands.Amount Generated");
		
		ArrayList<Integer> ids = new ArrayList<Integer>();
		
		for(int id = 0; id != amount; id++) if(cfg.getBoolean("Islands.ID-"+id+".Claimed")) ids.add(id);
		
		return ids;
	}
	/**
	 * Gibt eine ArrayList<Intger> mit den IDs aller ungeclaimten Inseln zurück.
	 * @return
	 */
	public static ArrayList<Integer> getUnclaimedIslands() {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(new File("plugins/SkyBlock/Insel-Index-File.yml"));
		int amount = cfg.getInt("Islands.Amount Generated");
		
		ArrayList<Integer> ids = new ArrayList<Integer>();
		
		for(int id = 0; id != amount; id++) if(cfg.getBoolean("Islands.ID-"+id+".Claimed") == false) ids.add(id);
		
		return ids;
	}
	/**
	 * Gibt die Insel-Datei eines Spielers zurück.
	 * getOwnerFileInsteadOfMemberFile gibt an, ob die Island-File des Members oder des Owners zurückgegeben werden soll.
	 * getOwnerFileInsteadOfMemberFile ist irrelevant, wenn der Spieler eine eigene Insel hat
	 * @param uuid
	 * @param getOwnerFileInsteadOfMemberFile
	 * @return
	 */
	public static File getIslandFile(String uuid, boolean getOwnerFileInsteadOfMemberFile) {
		if(isMemberOfAnIsland(UUID.fromString(uuid))) {
			File file = new File("plugins/SkyBlock/Inseln/"+uuid+"-Insel.yml");
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			if(getOwnerFileInsteadOfMemberFile)	return new File("plugins/SkyBlock/Inseln/"+cfg.getString("Islands.Owner UUID")+"-Insel.yml");
			else return file;
		}else return new File("plugins/SkyBlock/Inseln/"+uuid+"-Insel.yml");
	}
	/**
	 * Gibt die OwnerIsland-File zurück.
	 * @param uuid
	 * @return
	 */
	public static File getIslandFile(String uuid) {
		return getIslandFile(uuid, true);
	}
	/**
	 * Gibt die Insel-ID von der Insel zurück, auf der er ein Member ist.
	 * @param uuid
	 * @return
	 */
	public static int getIslandIDWhereHeIsMemberOf(UUID uuid) {
		if(isMemberOfAnIsland(uuid)) {			
			File file = new File("plugins/SkyBlock/Inseln/"+uuid+"-Insel.yml");
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			if(file.exists() == false) return -1;
			else return cfg.getInt("Islands.ID");
		}else return -1;
	}
	/**
	 * Gibt eine Liste mit den aktiven Removern zurück.
	 * @return
	 */
	public static ArrayList<Integer> getRemovers() {
		File file = new File("plugins/SkyBlock/running_island_remover.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		ArrayList<Integer> removers = new ArrayList<Integer>();
		if(cfg.getStringList("Removers") != null && cfg.getStringList("Removers").isEmpty() == false) {			
			for(String s : cfg.getStringList("Removers")) removers.add(Integer.parseInt(s));
		}
		
		return removers;
	}
	/**
	 * Gibt eine HashMap mit allen Settings zurück
	 * @param uuid
	 * @return
	 */
	public static HashMap<String, Boolean> getSettings(String uuid) {
		File is_file = getIslandFile(uuid);
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(is_file);
		HashMap<String, Boolean> settings = new HashMap<String, Boolean>();
		settings.put("Allow Visiting", cfg.getBoolean("Islands.Settings.Allow Visting"));
		settings.put("Firespread", cfg.getBoolean("Islands.Settings.Firespread"));
		settings.put("Monsterspawning", cfg.getBoolean("Islands.Settings.Monsterspawning"));
		settings.put("Animalspawning", cfg.getBoolean("Islands.Settings.Animalspawning"));
		settings.put("TNT Damage", cfg.getBoolean("Islands.Settings.TNT Damage"));
		settings.put("Mob Griefing", cfg.getBoolean("Islands.Settings.Mob Griefing"));
		settings.put("PVP", cfg.getBoolean("Islands.Settings.PVP"));
		return settings;
	}
	
	/**
	 * Gibt die ID der Insel B zurück, mit der sich die Insel A verbunden hat
	 * @param uuid
	 * @return
	 */
	public static int getCollaboratorIslandID(String uuid) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(getIslandFile(uuid));
		return cfg.getInt("Islands.Collab With");
	}
	
	/**
	 * Gibt die Z-Koordinate zurück, auf der sich die Insel befindet
	 * @param uuid
	 * @return
	 */
	public static int getLocationZ(String uuid) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(getIslandFile(uuid));
		return cfg.getInt("Islands.LocZ");
	}
	
	/**
	 * Gibt die X-Koordinate zurück, auf der sich die Insel befindet
	 * @param uuid
	 * @return
	 */
	public static int getLocationX(String uuid) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(getIslandFile(uuid));
		return cfg.getInt("Islands.LocX");
	}
	
	/**
	 * Gibt die Z-Koordinate zurück, auf der sich der Insel-Spielerspawn befindet
	 * @param uuid
	 * @return
	 */
	public static int getSpawnZ(String uuid) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(getIslandFile(uuid));
		return cfg.getInt("Islands.Spawnpoint.LocZ");
	}
	
	/**
	 * Gibt die X-Koordinate zurück, auf der sich der Insel-Spielerspawn befindet
	 * @param uuid
	 * @return
	 */
	public static int getSpawnX(String uuid) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(getIslandFile(uuid));
		return cfg.getInt("Islands.Spawnpoint.LocX");
	}
	
	/**
	 * Gibt die Zentrierte Location des SkyBlockSpawn zurück.
	 * @return
	 */
	public static Location getSkyBlockCenteredSpawnLocation() {
		File file = new File("plugins/SkyBlock/config.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				
		Location l = new Location(Bukkit.getWorld(cfg.getString("Spawn.Location.World")),
				cfg.getDouble("Spawn.Location.X"),
				cfg.getDouble("Spawn.Location.Y"),
				cfg.getDouble("Spawn.Location.Z"),
			(float)cfg.getInt("Spawn.Location.Yaw"), 
			(float)cfg.getInt("Spawn.Location.Pitch"));
		
		return l.clone();
	}
	
	/**
	 * Gibt den Cuboid des Spawn zurück
	 * @return
	 */
	public static Cuboid getSpawnCuboid() {
		File file = new File("plugins/SkyBlock/config.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		int radius = cfg.getInt("Spawn.Radius");
		int offsetY = cfg.getInt("Spawn.OffsetY");
		Location center = getSkyBlockCenteredSpawnLocation();
		
		return new Cuboid(center.clone().add(-radius,-offsetY,-radius), center.clone().add(radius,offsetY,radius));
	}
	
	/**
	 * Gibt die Welt zurück, in der sich die Insel befindet
	 * @param uuid
	 * @return
	 */
	public static String getWorldName(String uuid) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(getIslandFile(uuid));
		return cfg.getString("Islands.World");
	}
	
	/**
	 * Gibt die Welt zurück, in der sich die Insel befindet
	 * @param uuid
	 * @return
	 */
	public static World getWorld(String uuid) {
		String world = SkyBlockGenerator.skyworldName;
		if(Bukkit.getWorld(world) == null)  return null;
		else return Bukkit.getWorld(world);
	}
	/**
	 * Gibt die Weltenname, X und Z Werte einer Freien Insel aus der Island-Index-File zurück
	 * @param island_id
	 * @return
	 */
	public static String getWorldNameOutOfIslandIndexFile(int island_id) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(new File("plugins/SkyBlock/Insel-Index-File.yml"));
		return cfg.getString("Islands.Id-"+island_id+".Worldname");
	}
	public static int getLocXOutOfIslandIndexFile(int island_id) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(new File("plugins/SkyBlock/Insel-Index-File.yml"));
		return cfg.getInt("Islands.Id-"+island_id+".LocX");
	}
	public static int getLocZOutOfIslandIndexFile(int island_id) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(new File("plugins/SkyBlock/Insel-Index-File.yml"));
		return cfg.getInt("Islands.Id-"+island_id+".LocZ");
	}
	
	/**
	 * Gibt eine ArrayList mit allen Banned Players zurück
	 * @param random_pick
	 * @return
	 */
	public static int getUnclaimedIslandID(boolean random_pick) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(new File("plugins/SkyBlock/Insel-Index-File.yml"));
		int unclaimed_id = 0;
		if(random_pick) {
			while(cfg.getBoolean("Islands.ID-"+unclaimed_id+".Claimed") == true) {
				unclaimed_id = ran.nextInt(cfg.getInt("Islands.Amount Generated"));
			}
		}else {
			int amount_of_generated = cfg.getInt("Islands.Amount Generated");
			for(int i = 1; i != amount_of_generated; i++) {
				if(cfg.getBoolean("Islands.ID-"+i+".Claimed") == false) {
					unclaimed_id = i;
					break;
				}
			}
		}
		return unclaimed_id;
	}
	
	/**
	 * Gibt eine ArrayList mit allen Banned Players zurück
	 * @param uuid
	 * @return
	 */
	public static ArrayList<String> getBannedPlayers(String uuid) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(getIslandFile(uuid));
		ArrayList<String> members = (ArrayList<String>) cfg.getStringList("Islands.Banned Players");
		return members;
	}
	
	/**
	 * Gibt eine ArrayList mit allen Members zurück
	 * @param uuid
	 * @return
	 */
	public static ArrayList<String> getMembers(String uuid) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(getIslandFile(uuid));
		ArrayList<String> members = (ArrayList<String>) cfg.getStringList("Islands.Members");
		return members;
	}
	/**
	 * Gibt einen String zurück, der die Namen aller Mitglieder einer Insel beinhaltet.
	 * @param uuid
	 * @return
	 */
	public static String getStringListOfMembers(String uuid) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(getIslandFile(uuid));
		ArrayList<String> members = (ArrayList<String>) cfg.getStringList("Islands.Members");
		String t = "§3"+PlayerAtlas.getPlayername(members.get(0));
		for(int i = 1; i != members.size(); i++) t = t + "§f, §3"+PlayerAtlas.getPlayername(members.get(i));
		return t;
	}
	/**
	 * Gibt die Location einer Insel zurück
	 * @param owner
	 * @return
	 */
	public static Location getLocationOfIsland(int island_id) {
		File index = new File("plugins/SkyBlock/Insel-Index-File.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(index);
		if(island_id > cfg.getInt("Islands.Amount Generated")) return null;
		
		Location loc = null;
		
		if(island_id == 0) {
			/*  */
			loc = new Location(Bukkit.getWorld(SkyBlockGenerator.skyworldName),
					cfg.getDouble("Islands.ID-"+island_id+".LocX"),
							SkyBlockGenerator.islandHeight,
							cfg.getDouble("Islands.ID-"+island_id+".LocZ"));
		}else {
			loc = new Location(Bukkit.getWorld(SkyBlockGenerator.skyworldName),
					cfg.getDouble("Islands.ID-"+island_id+".LocX"),
							SkyBlockGenerator.islandHeight,
							cfg.getDouble("Islands.ID-"+island_id+".LocZ"));
		}
		return loc;
	}
	/**
	 * Gibt die Location einer Insel zurück
	 * @param owner
	 * @return
	 */
	public static Location getLocationOfIsland(Player owner) {
		Location loc = new Location(getWorld(PlayerProfiler.getUUID(owner).toString()),
				getLocationX(PlayerProfiler.getUUID(owner).toString()),
				SkyBlockGenerator.islandHeight,
				getLocationZ(PlayerProfiler.getUUID(owner).toString()));
		return loc;
	}
	/**
	 * Gibt die Location einer Insel zurück
	 * @param owner
	 * @return
	 */
	public static Location getLocationOfMemberIsland(Player owner) {
		Location loc = new Location(getWorld(PlayerProfiler.getUUID(owner).toString()),
				getLocationX(PlayerProfiler.getUUID(owner).toString()),
				SkyBlockGenerator.islandHeight,
				getLocationZ(PlayerProfiler.getUUID(owner).toString()));
		return loc;
	}
	/**
	 * Gibt den vom Spieler definierten Insel-Spawn zurück
	 * @param owner
	 * @return
	 */
	public static Location getPlayerDefinedIslandSpawn(Player owner) {
		File file = new File("plugins/SkyBlock/Inseln/"+PlayerProfiler.getUUID(owner).toString()+"-Insel.yml");
		if(file.exists() == false) return null;
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		Location loc = new Location(Bukkit.getWorld(cfg.getString("Islands.Spawnpoint.World")),
				cfg.getDouble("Islands.Spawnpoint.LocX"),
				cfg.getDouble("Islands.Spawnpoint.LocY"),
				cfg.getDouble("Islands.Spawnpoint.LocZ"),
				(float)cfg.getInt("Islands.Spawnpoint.Yaw"),
				(float)cfg.getInt("Islands.Spawnpoint.Pitch"));
		return loc;
	}
	/**
	 * Gibt den vom Spieler definierten Insel-Spawn zurück
	 * @param owner
	 * @return
	 */
	public static Location getPlayerDefinedIslandSpawn(int island_id) {
		String owner = "none";
		for(IslandStatus st : getIslandsWithStatus()) {
			if(st.getId() == island_id) {
				owner = st.getOwnerUUID();
				break;
			}
		}
		File file = new File("plugins/SkyBlock/Inseln/"+owner+"-Insel.yml");
		if(file.exists() == false) return null;
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		Location loc = new Location(Bukkit.getWorld(cfg.getString("Islands.Spawnpoint.World")),
				cfg.getDouble("Islands.Spawnpoint.LocX"),
				cfg.getDouble("Islands.Spawnpoint.LocY"),
				cfg.getDouble("Islands.Spawnpoint.LocZ"),
				(float)cfg.getInt("Islands.Spawnpoint.Yaw"),
				(float)cfg.getInt("Islands.Spawnpoint.Pitch"));
		return loc;
	}
	/**
	 * Setzt den vom Spieler definierten Insel-Spawn
	 * @param owner
	 * @param spawn
	 * @return
	 */
	public static boolean setPlayerDefinedIslandSpawn(Player owner, Location spawn) {
		for(String uuid : IslandManager.getProfile(owner).getMembers()) {
			if(Bukkit.getPlayer(UUID.fromString(uuid)) != null) {
				Player p = PlayerProfiler.getPlayer(UUID.fromString(uuid));
				File file = new File("plugins/SkyBlock/Inseln/"+PlayerProfiler.getUUID(p).toString()+"-Insel.yml");
				if(file.exists() == false) return false;
				FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				cfg.set("Islands.Spawnpoint.World", spawn.getWorld().getName());
				cfg.set("Islands.Spawnpoint.LocX", spawn.getX());
				cfg.set("Islands.Spawnpoint.LocY", spawn.getY());
				cfg.set("Islands.Spawnpoint.LocZ", spawn.getZ());
				cfg.set("Islands.Spawnpoint.Yaw", spawn.getYaw());
				cfg.set("Islands.Spawnpoint.Pitch", spawn.getPitch());
				try {
					cfg.save(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else if(Bukkit.getOfflinePlayer(UUID.fromString(uuid)) != null) {
				OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
				File file = new File("plugins/SkyBlock/Inseln/"+p.getUniqueId().toString()+"-Insel.yml");
				if(file.exists() == false) return false;
				FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				cfg.set("Islands.Spawnpoint.World", spawn.getWorld().getName());
				cfg.set("Islands.Spawnpoint.LocX", spawn.getX());
				cfg.set("Islands.Spawnpoint.LocY", spawn.getY());
				cfg.set("Islands.Spawnpoint.LocZ", spawn.getZ());
				cfg.set("Islands.Spawnpoint.Yaw", spawn.getYaw());
				cfg.set("Islands.Spawnpoint.Pitch", spawn.getPitch());
				try {
					cfg.save(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		File file = new File("plugins/SkyBlock/Inseln/"+PlayerProfiler.getUUID(owner).toString()+"-Insel.yml");
		if(file.exists() == false) return false;
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		cfg.set("Islands.Spawnpoint.World", spawn.getWorld().getName());
		cfg.set("Islands.Spawnpoint.LocX", spawn.getX());
		cfg.set("Islands.Spawnpoint.LocY", spawn.getY());
		cfg.set("Islands.Spawnpoint.LocZ", spawn.getZ());
		cfg.set("Islands.Spawnpoint.Yaw", spawn.getYaw());
		cfg.set("Islands.Spawnpoint.Pitch", spawn.getPitch());
		try {
			cfg.save(file);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Methode für den /isc Befehl.
	 * 
	 * Setzt einen neuen Besitzer einer Insel oder entfernt ihn.
	 * @param island_id
	 * @param uuid
	 */
	public static boolean EDITsetIslandOwner(int island_id, String uuid) {
		File file = new File("plugins/SkyBlock/Insel-Index-File.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		int prev_id = getIslandID(uuid);
		
		if(uuid == null ||uuid.equalsIgnoreCase("null")) {
			cfg.set("Islands.ID-"+prev_id+".Claimed", false);
			cfg.set("Islands.ID-"+prev_id+".Owner UUID", "none");
			
			cfg.set("Islands.ID-"+island_id+".Claimed", false);
			cfg.set("Islands.ID-"+island_id+".Owner UUID", "none");
		}else if(uuid.toString().equals(cfg.getString("Islands.ID-"+island_id+".Owner UUID")) == false){
			cfg.set("Islands.ID-"+prev_id+".Owner UUID", "none");
			cfg.set("Islands.ID-"+island_id+".Owner UUID", uuid);
		}
		try { cfg.save(file); } catch (IOException e) { e.printStackTrace(); }
		
		file = new File("plugins/SkyBlock/Inseln/"+uuid+"-Insel.yml");
		cfg = YamlConfiguration.loadConfiguration(file);
		
		if(uuid == null) {
			cfg.set("Islands", island_id);
			cfg.set("Islands.Owner UUID", "none");
		}else if(uuid.toString().equals(cfg.getString("Islands.ID.Owner UUID")) == false){
			cfg.set("Islands.Owner UUID", uuid);
		}
		try { cfg.save(file); } catch (IOException e) { e.printStackTrace(); }
		
		if(IslandManager.getProfile(uuid) != null) IslandManager.getProfile(uuid).reload();
		IslandManager.getIslandData(island_id).reloadData();
		
		return true;
	}
	
	/**
	 * Methode für den /isc Befehl.
	 * 
	 * Bearbeitet die Liste der verbannten Spieler
	 * @param island_id
	 * @param uuid
	 */
	public static boolean EDITsetBannedPlayers(int island_id, UUID owner, ArrayList<String> uuids) {
		File file = new File("plugins/SkyBlock/Inseln/"+owner.toString()+"-Insel.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		cfg.set("Islands.ID-"+island_id+".Banned Players", uuids);
		
		try { cfg.save(file); } catch (IOException e) { e.printStackTrace(); }
		return true;
	}
	
	/**
	 * Methode für den /isc Befehl.
	 * 
	 * Bearbeitet die Liste der Member
	 * @param island_id
	 * @param uuid
	 */
	public static boolean EDITsetMembers(int island_id, UUID owner, ArrayList<String> uuids) {
		File file = new File("plugins/SkyBlock/Inseln/"+owner.toString()+"-Insel.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		cfg.set("Islands.ID-"+island_id+".Members", uuids);
		
		try { cfg.save(file); } catch (IOException e) { e.printStackTrace(); }
		return true;
	}
	
	/**
	 * Methode für den /isc Befehl.
	 * 
	 * Bearbeitet eine Insel-Eigenschaft
	 * @param island_id
	 * @param uuid
	 */
	public static boolean EDITProperty(int island_id, String property, Object value) {
		IslandData data = IslandManager.getIslandData(island_id);
		if(data == null || data.getSettings() == null) return false;
		else data.getSettings().edit(property, value, null);
		return true;
	}
	
	/**
	 * Setzt den Spielerspawn neu
	 * @param owner
	 * @param loc
	 * @return
	 */
//	public static boolean setSpawn(Player owner, Location loc) {
//		File f = getIslandFile(PlayerProfiler.getUUID(owner).toString());
//		FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
//		cfg.set("Islands.Spawnpoint.World", loc.getWorld().getName());
//		cfg.set("Islands.Spawnpoint.LocX", loc.getX());
//		cfg.set("Islands.Spawnpoint.LocY", loc.getY());
//		cfg.set("Islands.Spawnpoint.LocZ", loc.getZ());
//		cfg.set("Islands.Spawnpoint.Yaw", loc.getYaw());
//		cfg.set("Islands.Spawnpoint.Pitch", loc.getPitch());
//		try {
//			cfg.save(f);
//			return true;
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
	
	/**
	 * Gibt eien Insel zum claimen frei
	 * @param island_id
	 * @param releaser
	 * @return
	 */
	public static boolean releaseIsland(int island_id) {
		File index = new File("plugins/SkyBlock/Insel-Index-File.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(index);
		
		int amount = cfg.getInt("Islands.Amount Generated");
		if(island_id > amount || island_id < 0) return false;
		
		if(isClaimed(island_id) == false) return false;
		
		cfg.set("Islands.Amount Claimed", (cfg.getInt("Islands.Amount Claimed") - 1));
		cfg.set("Islands.ID-"+island_id+".Claimed", false);
		cfg.set("Islands.ID-"+island_id+".Owner UUID", "none");
		try {
			cfg.save(index);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Löscht eine Insel-Datei eines Spielers.
	 * Wird verwendet, wenn der Spieler ein Member einer Insel ist, aber dann eine eigene möchte.
	 * @param uuid
	 * @return
	 */
	public static boolean deleteIslandFile(UUID uuid) {
		getIslandFile(uuid.toString(), false).delete();
		return true;
	}
	
	public static HashMap<Player, BukkitRunnable> runs = new HashMap<Player, BukkitRunnable>();
	/**
	 * Löscht die Insel eines Spielers
	 * @param owner
	 * @return
	 */
	public static boolean deleteIsland(Player owner) {
		if(SkyFileManager.hasIsland(owner)) {			
			if(runs.containsKey(owner)) {
				owner.sendMessage(Prefixes.SERVER.px()+"§cDu besitzt keine Insel");
				return false;
			}else {
				if(!Settings.isInMaintenance()) owner.teleport(SkyBlock.spawn);
				File index = new File("plugins/SkyBlock/Insel-Index-File.yml");
				FileConfiguration cfg1 = YamlConfiguration.loadConfiguration(getIslandFile(PlayerProfiler.getUUID(owner).toString()));
				FileConfiguration cfg2 = YamlConfiguration.loadConfiguration(index);
				int id = cfg1.getInt("Islands.ID");
				
				//Insel freigeben für neuen Besitzer
				if(Settings.decreaseAmountClaimedAfterDeletedIsland()) cfg2.set("Islands.Amount Claimed", (cfg2.getInt("Islands.Amount Claimed")-1));
				cfg2.set("Islands.ID-"+id+".Claimed", true);
				cfg2.set("Islands.ID-"+id+".Owner UUID", "none");
				
				//Insel Profil-Datei wird gelöscht
				getIslandFile(PlayerProfiler.getUUID(owner).toString()).delete();
				
				try {
					cfg2.save(index);
					IslandManager.clearIsland(runs, owner, id);
					IslandManager.getIslandData(id).reloadData();
					IslandManager.getProfile(owner).deleteAllMembers();
					
					IslandManager.unloadProfile(owner);
					owner.sendMessage(Prefixes.SERVER.px()+"§aDeine Insel wurde gelöscht");
					return true;
				} catch (IOException e) {
					e.printStackTrace();
					owner.sendMessage(Prefixes.SERVER.px()+"§cEtwas ist schief gelaufen, beim löschen deiner Insel");
					return false;
				}
			}
		}else Chat.sendHoverableMessage(owner, "Du kannst diese Insel nicht löschen.", "Du kannst diese Insel nicht lösche,\nweil sie nicht dir gehört.", false, true);
		return false;
	}
	
	/**
	 * Überprüft, ob ein Spieler eine Insel besitzt
	 * @param owner
	 * @return
	 */
	public static boolean hasIsland(Player owner) {
		return (hasIsland(PlayerProfiler.getUUID(owner).toString()));
	}
	public static boolean hasIsland(String owner) {
		boolean hasClaimed = isOwnerOf(owner, getIslandFile(owner));
		return hasClaimed;
	}
	
	/**
	 * Gibt an, ob ein Spieler der Owner einer Insel ist
	 * @param uuid
	 * @param file
	 * @return
	 */
	public static boolean isOwnerOf(String uuid, File file) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		return (uuid.equals(cfg.getString("Islands.Owner UUID")) ? true : false);
	}
	/**
	 * Gibt an, ob der Spieler ein Member einer Insel ist.
	 * @param uuid
	 * @return
	 */
	public static boolean isMemberOfAnIsland(UUID uuid) {
//		SkyBlock.sendMessage(MessageType.INFO, "UUID = "+uuid);
		File file = new File("plugins/SkyBlock/Inseln/"+uuid.toString()+"-Insel.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		if(file.exists() == false) return false;
		else if(uuid.toString() == null) return false;
		else return !(cfg.getString("Islands.Owner UUID").equals(uuid.toString()));
	}
	/**
	 * Gibt einen Boolean zurück, ob die Insel belegt oder frei ist
	 * @param island_id
	 * @return
	 */
	public static boolean isClaimed(int island_id) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(new File("plugins/SkyBlock/Insel-Index-File.yml"));
		return (cfg.getBoolean("Islands.ID-"+island_id+".Claimed"));
	}
	
//	/**
//	 * Erstellt eine Profil-Datei eines Spieler, der keine eigene Insel hat sondern nur Member auf einer anderen Insel ist.
//	 * @param island_id
//	 * @param owner
//	 * @param joiner
//	 * @return
//	 */
//	public static boolean createMemberIslandFile(int island_id, Player owner, Player joiner) {
//		File profile = new File("plugins/SkyBlock/Profiles/"+PlayerProfiler.getUUID(joiner).toString()+".yml");
//		FileConfiguration cfg = YamlConfiguration.loadConfiguration(profile);
//		
//		
//		cfg.set("Island.Owner", PlayerProfiler.getUUID(owner).toString());
//		try {
//			cfg.save(profile);
//			return true;
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
	
	/**
	 * Claimed eine Insel
	 * @param island_id
	 * @param owner
	 * @return
	 */
	public static boolean claimIsland(int island_id, Player player, Player owner) {
		File index = new File("plugins/SkyBlock/Insel-Index-File.yml");
		
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(index);
		if(isClaimed(island_id) == false || player != owner) {
			if(player == owner) cfg.set("Islands.Amount Claimed", (cfg.getInt("Islands.Amount Claimed") + 1));
			else cfg.set("Islands.Amount Claimed", cfg.getInt("Islands.Amount Claimed"));
			cfg.set("Islands.ID-"+island_id+".Claimed", true);
			cfg.set("Islands.ID-"+island_id+".Owner UUID", PlayerProfiler.getUUID(owner).toString());
			try {
				cfg.save(index);
			}
			catch (IOException e) {
				e.printStackTrace();
				if(PlayerProfiler.getUUID(player).toString().equals(PlayerProfiler.getUUID(owner).toString())) owner.sendMessage(Prefixes.SERVER.px()+"Deine Insel konnte nicht geclaimed werden");
				else Chat.sendHoverableMessage(owner,
						Prefixes.SERVER.px()+"§cDu konntest nicht als Member hinzugefügt werden.",
						"Bitte deinen Kumpel darum es nochmal zu versuchen: §e/is addfriend "+player.getName(), false, true);
			}
			
			File is_file = getIslandFile(PlayerProfiler.getUUID(player).toString());
			FileConfiguration cfg2 = YamlConfiguration.loadConfiguration(is_file);
			cfg2.set("Islands.ID", island_id);
			cfg2.set("Islands.Owner UUID", PlayerProfiler.getUUID(owner).toString());
			cfg2.set("Islands.World", cfg.getString("Islands.ID-"+island_id+".World"));
			cfg2.set("Islands.LocX", cfg.getInt("Islands.ID-"+island_id+".LocX"));
			cfg2.set("Islands.LocZ", cfg.getInt("Islands.ID-"+island_id+".LocZ"));
			cfg2.set("Islands.Spawnpoint.World", cfg.getString("Islands.ID-"+island_id+".World"));
			cfg2.set("Islands.Spawnpoint.LocX", cfg.getInt("Islands.ID-"+island_id+".LocX"));
			cfg2.set("Islands.Spawnpoint.LocZ", cfg.getInt("Islands.ID-"+island_id+".LocZ"));
			cfg2.set("Islands.Collab With", "none");
			cfg2.set("Islands.Biome", "minecraft:forest");
			cfg2.set("Islands.Settings.Allow Visiting", true);
			cfg2.set("Islands.Settings.Firespread", true);
			cfg2.set("Islands.Settings.Monsterspawning", true);
			cfg2.set("Islands.Settings.Animalspawning", true);
			cfg2.set("Islands.Settings.TNT Damage", true);
			cfg2.set("Islands.Settings.Mob Griefing", true);
			cfg2.set("Islands.Settings.PVP", false);
			cfg2.set("Islands.Members", emptylist.toString());
			cfg2.set("Islands.Banned Players", emptylist.toString());
			try {
				cfg2.save(is_file);
				
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				if(PlayerProfiler.getUUID(player).toString().equals(PlayerProfiler.getUUID(owner).toString())) owner.sendMessage(Prefixes.SERVER.px()+"Die Insel konnte nicht geclaimed werden");
				else Chat.sendHoverableMessage(owner,
						Prefixes.SERVER.px()+"§cDu konntest nicht als Member hinzugefügt werden.",
						"Bitte deinen Kumpel darum es nochmal zu versuchen: §e/is addfriend "+player.getName(), false, true);
				return false;
			}
			
		}else return false;
	}
	/**
	 * Aktualisiert die Insel des Spielers
	 * @param island_id
	 * @param uuid
	 * @param owner
	 */
	public static void updateProfileIsland(int island_id, Player owner, Player joiner) {
		
		if(hasIsland(joiner)) {
			deleteIsland(joiner);
		}
		
		if(claimIsland(island_id, joiner, owner)) {
			IslandManager.getProfile(joiner).reload();
			joiner.sendMessage("Dein Profil wurde geupdatet");
		}
//		File profile = new File("plugins/SkyBlock/Profiles/"+uuid+".yml");
//		FileConfiguration cfg_pro = YamlConfiguration.loadConfiguration(profile);
//		
//		if(owner == null) profile.delete();
//		else {			
//			cfg_pro.set("Island.ID", island_id);
//			cfg_pro.set("Island.isOwner", (uuid.equals(owner) ? true : false));
//			cfg_pro.set("Island.Owner UUID", owner);
//			try {
//				cfg_pro.save(profile);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	/**
	 * Fügt einen neuen Member hinzu, der auch gespeichert wird.
	 * @param uuid
	 * @param new_member
	 * @return
	 */
	public static boolean addMember(Player uuid, Player new_member) {
		File file = getIslandFile(PlayerProfiler.getUUID(uuid).toString());
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		ArrayList<String> members = (ArrayList<String>) cfg.getStringList("Islands.Members");
		members.add(PlayerProfiler.getUUID(new_member).toString());
		cfg.set("Islands.Members", members);
		try {
			cfg.save(file);
			updateProfileIsland(cfg.getInt("Islands.ID"), uuid, new_member);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Verbannt einen Spieler von einer Insel und löscht dabei seinen Member Status
	 * @param owner
	 * @param uuid
	 * @return
	 */
	public static boolean banPlayerFromIsland(Player owner, UUID uuid) {
		File file = new File("plugins/SkyBlock/Inseln/"+PlayerProfiler.getUUID(owner).toString()+"-Insel.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		List<String> banned = cfg.getStringList("Islands.Banned Players");
		if(banned.contains(uuid.toString())) return false;
		else {
			removeMember(PlayerProfiler.getUUID(owner).toString(), uuid.toString());
			banned.add(uuid.toString());
			cfg.set("Islands.Banned Players", banned);
			
			try {
				cfg.save(file);
				IslandManager.getIslandData(IslandManager.getProfile(owner).getIslandID()).getSettings().load(true);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
	}
	public static boolean unbanPlayerFromIsland(Player owner, UUID uuid) {
		File file = new File("plugins/SkyBlock/Inseln/"+PlayerProfiler.getUUID(owner).toString()+"-Insel.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		List<String> banned = cfg.getStringList("Islands.Banned Players");
		if(banned.contains(uuid.toString()) == false) return false;
		else {
			banned.remove(uuid.toString());
			cfg.set("Islands.Banned Players", banned);
			
			try {
				cfg.save(file);
				IslandManager.getIslandData(IslandManager.getProfile(owner).getIslandID()).getSettings().load(true);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
	}
	/**
	 * Entfernt einen Member, was auch gespeichert wird.
	 * @param uuid
	 * @param new_member
	 * @return
	 */
	public static boolean removeMember(String uuid, String member) {
		File file = getIslandFile(uuid, true);
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		ArrayList<String> members = (ArrayList<String>) cfg.getStringList("Islands.Members");
		if(members.contains(member)) {
			UUID owner_uuid = UUID.fromString(cfg.getString("Islands.Owner UUID"));
			members.remove(member);
			cfg.set("Islands.Members", members);
			try {
				cfg.save(file);
				if(PlayerProfiler.getPlayer(owner_uuid) != null) IslandManager.getProfile(PlayerProfiler.getPlayer(owner_uuid)).reload();
				
				deleteIslandFile(UUID.fromString(member));
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}else return false;
	}
	
	/**
	 * Gibt die Anzahl an freien Inseln zurück
	 * @param uuid
	 * @return
	 */
//	public static int getUnclaimedIslandsAmount() {
//		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
//		return (cfg.getInt("Islands.Amount.Generated") - cfg.getInt("Islands.Amount.Claimed"));
//	}
	
	/*
	 * TODO: Gibt die Anzahl an Belegten Inseln zurück
	 */
//	public static int getClaimedIslandsAmount() {
//		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
//		return (cfg.getInt("Islands.Amount.Claimed"));
//	}
	
	/*
	 * TODO: Gibt die Anzahl an Generierten Inseln zurück
	 */
//	public static int getGeneratedIslandsAmount() {
//		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
//		return (cfg.getInt("Islands.Amount.Generated"));
//	}
	
	
	/**
	 * Markiert eine Insel als Bewohnbar.
	 * Diese Methode wird erst dann ausgeführt, wenn die Insel komplett zerstört wurde.
	 * @param island_id
	 */
	public static void markIslandAsClaimable(int island_id) {
		File index = new File("plugins/SkyBlock/Insel-Index-File.yml");
		FileConfiguration cfg2 = YamlConfiguration.loadConfiguration(index);
		
		//Insel freigeben für neuen Besitzer
		cfg2.set("Islands.ID-"+island_id+".Claimed", false);
		
		try {
			cfg2.save(index);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Speichert einen aktuell, laufenden Remover um ihn wenn der Server abstürzt wieder starten zu können.
	 * @param island_id
	 */
	public static void saveIslandRemover(int island_id) {
		File file = new File("plugins/SkyBlock/running_island_remover.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		ArrayList<Integer> removers = new ArrayList<Integer>();
		if(cfg.getStringList("Removers") != null && cfg.getStringList("Removers").isEmpty() == false) {			
			for(String s : cfg.getStringList("Removers")) removers.add(Integer.parseInt(s));
		}
		removers.add(island_id);
		
		cfg.set("Removers", removers);
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		};
	}
	
	/**
	 * Entfernt einen Remover von den laufenden um ihn entgültig fertigzustellen.
	 * @param island_id
	 */
	public static void removeIslandRemover(int island_id) {
		File file = new File("plugins/SkyBlock/running_island_remover.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		cfg.set("Removers", cfg.getStringList("Removers").remove(""+island_id));
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		};
	}
	
	public static class IslandStatus {
		
		private int id = 0;
		private boolean claimed = false, needRelease= false;
		private String ownerUuid = "none";
		
		public IslandStatus(int id, boolean claimed, boolean needRelease) {
			this.id = id;
			this.claimed = claimed;
			this.needRelease = needRelease;
		}
		public IslandStatus(int id, boolean claimed, boolean needRelease, String ownerUuid) {
			this.id = id;
			this.claimed = claimed;
			this.needRelease = needRelease;
			this.ownerUuid = ownerUuid;
		}
		
		/**
		 * Gibt die ID der Insel zurück.
		 * @return
		 */
		public int getId() {return id;}
		/**
		 * Gibt an, ob die Insel mit der oben festgehaltenen ID gelcaimt ist oder nicht.
		 * @return
		 */
		public boolean isClaimed() {return claimed;}
		/**
		 * Gibt an, ob eine Insel reserviert für den Release ist und noch bereinigt werden muss.
		 * @return
		 */
		public boolean needRelease() {return needRelease;}
		
		/**
		 * Gibt die UUID des Besitzers der Insel zurück
		 * @return
		 */
		public String getOwnerUUID() { return ownerUuid; }
		
	}
	
}
