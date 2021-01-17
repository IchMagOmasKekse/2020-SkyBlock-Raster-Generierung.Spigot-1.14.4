package me.crafttale.de.casino;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.crafttale.de.Chat.MessageType;
import me.crafttale.de.SkyBlock;
import me.crafttale.de.casino.CasinoAlgorithm.ContentType;
import me.crafttale.de.casino.CasinoAlgorithm.ItemProbability;
import me.crafttale.de.particle.ParticleManager;

@SuppressWarnings("deprecation")
public class CasinoManager {
	
	private static HashMap<String, Casino> casinos = new HashMap<String, Casino>();
	private static LinkedList<Player> players = new LinkedList<Player>();
	private static HashMap<ContentType, LinkedList<ItemProbability>> casinoContent = new HashMap<ContentType, LinkedList<ItemProbability>>();
	private static BukkitRunnable startButtonRotator = null;
	
	public CasinoManager() {
		Inventory inv = Bukkit.createInventory(null, 54, "");
		ItemStack item = new ItemStack(Material.DIAMOND);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		inv.addItem(item);
		
		item = new ItemStack(Material.EMERALD);
		meta = item.getItemMeta();
		inv.addItem(item);
		
		item = new ItemStack(Material.IRON_INGOT);
		meta = item.getItemMeta();
		inv.addItem(item);
		
		item = new ItemStack(Material.GOLD_INGOT);
		meta = item.getItemMeta();
		inv.addItem(item);
		
		item = new ItemStack(Material.REDSTONE);
		meta = item.getItemMeta();
		inv.addItem(item);
		
		item = new ItemStack(Material.LAPIS_LAZULI);
		meta = item.getItemMeta();
		inv.addItem(item);
		
		item = new ItemStack(Material.COAL);
		meta = item.getItemMeta();
		inv.addItem(item);
		
		item = new ItemStack(Material.CHARCOAL);
		meta = item.getItemMeta();
		inv.addItem(item);
		
		item = new ItemStack(Material.FLINT);
		meta = item.getItemMeta();
		inv.addItem(item);
		
		item = new ItemStack(Material.DIRT);
		meta = item.getItemMeta();
		inv.addItem(item);
		
		item = new ItemStack(Material.GRASS_BLOCK);
		meta = item.getItemMeta();
		inv.addItem(item);
		
		item = new ItemStack(Material.GOLD_NUGGET);
		meta = item.getItemMeta();
		meta.setDisplayName("§eSkyCoin Depot");
		lore.clear();
		lore.add("§f§oLöse dieses Depot bei einem Bankier ein,");
		lore.add("§f§oum die §e§oSkyCoins §f§ozu erhalten");
		meta.setLore(lore);
		item.setItemMeta(meta);
		inv.addItem(item);
		
		writeContent(inv.getContents(), ContentType.DEFAULT);
		reloadContent();
		
		CasinoTypeA a = new CasinoTypeA();
		casinos.put(a.identifier, a);
		CasinoTypeB b = new CasinoTypeB();
		casinos.put(b.identifier, b);
		
		startButtonRotator = new BukkitRunnable() {
			int frame = 0;
			int particleAmount = 40;
			double fourthOfParticle = particleAmount/4;
			double fourthOfBlock = (1/fourthOfParticle);
			int trigger = 0;
			@Override public void run() {
				if(casinos.isEmpty() == false) {
					for(Casino c : casinos.values()) {
						if(c.rewardSpawnLocation != null && c.rewardItem != null && c.rewardItem.getCustomName().contains("CASINO-")) {
							switch(frame) {
							case 0:
								ParticleManager.sendParticleToAll(Particle.REDSTONE, c.rewardSpawnLocation.clone().add(trigger*fourthOfBlock, 0, 0));
								break;
							case 1:
								ParticleManager.sendParticleToAll(Particle.REDSTONE, c.rewardSpawnLocation.clone().add(1, 0, trigger*fourthOfBlock));
								break;
							case 2:
								ParticleManager.sendParticleToAll(Particle.REDSTONE, c.rewardSpawnLocation.clone().add((1-trigger*fourthOfBlock), 0, 1));
								break;
							case 3:
								ParticleManager.sendParticleToAll(Particle.REDSTONE, c.rewardSpawnLocation.clone().add(0, 0, (1-trigger*fourthOfBlock)));
								break;
							}
						}
						c.setRotationOfStartButtonEntity();
					}
					switch(frame) {
					case 0:
						if(trigger != fourthOfParticle) {
							trigger++;
						}else {
							trigger = 0;
							frame++;
						}
						break;
					case 1:
						if(trigger != fourthOfParticle) {
							trigger++;
						}else {
							trigger = 0;
							frame++;
						}
						break;
					case 2:
						if(trigger != fourthOfParticle) {
							trigger++;
						}else {
							trigger = 0;
							frame++;
						}
						break;
					case 3:
						if(trigger != fourthOfParticle) {
							trigger++;
						}else {
							trigger = 0;
							frame=0;
						}
						break;
					}
				}
			} };
		startButtonRotator.runTaskTimer(SkyBlock.getSB(), 0l, 5l);
	}
	
	public static void disable() {
		for(String s : casinos.keySet()) {
			casinos.get(s).disable();
		}
		startButtonRotator.cancel();
	}
	
	public static void remove(Player p) {
		players.remove(p);
	}
	
	public static void reloadContent() {
		casinoContent.clear();
		casinoContent.put(ContentType.DEFAULT, readContent(ContentType.DEFAULT));
	}
	
	
	/**
	 * Liest den Inhalt einer Content Anfertigung aus.
	 * Dieser wird dann im Casino zum Spielen zur Verfügung gestellt.
	 * @param type
	 * @return
	 */
	public static LinkedList<ItemProbability> readContent(ContentType type) {
		File file = new File("plugins/"+SkyBlock.getSB().getDescription().getName()+"/Casino/"+type.getFileName());
		if(file.exists() == false) {
			SkyBlock.sendOperatorMessage(MessageType.ERROR, "Es wurde gerade versucht den Casino-Inhalt namens '"+type.fileName+"' zu laden.",
					"Dieser existiert aber nicht*mehr.",
					"Gebe bitte §nsofort "+MessageType.ERROR.sx()+"Ariano(IchMagOmasKekse) Bescheid!");
			return null;
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		LinkedList<ItemProbability> content = new LinkedList<ItemProbability>();
		
		int itemAmount = cfg.getInt("Content.Item Amount");
		ItemStack item = null;
		ItemProbability iP = null;
		for(int i = 0; i != itemAmount; i++) {
			item = cfg.getItemStack("Content.Items."+i+".ItemStack");
			if(item != null && item.getType() != Material.AIR) {
				iP = new ItemProbability(item,
						cfg.getDouble("Content.Items."+i+".Settings.Chance"),
						cfg.getBoolean("Content.Items."+i+".Settings.Allow Doubling"),
						cfg.getInt("Content.Items."+i+".Settings.Allowed Doubling Amount"));
				content.add(iP.clone());
			}
		}
		
		return content;
	}
	
	/**
	 * Wandelt einen ItemStack-Array in eine LinkedList<ItemStack> um und speichert diesen Inhalt dann in eine Datei
	 * @param items
	 * @param type
	 * @return
	 */
	public static boolean writeContent(ItemStack[] items, ContentType type) {
		LinkedList<ItemStack> list = new LinkedList<ItemStack>();
		LinkedList<ItemStack> filtered_list = new LinkedList<ItemStack>();
		for(int i = 0; i != items.length; i++) {
			list.add(items[i]);
		}
		
		for(ItemStack item : list) {
			if(item != null && item.getType() != Material.AIR) filtered_list.add(item);
		}
		return writeContent(filtered_list, type);
	}
	
	/**
	 * Speichert einen Inhalt eines Casinos in eine Datei.
	 * @param items
	 * @param type
	 * @return
	 */
	public static boolean writeContent(LinkedList<ItemStack> items, ContentType type) {
		File file = new File("plugins/"+SkyBlock.getSB().getDescription().getName()+"/Casino/"+type.getFileName());
		if(file.exists()) return false;
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		cfg.set("Content.Item Amount", items.size());
		int index = 0;
		for(ItemStack i : items) {
			if(i != null && i.getType() != Material.AIR) {				
				index++;
				cfg.set("Content.Items."+index+".ItemStack", i);
				cfg.set("Content.Items."+index+".Settings.Chance", 50.0d);
				cfg.set("Content.Items."+index+".Settings.Allow Doubling", true);
				cfg.set("Content.Items."+index+".Settings.Allowed Doubling Amount", 3);
			}
		}
		try {cfg.save(file);} catch (IOException e) {e.printStackTrace();}
		return index == 0 ? false : true;
	}
	
	/**
	 * Gibt eine LinkedList<ItemProbability> mit Inhalt eines ContentTypes zurück.
	 * @param type
	 * @return
	 */
	public static LinkedList<ItemProbability> getContentByType(ContentType type) {
		return casinoContent.get(type);
	}
	
	public static void onPickupItem(PlayerPickupItemEvent e) {
		if(e.getItem() != null) {
			if(e.getItem().getCustomName() != null && e.getItem().getCustomName().equals("") == false && e.getItem().getCustomName().contains("CASINO-A-REWARD")) {
				
				e.getItem().setCustomName(e.getItem().getCustomName().replace("CASINO-A-REWARD", ""));
				
				if(casinos.get(CasinoType.ArmorStandDisplay.identifier).rewardItem == e.getItem())
					casinos.get(CasinoType.ArmorStandDisplay.identifier).rewardItem = null;
			}else if(e.getItem().getCustomName() != null && e.getItem().getCustomName().equals("") == false && e.getItem().getCustomName().contains("CASINO-B-REWARD")) {
				
				e.getItem().setCustomName(e.getItem().getCustomName().replace("CASINO-B-REWARD", ""));
				
				if(casinos.get(CasinoType.ItemFrameDisplay.identifier).rewardItem == e.getItem())
					casinos.get(CasinoType.ItemFrameDisplay.identifier).rewardItem = null;
			}
		}
	}
	
	public static void onEntityClick(PlayerInteractAtEntityEvent e) {
		if(e.getRightClicked().getCustomName() == null || e.getRightClicked().getCustomName().equals("")) return;
		if(e.getRightClicked() instanceof ItemFrame) {
			if(e.getRightClicked().getCustomName().equals("CASINO")) {
				e.setCancelled(true);
			}
		}else if(e.getRightClicked() instanceof ArmorStand) {
			if(e.getRightClicked().getCustomName().equals("CASINO")) {
				e.setCancelled(true);
			}
		}else if(e.getRightClicked() instanceof WanderingTrader) {
			if(e.getRightClicked().getCustomName().equals(CasinoTypeA.startButtonName)) {
				if(players.contains(e.getPlayer()) == false) {
					e.setCancelled(true);
					
					if(casinos.get(CasinoType.ArmorStandDisplay.id()).isInUse() == false) {						
						casinos.get(CasinoType.ArmorStandDisplay.id()).spin(e.getPlayer(), ContentType.DEFAULT);
					}else SkyBlock.sendMessage(MessageType.INFO, e.getPlayer(),	casinos.get(CasinoType.ArmorStandDisplay.id()).spinner.getName()+" ist gerade dran.");
				}
			}
		}else if(e.getRightClicked() instanceof Villager) {
			if(e.getRightClicked().getCustomName().equals(CasinoTypeB.startButtonName)) {				
				if(players.contains(e.getPlayer()) == false) {
					e.setCancelled(true);
					e.getPlayer().closeInventory();
					if(casinos.get(CasinoType.ItemFrameDisplay.id()).isInUse() == false) {
						casinos.get(CasinoType.ItemFrameDisplay.id()).spin(e.getPlayer(), ContentType.DEFAULT);
					}else SkyBlock.sendMessage(MessageType.INFO, e.getPlayer(),	casinos.get(CasinoType.ItemFrameDisplay.id()).spinner.getName()+" ist gerade dran.");
				}
			}
		}
	}
	
	public static void onDamage(EntityDamageByEntityEvent e) {
		if(e.getEntity().getCustomName() == null || e.getEntity().getCustomName().equals("")) return;
		if(e.getEntity() instanceof ItemFrame) {
			if(e.getEntity().getCustomName().equals("CASINO")) {
				e.setCancelled(true);
			}
		}else if(e.getEntity() instanceof ArmorStand) {
			if(e.getEntity().getCustomName().equals("CASINO")) {
				e.setCancelled(true);
			}
		}
	}
	
	public static void onOpeningInventory(InventoryOpenEvent e) {
		if(e.getView().getTitle().equals(CasinoTypeB.startButtonName)) e.setCancelled(true);
		else if(e.getView().getTitle().equals(CasinoTypeA.startButtonName)) e.setCancelled(true);
	}
	
	public static enum CasinoType {
		ArmorStandDisplay("Casino-ArmorDisplay"),
		ItemFrameDisplay("Casino-FrameDisplay");
		
		String identifier = "";
		
		CasinoType(String identifier) {
			this.identifier = identifier;
		}
		
		public String id() {
			return identifier;
		}
		
	}
	
}
