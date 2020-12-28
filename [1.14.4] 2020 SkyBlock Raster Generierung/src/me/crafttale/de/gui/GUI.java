package me.crafttale.de.gui;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.crafttale.de.profiles.IslandManager;
import me.crafttale.de.profiles.IslandManager.IslandData;

public abstract class GUI {
	
	protected Player player = null, player2 = null, player3 = null;
	protected UUID targetUUID = null;
	protected Inventory openedInv = null;
	protected GUIType type = null;
	protected boolean canBeClosedNow = true, forceClose = false, usesAnInventory = true, stopAnimation = false, closeInventoryWhenGetClosed = true;
	protected String processedTitle = "";
	
	protected int maxFrames = 10;
	protected int frame = 0;
	protected boolean loop = true;
	protected int page_number = 0;
	protected int max_page_numbers = 0;
	protected long speed = 3l;
	protected BukkitRunnable animator = null;
	protected ItemStack item = null; //Variable um Item in ein Inventar hinzuzufügen
	protected ItemMeta meta = null; //Variable um ItemMeta eines Items im Inventar zu ändern
	protected ArrayList<String> lore = new ArrayList<String>(); //Lore für Items die in das Inventar hinzugefügt werden
	
	public GUI(Player player, GUIType type) {
		this.player = player;
		this.type = type;
		start();
	}
	public GUI(Player player, UUID targetUUID, GUIType type) {
		this.player = player;
		this.type = type;
		this.targetUUID = targetUUID;
		start();
	}
	public GUI(Player player, Player player2, GUIType type) {
		this.player = player;
		this.player2 = player2;
		this.type = type;
		start();
	}
	public GUI(Player player, Player player2, Player player3, GUIType type) {
		this.player = player;
		this.player2 = player2;
		this.player3 = player3;
		this.type = type;
		start();
	}
	
	public abstract void start();
	public abstract void stop();
	public abstract void reset();
	public abstract void clickedItem(InventoryClickEvent e, ItemStack item);
	public void nextFrame() {
		frame++;
		if(frame == maxFrames && loop) {
		}else if(frame == maxFrames && loop == false) GUIManager.closeGUI(player);
	}
	public void stopAnimation() {
		this.stopAnimation = true;
	}
	
	
	public String getProcessedTitle() {
		return processedTitle;
	}
	
	/**
	 * Schließt das GUI unsicher
	 * @return
	 */
	public boolean forceClose() {
		forceClose = true;
		return saveAndCloseGUI();
	}
	
	/**
	 * Schließt ein GUI
	 * @return
	 */
	public void closeGui() {
		stop();
		if(closeInventoryWhenGetClosed) player.closeInventory();
	}
	
	/**
	 * Lädt ein GUI und öffnet dieses einen Spieler
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public void load() {
		
		ItemStack item = new ItemStack(Material.AIR);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		
		switch(type) {
		case BLANK_CHEST_BIG_GUI:
			openedInv = Bukkit.createInventory(player, type.getSize(), type.getTitle());
			processedTitle = type.getTitle();
			GUIManager.guis.put(player,  this);
			player.openInventory(openedInv);
			break;
		case BLANK_CHEST_NORMAL_GUI:
			openedInv = Bukkit.createInventory(player, type.getSize(), type.getTitle());
			processedTitle = type.getTitle();
			GUIManager.guis.put(player,  this);
			player.openInventory(openedInv);
			break;
		case BLANK_FURNACE_GUI:
			openedInv = Bukkit.createInventory(player, type.getType(), type.getTitle());
			processedTitle = type.getTitle();
			GUIManager.guis.put(player,  this);
			player.openInventory(openedInv);
			break;
		case BLANK_WORKBENCH_GUI:
			openedInv = Bukkit.createInventory(player, type.getType(), type.getTitle());
			processedTitle = type.getTitle();
			GUIManager.guis.put(player,  this);
			player.openInventory(openedInv);
			break;
		case OWN_CREATION:
			processedTitle = type.getTitle();
			GUIManager.guis.put(player,  this);
			player.openInventory(openedInv);
			break;
		case REWARD_GUI:
			openedInv = Bukkit.createInventory(player, 9, type.getTitle());
			processedTitle = type.getTitle();
			
			item = new ItemStack(Material.CHEST);
			meta = item.getItemMeta();
			meta.setDisplayName("§6SKYCOINS");
			lore.clear();
			lore.add(" §7» §aAnklicken um deine Belohnung abzuholen!");
			meta.setLore(lore);
			item.setItemMeta(meta);
			openedInv.setItem(4, item);
			
			GUIManager.guis.put(player,  this);
			player.openInventory(openedInv);
			break;
		case ISLAND_VISIT_GUI:
			openedInv = Bukkit.createInventory(null, type.getType(), "Besuch von: "+player2.getName());
			processedTitle = type.getTitle();
			
			item = new ItemStack(Material.LIME_BANNER);
			meta = item.getItemMeta();
			meta.setDisplayName("§aannehmen");
			item.setItemMeta(meta);
			openedInv.setItem(0, item);
			
			item = new ItemStack(Material.RED_BANNER);
			meta = item.getItemMeta();
			meta.setDisplayName("§cablehnen");
			item.setItemMeta(meta);
			openedInv.setItem(4, item);
			
			GUIManager.guis.put(player, this);
			player.openInventory(openedInv);
			break;
		case JOIN_MELODY:
			processedTitle = type.getTitle();
			GUIManager.guis.put(player,  this);
			break;
		case VISIT_ACCEPTED_MELODY:
			processedTitle = type.getTitle();
			GUIManager.guis.put(player,  this);
			break;
		case VISIT_DENIED_MELODY:
			processedTitle = type.getTitle();
			GUIManager.guis.put(player,  this);
			break;
		case LOADING_ANIMA:
			processedTitle = type.getTitle();
			GUIManager.guis.put(player,  this);
			break;
		case ISLAND_SETTINGS_GUI:
			openedInv = Bukkit.createInventory(null, type.getSize(), type.getTitle());
			processedTitle = type.getTitle();
			
			item = new ItemStack(Material.BOOK);
			meta = item.getItemMeta();
			meta.setDisplayName("§fBesucher");
			lore.clear();
			lore.add("§7Anderen Spielern eine Anfrage für einen Besuch deiner Insel erlauben/verbieten");
			meta.setLore(lore);
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
			item.setItemMeta(meta);
			openedInv.setItem(0, item);
			
			item = new ItemStack(Material.FLINT_AND_STEEL);
			meta = item.getItemMeta();
			meta.setDisplayName("§fFeuerausbreitung");
			lore.clear();
			lore.add("§7Ausbreiten und ungewolltes entstehen von Feuer erlauben/verbieten");
			meta.setLore(lore);
			item.setItemMeta(meta);
			openedInv.setItem(1, item);
			
			item = new ItemStack(Material.SKELETON_SKULL);
			meta = item.getItemMeta();
			meta.setDisplayName("§fMonster Spawning(durch die Natur)");
			lore.clear();
			lore.add("§7Das Spawnen von Monstern auf natürlicher Weise erlauben/verbieten");
			meta.setLore(lore);
			item.setItemMeta(meta);
			openedInv.setItem(2, item);
			
			item = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta sMeta = (SkullMeta)item.getItemMeta();
			sMeta.setOwner("Kolish");
			sMeta.setDisplayName("§fTier Spawning(durch die Natur)");
			lore.clear();
			lore.add("§7Das Spawnen von Tieren auf natürlicher Weise erlauben/verbieten");
			sMeta.setLore(lore);
			item.setItemMeta(sMeta);
			openedInv.setItem(3, item);
			
			item = new ItemStack(Material.TNT);
			meta = item.getItemMeta();
			meta.setDisplayName("§fZerstörung durch TNT");
			lore.clear();
			lore.add("§7Zerstörung durch TNT erlauben/verbieten");
			meta.setLore(lore);
			item.setItemMeta(meta);
			openedInv.setItem(4, item);
			
			item = new ItemStack(Material.CREEPER_HEAD);
			meta = item.getItemMeta();
			meta.setDisplayName("§fZerstörung durch Mobs");
			lore.clear();
			lore.add("§7Zerstörung durch Mobs erlauben/verbieten");
			meta.setLore(lore);
			item.setItemMeta(meta);
			openedInv.setItem(5, item);
			
			item = new ItemStack(Material.NETHERITE_SWORD);
			meta = item.getItemMeta();
			meta.setDisplayName("§fPVP");
			lore.clear();
			lore.add("§7Schaden durch anderen Spielern erlauben/verbieten");
			meta.setLore(lore);
			meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
			item.setItemMeta(meta);
			openedInv.setItem(6, item);
			
			item = new ItemStack(Material.SHIELD);
			meta = item.getItemMeta();
			meta.setDisplayName("§fMonster beseitigen");
			lore.clear();
			lore.add("§7Alle Monster auf deiner Insel beseitigen");
			meta.setLore(lore);
			item.setItemMeta(meta);
			openedInv.setItem(18, item);
			
			/* Status Item */
			
			ItemStack verboten = new ItemStack(Material.RED_STAINED_GLASS_PANE);
			meta = verboten.getItemMeta();
			meta.setDisplayName("§cVerboten");
			verboten.setItemMeta(meta);
			
			item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
			meta = item.getItemMeta();
			meta.setDisplayName("§aErlaubt");
			item.setItemMeta(meta);
			
			IslandData data = IslandManager.getIslandData(IslandManager.getProfile(player).getIslandID());
			
			openedInv.setItem(9, (data.getSettings().isAllowVisiting() ? item : verboten));
			openedInv.setItem(10, (data.getSettings().isFireSpread() ? item : verboten));
			openedInv.setItem(11, (data.getSettings().isMonsterSpawning() ? item : verboten));
			openedInv.setItem(12, (data.getSettings().isAnimalSpawning() ? item : verboten));
			openedInv.setItem(13, (data.getSettings().isTntDamage() ? item : verboten));
			openedInv.setItem(14, (data.getSettings().isMobGriefing() ? item : verboten));
			openedInv.setItem(15, (data.getSettings().isPvp() ? item : verboten));
			
			/* Funktionen hinzufügen */
			
			item = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
			meta = item.getItemMeta();
			meta.setDisplayName("§eDurchführen");
			item.setItemMeta(meta);
			
			openedInv.setItem(27, item);
			GUIManager.guis.put(player,  this);
			player.openInventory(openedInv);
			break;
		case ISLAND_BAN_PLAYER_GUI:
			GUIManager.guis.put(player, this);
			break;
		case ISLAND_INVITE__GUI:
			GUIManager.guis.put(player, this);
			break;
		case ISLAND_PARDON_PLAYER_GUI:
			GUIManager.guis.put(player, this);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Speichert das GUI und Schließt es anschließend
	 * @return;
	 */
	public boolean saveAndCloseGUI() {
		if(canBeClosedNow == false && forceClose == false) return false;
		/* Save*/
		
		/* Close */
		GUIManager.closeGUI(player);
		return true;
		
	}
	
	public GUIType getGUIType() {
		return type;
	}
	
	public static enum GUIType {
		
		/* Title, Size,  */
		JOIN_MELODY("§bGUI_MELODY", 9, InventoryType.CHEST, false, false),
		VISIT_ACCEPTED_MELODY("§bVISIT_ACCEPTED_MELODY", 9, InventoryType.CHEST, false, false),
		VISIT_DENIED_MELODY("§bVISIT_DENIED_MELODY", 9, InventoryType.CHEST, false, false),
		BLANK_CHEST_BIG_GUI("§bGUI_CHEST", 54, InventoryType.CHEST, false, true),
		BLANK_CHEST_NORMAL_GUI("§bGUI_CHEST", 27, InventoryType.CHEST, false, true),
		BLANK_WORKBENCH_GUI("§bGUI_WORKBENCH", 54, InventoryType.WORKBENCH, false, true),
		BLANK_FURNACE_GUI("§bGUI_FURNACE", 54, InventoryType.FURNACE, false, true),
		REWARD_GUI("§fTägliche Belohnung", 9, InventoryType.CHEST, false, true),
		ISLAND_VISIT_GUI("§bVISIT_GUI", 9, InventoryType.HOPPER, false, true),
		OWN_CREATION("§bEigene Kreation", 9, InventoryType.CHEST, false, true),
		LOADING_ANIMA("§bLade Animation", 9, InventoryType.CHEST, false, false),
		ISLAND_SETTINGS_GUI("§8Einstellungen", 54, InventoryType.CHEST, true, true),
		ISLAND_BAN_PLAYER_GUI("§8Verbannungs Bestätigung", 9, InventoryType.CHEST, true, true),
		ISLAND_INVITE__GUI("§8Einladung: ", 9, InventoryType.CHEST, true, true),
		ISLAND_PARDON_PLAYER_GUI("§8Anulierung: ", 9, InventoryType.CHEST, true, true);
		
		private String title;
		private int size;
		private InventoryType type;
		private boolean stayFucused, usesAnInventory;
		
		GUIType(String title, int size, InventoryType type, boolean stayFucused, boolean usesAnInventory) {
			this.title = title;
			this.size = size;
			this.type = type;
			this.usesAnInventory = usesAnInventory;
		}
		/**
		 * Gibt den Titel des Inventars zurück
		 * @return
		 */
		public String getTitle() {
			return title;
		}
		/**
		 * Gibt die Größe der Inventare zurück
		 * @return
		 */
		public int getSize() {
			return size;
		}
		/**
		 * Gibt den Typ des Inventares zurück
		 * @return
		 */
		public InventoryType getType() {
			return type;
		}
		/**
		 * Gibt an, ob ein GUI einfach normal geschlossen werden kann oder ob es gespeichert werden muss.
		 * @return
		 */
		public boolean hasToStayFucused() {
			return stayFucused;
		}
		/**
		 * Gibt an, ob ein GUIType ein Inventar benutzt und dementsprechend der GUIClicker reagieren soll oder nicht.
		 * @return
		 */
		public boolean usesAnInventory() {
			return usesAnInventory;
		}
		
		
	}
	
}