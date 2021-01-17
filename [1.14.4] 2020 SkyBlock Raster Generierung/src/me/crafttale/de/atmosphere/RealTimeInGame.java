package me.crafttale.de.atmosphere;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import me.crafttale.de.SkyBlock;

public class RealTimeInGame {
	
	public static boolean enabled = true;
//	private static BukkitRunnable timer;
	public static long time = 0;
	
	public RealTimeInGame() { }
	
	private static String hours = "";
	private static String minutes = "";
	private static String seconds = "";
	private static double h = 0.00d;
	private static double m = 0.00d;
	private static double s = 0.00d;
	private static boolean reverse = false;
	private static boolean check = false;
	public static void updateTime() {
		hours = SkyBlock.getCurrentHours();
		minutes = SkyBlock.getCurrentMinutes();
		seconds = SkyBlock.getCurrentSeconds();
		h = Double.parseDouble(hours);
		m = Double.parseDouble(minutes);
		s = Double.parseDouble(seconds);
		time = (long)(((h*1000)+(m*10)+(s*0.1)));
		if(h == 0 && m == 0 && check) {
			reverse = !reverse;
			check = false;
		}
		else if(check == false && h == 0 && m == 1) check = true;
		time = time-9000;
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(reverse) Bukkit.getWorld("world").setTime((24000-time));
				else Bukkit.getWorld("world").setTime(time);
//				Bukkit.getWorld("world").setTime(time);
			}
		}.runTask(SkyBlock.getSB());
		
	}
	
	
}
