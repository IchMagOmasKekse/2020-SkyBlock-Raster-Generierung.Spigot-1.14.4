package me.crafttale.de.profiles;

import java.io.File;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import me.crafttale.de.SkyBlock;
import me.crafttale.de.profiles.processing.ProcessSender;
import me.crafttale.de.profiles.processing.ProcessType;

public class PlayerSettings {
	
	private File file = new File("plugins/"+SkyBlock.getSB().getDescription().getName()+"/player_settings.yml");
	private static HashMap<Player, HashMap<String, Object>> settings = new HashMap<Player, HashMap<String, Object>>();
	
	public PlayerSettings() { }
	
	/**
	 * Behandelt einen Spieler, nachdem er gejoint ist.
	 * @param e
	 */
	public static void onJoin(PlayerJoinEvent e) {
		loadSettings(e.getPlayer());
	}
	
	/**
	 * Gibt an, ob die Einstellungen eines Spielers bereits geladen wurden.
	 * @param p
	 * @return
	 */
	public static boolean hasLoadedSettings(Player p) {
		return settings.get(p) == null ? false : true;
	}
	
	/**
	 * Lädt die Einstellungen eines Spielers
	 * @param p
	 */
	public static void loadSettings(Player p) {
		HashMap<String, Object> sets = null;
		if(hasLoadedSettings(p)) sets = settings.get(p);
		else sets = new HashMap<String, Object>();
		
		sets.put(ProcessType.SHOW_NEW_DAW_NOTIFICATION.getCodename(), true);
		sets.put(ProcessType.PLAY_JOIN_MELODY.getCodename(), true);
		sets.put(ProcessType.ALLOW_CHAT_MESSAGES.getCodename(), true);
		sets.put(ProcessType.SHOW_PARTICLE.getCodename(), true);
		
		settings.put(p, sets);
		p.sendMessage("Settings wurden geladen");
	}
	
	/**
	 * Gibt eine Einstellung zurück oder true, wenn die Einstellungen eines Spielers nicht geladen wurden
	 * @param p
	 * @param process
	 * @return
	 */
	public static Object getSetting(Player p, ProcessType process) {
		if(hasLoadedSettings(p)) return settings.get(p).get(process.getCodename());
		else return true;
	}
	
	/**
	 * Gibt die Ausführbestätigung eines Prozesses, wenn es die Spielereinstellung denn erlaubt.
	 * @param process
	 * @param obj
	 * @param value
	 * @param pSender
	 */
	public static void processIfAllowed(ProcessType process, Object obj, Object value, ProcessSender pSender) {
		switch(process) {
		case SHOW_NEW_DAW_NOTIFICATION:
			if(obj instanceof Player) {
				
			}
			break;
		case ALLOW_CHAT_MESSAGES:
			if(obj instanceof Player) {
				
			}
			break;
		case PLAY_JOIN_MELODY:
			break;
		default:
			break;
		}
	}
	
	public static Object getConfirmation(ProcessType process, Player p) {
		switch(process) {
		case SHOW_NEW_DAW_NOTIFICATION:
			return (boolean)getSetting(p, process);
		case ALLOW_CHAT_MESSAGES:
			return (boolean)getSetting(p, process);
		case PLAY_JOIN_MELODY:
			return (boolean)getSetting(p, process);
		}
		return true;
	}
	
}
