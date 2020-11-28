package me.dreamisland.de.schematics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import me.dreamisland.de.Cuboid;
import me.dreamisland.de.SkyBlock;

public class IslandPaster {
	
	private static boolean grow_flora = true;
	private static int dirt_layers = 3;
	private static int stone_layers = 6;
	private static int total_layers = 0;
	private static int island_size = 10;
	private static Random random = new Random();
	private static HashMap<Material, Integer> ores = new HashMap<Material, Integer>(); // Material & Maximale Adern
	private static HashMap<Material, Integer> flora = new HashMap<Material, Integer>(); // Material & Chance
	
	public IslandPaster() {
		total_layers = (dirt_layers+stone_layers);
		
		ores.put(Material.IRON_ORE, (int)((island_size*island_size*total_layers) / 64));
		ores.put(Material.COAL_ORE, (int)((island_size*island_size*total_layers) / 32));
		
		flora.put(Material.POPPY, 15);
		flora.put(Material.GRASS, 80);
		flora.put(Material.TALL_GRASS, 40);
		flora.put(Material.DANDELION, 20);
		flora.put(Material.OAK_SAPLING, 15);
	}
	
	public static boolean pasteIsland(Player p, Location loc, boolean isGameplay) {
		int step = 0;
		/**
		 * 0 = Stone-Layer, 1 = Dirt-Layer, 2 = Grass-Layer, 3 = Ore-Layer, 4 = Tree-Spawner, 5 = Tree&Chest-Layer, 6 = Flora-Layer
		 */
		
		World world = loc.getWorld();
		
		Cuboid c = new Cuboid(new Location(world, loc.getX()-(island_size/2), loc.getY()-total_layers, loc.getZ()-(island_size/2)), new Location(world, loc.getX()+(island_size/2), loc.getY(), loc.getZ()+(island_size/2)));
		
		Location l = null;
		
		if(isGameplay) p.teleport(c.getCenter());
		
		switch(step) {
		case 0:
			/* Stone-Layer */
			
			SkyBlock.sendMessage("Generiere Stein-Ebene...");
			
			fill(c.getPoint1(), c.getPoint2(), Material.STONE, 100);
			
			step = 1;
		case 1:
			
			/* Dirt-Layer */
			
			SkyBlock.sendMessage("Generiere Dirt-Ebene...");
			
			l = c.getPoint1();
			l.setY(c.getPoint2().getY()-(dirt_layers-1));
			
			fill(l.clone(), c.getPoint2().add(0,-1,0), Material.DIRT, 100);
			
			l.setY(c.getPoint2().getY()-(dirt_layers));
			
			fill(l.clone(), c.getPoint2().add(0,-(dirt_layers-1),0), Material.DIRT, 75);
			
			step = 2;
		case 2:
			
			/* Grass-Layer */
			
			SkyBlock.sendMessage("Generiere Grassblock-Ebene...");
			
			l = c.getPoint1();
			l.setY(c.getPoint2().getY());
			
			fill(l.clone(), c.getPoint2(), Material.GRASS_BLOCK, 100);
			
			step = 3;
		case 3:
			
			/* Ore-Layer */
			
			SkyBlock.sendMessage("Generiere Erze...");

			for(Material mat : ores.keySet()) {
				for(int i = 0; i != ores.get(mat); i++) {
					createVane(mat, c, Material.STONE, (random.nextInt(8)+1));
					
				}
			}
			
			
			step = 4;
		case 4:
			
			/* Flora-Layer */
			
			if(grow_flora) {
				SkyBlock.sendMessage("Generiere Flora...");
				
				int chance_for_nothing = 10; // Chance dass Anstatt Pflanzen halt Luft platziert wird.
				
				generateFlora(c.getPoint1(), c.getPoint2(), chance_for_nothing);
				
			}else SkyBlock.sendMessage("§eFlora Generation übersprungen...");
			
			step = 5;
		case 5:
			
			/* Tree Spawner */
			
			l = loc.clone();
			switch(random.nextInt(4)) {
			case 0:
				l.add(2,1,0).getBlock().setType(Material.BEDROCK);
				break;
			case 1:
				l.add(0,1,2).getBlock().setType(Material.BEDROCK);
				break;
			case 2:
				l.add(-2,1,0).getBlock().setType(Material.BEDROCK);
				break;
			case 3:
				l.add(0,1,-2).getBlock().setType(Material.BEDROCK);				
				break;
				default:
				break;
			}
			
			step=6;
		case 6:
			
			/* Tree&Chest-Layer */
			
			SkyBlock.sendMessage("Generiere Baum-Kisten-Ebene...");
			
			ArrayList<Block> scanned = scanFor(c, "BEDROCK");
			
			if(scanned.isEmpty() == false) {				
				for(Block b : scanned) {
					b.setType(Material.AIR);
					world.generateTree(b.getLocation(), TreeType.BIG_TREE);
				}
			}
			
			scanned = scanFor(c, "SAPLING");
			
			if(scanned.isEmpty() == false) {				
				for(Block b : scanned) {
					b.setType(Material.AIR);
					world.generateTree(b.getLocation(), TreeType.TREE);
				}
			}
			
			l = loc.clone();
			switch(random.nextInt(4)) {
			case 0:
				l.add(1,1,0).getBlock().setType(Material.CHEST);
				break;
			case 1:
				l.add(0,1,1).getBlock().setType(Material.CHEST);
				break;
			case 2:
				l.add(-1,1,0).getBlock().setType(Material.CHEST);
				break;
			case 3:
				l.add(0,1,-1).getBlock().setType(Material.CHEST);				
				break;
				default:
				break;
			}
			
			
			
			step+=1;
			default:
				SkyBlock.sendMessage("§aGeneration abgeschlossen.");
		}
		return true;
	}
	
	/**
	 * Scannt eine Region und gibt eine ArrayList<Block> mit den Blöcken zurück, die das gesuchte Material haben.
	 * @param c
	 * @param name_containment
	 * @return
	 */
	public static ArrayList<Block> scanFor(Cuboid c, String name_containment) {
		
		ArrayList<Block> scanned = new ArrayList<Block>();
		
		int x1,y1,z1,x2,y2,z2;
		
		x1 = c.getPoint1().getBlockX();
		y1 = c.getPoint2().getBlockY();
		z1 = c.getPoint1().getBlockZ();
		x2 = c.getPoint2().getBlockX();
		y2 = c.getPoint2().getBlockY()+2;
		z2 = c.getPoint2().getBlockZ();
		
		Location point = new Location(c.getPoint1().getWorld(), 0,0,0);
		
		for(int yy1 = y1; yy1 < y2; yy1++) {
			for(int xx1 = x1; xx1 < x2; xx1++) {
				for(int zz1 = z1; zz1 < z2; zz1++) {
					point.setX(xx1);
					point.setY(yy1);
					point.setZ(zz1);

					if(point.getBlock().getType().toString().contains(name_containment)) {
						scanned.add(point.clone().getBlock());
					}
				}
				
			}
			
		}
		
		return scanned;
	}
	
	/**
	 * Füllt eine Region mit einem beliebigen Material.
	 * @param pos1
	 * @param pos2
	 * @param mat
	 */
	public static void fill(Location pos1, Location pos2, Material mat, int chance_to_place) {
		int x1,y1,z1,x2,y2,z2;
		
		
		Cuboid c = new Cuboid(pos1, pos2);
		
		x1 = c.getPoint1().getBlockX();
		y1 = c.getPoint1().getBlockY();
		z1 = c.getPoint1().getBlockZ();
		x2 = c.getPoint2().getBlockX();
		y2 = c.getPoint2().getBlockY()+1/* +1 = Flora-Layer */;
		z2 = c.getPoint2().getBlockZ();
		
		Location point = new Location(pos1.getWorld(), 0,0,0);
		
		for(int yy1 = y1; yy1 < y2; yy1++) {
			for(int xx1 = x1; xx1 < x2; xx1++) {
				for(int zz1 = z1; zz1 < z2; zz1++) {
					if(random.nextInt(100) <= chance_to_place) {						
						point.setX(xx1);
						point.setY(yy1);
						point.setZ(zz1);
						point.getBlock().setType(mat);
					}
				}
				
			}
		}
	}
	
	/**
	 * Generiert eine Flora.
	 * @param loc
	 * @param chance_for_nothing
	 */
	public static void generateFlora(Location pos1, Location pos2, int chance_for_nothing) {
		int x1,z1,x2,z2;
		
		
		Cuboid c = new Cuboid(pos1, pos2);
		
		x1 = c.getPoint1().getBlockX();
		z1 = c.getPoint1().getBlockZ();
		x2 = c.getPoint2().getBlockX();
		z2 = c.getPoint2().getBlockZ();
		
		Location point = new Location(pos1.getWorld(), 0,0,0);
		
		int r = random.nextInt(100);
		int offset = 0;
		
		for(int xx1 = x1; xx1 < x2; xx1++) {
			for(int zz1 = z1; zz1 < z2; zz1++) {
				point.setX(xx1);
				point.setZ(zz1);
				while(point.getWorld().getHighestBlockAt(point.add(0,-offset,0)).getType() != Material.GRASS_BLOCK) {
					offset++;
					if(offset == 15) break;
				}
				point.setY(point.getWorld().getHighestBlockYAt(point.add(0,-offset,0))+1);
				
				
				
				for(Material mat : flora.keySet()) {
					
					if(random.nextInt(100) <= 100) {
						
						r = random.nextInt(100);
						
						if(r <= chance_for_nothing) {
							
							/* Luft soll platziert werden */
							point.getBlock().setType(Material.AIR);
						}else if(r <= flora.get(mat)) {
							
							point.getBlock().setType(mat);
						}
					}
				}
			}
			
		}
	}
	
	/**
	 * Erstellt Material-Venen in maximaler Größer von 8.
	 * Wird meistens für Erze-Generierung genutzt.
	 * @param ore
	 * @param location
	 * @param to_replace
	 * @param vane_size
	 */
	public static void createVane(Material ore, Cuboid c, Material to_replace, int vane_size) {
		if(vane_size > 8 ) vane_size = 8;
		Location location = c.getRandomLocation();
		switch(vane_size) {
		case 0:
			return;
		case 1:
			if(location.getBlock().getType() == to_replace) location.getBlock().setType(ore);
			break;
		case 2:
			if(location.getBlock().getType() == to_replace) location.getBlock().setType(ore);
			if(location.clone().add(1,0,0).getBlock().getType() == to_replace) location.clone().add(1,0,0).getBlock().setType(ore);
			break;
		case 3:
			if(location.getBlock().getType() == to_replace) location.getBlock().setType(ore);
			if(location.clone().add(1,0,0).getBlock().getType() == to_replace) location.clone().add(1,0,0).getBlock().setType(ore);
			if(location.clone().add(1,0,1).getBlock().getType() == to_replace) location.clone().add(1,0,1).getBlock().setType(ore);
			break;
		case 4:
			if(location.getBlock().getType() == to_replace) location.getBlock().setType(ore);
			if(location.clone().add(1,0,0).getBlock().getType() == to_replace) location.clone().add(1,0,0).getBlock().setType(ore);
			if(location.clone().add(1,0,1).getBlock().getType() == to_replace) location.clone().add(1,0,1).getBlock().setType(ore);
			if(location.clone().add(0,0,1).getBlock().getType() == to_replace) location.clone().add(0,0,1).getBlock().setType(ore);
			break;
		case 5:
			if(location.getBlock().getType() == to_replace) location.getBlock().setType(ore);
			if(location.clone().add(1,0,0).getBlock().getType() == to_replace) location.clone().add(1,0,0).getBlock().setType(ore);
			if(location.clone().add(1,0,1).getBlock().getType() == to_replace) location.clone().add(1,0,1).getBlock().setType(ore);
			if(location.clone().add(0,0,1).getBlock().getType() == to_replace) location.clone().add(0,0,1).getBlock().setType(ore);
			if(location.clone().add(0,1,0).getBlock().getType() == to_replace) location.clone().add(0,1,0).getBlock().setType(ore);
			break;
		case 6:
			if(location.getBlock().getType() == to_replace) location.getBlock().setType(ore);
			if(location.clone().add(1,0,0).getBlock().getType() == to_replace) location.clone().add(1,0,0).getBlock().setType(ore);
			if(location.clone().add(1,0,1).getBlock().getType() == to_replace) location.clone().add(1,0,1).getBlock().setType(ore);
			if(location.clone().add(0,0,1).getBlock().getType() == to_replace) location.clone().add(0,0,1).getBlock().setType(ore);
			if(location.clone().add(0,1,0).getBlock().getType() == to_replace) location.clone().add(0,1,0).getBlock().setType(ore);
			if(location.clone().add(1,1,0).getBlock().getType() == to_replace) location.clone().add(1,1,0).getBlock().setType(ore);
			break;
		case 7:
			if(location.getBlock().getType() == to_replace) location.getBlock().setType(ore);
			if(location.clone().add(1,0,0).getBlock().getType() == to_replace) location.clone().add(1,0,0).getBlock().setType(ore);
			if(location.clone().add(1,0,1).getBlock().getType() == to_replace) location.clone().add(1,0,1).getBlock().setType(ore);
			if(location.clone().add(0,0,1).getBlock().getType() == to_replace) location.clone().add(0,0,1).getBlock().setType(ore);
			if(location.clone().add(0,1,0).getBlock().getType() == to_replace) location.clone().add(0,1,0).getBlock().setType(ore);
			if(location.clone().add(1,1,0).getBlock().getType() == to_replace) location.clone().add(1,1,0).getBlock().setType(ore);
			if(location.clone().add(0,1,1).getBlock().getType() == to_replace) location.clone().add(0,1,1).getBlock().setType(ore);
			break;
		case 8:
			if(location.getBlock().getType() == to_replace) location.getBlock().setType(ore);
			if(location.clone().add(1,0,0).getBlock().getType() == to_replace) location.clone().add(1,0,0).getBlock().setType(ore);
			if(location.clone().add(1,0,1).getBlock().getType() == to_replace) location.clone().add(1,0,1).getBlock().setType(ore);
			if(location.clone().add(0,0,1).getBlock().getType() == to_replace) location.clone().add(0,0,1).getBlock().setType(ore);
			if(location.clone().add(0,1,0).getBlock().getType() == to_replace) location.clone().add(0,1,0).getBlock().setType(ore);
			if(location.clone().add(1,1,0).getBlock().getType() == to_replace) location.clone().add(1,1,0).getBlock().setType(ore);
			if(location.clone().add(0,1,1).getBlock().getType() == to_replace) location.clone().add(0,1,1).getBlock().setType(ore);
			if(location.clone().add(1,1,1).getBlock().getType() == to_replace) location.clone().add(1,1,1).getBlock().setType(ore);
			break;
		}
	}

	
}
