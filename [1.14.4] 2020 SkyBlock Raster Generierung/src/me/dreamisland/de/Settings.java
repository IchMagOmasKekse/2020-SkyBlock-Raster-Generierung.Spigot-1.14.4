package me.dreamisland.de;

import java.io.File;

import me.dreamisland.de.economy.EconomyManager.MoneyType;

public class Settings {
	
	private static int islandsPerPage = 19; //Die Anzahl an Inseln, die bei /unclaimed und /claimed angezeigt werden. Maximal 99!
	private static boolean decreaseAmountClaimedAfterDeletedIsland = false;
	private static boolean isMaintenance = false; //Gibt an, ob der Server gerade von einem Developer berarbeitet wird
	private static boolean respawnAtIslandIFDiedInSkyBlock = true; //Gibt an, ob der Spieler auf seiner Insel respawnt, nachdem er von ihr heruntergefallen ist
	private static int start_money1 = 0; //Startmoney: Money1
	private static int start_money2 = 0; //Startmoney: Money2
	private static int start_money3 = 0; //Startmoney: Money3
	private static int start_money4 = 0; //Startmoney: Money4
	private static int start_money5 = 0; //Startmoney: Money5
	public static void reloadSettings() {
		@SuppressWarnings("unused")
		File file = new File("plugins/SkyBlock/config.yml");
		
		/*
		 * READ SETTINGS OUT OF config.yml
		 */
		
	}
	
	/**
	 * Gibt an, ob die Anzahl der geclaimten Inseln verringert wrden soll, nachdem eine Insel deletet wurde.
	 * Der Nutzen dahinter ist, dass die Inseln bisher nicht automatisch bereinigt werden, sondern von einem Admin persönlich removed wird.
	 * Wenn also der Boolean decreaseAmountClaimedAfterDeletedIsland auf False steht, gibt es Inoffiziell verfübare Inseln.
	 * @return
	 */
	public static boolean decreaseAmountClaimedAfterDeletedIsland() {return decreaseAmountClaimedAfterDeletedIsland;}
	
	
	/**
	 * Gibt an, wie viele Inseln per Benutzung von /unclaimed und /claimed angezeigt werden.
	 * Maximal 99 Nachrichten im Minecraft Chat möglich!
	 * @return
	 */
	public static int getIslandsPerPage() {return islandsPerPage;}
	
	/**
	 * Gibt an, ob der Server gerade von einem Developer bearbeitet wird.
	 * @return
	 */
	public static boolean isInMaintenance() {return isMaintenance;}
	
	/**
	 * Gibt an, ob der Spieler auf seiner Insel respawnt, nachdem er von ihr heruntergefallen ist
	 * @return
	 */
	public static boolean respawnAtIslandIFDiedInSkyBlock() {return respawnAtIslandIFDiedInSkyBlock;}
	
	/**
	 * Gibt das Startkapital für einen Spieler einer Währung zurück
	 * @param type
	 * @return
	 */
	public static int getStartMoney(MoneyType type) {
		switch(type) {
		case MONEY1: return start_money1;
		case MONEY2: return start_money2;
		case MONEY3: return start_money3;
		case MONEY4: return start_money4;
		case MONEY5: return start_money5;
		default: return 0;
		}
	}
}
