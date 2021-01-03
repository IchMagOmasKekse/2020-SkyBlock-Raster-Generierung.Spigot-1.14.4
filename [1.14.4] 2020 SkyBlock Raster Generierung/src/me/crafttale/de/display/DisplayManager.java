package me.crafttale.de.display;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import me.crafttale.de.SkyBlock;
import me.crafttale.de.profiles.log.XLogger;
import me.crafttale.de.profiles.log.XLogger.LogType;

public class DisplayManager extends BukkitRunnable {
	
	public static Random ran = new Random();
	public static ConcurrentHashMap<Integer, Display> displays = new ConcurrentHashMap<Integer, Display>();
	private static boolean loaded = false;
	
	public DisplayManager() {
		loadDisplays();
		runTaskTimer(SkyBlock.getSB(), 0, 1l);
	}
	
	@Override
	public void run() {
		for(int i : displays.keySet()) {
			displays.get(i).update();
		}
	}
	
	public static boolean registerDisplay(Display display) {
		if(displays.containsKey(display.id)) return false;
		else displays.put(display.id, display);
		return true;
	}
	
	public static void loadDisplays() {
		XLogger.log(LogType.PluginInternProcess, "Lade Displays...");
		if(loaded) return;
		
		Display display = new SkyBlockPortalDisplay(new Location(Bukkit.getWorld("skyblockworld"), 37.5, 84, 75.5));
		displays.put(display.id, display);
		
		display = new LootChestDisplay(new Location(Bukkit.getWorld("skyblockworld"), 4.5, 87, 61.5));
		displays.put(display.id, display);
		
		display = new LootChestDisplay(new Location(Bukkit.getWorld("skyblockworld"), 4.5, 87, 89.5));
		displays.put(display.id, display);
		
		display = new UniversalDisplay("§bBoost-Pad", new Location(Bukkit.getWorld("skyblockworld"), 36.5, 84, 85.5));
		displays.put(display.id, display);
		
		display = new UniversalDisplay("§bBoost-Pad", new Location(Bukkit.getWorld("skyblockworld"), 36.5, 84, 65.5));
		displays.put(display.id, display);
		
		display = new UniversalDisplay("§cPVP Arena", new Location(Bukkit.getWorld("skyblockworld"), 20.5, 84, 51.5));
		displays.put(display.id, display);
				
		loaded = true;
	}
	
	public static void removeAll() {
		XLogger.log(LogType.PluginInternProcess, "Lösche alle Displays...");
		for(Display d : displays.values()) d.disable();
		
		loaded = false;
	}
	
	public static int createNewId() {
		return ran.nextInt(1000);
	}
	
}
