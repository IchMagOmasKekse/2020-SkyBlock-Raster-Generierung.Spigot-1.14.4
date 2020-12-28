package me.crafttale.de.gui;

import java.util.HashMap;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class GUIManager  {
	static boolean doError = false;
	/**
	 * Diese Klasse steuert GUIs(Inventare) und handled diese
	 */
	
	public static HashMap<HumanEntity, GUI> guis = new HashMap<HumanEntity, GUI>();
	
		
	/**
	 * Gibt das offene GUI eines Spielers zurück
	 * @param p
	 * @return
	 */
	public static GUI getGUI(Player p) {
		if(hasOpenInventory(p)) return guis.get(p);
		else return null;
	}
	/**
	 * Gibt das offene GUI eines Spielers zurück
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
	 * Schließt alle GUIs unsicher.
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
	 * Schließt ein GUI Sicher
	 * @param p
	 * @return
	 */
	public static boolean closeGUI(HumanEntity p) {
		return closeGUI(p, false);
	}
	
	/**
	 * Schließt ein GUI unsicher
	 * @param p
	 * @param forcfully
	 * @return
	 */
	public static boolean closeGUI(HumanEntity p, boolean forcfully) {
		
		doError = true;
		if(guis.containsKey(p) && guis.get(p).getGUIType().hasToStayFucused()) {
			return guis.get(p).saveAndCloseGUI();
		}else if(guis.containsKey(p) && guis.get(p).getGUIType().hasToStayFucused() == false) {
			guis.get(p).closeGui();
			guis.remove(p);
			return true;
		}else if(guis.containsKey(p) == false) {
			return false;
		} else return false;
		
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
	 * Öffnet ein GUI
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
		
//		guis.put(p, gui);
		gui.load();
		return true;
	}
	
}