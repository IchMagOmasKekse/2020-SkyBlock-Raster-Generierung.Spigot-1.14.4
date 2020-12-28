package me.crafttale.de.profiles;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.crafttale.de.ActionBar;
import me.crafttale.de.Chat.MessageType;
import me.crafttale.de.Cuboid;
import me.crafttale.de.PlayerAtlas;
import me.crafttale.de.SkyBlock;
import me.crafttale.de.SkyBlockGenerator;
import me.crafttale.de.filemanagement.SkyFileManager;
import me.crafttale.de.profiles.PlayerProfiler.PlayerProfile;
import me.crafttale.de.tablist.TablistManager;

public class IslandManager {
	
	public static HashMap<Player, IslandProfile> profiles = new HashMap<Player, IslandProfile>();
	public static HashMap<Integer, IslandData> islandDatas = new HashMap<Integer, IslandData>();
	
	private static BukkitRunnable timer = null;
	
	public IslandManager() {
		for(Player p : Bukkit.getOnlinePlayers()) loadProfile(p);
		
		for(int i : SkyFileManager.getRemovers()) {
			clearIsland(i);
		}
		
		timer = new BukkitRunnable() {
			boolean isOnAnIsland = false;
			boolean pvp = false;
			@Override
			public void run() {
				if(islandDatas == null) loadAllIslandData();
					for(Player p : Bukkit.getOnlinePlayers()) {
						PlayerProfile profile = PlayerProfiler.getProfile(PlayerProfiler.getUUID(p));
						if(profile.getStandort() == null) profile.registerStandort();
						if(islandDatas.isEmpty() == false) {
							for(IslandData data : islandDatas.values()) {
								if(isOnAnIsland == false && data.getVolume().isIn(p.getLocation())) {
									if(p != null && data != null && data.getOwnerUuid() != null) ActionBar.send(false, "§fDein Standort: §aInsel #"+data.getIslandId()+" §fBesitzer: §a"+(PlayerAtlas.getPlayername(data.getOwnerUuid().toString())), p);
									else ActionBar.send(false, "§fDein Standort: §aWildnis", p);
									
									if(profile.getStandort().getIslandData() == null) profile.registerStandort(data);
									profile.getStandort().reset().setIslandId(data.island_id, p.getLocation(), data).getIslandData().checkForBannedLocation(p);
									
									isOnAnIsland = true;																			
								}
							}
						}
						if(isOnAnIsland == false) {
							if(SkyBlock.spawnCuboid.isIn(p.getLocation())) {
								ActionBar.send(false, "§fDein Standort: §eSpawn", p);
								PlayerProfiler.getProfile(PlayerProfiler.getUUID(p)).getStandort().setName("Spawn", p.getLocation());
							}else if(p.getWorld().getName().equals("shopworld")) {
								ActionBar.send(false, "§fDein Standort: §bShop", p);
								PlayerProfiler.getProfile(PlayerProfiler.getUUID(p)).getStandort().setName("Shop", p.getLocation());
							}else if(p.getLocation().getY() < 0) {
								ActionBar.send(false, "§8† §7Wir sehen uns im nächsten Leben! §8†", p);
								PlayerProfiler.getProfile(PlayerProfiler.getUUID(p)).getStandort().setName("Void", p.getLocation());
							}else {
								ActionBar.send(false, "§fDein Standort: §aWildnis", p);
								PlayerProfiler.getProfile(PlayerProfiler.getUUID(p)).getStandort().setName("Wildnis", p.getLocation());
							}
						}else isOnAnIsland = false;
						
						TablistManager.setTablist(p);
					}
					pvp = !pvp;
			}
		};
		timer.runTaskTimerAsynchronously(SkyBlock.getSB(), 0l, 5l);
	}
	
	/*
	 * TODO: Gibt zurück, ob ein Spieler sich auf einem Insel Grundstück befindet
	 */
	public static boolean isInIslandRegion(String uuid_target, Player p) {
		for(Player t : profiles.keySet()) {
			profiles.get(p).cuboid.isIn(t); //<-- Das kann Fehler verursachen. 'p' 't' und 'uuid_target' könnten vertauscht sein
		}
		return false;
	}
	
	/*
	 * TODO: Gibt das Insel Profil zurück
	 */
	public static IslandProfile getProfile(Player p) {
		if(profiles.containsKey(p)) return profiles.get(p);
		else {
			loadProfile(p);
			return profiles.get(p);
		}
	}
	/**
	 * Gibt die IslandData einer Insel zurück
	 * @param island_id
	 * @return
	 */
	public static IslandData getIslandData(int island_id) {
		if(islandDatas.containsKey(island_id)) return islandDatas.get(island_id);
		else return null;
	}
	
	/*
	 * TODO: Meldet alle Insel Profile ab
	 */
	public static boolean unloadAll() {
		profiles.clear();
		for(IslandData data : islandDatas.values()) data.save();
		islandDatas.clear();
		timer.cancel();
		return true;
	}
	
	/*
	 * TODO: Meldet das Profil einer Insel ab
	 */
	public static boolean unloadProfile(Player p) {
		if(profiles.containsKey(p)) {
			profiles.remove(p);
			return true;
		}else return false;
	}
	
	/*
	 * TODO: Registriert das Profil einer Insel 
	 */
	public static boolean loadProfile(Player p) {
		if(profiles.containsKey(p)) return false;
		else {
			UUID uuid = PlayerProfiler.getUUID(p);
			if(SkyFileManager.hasIsland(p)) {
				IslandProfile profile = new IslandProfile(SkyFileManager.getIslandID(uuid.toString()), uuid);
				profiles.put(p, profile);
			}else if(SkyFileManager.isMemberOfAnIsland(uuid)) {
				IslandProfile profile = new IslandProfile(SkyFileManager.getIslandIDWhereHeIsMemberOf(uuid), uuid);
				profiles.put(p, profile);	
			}else return false;
			return true;
		}
	}
	
	/**
	 * Lädt die IslandData jeder Insel. Auch die ohne Besitzer.
	 * @return
	 */
	public static boolean loadAllIslandData() {
		File file = new File("plugins/SkyBlock/Insel-Index-File.yml");
		if(file.exists() == false) return false;
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		islandDatas = new HashMap<Integer, IslandData>();
		
		for(int id = 1; id != SkyBlockGenerator.amountOfIslands; id++) {
			islandDatas.put(id,
					new IslandData(id, new Location(Bukkit.getWorld(cfg.getString("Islands.ID-"+id+".World")), cfg.getDouble("Islands.ID-"+id+".LocX"), cfg.getDouble("Islands.ID-"+id+".LocY"), cfg.getDouble("Islands.ID-"+id+".LocZ")),
					(cfg.getString("Islands.ID-"+id+".Owner UUID").equals("none") ? false : true),
					cfg.getString("Islands.ID-"+id+".Owner UUID")));
		}
		return true;
	}
	
	/**
	 * Gibt den Cuboid einer Insel zurück
	 * @param island_id
	 * @return
	 */
	public static Cuboid getIslandCuboid(int island_id) {
		File index = new File("plugins/SkyBlock/Insel-Index-File.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(index);
		
		int x1 = (int)(cfg.getDouble("Islands.ID-"+island_id+".LocX")-(SkyBlockGenerator.issize/2));
		int z1 = (int)(cfg.getDouble("Islands.ID-"+island_id+".LocZ")-(SkyBlockGenerator.issize/2));
		int x2 = x1+(SkyBlockGenerator.issize);
		int z2 = z1+(SkyBlockGenerator.issize);
		Location pos1 = new Location(Bukkit.getWorld(cfg.getString("Islands.ID-"+island_id+".World")), x1, 0, z1);
		Location pos2 = new Location(Bukkit.getWorld(cfg.getString("Islands.ID-"+island_id+".World")), x2, 256, z2);
		return new Cuboid(pos1, pos2);
	}
	
	/*
	 * TODO: Beseitigt die Insel
	 */
	public static boolean clearIsland(final HashMap<Player, BukkitRunnable> runs, final Player p, final int island_id) {
		
		MessageType type = MessageType.WARNING;
		SkyBlock.sendConsoleMessage(type, "Die Insel mit der ID §f"+island_id+type.getSuffix()+" wurde vom Besitzer befreit.",
				"Diese Insel kann dennoch nicht neu geclaimt werden, weil sie noch von einem Admin gereinigt und anschließend mit §f/release "+island_id+type.getSuffix()+" freigegeben werden muss.");
		
//		IslandPaster.removeLiquidAndTileEntities(getProfile(p).cuboid);
//		SkyFileManager.saveIslandRemover(island_id);
		
//		runs.put(p,  new BukkitRunnable() {			
//			ArrayList<Block> blocks = getProfile(p).cuboid.blockList();
//			int a = 0;
//			int max = getProfile(p).cuboid.getTotalBlockSize();
//			@Override
//			public void run() {
//				for(int i = 0; i != 1; i++) {					
//					if(blocks.size() > a) {
//						final Block b = blocks.get(a);
//						if(b.getType() != Material.AIR && b.getType() != Material.CAVE_AIR) {
//							b.setType(Material.AIR);
//						}
//						a++;
//						if(a == max) {
//							SkyFileManager.markIslandAsClaimable(island_id);
//							SkyFileManager.removeIslandRemover(island_id);
//							SkyBlock.sendDeveloperMessage("Die Insel "+island_id+" ist nun wieder claimable");
//							runs.get(p).cancel();
//							runs.remove(p);
//						}
//					}else {
//						SkyFileManager.markIslandAsClaimable(island_id);
//						SkyFileManager.removeIslandRemover(island_id);
//						SkyBlock.sendDeveloperMessage("Die Insel "+island_id+" ist nun wieder claimable");
//						runs.get(p).cancel();
//						runs.remove(p);
//					}
//				}
//			}
//		});
//		runs.get(p).runTaskTimer(SkyBlock.getInstance(), 0l, 0l);
		return true;
	}
	public static boolean clearIsland(final int island_id) {
		MessageType type = MessageType.WARNING;
		SkyBlock.sendConsoleMessage(type, "Die Insel mit der ID §f"+island_id+type.getSuffix()+" wurde vom Besitzer befreit.",
				"Diese Insel kann dennoch nicht neu geclaimt werden, weil sie noch von einem Admin gereinigt und anschließend mit §f/claimable "+island_id+type.getSuffix()+" freigegeben werden muss.");
		
//		IslandPaster.removeLiquidAndTileEntities(getIslandCuboid(island_id));
		
//		final BukkitRunnable runner = new BukkitRunnable() {
//			ArrayList<Block> blocks = getIslandCuboid(island_id).blockList();
//			int a = 0;
//			int max = getIslandCuboid(island_id).getTotalBlockSize();
//			@Override
//			public void run() {-
//				for(int i = 0; i != 1; i++) {					
//					if(blocks.size() > a) {
//						final Block b = blocks.get(a);
//						if(b.getType() != Material.AIR && b.getType() != Material.CAVE_AIR) {
//							b.setType(Material.AIR);
//						}
//						a++;
//						if(a == max) {
//							SkyFileManager.markIslandAsClaimable(island_id);
//							SkyFileManager.removeIslandRemover(island_id);
//							SkyBlock.sendDeveloperMessage(MessageType.INFO, "Die Insel Nummer "+island_id+" ist nun wieder claimable");
//							cancel();
//						}
//					}else {
//						SkyFileManager.markIslandAsClaimable(island_id);
//						SkyFileManager.removeIslandRemover(island_id);
//						SkyBlock.sendDeveloperMessage(MessageType.INFO, "Die Insel Nummer "+island_id+" ist nun wieder claimable");
//						cancel();
//					}
//				}
//			}
//		};
//		runner.runTaskTimer(SkyBlock.getInstance(), 0l, 0l);
		return true;
	}
	
	/**
	 * Gibt die InselID der Insel zurück, auf der sich die Location loc befindet.
	 * @param loc
	 * @return
	 */
	public static int getIslandId(Location loc) {
		for(IslandData data : islandDatas.values()) if(data.getVolume().isIn(loc)) return data.getIslandId(); 
		return 0;
	}
	
	public static class IslandData {
		
		private int island_id = 0;
		private UUID owner_uuid = null;
		private Cuboid volume = null;
		private Location loc1, loc2;
		private Settings settings = null;
		private boolean hasOwner = true;
		
		public IslandData(int island_id, Location center, boolean hasOwner, String owner_uuid) {
			this.island_id = island_id;
			this.hasOwner = hasOwner;
			if(hasOwner) this.owner_uuid = UUID.fromString(owner_uuid);
			else this.owner_uuid = null;
			
			this.volume = new Cuboid(center.clone().add(-SkyBlockGenerator.issize, 0, -SkyBlockGenerator.issize),
					center.clone().add(SkyBlockGenerator.issize, 256, SkyBlockGenerator.issize));
			loc1 = volume.getPoint1();
			loc2 = volume.getPoint2();
			
			if(hasOwner) {
				settings = new Settings(island_id);
			}
		}
		
		public void checkForBannedLocation(Player p) {
			if(getSettings() != null && getVolume() != null && getVolume().isIn(p.getLocation()) && getSettings().isBanned(PlayerProfiler.getUUID(p))) {
				if(p.isOp()) return;
				else {
					BukkitRunnable runner = new BukkitRunnable() { @Override public void run() { p.performCommand("spawn");} };
					runner.runTask(SkyBlock.getSB());
				}
			}
		}
		
		public void save() {
			if(settings != null) settings.save();
		}
		/**
		 * Registriert in der Data Klasse einen Owner, wenn keiner vorhanden ist.
		 * @param uuid
		 * @return
		 */
		public boolean setNewOwner(UUID uuid) {
			if(hasOwner() == false) {
				this.owner_uuid = uuid;
				settings = new Settings(island_id);
			}
			else return false;
			return true;
		}
		/**
		 * Gibt die IslandID zurück
		 * @return
		 */
		public int getIslandId() {
			return island_id;
		}
		/**
		 * Gibt die UUID des Owners zurück
		 * @return
		 */
		public UUID getOwnerUuid() {
			return owner_uuid;
		}
		/**
		 * Gibt das Volumen des Insel-Grundstücks zurück
		 * @return
		 */
		public Cuboid getVolume() {
			return volume;
		}
		/**
		 * Gibt die Location 1 des Insel-Grundstücks zurück
		 * @return
		 */
		public Location getLoc1() {
			return loc1.clone();
		}
		/**
		 * Gibt die Location 2 des Insel-Grundstücks zurück
		 * @return
		 */
		public Location getLoc2() {
			return loc2.clone();
		}
		/**
		 * Lädt die Einstellungen nachträglich
		 * @return
		 */
		public boolean loadSettings() {
			settings = new Settings(island_id);
			return true;
		}
		/**
		 * Gibt die Settings des Insel-Grundstücks zurück
		 * @return
		 */
		public Settings getSettings() {
			return settings;
		}
		/**
		 * BEarbeitet eine Einstellung des Insel-Grundstücks zurück
		 * @return
		 */
		public boolean editSettings(String setting, Object value, Player player) {
			return settings.edit(setting, value, player);
		}
		/**
		 * Gibt an, ob diese Insel einen Beitzer hat
		 * @return
		 */
		public boolean hasOwner() {
			return hasOwner;
		}
		
		
		public class Settings {
			
			private boolean allowVisiting = true;
			private boolean fireSpread = false;
			private boolean monsterSpawning = true;
			private boolean animalSpawning = true;
			private boolean tntDamage = false;
			private boolean mobGriefing = false;
			private boolean pvp = false;
			
			private ArrayList<String> banned_players = null;
			
			private int island_id = 0;
			
			public Settings(int island_id) {
				this.island_id = island_id;
				load(false);
			}
			
			public void load(boolean saveBefore) {
				if(saveBefore) save();
				if(PlayerProfiler.isRegistered(owner_uuid)) {
					File file = new File("plugins/SkyBlock/Inseln/"+owner_uuid.toString()+"-Insel.yml");
					if(file.exists() == false) return;
					FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
					
					allowVisiting = cfg.getBoolean("Islands.Settings.Allow Visiting");
					fireSpread = cfg.getBoolean("Islands.Settings.Firespread");
					monsterSpawning = cfg.getBoolean("Islands.Settings.Monsterspawning");
					animalSpawning = cfg.getBoolean("Islands.Settings.Animalspawning");
					tntDamage = cfg.getBoolean("Islands.Settings.TNT Damage");
					mobGriefing = cfg.getBoolean("Islands.Settings.Mob Griefing");
					pvp = cfg.getBoolean("Islands.Settings.PVP");
					
				}
				
				banned_players = SkyFileManager.getBannedPlayers(owner_uuid.toString());
			}
			
			public boolean isBanned(UUID uuid) {
				return banned_players.contains(uuid.toString());
			}
			
			public String getStringListOfBannedPlayer() {
				ArrayList<String> banned = getSettings().getBannedPlayers();
				String t = "§3"+PlayerAtlas.getPlayername(banned.get(0));
				for(int i = 1; i != banned.size(); i++) t = t + "§f, §3"+PlayerAtlas.getPlayername(banned.get(i));
				return t;
			}
			
			public void save() {
				//TODO
				if(PlayerProfiler.isRegistered(owner_uuid)) {
					File file = new File("plugins/SkyBlock/Inseln/"+owner_uuid.toString()+"-Insel.yml");
					if(file.exists() == false) return;
					FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
					
					cfg.set("Islands.Settings.Allow Visiting", allowVisiting);
					cfg.set("Islands.Settings.Firespread", fireSpread);
					cfg.set("Islands.Settings.Monsterspawning", monsterSpawning);
					cfg.set("Islands.Settings.Animalspawning", animalSpawning);
					cfg.set("Islands.Settings.TNT Damage", tntDamage);
					cfg.set("Islands.Settings.Mob Griefing", mobGriefing);
					cfg.set("Islands.Settings.PVP", pvp);
					
					try {
						cfg.save(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}
			
			public boolean edit(String setting, Object value, Player player) {
				//TODO
				if(PlayerProfiler.isRegistered(owner_uuid) == false) return false;
				if(value instanceof Integer) {
					PlayerProfiler.getPlayer(owner_uuid).sendMessage("IslandManager.IslandData.Settings: Value ist ein INT");
					return true;
				}else if(value instanceof Boolean) {
					if(setting.equals("allowVisiting")) {
						allowVisiting = (boolean)value;
						SkyBlock.sendMessage(MessageType.WARNING, player, "Eigenschaft §bErlaube Besuche §czu §3"+value+" §cgeändert");
						for(String uuid : getProfile(player).getMembers())
							if(Bukkit.getPlayer(UUID.fromString(uuid)) != null) 
							SkyBlock.sendMessage(MessageType.WARNING, Bukkit.getPlayer(UUID.fromString(uuid)), "Eigenschaft §bErlaube Besuche §czu §3"+value+" §cgeändert");
					} else if(setting.equals("fireSpread")) {
						fireSpread = (boolean)value;
						SkyBlock.sendMessage(MessageType.WARNING, player, "Eigenschaft §bErlaube Feuerausbreitung §czu §3"+value+" §cgeändert");
						for(String uuid : getProfile(player).getMembers())
							if(Bukkit.getPlayer(UUID.fromString(uuid)) != null)
								SkyBlock.sendMessage(MessageType.WARNING, Bukkit.getPlayer(UUID.fromString(uuid)), "Eigenschaft §bErlaube Feuerausbreitung §czu §3"+value+" §cgeändert");
					} else if(setting.equals("monsterSpawning")) {
						monsterSpawning = (boolean)value;
						SkyBlock.sendMessage(MessageType.WARNING, player, "Eigenschaft §bErlaube natürliche Monster-Spawning §czu §3"+value+" §cgeändert");
						for(String uuid : getProfile(player).getMembers())
							if(Bukkit.getPlayer(UUID.fromString(uuid)) != null) 
							SkyBlock.sendMessage(MessageType.WARNING, Bukkit.getPlayer(UUID.fromString(uuid)), "Eigenschaft §bErlaube natürliche Monster-Spawning §czu §3"+value+" §cgeändert");
					} else if(setting.equals("animalSpawning")) {
						animalSpawning = (boolean)value;
						SkyBlock.sendMessage(MessageType.WARNING, player, "Eigenschaft §bErlaube natürliche Tiere-Spawning §czu §3"+value+" §cgeändert");
						for(String uuid : getProfile(player).getMembers())
							if(Bukkit.getPlayer(UUID.fromString(uuid)) != null) 
							SkyBlock.sendMessage(MessageType.WARNING, Bukkit.getPlayer(UUID.fromString(uuid)), "Eigenschaft §bErlaube natürliche Tiere-Spawning §czu §3"+value+" §cgeändert");
					} else if(setting.equals("tntDamage")) {
						tntDamage = (boolean)value;
						SkyBlock.sendMessage(MessageType.WARNING, player, "Eigenschaft §bErlaube Zerstörung durch TNT §czu §3"+value+" §cgeändert");
						for(String uuid : getProfile(player).getMembers())
							if(Bukkit.getPlayer(UUID.fromString(uuid)) != null) 
							SkyBlock.sendMessage(MessageType.WARNING, Bukkit.getPlayer(UUID.fromString(uuid)), "Eigenschaft §bErlaube Zerstörung durch TNT §czu §3"+value+" §cgeändert");
					} else if(setting.equals("mobGriefing")) {
						mobGriefing = (boolean)value;
						SkyBlock.sendMessage(MessageType.WARNING, player, "Eigenschaft §bErlaube Zerstörung durch Mobs §czu §3"+value+" §cgeändert");
						for(String uuid : getProfile(player).getMembers())
							if(Bukkit.getPlayer(UUID.fromString(uuid)) != null)
							SkyBlock.sendMessage(MessageType.WARNING, Bukkit.getPlayer(UUID.fromString(uuid)), "Eigenschaft §bErlaube Zerstörung durch Mobs §czu §3"+value+" §cgeändert");
					} else if(setting.equals("pvp")) {
						pvp = (boolean)value;
						SkyBlock.sendMessage(MessageType.WARNING, player, "Eigenschaft §bPVP §czu §3"+value+" §cgeändert");
						for(String uuid : getProfile(player).getMembers())
							if(Bukkit.getPlayer(UUID.fromString(uuid)) != null)
							SkyBlock.sendMessage(MessageType.WARNING, Bukkit.getPlayer(UUID.fromString(uuid)), "Eigenschaft §bPVP §czu §3"+value+" §cgeändert");
					}			
					return true;
				}else if(value instanceof Float) {
					PlayerProfiler.getPlayer(owner_uuid).sendMessage("IslandManager.IslandData.Settings: Value ist ein FLOAT");
					return true;
				}else if(value instanceof String) {
					PlayerProfiler.getPlayer(owner_uuid).sendMessage("IslandManager.IslandData.Settings: Value ist ein STRING");
					return true;
				}else if(value instanceof Double) {
					PlayerProfiler.getPlayer(owner_uuid).sendMessage("IslandManager.IslandData.Settings: Value ist ein DOUBLE");
					return true;
				}else PlayerProfiler.getPlayer(owner_uuid).sendMessage("IslandManager.IslandData.Settings: Kein Type erkannt");
				return false;
			}
			
			public ArrayList<String> getBannedPlayers() {
				return (ArrayList<String>) banned_players;
			}
			
			public int getIslandID() {
				return island_id;
			}
			
			public boolean pvp() {
				return pvp;
			}

			public boolean isAllowVisiting() {
				return allowVisiting;
			}

			public boolean isFireSpread() {
				return fireSpread;
			}

			public boolean isMonsterSpawning() {
				return monsterSpawning;
			}

			public boolean isAnimalSpawning() {
				return animalSpawning;
			}

			public boolean isTntDamage() {
				return tntDamage;
			}

			public boolean isMobGriefing() {
				return mobGriefing;
			}

			public boolean isPvp() {
				return pvp;
			}
			
		}
		
	}
	
	public static class IslandProfile {
		
		private int id = 0;
		private UUID uuid = null;
		private Location spawnpoint = null;
		private Cuboid cuboid = null;
		private ArrayList<String> members = new ArrayList<String>();
		
		public IslandProfile(int island_id, UUID owner) {
			this.id = island_id;
			this.uuid = owner;
			
			if(SkyFileManager.hasIsland(owner.toString())) {
				spawnpoint = SkyFileManager.getIslandPlayerSpawn(uuid.toString());
				
				int x1 = SkyFileManager.getLocationX(owner.toString())-(SkyBlockGenerator.issize/2);
				int z1 = SkyFileManager.getLocationZ(owner.toString())-(SkyBlockGenerator.issize/2);
				int x2 = x1+(SkyBlockGenerator.issize);
				int z2 = z1+(SkyBlockGenerator.issize);
				if(SkyFileManager.getWorldName(owner.toString()) == null) Bukkit.broadcastMessage("getWorldName == null");
				if(Bukkit.getWorld(SkyFileManager.getWorldName(owner.toString())) == null) Bukkit.broadcastMessage("getWorld == null");
				Location pos1 = new Location(Bukkit.getWorld(SkyFileManager.getWorldName(owner.toString())), x1, 0, z1);
				Location pos2 = new Location(Bukkit.getWorld(SkyFileManager.getWorldName(owner.toString())), x2, 256, z2);
				this.cuboid = new Cuboid(pos1, pos2);
				members = getMembers();
			}
		}
		
		/**
		 * Lädt Einstellungen und Daten einer Insel neu
		 * @param
		 */
		public void reload() {
			if(SkyFileManager.hasIsland(uuid.toString())) {
				spawnpoint = SkyFileManager.getIslandPlayerSpawn(uuid.toString());
				
				int x1 = SkyFileManager.getLocationX(uuid.toString())-(SkyBlockGenerator.issize/2);
				int z1 = SkyFileManager.getLocationZ(uuid.toString())-(SkyBlockGenerator.issize/2);
				int x2 = x1+(SkyBlockGenerator.issize);
				int z2 = z1+(SkyBlockGenerator.issize);
				if(SkyFileManager.getWorldName(uuid.toString()) == null) Bukkit.broadcastMessage("getWorldName == null");
				if(Bukkit.getWorld(SkyFileManager.getWorldName(uuid.toString())) == null) Bukkit.broadcastMessage("getWorld == null");
				Location pos1 = new Location(Bukkit.getWorld(SkyFileManager.getWorldName(uuid.toString())), x1, 0, z1);
				Location pos2 = new Location(Bukkit.getWorld(SkyFileManager.getWorldName(uuid.toString())), x2, 256, z2);
				this.cuboid = new Cuboid(pos1, pos2);	
				members = getMembers();
			}
		}
		
		/*
		 * TODO: Gibt den Spawnpunkt einer Insel zurück
		 */
		public Location getSpawnpoint() {
			return spawnpoint;
		}
		
		/*
		 * TODO: Gibt die ID von der Insel zurück
		 */
		public int getIslandID() {
			return id;
		}
		
		/*
		 * TODO: Gibt die UUID des Owners zurück
		 */
		public UUID getOwnerUUID() {
			return uuid;
		}
		
		/*
		 * TODO: Gibt zurück, ob der Spieler p sich auf dem Grundstück der Insel befindet
		 */
		public boolean isInIslandregion(Player p) {
			return cuboid.isIn(p);
		}
		
		/**
		 * Gibt eine ArrayList<UUID> mit den Insel-Members zurück.
		 * @return
		 */
		public ArrayList<String> getMembers() {
			return SkyFileManager.getMembers(uuid.toString());
		}
		
		/**
		 * Fügt einen neuen Member hinzu.
		 * @param uuid
		 * @return
		 */
		public boolean addMember(Player new_member) {
			if(members.contains(PlayerProfiler.getUUID(new_member).toString())) loadProfile(new_member);
			if(uuid == null) {
				unloadProfile(new_member);
				loadProfile(new_member);
			}
			if(SkyFileManager.addMember(PlayerProfiler.getPlayer(uuid), new_member)) {
				members.add(PlayerProfiler.getUUID(new_member).toString());
				return true;
			}else return false;
		}
		
		/**
		 * Entfernt ein Member von der Insel. Er hat nun keine Rechte mehr.
		 * @param member
		 * @return
		 */
		public boolean removeMember(Player member) {
			if(members.contains(PlayerProfiler.getUUID(member).toString()) == false) return false;
			if(SkyFileManager.removeMember(uuid.toString(), PlayerProfiler.getUUID(member).toString())) {
				members.remove(PlayerProfiler.getUUID(member).toString());
				return true;
			}else return false;
		}
	}
	
	public static class Standort {
		
		private String name = "";
		private Location loc = null;
		private int island_id = 0;
		private IslandData data = null;
		
		public Standort() {
		}
		public Standort(IslandData data) {
			this.data = data;
		}
		public Standort(IslandData data, String name, Location loc) {
			this.data = data;
			this.name = name;
			if(loc == null) this.loc = null;
			else this.loc = loc.clone();
		}
		public IslandData getIslandData() {
			return data;
		}
		public String getName() {
			return name;
		}
		public Standort setName(String name, Location loc) {
			this.name = name;
			if(loc == null) this.loc = null;
			else this.loc = loc.clone();
			return this;
		}
		public Location getLoc() {
			return loc;
		}
		public Standort setLoc(Location loc) {
			if(loc == null) this.loc = null;
			else this.loc = loc.clone();
			return this;
		}
		public int getIslandId() {
			return island_id;
		}
		public Standort setIslandId(int island_id, Location loc, IslandData data) {
			this.island_id = island_id;
			this.data = data;
			if(loc == null) this.loc = null;
			else this.loc = loc.clone();
			return this;
		}
		public Standort reset() {
			setName("", null);
			setLoc(null);
			setIslandId(1, null, null);
			return this;
		}
		
		
	}
}
