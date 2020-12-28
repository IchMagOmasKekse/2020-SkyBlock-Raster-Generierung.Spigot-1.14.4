package me.crafttale.de.economy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import me.crafttale.de.Chat.MessageType;
import me.crafttale.de.SkyBlock;
import me.crafttale.de.economy.shops.DemoShop.ItemPrice;

public class PriceSetter {
	
	private static HashMap<Material, ItemPrice> prices = new HashMap<Material, ItemPrice>();
	
	public PriceSetter() {
		File file = new File("plugins/SkyBlock/price_list.yml");
		if(file.exists() == false) {			
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			
			ArrayList<String> items = new ArrayList<String>();
			
			for(Material m : Material.values()) {
				if(m.toString().contains("LEGACY") == false && m != Material.AIR) {
					items.add("["+m.toString()+":1x:BuyPrice=0.0d:1x:SellPrice=0.0d:isBuyable=true:displayname=Test Item:placeholder1=true:placeholder2=true:placeholder3=true]");
				}
			}
			
			cfg.set("Shop.Items", items);
			
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		loadPrices();
		
	}
	public static HashMap<Material, ItemPrice> getProducts() {
		return prices;
	}
	/**
	 * [STONE:1x:BuyPrice=0.0d:1x:SellPrice=0.0d:isBuyable=true:placeholder1=true:placeholder2=true:placeholder3=true]
	 */
	@SuppressWarnings("unchecked")
	public static void loadPrices() {
		BukkitRunnable asyncer = new BukkitRunnable() {
			
			@Override
			public void run() {
				File file = new File("plugins/SkyBlock/price_list.yml");
				if(file.exists()) {
					FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);		
					
					ArrayList<String> entries = (ArrayList<String>) cfg.getStringList("Shop.Items");
					HashMap<Material, ItemPrice> p = new HashMap<Material, ItemPrice>();
					
					String[] splitter = "".split("");
					ItemPrice ip = null;
					
					double bp = 0.0d; //BuyPrice
					int ba = 0; //BuyAmount
					double sp = 0.0d; //SellAmount
					int sa = 0; //SellAmount
					
					for(String entry : entries) {
						entry = entry.substring(1, entry.length()-1);
						splitter = entry.split(":");
						ba = Integer.parseInt(splitter[1].replace("x", ""));
						sa = Integer.parseInt(splitter[3].replace("x", ""));
						if(ba > 0 && sa > 0) {							
							bp = Double.parseDouble(splitter[2].replace("BuyPrice=", ""));
							sp = Double.parseDouble(splitter[4].replace("SellPrice=", ""));
							Material m = Material.valueOf(splitter[0]);
							ip = new ItemPrice(splitter[6].replace("displayname=", ""),
									bp,
									ba,
									sp,
									sa);
							p.put(m, ip);
						}
					}
					prices.clear();
					prices = (HashMap<Material, ItemPrice>) p.clone();
					SkyBlock.sendOperatorMessage(MessageType.INFO, "Es wurden "+prices.size()+" Shop-Items geladen");
				}
			}
		};
		asyncer.runTaskAsynchronously(SkyBlock.getSB());
	}
	
	public static ItemPrice getItemPrice(Material mat) {
		if(prices.containsKey(mat)) return prices.get(mat);
		else return null;
	}
	
}
