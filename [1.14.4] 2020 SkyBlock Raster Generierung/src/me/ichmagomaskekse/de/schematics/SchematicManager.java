package me.ichmagomaskekse.de.schematics;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.ichmagomaskekse.de.Cuboid;

public class SchematicManager {
	
	public HashMap<Player, SchematicProfile> profiles = new HashMap<Player, SchematicProfile>();
	public static final String schematic_path = getSchematicPath();
	
	public SchematicManager() {
		
	}
	/*
	 * TODO: Gibt den Pfad der Gespeicherten Schematic-Files zurück
	 */
	public static String getSchematicPath() {
		return "plugins/SkyBlock/Schematics/Saved/";
	}
	
	public class SchematicProfile {
		
		private Player p = null;
		private String loaded_schematic_name = "";
		private Schematic schematic = null;
		
		public SchematicProfile(Player p) {
			this.p = p;
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
	
	public class Schematic {
		
		private String schematic_name = "";
		private Location pos1, pos2; //Pos1 und Pos2 sind die beiden diagonalen Ecken der Schematic Region
		private int offsetX = 0; //Der Offset ist dazu da, um die Schematic perfekt mittig auf einem Punkt spawnen zu können
		private int offsetY = 0;
		private int offsetZ = 0;
		private HashMap<Block, String> content = new HashMap<Block, String>();
		private Cuboid cuboid = null;
		
		public Schematic(String name) {
			this.schematic_name = name;
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
			
			cuboid = new Cuboid(pos1, pos2);
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
