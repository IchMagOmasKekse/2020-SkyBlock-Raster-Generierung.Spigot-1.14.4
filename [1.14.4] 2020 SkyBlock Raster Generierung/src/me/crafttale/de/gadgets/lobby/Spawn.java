package me.crafttale.de.gadgets.lobby;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.crafttale.de.Cuboid;
import me.crafttale.de.SkyWorld;
import me.crafttale.de.profiles.PlayerProfiler;

public class Spawn {
	
	private static Location spawn_teleport_area_pos1 = new Location(Bukkit.getWorld("skyblockworld"), 17, 86, 74 , -90, 0);
	private static Location spawn_teleport_area_pos2 = new Location(Bukkit.getWorld("skyblockworld"), 19, 86, 76, -90, 0);
	
	private static Cuboid spawn_teleport_are = null;
	
	private static ArrayList<Cuboid> areas = new ArrayList<Cuboid>();
	
	public Spawn() {
		World w = Bukkit.getWorld(SkyWorld.skyblockworld);
		areas.add(new Cuboid(new Location(w, 40, 81, 72), new Location(w, 31, 89, 78))); //SkyBlock Portal
		areas.add(new Cuboid(new Location(w, 31, 89, 60), new Location(w, 40, 81, 69))); //Boostpad links
		areas.add(new Cuboid(new Location(w, 31, 89, 81), new Location(w, 40, 81, 90))); //Boostpad rechts
		areas.add(new Cuboid(new Location(w, 23, 86, 90), new Location(w, 30, 80, 93))); // Treppe Rechts
		areas.add(new Cuboid(new Location(w, 23, 86, 60), new Location(w, 30, 80, 57))); // Treppe Links
		areas.add(new Cuboid(new Location(w, -1, 91, 95), new Location(w, 8, 85, 85))); // Loot-Chest rechts
		areas.add(new Cuboid(new Location(w, -1, 91, 55), new Location(w, 8, 85, 65))); // Loot-Chest rechts
		spawn_teleport_are = new Cuboid(spawn_teleport_area_pos1, spawn_teleport_area_pos2);

	}
	
	/**
	 * Gibt eine zufällige Location in der definierten Zone zurück.
	 * @return
	 */
	public static Location getRandomLocationInSpawnArea() {
		Location loc = spawn_teleport_are.getRandomLocation();
		loc.setY(86+1);
		loc.setYaw(-90);
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
	
	public static void onRespawn(PlayerRespawnEvent e) {
		e.setRespawnLocation(Spawn.getRandomLocationInSpawnArea());
	}
	
}
