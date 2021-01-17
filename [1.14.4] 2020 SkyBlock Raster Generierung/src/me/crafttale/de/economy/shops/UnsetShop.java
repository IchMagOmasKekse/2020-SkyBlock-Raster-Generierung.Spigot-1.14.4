package me.crafttale.de.economy.shops;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.crafttale.de.economy.ShopManager;
import me.crafttale.de.gui.GUIManager;
import me.crafttale.de.gui.shop.ShopTypeSelectGUI;

public class UnsetShop extends Shop {

	public UnsetShop() {
		super("unset", "Ohne Berufung", Material.BEDROCK);
	}

	@Override
	public void openGUI(Player player, Villager villager) {
		GUIManager.openGUI(player, new ShopTypeSelectGUI(player, villager.getUniqueId()));
	}

	@Override
	public void onClick(InventoryClickEvent e, ItemStack item) {
		
	}

	@Override
	public void addEntity(Villager villager) {
		villager.setCustomName(ShopManager.shops.get("unset").getDisplayName());
		villager.setCustomNameVisible(true);
		this.villagers.add(villager);
	}

}
