package me.dreamisland.de.inventory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.dreamisland.de.Chat.MessageType;
import me.dreamisland.de.SkyBlock;

public class ChestContent {
	
	private static HashMap<String, ItemStack[]> contents = new HashMap<String, ItemStack[]>();
	
	public ChestContent() {
		File file = new File("plugins/SkyBlock/ChestGenerator/Contents/");
		if(file.listFiles().length > 0) {
			File[] files = file.listFiles();
			for(File f : files) {
				if(f.isFile() && f.getAbsolutePath().endsWith(".chest")) {
					loadContent(f.getName().replace(".chest", ""));
				}
			}
		}
	}
	
	public static void loadContent(String name) {
		if(contents.containsKey(name) == false) {			
			File file = new File("plugins/SkyBlock/ChestGenerator/Contents/"+name+".chest");
			if(file.exists() == false) return;
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			
			Inventory inv = Bukkit.createInventory(null, cfg.getInt("Inventory.Size"));
			for(int i = 0; i != inv.getSize(); i++) {
				ItemStack item = cfg.getItemStack("Content."+i);
				if(item != null) inv.addItem(item);
			}
			
			contents.put(name, inv.getContents());
		}
	}
	
	public static ItemStack[] getRandomContent(String name) {
		ItemStack[] cont = null;
		Inventory inv = Bukkit.createInventory(null,  27, name);
		Inventory inv2 = Bukkit.createInventory(null,  27, name);
		inv.setContents(contents.get(name));
		if(inv.getContents().length > 0) {
			ItemStack it = null;
			for(ItemStack i : inv.getContents()) {
				if(i != null) {
					if(i.getAmount() > 1) {
						
						if(inv.getContents().length == 1) {
							it = i.clone();
							it.setAmount(1);
							inv2 = placeItemAtRandomSlot(inv2, it);
						}else if(SkyBlock.randomInteger(0, 1) == 0) {
							for(int am = 0; am != i.getAmount(); am++) {
								if(i.getAmount() > 1 && SkyBlock.randomInteger(0, 1) == 0) {
									it = i.clone();
									it.setAmount(1);
									inv2 = placeItemAtRandomSlot(inv2, it);
								}
							}
						}
					}else inv2 = placeItemAtRandomSlot(inv2, i);
				}
			}
			if(inv2.isEmpty()) inv2.addItem(inv.getContents()[SkyBlock.randomInteger(0, inv.getContents().length-1)]);
		}
		cont = inv2.getContents();
		return cont;
	}
	
	public static void createDefaultChestContent() {
		/* Default Inventar 1 Generieren */
		Inventory inv = createNewDefaultChestContent(1);
		
		if(inv != null && inv.getContents().length > 0) {			
			File file = new File("plugins/SkyBlock/ChestGenerator/Contents/default.chest");
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			int index = 0;
			cfg.set("Inventory.Size", inv.getSize());
			for(ItemStack i : inv.getContents()) {
				if(i != null) cfg.set("Content."+index, i);
				index++;
			}
			try { cfg.save(file); } catch (IOException e) { e.printStackTrace(); }
			
			/* Default Inventar 2 Generieren */
			inv = createNewDefaultChestContent(2);
			
			file = new File("plugins/SkyBlock/ChestGenerator/Contents/default_2.chest");
			cfg = YamlConfiguration.loadConfiguration(file);
			index = 0;
			cfg.set("Inventory.Size", inv.getSize());
			for(ItemStack i : inv.getContents()) {
				if(i != null) cfg.set("Content."+index, i);
				index++;
			}
			try { cfg.save(file); } catch (IOException e) { e.printStackTrace(); }
			
			/* Default Inventar 3 Generieren */
			inv = createNewDefaultChestContent(3);
			
			file = new File("plugins/SkyBlock/ChestGenerator/Contents/default_3.chest");
			cfg = YamlConfiguration.loadConfiguration(file);
			index = 0;
			cfg.set("Inventory.Size", inv.getSize());
			for(ItemStack i : inv.getContents()) {
				if(i != null) cfg.set("Content."+index, i);
				index++;
			}
			try { cfg.save(file); } catch (IOException e) { e.printStackTrace(); }
			
			/* Default Inventar 4 Generieren */
			inv = createNewDefaultChestContent(4);
			
			file = new File("plugins/SkyBlock/ChestGenerator/Contents/default_4.chest");
			cfg = YamlConfiguration.loadConfiguration(file);
			index = 0;
			cfg.set("Inventory.Size", inv.getSize());
			for(ItemStack i : inv.getContents()) {
				if(i != null) cfg.set("Content."+index, i);
				index++;
			}
			try { cfg.save(file); } catch (IOException e) { e.printStackTrace(); }
			
			/* Default Inventar 5 Generieren */
			inv = createNewDefaultChestContent(5);
			
			file = new File("plugins/SkyBlock/ChestGenerator/Contents/default_5.chest");
			cfg = YamlConfiguration.loadConfiguration(file);
			index = 0;
			cfg.set("Inventory.Size", inv.getSize());
			for(ItemStack i : inv.getContents()) {
				if(i != null) cfg.set("Content."+index, i);
				index++;
			}
			try { cfg.save(file); } catch (IOException e) { e.printStackTrace(); }
			
			/* Default Inventar 6 Generieren */
			inv = createNewDefaultChestContent(6);
			
			file = new File("plugins/SkyBlock/ChestGenerator/Contents/default_6.chest");
			cfg = YamlConfiguration.loadConfiguration(file);
			index = 0;
			cfg.set("Inventory.Size", inv.getSize());
			for(ItemStack i : inv.getContents()) {
				if(i != null) cfg.set("Content."+index, i);
				index++;
			}
			try { cfg.save(file); } catch (IOException e) { e.printStackTrace(); }
			
			/* Default Inventar 7 Generieren */
			inv = createNewDefaultChestContent(7);
			
			file = new File("plugins/SkyBlock/ChestGenerator/Contents/default_7.chest");
			cfg = YamlConfiguration.loadConfiguration(file);
			index = 0;
			cfg.set("Inventory.Size", inv.getSize());
			for(ItemStack i : inv.getContents()) {
				if(i != null) cfg.set("Content."+index, i);
				index++;
			}
			try { cfg.save(file); } catch (IOException e) { e.printStackTrace(); }
			
			/* Default Inventar 8 Generieren */
			inv = createNewDefaultChestContent(8);
			
			file = new File("plugins/SkyBlock/ChestGenerator/Contents/default_8.chest");
			cfg = YamlConfiguration.loadConfiguration(file);
			index = 0;
			cfg.set("Inventory.Size", inv.getSize());
			for(ItemStack i : inv.getContents()) {
				if(i != null) cfg.set("Content."+index, i);
				index++;
			}
			try { cfg.save(file); } catch (IOException e) { e.printStackTrace(); }
			
		}else SkyBlock.sendOperatorMessage(MessageType.ERROR, "Gebe Ari bescheid, dass es einen Fehler bei der Generation des Default-Chest-Contents gibt");
	}
	
	private static Inventory createNewDefaultChestContent(int index) {
		
		Inventory inv = Bukkit.createInventory(null, 27);
		ItemStack item = null;
		ItemMeta meta = null;
		switch(index) {
		case 1:
			item = new ItemStack(Material.APPLE, 9);
			meta = item.getItemMeta();
			item.setItemMeta(meta);
			inv.addItem(item);
			
			item = new ItemStack(Material.OAK_LOG, 32);
			meta = item.getItemMeta();
			item.setItemMeta(meta);
			inv.addItem(item);
			
			item = new ItemStack(Material.OAK_SAPLING, 7);
			meta = item.getItemMeta();
			item.setItemMeta(meta);
			inv.addItem(item);
			break;
		case 2:
			item = new ItemStack(Material.DIAMOND, 3);
			meta = item.getItemMeta();
			item.setItemMeta(meta);
			inv.addItem(item);
			
			item = new ItemStack(Material.FLINT, 2);
			meta = item.getItemMeta();
			item.setItemMeta(meta);
			inv.addItem(item);
			
			item = new ItemStack(Material.FEATHER, 2);
			meta = item.getItemMeta();
			item.setItemMeta(meta);
			inv.addItem(item);
			
			item = new ItemStack(Material.IRON_INGOT, 7);
			meta = item.getItemMeta();
			item.setItemMeta(meta);
			inv.addItem(item);
			
			item = new ItemStack(Material.STRING, 5);
			meta = item.getItemMeta();
			item.setItemMeta(meta);
			inv.addItem(item);
			break;
		case 3:
			item = new ItemStack(Material.EMERALD, 6);
			meta = item.getItemMeta();
			item.setItemMeta(meta);
			inv.addItem(item);
			
			item = new ItemStack(Material.EXPERIENCE_BOTTLE, 9);
			meta = item.getItemMeta();
			item.setItemMeta(meta);
			inv.addItem(item);
			break;
		case 4:
			item = new ItemStack(Material.CHICKEN, 11);
			meta = item.getItemMeta();
			item.setItemMeta(meta);
			inv.addItem(item);
			
			item = new ItemStack(Material.PORKCHOP, 11);
			meta = item.getItemMeta();
			item.setItemMeta(meta);
			inv.addItem(item);
			
			item = new ItemStack(Material.BEEF, 10);
			meta = item.getItemMeta();
			item.setItemMeta(meta);
			inv.addItem(item);
			break;
		case 5:
			item = new ItemStack(Material.SHEEP_SPAWN_EGG, 1);
			meta = item.getItemMeta();
			item.setItemMeta(meta);
			inv.addItem(item);
			break;
		case 6:
			item = new ItemStack(Material.CHICKEN_SPAWN_EGG, 1);
			meta = item.getItemMeta();
			item.setItemMeta(meta);
			inv.addItem(item);
			break;
		case 7:
			item = new ItemStack(Material.PIG_SPAWN_EGG, 1);
			meta = item.getItemMeta();
			item.setItemMeta(meta);
			inv.addItem(item);
			break;
		case 8:
			item = new ItemStack(Material.COW_SPAWN_EGG, 1);
			meta = item.getItemMeta();
			item.setItemMeta(meta);
			inv.addItem(item);
			break;
		}
		
		return inv;
	}
	
	/**
	 * Platziert ein Item an einer zufälligen Stelle in einem Inventar.
	 * @param inv
	 * @param item
	 * @return
	 */
	public static Inventory placeItemAtRandomSlot(Inventory inv, ItemStack item) {
		int slot = SkyBlock.randomInteger(0, inv.getSize());
		if(slot == inv.getSize()) slot-=1;
		int attempts = 0;
		while(inv.getItem(slot) != null && inv.getItem(slot).getType() != Material.AIR && inv.getItem(slot) != item && attempts != inv.getSize()) {
			slot = SkyBlock.randomInteger(0, inv.getSize());
			if(slot == inv.getSize()) slot-=1;
			attempts++;
		}
		if(attempts == inv.getSize()) {
			inv.addItem(item);			
		}else inv.setItem(slot, item);			
		return inv;
	}
	
}
