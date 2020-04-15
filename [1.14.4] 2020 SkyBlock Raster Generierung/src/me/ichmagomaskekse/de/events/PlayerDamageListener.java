package me.ichmagomaskekse.de.events;

import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.ichmagomaskekse.de.SkyBlock;

public class PlayerDamageListener implements Listener {
	
	public PlayerDamageListener() {
		SkyBlock.getInstance().getServer().getPluginManager().registerEvents(this, SkyBlock.getInstance());
	}
	
	/*
	 * TODO: onDamage() Soll geprüft werden, ob der SPieler Schaden durch eine Rackete bekommen hat
	 * Wenn ja, dann soll dieses Event Abgrebrochen werden, wenn diese Rackete 'SERVER' heißt
	 */
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Firework && e.getDamager().getCustomName().equals("SERVER")) {
			e.setCancelled(true);
			e.setDamage(0d);
		}
	}
}
