package me.dreamisland.de.inventory;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.dreamisland.de.SkyBlock;
import me.dreamisland.de.Chat.MessageType;

public class ChestContent {
	
	public static Inventory getContent(String name) {
		File file = new File("plugins/SkyBlock/ChestGenerator/Contents/"+name+".chest");
		if(file.exists() == false) return null;
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		Inventory inv = Bukkit.createInventory(null, cfg.getInt("Inventory.Size"));
		for(int i = 0; i != inv.getSize(); i++) {
			ItemStack item = cfg.getItemStack("Content."+i);
			if(item != null) inv.addItem(item);
		}
		
		return inv;
	}
	
	public static void createDefaultChestContent() {
		Inventory inv = createNewDefaultChestContent();
		
		if(inv != null && inv.getContents().length > 0) {			
			File file = new File("plugins/SkyBlock/ChestGenerator/Contents/default.chest");
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			int index = 0;
			cfg.set("Inventory.Size", inv.getSize());
			for(ItemStack i : inv.getContents()) {
				if(i != null) cfg.set("Content."+index, i);
				index++;
			}
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else SkyBlock.sendOperatorMessage(MessageType.ERROR, "Gebe Ari bescheid, dass es einen Fehler bei der Generation des Default-Chest-Contents gibt");
	}
	
	private static Inventory createNewDefaultChestContent() {
		
		Inventory inv = Bukkit.createInventory(null, 27);
		
		ItemStack item = null;
		ItemMeta meta = null;
		
		item = new ItemStack(Material.APPLE);
		meta = item.getItemMeta();
		item.setItemMeta(meta);
		inv.addItem(item);
		
		item = new ItemStack(Material.OAK_LOG, 16);
		meta = item.getItemMeta();
		item.setItemMeta(meta);
		inv.addItem(item);
		
		item = new ItemStack(Material.OAK_SAPLING, 3);
		meta = item.getItemMeta();
		item.setItemMeta(meta);
		inv.addItem(item);
		
		return inv;
	}
	
}
