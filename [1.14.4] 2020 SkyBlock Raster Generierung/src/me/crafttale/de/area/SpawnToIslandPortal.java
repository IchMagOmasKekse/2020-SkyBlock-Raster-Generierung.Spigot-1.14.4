package me.crafttale.de.area;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.crafttale.de.SkyBlock;
import me.crafttale.de.schematics.SkyRect;

public class SpawnToIslandPortal implements Listener {
	
	
	public Location spawn_to_island_pos1 = new Location(Bukkit.getWorld("world"), 87, 120, 327);
	public Location spawn_to_island_pos2 = new Location(Bukkit.getWorld("world"), 83, 115, 328);
	public SkyRect rect = new SkyRect(spawn_to_island_pos1, spawn_to_island_pos2);
	public BukkitRunnable checker = null;
	
	public SpawnToIslandPortal() {
		checker = new BukkitRunnable() {
			
			@Override
			public void run() {
				check();
			}
		};
		checker.runTaskTimer(SkyBlock.getSB(), 0l, 2l);
		SkyBlock.getSB().getServer().getPluginManager().registerEvents(this,  SkyBlock.getSB());
	}
	
	/*
	 * TODO: check() überprüft jeden Spieler, ob diese in dem Portal ist
	 */
	public void check() {
		for(Player p : Bukkit.getWorld("world").getPlayers()) {
			if(rect.isIn(p.getLocation().clone())) {
				p.performCommand("is");
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onNether(PlayerPortalEvent e) {
		if(e.getPlayer().getWorld().getName().equals(rect.getWorld().getName())) {
			if(rect.isIn(e.getPlayer().getLocation().clone())) {
				e.setCancelled(true);
				e.setTo(SkyBlock.spawn);
				e.getPlayer().performCommand("is");
			}
		}
	}
	
}
