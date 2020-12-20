package me.dreamisland.de.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.dreamisland.de.SkyBlock;

public class GUIClicker implements Listener {
	
	public GUIClicker() {
		SkyBlock.getSB().getServer().getPluginManager().registerEvents(this, SkyBlock.getSB());
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(GUIManager.hasOpenInventory(e.getWhoClicked()) &&
				e.getClickedInventory() != null &&
				e.getClickedInventory() == GUIManager.getGUI(e.getWhoClicked()).openedInv) {
			GUIManager.getGUI(e.getWhoClicked()).clickedItem(e.getCurrentItem());
			if(e.getClick() != ClickType.LEFT) e.setCancelled(true);
			e.setCancelled(true);
		}else if(GUIManager.hasOpenInventory(e.getWhoClicked()) && e.getClickedInventory() == e.getWhoClicked().getInventory()) {
			if(e.getClick() != ClickType.LEFT) e.setCancelled(true);
		}
	}
	
}
