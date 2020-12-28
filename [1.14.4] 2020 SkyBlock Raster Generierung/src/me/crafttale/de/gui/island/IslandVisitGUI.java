package me.crafttale.de.gui.island;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.crafttale.de.gui.GUI;
import me.crafttale.de.requests.Request.RequestManager;


public class IslandVisitGUI extends GUI {
		
	public IslandVisitGUI(Player player, Player requester) {
		super(player, requester, GUIType.ISLAND_VISIT_GUI);
		loop = false;
	}

	@Override
	public void clickedItem(InventoryClickEvent e, ItemStack item) {
		if(item != null &&
				(item.getType() == Material.LIME_BANNER || item.getType() == Material.RED_BANNER) &&
				item.hasItemMeta() &&
				item.getItemMeta().hasDisplayName()) {		
			if(item.getType() == Material.LIME_BANNER) {
				RequestManager.getReceivedRequest(player).accept();
			}else if(item.getType() == Material.RED_BANNER) {
				RequestManager.getReceivedRequest(player).deny();
			}
		}
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		
	}

	@Override
	public void stop() {
		if(RequestManager.hasReceivedRequest(player)) RequestManager.getReceivedRequest(player).deny();
	}
	
	
	
}
