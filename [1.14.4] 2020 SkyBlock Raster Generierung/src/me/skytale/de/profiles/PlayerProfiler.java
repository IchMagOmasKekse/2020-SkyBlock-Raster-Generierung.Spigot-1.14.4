package me.skytale.de.profiles;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.skytale.de.UUIDFetcher;

public class PlayerProfiler {
	
	public static HashMap<Player, PlayerProfile> profiles = new HashMap<Player, PlayerProfile>();
	public static HashMap<UUID, Player> players = new HashMap<UUID, Player>();
	
	public PlayerProfiler() {}
	
	/**
	 * Registriert alle Online Spieler, wenn sie noch nicht registriert wurden
	 * @return
	 */
	public static void registerAll() {
		for(Player p : Bukkit.getOnlinePlayers()) registerPlayer(p);
	}
	
	/**
	 * Unregistriert alle Online Spieler, wenn sie noch registriert sind
	 * @return
	 */
	public static void unregisterAll() {
		for(Player p : Bukkit.getOnlinePlayers()) unregisterPlayer(p);
	}
	
	/**
	 * Gibt das registrierte Spielerprofil eines Spielers zurück.
	 * Wenn noch nicht registriert, wird eines registriert und dann zurückgegeben.
	 * @param p
	 * @return
	 */
	public static PlayerProfile getProfile(Player p) {
		if(isRegistered(p)) return profiles.get(p);
		registerPlayer(p);
		return profiles.get(p);
	}
	/**
	 * Gibt das registriert Spielerprofil eines SPielers zurück anhand der UUID.
	 * @param uuid
	 * @return
	 */
	public static PlayerProfile getProfile(UUID uuid) {
		for(Player p : profiles.keySet()) if(profiles.get(p).getUUID().toString().equals(uuid.toString())) return profiles.get(p);
		return null;
	}
	
	/**
	 * Unregistered einen Spieler und dessen Spielerprofil.
	 * @param p
	 * @return
	 */
	public static boolean unregisterPlayer(Player p) {
		if(isRegistered(p) == false) return false;
		else profiles.remove(p);
		return true;
	}
	
	/**
	 * Registriert einen Spieler mit dessen Spielerprofile.
	 * @param p
	 * @return
	 */
	public static boolean registerPlayer(Player p) {
		if(isRegistered(p) && getProfile(p).getUUID() != null && getProfile(p).getPlayerName() != null) return false;
		else if(isRegistered(p)) unregisterPlayer(p);
		profiles.put(p, new PlayerProfile(p));
		players.put(getUUID(p), p);
		return true;
	}
	
	/**
	 * Gibt an, ob ein Spieler bereits mit dessen Spielerprofile registriert ist.
	 * @param p
	 * @return
	 */
	public static boolean isRegistered(Player p) {
		return profiles.containsKey(p);
	}
	
	/**
	 * Gibt die im Spielerprofile registrierte UUID zurück
	 * @param p
	 * @return
	 */
	public static UUID getUUID(Player p) {
		if(isRegistered(p) == false) registerPlayer(p);
		return getProfile(p).getUUID();
	}
	
	/**
	 * Gibt den im Spielerprofil registrieten Spielernamen zurück.
	 * @param p
	 * @return
	 */
	public static String getCurrentPlayerName(Player p) {
		if(isRegistered(p) == false) registerPlayer(p);
		return getProfile(p).getPlayerName();
	}
	
	/**
	 * Gibt den registrierten Spieler einer UUID wieder
	 * @param uuid
	 * @return
	 */
	public static Player getPlayer(UUID uuid) {
		Player p = players.get(uuid);
		if(p == null) return null;
		return p;
	}
	
	public static class PlayerProfile {
		
		private Player player = null;
		private UUID uuid = null;
		private String current_username = "";
		
		public PlayerProfile(Player player) {
			this.player = player;
			this.uuid = UUIDFetcher.getUUID(player.getName());
			this.current_username = UUIDFetcher.getPlayerName(uuid);
		}
		
		/**
		 * Gibt den Spieler zurück
		 * @return
		 */
		public Player getPlayer() {
			return player;
		}
		
		/**
		 * Gibt die vom UUIDFetcher gefetchte UUID zurück.
		 * Daten vom Mojang Server ausgelesen
		 * @return
		 */
		public UUID getUUID() {
			return uuid;
		}
		
		/**
		 * Gibt den aktuellen vom UUIDFetcher gefetchten Spielernamen zurück.
		 * Daten vom Mojang Server ausgelesen
		 * @return
		 */
		public String getPlayerName() {
			return current_username;
		}
		
	}
	
}
