package me.dreamisland.de.schematics;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
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
import org.bukkit.scheduler.BukkitRunnable;

import me.dreamisland.de.Cuboid;
import me.dreamisland.de.Prefixes;
import me.dreamisland.de.SkyBlock;
import me.dreamisland.de.commands.SkyBlockCommandFunction;
import me.dreamisland.de.filemanagement.SkyFileManager;
import me.dreamisland.de.schematics.SchematicReaderAndWriter.BlockProfile;

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
		if(profiles.containsKey(p) == false) {
			profiles.put(p, new SchematicProfile(p));
		}
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
//		Schematic schem = new Schematic(getProfile(p));
//		return schem.pasteTimed(p, loc.clone(), name, 0l, 0l, isGameplay);
//		if(isGameplay) p.setGameMode(GameMode.SPECTATOR);
		boolean success = IslandPaster.pasteIsland(p, loc, isGameplay);
		if(SkyBlockCommandFunction.isCreating.contains(p)) SkyBlockCommandFunction.isCreating.remove(p);
		p.setGameMode(GameMode.SURVIVAL);
		p.performCommand("is");
		return success;
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
		public String loaded_schematic_name = "";
		public String saved_schematic_name = "";
		private Schematic schematic = null;
		public Cuboid selected_cuboid = null;
		
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
			return true;
//			if(getSchematic().content.isEmpty() == false) return true;
//			else return false;
		}
		
		/*
		 * TODO: Gibt zurück, ob die Schematic bereit zum kopieren ist
		 */
		public boolean readyToCopy() {
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
		private ConcurrentHashMap<Location, Block> content = new ConcurrentHashMap<Location, Block>();
		private ConcurrentHashMap<Location, BlockProfile> postPlacing = new ConcurrentHashMap<Location, BlockProfile>();
		private ArrayList<Material> blacklist = new ArrayList<Material>();
		private ArrayList<Location> chests = new ArrayList<Location>();
		private Location locationWherePlayerWillSpawn = null;
		
		@SuppressWarnings("deprecation")
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
			
			blacklist.add(Material.ACACIA_FENCE);
			blacklist.add(Material.ACACIA_FENCE_GATE);
			blacklist.add(Material.BIRCH_FENCE);
			blacklist.add(Material.BIRCH_FENCE_GATE);
			blacklist.add(Material.DARK_OAK_FENCE);
			blacklist.add(Material.DARK_OAK_FENCE_GATE);
			blacklist.add(Material.JUNGLE_FENCE);
			blacklist.add(Material.JUNGLE_FENCE_GATE);
			blacklist.add(Material.LEGACY_ACACIA_FENCE);
			blacklist.add(Material.LEGACY_ACACIA_FENCE_GATE);
			blacklist.add(Material.LEGACY_BIRCH_FENCE);
			blacklist.add(Material.LEGACY_BIRCH_FENCE_GATE);
			blacklist.add(Material.LEGACY_DARK_OAK_FENCE);
			blacklist.add(Material.LEGACY_DARK_OAK_FENCE_GATE);
			blacklist.add(Material.LEGACY_FENCE);
			blacklist.add(Material.LEGACY_FENCE_GATE);
			blacklist.add(Material.LEGACY_IRON_FENCE);
			blacklist.add(Material.LEGACY_JUNGLE_FENCE);
			blacklist.add(Material.LEGACY_JUNGLE_FENCE_GATE);
			blacklist.add(Material.LEGACY_NETHER_FENCE);
			blacklist.add(Material.LEGACY_SPRUCE_FENCE);
			blacklist.add(Material.LEGACY_SPRUCE_FENCE_GATE);
			blacklist.add(Material.NETHER_BRICK_FENCE);
			blacklist.add(Material.OAK_FENCE);
			blacklist.add(Material.OAK_FENCE_GATE);
			blacklist.add(Material.SPRUCE_FENCE);
			blacklist.add(Material.SPRUCE_FENCE_GATE);
			
			blacklist.add(Material.CHEST);
			blacklist.add(Material.CAMPFIRE);
			blacklist.add(Material.LEGACY_CROPS);
			blacklist.add(Material.LEGACY_WHEAT);
			blacklist.add(Material.BEETROOT_SEEDS);
			blacklist.add(Material.LEGACY_BEETROOT_SEEDS);
			blacklist.add(Material.LEGACY_MELON_SEEDS);
			blacklist.add(Material.LEGACY_PUMPKIN_SEEDS);
			blacklist.add(Material.LEGACY_SEEDS);
			blacklist.add(Material.MELON_SEEDS);
			blacklist.add(Material.PUMPKIN_SEEDS);
			blacklist.add(Material.WHEAT_SEEDS);
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
			content = SchematicReaderAndWriter.copy(profile, profile.getPos1(), profile.getPos2(), withAir);
			return true;
		}
		
		/*
		 * TODO: save(overwrite) soll die geladene Schematic speichern und die Datei überschreiben,
		 *       falls dies gewünscht ist
		 */
		public boolean save(boolean overwrite) {
			return SchematicReaderAndWriter.save(profile, profile.saved_schematic_name, overwrite);
		}
		
		public FileConfiguration writeBlockToCfg(FileConfiguration cfg, int counter, Block block) {
			
			cfg.set("Schematic.Content.B-"+counter+".type", block.getType().toString());
//			cfg.set("Schematic.Content.B-"+counter+".blockdata", block.getBlockData());
			cfg.set("Schematic.Content.B-"+counter+".metadata", block.getMetadata("snowy"));
			cfg.set("Schematic.Content.B-"+counter+".biome", block.getBiome().toString());
			
			
			return cfg;
		}
		
		/*
		 * TODO: Füllt die Chests mit einem Kit
		 */
		public void fillChests(ArrayList<Location> chests, String kit) {
			for(Location l : chests) {
				if(l.getBlock().getState() instanceof Chest) {
					Chest c = (Chest) l.getBlock().getState();
					c.getInventory().setContents(ChestGenerator.readInventory(kit).getContents());
				}
			}
		}
		
		
		/*
		 * TODO: pasteTimed() platziert eine Schematic Block für Block und nicht alles mit einmal
		 */
		private boolean postProcess = false;
		public boolean pasteTimed(final CommandSender sender, final Location origin, final String file_name, long delay, long period, final boolean isGameplay) {
			
			if(timers.containsKey(this)) {
				return false;
			}else {
				final Schematic schematic = this;
				final File file = new File(getSchematicPath()+file_name+".yml");
				if(file.exists() == false) {
					return false;
				}
				FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				final int xWidth = cfg.getInt("Schematic.Sizing.xWidth");
				final int height = cfg.getInt("Schematic.Sizing.height");
				final int zWidth = cfg.getInt("Schematic.Sizing.zWidth");
				origin.add(-(xWidth/2), -(height / 2), -(zWidth / 2));
				
				if(isGameplay && sender instanceof Player) ((Player)sender).setGameMode(GameMode.SPECTATOR);
				
				timers.put(schematic, new BukkitRunnable() {
					ConcurrentHashMap<Location, BlockProfile> content = SchematicReaderAndWriter.readSchematic(file_name);
					Location l = null;
					Material m = null;
					BlockProfile bp = null;
					Location player_spawn = null;
					int finish_delay = 100;
					boolean repeat = false;
					@Override
					public void run() {
						if(postProcess && postPlacing.size() != 0) {
							if(postPlacing.size() != 0) {
								for(Location lo : postPlacing.keySet()) {
									l = lo.clone();
									m = postPlacing.get(lo).mat;
									lo = origin.clone().add(l.clone());
																	
									lo.getBlock().setType(m);
									lo.getBlock().setBlockData(postPlacing.get(l).data);
									if(isGameplay)origin.getWorld().spawnParticle(Particle.FLAME, lo.clone(), 10, 0.5d, 0.5d, 0.5d, 0);
									if(isGameplay)origin.getWorld().playSound(lo.clone(), Sound.ENTITY_CHICKEN_EGG, 6f, -2f);
									break;
								}
								postPlacing.remove(l);
							}
						}else {
							if(content.size() != 0) {								
								for(Location lo : content.keySet()) {
									l = lo.clone();
									m = content.get(lo).mat;
									lo = origin.clone().add(l.clone());
									
									if(m == Material.EMERALD_BLOCK && isGameplay) {
										player_spawn = lo.clone().add(0.5,0.5,0.5);
										repeat = true;
									}else if(blacklist.contains(m) == false) {
										lo.getBlock().setType(m);
										lo.getBlock().setBlockData(content.get(l).data);
										if(m == Material.CHEST)chests.add(lo.clone());
										if(isGameplay)origin.getWorld().spawnParticle(Particle.FLAME, lo.clone(), 10, 0.5d, 0.5d, 0.5d, 0);
										if(isGameplay)origin.getWorld().playSound(lo.clone(), Sound.ENTITY_CHICKEN_EGG, 6f, 0f);
									}else {
										if(m == Material.CHEST)chests.add(lo.clone());
										bp = content.get(l);
										postPlacing.put(l.clone(), new BlockProfile(bp.mat, bp.location.clone(), bp.data));
										repeat = true;
									}
									
									
									content.remove(l);
									if(repeat==false)break;
								}
							}
							repeat = false;
						}
						
						if(content.size() == 0 && postProcess == true && postPlacing.size() == 0) {
							
							if(finish_delay == 0) {								
								timers.get(schematic).cancel();
								timers.remove(schematic);
								fillChests(chests, "&aStarter_Kit");
								if(sender != null) {
									if(isGameplay && sender instanceof Player) {
										Player p = (Player) sender;
										if(player_spawn != null) {
											profile.getSchematic().locationWherePlayerWillSpawn = player_spawn.clone();
											if(p != null) {
												p.teleport(player_spawn);												
												SkyFileManager.setSpawn(p, player_spawn);
											}
										}
										if(p != null) {
											if(SkyBlockCommandFunction.isCreating.contains(p)) SkyBlockCommandFunction.isCreating.remove(p);
											SkyBlock.spawnFireworks(p.getLocation(), 1, true, false, Type.BURST, Color.LIME);
											p.setGameMode(GameMode.SURVIVAL);
											p.sendMessage(Prefixes.SERVER.getPrefix()+"Viel Spaß!");
										}
									}else {
										sender.sendMessage(Prefixes.SCHEMATIC.getPrefix()+"Schematic wurde platziert");
									}
								}
							}else finish_delay--;
						}else if(content.size() == 0 && postProcess == false) {
							postProcess = true;
						}
						
					}
				});
				timers.get(schematic).runTaskTimer(SkyBlock.getInstance(), delay, period);
			}
			
			return true;
		}
//		public int setBlock(CommandSender sender, int step, int size, FileConfiguration cfg, int tempX, int tempY, int tempZ, String mat, Material m, Block b, Biome bio, Location origin, boolean postProcess, boolean isGameplay) {
//			boolean again = true;
//			while(again == true) {
//				if(postProcess) {
//					if(postPlacing.isEmpty()) {
//						step = size;
//						return step;
//					}else {
//						for(String s : postPlacing) {
//							step = Integer.valueOf(s);
//							postPlacing.remove(s);
//							break;
//						}
//					}
//				}
//				tempX = cfg.getInt("Schematic.Content.B-"+step+".Location.xDiff");
//				tempY = cfg.getInt("Schematic.Content.B-"+step+".Location.yDiff");
//				tempZ = cfg.getInt("Schematic.Content.B-"+step+".Location.zDiff");
//				b = origin.clone().add(tempX, tempY, tempZ).getBlock();
//				
//				
//				mat = cfg.getString("Schematic.Content.B-"+step+".type");
//				try{
//					m = Material.valueOf(mat);
//				}catch(IllegalArgumentException ex1) {
//					try{
//						m = Material.valueOf("LEGACY_"+mat);
//					}catch(IllegalArgumentException ex2) {
//						m = Material.AIR;
//					}
//				}
//				if(blacklist.contains(m) && postProcess == false) {
//					postPlacing.add(step+"");
//				}else if(m == Material.EMERALD_BLOCK){
//					locationWherePlayerWillSpawn = b.getLocation();
//					sender.sendMessage("Spawn gefunden");
//				} else {
//					if(m== Material.AIR || m == Material.CAVE_AIR) {
//						again = true;
//					}else {
//						bio = Biome.valueOf(cfg.getString("Schematic.Content.B-"+step+".biome"));
//						b.setType(m);
//						if(m == Material.TALL_GRASS) b.setBlockData(m.createBlockData());
//						b.setBiome(bio);
//						origin.getWorld().spawnParticle(Particle.FLAME, origin.clone().add(tempX, tempY, tempZ), 10, 0.5d, 0.5d, 0.5d, 0);
//						origin.getWorld().playSound(((Player)sender).getLocation(), Sound.ENTITY_CHICKEN_EGG, 6f, 0f);
//						again = false;
//						if(isGameplay) {							
//							if(b.getState() instanceof Chest) {
//								Chest c = (Chest) b.getState();
//								Inventory inv = ChestGenerator.readInventory("&aStarter_Kit");
//								if(inv.getContents() != null) c.getInventory().setContents(inv.getContents());
//							}
//						}
//					}
//				}
//				if(step == size) {
//					return step;
//				}else {
//					step++;
//					if(step == size) {
//						return step;
//					}
//				}
//			}
//			return step;
//		}
		
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
