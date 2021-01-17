package me.crafttale.de.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import me.crafttale.de.SkyBlock;
import me.crafttale.de.gadgets.lobby.Spawn;

public class ItemDropListener implements Listener {

	public ItemDropListener() {
		SkyBlock.getSB().getServer().getPluginManager().registerEvents(this, SkyBlock.getSB());
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onDrop(PlayerDropItemEvent e) {
		Spawn.onDropItem(e);
	}
	
}
