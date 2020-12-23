package me.skytale.de.schematics;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import me.skytale.de.SkyBlockGenerator;

public class SkyRect {
	
	private Location pos1 = null;
	private Location pos2 = null;
	private World world = null;
	private double xMin = 0;
	private double yMin = 0;
	private double zMin = 0;
	private double xMax = 0;
	private double yMax = 0;
	private double zMax = 0;
	
	public SkyRect(Location pos1, Location pos2) {
		this.pos1 = getPos1OrPos2(pos1.clone(), pos2.clone(), true);
		this.pos2 = getPos1OrPos2(pos1.clone(), pos2.clone(), false);
		world = pos1.getWorld();
		this.xMin = getCorner1().getX();
		this.yMin = getCorner1().getY();
		this.zMin = getCorner1().getZ();
		this.xMax = getCorner7().getX();
		this.yMax = getCorner7().getY();
		this.zMax = getCorner7().getZ();
	}
	
	public World getWorld() {
		return world;
	}
	
	
	/*
	 * TODO: isIn() gibt zurück, über eine Location sich innerhalb des SkyRects befindet
	 */
	public boolean isIn(Location loc) {
		if(loc.getWorld() == world) {
			
			if(loc.getX() >= xMin && loc.getX() <= xMax) {
				
				if(loc.getY() >= yMin && loc.getY() <= yMax) {
					
					if(loc.getZ() >= zMin && loc.getZ() <= zMax) {
						
						return true;
					}	
				}	
			}
		}
		return false;
	}
	
	public Location getCorner1() {
		return getLNW().clone();
	}
	public Location getCorner2() {
		return getLNE().clone();
	}
	public Location getCorner3() {
		return getLSE().clone();
	}
	public Location getCorner4() {
		return getLSW().clone();
	}
	public Location getCorner5() {
		return getUNW().clone();
	}
	public Location getCorner6() {
		return getUNE().clone();
	}
	public Location getCorner7() {
		return getUSE().clone();
	}
	public Location getCorner8() {
		return getUSW().clone();
	}
	
	public Location getLNE() {
		return new Location(pos1.getWorld(), pos2.getX(), pos1.getY(), pos1.getZ());	
	}
	public Location getUNE() {
		return new Location(pos1.getWorld(), pos2.getX(), pos2.getY(), pos1.getZ());	
	}
	
	public Location getLSE() {
		return new Location(pos1.getWorld(), pos2.getX(), pos1.getY(), pos2.getZ());	
	}
	public Location getUSE() {
		return pos2.clone();
	}
	
	public Location getLSW() {
		return new Location(pos1.getWorld(), pos1.getX(), pos1.getY(), pos2.getZ());	
	}
	public Location getUSW() {
		return new Location(pos1.getWorld(), pos1.getX(), pos2.getY(), pos2.getZ());	
	}

	public Location getLNW() {
		return pos1.clone();
	}
	public Location getUNW() {
		return new Location(pos1.getWorld(), pos1.getX(), pos2.getY(), pos1.getZ());	
	}
	
	public static Location getPos1OrPos2(Location l1, Location l2, boolean selectPos1) {
		Location l = null;
		
		int x1 = (int) l1.getX();
		int y1 = (int) l1.getY();
		int z1 = (int) l1.getZ();
		
		int x2 = (int) l2.getX();
		int y2 = (int) l2.getY();
		int z2 = (int) l2.getZ();
		
		int fX = 0;
		int fY = 0;
		int fZ = 0;
		
		if(selectPos1) {			
			if(x1 > x2) fX = x2;
			else fX = x1;
			
			if(y1 > y2) fY = y2;
			else fY = y1;
			
			if(z1 > z2) fZ = z2;
			else fZ = z1;
		}else {
			if(x1 < x2) fX = x2;
			else fX = x1;
			
			if(y1 < y2) fY = y2;
			else fY = y1;
			
			if(z1 < z2) fZ = z2;
			else fZ = z1;
		}
		
		l = new Location(Bukkit.getWorld(SkyBlockGenerator.skyworldName), fX, fY, fZ);
		
		return l;
	}
	
}
