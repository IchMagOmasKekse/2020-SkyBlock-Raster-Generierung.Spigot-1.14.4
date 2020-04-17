package me.ichmagomaskekse.de.schematics;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.ichmagomaskekse.de.Cuboid;
import me.ichmagomaskekse.de.Prefixes;
import me.ichmagomaskekse.de.SkyBlock;

public class SchematicManager implements Listener {
	
	private static HashMap<Player, SchematicProfile> profiles = new HashMap<Player, SchematicProfile>();
	public static final String schematic_path = getSchematicPath();
	
	public SchematicManager() {
		SkyBlock.getInstance().getServer().getPluginManager().registerEvents(this, SkyBlock.getInstance());
	}
	/*
	 * TODO: Gibt den Pfad der Gespeicherten Schematic-Files zurück
	 */
	public static String getSchematicPath() {
		return "plugins/SkyBlock/Schematics/Saved/";
	}
	
	/*
	 * TODO: Gibt das Aktuelle Schematic Profil eines Spielers zurück
	 */
	public static SchematicProfile getProfile(Player p) {
		if(profiles.containsKey(p)) return profiles.get(p);
		else return registerProfile(p);
	}
	
	/*
	 * TODO: Registrier ein SchematicProfil und gibt dieses zurück
	 */
	public static SchematicProfile registerProfile(Player p) {
		if(profiles.containsKey(p) == false) profiles.put(p, new SchematicProfile(p));
		return profiles.get(p);
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		SchematicProfile profile = SchematicManager.getProfile(p);
		if(e.getItem() != null && e.getItem().getType() == Material.GOLDEN_AXE && p.hasPermission("skyblock.schematic.useaxe")) {			
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				e.setCancelled(true);
				profile.setPos2(e.getClickedBlock().getLocation());
				p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Position §72 §ewurde gesetzt");
			}else if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
				e.setCancelled(true);
				profile.setPos1(e.getClickedBlock().getLocation());
				p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Position §71 §ewurde gesetzt");
			}
		}
	}
	
	public static class SchematicProfile {
		
		private Player p = null;
		private String loaded_schematic_name = "";
		private Schematic schematic = null;
		
		private Location pos1, pos2; //Pos1 und Pos2 sind die beiden diagonalen Ecken der Schematic Region
		
		public SchematicProfile(Player p) {
			this.p = p;
			this.schematic = new Schematic("Unnamed", this);
		}
		
		/*
		 * TODO: Gibt die Schematic zurück
		 */
		public Schematic getSchematic() {
			return schematic;
		}
		
		/*
		 * TODO: Gibt zurück, ob zwei Locations ausgewählt wurden
		 */
		public boolean readyTocopy() {
			if(pos1 != null && pos2 != null) return true;
			else return false;
		}
		
		/*
		 * TODO: Setzt und Gibt die Pos1 und Pos2 zurück
		 */
		public Location getPos1() {
			return pos1;
		}
		public Location getPos2() {
			return pos2;
		}
		public void setPos1(Location pos1) {
			this.pos1 = pos1;
		}
		public void setPos2(Location pos2) {
			this.pos2 = pos2;
		}
		
		/*
		 * TODO: Gibt den Spieler zurück
		 */
		public Player getHost() {
			return p;
		}
		
		/*
		 * TODO: Lädt eine Schematic
		 */
		public boolean loadSchematic(String schematic_name) {
			//TODO: Muss gemacht werden
			return false;
		}
		
	}
	
	public static class Schematic {
		
		private String schematic_name = "";

		private SchematicProfile profile;
		private int offsetX = 0; //Der Offset ist dazu da, um die Schematic perfekt mittig auf einem Punkt spawnen zu können
		private int offsetY = 0;
		private int offsetZ = 0;
		private HashMap<Block, String> content = new HashMap<Block, String>();
		private Cuboid cuboid = null;
		
		public Schematic(String name, SchematicProfile profile) {
			this.schematic_name = name;
			this.profile = profile;
		}
		
		/*
		 * TODO: Setzt und Gibt die Offsets zurück
		 */
		public void setOffsetX(int offsetX) {
			this.offsetX = offsetX;
		}
		public void setOffsetY(int offsetY) {
			this.offsetY = offsetY;
		}
		public void setOffsetZ(int offsetZ) {
			this.offsetZ = offsetZ;
		}
		public int getOffsetX() {
			return offsetX;
		}
		public int getOffsetY() {
			return offsetY;
		}
		public int getOffsetZ() {
			return offsetZ;
		}
		
		/*
		 * TODO: Liest die Blöcke in der Welt in einem Bereich aus und speichert diese
		 */
		public boolean copy(boolean withAir) {
			
			/*
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * Hinweis: Die Copy Methode speichert bisher die Blöcke in eine Datei. Sie soll die Blöcke aber erst in eine arraylist packen.
			 *          Wenn die Methode save() aufgerufen wird, dann soll die arrayList ausgelesen und die Blöcke in eine Datei gespeichert werden
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 */
			File file = new File(getSchematicPath()+schematic_name+".yml");
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			
			boolean xPositive = false; //Gibt an, ob bei der .add(x,y,z) Methode negiert werden muss oder nicht
			boolean yPositive = false; //Gibt an, ob bei der .add(x,y,z) Methode negiert werden muss oder nicht
			boolean zPositive = false; //Gibt an, ob bei der .add(x,y,z) Methode negiert werden muss oder nicht
			
			double x1 = profile.getPos1().getX();
			double y1 = profile.getPos1().getY();
			double z1 = profile.getPos1().getZ();
			double x2 = profile.getPos2().getX();
			double y2 = profile.getPos2().getY();
			double z2 = profile.getPos2().getZ();
			
			Location l = new Location(profile.getPos1().getWorld(), profile.getPos1().getX(), profile.getPos1().getY(), profile.getPos1().getZ());
			
			/* Detektion, ob negiert werden muss oder nicht */
			if(x1 > x2) xPositive = false;
			else xPositive = true;
			
			if(y1 > y2) yPositive = false;
			else yPositive = true;
			
			if(z1 > z2) zPositive = false;
			else zPositive = true;
			/* -------------------------------------------- */
			
			
			int xDiff = (int)(x1 - x2);
			int yDiff = (int)(y1 - y2);
			int zDiff = (int)(z1 - z2);
			
			int tempX = 0;
			int tempY = 0;
			int tempZ = 0;
			
			int counter = 0;
			cfg.set("Schematic.Name", "");
			cfg.set("Schematic.Total Of Blocks", counter);
			
			for(int i = 0; i != yDiff; i++) {
				for(int ii = 0; ii != xDiff; ii++) {				
					for(int iii = 0; iii != zDiff; iii++) {
						
						if(yPositive) {tempY = -i; l.add(0,-i,0);}
						else {tempY = i; l.add(0,i,0);}
						
						if(xPositive) {tempX = -ii; l.add(-ii,0,0);}
						else {tempX = ii; l.add(ii,0,0);}
						
						if(zPositive) {tempZ = -iii; l.add(0,0,-iii);}
						else {tempZ = iii; l.add(0,0,iii);}
						
						cfg.set("Schematic.Content.B-"+counter+".Location.xDiff", tempX);
						cfg.set("Schematic.Content.B-"+counter+".Location.yDiff", tempY);
						cfg.set("Schematic.Content.B-"+counter+".Location.zDiff", tempZ);
						
						if(l.getBlock().getType() != Material.AIR && withAir) {	
							cfg = writeBlockToCfg(cfg, counter, l.getBlock());
						}
						if(counter == 100) break;
						counter++;
					}
					if(counter == 100) break;
				}
				if(counter == 100) break;

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
		public FileConfiguration writeBlockToCfg(FileConfiguration cfg, int counter, Block block) {
			
			cfg.set("Schematic.Content.B-"+counter+".type", block.getType().toString());
			cfg.set("Schematic.Content.B-"+counter+".blockdata", block.getBlockData());
			cfg.set("Schematic.Content.B-"+counter+".metadata", block.getMetadata("snowy"));
			cfg.set("Schematic.Content.B-"+counter+".biome", block.getBiome().toString());
			
			return cfg;
		}
		
		/*
		 * TODO: load() lädt die Schematic aus der Datei
		 */
		public boolean load() {
			File file = new File(getSchematicPath());
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			int size = cfg.getInt("Schematic.Size");
			
			Block b = null;
			for(int i = 0; i != size; i++) {
				//TODO: Muss weiter gemacht werden
			}
			
			cuboid = new Cuboid(profile.getPos1(), profile.getPos2());
			//TODO: Muss weitergemacht werden
			return false;
		}
		
		/*
		 * TODO: Konvertiert 3 Integer zu einem OffsetString
		 */
		public String convertToOffsetString(int a, int b, int c) {
			return a+":"+b+":"+c;
		}
		
		/*
		 * TODO: Gibt die Anzahl an Blöcken der Schematic zurück
		 */
		public int getBlockAmount() {
			return content.size();
		}
		
		/*
		 * TODO: paste() platziert die Schematic an Location 'center'
		 */
		public boolean paste(Location center) {
			
			return false;
		}
		
	}
	
}
