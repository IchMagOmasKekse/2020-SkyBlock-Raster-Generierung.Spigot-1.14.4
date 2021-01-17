package me.crafttale.de.models;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import me.crafttale.de.SkyBlock;
import me.crafttale.de.gadgets.lobby.Spawn;
import me.crafttale.de.log.XLogger;
import me.crafttale.de.log.XLogger.LogType;

public class Model {

	private String modelName = "";
	private ItemStack itemDisplay;
	private boolean isSaved = false, isLoaded = false, isSummoned = false, isShowing = true, small = false, autoSummon = false, isInEditing = false;
	private ArmorStand modelArm = null;
	private EulerAngle angle = null;
	private Location location;
	private List<String> history = new LinkedList<String>();
	
	public Model(String modelName) {
		this.modelName = modelName;
		this.angle = new EulerAngle(0,0,0);
	}
	
	/**
	 * Lädt das Model und seine Eigenschaften, setzt diese und spawnt das Model.
	 * @param reason
	 * @param player
	 * @return
	 */
	public boolean loadModel(LoadReason reason, Player player) {
		if(isLoaded) return false;
		File file = new File("plugins/" + SkyBlock.getSB().getDescription().getName() + "/Models/saved models/" + this.modelName + ".model");
		if(file.exists() == false) {
			if(reason == LoadReason.PLAYER_SELECTION) {
				if(player != null) player.sendMessage("§2Das Model §f"+this.modelName+" §2existiert nicht.");
				XLogger.log(LogType.PluginInternProcess, "Model '"+this.modelName+"' wurde " + (player == null ? "" : "von " + player.getName() + " ") + "versucht zu laden, obwohl es nicht existiert.");
				return false;
			}else if(reason == LoadReason.PLUGIN_BOOT) {				
				XLogger.log(LogType.PluginInternProcess, "Model '"+this.modelName+"' wurde " + (player == null ? "" : "von " + player.getName() + " ") + "versucht zu laden, obwohl es nicht existiert.");
				return false;
			}else if(reason == LoadReason.MODEL_RELOAD) {				
				XLogger.log(LogType.PluginInternProcess, "Model '"+this.modelName+"' wurde versucht neu zu laden, obwohl es nicht existiert.");
				return false;
			}else if(reason == LoadReason.LOAD_BY_PLAYER) {				
				XLogger.log(LogType.PluginInternProcess, "Model '"+this.modelName+"' wurde " + (player == null ? "" : "von " + player.getName() + " ") + "versucht zu laden, obwohl es nicht existiert.");				return false;
			}
		}
		
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		this.small = cfg.getBoolean("Is small");
		this.autoSummon = cfg.getBoolean("Auto Summon");
		this.itemDisplay = new ItemStack(Material.valueOf(cfg.getString("Item Display")));
		this.location = new Location(Bukkit.getWorld(cfg.getString("Location.World")), cfg.getDouble("Location.X"), cfg.getDouble("Location.Y"), cfg.getDouble("Location.Z"));
		this.angle = new EulerAngle(cfg.getDouble("HeadPose.X"),cfg.getDouble("HeadPose.Y"), cfg.getDouble("HeadPose.Z"));		
		this.isLoaded = true;
		XLogger.log(LogType.PluginInternProcess, "Lade Model '" + this.modelName + "'");
		return true;
	}
	
	/**
	 * Spawnt das Model und wendet Grundeinstellungen sowie die geladenen Einstellungen an.
	 * @return
	 */
	public boolean summonModel(SummonReason reason) {
		if(isSummoned) return false;
		if(reason == SummonReason.SUMMON_BY_RELOAD && autoSummon == false) return false;
		if(isLoaded == false) {
			XLogger.log(LogType.PluginInternProcess, "Es wurde versucht das Model '" + this.modelName + "' zu beschwören, obwohl es nicht geladen wurde.");
			return false;
		}
		this.modelArm = (ArmorStand) this.location.getWorld().spawn(this.location, ArmorStand.class);
		this.modelArm.setGravity(false);
		this.modelArm.setSmall(this.small);
		this.modelArm.setVisible(false);
		this.modelArm.setHeadPose(angle);
		if(isShowing) if(this.itemDisplay != null) this.modelArm.getEquipment().setHelmet(itemDisplay);
//		this.modelArm.addEquipmentLock(EquipmentSlot.HEAD, LockType.REMOVING_OR_CHANGING);
//		this.modelArm.addEquipmentLock(EquipmentSlot.CHEST, LockType.REMOVING_OR_CHANGING);
//		this.modelArm.addEquipmentLock(EquipmentSlot.LEGS, LockType.REMOVING_OR_CHANGING);
//		this.modelArm.addEquipmentLock(EquipmentSlot.FEET, LockType.REMOVING_OR_CHANGING);
//		this.modelArm.addEquipmentLock(EquipmentSlot.OFF_HAND, LockType.REMOVING_OR_CHANGING);
//		this.modelArm.addEquipmentLock(EquipmentSlot.HAND, LockType.REMOVING_OR_CHANGING);
		this.modelArm.setCustomName(this.modelName);
//		this.modelArm.setCustomNameVisible(true);
		XLogger.log(LogType.PluginInternProcess, "Spawne Model '" + this.modelName + "'");
		isSummoned = true;
		return true;
	}
	
	/**
	 * Speichert alle Änderungen.
	 * @return
	 */
	public boolean saveChanges() {
		File file = new File("plugins/" + SkyBlock.getSB().getDescription().getName() + "/Models/saved models/" + this.modelName + ".model");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		XLogger.log(LogType.PluginInternProcess, "Speichere Änderungen an Model '" + this.modelName + "'.");
		
		cfg.set("Is small", this.small);
		cfg.set("Auto Summon", this.autoSummon);
		cfg.set("Item Display", this.itemDisplay.getType().toString());
		cfg.set("Location.World", this.location.getWorld().getName());
		cfg.set("Location.X", this.location.getX());
		cfg.set("Location.Y", this.location.getY());
		cfg.set("Location.Z", this.location.getZ());
		cfg.set("HeadPose.X", this.angle.getX());
		cfg.set("HeadPose.Y", this.angle.getY());
		cfg.set("HeadPose.Z", this.angle.getZ());
		
		try {
			cfg.save(file);
			this.isSaved = true;
			XLogger.log(LogType.PluginInternProcess, "Änderungen an Model '" + this.modelName + "' wurden gespeichert.");
			return true;
		} catch (IOException e) {
			this.isSaved = false;
			XLogger.log(LogType.PluginInternProcess, "Änderungen an Model '" + this.modelName + "' konnten nicht gespeichert werden.");
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Ändert die HeadPose.
	 * @param x
	 * @param y
	 * @param z
	 */
	public void addAngle(double x, double y, double z) {
		this.angle = angle.add(x, y, z);
		this.isSaved = false;
		this.history.add("EulerAngle:ADD:" + x + "/" + y + "/" + z);
		this.modelArm.setHeadPose(new EulerAngle(angle.getX(), angle.getY(), angle.getZ()));
	}
	/**
	 * Ändert die HeadPose.
	 * @param x
	 * @param y
	 * @param z
	 */
	public void subtractAngle(double x, double y, double z) {
		this.angle = angle.subtract(x, y, z);
		this.isSaved = false;
		this.history.add("EulerAngle:SUBTRACT:" + x + "/" + y + "/" + z);
		this.modelArm.setHeadPose(new EulerAngle(angle.getX(), angle.getY(), angle.getZ()));
	}
	
	/**
	 * Ändert die Location.
	 * @param x
	 * @param y
	 * @param z
	 */
	public void addLocation(double x, double y, double z) {
		this.location.add(x, y, z);
		this.isSaved = false;
		this.history.add("Location:ADD:" + x + "/" + y + "/" + z);
		this.modelArm.teleport(location);
	}
	
	/**
	 * Ändert die Location.
	 * @param x
	 * @param y
	 * @param z
	 */
	public void subtractLocation(double x, double y, double z) {
		this.location.subtract(x, y, z);
		this.isSaved = false;
		this.history.add("Location:SUBTRACT:" + x + "/" + y + "/" + z);
		this.modelArm.teleport(location);
	}
	
	/**
	 * Ändert die Einstellung: Auto-Summon für dieses Model.
	 * @param autoSummon
	 * @return
	 */
	public void setAutoSummon(boolean autoSummon) {
		this.autoSummon = autoSummon;
	}
	
	/**
	 * Ändert das DisplayItem.
	 * @param item
	 */
	public void setDisplayItem(ItemStack item) {
		this.itemDisplay = item;
		this.modelArm.getEquipment().setHelmet(item);
	}
	
	/**
	 * Setzt einen neuen Small Wert.
	 * @param small
	 * @return
	 */
	public void setSmall(boolean small) {
		this.small = small;
		this.modelArm.setSmall(small);
	}
	
	/**
	 * Gibt die aktuelle Location zurück.
	 * @return
	 */
	public Location getLocation() {
		return (this.location == null ?  new Location(Bukkit.getWorld(Spawn.spawn_world_name), 0, 0, 0) : this.location.clone());
	}
	
	/**
	 * Gibt die aktuelle EulerAngle zurück.
	 * @return
	 */
	public EulerAngle getEulerAngle() {
		return this.angle;
	}
	
	/**
	 * Gibt AutoSummon zurück.
	 * @return
	 */
	public boolean getAutoSummon() {
		return autoSummon;
	}
	
	
	/**
	 * Gibt an, ob das Model small ist.
	 * @return
	 */
	public boolean getSmall() {
		return small;
	}
	
	/**
	 * Gibt an, ob dieses Model gerade bearbeitet wird.
	 * @return
	 */
	public boolean isInEditing() {
		return isInEditing;
	}
	
	/**
	 * Setzt die 'In Bearbeitung'-Status auf false
	 */
	public void removeEditorState() {
		this.isInEditing = false;
	}
	
	/**
	 * Setzt die 'In Bearbeitung'-Status auf true
	 */
	public void setEditorState() {
		this.isInEditing = true;
	}
	
	/**
	 * Updatet das Model
	 */
	public void update() {}
	
	/**
	 * Macht eine Änderung rückgängig.
	 * @return
	 */
	public boolean undo() {
		//TODO:
		return false;
	}
	
	/**
	 * Macht das letzte Rückgängigmachen wieder wirksam.
	 * @return
	 */
	public boolean redo() {
		//TODO:
		return false;
	}
	
	/**
	 * Leer den Änderungs-Verlauf.
	 * @return
	 */
	public boolean clearHistory() {
		history.clear();
		return true;
	}
	/**
	 * Fährt diese Model Instanz herunter.
	 * @return
	 */
	public boolean close() {
		saveChanges();
		if(modelArm != null) modelArm.remove();
		return true;
	}
	/**
	 * Gibt den Namen des Models zurück.
	 * @return
	 */
	public String getModelName() {
		return this.modelName;
	}
	/**
	 * Gibt an, ob das Model bereits alle Änderungen gespeichert hat
	 * @return
	 */
	public boolean isSaved() {
		return isSaved;
	}
	
	/**
	 * Lädt das Model neu und resummoned es.
	 */
	public void reSummon() {
		if(modelArm != null) modelArm.remove();
		isSummoned = false;
		loadModel(LoadReason.MODEL_RELOAD, null);
		summonModel(SummonReason.RESUMMON);
	}
	
	/**
	 * Zeigt das DisplayItem
	 * @return
	 */
	public void show() {
		this.isShowing = true;
	}
	
	/**
	 * Versteckt das DisplayItem
	 */
	public void hide() {
		this.isShowing = false;
	}
	
	/**
	 * Gibt den ArmorStand zurück.
	 * @return
	 */
	public ArmorStand getArmorStand() {
		return this.modelArm;
	}
	
}
