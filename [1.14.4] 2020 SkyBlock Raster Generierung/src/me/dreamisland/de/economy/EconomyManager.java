package me.dreamisland.de.economy;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.dreamisland.de.PlayerAtlas;
import me.dreamisland.de.Settings;
import me.dreamisland.de.SkyBlock;

public class EconomyManager {
	
	public static int max_money = Integer.MAX_VALUE;
	public static int max_min_money = -1000;
	
	/**
	 * Gibt an, ob ein Spieler bereits ein Konto besitzt.
	 * @param uuid
	 * @return
	 */
	public static boolean hasAccount(UUID uuid) {
		
		File file = new File("plugins/SkyBlock/economy.yml");
		if(file.exists() == false) SkyBlock.getSB().saveResource("economy.yml", true);
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		return cfg.getBoolean(uuid.toString()+".account.exists");
		
	}
	/**
	 * Gibt an, ob ein Spieler bereits ein Konto besitzt.
	 * @param playername
	 * @return
	 */
	public static boolean hasAccount(String playername) {
		return hasAccount(PlayerAtlas.getUUID(playername));
	}
	
	/**
	 * Erstellt ein neues Bankkonto für einen Spieler
	 * @param uuid
	 * @return
	 */
	public static boolean createAccount(UUID uuid) {
		if(hasAccount(uuid) == false) {
			File file = new File("plugins/SkyBlock/economy.yml");
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			
			cfg.set(uuid.toString()+".account.exists", true);
			cfg.set(uuid.toString()+".account.money1", Settings.getStartMoney(MoneyType.MONEY1));
			cfg.set(uuid.toString()+".account.money2", Settings.getStartMoney(MoneyType.MONEY2));
			cfg.set(uuid.toString()+".account.money3", Settings.getStartMoney(MoneyType.MONEY3));
			cfg.set(uuid.toString()+".account.money4", Settings.getStartMoney(MoneyType.MONEY4));
			cfg.set(uuid.toString()+".account.money5", Settings.getStartMoney(MoneyType.MONEY5));
			
			try {
				cfg.save(file);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}else return false;
	}
	/**
	 * Erstellt ein neues Bankkonto für einen Spieler
	 * @param uuid
	 * @return
	 */
	public static boolean createAccount(String playername) {
		return createAccount(PlayerAtlas.getUUID(playername));
	}
	
	/**
	 * Gibt die Anzahl an Geld einer Währung eines Spielers zurück
	 * @param uuid
	 * @param type
	 * @return
	 */
	public static int getMoney(UUID uuid, MoneyType type) {
		if(hasAccount(uuid)) createAccount(uuid);
		File file = new File("plugins/SkyBlock/economy.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		switch(type) {
		case MONEY1: return cfg.getInt(uuid.toString()+".account.money1");
		case MONEY2: return cfg.getInt(uuid.toString()+".account.money2");
		case MONEY3: return cfg.getInt(uuid.toString()+".account.money3");
		case MONEY4: return cfg.getInt(uuid.toString()+".account.money4");
		case MONEY5: return cfg.getInt(uuid.toString()+".account.money5");
		default: return 0;
		}
	}
	
	/**
	 * Gibt die Anzahl an Geld einer Währung eines Spielers zurück
	 * @param playername
	 * @param type
	 * @return
	 */
	public static int getMoney(String playername, MoneyType type) {
		return getMoney(PlayerAtlas.getUUID(playername), type);
	}
	
	/**
	 * Fügt einem Spieler Geld einer bestimmten Währung hinzu.
	 * Wenn subtract == false -> Geld wird Addiert.
	 * Wenn subtract == true -> Geld wird subtrahiert
	 * @param uuid
	 * @param type
	 * @param value
	 * @param subtract
	 * @param editOperation
	 * @return
	 */
	public static boolean editMoney(UUID uuid, MoneyType type, double value, EditOperation editOperation) {
		if(hasAccount(uuid)) createAccount(uuid);
		
		File file = new File("plugins/SkyBlock/economy.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		boolean hasChanged = false;
		
		switch(type) {
		case MONEY1:
			switch(editOperation) {
			case ADD:
				cfg.set(uuid.toString()+".account.money1", (cfg.getDouble(uuid.toString()+".account.money1")+value));
				hasChanged=true;
				break;
			case SUBTRACT:
				cfg.set(uuid.toString()+".account.money1", (cfg.getDouble(uuid.toString()+".account.money1")-value));
				hasChanged=true;
				break;
			case SET:
				cfg.set(uuid.toString()+".account.money1", (value));
				hasChanged=true;
				break;
			}
			break;
		case MONEY2:
			switch(editOperation) {
			case ADD:
				cfg.set(uuid.toString()+".account.money2", (cfg.getDouble(uuid.toString()+".account.money2")+value));
				hasChanged=true;
				break;
			case SUBTRACT:
				cfg.set(uuid.toString()+".account.money2", (cfg.getDouble(uuid.toString()+".account.money2")-value));
				hasChanged=true;
				break;
			case SET:
				cfg.set(uuid.toString()+".account.money2", (value));
				hasChanged=true;
				break;
			}
			break;
		case MONEY3:
			switch(editOperation) {
			case ADD:
				cfg.set(uuid.toString()+".account.money3", (cfg.getDouble(uuid.toString()+".account.money3")+value));
				hasChanged=true;
				break;
			case SUBTRACT:
				cfg.set(uuid.toString()+".account.money3", (cfg.getDouble(uuid.toString()+".account.money3")-value));
				hasChanged=true;
				break;
			case SET:
				cfg.set(uuid.toString()+".account.money3", (value));
				hasChanged=true;
				break;
			}
			break;
		case MONEY4:
			switch(editOperation) {
			case ADD:
				cfg.set(uuid.toString()+".account.money4", (cfg.getDouble(uuid.toString()+".account.money4")+value));
				hasChanged=true;
				break;
			case SUBTRACT:
				cfg.set(uuid.toString()+".account.money4", (cfg.getDouble(uuid.toString()+".account.money4")-value));
				hasChanged=true;
				break;
			case SET:
				cfg.set(uuid.toString()+".account.money4", (value));
				hasChanged=true;
				break;
			}
			break;
		case MONEY5:
			switch(editOperation) {
			case ADD:
				cfg.set(uuid.toString()+".account.money5", (cfg.getDouble(uuid.toString()+".account.money5")+value));
				hasChanged=true;
				break;
			case SUBTRACT:
				cfg.set(uuid.toString()+".account.money5", (cfg.getDouble(uuid.toString()+".account.money5")-value));
				hasChanged=true;
				break;
			case SET:
				cfg.set(uuid.toString()+".account.money5", (value));
				hasChanged=true;
				break;
			}
			break;
		}
		
		if(hasChanged) {
			try { cfg.save(file); return true; } catch (IOException e) { e.printStackTrace(); return false; }
		}else return false;
	}
	
	/**
	 * Fügt einem Spieler Geld einer bestimmten Währung hinzu
	 * @param playername
	 * @param type
	 * @param value
	 * @param editOperation
	 * @return
	 */
	public static boolean editMoney(String playername, MoneyType type, double value, EditOperation editOperation) {
		return editMoney(PlayerAtlas.getUUID(playername), type, value, editOperation);
	}
	
	public static enum EditOperation{
		SUBTRACT, ADD, SET;
	}
	
	public static enum MoneyType {
		MONEY1("SkyCoins", "C"),
		MONEY2("", ""),
		MONEY3("", ""),
		MONEY4("", ""),
		MONEY5("", "");
		
		String displayname, shortcut;
		
		MoneyType(String displayname, String shortcut) {
			this.displayname=displayname;
			this.shortcut=shortcut;
		}
		
		public String getDisplayname() { return displayname; }
		public String getShortcut() { return shortcut; }
	}
	
}
