package me.crafttale.de.gui.shop;

import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.crafttale.de.gui.GUI;

public class OreShopGUI extends GUI {

	public OreShopGUI(Player player, UUID villagerUUID) {
		super(player, GUIType.OWN_CREATION);
		this.targetUUID = villagerUUID;
	}

	@Override
	public void start() {
		openedInv = Bukkit.createInventory(null,  45, "Erze");
		processedTitle = type.getTitle();
		LinkedList<String> lore = new LinkedList<String>();
		lore.add("");
		lore.add(" §8> §6Kaufpreis: §730€");
		item = new ItemStack(Material.DIAMOND);
		meta = item.getItemMeta();
		meta.setDisplayName("Diamant");
		meta.setLore(lore);
		item.setItemMeta(meta);
		openedInv.setItem(0, item);
		
		item = new ItemStack(Material.EMERALD);
		meta = item.getItemMeta();
		meta.setDisplayName("Smaragd");
		meta.setLore(lore);
		item.setItemMeta(meta);
		openedInv.setItem(1, item);
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
