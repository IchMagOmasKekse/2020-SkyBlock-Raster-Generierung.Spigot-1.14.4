package me.crafttale.de.schematics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.crafttale.de.Prefixes;
import me.crafttale.de.SkyBlock;

public class ChestGenerator implements Listener, CommandExecutor {
	
	
	public ChestGenerator() {
		SkyBlock.getSB().getCommand("loaditem").setExecutor(this);
		SkyBlock.getSB().getCommand("saveitem").setExecutor(this);
		SkyBlock.getSB().getServer().getPluginManager().registerEvents(this, SkyBlock.getSB());
		createItemPreset();
	}
	
	/**
	 * Gibt ein Inventar zurück, welches die Inhalte der Standart-SkyBlock-Kisten beinhaltet
	 * @return
	 */
	public static Inventory getDefaultChestInventory(boolean big_chest) {
		Inventory inv = Bukkit.createInventory(null, 27 * (big_chest ? 2 : 1), "Chest-Inventory");
		
		ItemStack item = new ItemStack(Material.AIR);
		ItemMeta meta = item.getItemMeta();
		@SuppressWarnings("unused")
		ArrayList<String> lore = new ArrayList<String>();
		int slot = 0;
		
		item = new ItemStack(Material.ICE, 2);
		meta = item.getItemMeta();
		meta.setLore(getKitLore("default"));
		item.setItemMeta(meta);
		
		inv.setItem(slot, item);slot++;
		
		item = new ItemStack(Material.LAVA_BUCKET);
		meta = item.getItemMeta();
		meta.setLore(getKitLore("default"));
		item.setItemMeta(meta);
		
		inv.setItem(slot, item);slot++;
		
		item = new ItemStack(Material.APPLE, 16);
		meta = item.getItemMeta();
		meta.setLore(getKitLore("default"));
		item.setItemMeta(meta);
		
		inv.setItem(slot, item);slot++;
		
		return inv;
	}
	
	/**
	 * Erstellt eine itemPresetfile
	 * @param
	 */
	public void createItemPreset() {
		File file = new File("plugins/SkyBlock/ChestGenerator/item_preset.yml");
		if(file.exists()) return;
		else {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			
			ItemStack item = new ItemStack(Material.STONE);
			ItemMeta meta = item.getItemMeta();
			ArrayList<String> lore = new ArrayList<String>();
			if(meta.hasLore()) {
				meta.getLore().clear();
				meta.getLore().set(0, "Line 1");
				meta.getLore().set(1, "Line 2");
				meta.getLore().set(2, "Line 3");				
			}else {
				lore.add("Line 1");
				lore.add("Line 2");
				lore.add("Line 3");
				meta.setLore(lore);
			}
			item.setAmount(10);
			meta.setDisplayName("Displayname");
			meta.setUnbreakable(false);
			meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
			meta.addEnchant(Enchantment.DIG_SPEED, 10, true);
			item.setItemMeta(meta);
			item.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
			
			
			cfg.set("Item", item);
			try {
				cfg.save(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/*
	 * TODO: loadItem() lädt ein Item, das in der Item-Preset Datei erstellt wurde und gibt diese zurück
	 */
	public ItemStack loadItemFromFile() {
		File file = new File("plugins/SkyBlock/ChestGenerator/item_preset.yml");
		if(file.exists() == false) return null;
		else {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			ItemStack item = cfg.getItemStack("Item");
			ItemMeta meta = item.getItemMeta();
			ArrayList<String> lore = new ArrayList<String>();
			for(int i = meta.getLore().size()-1; i != 0; i--) {
				lore.add(meta.getLore().get(i).replace("&", "§"));
			}
			item.setItemMeta(meta);
			return item;
		}
	}
	/*
	 * TODO: saveItem() speichert ein Item, in die Item-Preset Datei
	 */
	public void saveItem(ItemStack item) {
		File file = new File("plugins/SkyBlock/ChestGenerator/item_preset.yml");
		if(file.exists() == false) return;
		else {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			cfg.set("Item", item);
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * TODO: saveInventory() soll das Inventar in eine Datei speichern, damit es immer abgerufen werden kann
	 */
	public boolean saveInventory(Inventory inv, String name, boolean overwrite) {
		
		File file = new File("plugins/SkyBlock/ChestGenerator/Chests/"+name+".yml");
		if(file.exists() && overwrite == false) return false;
		else {
			int item_nr = 0;
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			cfg.set("Inventory.Size", inv.getSize());
			for(ItemStack item : inv.getContents()) {
				
				if(item != null) {
					cfg.set("Inventory.Content.I-"+item_nr+"", item);
				}
				item_nr++;
			}
			try {
				cfg.save(file);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		
	}
	
	/*
	 * TODO: readInventory() liest ein Inventar aus einer Datei aus und gibt dieses zurück
	 */
	public static Inventory readInventory(String name) {
		File file = new File("plugins/SkyBlock/ChestGenerator/Chests/"+name+".yml");
		if(file.exists() == false) return null;
		else {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			int size = cfg.getInt("Inventory.Size");
			Inventory inv = Bukkit.createInventory(null, size, name);
			for(int i = 0; i != size; i++) {
				inv.setItem(i, cfg.getItemStack("Inventory.Content.I-"+i));
			}
			return inv;
		}
	}
	
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if(e.getItemInHand().getItemMeta().getDisplayName().contains("Chest Loader:")) {
			if(SkyBlock.hasPermission(p, "skyblock.chestloader")) {
				Chest c = (Chest) e.getBlock().getState();
				String n = e.getItemInHand().getItemMeta().getDisplayName();
				String[] s = n.split(":");
				Inventory inv = readInventory(s[1]);
				if(inv == null) {
					p.sendMessage(Prefixes.SERVER.px()+"Chest '§e"+s[1]+"' §7existiert nicht");								
				}else {								
					c.getInventory().setContents(inv.getContents());
					c.setCustomName(s[1].replace("&", "§").replace("_", " ").replace("Chest Loader:", ""));
					p.sendMessage(Prefixes.SERVER.px()+"Chest '§e"+s[1]+"' §7wurde geladen");
				}
			}
		}
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getPlayer().isSneaking()) {
			Player p = e.getPlayer();
			if(e.getItem() != null
					&& e.getItem().getType() == Material.CHEST
					&& e.getItem().hasItemMeta()
					&& e.getItem().getItemMeta().hasDisplayName()) {
				if(e.getClickedBlock().getType() == Material.CHEST) {
					if(e.getItem().getItemMeta().getDisplayName().contains("Chest Saver:")) {				
						if(SkyBlock.hasPermission(p, "skyblock.chestsaver")) {
							e.setCancelled(true);
							Chest c = (Chest) e.getClickedBlock().getState();
							String n = e.getItem().getItemMeta().getDisplayName();
							String[] s = n.split(":");
							boolean overwrite = false;
							if(s.length >= 3) {
								if(s[2].equals("overwrite")) overwrite = true;
							}
							if(saveInventory(c.getInventory(), s[1], overwrite)) {
								p.sendMessage(Prefixes.SERVER.px()+"Chest '§e"+s[1]+"' §7wurde gespeichert");
							}else p.sendMessage(Prefixes.SERVER.px()+"Chest '§e"+s[1]+"' §7konnte nicht gespeichert werden");
						}
					}
				}
			}
		}
	}
	
	/**
	 * Gibt die Lore eines Kits zurück.
	 * @param kitname
	 * @return
	 */
	public static ArrayList<String> getKitLore(String kitname){
		ArrayList<String> lore = new ArrayList<String>();
		
		if(kitname.equals("default")) {
			lore.add("§a§oInhalt des Standart-Kits.");
		}else {
			//DEFAULT
			return lore;
		}
		return lore;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(cmd.getName().equalsIgnoreCase("loaditem")) {
				if(SkyBlock.hasPermission(p, "skyblock.loaditem")) {
					p.getInventory().addItem(loadItemFromFile());
					p.sendMessage(Prefixes.SERVER.px()+"Das Item wurde deinem Inventar hinzugefügt");
				}
			}else if(cmd.getName().equalsIgnoreCase("saveitem")) {
				if(SkyBlock.hasPermission(p, "skyblock.saveitem")) {
					saveItem(p.getInventory().getItemInMainHand());
					p.sendMessage(Prefixes.SERVER.px()+"Das Item wurde gespeichert");
				}
			}
		}
		return false;
	}
	
}
