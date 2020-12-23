package me.skytale.de;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.skytale.de.profiles.PlayerProfiler;


public class PlayerAtlas {
	
	private static String atlas_path = "plugins/SkyBlock/player_atlas.yml";
	
	public PlayerAtlas(SkyBlock pl) {
		pl.saveResource("player_atlas.yml", false);
	}
	
	public static UUID getUUID(String name) {
		File file = new File(atlas_path);
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		UUID uuid = null;
		
		try {
			uuid = UUID.fromString(cfg.getString(name));
		}catch(NullPointerException ex) {
			uuid = null;
		}
		
		return uuid;
	}
	
	public static String getPlayername(String uuid) {
		File file = new File(atlas_path);
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		return cfg.getString(uuid);
	}
	
	public static void savePlayer(Player p) {
		File file = new File(atlas_path);
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		if(getPlayername(PlayerProfiler.getUUID(p).toString()) == null || getPlayername(PlayerProfiler.getUUID(p).toString()).equals(p.getName()) == false) {
			cfg.set(p.getName(), PlayerProfiler.getUUID(p).toString());
			cfg.set(PlayerProfiler.getUUID(p).toString(), p.getName());
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
