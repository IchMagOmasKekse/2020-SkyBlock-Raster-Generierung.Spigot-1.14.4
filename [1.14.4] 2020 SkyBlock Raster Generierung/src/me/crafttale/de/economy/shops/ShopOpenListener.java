package me.crafttale.de.economy.shops;

import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import me.crafttale.de.SkyBlock;
import me.ichmagomaskekse.de.GUIManager;

public class ShopOpenListener implements Listener {
	
	public ShopOpenListener() {
		SkyBlock.getSB().getServer().getPluginManager().registerEvents(this, SkyBlock.getSB());
	}
	
	@EventHandler
	public void onClick(PlayerInteractEntityEvent e) {
		if(e.getRightClicked() instanceof Villager) {
			if(e.getRightClicked().getCustomName() != null && e.getRightClicked().getCustomName().equals("") == false && e.getRightClicked().getCustomName().equals("§eVerkäufer")) {
				e.setCancelled(true);
				GUIManager.getGUIManager().openGUI(e.getPlayer(), new DemoShop(e.getPlayer()));
			}
		}
	}
}
