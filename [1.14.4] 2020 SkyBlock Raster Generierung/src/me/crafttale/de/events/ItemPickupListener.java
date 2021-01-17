package me.crafttale.de.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import me.crafttale.de.SkyBlock;
import me.crafttale.de.casino.CasinoManager;

@SuppressWarnings("deprecation")
public class ItemPickupListener implements Listener {

	public ItemPickupListener() {
		SkyBlock.getSB().getServer().getPluginManager().registerEvents(this, SkyBlock.getSB());
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onDrop(PlayerPickupItemEvent e) {
		CasinoManager.onPickupItem(e);
	}
	
}
