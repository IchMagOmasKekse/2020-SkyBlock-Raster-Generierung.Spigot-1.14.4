package me.crafttale.de.economy.shops;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.crafttale.de.economy.ShopManager;
import me.crafttale.de.gui.GUIManager;
import me.crafttale.de.gui.shop.OreShopGUI;

public class OreShop extends Shop {

	public OreShop() {
		super("ore-shop", "§6Händler §8| §7Erze", Material.DIAMOND);
	}

	@Override
	public void openGUI(Player player, Villager villager) {
		GUIManager.openGUI(player, new OreShopGUI(player, villager.getUniqueId()));
	}

	@Override
	public void onClick(InventoryClickEvent e, ItemStack item) {
		
	}

	@Override
	public void addEntity(Villager villager) {
		villager.setCustomName(ShopManager.shops.get("ore-shop").getDisplayName());
		villager.setCustomNameVisible(true);
		this.villagers.add(villager);
	}

}
