package me.crafttale.de;

import java.util.ArrayList;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.crafttale.de.Chat.MessageType;
import me.crafttale.de.economy.EconomyManager;
import me.crafttale.de.economy.EconomyManager.EditOperation;
import me.crafttale.de.economy.EconomyManager.MoneyType;
import me.crafttale.de.filemanagement.SkyFileManager;
import me.crafttale.de.profiles.PlayerProfiler;

public class SkyWorld implements Listener {
	
	public static final String skyblockworld = "skyblockworld";
	public static ArrayList<String> mob_names = new ArrayList<String>();
	
	public SkyWorld() {
		SkyBlock.getSB().getServer().getPluginManager().registerEvents(this, SkyBlock.getSB());
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
			if(e.getEntity().getType() == EntityType.VILLAGER) {
				if(SkyBlock.randomInteger(0, 1) == 0) e.getEntity().setCustomName("§eVerkäufer");
			}else if(e.getEntity().getType() == EntityType.PHANTOM) {
				e.setCancelled(true);
			}else if(e.getEntity() instanceof LivingEntity) {
				e.getEntity().setCustomName(mob_names.get(SkyBlock.randomInteger(0, mob_names.size()-1)));
			}
		}
	}
	@EventHandler
	public void onMobSpawn(EntityDamageEvent e) {
		if(e.getEntity().getWorld().getName().equals("world")) {			
			if(e.getEntity() instanceof Player) {
				e.setCancelled(true);
				((Player)e.getEntity()).setHealth(((Player)e.getEntity()).getHealthScale());
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
			EconomyManager.editMoney(PlayerProfiler.getUUID(((Player)e.getEntity())), MoneyType.MONEY1, 15, EditOperation.SUBTRACT);
			SkyBlock.sendMessage(MessageType.WARNING, ((Player)e.getEntity()), "Du hast §a15 SkyCoins §cverloren, dafür dass du in den Abgrund gefallen bist");
		}else if(e.getEntity().getWorld().getName().equals("world")) {
			e.setKeepLevel(true);
			e.setKeepInventory(true);
			e.getDrops().clear();
			e.setDroppedExp(0);
			EconomyManager.editMoney(PlayerProfiler.getUUID(((Player)e.getEntity())), MoneyType.MONEY1, 15, EditOperation.SUBTRACT);
			SkyBlock.sendMessage(MessageType.WARNING, ((Player)e.getEntity()), "Du hast §a15 SkyCoins §cverloren, dafür dass du in den Abgrund gefallen bist");
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onRespawn(PlayerRespawnEvent e) {
		if(Settings.respawnAtIslandIFDiedInSkyBlock()) {
			if(e.getPlayer().getWorld().getName().equals(skyblockworld)) {
				e.setRespawnLocation(SkyFileManager.getPlayerDefinedIslandSpawn(e.getPlayer()));
			}else if(e.getPlayer().getWorld().getName().equals("world")) {
				
			}
		}
	}
	@EventHandler
	public void onRespawn(PlayerPortalEvent e) {
		if(e.getPlayer().getWorld().getName().equals(skyblockworld)) {
			e.setCancelled(true);
			e.getPlayer().teleport(SkyFileManager.getPlayerDefinedIslandSpawn(e.getPlayer()));
		}
	}
	
	
	
}
