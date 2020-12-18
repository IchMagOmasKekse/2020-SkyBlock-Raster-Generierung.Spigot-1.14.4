package me.dreamisland.de;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SkyCoinHandler implements Listener {
	
	
	/* Der Wert der beschreibt, wie viel Diamanten man für einen Coin bekommt */
	private static final double coin_equals_diamonds_amount = 3.15;
	private static double coins_per_join = calcDiamondToCoins(50);
	
	public SkyCoinHandler() {
		
		SkyBlock.getInstance().getServer().getPluginManager().registerEvents(this, SkyBlock.getInstance());
		coins_per_join = calcDiamondToCoins(50*coin_equals_diamonds_amount);
	}
	
	/**
	 * Gibt dem beitretenden Spieler SkyCoins, wenn der Spieler zum ersten Mal an diesen Tag gejoint ist.
	 * @param event
	 */
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		e.getPlayer().sendMessage(Prefixes.SERVER.px()+"Du hättest §e"+coins_per_join+"C §7erhalten");
	}
	
	/**
	 * Rechnet Coins in Diamanten um und gibt diese auch zurück.
	 * @param coins
	 * @return
	 */
	public static double calcCoinsToDiamonds(double coins) {
		return (coins + ((coins / coin_equals_diamonds_amount) * getBoosters().getPercentage()));
	}
	
	/**
	 * Gibt Eine Liste aller aktiven Booster zurück.
	 * @return
	 */
	public static Booster getBoosters() {
		return new Booster(1.950d);
	}
	
	/**
	 * Rechnet Diamanten in Coins um und gibt diese zurück.
	 * @param diamonds
	 * @return
	 */
	public static double calcDiamondToCoins(double diamonds) {
		double coins = (diamonds * coin_equals_diamonds_amount);
		
		double raw =  coins;
		raw += (coins * getBoosters().getPercentage());
		raw = raw * 100;
		raw = Math.round(raw);
		raw = raw / 100;
		return raw;
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
