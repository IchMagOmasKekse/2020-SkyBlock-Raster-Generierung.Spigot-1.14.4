package me.crafttale.de.gadgets.lobby;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.crafttale.de.ActionBar;
import me.crafttale.de.Cuboid;
import me.crafttale.de.SkyBlock;
import me.crafttale.de.SkyWorld;
import me.crafttale.de.gadgets.JumpPads;
import me.crafttale.de.gui.GUI.GUIType;
import me.crafttale.de.profiles.PlayerProfiler;

@SuppressWarnings("unused")
public class Spawn {
	
	public static String spawn_world_name = "world";
	public static String shop_world_name  = "shopworld";
	
	/* Definition der SpawnArea in der die Spieler spawnen, wenn sie Joinen */
	private static Location playerspawn_pos1 = new Location(Bukkit.getWorld(spawn_world_name),   14, 100,  172, 0, 0);
	private static Location playerspawn_pos2 = new Location(Bukkit.getWorld(spawn_world_name),   34,  89,  152, 0, 0);
	private static Location spawn_pos1       = new Location(Bukkit.getWorld(spawn_world_name), -150,  60,  580, 0, 0);
	private static Location spawn_pos2       = new Location(Bukkit.getWorld(spawn_world_name),  550, 400, -100, 0, 0);
	
	private static Location sb_portal_pos1   = new Location(Bukkit.getWorld(spawn_world_name),  84,  116,  326,  0, 0);
	private static Location sb_portal_pos2   = new Location(Bukkit.getWorld(spawn_world_name),  87,  113,  323,  0, 0);
	
	
	/* Back to Lobby Portals */
	
	private static Location spawn_teleport_area_pos1 = null;
	private static Location spawn_teleport_area_pos2 = null;
	
	private static Cuboid cuboid_spawn = null;
	private static Cuboid portal_sb= null;
	
	private static Cuboid spawn_teleport_area = null;
	
	private static HashMap<String, Cuboid> areas = new HashMap<String, Cuboid>();
	
	private static BukkitRunnable timer = null;
	
	public Spawn() {
		World w = Bukkit.getWorld(spawn_world_name);
		spawn_teleport_area_pos1 = new Location(w, 93, 117, 313, -90, 0);
		spawn_teleport_area_pos2 = new Location(w, 89, 117, 309, -90, 0);
		
		areas.put(CuboidType.SKYBLOCK_PORTAL.getCodename(), new Cuboid(new Location(w, 89, 113, 321),  new Location(w, 82, 119, 328))); //SkyBlock Portal
		areas.put(CuboidType.PVP_ARENA.getCodename(),       new Cuboid(new Location(w, 221, 116, 342), new Location(w, 263, 133, 382)));
		areas.put(CuboidType.SHOP_ENTRY.getCodename(),      new Cuboid(new Location(w, 187, 137, 251), new Location(w, 181, 116, 285)));
		areas.put(CuboidType.SHOP_VOTE.getCodename(),       new Cuboid(new Location(w, 126, 112, 117), new Location(w, 116, 117, 103)));
		areas.put(CuboidType.RESOURCES_1_SHOP.getCodename(),      new Cuboid(new Location(w, 131, 112, 113), new Location(w, 136, 114, 109)));
		
		spawn_teleport_area = new Cuboid(spawn_teleport_area_pos1, spawn_teleport_area_pos2);
		
		cuboid_spawn = new Cuboid(spawn_pos1, spawn_pos2);		
		portal_sb = new Cuboid(sb_portal_pos1, sb_portal_pos2);
		
		timer = new BukkitRunnable() {
			
			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) {
					if(isAtSpawn(p)) {
						checkForPortal(p);
						checkForSubregion(p);					
					}
				}
			}
		};
		timer.runTaskTimerAsynchronously(SkyBlock.getSB(), 0l, 5l);
	}
	
	/**
	 * Check nach einer Subregion des Spieler
	 * @param p
	 */
	public static void checkForSubregion(Player p) {
		boolean b = false;
		for(String s : areas.keySet()) {
			
			if(areas.get(s).isIn(p.getLocation())) {
				ActionBar.send(false, "§fDein Standort: §eSpawn §7| §f"+s, p);
				b = true;
			}
		}
		JumpPads.checkJumpPad(p);
		if(b == false) ActionBar.send(false, "§fDein Standort: §eSpawn", p);
	}
	
	/**
	 * Gibt an, ob der Spieler am Spawn ist
	 * @param p
	 * @return
	 */
	public static boolean isAtSpawn(Player p) {
		if(cuboid_spawn != null) return cuboid_spawn.isIn(p.getLocation());
		else return false;
	}
	
	/**
	 * Überprüft, ob der Spieler in einem Portal ist
	 * @param p
	 */
	public void checkForPortal(Player p) {
		Location loc = p.getLocation();
		if(p.getWorld().getName().equals(spawn_world_name) || p.getWorld().getName().equals(shop_world_name)) {
			if(isAtSpawn(p)) {
				if(portal_sb.isIn(loc)) {
					new BukkitRunnable() {
						
						@Override
						public void run() {
							p.performCommand("is");
						}
					}.runTask(SkyBlock.getSB());
				}
			}
		}
	}
	
	/**
	 * Gibt eine zufällige Location in der definierten Zone zurück.
	 * @return
	 */
	public static Location getRandomLocationInSpawnArea() {
		Location loc = spawn_teleport_area.getRandomLocation();
		loc.setY(116+1);
		loc.setYaw(90);
		return loc;
	}
	
	public static void onDamage(EntityDamageEvent e) {
		if(e.getEntity().getWorld().getName().equals(SkyWorld.skyblockworld)) {
			if(e.getEntity() instanceof Player) {
				if(PlayerProfiler.getProfile((Player)e.getEntity()).getStandort().getName().toLowerCase().equals("spawn")) {					
					e.setCancelled(true);
					((Player)e.getEntity()).setHealth(((Player)e.getEntity()).getHealthScale());
				}
			}
		}
	}
	
	/**
	 * Behandelt das Respawnen eines Spielers.
	 * @param e
	 */
	public static void onRespawn(PlayerRespawnEvent e) {
		e.setRespawnLocation(Spawn.getRandomLocationInSpawnArea());
	}
	
	/**
	 * Behandelt das droppen eines Item von einem Spieler.
	 * @param e
	 */
	public static void onDropItem(PlayerDropItemEvent e) {
		if(e.getPlayer().getWorld().getName().equals(Spawn.spawn_world_name) &&
				e.getPlayer().getGameMode() != GameMode.CREATIVE &&
				e.getPlayer().isSneaking() == false) {			
			e.getItemDrop().setOwner(e.getPlayer().getUniqueId());
			e.getItemDrop().setThrower(e.getPlayer().getUniqueId());
		}
	}
	
	/**
	 * Behandelt einen Spieler, nachdem er gejoint ist.
	 * @param e
	 */
	public static void onJoin(PlayerJoinEvent e) {
		e.getPlayer().teleport(Spawn.getRandomLocationInSpawnArea());
	}
	
	public enum CuboidType {
		SKYBLOCK_PORTAL("§aSkyBlock§f-Portal"),
		BOOSTPAD_NORD("§bBoostpad§f-Nord"),
		BOOSTPAD_SÜD("§bBoostpad§f-Süd"),
		TREPPE_NORD("§3Treppe§f-Nord"),
		TREPPE_SÜD("§3Treppe§f-Süd"),
		LOOT_CHEST_NORD("§5Loot-Chest§f-Nord"),
		LOOT_CHEST_SÜD("§5Loot-Chest§f-Süd"),
		PVP_ARENA_EINGANG("§fEingang §cPVP-Arena"),
		PVP_ARENA("§cPVP-Arena"),
		SHOP_ENTRY("§bShop-Eingang"),
		SHOP_VOTE("§dVotehändler"),
		RESOURCES_1_SHOP("§bResourcenhändler");
		
		String codename = "";
		
		CuboidType(String codename) {
			this.codename = codename;
		}
		
		public String getCodename() {
			return codename;
		}
	}

	/**
	 * Spielt die Join Melody für einen Spieler ab.
	 * @param e
	 */
	public static void playJoinMelody(PlayerJoinEvent e) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Bukkit.dispatchCommand(e.getPlayer(), "gui "+GUIType.JOIN_MELODY.toString());
			}
		}.runTaskLater(SkyBlock.getSB(), 15l);
	}
	
}
