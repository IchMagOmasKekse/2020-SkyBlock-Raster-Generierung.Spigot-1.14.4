package me.crafttale.de.economy.shops;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class NotFoundShop extends Shop {

	public NotFoundShop(String codeName, String displayName, Material displayMaterial) {
		super(codeName, displayName, displayMaterial);
	}

	@Override
	public void openGUI(Player player, Villager villager) {
		player.sendMessage("§cDiese Shop-Art ist nicht registriert.");
	}

	@Override
	public void onClick(InventoryClickEvent e, ItemStack item) {
		
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addEntity(Villager villager) {
		// TODO Auto-generated method stub
		
	}

}
