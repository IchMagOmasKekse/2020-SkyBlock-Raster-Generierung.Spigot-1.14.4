package me.ichmagomaskekse.de.area;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.ichmagomaskekse.de.SkyBlock;
import me.ichmagomaskekse.de.schematics.SkyRect;

public class BuildProtection implements Listener {
	
	public Location spawn_pos1 = new Location(Bukkit.getWorld("world"), -190, 0, -32);
	public Location spawn_pos2 = new Location(Bukkit.getWorld("world"), 425, 248, 658);
	public SkyRect spawn = new SkyRect(spawn_pos1, spawn_pos2);
	
	private HashMap<String, SkyRect> rects = new HashMap<String, SkyRect>();
	
	public BuildProtection() {
		SkyBlock.getInstance().getServer().getPluginManager().registerEvents(this, SkyBlock.getInstance());
		rects.put("spawn", new SkyRect(new Location(Bukkit.getWorld("world"), -190, 0, -32), new Location(Bukkit.getWorld("world"), 425, 248, 658)));
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if(p.isOp() == false && p.hasPermission("skyblock.modify.skyrects") == false) {
			for(String s : rects.keySet()) {
				if(rects.get(s).isIn(e.getBlockPlaced().getLocation())) {
					e.setBuild(false);
					e.setCancelled(false);
				}
			}
		}
	}
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(p.isOp() == false && p.hasPermission("skyblock.modify.skyrects") == false) {
			for(String s : rects.keySet()) {
				if(rects.get(s).isIn(e.getBlock().getLocation())) {
					e.setCancelled(false);
				}
			}
		}
	}
	@EventHandler
	public void onInteract(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		if(p.isOp() == false && p.hasPermission("skyblock.interact.entity") == false) {
			for(String s : rects.keySet()) {
				if(rects.get(s).isIn(e.getRightClicked().getLocation())) {
					e.setCancelled(false);
				}
			}
		}
	}
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {			
			if(p.isOp() == false && p.hasPermission("skyblock.interact.blocks") == false) {
				for(String s : rects.keySet()) {
					if(rects.get(s).isIn(e.getClickedBlock().getLocation())) {
						e.setCancelled(false);
					}
				}
			}
		}
	}

}
