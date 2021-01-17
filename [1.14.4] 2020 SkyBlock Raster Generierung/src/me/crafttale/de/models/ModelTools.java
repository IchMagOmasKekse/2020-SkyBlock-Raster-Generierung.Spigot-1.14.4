package me.crafttale.de.models;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ModelTools {
	
	public static void giveTools(Player p) {
		
	}
	
	/**
	 * Gibt dem Spieler die notwendigen Werkzeuge, um das Model zu bearbeiten.
	 * @param host
	 */
	public static void giveEditorInventory(Player host) {
		host.getInventory().clear();
		ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new LinkedList<String>();
		lore.add("§7Linksklick:§a+ §7Rechtsklick:§c-"); lore.add("§7Sneak = §a+§7/§c-§f"+ModelManager.fineOffset); lore.add("§7Ohne Sneak = §a+§7/§c-§f0.1");
		
		item = new ItemStack(Material.DIAMOND_PICKAXE);
		meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.setLore(lore); meta.setDisplayName("§7Position §fZ"); item.setItemMeta(meta);
		host.getInventory().setItem(1, item);
		
		item = new ItemStack(Material.DIAMOND_AXE);
		meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.setLore(lore); meta.setDisplayName("§7Position §fX"); item.setItemMeta(meta);
		host.getInventory().setItem(2, item);
		
		item = new ItemStack(Material.DIAMOND_SHOVEL);
		meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.setLore(lore); meta.setDisplayName("§7Postition §fY"); item.setItemMeta(meta);
		host.getInventory().setItem(3, item);
		
		item = new ItemStack(Material.PRISMARINE_SHARD);
		meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		lore.clear(); lore.add("§7Rechtsklick auf ein Model um den Namen im Chat anzuzeigen.");
		lore.add("§7Klicke die Nachricht an, um das Model zu selektieren.");
		meta.setLore(lore); meta.setDisplayName("§7Model Selektor"); item.setItemMeta(meta);
		host.getInventory().setItem(4, item);
		
		item = new ItemStack(Material.IRON_PICKAXE);
		meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		lore.clear();
		meta.setLore(lore); meta.setDisplayName("§7Headpose §fZ"); item.setItemMeta(meta);
		host.getInventory().setItem(5, item);
		
		item = new ItemStack(Material.IRON_AXE);
		meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.setLore(lore); meta.setDisplayName("§7Headpose §fX"); item.setItemMeta(meta);
		host.getInventory().setItem(6, item);
		
		item = new ItemStack(Material.IRON_SHOVEL);
		meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.setLore(lore); meta.setDisplayName("§7Headpose §fY"); item.setItemMeta(meta);
		host.getInventory().setItem(7, item);
		
		item = new ItemStack(Material.BARRIER);
		meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		lore.clear(); lore.add("§4ACHTUNG §7Wird mit SOFORTIGER Wirkung ausgeführt,");
		lore.add("§7sobald das Item angeklickt wird!");
		meta.setLore(lore);
		meta.setDisplayName("§cModel löschen"); item.setItemMeta(meta);
		host.getInventory().setItem(17, item);
		
		item = new ItemStack(Material.BOOK);
		meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		lore.clear(); lore.add("§7Anklicken, um das Model neu zu laden.");
		meta.setLore(lore);
		meta.setDisplayName("§fNeu laden"); item.setItemMeta(meta);
		host.getInventory().setItem(9, item);
		
		item = new ItemStack(Material.WRITABLE_BOOK);
		meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		lore.clear(); lore.add("§7Anklicken, um das Model zu speichern.");
		meta.setLore(lore);
		meta.setDisplayName("§fSpeichern"); item.setItemMeta(meta);
		host.getInventory().setItem(10, item);
		
		item = new ItemStack(Material.PAPER);
		meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		lore.clear(); lore.add("§7Anklicken, um Informationen über das Model zu erfahren");
		meta.setLore(lore);
		meta.setDisplayName("§fInformation"); item.setItemMeta(meta);
		host.getInventory().setItem(11, item);
		
		item = new ItemStack(Material.GUNPOWDER);
		meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		lore.clear(); lore.add("§7Anklicken, um das Model sichtbar zu machen");
		meta.setLore(lore);
		meta.setDisplayName("§fSichtbar machen"); item.setItemMeta(meta);
		host.getInventory().setItem(12, item);
		
		item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
		meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		lore.clear(); lore.add("§7Platziere hier ein Item, dass angezeigt werden soll.");
		meta.setLore(lore);
		meta.setDisplayName("§fAngezeigtes Item"); item.setItemMeta(meta);
		host.getInventory().setItem(31, item);
		
		item = new ItemStack(Material.EGG);
		meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		lore.clear(); lore.add("§7Zwischen Kind und Erwachsen switchen");
		meta.setLore(lore);
		meta.setDisplayName("§fSmall/Adult"); item.setItemMeta(meta);
		host.getInventory().setItem(30, item);
		
		item = new ItemStack(Material.GOLD_INGOT);
		meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		lore.clear(); lore.add("§7Zwischen On und Off switchen");
		meta.setLore(lore);
		meta.setDisplayName("§fAuto Summon"); item.setItemMeta(meta);
		host.getInventory().setItem(32, item);
		
		item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.setDisplayName(""); item.setItemMeta(meta);
		for(int i = 0; i != 36; i++) {
			if(i != 0 && i != 8) {
				if(host.getInventory().getItem(i) == null ||
						host.getInventory().getItem(i).getType() == Material.AIR)host.getInventory().setItem(i, item);
			}
		}
	}
	
}
