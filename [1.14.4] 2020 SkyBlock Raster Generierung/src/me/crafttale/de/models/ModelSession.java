package me.crafttale.de.models;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ModelSession {
	
	public final Player host;
	public String modelName = "";
	public Inventory inv = null;
	
	public ModelSession(Player host, String modelName) {
		this.host = host;
		this.modelName = modelName;
	}
	public ModelSession(Player host) {
		this.host = host;
	}
	
	public boolean close() {
		if(getModel() == null) return true;
		if(getModel().close()) {
			modelName = "";
			host.getInventory().clear();
			host.getInventory().setContents(inv.getContents());
			return true;
		}else return false;
	}
	
	/**
	 * Gibt an, ob der Spieler ein Model zum Bearbeiten ausgewählt hat.
	 * @return
	 */	
	public boolean hasSelectedModel() {
		return (modelName.equals("") ? false : true);
	}
	
	/**
	 * Setzt einen neuen Small Wert.
	 * @param small
	 */
	public void setSmall(boolean small) {
		getModel().setSmall(small);
	}
	
	/**
	 * Setzt das Inventar, welches der Spieler vor der Session hatte.
	 * @param inv
	 */
	public void setPlayerPreviousInventory(Inventory inv) {
		this.inv = inv;
	}
	
	/**
	 * Gibt das ausgewählte Model zurück.
	 * @return
	 */
	public Model getModel() {
		return ModelManager.getModel(modelName);
	}
	
	/**
	 * Gibt an, ob das Model small ist.
	 * @return
	 */
	public boolean getSmall() {
		return getModel().getSmall();
	}
	
	/**
	 * Wählt ein Model aus.
	 * @param model
	 * @return
	 */
	public boolean selectModel(String modelName) {
		Model modelSelected = ModelManager.getModel(this.modelName);
		if(modelSelected != null && modelSelected.isSaved() == false) {
			modelSelected.saveChanges();
			modelSelected.close();			
		}else if(modelSelected != null) modelSelected.removeEditorState();
		if(ModelManager.getModel(modelName) != null && ModelManager.getModel(modelName).isInEditing() == false) {
			this.modelName = modelName;
			ModelManager.getModel(modelName).setEditorState();
			return true;
		}else return false;
	}
	
	/**
	 * Entfernt die Selektierung.
	 */
	public void unselectModel() {
		if(hasSelectedModel()) ModelManager.getModel(this.modelName).removeEditorState();
		this.modelName = "";
	}
	
	/**
	 * Ändert die Einstellung: Auto-Summon für dieses Model.
	 * @param autoSummon
	 * @return
	 */
	public void setAutoSummon(boolean autoSummon) {
		getModel().setAutoSummon(autoSummon);
	}
	
	/**
	 * Ändert das DisplayItem.
	 * @param item
	 */
	public void setItemDisplay(ItemStack item) {
		getModel().setDisplayItem(item);
	}
	
}
