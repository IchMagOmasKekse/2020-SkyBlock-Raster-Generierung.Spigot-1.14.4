package me.crafttale.de.economy;

import java.util.HashMap;
import java.util.LinkedList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShopCreatorItem {
	
	public static String creatorDisplayName = "Shop Spawner";
	public static String jobCodeWord = "Beruf";
	public static Material itemType = Material.FLOWER_POT;
	
	public ShopCreatorItem() {
		new ShopType();
	}
	
	/**
	 * Gibt einen Shop Spawner ohne Berufung zurück.
	 * @return
	 */
	public static ItemStack getCreatorItem() {
		return getCreatorItem("unset");
	}
	
	/**
	 * Gibt einen Shop Spawner mit Berufung zurück.
	 * @param job
	 * @return
	 */
	public static ItemStack getCreatorItem(String job) {
		ItemStack item = new ItemStack(itemType);
		ItemMeta meta = item.getItemMeta();
		LinkedList<String> lore = new LinkedList<String>();
		
		meta.setDisplayName(creatorDisplayName);
		lore.add(" §8> §7Platziere diesen Blumentopf an der Stelle,");
		lore.add(" §8> §7wo der Shop gespawnt werden soll.");
		lore.add("");
		lore.add(" §8> §7" + jobCodeWord + ": §e§o" + ShopManager.shops.get(job).getDisplayName());
		lore.add("");
		meta.setLore(lore);
		meta.addEnchant(Enchantment.ARROW_FIRE, 10, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		return item;
	}
	
	public static class ShopType {
		
		private static HashMap<String, String> codenames = new HashMap<String, String>();
		
		public ShopType() {
			codenames.put("unset", "Ohne Berufung");
			codenames.put("ore-shop", "§6Händler §8| §7Erze");
			codenames.put("garden-shop", "§aGärtner");
		}
		
		public static HashMap<String, String> getCodenames() {
			return codenames;
		}
		
//		public static String getCodenameByDisplayname(String job) {
//			for(String codename : codenames.keySet()) {
//				if(codenames.get(codename).equals(job)) return codename;
//			}
//			return"unset";
//		}
		
	}
	
}
