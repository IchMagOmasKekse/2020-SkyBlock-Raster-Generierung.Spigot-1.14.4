package me.crafttale.de.waitlobby;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.crafttale.de.SkyBlock;

public class WaitLobby {
	
	private static Location waitForGeneratingLobby = new Location(Bukkit.getWorld("skyblockworld"), 18.5, 82, 75.5, -90, 0);
	private static ArrayList<Player> lobby = new ArrayList<Player>();
	
	/**
	 * Bringt den Spieler zur Wartelobby
	 * @param p
	 */
	public static void sendToWaitingLobby(Player p) {
		p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 255, 255));
		hideAllPlayers(p);
		p.teleport(waitForGeneratingLobby);
	}
	
	/**
	 * Sendet einen Spieler aus der Wartelobby raus
	 * @param p
	 * @param loc
	 */
	public static void takeOutFromWaitingLobby(Player p, Location loc) {
		showAllPlayers(p);
		p.teleport(loc);
		p.removePotionEffect(PotionEffectType.BLINDNESS);
	}
	
	/**
	 * Gibt an, ob ein Spieler sich in der Wartelobby befindet
	 * @param p
	 * @return
	 */
	public static boolean isInWaitingLobby(Player p) {
		return lobby.contains(p);
	}
	
	/**
	 * Versteckt alle Spieler
	 * @return
	 */
	public static void hideAllPlayers(Player p) {
		for(Player t : Bukkit.getOnlinePlayers()) {
			p.hidePlayer(SkyBlock.getSB(), t);
		}
	}
	
	/**
	 * Zeigt alle Spieler
	 * @return
	 */
	public static void showAllPlayers(Player p) {
		for(Player t : Bukkit.getOnlinePlayers()) {
			p.showPlayer(SkyBlock.getSB(), t);
		}
	}
	
	/**
	 * Versteckt zusätzlich einen Spieler, zum Beispiel falls dieser neu gejoint ist
	 * @param p
	 * @param hide
	 */
	public static void hideExtraPlayer(Player hide) {
		for(Player p : lobby) {			
			if(p.canSee(hide)) p.hidePlayer(SkyBlock.getSB(), hide);
		}
	}
	
}
