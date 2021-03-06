package me.crafttale.de;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SkyBlockGenerator {
	
	private static ConcurrentLinkedQueue<Location> locs = new ConcurrentLinkedQueue<Location>();
	public static int issize = 200; // Verhältnis 1:1 ; Angegeben in Blöcken(Durchmesser)
	public static int amountOfIslands = 2500;
	public static int spaceBetweenIslands = 25; //Verhältnis 1:1 ; Angegeben in Blöcken
	public static int default_issize = 400; //NICHT VERÄNDERN! // Verhältnis 1:1 ; Angegeben in Blöcken
	public static int default_amountOfIslands = 500; //NICHT VERÄNDERN!
	public static int default_spaceBetweenIslands = 200; //NICHT VERÄNDERN!//Verhältnis 1:1 ; Angegeben in Blöcken
	public static boolean pause = false;
	static int curx = 0;
	static int curz = 0;
	public static int islandHeight = 72;
	public static String skyworldName = SkyWorld.skyblockworld;
	static Location curLoc = null;
	
	public SkyBlockGenerator() {
		curLoc = new Location(Bukkit.getWorld(skyworldName), curx, islandHeight, curz);
	}	
	public static boolean generateIfReady(int amount, int islandsize, int spacebetween) {
		default_amountOfIslands = amount;
		default_issize = islandsize;
		default_spaceBetweenIslands = spacebetween;
		return generateIfReady();
	}
	
	public static boolean generateIfReady(int amount, int islandsize) {
		default_amountOfIslands = amount;
		default_issize = islandsize;
		return generateIfReady();
	}
	
	public static boolean generateIfReady(int amount) {
		default_amountOfIslands = amount;
		return generateIfReady();
	}
	
	
	/**
	 * Generiert die Insel-Index-File.yml
	 * @return
	 */
	public static boolean generateIfReady() {
		World world = Bukkit.getWorld(skyworldName);
		if(world == null) {
			for(Player p : Bukkit.getOnlinePlayers()) p.sendMessage("Die Welt ist noch nicht vorhanden, obwohl sie Generiert wurde... lol");
		}
		pause = false;
		curx = 0;
		curz = 0;
		curLoc = new Location(world, curx, 72, curz);
		int left = 1;
		int round = 0;
		int addx1 = 1; //â†§
		int addy1 = 1; //â†“
		int addx2 = 2; //â†’
		int addy2 = 0; //â†‘
		while(left < (amountOfIslands+1) || pause == true) {
			
			if(round==0) {
				addIsland(curx, curz, left);
				left++;
			}
			
			if(left < (amountOfIslands+1)) { //â†‘
				for(int a = 0; a != addy2; a++) {
					if(left < (amountOfIslands+1)) {						
						curz-=(issize+spaceBetweenIslands);
						addIsland(curx, curz, left);
						left++;
					}else break;
				}
			}
			
			if(left < (amountOfIslands+1)) { //â†§
				for(int a = 0; a != addx1; a++) {
					if(left < (amountOfIslands+1)) {						
						curx-=(issize+spaceBetweenIslands);
						addIsland(curx, curz, left);
						left++;
					}else break;
				}			
			}
			
			if(left < (amountOfIslands+1)) { //â†“
				for(int a = 0; a != addy1; a++) {
					if(left < (amountOfIslands+1)) {						
						curz+=(issize+spaceBetweenIslands);
						addIsland(curx, curz, left);
						left++;
					}else break;
				}	
			}
			
			if(left < (amountOfIslands+1)) { //â†’
				for(int a = 0; a != addx2; a++) {
					if(left < (amountOfIslands+1)) {
						curx+=(issize+spaceBetweenIslands);
						addIsland(curx, curz, left);
						left++;
					}else break;
				}
			}
			addy2+=2; //â†‘
			addx1+=2; //â†§
			addy1+=2; //â†“
			addx2+=2; //â†’
			round++;
		}
		return true;
	}
	
	private static void addIsland(int x, int z, int id) {
		
		curLoc.setY(islandHeight);
		curLoc.setX(x);
		curLoc.setZ(z);
		
		locs.add(curLoc.clone());
	}
	
	public static void undo() {
		for(Location loc : locs) {			
			loc.getBlock().setType(Material.AIR);
		}
	}
	
}
