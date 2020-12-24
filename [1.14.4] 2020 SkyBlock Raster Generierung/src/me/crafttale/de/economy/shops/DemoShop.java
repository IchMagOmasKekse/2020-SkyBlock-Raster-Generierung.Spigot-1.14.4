package me.crafttale.de.economy.shops;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.crafttale.de.SkyBlock;
import me.crafttale.de.Chat.MessageType;
import me.crafttale.de.economy.EconomyManager;
import me.crafttale.de.economy.EconomyManager.EditOperation;
import me.crafttale.de.economy.EconomyManager.MoneyType;
import me.crafttale.de.profiles.PlayerProfiler;
import me.ichmagomaskekse.de.GUI;

public class DemoShop extends GUI {
	
	protected static HashMap<Material, ItemPrice> prices = new HashMap<Material, ItemPrice>();
	private double umsatz = 0.0d;
	
	public DemoShop(Player player) {
		super(player, GUIType.OWN_CREATION);
	}

	@Override
	public void start() {
		prices.put(Material.DIRT, new ItemPrice(40d, 1, 2.5d, 3));
		prices.put(Material.GRASS_BLOCK, new ItemPrice(45d, 1, 3d, 3));
		prices.put(Material.COBBLESTONE, new ItemPrice(15d, 16, 0.3d, 8));
		prices.put(Material.STONE, new ItemPrice(17d, 16, 0.5d, 8));
		
		prices.put(Material.BOW, new ItemPrice(120d, 1, 1.5d, 1));
		prices.put(Material.BONE, new ItemPrice(40d, 16, 0.5d, 6));
		prices.put(Material.ROTTEN_FLESH, new ItemPrice(5d, 12, 0.2d, 8));
		prices.put(Material.GUNPOWDER, new ItemPrice(4d, 1, 0.2d, 6));
		prices.put(Material.ARROW, new ItemPrice(25d, 8, 0.7d, 9));
		prices.put(Material.STRING, new ItemPrice(12d, 4, 0.4d, 12));
		openedInv = Bukkit.createInventory(null, 27, "DemoShop");
		
		
		for(Material mat : prices.keySet()) {			
			item = new ItemStack(mat);
			meta = item.getItemMeta();
			String mat_name = (mat.toString().substring(0,1).toUpperCase()+mat.toString().substring(1,mat.toString().length()).toLowerCase()).replace("_", "");
			meta.setDisplayName("§a"+mat_name);
			lore.clear();
			lore.add("");
			addLore(lore, item.getType());
			meta.setLore(lore);
			item.setItemMeta(meta);
			
			openedInv.addItem(item.clone());
		}
		
		item = new ItemStack(Material.GOLD_NUGGET);
		meta = item.getItemMeta();
		meta.setDisplayName("§aDu hast insgesamt §6 0 Coins §abekommen");
		item.setItemMeta(meta);
		
		openedInv.setItem(openedInv.getSize()-1, item.clone());
	}
	
	public void addLore(ArrayList<String> lore, Material type) {
		lore.add(" §7» §fKaufpreis:§6 "+prices.get(item.getType()).getBuyAmount()+" Stück -> "+prices.get(item.getType()).getBuyPrice()+" Coins");
		lore.add(" §7» §fVerkaufpreis:§6 "+prices.get(item.getType()).getSellAmount()+" Stück -> "+prices.get(item.getType()).getSellPrice()+" Coins");
		lore.add(" §7» §eLinksklick §fzum Kaufen");
		lore.add(" §7» §eRechtsklick §fzum Verkaufen");
	}

	@Override
	public void stop() {
		player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 6f, -0.5f);
	}

	@Override
	public void reset() {
	}

	@Override
	public void clickedItem(InventoryClickEvent e, ItemStack item) {
		item = new ItemStack(item.getType());
		if(prices.containsKey(item.getType()) == false) return;
		if(e.getClick() == ClickType.LEFT) {
			item.setAmount(prices.get(item.getType()).getBuyAmount());
			if(buy(item)) {
				//Ausreichend Geld
//				SkyBlock.sendMessage(MessageType.INFO, player, "Du hast ein Item gekauft");
				ItemStack i = openedInv.getItem(openedInv.getSize()-1);
				ItemMeta meta = i.getItemMeta();
				meta.setDisplayName("§aDu hast insgesamt §6 "+umsatz+" Coins §aerhalten");
				i.setItemMeta(meta);
				openedInv.setItem(openedInv.getSize()-1, i);
				
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 6f, 6f);
			} else {
				//Zu wenig Geld
				SkyBlock.sendMessage(MessageType.INFO, player, "Du hast zu wenig Geld");
				
			}
		}else if(e.getClick() == ClickType.RIGHT) {
			item.setAmount(prices.get(item.getType()).getSellAmount());
			if(sell(item)) {
				//Ausreichend Geld
//				SkyBlock.sendMessage(MessageType.INFO, player, "Du hast ein Item verkauft");
				ItemStack i = openedInv.getItem(openedInv.getSize()-1);
				ItemMeta meta = i.getItemMeta();
				meta.setDisplayName("§aDu hast insgesamt §6 "+umsatz+" Coins §aerhalten");
				i.setItemMeta(meta);
				openedInv.setItem(openedInv.getSize()-1, i);
				
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 6f, 5.5f);
				
			} else {
				//Zu wenig Geld
				SkyBlock.sendMessage(MessageType.INFO, player, "Du hast kein passendes Item zum Verkauf in deinem Inventar");
				
			}
		}
		
	}
	
	/**
	 * Kauft ein Item und zieht dem Käufer das Geld ab und gibt das Item.
	 * Schickt jedoch keine Kauf-Bestätigung
	 * @param item
	 * @return
	 */
	public boolean buy(ItemStack item) {
		if(EconomyManager.getMoney(PlayerProfiler.getUUID(player), MoneyType.MONEY1) >= prices.get(item.getType()).getBuyPrice()) {
			EconomyManager.editMoney(PlayerProfiler.getUUID(player), MoneyType.MONEY1, prices.get(item.getType()).getBuyPrice(), EditOperation.SUBTRACT);
			player.getInventory().addItem(item);
			umsatz-=prices.get(item.getType()).getBuyPrice();
			umsatz = EconomyManager.round(umsatz, 2);
			return true;
		}else return false;
	}
	/**
	 * Verkauft ein Item und gibt dem Spieler das Geld und zieht das Item ab.
	 * Schickt jedoch keine Verkaufs-Bestätigung
	 * @param item
	 * @return
	 */
	public boolean sell(ItemStack item) {
		if(player.getInventory().contains(item.getType())) {
			boolean cancel = true;
			for(ItemStack i : player.getInventory().getContents()) {
				if(i != null && i.getType() == item.getType()) {
					if(i.getAmount() >= item.getAmount()) cancel = false;
				}
			}
			if(cancel) return false;
			EconomyManager.editMoney(PlayerProfiler.getUUID(player), MoneyType.MONEY1, prices.get(item.getType()).getSellPrice(), EditOperation.ADD);
			player.getInventory().removeItem(item);
			umsatz+=prices.get(item.getType()).getSellPrice();
			umsatz = EconomyManager.round(umsatz, 2);
			return true;
		}else return false;
	}
	
	public class ItemPrice {
		
		double sell_price, buy_price;
		int sell_amount, buy_amount;
		
		public ItemPrice(double buy_price, int buy_amount, double sell_price, int sell_amount) {
			this.buy_price = buy_price;
			this.buy_amount = buy_amount;
			this.sell_price = sell_price;
			this.sell_amount = sell_amount;
		}

		public double getSellPrice() {
			return sell_price;
		}

		public double getBuyPrice() {
			return buy_price;
		}

		public int getSellAmount() {
			return sell_amount;
		}

		public int getBuyAmount() {
			return buy_amount;
		}
		
		
	}

}
