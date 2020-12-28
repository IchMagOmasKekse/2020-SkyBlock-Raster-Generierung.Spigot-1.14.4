package me.crafttale.de.economy.shops;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import me.crafttale.de.Chat.MessageType;
import me.crafttale.de.SkyBlock;
import me.crafttale.de.economy.EconomyManager;
import me.crafttale.de.economy.EconomyManager.EditOperation;
import me.crafttale.de.economy.EconomyManager.MoneyType;
import me.crafttale.de.economy.PriceSetter;
import me.crafttale.de.gui.GUI;
import me.crafttale.de.profiles.PlayerProfiler;
import net.md_5.bungee.api.ChatColor;

public class DemoShop extends GUI {
	
//	protected static HashMap<Material, ItemPrice> prices = new HashMap<Material, ItemPrice>();
	private double umsatz = 0.0d;
	
	public DemoShop(Player player) {
		super(player, GUIType.OWN_CREATION);
	}

	@Override
	public void start() {
//		prices.put(Material.DIRT, new ItemPrice(40d, 16, 2.5d, 3));
//		prices.put(Material.GRASS_BLOCK, new ItemPrice(45d, 1, 3d, 3));
//		prices.put(Material.COBBLESTONE, new ItemPrice(15d, 16, 0.3d, 8));
//		prices.put(Material.STONE, new ItemPrice(17d, 16, 0.5d, 8));
//		
//		prices.put(Material.BOW, new ItemPrice(120d, 1, 1.5d, 1));
//		prices.put(Material.BONE, new ItemPrice(40d, 16, 0.5d, 6));
//		prices.put(Material.ROTTEN_FLESH, new ItemPrice(5d, 12, 0.2d, 8));
//		prices.put(Material.GUNPOWDER, new ItemPrice(4d, 1, 0.2d, 6));
//		prices.put(Material.ARROW, new ItemPrice(25d, 8, 0.7d, 9));
//		prices.put(Material.STRING, new ItemPrice(12d, 4, 0.4d, 12));
		openedInv = Bukkit.createInventory(null, 27, "DemoShop");
		
		
		for(Material mat : PriceSetter.getProducts().keySet()) {			
			item = new ItemStack(mat);
			meta = item.getItemMeta();
			meta.setDisplayName("§a"+PriceSetter.getItemPrice(mat).getDisplayname());
			lore.clear();
			lore.add("");
			addLore(lore, item.getType());
			meta.setLore(lore);
			item.setItemMeta(meta);
			
			openedInv.addItem(item.clone());
		}
		
		item = new ItemStack(Material.GOLD_NUGGET);
		meta = item.getItemMeta();
		meta.setDisplayName("§aDu hast noch keinen Kauf oder Verkauf getätigt");
		lore.clear();
		lore.add("§7Dein Kontostand: §a"+EconomyManager.getMoney(PlayerProfiler.getCurrentPlayerName(player), MoneyType.MONEY1)+" SkyCoins");
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		openedInv.setItem(openedInv.getSize()-1, item.clone());
	}
	
	public void addLore(ArrayList<String> lore, Material type) {
		ItemPrice price = PriceSetter.getItemPrice(type);
		if(price != null) {
			lore.add(" §7» §fKaufpreis:§6 "+price.getBuyAmount()+" Stück -> "+price.getBuyPrice()+" Coins");
			lore.add(" §7» §fVerkaufpreis:§6 "+price.getSellAmount()+" Stück -> "+price.getSellPrice()+" Coins");
			lore.add(" §7» §eLinksklick §fzum Kaufen");
			lore.add(" §7» §eRechtsklick §fzum Verkaufen");
		}
	}

	@Override
	public void stop() {
		player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 6f, -0.5f);
		if(umsatz != 0) SkyBlock.sendMessage(MessageType.INFO, player, (umsatz > 0 ? "§aDu hast insgesamt §6 "+umsatz+" Coins §aerhalten" : "§aDu hast insgesamt §6 "+(umsatz * (-1))+" Coins §aausgegeben"));
	}

	@Override
	public void reset() {
	}

	@Override
	public void clickedItem(InventoryClickEvent e, ItemStack item) {
		item = new ItemStack(item.getType());
		if(PriceSetter.getProducts().containsKey(item.getType()) == false) return;
		ItemPrice price = PriceSetter.getItemPrice(item.getType());
		if(price != null) {			
			if(e.getClick() == ClickType.LEFT) {
				item.setAmount(price.getBuyAmount());
				if(buy(item)) {
					//Ausreichend Geld
//				SkyBlock.sendMessage(MessageType.INFO, player, "Du hast ein Item gekauft");
					ItemStack i = openedInv.getItem(openedInv.getSize()-1);
					ItemMeta meta = i.getItemMeta();
					meta.setDisplayName((umsatz > 0 ? "§aDu hast insgesamt §6 "+umsatz+" Coins §aerhalten" : "§aDu hast insgesamt §6 "+(umsatz * (-1))+" Coins §aausgegeben"));
					lore.clear();
					lore.add("§7Dein Kontostand: §a"+EconomyManager.getMoney(PlayerProfiler.getCurrentPlayerName(player), MoneyType.MONEY1)+" SkyCoins");
					meta.setLore(lore);
					i.setItemMeta(meta);
					openedInv.setItem(openedInv.getSize()-1, i);
					
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 6f, 6f);
				} else {
					//Zu wenig Geld
					SkyBlock.sendMessage(MessageType.INFO, player, "Du hast zu wenig Geld oder Dein Inventar ist voll!");
					
				}
			}else if(e.getClick() == ClickType.RIGHT) {
				item.setAmount(price.getSellAmount());
				if(sell(item)) {
					//Ausreichend Geld
//				SkyBlock.sendMessage(MessageType.INFO, player, "Du hast ein Item verkauft");
					ItemStack i = openedInv.getItem(openedInv.getSize()-1);
					ItemMeta meta = i.getItemMeta();
					meta.setDisplayName((umsatz > 0 ? "§aDu hast insgesamt §6 "+umsatz+" Coins §aerhalten" : "§aDu hast insgesamt §6 "+(umsatz * (-1))+" Coins §aausgegeben"));
					lore.clear();
					lore.add("§7Dein Kontostand: §a"+EconomyManager.getMoney(PlayerProfiler.getCurrentPlayerName(player), MoneyType.MONEY1)+" SkyCoins");
					meta.setLore(lore);
					i.setItemMeta(meta);
					openedInv.setItem(openedInv.getSize()-1, i);
					
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 6f, 5.5f);
					
				} else {
					//Zu wenig Geld
					SkyBlock.sendMessage(MessageType.INFO, player, "Du hast kein passendes Item zum Verkauf in deinem Inventar");
					
				}
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
		if(getEmptySlots(player) == 0) {
			return false;
		}
		ItemPrice price = PriceSetter.getItemPrice(item.getType());
		if(EconomyManager.getMoney(PlayerProfiler.getUUID(player), MoneyType.MONEY1) >= price.getBuyPrice()) {
			EconomyManager.editMoney(PlayerProfiler.getUUID(player), MoneyType.MONEY1, price.getBuyPrice(), EditOperation.SUBTRACT);
			player.getInventory().addItem(item);
			umsatz-= price.getBuyPrice();
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
		ItemPrice price = PriceSetter.getItemPrice(item.getType());
		if(player.getInventory().contains(item.getType())) {
			boolean cancel = true;
			for(ItemStack i : player.getInventory().getContents()) {
				if(i != null && i.getType() == item.getType()) {
					if(i.getAmount() >= item.getAmount()) {
						if(item.getItemMeta() instanceof Damageable && i.getItemMeta() instanceof Damageable) {
							if(((Damageable)item.getItemMeta()).getDamage() == ((Damageable)i.getItemMeta()).getDamage()) {
								cancel = false;		
							}
						}else cancel = false;
					}
				}
			}
			if(cancel) return false;
			EconomyManager.editMoney(PlayerProfiler.getUUID(player), MoneyType.MONEY1, price.getSellPrice(), EditOperation.ADD);
			player.getInventory().removeItem(item);
			umsatz+= price.getSellPrice();
			umsatz = EconomyManager.round(umsatz, 2);
			return true;
		}else return false;
	}
	
    public int getEmptySlots(Player p) {
        PlayerInventory inventory = (PlayerInventory) p.getInventory();
        ItemStack[] cont = inventory.getContents();
        int i = 0;
        for (ItemStack item : cont)
          if (item != null && item.getType() != Material.AIR) {
            i++;
          }
        return 36 - i;
    }
	
	public static class ItemPrice {
		
		String displayname = "";
		double sell_price, buy_price;
		int sell_amount, buy_amount;
		
		public ItemPrice(String displayname, double buy_price, int buy_amount, double sell_price, int sell_amount) {
			this.displayname = ChatColor.translateAlternateColorCodes('&', displayname);
			this.buy_price = buy_price;
			this.buy_amount = buy_amount;
			this.sell_price = sell_price;
			this.sell_amount = sell_amount;
		}

		public String getDisplayname() {
			return displayname;
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
