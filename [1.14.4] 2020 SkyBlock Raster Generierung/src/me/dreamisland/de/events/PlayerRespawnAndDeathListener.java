package me.dreamisland.de.events;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.dreamisland.de.Prefixes;
import me.dreamisland.de.SkyBlock;
import me.dreamisland.de.SkyBlockGenerator;
import me.dreamisland.de.filemanagement.SkyFileManager;

public class PlayerRespawnAndDeathListener implements Listener {
	
	public PlayerRespawnAndDeathListener() {
		SkyBlock.getInstance().getServer().getPluginManager().registerEvents(this, SkyBlock.getInstance());
	}
	
	/*
	 * TODO: onRespawn() Soll den Spieler zu seiner Insel teleportieren, sobald er in der SKyBlock Welt stirbt
	 */
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		if(e.getPlayer().getWorld().getName().equals(SkyBlockGenerator.skyworldName)) {
			Location respawn_loc = SkyFileManager.getLocationOfIsland(e.getPlayer()).add(0,0.5,0);
			e.setRespawnLocation(respawn_loc);
		}else if(e.getPlayer().getWorld().getName().equals("Insel")) {
			e.setRespawnLocation(SkyBlock.spawn.clone());
		}
		
	}
	
	/*
	 * TODO:
	 */
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		e.setDeathMessage(Prefixes.DEATH.getPrefix()+e.getEntity().getName()+" ist flöten gegangen");
	}
	
}
