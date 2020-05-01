package me.dreamisland.de.schematics;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.dreamisland.de.Prefixes;
import me.dreamisland.de.SkyBlockGenerator;
import me.dreamisland.de.schematics.SchematicManager.SchematicProfile;

public class SchematicReaderAndWriter {
	
	private static boolean debug = false;
	public static HashMap<SchematicProfile, ConcurrentHashMap<Location, Block>> contents = new HashMap<SchematicProfile, ConcurrentHashMap<Location, Block>>();
	
	public SchematicReaderAndWriter() {
		
	}

	/*
	 * TODO: read() ließt die Blöcke aus der Welt aus und speichert diese in eine ConcurrentHashMap<> die dann in die HashMap 'contents' hinzugefügt wird.
	 *       Dies ist eine Art 'Session' die man immer wieder abrufen kann
	 */
	public static ConcurrentHashMap<Location, Block> copy(SchematicProfile profile, Location pos1, Location pos2, boolean withAir) {
		int xSize = 0;
		int ySize = 0;
		int zSize = 0;
		ConcurrentHashMap<Location, Block> blocks = new ConcurrentHashMap<Location, Block>();
		xSize = (int) (pos1.getX() - pos2.getX());
		ySize = (int) (pos1.getY() - pos2.getY());
		zSize = (int) (pos1.getZ() - pos2.getZ());
		if(xSize < 0) xSize *= (-1);
		if(ySize < 0) ySize *= (-1);
		if(zSize < 0) zSize *= (-1);
		if(ySize == 0) ySize = 1;
		xSize+=1;
		ySize+=1;
		zSize+=1;
		SkyRect rect = new SkyRect(pos1, pos2);
		Block b = null;
		for(int z = 0; z != zSize; z++) {
			for(int x = 0; x != xSize; x++) {
				for(int y = 0; y != ySize; y++) {
					
					b = rect.getCorner1().add(x,y,z).getBlock();
					if(withAir == false && (b.getType() == Material.AIR || b.getType() == Material.CAVE_AIR)) {
						
					}else {
						if(debug == false) {
							blocks.put(rect.getCorner1().add(x,y,z), b);
						}else {
							b.setType(Material.OBSIDIAN);
						}						
					}
				}
			}
		}
		if(contents.containsKey(profile)) contents.remove(profile);
		contents.put(profile, blocks);
		return blocks;
	}
	
	/*
	 * TODO: save() speichert die Blöcke aus der Session in eine Datei
	 */
	public static boolean save(SchematicProfile profile, String filename, boolean overwrite) {
		File file = new File("plugins/SkyBlock/Schematics/Saved/"+filename+".yml");
		if(file.exists()) {
			if(overwrite == false) {
				profile.getHost().sendMessage(Prefixes.SCHEMATIC.getPrefix()+"§cDiese Schematic existiert bereits");
				return false;
			}
		}
		if(contents.containsKey(profile) == false){
			profile.getHost().sendMessage(Prefixes.SCHEMATIC.getPrefix()+"§cKein Bereich kopiert");
			return false;
		}else {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			
			ConcurrentHashMap<Location, Block> blocks = contents.get(profile);
			int counter = 0;
			
			cfg.set("Schematic.Name", profile.saved_schematic_name);
			cfg.set("Schematic.Total Of Blocks", counter);
			cfg.set("Schematic.Sizing.xWidth", profile.selected_cuboid.getXWidth());
			cfg.set("Schematic.Sizing.height", profile.selected_cuboid.getHeight());
			cfg.set("Schematic.Sizing.zWidth", profile.selected_cuboid.getZWidth());
			
			int xDiff = 0;
			int yDiff = 0;
			int zDiff = 0;
			
			SkyRect rect = new SkyRect(profile.getPos1(), profile.getPos2());
			
			for(Location loc : blocks.keySet()) {
				xDiff = (int)(loc.getX()-rect.getCorner1().getX());
				yDiff = (int)(loc.getY()-rect.getCorner1().getY());
				zDiff = (int)(loc.getZ()-rect.getCorner1().getZ());
				
				writeBlockToFile(cfg, xDiff, yDiff, zDiff, blocks.get(loc), counter);
				counter++;
			}
			cfg.set("Schematic.Total Of Blocks", counter);
			try {
				cfg.save(file);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
	}
	
	/*
	 * TODO: readSchematic() liest eine Schematic aus und gibt eine HashMap mit dem Content zurück
	 */
	public static ConcurrentHashMap<Location, BlockProfile> readSchematic(String name) {
		File file = new File("plugins/SkyBlock/Schematics/Saved/"+name+".yml");
		if(file.exists() == false) return null;
		else {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			ConcurrentHashMap<Location, BlockProfile> blocks = new ConcurrentHashMap<Location, BlockProfile>();
			int size = cfg.getInt("Schematic.Total Of Blocks");
			String mat = "";
			Material m = Material.AIR;
			World world = Bukkit.getWorld(SkyBlockGenerator.skyworldName);
			Location l = null;
			for(int i = 0; i != size; i++) {
				mat = cfg.getString("Schematic.Content.B-"+i+".type");
				try{
					m = Material.valueOf(mat);
				}catch(IllegalArgumentException ex1) {
					try{
						m = Material.valueOf("LEGACY_"+mat);
					}catch(IllegalArgumentException ex2) {
						m = Material.AIR;
					}
				}
				
				l = new Location(world,
						cfg.getInt("Schematic.Content.B-"+i+".Location.xDiff"),
						cfg.getInt("Schematic.Content.B-"+i+".Location.yDiff"),
						cfg.getInt("Schematic.Content.B-"+i+".Location.zDiff"));
				
				blocks.put(l.clone(), new BlockProfile(m, l.clone(), m.createBlockData()));
			}
			return blocks;
		}
	}
	
	/*
	 * TODO: writeBlockToFile() schreibt einen Block in eine übergebene FileConfiguration
	 */
	public static FileConfiguration writeBlockToFile(FileConfiguration cfg, int x, int y, int z, Block b, int counter) {
		cfg.set("Schematic.Content.B-"+counter+".Location.xDiff", x);
		cfg.set("Schematic.Content.B-"+counter+".Location.yDiff", y);
		cfg.set("Schematic.Content.B-"+counter+".Location.zDiff", z);
		
		cfg.set("Schematic.Content.B-"+counter+".type", b.getType().toString());
		cfg.set("Schematic.Content.B-"+counter+".metadata", b.getMetadata("snowy"));
		cfg.set("Schematic.Content.B-"+counter+".biome", Biome.FOREST);
		return cfg;
	}
	
	public static Location getEcke(Location l1, Location l2) {
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
		
		if(x1 > x2) fX = x2;
		else fX = x1;
		
		if(y1 > y2) fY = y2;
		else fY = y1;
		
		if(z1 > z2) fZ = z2;
		else fZ = z1;
		
		Bukkit.broadcastMessage("x1/y1/z1: "+x1+"/"+y1+"/"+z1);
		Bukkit.broadcastMessage("x2/y2/z2: "+x2+"/"+y2+"/"+z2);
		Bukkit.broadcastMessage("fX/fY/fZ: "+fX+"/"+fY+"/"+fZ);
		
		l = new Location(Bukkit.getWorld(SkyBlockGenerator.skyworldName), fX, fY, fZ);
		l.getBlock().setType(Material.COAL_BLOCK);
		
		return l;
	}
	
	public static class BlockProfile {
		
		public Material mat = null;
		public Location location = null;
		public BlockData data = null;
		
		public BlockProfile(Material mat, Location location, BlockData data) {
			this.mat = mat;
			this.location = location;
			this.data = data;
		}
		
	}
	
}
