package me.dreamisland.de.gui;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.dreamisland.de.SkyBlock;

public class GUIManager implements Listener {
	/**
	 * Diese Klasse steuert GUIs(Inventare) und handled diese
	 */
	
	private static HashMap<HumanEntity, GUI> guis = new HashMap<HumanEntity, GUI>();
	
	public GUIManager() {
		SkyBlock.getSB().getServer().getPluginManager().registerEvents(this, SkyBlock.getSB());
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if(guis.isEmpty() == false) {
			if(guis.containsKey(e.getPlayer())) {
				if(guis.get(e.getPlayer()).openedInv == e.getInventory()) {
					unregisterGUI(e.getPlayer());
				}
			}
		}
	}
		
	/**
	 * Gibt das offene GUI eines Spielers zur¸ck
	 * @param p
	 * @return
	 */
	public static GUI getGUI(Player p) {
		if(hasOpenInventory(p)) return guis.get(p);
		else return null;
	}
	/**
	 * Gibt das offene GUI eines Spielers zur¸ck
	 * @param p
	 * @return
	 */
	public static GUI getGUI(HumanEntity p) {
		if(hasOpenInventory(p)) return guis.get(p);
		else return null;
	}
	
	/**
	 * Gibt an, ob ein Spieler ein offenes GUI hat
	 * @param p
	 * @return
	 */
	public static boolean hasOpenInventory(Player p) {
		return guis.containsKey(p);
	}
	/**
	 * Gibt an, ob ein Spieler ein offenes GUI hat
	 * @param p
	 * @return
	 */
	public static boolean hasOpenInventory(HumanEntity p) {
		return guis.containsKey(p);
	}
	
	/**
	 * Schlieﬂt alle GUIs unsicher.
	 * Wird beim Abschalten des Plugins verwendet
	 * @return
	 */
	public static boolean closeAll() {
		if(guis.isEmpty() == false) {			
			for(HumanEntity p : guis.keySet()) {
				guis.get(p).forceClose();
			}
			return true;
		}else return true;
	}
	
	/**
	 * Schlieﬂt ein GUI Sicher
	 * @param p
	 * @return
	 */
	public static boolean closeGUI(HumanEntity p) {
		return closeGUI(p, false);
	}
	
	/**
	 * Schlieﬂt ein GUI unsicher
	 * @param p
	 * @param forcfully
	 * @return
	 */
	public static boolean closeGUI(HumanEntity p, boolean forcfully) {
		if(guis.containsKey(p) && guis.get(p).getGUIType().hasToStayFucused()) {
			return guis.get(p).saveAndCloseGUI();
		}else if(guis.containsKey(p) && guis.get(p).getGUIType().hasToStayFucused() == false) {
			guis.get(p).closeGui();
			guis.remove(p);
			return true;
		}else if(guis.containsKey(p) == false) return false;
		else return false;
		
	}
	
	/**
	 * Entfernt das GUI aus der HashMap
	 * @param p
	 * @return
	 */
	public static boolean unregisterGUI(HumanEntity p) {
		if(guis.containsKey(p)) {
			guis.get(p).stop();
			guis.remove(p);
			return true;
		}else return false;
		
	}
	
	/**
	 * ÷ffnet ein GUI
	 * @param p
	 * @param gui
	 * @return
	 */
	public static boolean openGUI(HumanEntity p, GUI gui) {
		if(guis.containsKey(p) && guis.get(p).getGUIType().hasToStayFucused()) {
			return guis.get(p).saveAndCloseGUI();
		}else if(guis.containsKey(p)){
			guis.get(p).closeGui();
			guis.remove(p);
		}
		
		guis.put(p, gui);
		gui.load();
		return true;
	}
	
	public static abstract class GUI {
		
		protected Player player = null;
		protected Inventory openedInv = null;
		protected GUIType type = null;
		protected boolean canBeClosedNow = true, forceClose = false;
		protected String processedTitle = "";
		
		protected int maxFrames = 10;
		protected int frame = 0;
		protected boolean loop = true;
		protected BukkitRunnable animator = null;
		
		public GUI(Player player, GUIType type) {
			this.player = player;
			this.type = type;
			start();
		}
		
		public abstract void start();
		public abstract void stop();
		public abstract void reset();
		public abstract void clickedItem(ItemStack item);
		public void nextFrame() {
			frame++;
			if(frame == maxFrames && loop) {
			}else if(frame == maxFrames && loop == false) stop();
		}
		
		
		public String getProcessedTitle() {
			return processedTitle;
		}
		
		/**
		 * Schlieﬂt das GUI unsicher
		 * @return
		 */
		public boolean forceClose() {
			forceClose = true;
			return saveAndCloseGUI();
		}
		
		/**
		 * Schlieﬂt ein GUI
		 * @return
		 */
		public void closeGui() {
			stop();
			player.closeInventory();
		}
		
		/**
		 * L‰dt ein GUI und ˆffnet dieses einen Spieler
		 * @return
		 */
		public void load() {
			
			ItemStack item = new ItemStack(Material.AIR);
			ItemMeta meta = item.getItemMeta();
			ArrayList<String> lore = new ArrayList<String>();
			
			switch(type) {
			case BLANK_CHEST_BIG_GUI:
				openedInv = Bukkit.createInventory(player, type.getSize(), type.getTitle());
				processedTitle = type.getTitle();
				guis.put(player,  this);
				player.openInventory(openedInv);
				break;
			case BLANK_CHEST_NORMAL_GUI:
				openedInv = Bukkit.createInventory(player, type.getSize(), type.getTitle());
				processedTitle = type.getTitle();
				guis.put(player,  this);
				player.openInventory(openedInv);
				break;
			case BLANK_FURNACE_GUI:
				openedInv = Bukkit.createInventory(player, type.getType(), type.getTitle());
				processedTitle = type.getTitle();
				guis.put(player,  this);
				player.openInventory(openedInv);
				break;
			case BLANK_WORKBENCH_GUI:
				openedInv = Bukkit.createInventory(player, type.getType(), type.getTitle());
				processedTitle = type.getTitle();
				guis.put(player,  this);
				player.openInventory(openedInv);
				break;
			case REWARD_GUI:
				openedInv = Bukkit.createInventory(player, type.getSize(), type.getTitle());
				processedTitle = type.getTitle();
				
				item = new ItemStack(Material.CHEST);
				meta = item.getItemMeta();
				meta.setDisplayName(RewardGUI.reward_displayname);
				lore.add("ß7> ßaKlicke um deine Belohnung abzuholen");
				meta.setLore(lore);
				item.setItemMeta(meta);
				
				openedInv.setItem((openedInv.getSize()/2), item);
				
				guis.put(player,  this);
				player.openInventory(openedInv);
				break;
			case OWN_CREATION:
				guis.put(player,  this);
				player.openInventory(openedInv);
				break;
			default:
				break;
			}
		}
		
		/**
		 * Speichert das GUI und Schlieﬂt es anschlieﬂend
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
			BLANK_CHEST_BIG_GUI("ßbGUI_CHEST", 54, InventoryType.CHEST, false),
			BLANK_CHEST_NORMAL_GUI("ßbGUI_CHEST", 27, InventoryType.CHEST, false),
			BLANK_WORKBENCH_GUI("ßbGUI_WORKBENCH", 54, InventoryType.WORKBENCH, false),
			BLANK_FURNACE_GUI("ßbGUI_FURNACE", 54, InventoryType.FURNACE, false),
			REWARD_GUI("ßfT‰gliche Belohnung", 9, InventoryType.CHEST, false),
			JOIN_MELODY("ßbGUI_MELODY", 9, InventoryType.CHEST, false),
			OWN_CREATION("ßbEigene Kreation", 9, InventoryType.CHEST, false);
			
			private String title;
			private int size;
			private InventoryType type;
			private boolean stayFucused;
			
			GUIType(String title, int size, InventoryType type, boolean stayFucused) {
				this.title = title;
				this.size = size;
				this.type = type;
			}

			public String getTitle() {
				return title;
			}
			public int getSize() {
				return size;
			}
			public InventoryType getType() {
				return type;
			}
			/**
			 * Gibt an, ob ein GUI einfach normal geschlossen werden kann oder ob es gespeichert werden muss
			 * @return
			 */
			public boolean hasToStayFucused() {
				return stayFucused;
			}
			
			
		}
		
	}
	
}