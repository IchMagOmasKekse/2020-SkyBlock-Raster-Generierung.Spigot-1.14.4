package me.crafttale.de.gui.shop;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.crafttale.de.gui.GUI;
import me.crafttale.de.gui.GUIManager;

public class CropShopGUI extends GUI {

	public CropShopGUI(Player player, UUID villagerUUID) {
		super(player, GUIType.OWN_CREATION);
		this.targetUUID = villagerUUID;
	}

	@Override
	public void start() {
		player.sendMessage("§cDiese Shop-Art ist nicht registriert.");
		GUIManager.closeGUI(player, true);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clickedItem(InventoryClickEvent e, ItemStack item) {
		// TODO Auto-generated method stub
		
	}

}
