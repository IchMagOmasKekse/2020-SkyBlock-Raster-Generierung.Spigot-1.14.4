package me.skytale.de.area;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.skytale.de.SkyBlock;
import me.skytale.de.SkyWorld;
import me.skytale.de.filemanagement.SkyFileManager;
import me.skytale.de.profiles.IslandManager;
import me.skytale.de.profiles.PlayerProfiler;

public class IslandProtection implements Listener {
	
	public IslandProtection() {
		SkyBlock.getSB().getServer().getPluginManager().registerEvents(this, SkyBlock.getSB());
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		/* Abfragen, ob der Spieler gerade einen Block auf einer Insel platzieren m�chte */
		if(p.getWorld().getName().equals(SkyWorld.skyblockworld)) {
			if(p.hasPermission("skyblock.modify.islands") == false && p.hasPermission("skyblock.*") == false && p.isOp() == false) {
				e.setCancelled(true);
				if(SkyFileManager.hasIsland(p) || SkyFileManager.isMemberOfAnIsland(PlayerProfiler.getUUID(p))) {
					if(IslandManager.getIslandCuboid(IslandManager.getProfile(p).getIslandID()).isIn(e.getBlockPlaced().getLocation())) {
						e.setCancelled(false);
					} else SkyBlock.sendNoPermissionTitle(p);
				}else {
					e.setCancelled(true);
					SkyBlock.sendNoPermissionTitle(p);
				}
			}
		}
	}
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		/* Abfragen, ob der Spieler gerade einen Block auf einer Insel platzieren m�chte */
		if(p.getWorld().getName().equals(SkyWorld.skyblockworld)) {
			if(p.hasPermission("skyblock.modify.islands") == false && p.hasPermission("skyblock.*") == false && p.isOp() == false) {
				e.setCancelled(true);
				if(SkyFileManager.hasIsland(p) || SkyFileManager.isMemberOfAnIsland(PlayerProfiler.getUUID(p))) {
					if(IslandManager.getIslandCuboid(IslandManager.getProfile(p).getIslandID()).isIn(e.getBlock().getLocation())) {						
						e.setCancelled(false);
					}else SkyBlock.sendNoPermissionTitle(p);
				}else if(p.hasPermission("skyblock.*") == false && p.isOp() == false){
					e.setCancelled(true);
					SkyBlock.sendNoPermissionTitle(p);
				}
			}
		}
	}
	@EventHandler
	public void onInteract(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		/* Abfragen, ob der Spieler gerade mit einem Entity auf einer Insel interagieren m�chte */
		if(p.getWorld().getName().equals(SkyWorld.skyblockworld)) {
			if(p.hasPermission("skyblock.interact.entity") == false && p.hasPermission("skyblock.*") == false && p.isOp() == false) {
				e.setCancelled(true);
				if(SkyFileManager.hasIsland(p) || SkyFileManager.isMemberOfAnIsland(PlayerProfiler.getUUID(p))) {
					if(IslandManager.getIslandCuboid(IslandManager.getProfile(p).getIslandID()).isIn(e.getRightClicked().getLocation()))
						e.setCancelled(false);					
					else SkyBlock.sendNoPermissionTitle(p);
				}else if(p.hasPermission("skyblock.*") == false && p.isOp() == false){
					e.setCancelled(true);
				}
			}
		}else {
			
		}
	}
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if(p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType().isBlock()) return;
				if(p.getInventory().getItemInOffHand() != null && p.getInventory().getItemInOffHand().getType().isBlock()) return;
				
				/* Abfragen, ob der Spieler gerade versucht mit einem Block zu interagieren */
				if(p.getWorld().getName().equals(SkyWorld.skyblockworld)) {
					if(p.hasPermission("skyblock.modify.islands") == false && p.hasPermission("skyblock.*") == false && p.isOp() == false) {
						e.setCancelled(true);
						if(SkyFileManager.hasIsland(p) || SkyFileManager.isMemberOfAnIsland(PlayerProfiler.getUUID(p))) {
							if(IslandManager.getIslandCuboid(IslandManager.getProfile(p).getIslandID()).isIn(e.getClickedBlock().getLocation()))
								e.setCancelled(false);
							else SkyBlock.sendNoPermissionTitle(p);
						}else if(p.hasPermission("skyblock.*") == false && p.isOp() == false){
							e.setCancelled(true);
							SkyBlock.sendNoPermissionTitle(p);
						}
					}
				}
			}
		}
	}
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			
			Player p = (Player) e.getDamager();
			if(p.getWorld().getName().equals(SkyWorld.skyblockworld)) {
				
				e.setCancelled(true);
				SkyBlock.sendNoPermissionTitle(p);
			}
		}
	}

}