package me.crafttale.de.economy;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.entity.Villager;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.meta.ItemMeta;

import me.crafttale.de.economy.shops.CropShop;
import me.crafttale.de.economy.shops.OreShop;
import me.crafttale.de.economy.shops.Shop;
import me.crafttale.de.economy.shops.UnsetShop;

public class ShopManager {
	
	public static ConcurrentHashMap<String, Shop> shops = new ConcurrentHashMap<String, Shop>();
	
	private static boolean debug = false;
	
	public ShopManager() {
		shops.put("unset", new UnsetShop());
		shops.put("ore-shop", new OreShop());
		shops.put("garden-shop", new CropShop());
	}
	
	public static void despawnAllShops() {
		for(Shop shop : shops.values()) shop.disable();
	}
	
	/**
	 * 
	 * @param e
	 */
	public static void onPlace(BlockPlaceEvent e) {
		if(debug) {			
			if(e.getItemInHand() == null) e.getPlayer().sendMessage("1");
			if(e.getItemInHand().getType() != ShopCreatorItem.itemType) e.getPlayer().sendMessage("2");
			if(!e.getItemInHand().hasItemMeta()) e.getPlayer().sendMessage("3");
			if(!e.getItemInHand().getItemMeta().hasDisplayName()) e.getPlayer().sendMessage("4");
			if(!e.getItemInHand().getItemMeta().getDisplayName().contains(ShopCreatorItem.creatorDisplayName)) e.getPlayer().sendMessage("5");
		}
		
		if(e.getItemInHand() != null && e.getItemInHand().getType() == ShopCreatorItem.itemType && e.getItemInHand().hasItemMeta() &&
				e.getItemInHand().getItemMeta().hasDisplayName() && e.getItemInHand().getItemMeta().getDisplayName().contains(ShopCreatorItem.creatorDisplayName)) {
			ItemMeta meta = e.getItemInHand().getItemMeta();
			if(meta.hasLore() && meta.getLore() != null && meta.getLore().isEmpty() == false) {
				e.setCancelled(true);
				String jobCodename = "unset";
				if(ShopCreatorItem.ShopType.getCodenames().isEmpty() == false) {
					HashMap<String, String> names = ShopCreatorItem.ShopType.getCodenames();
					for(String s : names.keySet()) {
						if(meta.getLore().toString().contains(names.get(s))) {
							jobCodename = s;
							break;
						}
					}
					spawnShop(jobCodename, e.getBlock().getLocation());
					if(debug) e.getPlayer().sendMessage("JOB: §e§o" + jobCodename + "§7(§e§o" + shops.get(jobCodename).getDisplayName() + "§7)");
				}
			}else if(debug) e.getPlayer().sendMessage("a");
		}else if(debug) e.getPlayer().sendMessage("b");
	}
	
	public static void onInteractEntity(PlayerInteractEntityEvent e) {
		if(e.getRightClicked() instanceof Villager && e.getRightClicked().getCustomName() != null && e.getRightClicked().getCustomName().equals("") == false) {
			for(Shop shop : shops.values()) {
				if(e.getRightClicked().getCustomName().equals(shop.getDisplayName())) {
					if(debug) e.getPlayer().sendMessage("Name: "+shop.getCodeName());
					shop.openGUI(e.getPlayer(), (Villager)e.getRightClicked());
					break;
				}
			}
		}
	}
	
	public static boolean spawnShop(String jobCodename, Location location) {
		Villager vil = location.getWorld().spawn(location.add(0.5,0,0.5), Villager.class);
		vil.setCustomName(shops.get(jobCodename).getDisplayName());
		vil.setCustomNameVisible(true);
		shops.get(jobCodename).addEntity(vil);
		return true;
	}
	
}
