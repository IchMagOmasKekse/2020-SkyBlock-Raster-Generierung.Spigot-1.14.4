package me.skytale.de.economy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PriceSetter {
	
	public PriceSetter() {
		
		File file = new File("plugins/SkyBlock/price_list.yml");
		if(file.exists() == false) {			
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			
			ArrayList<String> items = new ArrayList<String>();
			
			for(Material m : Material.values()) {
				if(m.toString().contains("LEGACY") == false && m != Material.AIR) {
					items.add("["+m.toString()+":1x:BuyPrice=0.0d:SellPrice=0.0d:isBuyable=true:placeholder1=true:placeholder2=true:placeholder3=true]");
				}
			}
			
			cfg.set("Shop.Items", items);
			
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
