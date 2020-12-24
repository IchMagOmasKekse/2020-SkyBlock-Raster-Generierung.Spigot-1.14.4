package me.crafttale.de.filemanagement;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.crafttale.de.SkyBlock;
import me.crafttale.de.Chat.MessageType;

public class SkyFileManagerOld {
	
	/*
	 * Diese Klasse ist keine optimale Lösung.
	 * Beim Abfragen gewissen Daten verursacht diese Klasse Laggs,
	 * da es nur eine Datei gibt, wo alle drinnen steht und diese vo Speicher her zu groß ist.
	 */
	
	private static File file = new File("plugins/SkyBlock/Island-Databank.yml");
	private static Random ran = new Random();
	
	public SkyFileManagerOld() {
		if(file.exists() == false) {
			SkyBlock.sendOperatorMessage(MessageType.ERROR, "Die Island-Databank.yml im Ordner §f'§7/plugins/SkyBlock/§f'§c existiert nicht!",
					                     "Gebe §nbitte§c umgehend §7§nIchMagOmasKekse§c Bescheid!");
		}
	}
	
	
	/*
	 * TODO: Gibt eine ArrayList mit allen Banned Players zurück
	 */
	public static int getUnclaimedIslandID(boolean random_pick) {
		int generated = 0;
		int unclaimed_id = 0;
		boolean unclaimed = false;
		if(random_pick == false) {
			int claimed = 0;
			while(unclaimed == false) {
				claimed = getClaimedIslandsAmount();
				if(isClaimed(claimed) == false) {
					unclaimed = true;
					unclaimed_id = claimed+1;
				}
			}
		}else {
			generated = getGeneratedIslandsAmount();
			int i = ran.nextInt(generated);
			while(unclaimed == false) {
				if(isClaimed(i) == false) {
					unclaimed_id = i;
				}else i = ran.nextInt(generated);
			}
		}

		return unclaimed_id;
	}
	
	/*
	 * TODO: Gibt eine ArrayList mit allen Banned Players zurück
	 */
	public static ArrayList<String> getBannedPlayers(int island_id) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		ArrayList<String> members = (ArrayList<String>) cfg.getStringList("Islands.ID-"+island_id+".Banned Players");
		return members;
	}
	
	/*
	 * TODO: Gibt eine ArrayList mit allen Members zurück
	 */
	public static ArrayList<String> getMembers(int island_id) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		ArrayList<String> members = (ArrayList<String>) cfg.getStringList("Islands.ID-"+island_id+".Members");
		return members;
	}
	
	/*
	 * TODO: Gibt eine HashMap mit allen Settings zurück
	 */
	public static HashMap<String, Boolean> getSettings(int island_id) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		HashMap<String, Boolean> settings = new HashMap<String, Boolean>();
		settings.put("Allow Visiting", cfg.getBoolean("Islands.ID-"+island_id+".Settings.Allow Visting"));
		settings.put("Firespread", cfg.getBoolean("Islands.ID-"+island_id+".Settings.Firespread"));
		settings.put("Monsterspawning", cfg.getBoolean("Islands.ID-"+island_id+".Settings.Monsterspawning"));
		settings.put("Animalspawning", cfg.getBoolean("Islands.ID-"+island_id+".Settings.Animalspawning"));
		settings.put("TNT Damage", cfg.getBoolean("Islands.ID-"+island_id+".Settings.TNT Damage"));
		settings.put("Mob Griefing", cfg.getBoolean("Islands.ID-"+island_id+".Settings.Mob Griefing"));
		settings.put("PVP", cfg.getBoolean("Islands.ID-"+island_id+".Settings.PVP"));
		return settings;
	}
	
	/*
	 * TODO: Gibt die ID der Insel B zurück, mit der sich die Insel A verbunden hat
	 */
	public static int getCollaboratorIslandID(int island_id) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		return cfg.getInt("Islands.ID-"+island_id+".Collab With");
	}
	
	/*
	 * TODO: Gibt die Z-Koordinate zurück, auf der sich die Insel befindet
	 */
	public static int getLocationZ(int island_id) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		return cfg.getInt("Islands.ID-"+island_id+".LocZ");
	}
	
	/*
	 * TODO: Gibt die X-Koordinate zurück, auf der sich die Insel befindet
	 */
	public static int getLocationX(int island_id) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		return cfg.getInt("Islands.ID-"+island_id+".LocX");
	}
	
	/*
	 * TODO: Gibt die Welt zurück, in der sich die Insel befindet
	 */
	public static String getWorldName(int island_id) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		return cfg.getString("Islands.ID-"+island_id+".World");
	}
	
	/*
	 * TODO: Gibt die Welt zurück, in der sich die Insel befindet
	 */
	public static World getWorld(int island_id) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		String world = cfg.getString("Islands.ID-"+island_id+".World");
		if(Bukkit.getWorld(world) == null)  return null;
		else return Bukkit.getWorld(world);
	}
	
	/*
	 * TODO: Gibt die UUID des Insel-Besitzers zurück
	 */
	public static UUID getOwnerUUID(int island_id) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		UUID uuid = null;
		String owner = cfg.getString("Islands.ID-"+island_id+".Owner UUID");
		if(owner.equalsIgnoreCase("none") == false) uuid = UUID.fromString(owner);
		return uuid;
	}
	
	/*
	 * TODO: Gibt einen Boolean zurück, ob die Insel belegt oder frei ist
	 */
	public static boolean isClaimed(int island_id) {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		return (cfg.getBoolean("Islands.ID-"+island_id+".Claimed"));
	}
	
	/*
	 * TODO: Gibt die Anzahl an freien Inseln zurück
	 */
	public static int getUnclaimedIslandsAmount() {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		return (cfg.getInt("Islands.Amount.Generated") - cfg.getInt("Islands.Amount.Claimed"));
	}
	
	/*
	 * TODO: Gibt die Anzahl an Belegten Inseln zurück
	 */
	public static int getClaimedIslandsAmount() {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		return (cfg.getInt("Islands.Amount.Claimed"));
	}
	
	/*
	 * TODO: Gibt die Anzahl an Generierten Inseln zurück
	 */
	public static int getGeneratedIslandsAmount() {
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		return (cfg.getInt("Islands.Amount.Generated"));
	}
	
}
