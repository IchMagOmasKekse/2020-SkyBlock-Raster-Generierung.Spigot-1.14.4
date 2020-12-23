package me.skytale.de.economy;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.entity.Player;

import me.skytale.de.Chat;
import me.skytale.de.SkyBlock;
import me.skytale.de.Chat.MessageType;
import me.skytale.de.economy.EconomyManager.EditOperation;
import me.skytale.de.economy.EconomyManager.MoneyType;
import me.skytale.de.profiles.PlayerProfiler;

public class SkyCoinHandler {
	
	
	/* Der Wert der beschreibt, wie viel Diamanten man für einen Coin bekommt */
	private static final double coin_equals_diamonds_amount = .15d; //3.15 Diamanten sind 1 SkyCoin
	private static double coins_per_join = 0; //50 Diamanten in SkyCoins umrechnen
	
	private static HashMap<String, Booster> boosters = new HashMap<String, Booster>();
	
	public SkyCoinHandler() {
		
		boosters = new HashMap<String, Booster>();		
		boosters.put("BETA-BOOSTER", new Booster(.75d));
		
		coins_per_join = calcDiamondToCoins(50/*<- Reward Diamonds */ );
	}
	
	/**
	 * Gibt dem beitretenden Spieler SkyCoins, wenn der Spieler zum ersten Mal an diesen Tag gejoint ist.
	 * @param event
	 */
	public static void addJoinCoins(Player p) {
		Chat.sendHoverableMessage(p, "Du hast §e"+coins_per_join+"C §ferhalten", "Leider ist das GeldSystem vorrübergehend deaktiviert", false, true);
		EconomyManager.editMoney(PlayerProfiler.getUUID(p), MoneyType.MONEY1, coins_per_join, EditOperation.ADD);
	}
	
	/**
	 * Rechnet Coins in Diamanten um und gibt diese auch zurück.
	 * @param coins
	 * @return
	 */
	public static double calcCoinsToDiamonds(double coins) {
		
		double raw_diamonds = 0;
		double diamonds = 0;

		raw_diamonds = (coins + ((coins / coin_equals_diamonds_amount)));
		diamonds = raw_diamonds;
		
		if(getBoosters() != null) {			
			for(Booster b : getBoosters()) {
				diamonds += raw_diamonds * b.getPercentage();
			}
		}
		
		return diamonds;
	}
	
	/**
	 * Gibt Eine Liste aller aktiven Booster zurück.
	 * @return
	 */
	public static Collection<Booster> getBoosters() {
		if(boosters.isEmpty()) return null;
		return boosters.values();
	}
	
	/**
	 * Sendet alle aktiven Booster an einen Spieler
	 * @param p
	 */
	public static void listBoosters(Player p) {
		SkyBlock.sendMessage(MessageType.INFO, p, "Aktive Booster:");
		for(String s : boosters.keySet()) {
			p.sendMessage("- §b"+s+MessageType.INFO.getSuffix()+"-> §a"+(boosters.get(s).getPercentage()*100)+"%");
		}
	}
	
	/**
	 * Rechnet Diamanten in Coins um und gibt diese zurück.
	 * @param diamonds
	 * @return
	 */
	public static double calcDiamondToCoins(double diamonds) {
		double coins = (diamonds / coin_equals_diamonds_amount);
		
		double raw_coins = coins;
		double final_coins = raw_coins;
		if(getBoosters() != null) {			
			for(Booster b : getBoosters()) {
				final_coins += raw_coins * b.getPercentage();
			}
		}
		final_coins = final_coins * 100;
		final_coins = Math.round(final_coins);
		final_coins = final_coins / 100;
		return final_coins;
	}
	
	public static class Booster {
		
		double percentage = 0.0d;
		
		public Booster(double percentage) {
			this.percentage = percentage;
		}
		
		public double getPercentage() {
			return percentage;
		}
		
	}
	
}
