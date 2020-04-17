package me.ichmagomaskekse.de.filemanagement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.ichmagomaskekse.de.Prefixes;
import me.ichmagomaskekse.de.SkyBlock;
import me.ichmagomaskekse.de.SkyBlockGenerator;

public class SkyFileManager {
	
	private static Random ran = new Random();
	private static File island_index_File = new File("plugins/SkyBlock/Insel-Index-File.yml");
	private static ArrayList<String> emptylist = new ArrayList<String>();

	public SkyFileManager() {
		if(island_index_File.exists() == false) {
			//TODO: Es muss die Datei neugerneriert werden
		}
	}
	
	/*
	 * TODO: Gibt die ID einer Insel zurück
	 */
	public static int getIslandID(String uuid) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(getIslandFile(uuid));
		return cfg.getInt("Islands.ID");
	}
	
	/*
	 * TODO: Gibt den Spawnpunkt einer Insel zurück
	 */
	public static Location getSpawnpoint(String uuid) {
		Location spawnpoint = new Location(getWorld(uuid), getLocationX(uuid), SkyBlockGenerator.islandHeight, getLocationZ(uuid));
		return spawnpoint;
	}
	
	/*
	 * TODO: LÃ¶scht die Insel eines Spielers
	 */
	public static boolean deleteIsland(Player owner) {
		owner.teleport(SkyBlock.spawn);
		FileConfiguration cfg1 = YamlConfiguration.loadConfiguration(getIslandFile(owner.getUniqueId().toString()));
		FileConfiguration cfg2 = YamlConfiguration.loadConfiguration(island_index_File);
		int id = cfg1.getInt("Islands.ID");
		
		//Insel freigeben für neuen Besitzer
		cfg2.set("Islands.Amount Claimed", (cfg2.getInt("Islands.Amount Claimed")-1));
		cfg2.set("Islands.ID-"+id+".Claimed", false);
		cfg2.set("Islands.ID-"+id+".Owner UUID", "none");
		
		//Insel Profil-Datei wird gelÃ¶scht
		getIslandFile(owner.getUniqueId().toString()).delete();
		
		try {
			cfg2.save(island_index_File);			
			owner.sendMessage(Prefixes.SERVER.getPrefix()+"§aDeine Insel wurde gelöscht");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			owner.sendMessage(Prefixes.SERVER.getPrefix()+"§cEtwas ist schief gelaufen, beim löschen deiner Insel");
			return false;
		}
	}
	
	/*
	 * TODO: Gibt die Location einer Insel zurück
	 */
	public static Location getLocationOfIsland(Player owner) {
		Location loc = new Location(getWorld(owner.getUniqueId().toString()),
				getLocationX(owner.getUniqueId().toString()),
						SkyBlockGenerator.islandHeight,
						getLocationZ(owner.getUniqueId().toString()));
		return loc;
	}
	
	/*
	 * TODO: Ãœberprüft, ob ein Spieler eine Insel besitzt
	 */
	public static boolean hasIsland(Player owner) {
		boolean hasClaimed = getIslandFile(owner.getUniqueId().toString()).exists();
		return hasClaimed;
	}
	
	/*
	 * TODO: Claimed eine Insel
	 */
	public static boolean claimIsland(int island_id, Player owner) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(island_index_File);
		if(isClaimed(island_id) == false) {
			cfg.set("Islands.Amount Claimed", (cfg.getInt("Islands.Amount Claimed") + 1));
			cfg.set("Islands.ID-"+island_id+".Claimed", true);
			cfg.set("Islands.ID-"+island_id+".Owner UUID", owner.getUniqueId().toString());
			try {
				cfg.save(island_index_File);
			}
			catch (IOException e) {
				e.printStackTrace();
				owner.sendMessage(Prefixes.SERVER.getPrefix()+"Deine Insel konnte nicht geclaimed werden");
				}
			
			File is_file = getIslandFile(owner.getUniqueId().toString());
			FileConfiguration cfg2 = YamlConfiguration.loadConfiguration(is_file);
			cfg2.set("Islands.ID", island_id);
			cfg2.set("Islands.Owner UUID", owner.getUniqueId().toString());
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
				owner.sendMessage(Prefixes.SERVER.getPrefix()+"Deine Insel konnte nicht geclaimed werden");
				return false;
			}
			
		}else return false;
	}
	/*
	 * TODO: Gibt die Weltenname, X und Z Werte einer Freien Insel aus der Island-Index-File zurück
	 */
	public static String getWorldNameOutOfIslandIndexFile(int island_id) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(island_index_File);
		return cfg.getString("Islands.Id-"+island_id+".Worldname");
	}
	public static int getLocXOutOfIslandIndexFile(int island_id) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(island_index_File);
		return cfg.getInt("Islands.Id-"+island_id+".LocX");
	}
	public static int getLocZOutOfIslandIndexFile(int island_id) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(island_index_File);
		return cfg.getInt("Islands.Id-"+island_id+".LocZ");
	}
	
	/*
	 * TODO: Gibt eine ArrayList mit allen Banned Players zurück
	 */
	public static int getUnclaimedIslandID(boolean random_pick) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(island_index_File);
		int unclaimed_id = 0;
		if(random_pick) {
			while(cfg.getBoolean("Islands.ID-"+unclaimed_id+".Claimed") == true) {
				unclaimed_id = ran.nextInt(cfg.getInt("Islands.Amount Generated"));
			}
		}else {
			unclaimed_id = (cfg.getInt("Islands.Amount Claimed")+1);
		}
		return unclaimed_id;
	}
	
	/*
	 * TODO: Gibt eine ArrayList mit allen Banned Players zurück
	 */
	public static ArrayList<String> getBannedPlayers(String uuid) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(getIslandFile(uuid));
		ArrayList<String> members = (ArrayList<String>) cfg.getStringList("Islands.Banned Players");
		return members;
	}
	
	/*
	 * TODO: Gibt eine ArrayList mit allen Members zurück
	 */
	public static ArrayList<String> getMembers(String uuid) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(getIslandFile(uuid));
		ArrayList<String> members = (ArrayList<String>) cfg.getStringList("Islands.Members");
		return members;
	}
	
	/*
	 * TODO: Gibt eine HashMap mit allen Settings zurück
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
	
	/*
	 * TODO: Gibt die ID der Insel B zurück, mit der sich die Insel A verbunden hat
	 */
	public static int getCollaboratorIslandID(String uuid) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(getIslandFile(uuid));
		return cfg.getInt("Islands.Collab With");
	}
	
	/*
	 * TODO: Gibt die Z-Koordinate zurück, auf der sich die Insel befindet
	 */
	public static int getLocationZ(String uuid) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(getIslandFile(uuid));
		return cfg.getInt("Islands.LocZ");
	}
	
	/*
	 * TODO: Gibt die X-Koordinate zurück, auf der sich die Insel befindet
	 */
	public static int getLocationX(String uuid) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(getIslandFile(uuid));
		return cfg.getInt("Islands.LocX");
	}
	
	/*
	 * TODO: Gibt die Welt zurück, in der sich die Insel befindet
	 */
	public static String getWorldName(String uuid) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(getIslandFile(uuid));
		return cfg.getString("Islands.World");
	}
	
	/*
	 * TODO: Gibt die Welt zurück, in der sich die Insel befindet
	 */
	public static World getWorld(String uuid) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(getIslandFile(uuid));
		String world = SkyBlockGenerator.skyworldName;
		if(Bukkit.getWorld(world) == null)  return null;
		else return Bukkit.getWorld(world);
	}
	
	/*
	 * TODO: Gibt einen Boolean zurück, ob die Insel belegt oder frei ist
	 */
	public static boolean isClaimed(int island_id) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(island_index_File);
		return (cfg.getBoolean("Islands.ID-"+island_id+".Claimed"));
	}
	
	/*
	 * TODO: Gibt die Anzahl an freien Inseln zurück
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
	
	/*
	 * TODO: getIslandFile() gibt die Insel-Datei eines Spielers zurück 
	 */
	public static File getIslandFile(String uuid) {
		return new File("plugins/SkyBlock/Inseln/"+uuid+"-Insel.yml");
	}
	
}
