package me.crafttale.de.economy;

import java.io.File;
import java.util.HashMap;

import me.crafttale.de.SkyBlock;
import me.crafttale.de.log.XLogger;
import me.crafttale.de.log.XLogger.LogType;

public class ShopIO {
	
	/*                    ShopName          Slot im Inv.    Inhalt des Shops   */
	public static HashMap< String,   HashMap< Integer,          ShopItem>> shops = new HashMap<String, HashMap<Integer, ShopItem>>(); 
	
	public ShopIO() {
		reloadShops();
	}
	
	public static void reloadShops() {
		File[] files = new File("plugins/" + SkyBlock.getSB().getDescription().getName() + "/Shops/").listFiles();
		if(files.length == 0) {
			XLogger.log(LogType.PluginInternProcess, "Die Shops konnten nicht gereloaded werden, da keine SHop Dateien vorhanden sind!");
			return;
		}
		/**
		 * 
		 * TODO: SHOPS LADEN:
		 * 
		 */
	}
	
	
	public static class ShopItem {
		
		
	}
}
