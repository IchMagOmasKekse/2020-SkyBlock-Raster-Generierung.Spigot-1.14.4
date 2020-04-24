package me.ichmagomaskekse.de.schematics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import me.ichmagomaskekse.de.Cuboid;
import me.ichmagomaskekse.de.Prefixes;
import me.ichmagomaskekse.de.SkyBlock;
import me.ichmagomaskekse.de.filemanagement.SkyFileManager;
import me.ichmagomaskekse.de.schematics.SchematicManager.Schematic.BlockStore;

public class SchematicManager implements Listener {
	
	private static HashMap<Player, SchematicProfile> profiles = new HashMap<Player, SchematicProfile>();
	public static final String schematic_path = getSchematicPath();
	private static HashMap<Schematic, BukkitRunnable> timers = new HashMap<Schematic, BukkitRunnable>();
	
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
	 * TODO: Registriert ein SchematicProfil und gibt dieses zurück
	 */
	public static SchematicProfile registerProfile(Player p) {
		if(profiles.containsKey(p) == false) profiles.put(p, new SchematicProfile(p));
		return profiles.get(p);
	}
	
	/*
	 * TODO: pastet eine Schematic
	 */
	public static boolean pasteSchematic(Player p, String name, boolean isGameplay) {
		Schematic schem = new Schematic(getProfile(p));
		return schem.pasteTimed(p, p.getLocation().clone(), name, 0l, 0l, isGameplay);
	}
	public static boolean pasteSchematicAt(Location loc, Player p, String name, boolean isGameplay) {
		Schematic schem = new Schematic(getProfile(p));
		return schem.pasteTimed(p, loc.clone(), name, 0l, 0l, isGameplay);
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
		private String saved_schematic_name = "";
		private Schematic schematic = null;
		private Cuboid selected_cuboid = null;
		private HashMap<Integer, BlockStore> storage = new HashMap<Integer, BlockStore>();
		
		private Location pos1, pos2; //Pos1 und Pos2 sind die beiden diagonalen Ecken der Schematic Region
		
		public SchematicProfile(Player p) {
			this.p = p;
			this.schematic = new Schematic(this);
		}
		
		/*
		 * TODO: Gibt die Schematic zurück
		 */
		public Schematic getSchematic() {
			return schematic;
		}
		
		/*
		 * TODO: Gibt zurück, ob die Schematic bereit zum speichern ist
		 */
		public boolean readyToSave() {
			if(getSchematic().content.isEmpty() == false) return true;
			else return false;
		}
		
		/*
		 * TODO: Gibt zurück, ob die Schematic bereit zum kopieren ist
		 */
		public boolean readyToCopy() {
//			if(pos1 != null && pos2 != null) return false;
			if(pos1 != null && pos2 != null) return true;
			else return false;
		}
		
		/*
		 * TODO: Registriert den Schematic Dateinamen der zu speichernden Schematic
		 */
		public void setSavedSchematicName(String name) {
			saved_schematic_name = name;
		}
		
		/*
		 * TODO: Registriert den Schematic Dateinamen der geladeten Schematic
		 */
		public void setLoadedSchematicName(String name) {
			loaded_schematic_name = name;
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
			if(selected_cuboid == null && pos1 != null && pos2 != null) selected_cuboid = new Cuboid(pos1, pos2);
			else if(selected_cuboid != null) selected_cuboid.setPoints(pos1, pos2);
		}
		public void setPos2(Location pos2) {
			this.pos2 = pos2;
			if(selected_cuboid == null && pos1 != null && pos2 != null) selected_cuboid = new Cuboid(pos1, pos2);
			else if(selected_cuboid != null) selected_cuboid.setPoints(pos1, pos2);
		}
		
		/*
		 * TODO: Gibt den Spieler zurück
		 */
		public Player getHost() {
			return p;
		}
		
		/*
		 * TODO: lädt eine Schematic
		 */
		public boolean loadSchematic(String schematic_name) {
			//TODO: Muss gemacht werden
			return false;
		}
		
	}
	
	public static class Schematic {
		
//		private String schematic_name = "";

		private SchematicProfile profile;
		private int offsetX = 0; //Der Offset ist dazu da, um die Schematic perfekt mittig auf einem Punkt spawnen zu k§nnen
		private int offsetY = 0;
		private int offsetZ = 0;
		private HashMap<Block, String> content = new HashMap<Block, String>();
		private ArrayList<Material> blacklist = new ArrayList<Material>();
		private ArrayList<String> postPlacing = new ArrayList<String>();
		private Cuboid cuboid = null;
		private Location locationWhereSchematicWillSpawn = null;
		private Location locationWherePlayerWillSpawn = null;
		
		public Schematic(SchematicProfile profile) {
//			this.schematic_name = name;
			this.profile = profile;
			blacklist.add(Material.GRAVEL);
			blacklist.add(Material.LEGACY_GRAVEL);
			blacklist.add(Material.SAND);
			blacklist.add(Material.LEGACY_SAND);
			
			blacklist.add(Material.TORCH);			
			blacklist.add(Material.LEGACY_TORCH);			
			blacklist.add(Material.WALL_TORCH);			
			blacklist.add(Material.REDSTONE_TORCH);			
			blacklist.add(Material.REDSTONE_WALL_TORCH);			
			blacklist.add(Material.LEGACY_REDSTONE_TORCH_OFF);			
			blacklist.add(Material.LEGACY_REDSTONE_TORCH_ON);			
			
			blacklist.add(Material.TALL_GRASS);
			blacklist.add(Material.GRASS);
			blacklist.add(Material.ROSE_BUSH);
			blacklist.add(Material.SWEET_BERRY_BUSH);
			blacklist.add(Material.LEGACY_LONG_GRASS);
			blacklist.add(Material.FERN);
			blacklist.add(Material.DEAD_BUSH);
			blacklist.add(Material.LEGACY_DEAD_BUSH);
			blacklist.add(Material.SEAGRASS);
			blacklist.add(Material.TALL_SEAGRASS);
			blacklist.add(Material.SEA_PICKLE);
			blacklist.add(Material.DANDELION);
			blacklist.add(Material.POPPY);
			blacklist.add(Material.BLUE_ORCHID);
			blacklist.add(Material.ALLIUM);
			
			blacklist.add(Material.OAK_SAPLING);
			blacklist.add(Material.SPRUCE_SAPLING);
			blacklist.add(Material.BIRCH_SAPLING);
			blacklist.add(Material.JUNGLE_SAPLING);
			blacklist.add(Material.DARK_OAK_SAPLING);
			blacklist.add(Material.ACACIA_SAPLING);
			
			blacklist.add(Material.ACACIA_LEAVES);
			blacklist.add(Material.BIRCH_LEAVES);
			blacklist.add(Material.DARK_OAK_LEAVES);
			blacklist.add(Material.JUNGLE_LEAVES);
			blacklist.add(Material.LEGACY_LEAVES);
			blacklist.add(Material.LEGACY_LEAVES_2);
			blacklist.add(Material.OAK_LEAVES);
			blacklist.add(Material.SPRUCE_LEAVES);
			
			blacklist.add(Material.WATER);
			blacklist.add(Material.LEGACY_WATER);
			blacklist.add(Material.LAVA);
			blacklist.add(Material.LEGACY_LAVA);
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
			
			boolean xPositive = false; //Gibt an, ob bei der .add(x,y,z) Methode negiert werden muss oder nicht
			boolean yPositive = false; //Gibt an, ob bei der .add(x,y,z) Methode negiert werden muss oder nicht
			boolean zPositive = false; //Gibt an, ob bei der .add(x,y,z) Methode negiert werden muss oder nicht
			
			double x1 = profile.getPos1().clone().getX();
			double y1 = profile.getPos1().clone().getY();
			double z1 = profile.getPos1().clone().getZ();
			double x2 = profile.getPos2().clone().getX();
			double y2 = profile.getPos2().clone().getY();
			double z2 = profile.getPos2().clone().getZ();
			
			locationWhereSchematicWillSpawn = new Location(profile.getPos1().getWorld(), profile.getPos1().getX(), profile.getPos1().getY(), profile.getPos1().getZ());
			Location l = new Location(locationWhereSchematicWillSpawn.clone().getWorld(),
					locationWhereSchematicWillSpawn.clone().getX(),
					locationWhereSchematicWillSpawn.clone().getY(),
					locationWhereSchematicWillSpawn.clone().getZ());
			
			/* Detektion, ob negiert werden muss oder nicht */
			if(x1 > x2) xPositive = false;
			else xPositive = true;
			
			if(y1 > y2) yPositive = false;
			else yPositive = true;
			
			if(z1 > z2) zPositive = false;
			else zPositive = true;
			/* -------------------------------------------- */
			
			int xDiff = profile.selected_cuboid.getXWidth()+1;
			int yDiff = profile.selected_cuboid.getHeight()+1;
			int zDiff = profile.selected_cuboid.getZWidth()+1;
			
			int tempX = 0;
			int tempY = 0;
			int tempZ = 0;
			
			int counter = 0;
			
			for(int i = 0; i != yDiff; i++) {
				for(int ii = 0; ii != xDiff; ii++) {
					for(int iii = 0; iii != zDiff; iii++) {
						
						
						
						if(yPositive) {tempY = i;}
						else {tempY = -i;}
						
						if(xPositive) {tempX = ii;}
						else {tempX = -ii;}
						
						if(zPositive) {tempZ = iii;}
						else {tempZ = -iii;}
						
						Block b = l.clone().add(tempX, tempY, tempZ).getBlock();
						if(withAir == false && (b.getType() != Material.AIR && b.getType() != Material.CAVE_AIR)) {
							content.put(b, convertToOffsetString(tempX, tempY, tempZ));
//							if(counter == 5000) break;							
							counter++;
						}else if(withAir == true){
							content.put(b, convertToOffsetString(tempX, tempY, tempZ));
//							if(counter == 5000) break;							
							counter++;							
						}
					}
//					if(counter == 5000) break;
				}
//				if(counter >= 5000) break;
				l = locationWhereSchematicWillSpawn.clone();
			}
			
			return true;
		}
		
		/*
		 * TODO: save(overwrite) soll die geladene Schematic speichern und die Datei überschreiben,
		 *       falls dies gewünscht ist
		 */
		public boolean save(boolean overwrite) {
			File file = new File(getSchematicPath()+profile.saved_schematic_name+".yml");
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			int counter = 0;
			
			int tempX = 0;
			int tempY = 0;
			int tempZ = 0;
			
			cfg.set("Schematic.Name", profile.saved_schematic_name);
			cfg.set("Schematic.Total Of Blocks", counter);
			cfg.set("Schematic.Sizing.xWidth", profile.selected_cuboid.getXWidth());
			cfg.set("Schematic.Sizing.height", profile.selected_cuboid.getHeight());
			cfg.set("Schematic.Sizing.zWidth", profile.selected_cuboid.getZWidth());
			String code = "";
			String[] a = null;
			for(Block b : content.keySet()) {
				
				code = content.get(b);
				a = code.split(":");
				
				tempX = Integer.parseInt(a[0]);
				tempY = Integer.parseInt(a[1]);
				tempZ = Integer.parseInt(a[2]);
				
				cfg.set("Schematic.Content.B-"+counter+".Location.xDiff", tempX);
				cfg.set("Schematic.Content.B-"+counter+".Location.yDiff", tempY);
				cfg.set("Schematic.Content.B-"+counter+".Location.zDiff", tempZ);
				
				cfg = writeBlockToCfg(cfg, counter, locationWhereSchematicWillSpawn.clone().add(tempX,tempY,tempZ).getBlock());
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
		
		public FileConfiguration writeBlockToCfg(FileConfiguration cfg, int counter, Block block) {
			
			cfg.set("Schematic.Content.B-"+counter+".type", block.getType().toString());
//			cfg.set("Schematic.Content.B-"+counter+".blockdata", block.getBlockData());
			cfg.set("Schematic.Content.B-"+counter+".metadata", block.getMetadata("snowy"));
			cfg.set("Schematic.Content.B-"+counter+".biome", block.getBiome().toString());
			
			
			return cfg;
		}
		
		/*
		 * TODO: paste() platziert die Schematic an Location origin
		 */
		public boolean paste(Player p, Location origin, String file_name) {
			File file = new File(getSchematicPath()+file_name+".yml");
			if(file.exists() == false) return false;
			ArrayList<Chest> chests = new ArrayList<Chest>();
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			
			BlockStore storage = new BlockStore(getProfile(p).storage.size()+1);
			
			int size = cfg.getInt("Schematic.Total Of Blocks");
			
			int tempX = 0;
			int tempY = 0;
			int tempZ = 0;
			
			int xWidth = cfg.getInt("Schematic.Sizing.xWidth");
			int height = cfg.getInt("Schematic.Sizing.height");
			int zWidth = cfg.getInt("Schematic.Sizing.zWidth");
			p.sendMessage("XW, hei, zW: "+xWidth+", "+height+", "+zWidth);
			origin.add(-(xWidth/2), -(height/2), -(zWidth/2));
			
			String mat = "";
			Block b = null;
			Material m = Material.AIR;
			Biome bio = Biome.BADLANDS;
			for(int i = 0; i != size; i++) {
				tempX = cfg.getInt("Schematic.Content.B-"+i+".Location.xDiff");
				tempY = cfg.getInt("Schematic.Content.B-"+i+".Location.yDiff");
				tempZ = cfg.getInt("Schematic.Content.B-"+i+".Location.zDiff");
				b = origin.clone().add(tempX, tempY, tempZ).getBlock();
				
				storage.addBlock(origin.clone().add(tempX, tempY, tempZ), b);
				
				mat = "LEGACY_"+cfg.getString("Schematic.Content.B-"+i+".type");
//				Bukkit.broadcastMessage("Block#"+i+"Material:"+mat);
				try{
					m = Material.valueOf(mat);
				}catch(IllegalArgumentException ex1) {
					try{
						m = Material.valueOf(mat.replace("LEGACY_", ""));
					}catch(IllegalArgumentException ex2) {
						m = Material.AIR;
					}
				}
				bio = Biome.valueOf(cfg.getString("Schematic.Content.B-"+i+".biome"));
				b.setType(m);
				b.setBiome(bio);
				origin.getWorld().spawnParticle(Particle.FLAME, origin.clone().add(tempX, tempY, tempZ), 10, 0.5d, 0.5d, 0.5d, 0);
				if(b.getState() instanceof Chest) {
					Chest c = (Chest) b.getState();
					Inventory inv = ChestGenerator.readInventory("&aStarter_Kit");
					if(inv.getContents() != null) c.getInventory().setContents(inv.getContents());
				}
//				Bukkit.broadcastMessage("Counter:"+i);
			}
						
			if(p != null) {
				getProfile(p).storage.put(storage.id, storage);
				p.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Schematic wurde platziert");
			}
			return true;
		}
		
		/*
		 * TODO: Füllt die Chests mit einem Kit
		 */
		public void fillChests(ArrayList<Chest> chests, String kit) {
			Bukkit.broadcastMessage("Chests:"+chests.size());
			for(Chest c : chests) {
				c.getInventory().setContents(ChestGenerator.readInventory(kit).getContents());
			}
		}
		
		
		/*
		 * TODO: pasteTimed() platziert eine Schematic Block für Block und nicht alles mit einmal
		 */
		public boolean pasteTimed(final CommandSender sender, final Location origin, String file_name, long delay, long period, final boolean isGameplay) {
			
			if(timers.containsKey(this)) {
				return false;
			}else {
				final Schematic schematic = this;
				final File file = new File(getSchematicPath()+file_name+".yml");
				if(file.exists() == false) {
					return false;
				}
				
				timers.put(schematic, new BukkitRunnable() {
					
					FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
					
					String mat = "";
					Block b = null;
					Material m = Material.AIR;
					Biome bio = Biome.BADLANDS;
					
					boolean postProcess = false;
//					ArrayList<Chest> chests = new ArrayList<Chest>();
					
					int step = 0; //step = der Schritt, in dem die Platzier Animation gerade ist
					int size = cfg.getInt("Schematic.Total Of Blocks");
					int tempX = 0;
					int tempY = 0;
					int tempZ = 0;
					
					int xWidth = cfg.getInt("Schematic.Sizing.xWidth");
					int height = cfg.getInt("Schematic.Sizing.height");
					int zWidth = cfg.getInt("Schematic.Sizing.zWidth");
					
					@Override
					public void run() {
//						if((step+"").endsWith("0")) Bukkit.broadcastMessage("Step: "+step);
						if(step == 0 && postProcess == false) {
							origin.add(-(xWidth/2), (height/2), -(zWidth/2));
							step++;
						}else if(step != size) {
							step = setBlock(sender, step, size, cfg, tempX, tempY, tempZ, mat, m, b, bio, origin, postProcess);
						}else if(step == size) {
							if(postProcess == false) {
								postProcess = true;
								step = 0;
							}else {								
								timers.get(schematic).cancel();
								timers.remove(schematic);
								if(sender != null) {
									if(isGameplay && sender instanceof Player) {
										Player p = (Player) sender;
										if(getProfile(p).getSchematic().getPlayerSpawn() != null) {											
											p.teleport(getProfile(p).getSchematic().getPlayerSpawn().add(0.5, 1.5, 0.5));
										}else {
											p.teleport(SkyFileManager.getLocationOfIsland(p).add(0.5, 1.5, 0.5));
										}
										SkyBlock.spawnFireworks(p.getLocation(), 1, true, false, Type.BURST, Color.LIME);
										p.setGameMode(GameMode.SURVIVAL);
										p.sendMessage(Prefixes.SERVER.getPrefix()+"Viel Spaß!");
									}
								}
							}
						}
					}
				});
				timers.get(schematic).runTaskTimer(SkyBlock.getInstance(), delay, period);
			}
			
			return true;
		}
		public int setBlock(CommandSender sender, int step, int size, FileConfiguration cfg, int tempX, int tempY, int tempZ, String mat, Material m, Block b, Biome bio, Location origin, boolean postProcess) {
			boolean again = true;
			while(again == true) {
				if(postProcess) {
					if(postPlacing.isEmpty()) {
						step = size;
						return step;
					}else {
						for(String s : postPlacing) {
							step = Integer.valueOf(s);
							postPlacing.remove(s);
							break;
						}
					}
				}
				tempX = cfg.getInt("Schematic.Content.B-"+step+".Location.xDiff");
				tempY = cfg.getInt("Schematic.Content.B-"+step+".Location.yDiff");
				tempZ = cfg.getInt("Schematic.Content.B-"+step+".Location.zDiff");
				b = origin.clone().add(tempX, tempY, tempZ).getBlock();
				
				
				mat = cfg.getString("Schematic.Content.B-"+step+".type");
				try{
					m = Material.valueOf(mat);
				}catch(IllegalArgumentException ex1) {
					try{
						m = Material.valueOf("LEGACY_"+mat);
					}catch(IllegalArgumentException ex2) {
						m = Material.AIR;
					}
				}
				if(blacklist.contains(m) && postProcess == false) {
					postPlacing.add(step+"");
				}else if(m == Material.EMERALD_BLOCK){
					locationWherePlayerWillSpawn = b.getLocation();
					sender.sendMessage("Spawn gefunden");
				} else {
					if(m== Material.AIR || m == Material.CAVE_AIR) {
						again = true;
					}else {									
						bio = Biome.valueOf(cfg.getString("Schematic.Content.B-"+step+".biome"));
						b.setType(m);
						if(m == Material.TALL_GRASS) b.setBlockData(m.createBlockData());
						b.setBiome(bio);
						origin.getWorld().spawnParticle(Particle.FLAME, origin.clone().add(tempX, tempY, tempZ), 10, 0.5d, 0.5d, 0.5d, 0);
						origin.getWorld().playSound(((Player)sender).getLocation(), Sound.ENTITY_CHICKEN_EGG, 6f, 0f);
						again = false;
						if(b.getState() instanceof Chest) {
							Chest c = (Chest) b.getState();
							Inventory inv = ChestGenerator.readInventory("&aStarter_Kit");
							if(inv.getContents() != null) c.getInventory().setContents(inv.getContents());
						}
					}
				}
				if(step == size) {
					return step;
				}else {
					step++;
					if(step == size) {
						return step;
					}
				}
			}
			return step;
		}
		
		/*
		 * TODO: Gibt den Spawn des Spielers zurück
		 */
		public Location getPlayerSpawn() {
			return locationWherePlayerWillSpawn;
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
		
		public static class BlockStore {
			
			/*
			 * TODO: BlockStore soll alle Blöcke einer Änderung speichern, damit man /s undo machen kann
			 */
			public HashMap<Location, Block> content = new HashMap<Location, Block>();
			public int id = 0;
			
			public BlockStore(int id) {
				this.id = id;
			}
			
			public boolean addBlock(Location l, Block b) {
				if(content.containsKey(l)) return false;
				else content.put(l, b);
				return true;
			}
			
			/*
			 * TODO: Alle Blöcke werden wieder dort platziert, wo sie standen
			 */
			public boolean restore() {
				for(Location l : content.keySet()) {
					l.getBlock().setType(content.get(l).getType());
					l.getBlock().setBlockData(content.get(l).getBlockData());
					l.getBlock().setBiome(content.get(l).getBiome());
				}
				return true;
			}
			
		}
		
	}
	
}
