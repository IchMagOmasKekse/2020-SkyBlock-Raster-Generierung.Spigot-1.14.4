package me.crafttale.de.area;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.crafttale.de.PlayerAtlas;
import me.crafttale.de.Settings;
import me.crafttale.de.SkyBlock;
import me.crafttale.de.SkyWorld;
import me.crafttale.de.casino.CasinoManager;
import me.crafttale.de.economy.ShopManager;
import me.crafttale.de.filemanagement.SkyFileManager;
import me.crafttale.de.log.LogBlock;
import me.crafttale.de.models.ModelManager;
import me.crafttale.de.profiles.IslandManager;
import me.crafttale.de.profiles.PlayerProfiler;

public class IslandProtection implements Listener {
	
	private static HashMap<Player, BukkitRunnable> spawnProtections = new HashMap<Player, BukkitRunnable>();
	
	public IslandProtection() {
		SkyBlock.getSB().getServer().getPluginManager().registerEvents(this, SkyBlock.getSB());
	}
	
	public static void giveSpawnProtection(Player p, boolean resetIfExist) {
		if(spawnProtections.containsKey(p) && resetIfExist) {
			spawnProtections.get(p).cancel();
			spawnProtections.remove(p);
		}
		if(spawnProtections.containsKey(p) == false) {
			spawnProtections.put(p, new BukkitRunnable() {
				
				@Override
				public void run() {
					spawnProtections.remove(p);
				}
				
			});
			spawnProtections.get(p).runTaskLater(SkyBlock.getSB(), 4*20l);
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if(e.getBlock().getWorld().getName().equals(SkyBlock.spawnWorldName)) {
			if(SkyBlock.hasPermission(e.getPlayer(), "skyblock.modify.spawn") == false || e.getPlayer().isOp() == false) e.setCancelled(true);				
		}
		Player p = e.getPlayer();
		/* Abfragen, ob der Spieler gerade einen Block auf einer Insel platzieren möchte */
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
		ModelManager.onPlace(e);
		ShopManager.onPlace(e);
		LogBlock.log(e.getBlock().getLocation(), e.getPlayer());
	}
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if(e.getBlock().getWorld().getName().equals(SkyBlock.spawnWorldName)) {
			if(SkyBlock.hasPermission(e.getPlayer(), "skyblock.modify.spawn") == false || e.getPlayer().isOp() == false) e.setCancelled(true);				
		}
		Player p = e.getPlayer();
		/* Abfragen, ob der Spieler gerade einen Block auf einer Insel platzieren möchte */
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
		ModelManager.onBreak(e);
		if(e.getPlayer().isSneaking() && e.getPlayer().hasPermission("skyblock.logblock")) {
			e.setCancelled(true);
			UUID uuid = LogBlock.getChanger(e.getBlock().getLocation());
			if(uuid == null) p.sendMessage("§3Hier war niemand");
			else p.sendMessage("§3Hier war §b"+PlayerAtlas.getPlayername(uuid.toString()));
		}else LogBlock.log(e.getBlock().getLocation(), e.getPlayer());
	}
	@EventHandler
	public void onManipulate(PlayerArmorStandManipulateEvent e) {
		ModelManager.onManipulate(e);
	}
	@EventHandler
	public void onInteract(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		if(e.getRightClicked() instanceof Evoker) {
			if(e.getRightClicked().getCustomName().equals("Portal Wächter")) {
				p.sendMessage("IslandProtection.onInteract");
			}
		}
		/* Abfragen, ob der Spieler gerade mit einem Entity auf einer Insel interagieren möchte */
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
		}
		ShopManager.onInteractEntity(e);
	}
	@EventHandler
	public void onInteract(PlayerInteractAtEntityEvent e) {		
		ModelManager.onInteractEntity(e);
	}
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		ModelManager.checkInput(e.getPlayer(), e.getItem(), e.getAction());
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
		ModelManager.onInteract(e);
	}
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if(e.getEntity().getWorld().getName().equals(SkyBlock.spawnWorldName)) {
			if((e.getCause() == DamageCause.BLOCK_EXPLOSION || e.getCause() == DamageCause.ENTITY_EXPLOSION)) e.setCancelled(true);
			else if(e.getCause() == DamageCause.ENTITY_ATTACK) e.setCancelled(false);
			else e.setCancelled(true);
		}
		
		if(e.getEntity() instanceof Player && spawnProtections.containsKey(((Player)e.getEntity()))) e.setCancelled(true);
	}
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		
		if(e.getEntity().getWorld().getName().equals(SkyBlock.spawnWorldName)) {
			if(e.getDamager() instanceof Player) {
				if(((Player)e.getDamager()).hasPermission("skyblock.damage.atspawn") == false || ((Player)e.getDamager()).isOp() == false) {
					e.setCancelled(true);
				}else e.setCancelled(false);
			}else {				
				if((e.getCause() == DamageCause.BLOCK_EXPLOSION || e.getCause() == DamageCause.ENTITY_EXPLOSION)) e.setCancelled(true);
				else e.setCancelled(true);
			}
		}
		
		if(e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			
			Player p = (Player) e.getDamager();
			if(p.getWorld().getName().equals(SkyWorld.skyblockworld)) {
				if(IslandManager.getIslandData(PlayerProfiler.getProfile(p).getStandort().getIslandId()) != null &&
						IslandManager.getIslandData(PlayerProfiler.getProfile(p).getStandort().getIslandId()).getSettings().pvp() == false) {					
					e.setCancelled(true);
					SkyBlock.sendNoPermissionTitle(p);
				}
			}
		}
		
		CasinoManager.onDamage(e);
		ModelManager.onDamage(e);
	}
	
	@EventHandler
	public void onEntitySpawn(CreatureSpawnEvent e) {
		if(e.getEntityType() == EntityType.TRADER_LLAMA) {
			if(Settings.allowTraderSpawnOnIsland() == false) e.setCancelled(true);
		}else {			
			if(e.getSpawnReason() == SpawnReason.NATURAL ||e.getSpawnReason() == SpawnReason.SPAWNER) {
				
				if(e.getEntityType().isAlive()) {
					int island_id = IslandManager.getIslandId(e.getLocation());
					if(e.getEntity() instanceof Monster) {
						if(e.getEntity().getWorld().getName().equals(SkyBlock.spawnWorldName) && e.getEntityType() == EntityType.PHANTOM) e.setCancelled(true);
						if(island_id == 0) {						
							e.setCancelled(true);	
						}else if(island_id != 0 &&
								IslandManager.getIslandData(IslandManager.getIslandId(e.getLocation())) != null &&
								IslandManager.getIslandData(IslandManager.getIslandId(e.getLocation())).getSettings() != null &&
								IslandManager.getIslandData(IslandManager.getIslandId(e.getLocation())).getSettings().isMonsterSpawning() == false) {
							e.setCancelled(true);	
						}
					}else {
						if(island_id == 0) {
							
						}else if(island_id != 0 &&
								IslandManager.getIslandData(IslandManager.getIslandId(e.getLocation())) != null &&
								IslandManager.getIslandData(IslandManager.getIslandId(e.getLocation())).getSettings() != null &&
								IslandManager.getIslandData(IslandManager.getIslandId(e.getLocation())).getSettings().isAnimalSpawning() == false) {
							e.setCancelled(true);	
						}
					}
				}
			}
		}
	}
	@EventHandler
	public void onExplosion(EntityExplodeEvent e) {
		if(e.getEntity().getWorld().getName().equals(SkyBlock.spawnWorldName)) e.setCancelled(true);
		int island_id = IslandManager.getIslandId(e.getLocation());
		if(island_id == 0) {
			e.setCancelled(true);
		}else {
			if(e.getEntityType() == EntityType.CREEPER) {
				if(island_id != 0 &&
						IslandManager.getIslandData(IslandManager.getIslandId(e.getLocation())) != null &&
						IslandManager.getIslandData(IslandManager.getIslandId(e.getLocation())).getSettings() != null &&
						IslandManager.getIslandData(IslandManager.getIslandId(e.getLocation())).getSettings().isMobGriefing() == false) {
					e.setCancelled(true);	
				}
			}else {
				if(island_id != 0 &&
						IslandManager.getIslandData(IslandManager.getIslandId(e.getLocation())) != null &&
						IslandManager.getIslandData(IslandManager.getIslandId(e.getLocation())).getSettings() != null &&
						IslandManager.getIslandData(IslandManager.getIslandId(e.getLocation())).getSettings().isTntDamage() == false) {
					e.setCancelled(true);
				}
			}
		}
	}
	@EventHandler
	public void onExplosion(BlockExplodeEvent e) {
		if(e.getBlock().getWorld().getName().equals(SkyBlock.spawnWorldName)) e.setCancelled(true);
		int island_id = IslandManager.getIslandId(e.getBlock().getLocation());
		if(island_id == 0) {
			e.setCancelled(true);
		}else if(island_id != 0 &&
				IslandManager.getIslandData(IslandManager.getIslandId(e.getBlock().getLocation())) != null &&
				IslandManager.getIslandData(IslandManager.getIslandId(e.getBlock().getLocation())).getSettings() != null &&
				IslandManager.getIslandData(IslandManager.getIslandId(e.getBlock().getLocation())).getSettings().isTntDamage() == false) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onFireSpread(BlockIgniteEvent e) {
		if(e.getIgnitingEntity() instanceof Player == false) {			
			int island_id = IslandManager.getIslandId(e.getBlock().getLocation());
			if(island_id == 0) {
				e.setCancelled(true);
			}else if(island_id != 0 &&
					IslandManager.getIslandData(IslandManager.getIslandId(e.getBlock().getLocation())) != null &&
					IslandManager.getIslandData(IslandManager.getIslandId(e.getBlock().getLocation())).getSettings() != null &&
					IslandManager.getIslandData(IslandManager.getIslandId(e.getBlock().getLocation())).getSettings().isFireSpread() == false) {
				e.setCancelled(true);
			}
		}
	}

}
