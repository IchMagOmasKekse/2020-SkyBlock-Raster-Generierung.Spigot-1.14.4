package me.ichmagomaskekse.de;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SkyBlockGenerator {
	
	private static ConcurrentLinkedQueue<Location> locs = new ConcurrentLinkedQueue<Location>();
	private static int issize = 10; // Verhältnis 1:1 ; Angegeben in Blöcken
	private static int spaceBetweenIslands = 2; //Verhältnis 1:1 ; Angegeben in Blöcken
	public static boolean pause = false;
	public static int amountOfIslands = 65;
	static int curx = 0;
	static int curz = 0;
	
	public static boolean generateIfReady(Player executor, int amount, int islandsize, int spacebetween) {
		amountOfIslands = amount;
		issize = islandsize;
		spaceBetweenIslands = spacebetween;
		return generateIfReady(executor);
	}
	
	public static boolean generateIfReady(Player executor, int amount, int islandsize) {
		amountOfIslands = amount;
		issize = islandsize;
		return generateIfReady(executor);
	}
	
	public static boolean generateIfReady(Player executor, int amount) {
		amountOfIslands = amount;
		return generateIfReady(executor);
	}
	
	public static boolean generateIfReady(Player executor) {
		pause = false;
		curx = (int)executor.getLocation().clone().getX();
		curz = (int)executor.getLocation().clone().getY();
		int left = 1;
		int round = 0;
		int addx1 = 1; //←
		int addy1 = 1; //↓
		int addx2 = 2; //→
		int addy2 = 0; //↑
		while(left < (amountOfIslands+1) || pause == true) {
			
			if(round==0) {
				addIsland(executor, curx, curz, left);
				left++;
			}
			
			if(left < (amountOfIslands+1)) { //↑
				for(int a = 0; a != addy2; a++) {
					if(left < (amountOfIslands+1)) {						
						curz-=(issize+spaceBetweenIslands);
						addIsland(executor, curx, curz, left);
						left++;
					}else break;
				}
			}
			
			if(left < (amountOfIslands+1)) { //←
				for(int a = 0; a != addx1; a++) {
					if(left < (amountOfIslands+1)) {						
						curx-=(issize+spaceBetweenIslands);
						addIsland(executor, curx, curz, left);
						left++;
					}else break;
				}			
			}
			
			if(left < (amountOfIslands+1)) { //↓
				for(int a = 0; a != addy1; a++) {
					if(left < (amountOfIslands+1)) {						
						curz+=(issize+spaceBetweenIslands);
						addIsland(executor, curx, curz, left);
						left++;
					}else break;
				}	
			}
			
			if(left < (amountOfIslands+1)) { //→
				for(int a = 0; a != addx2; a++) {
					if(left < (amountOfIslands+1)) {
						curx+=(issize+spaceBetweenIslands);
						addIsland(executor, curx, curz, left);
						left++;
					}else break;
				}
			}
			addy2+=2; //↑
			addx1+=2; //←
			addy1+=2; //↓
			addx2+=2; //→
			round++;
		}
		if(pause == false) executor.sendMessage(">>> §aDie Simulation ist abgeschlossen. Flieg nach oben auf Höhe Y = 230 um sie zu sehen");
		else executor.sendMessage(">>> §cDie Simulation wurde abgebrochen. Es wurde gerade versucht eine 2te Simulation zu starten.");
		return true;
	}
	
	private static void addIsland(Player p, int x, int z, int id) {
		Location loc = p.getLocation().clone();
		loc.setY(230);
		loc.setX(x);
		loc.setZ(z);
		
		loc.getBlock().setType(Material.BEDROCK);
		locs.add(loc.clone());
		Location l = loc.clone();
		l.add(1, 0, 0).getBlock().setType(Material.COBBLESTONE);locs.add(l.clone());
		l.add(-2, 0, 0).getBlock().setType(Material.COBBLESTONE);locs.add(l.clone());
		l.add(1, 0, 1).getBlock().setType(Material.COBBLESTONE);locs.add(l.clone());
		l.add(0, 0, -2).getBlock().setType(Material.COBBLESTONE);locs.add(l.clone());
	}
	
	public static void undo() {
		for(Location loc : locs) {			
			loc.getBlock().setType(Material.AIR);
		}
	}
	
}
