package me.dreamisland.de.events;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;

import me.dreamisland.de.Cuboid;
import me.dreamisland.de.SkyBlock;

public class BlockDetectorListener implements Listener {
	
	public static ConcurrentLinkedQueue<Cuboid> cancel_zones = new ConcurrentLinkedQueue<Cuboid>();
	
	public BlockDetectorListener() {
		//Registriere alle Envents in dieser Klasse
		SkyBlock.getSB().getServer().getPluginManager().registerEvents(this, SkyBlock.getSB());
	}
	
	
//	@EventHandler
//	public void onSneak(PlayerToggleSneakEvent e) {
//		if(e.isSneaking() && e.getPlayer().isFlying() == false && e.getPlayer().getGameMode() == GameMode.CREATIVE) {
//			Player p = e.getPlayer();
//			Block b = p.getLocation().getBlock();
//			p.sendMessage("Block �b"+b.getType().toString()+"["+b.isLiquid()+"]");
//		}
//	}
	
	@EventHandler
	public void onLiquid(BlockPhysicsEvent e) {
		if(e.getBlock().isLiquid()) {
			for(Cuboid c : cancel_zones) {
				if(c.isIn(e.getBlock().getLocation())) {
					e.getBlock().setType(Material.AIR);
					e.setCancelled(true);					
				}
			}
		}
	}
}
