package me.crafttale.de.gui.shop;

import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import me.crafttale.de.economy.ShopManager;
import me.crafttale.de.economy.shops.Shop;
import me.crafttale.de.gui.GUI;

public class ShopTypeSelectGUI extends GUI {

	public ShopTypeSelectGUI(Player player, UUID villagerUUID) {
		super(player, GUIType.OWN_CREATION);
		this.targetUUID = villagerUUID;
	}

	@Override
	public void start() {
		openedInv = Bukkit.createInventory(null,  InventoryType.HOPPER, "Shop-Art auswählen");
		processedTitle = type.getTitle();
		LinkedList<String> lore = new LinkedList<String>();
		lore.add("");
		lore.add(" §8> §7Neue §6Shop-Art §7auswählen.");
		int slot = 0;
		for(Shop shop : ShopManager.shops.values()) {
			item = new ItemStack(shop.getDisplayMaterial());
			meta = item.getItemMeta();
			meta.setDisplayName(shop.getDisplayName());
			meta.setLore(lore);
			item.setItemMeta(meta);
			openedInv.setItem(slot, item);
			slot++;
		}
		
//		player.openInventory(openedInv);
	}

	@Override
	public void stop() {
		player.playSound(player.getEyeLocation(), Sound.UI_BUTTON_CLICK, 6f, 2f);
		player.playSound(player.getEyeLocation(), Sound.BLOCK_ANVIL_LAND, 6f, 2f);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clickedItem(InventoryClickEvent e, ItemStack item) {
		if(item != null) {
			e.setCancelled(true);
			Villager target = null;
			if(item.getType() == ShopManager.shops.get("unset").getDisplayMaterial()) {
				for(Villager vil : player.getWorld().getEntitiesByClass(Villager.class)) {
					if(vil.getUniqueId() == this.targetUUID) {
						ShopManager.shops.get("unset").addEntity(vil);
						target = vil;
						break;
					}
				}
				ShopManager.shops.get("unset").openGUI((Player)e.getWhoClicked(), target);
				
			}else if(item.getType() == ShopManager.shops.get("ore-shop").getDisplayMaterial()) {
				for(Villager vil : player.getWorld().getEntitiesByClass(Villager.class)) {
					if(vil.getUniqueId() == this.targetUUID) {
						ShopManager.shops.get("ore-shop").addEntity(vil);
						target = vil;
						break;
					}
				}
				ShopManager.shops.get("ore-shop").openGUI((Player)e.getWhoClicked(), target);
				
			}else if(item.getType() == ShopManager.shops.get("garden-shop").getDisplayMaterial()) {
				for(Villager vil : player.getWorld().getEntitiesByClass(Villager.class)) {
					if(vil.getUniqueId() == this.targetUUID) {
						ShopManager.shops.get("garden-shop").addEntity(vil);
						target = vil;
						break;
					}
				}
				ShopManager.shops.get("garden-shop").openGUI((Player)e.getWhoClicked(), target);
				
			}
			
		}
	}

}
