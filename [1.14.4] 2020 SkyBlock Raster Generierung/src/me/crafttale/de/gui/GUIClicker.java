package me.crafttale.de.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import me.crafttale.de.SkyBlock;
import me.crafttale.de.casino.CasinoManager;
import me.crafttale.de.models.ModelManager;

public class GUIClicker implements Listener {
	
	public GUIClicker() {
		SkyBlock.getSB().getServer().getPluginManager().registerEvents(this, SkyBlock.getSB());
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onOpen(InventoryOpenEvent e) {
		CasinoManager.onOpeningInventory(e);
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onClick(InventoryClickEvent e) {
		ModelManager.onInvClick(e);
		if(GUIManager.hasOpenInventory(e.getWhoClicked()) &&
				GUIManager.getGUI(e.getWhoClicked()).getGUIType().usesAnInventory() &&
				e.getClickedInventory() != null &&
				e.getClickedInventory().equals(GUIManager.getGUI(e.getWhoClicked()).openedInv)) {
			
			GUIManager.getGUI(e.getWhoClicked()).clickedItem(e, e.getCurrentItem());
			e.setCancelled(true);
		}else if(GUIManager.hasOpenInventory(e.getWhoClicked()) && e.getClickedInventory() == e.getWhoClicked().getInventory()) {
			
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onInventoryClose(InventoryCloseEvent e) {
		if(GUIManager.guis.isEmpty() == false) {
			if(GUIManager.guis.containsKey(e.getPlayer())) {
				GUIManager.unregisterGUI(e.getPlayer());
			}
		}
	}
	
}
