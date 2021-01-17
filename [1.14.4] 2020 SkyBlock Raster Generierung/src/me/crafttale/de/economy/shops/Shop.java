package me.crafttale.de.economy.shops;

import java.util.LinkedList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public abstract class Shop {
	
	protected String codeName = "";
	protected String displayName = "";
	protected Material displayMaterial = Material.DIAMOND;
	protected LinkedList<Villager> villagers = new LinkedList<Villager>();
	
	public Shop(String codeName, String displayName, Material displayMaterial) {
		this.codeName = codeName;
		this.displayName = displayName;
		this.displayMaterial = displayMaterial;
	}
	
	public abstract void openGUI(Player player, Villager villager);
	public abstract void onClick(InventoryClickEvent e, ItemStack item);
	public abstract void addEntity(Villager villager);
	public void disable() {
		for(Villager vill : villagers) vill.remove();
	}
	
	public String getCodeName() {
		return codeName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public Material getDisplayMaterial() {
		return displayMaterial;
	}
	
	public LinkedList<Villager> getEntities() {
		return villagers;
	}
	
}
