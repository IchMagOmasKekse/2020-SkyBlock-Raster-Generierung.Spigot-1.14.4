package me.crafttale.de;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.crafttale.de.economy.EconomyManager.MoneyType;
import me.crafttale.de.tablist.TablistManager;
import net.md_5.bungee.api.ChatColor;

public class Settings {
	
	/* Random */
	private static int islandsPerPage = 19; //Die Anzahl an Inseln, die bei /unclaimed und /claimed angezeigt werden. Maximal 99!
	private static boolean decreaseAmountClaimedAfterDeletedIsland = false;
	private static boolean isMaintenance = false; //Gibt an, ob der Server gerade von einem Developer berarbeitet wird
	private static boolean respawnAtIslandIFDiedInSkyBlock = true; //Gibt an, ob der Spieler auf seiner Insel respawnt, nachdem er von ihr heruntergefallen ist
	private static boolean allowTraderSpawnOnIsland = true; //Gibt an, ob Händler(mit Lamas) auf der Insel spawnen darf
	/* Economy */
	private static int start_money1 = 0; //Startmoney: Money1
	private static int start_money2 = 0; //Startmoney: Money2
	private static int start_money3 = 0; //Startmoney: Money3
	private static int start_money4 = 0; //Startmoney: Money4
	private static int start_money5 = 0; //Startmoney: Money5
	public static int daily_Reward_reset_at_seconds = 0; //Wann sollen die Daily Rewards wieder verfügbar sein?
	public static int daily_Reward_reset_at_minutes = 0; //Wann sollen die Daily Rewards wieder verfügbar sein?
	public static int daily_Reward_reset_at_hours = 0; //Wann sollen die Daily Rewards wieder verfügbar sein?
	/* Ranks */
	public static String prefix_default = "";
	public static String prefix_donor1 = "";
	public static String prefix_donor2 = "";
	public static String prefix_donor3 = "";
	public static String prefix_donor4 = "";
	public static String prefix_epic = "";
	public static String prefix_supporter = "";
	public static String prefix_admin = "";
	public static String prefix_developer = "";
	public static String suffix_default = "";
	public static String suffix_donor1 = "";
	public static String suffix_donor2 = "";
	public static String suffix_donor3 = "";
	public static String suffix_donor4 = "";
	public static String suffix_epic = "";
	public static String suffix_supporter = "";
	public static String suffix_admin = "";
	public static String suffix_developer = "";
	/* Chat */
	public static String chat_seperator = "";
	public static String chat_format = "";
	/* XLogger */
	public static boolean xloggerAutoSave = true;
	public static int xloggerAutoSaveAt = 5000;
	public static boolean xloggerLogDate = true;
	public static boolean xloggerLogCommandUsage = true;
	public static boolean xloggerLogChatMessage = true;
	public static boolean xloggerLogDebugMessage = true;
	public static boolean xloggerLogPluginInternProcesses = true;
	
	public static void reloadSettings() {
		File file = new File("plugins/"+SkyBlock.getSB().getDescription().getName()+"/config.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		if(file.exists() == false) SkyBlock.getSB().saveResource("config.yml", true);
		
		allowTraderSpawnOnIsland = cfg.getBoolean("Spawn.Allow Trader With Llama");
		daily_Reward_reset_at_seconds = cfg.getInt("Daily Reward.Reset At.Seconds");
		daily_Reward_reset_at_minutes = cfg.getInt("Daily Reward.Reset At.Minutes");
		daily_Reward_reset_at_hours = cfg.getInt("Daily Reward.Reset At.Hours");
		
		prefix_default =   ChatColor.translateAlternateColorCodes('&', cfg.getString("Ranks.default.Prefix"));
		prefix_donor1 =    ChatColor.translateAlternateColorCodes('&', cfg.getString("Ranks.donor1.Prefix"));
		prefix_donor2 =    ChatColor.translateAlternateColorCodes('&', cfg.getString("Ranks.donor2.Prefix"));
		prefix_donor3 =    ChatColor.translateAlternateColorCodes('&', cfg.getString("Ranks.donor3.Prefix"));
		prefix_donor4 =    ChatColor.translateAlternateColorCodes('&', cfg.getString("Ranks.donor4.Prefix"));
		prefix_epic =      ChatColor.translateAlternateColorCodes('&', cfg.getString("Ranks.epic.Prefix"));
		prefix_supporter = ChatColor.translateAlternateColorCodes('&', cfg.getString("Ranks.supporter.Prefix"));
		prefix_admin =     ChatColor.translateAlternateColorCodes('&', cfg.getString("Ranks.admin.Prefix"));
		prefix_developer = ChatColor.translateAlternateColorCodes('&', cfg.getString("Ranks.developer.Prefix"));
		
		suffix_default =   ChatColor.translateAlternateColorCodes('&', cfg.getString("Ranks.default.Suffix"));
		suffix_donor1 =    ChatColor.translateAlternateColorCodes('&', cfg.getString("Ranks.donor1.Suffix"));
		suffix_donor2 =    ChatColor.translateAlternateColorCodes('&', cfg.getString("Ranks.donor2.Suffix"));
		suffix_donor3 =    ChatColor.translateAlternateColorCodes('&', cfg.getString("Ranks.donor3.Suffix"));
		suffix_donor4 =    ChatColor.translateAlternateColorCodes('&', cfg.getString("Ranks.donor4.Suffix"));
		suffix_epic =      ChatColor.translateAlternateColorCodes('&', cfg.getString("Ranks.epic.Suffix"));
		suffix_supporter = ChatColor.translateAlternateColorCodes('&', cfg.getString("Ranks.supporter.Suffix"));
		suffix_admin =     ChatColor.translateAlternateColorCodes('&', cfg.getString("Ranks.admin.Suffix"));
		suffix_developer = ChatColor.translateAlternateColorCodes('&', cfg.getString("Ranks.developer.Suffix"));
				
		chat_seperator = ChatColor.translateAlternateColorCodes('&', cfg.getString("Chat.Chat Seperator"));
		chat_format = ChatColor.translateAlternateColorCodes('&', cfg.getString("Chat.Chat Format")).replace("{CHAT_SEPERATOR}", chat_seperator).replace("{RANK_SEPERATOR}", TablistManager.pxToPlayernameSeperator);
		
		xloggerAutoSave = cfg.getBoolean("XLogger.Auto Save.Enabled");
		xloggerAutoSaveAt = cfg.getInt("XLogger.Auto Save.Save At");
		xloggerLogDate = cfg.getBoolean("XLogger.Log Date");
		xloggerLogCommandUsage = cfg.getBoolean("XLogger.Log.Command Usage");
		xloggerLogChatMessage = cfg.getBoolean("XLogger.Log.Chatmessages");
		xloggerLogDebugMessage = cfg.getBoolean("XLogger.Log.Debug Messages");
		xloggerLogPluginInternProcesses = cfg.getBoolean("XLogger.Log.Plugin Intern Processes");
		
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
	
	public static boolean allowTraderSpawnOnIsland() { return allowTraderSpawnOnIsland; }
	
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
