package me.dreamisland.de;

import java.util.ArrayList;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.dreamisland.de.filemanagement.SkyFileManager;

public class SkyWorld implements Listener {
	
	public static final String skyblockworld = "skyblockworld";
	public static ArrayList<String> mob_names = new ArrayList<String>();
	
	public SkyWorld() {
		SkyBlock.getInstance().getServer().getPluginManager().registerEvents(this, SkyBlock.getInstance());
		mob_names.add("Shalom!");
		mob_names.add("Salute");
		mob_names.add("Naaa");
		mob_names.add("HELLO");
		mob_names.add("Tu mir nichts...!");
	}
	
	/**
	 * Soll Phantome despawnen und testweise Customnames verteilen
	 * @param e
	 */
	@EventHandler
	public void onMobSpawn(EntitySpawnEvent e) {
		if(e.getEntity().getWorld().getName().equals(skyblockworld)) {			
			if(e.getEntity() instanceof Monster) {
				if(e.getEntity().getType() == EntityType.PHANTOM) e.setCancelled(true);
			}else if(e.getEntity() instanceof LivingEntity) {
				e.getEntity().setCustomName(mob_names.get(SkyBlock.randomInteger(0, mob_names.size()-1)));
			}
		}
	}
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		e.setDeathMessage(Prefixes.DEATH.px()+e.getEntity().getName()+" ist flöten gegangen");
		if(e.getEntity().getWorld().getName().equals(skyblockworld)) {
			e.setKeepLevel(true);
			e.setKeepInventory(true);
			e.getDrops().clear();
			e.setDroppedExp(0);
		}
	}
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		if(Settings.respawnAtIslandIFDiedInSkyBlock()) {
			if(e.getPlayer().getWorld().getName().equals(skyblockworld)) {
				e.setRespawnLocation(SkyFileManager.getLocationOfIsland(e.getPlayer()).add(0.5,1.5,0.5));
			}			
		}
	}
	@EventHandler
	public void onRespawn(PlayerPortalEvent e) {
		if(e.getPlayer().getWorld().getName().equals(skyblockworld)) {
			e.setCancelled(true);
			e.getPlayer().teleport(SkyFileManager.getLocationOfIsland(e.getPlayer()).add(0.5,1.5,0.5));
		}
	}
	
	
	
}
