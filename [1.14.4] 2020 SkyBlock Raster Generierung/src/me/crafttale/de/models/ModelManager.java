package me.crafttale.de.models;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.crafttale.de.Chat;
import me.crafttale.de.SkyBlock;
import me.crafttale.de.gadgets.lobby.Spawn;
import me.crafttale.de.log.XLogger;
import me.crafttale.de.log.XLogger.LogType;
import me.crafttale.de.particle.ParticleManager;

public class ModelManager {
	
	private static ConcurrentHashMap<String, Model> models = new ConcurrentHashMap<String, Model>();
	public static HashMap<Player, ModelSession> editMode = new HashMap<Player, ModelSession>();
	private static double offset = 0.1; //Offset, wie weit das Model sich bewgen soll, wenn man es barbeitet
	static double fineOffset = 0.01; //Fein einstellung des Offsets
	
	public ModelManager() {
		createModelFile("example");
		loadModels();
	}
	/**
	 * Gibt die HashMap mit allen registrierten Models zurück.
	 * @return
	 */
	public static ConcurrentHashMap<String, Model> getModels() {
		return models;
	}
	
	/**
	 * Lädt alle gespeicherten Models und spawnt diese.
	 * @return
	 */
	public static void loadModels() {
		File f = new File("plugins/" + SkyBlock.getSB().getDescription().getName() + "/Models/saved models/");
		File[] files = f.listFiles();
		if(files.length != 0) {
			for(File file : files) {
				if(registerModel(new Model(file.getName().replace(".model", "")), LoadReason.PLUGIN_BOOT, SummonReason.SUMMON_BY_RELOAD) == false) XLogger.log(LogType.PluginInternProcess, "Model '" + file.getName() + "' konnte nicht geladen werden.");
				else XLogger.log(LogType.PluginInternProcess, "Model '" + file.getName() + "' wurde geladen.");
			}
		}
		return;
	}
	
	/**
	 * Lädt ein spezielles Model.
	 * @param modelName
	 * @return
	 */
	public static boolean loadModel(String modelName, LoadReason loadReason, SummonReason summonReason) {
		File file = new File("plugins/" + SkyBlock.getSB().getDescription().getName() + "/Models/saved models/" + modelName + ".model");
		if(file.exists()) {
			if(registerModel(new Model(modelName), loadReason, summonReason) == false) XLogger.log(LogType.PluginInternProcess, "Model '" + file.getName() + "' konnte nicht geladen werden.");
			else {
				XLogger.log(LogType.PluginInternProcess, "Model '" + file.getName() + "' wurde geladen.");
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Erstelle eine neue Model-Datei.
	 * @param modelName
	 * @return
	 */
	public static boolean createModelFile(String modelName) {
		return createModelFile(modelName, null, false);
	}
	
	/**
	 * Erstelle eine neue Model-Datei.
	 * @param modelName
	 * @return
	 */
	public static boolean createModelFile(String modelName, Location location) {
		return createModelFile(modelName, location, false);
	}
	
	/**
	 * Erstelle eine neue Model-Datei und überschreibt, falls vorhanden und erwünscht.
	 * @param modelName
	 * @param overwrite
	 * @return
	 */
	public static boolean createModelFile(String modelName, Location location, boolean overwrite) {
		File file = new File("plugins/" + SkyBlock.getSB().getDescription().getName() + "/Models/saved models/" + modelName + ".model");
		if(overwrite == false && file.exists()) return false;
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		cfg.set("Is small", false);
		cfg.set("Auto Summon", true);
		cfg.set("Item Display", Material.DIAMOND.toString());
		cfg.set("Location.World", "world");
		if(location != null) {
			cfg.set("Location.X", location.getX());
			cfg.set("Location.Y", location.getY());
			cfg.set("Location.Z", location.getZ());
		} else {
			cfg.set("Location.X", 0);
			cfg.set("Location.Y", 0);
			cfg.set("Location.Z", 0);
		}
		cfg.set("HeadPose.X", 0);
		cfg.set("HeadPose.Y", 0);
		cfg.set("HeadPose.Z", 0);
	
		try {
			cfg.save(file);
			XLogger.log(LogType.PluginInternProcess, "Model " + modelName + " wurde gespeichert.");
			return true;
		} catch (IOException e) {
			XLogger.log(LogType.PluginInternProcess, "Model " + modelName + " konnte nicht gespeichert werden.");
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Benennt ein Model um.
	 * @param modelName
	 * @param newModelName
	 * @return
	 */
	public static boolean renameModel(String modelName, String newModelName) {
		Player host = null;
		for(Player p : editMode.keySet()) {
			if(getModelSession(p).getModel() != null && getModelSession(p).getModel().getModelName().equals(modelName)) {
				getModelSession(p).close();
				host = p;
				break;
			}
		}
		unregisterModel(modelName);
		File file = new File("plugins/" + SkyBlock.getSB().getDescription().getName() + "/Models/saved models/" + modelName + ".model");		
		if(file.renameTo(new File("plugins/" + SkyBlock.getSB().getDescription().getName() + "/Models/saved models/" + newModelName + ".model"))) {
			reloadModels();
			createNewSession(host, getModel(newModelName));
			return true;
		} else return false;
	}
	
	/**
	 * Schließt alle Models.
	 */
	public static void closeAllModels() {
		if(editMode.isEmpty() == false) {			
			for(Player p : editMode.keySet()) {
				p.sendMessage("§cDeine ModelSession musst aufgrund eines Off-Befehls eines Admins zwangsgeschlossen werden.");
				if(editMode.get(p).hasSelectedModel()) {					
					p.sendMessage("§7§oDeine Änderungen wurden gespeichert.");
					unregisterModel(editMode.get(p).getModel().getModelName());
				}
			}
		}
		if(models.isEmpty()) return;
		for(Model m : models.values()) {
			m.close();
			unregisterModel(m.getModelName());
		}
	}
	
	/**
	 * Lädt die Einstellungen aller Models neu und registriert neue Models, wenn sie noch nich registriert sind.
	 */
	public static int reloadModels() {
		File f = new File("plugins/" + SkyBlock.getSB().getDescription().getName() + "/Models/saved models/");
		File[] files = f.listFiles();
		if(files != null && files.length != 0) {
			for(File file : files) {
				if(models.containsKey(file.getName().replace(".model", "")) == false) {
					if(registerModel(new Model(file.getName().replace(".model", "")), LoadReason.MODEL_RELOAD, SummonReason.SUMMON_BY_RELOAD) == false) XLogger.log(LogType.PluginInternProcess, "Model '" + file.getName().replace(".model", "") + "' konnte nicht geladen werden.");
					else XLogger.log(LogType.PluginInternProcess, "Model '" + file.getName().replace(".model", "") + "' wurde geladen.");
				}else {
					models.get(file.getName().replace(".model", "")).reSummon();
				}
			}
		}
		return models.size();
	}
	
	/**
	 * Reigstriert ein Model.
	 * @param model
	 * @return
	 */
	public static boolean registerModel(Model model, LoadReason loadReason, SummonReason summonReason) {
		if(models.isEmpty() == false && models.containsKey(model.getModelName())) return false;
		else {
			model.loadModel(loadReason, null);
			if(model.summonModel(summonReason)) {
				models.put(model.getModelName(), model);
			}else XLogger.log(LogType.PluginInternProcess, "Es wurde versucht das Model '" + model.getModelName() + "' zu laden, obwohl Auto Summon deaktiviert ist.");
		}
		return true;
	}
	
	/**
	 * Deaktiviert ein Model.
	 * @param modelName
	 * @return
	 */
	public static boolean unregisterModel(String modelName) {
		if(models.isEmpty() == false && models.containsKey(modelName) == false) return false;
		else {
			models.get(modelName).close();
			models.remove(modelName);
		}
		return true;
	}
	
	/**
	 * Gibt ein geladenes Model zurück.
	 * @param modelName
	 * @return
	 */
	public static Model getModel(String modelName) {
		if(models.containsKey(modelName)) return models.get(modelName);
		else return null;
	}
	
	/**
	 * Erstellt eine ModelSession, wenn keine vorhanden ist.
	 * @param host
	 * @return
	 */
	public static boolean createNewSession(Player host) {
		if(editMode.containsKey(host)) return false;
		ModelSession ms = new ModelSession(host);
		editMode.put(host, ms);
		ms.setPlayerPreviousInventory(host.getInventory());
		giveEditorInventory(host);
		return true;
	}
	/**
	 * Erstellt eine ModelSession, wenn keine vorhanden ist.
	 * @param host
	 * @return
	 */
	public static boolean createNewSession(Player host, Model model) {
		if(editMode.containsKey(host)) return false;
//		host.performCommand("/model edit");
		ModelSession ms = new ModelSession(host, model.getModelName());
		editMode.put(host, ms);
		return true;
	}
	
	/**
	 * Speichert und schließt eine Session.
	 * @param host
	 * @return
	 */
	public static boolean closeSession(Player host) {
		if(hasSession(host) == false) return false;
		
		ModelSession ms = editMode.get(host);
		if(ms.hasSelectedModel()) {
			String modelName = ms.getModel().getModelName();
			if(ms.getModel().saveChanges()) {
				host.sendMessage("§aDeine Änderungen am Model '§f" + modelName + "§a' wurden gespeichert.");
				ms.getModel().removeEditorState();
				editMode.remove(host);
			}else host.sendMessage("§cDeine Änderungen am Model '§f" + modelName + "§a' konnten nicht gespeichert werden.");
		}else {
			editMode.remove(host);
			host.getInventory().clear();
			host.sendMessage("§aDeine ModelSession wurde geschlossen");
		}
		return true;
	}
	
	/**
	 * Beendet eine Session, ohne zu speichern, wenn erwünscht.
	 * @param host
	 * @param save
	 * @return
	 */
	public static boolean closeSession(Player host, boolean save) {
		if(hasSession(host) == false) return false;
		
		if(save) {
			closeSession(host);
		}else {
			if(getModelSession(host).hasSelectedModel()) getModelSession(host).getModel().removeEditorState();
			editMode.remove(host);
		}
		return true;
	}
	
	/**
	 * Gibt die aktive ModelSession eines Spielers zurück.
	 * @param host
	 * @return
	 */
	public static ModelSession getModelSession(Player host) {
		if(editMode.containsKey(host) == false) return null;
		else return editMode.get(host);
	}
	
	/**
	 * Gibt an, ob der Spieler eine Session am laufen hat.
	 * @param host
	 * @return
	 */
	public static boolean hasSession(Player host) {
		return editMode.containsKey(host);
	}
	
	/**
	 * Gibt an, ob ein spezielles Model existiert.
	 * @param modelName
	 * @return
	 */
	public static boolean modelExists(String modelName) {
		return new File("plugins/" + SkyBlock.getSB().getDescription().getName() +  "/Models/saved models/" + modelName + ".model").exists();
	}
	
	public static void update() {
		spawnParticles();
		
		for(Model m : models.values()) m.update();
	}
	
	private static int particleDelay = 0;
	/**
	 * Zeigt den Kopf des Armorstand mittels Particle für den Spieler, der dieses Model bearbeitet.
	 * @return
	 */
	public static void spawnParticles() {
		if(particleDelay == 2) {			
			for(Player p : editMode.keySet()) {
				for(Model m : models.values()) {
					if(hasSession(p) && getModelSession(p).hasSelectedModel() && getModelSession(p).getModel().getModelName().equals(m.getModelName())) {
						ParticleManager.sendParticle(p, Particle.REDSTONE, m.getArmorStand().getEyeLocation(), 120, 255, 120);						
					}else if(m.isInEditing()) ParticleManager.sendParticle(p, Particle.REDSTONE, m.getArmorStand().getEyeLocation(), 245, 215, 66);
					else ParticleManager.sendParticle(p, Particle.REDSTONE, m.getArmorStand().getEyeLocation());
				}
			}
			particleDelay = 0;
		}else particleDelay++;
	}
	
	/**
	 * Löscht ein Model.
	 * @param modelName
	 * @return
	 */
	public static boolean deleteModel(String modelName) {
		File file = new File("plugins/" + SkyBlock.getSB().getDescription().getName() +  "/Models/saved models/" + modelName + ".model");
		for(Player p : editMode.keySet()) {
			if(getModelSession(p).hasSelectedModel() && getModelSession(p).getModel().getModelName().equals(modelName)) {
				editMode.get(p).close();
			}
		}
		if(models.containsKey(modelName)) unregisterModel(modelName);
		if(file.delete()) {
			XLogger.log(LogType.PluginInternProcess, "Model " + modelName + " wurde gelöscht.");
			return true;
		}else {
			XLogger.log(LogType.PluginInternProcess, "Model " + modelName + " konnte nicht gelöscht werden.");
			return false;
		}
	}
	
	
	/**
	 * Gibt eine LinkedList<String> mit den Namen aller existierenden Models zurück.
	 * @return
	 */
	public static LinkedList<String> getExistingModels() {
		File f = new File("plugins/" + SkyBlock.getSB().getDescription().getName() +  "/Models/saved models/");
		File[] files = f.listFiles();
		LinkedList<String> names = new LinkedList<String>();
		for(File file : files) {
			if(file.isFile() && file.getName().endsWith(".model")) {
				names.add(file.getName().replace(".model", ""));
			}
		}
		return names;
	}
	
	/**
	 * Gibt dem Spieler die notwendigen Werkzeuge, um das Model zu bearbeiten.
	 * @param host
	 */
	private static void giveEditorInventory(Player host) {
		ModelTools.giveEditorInventory(host);
	}
	
	/**
	 * Überprüft, ob der Spieler ein Model bearbeiten möchte.
	 * @param e
	 */
	public static void onManipulate(PlayerArmorStandManipulateEvent e) {
		if(e.getPlayer().hasPermission("skyblock.model") == false && e.getRightClicked().getWorld().getName().equals(Spawn.spawn_world_name)) {
			e.setCancelled(true);
			return;
		}
		if(e.getRightClicked().getCustomName() != null &&
				e.getRightClicked().getCustomName().equals("") == false &&
				models.containsKey(e.getRightClicked().getCustomName())) e.setCancelled(true);
		if(hasSession(e.getPlayer()) == false) return;
		ModelSession ms = getModelSession(e.getPlayer());
		e.getPlayer().sendMessage("§aDein aktuelles Model, welches Du bearbeitet hast, wurde gespeichert.");
		e.setCancelled(true);
		if(e.getRightClicked().getCustomName() != null && e.getRightClicked().getCustomName().equals("") == false) {			
			for(Model m : models.values()) {
				if(m.getArmorStand().getCustomName().equals(e.getRightClicked().getCustomName())) {
					if(ms.hasSelectedModel() && ms.getModel().getModelName().equals(m.getModelName())) return;
					if(ms.selectModel(m.getModelName())) {
						e.getPlayer().sendMessage("§aDu bearbeitest nun das Model §f" + m.getModelName());
					}else e.getPlayer().sendMessage("§cBeim auswählen des neuen Models ist etwas schief gelaufen.");
					return;
				}
			}
			e.getPlayer().sendMessage("§cDieser ArmorStand ist kein registriertes Model.");
		}
	}
	
	public static void onInteract(PlayerInteractEvent e) {
		if(checkInput(e.getPlayer(), e.getItem(), e.getAction())) e.setCancelled(true);
	}
	
	public static void onInteractEntity(PlayerInteractAtEntityEvent e) {
		if(e.getRightClicked() instanceof ArmorStand) {
			if((e.getPlayer().getInventory().getItemInMainHand() == null ||
					e.getPlayer().getInventory().getItemInMainHand().getType() == Material.PRISMARINE_SHARD)) {
				e.setCancelled(true);
//				e.getPlayer().sendMessage("§aName: §7"+e.getRightClicked().getCustomName());
				Chat.sendClickableMessage(e.getPlayer(), "§aName: §7"+e.getRightClicked().getCustomName(), "§fKlick, um das Model zu selektieren.", "/model select "+e.getRightClicked().getCustomName(), false, false);
			}			
		}
	}
	
	/**
	 * Check den Input des Spielers.
	 * Je nachdem wird dann die erwünschte Funktion ausgeführt.
	 * @param player
	 * @param item
	 * @param action
	 */
	public static boolean checkInput(Player player, ItemStack item, Action action) {
		if(editMode.containsKey(player) == false) return false;
		if(item == null) return false;
		offset = 0.1;
		if(player.isSneaking()) offset = fineOffset;
		Model model = getModelSession(player).getModel();
		if(model == null) {
			player.sendMessage("§cDu musst erst ein Model selektieren.");
			return true;
		}
		if(action.toString().contains("LEFT")) {
			switch(item.getType()) {
			case DIAMOND:
				model.reSummon();
				player.sendTitle("", "Armorstand spawned", 0, 20, 0);
				return true;
			case DIAMOND_PICKAXE:
				model.addLocation(0, 0, offset);
				player.sendTitle("", "Location Z + "+offset, 0, 20, 0);
				return true;
			case DIAMOND_AXE:
				model.addLocation(offset, 0, 0);
				player.sendTitle("", "Location X + "+offset, 0, 20, 0);
				return true;
			case DIAMOND_SHOVEL:
				model.addLocation(0, offset, 0);
				player.sendTitle("", "Location Y + "+offset, 0, 20, 0);
				return true;
			case IRON_PICKAXE:
				model.addAngle(0, 0, offset);
				player.sendTitle("", "Headpose Z + "+offset, 0, 20, 0);
				return true;
			case IRON_AXE:
				model.addAngle(offset, 0, 0);
				player.sendTitle("", "Headpose X + "+offset, 0, 20, 0);
				return true;
			case IRON_SHOVEL:
				model.addAngle(0, offset, 0);
				player.sendTitle("", "Headpose Y + "+offset, 0, 20, 0);
				return true;
			case BARRIER:
				ModelManager.deleteModel(model.getModelName());
				player.sendTitle("", "§cModel gelöscht", 0, 20, 0);
				return true;
			default:
				
				return true;
			}
		}else if(action.toString().contains("RIGHT")) {
			switch(item.getType()) {
			case DIAMOND_PICKAXE:
				model.subtractLocation(0, 0, offset);
				player.sendTitle("", "Location Z - "+offset, 0, 20, 0);
				return true;
			case DIAMOND_AXE:
				model.subtractLocation(offset, 0, 0);
				player.sendTitle("", "Location X - "+offset, 0, 20, 0);
				return true;
			case DIAMOND_SHOVEL:
				model.subtractLocation(0, offset, 0);
				player.sendTitle("", "Location Y - "+offset, 0, 20, 0);
				return true;
			case IRON_PICKAXE:
				model.subtractAngle(0, 0, offset);
				player.sendTitle("", "Headpose Z - "+offset, 0, 20, 0);
				return true;
			case IRON_AXE:
				model.subtractAngle(offset, 0, 0);
				player.sendTitle("", "Headpose X - "+offset, 0, 20, 0);
				return true;
			case IRON_SHOVEL:
				model.subtractAngle(0, offset, 0);
				player.sendTitle("", "Headpose Y - "+offset, 0, 20, 0);
				return true;
			case BARRIER:
				ModelManager.deleteModel(model.getModelName());
				player.sendTitle("", "§cModel gelöscht", 0, 20, 0);
				return true;
			default:
				
				return true;
			}
		}else return false;
	}
	
	public static void onBreak(BlockBreakEvent e) {
		if(editMode.containsKey(e.getPlayer())) {
			e.setCancelled(true);
			checkInput(e.getPlayer(), e.getPlayer().getInventory().getItemInMainHand(), Action.LEFT_CLICK_BLOCK);
		}
	}
	
	public static void onPlace(BlockPlaceEvent e) {
		if(editMode.containsKey(e.getPlayer())) {
			e.setCancelled(true);
			checkInput(e.getPlayer(), e.getPlayer().getInventory().getItemInMainHand(), Action.RIGHT_CLICK_BLOCK);
		}
	}
	
	public static void onDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player && e.getEntity() instanceof ArmorStand) {
			if(e.getEntity().getCustomName() != null && e.getEntity().getCustomName().equals("") == false) {				
				if((((Player)e.getDamager()).getInventory().getItemInMainHand() == null ||
						((Player)e.getDamager()).getInventory().getItemInMainHand().getType() == Material.PRISMARINE_SHARD && ((Player)e.getDamager()).isSneaking())) e.setCancelled(false);
				else {
					e.setDamage(0d);
					e.setCancelled(true);
				}
				if(editMode.containsKey(((Player)e.getDamager()))) {
					e.setCancelled(true);
					checkInput((Player)e.getDamager(), ((Player)e.getDamager()).getInventory().getItemInMainHand(), Action.LEFT_CLICK_AIR);
				}
			}
		}
	}
	
	public static void onInvClick(InventoryClickEvent e) {
//		e.getWhoClicked().sendMessage(e.getView().getTitle() + " slot: "+e.getRawSlot());
		if(hasSession((Player)e.getWhoClicked())) {
			if(e.getClickedInventory() == ((Player)e.getWhoClicked()).getInventory()) {
				if(e.getRawSlot() >= 0 && e.getRawSlot() <= 8) e.setCancelled(true);
				else if(e.getCurrentItem() != null) {
					switch(e.getCurrentItem().getType()) {
					case BARRIER:
						e.setCancelled(true);
						e.getWhoClicked().sendMessage("Löschen");
						e.setCurrentItem(new ItemStack(Material.AIR));
						break;
					case BOOK:
						e.setCancelled(true);
						e.getWhoClicked().sendMessage("Neu laden");
						e.setCurrentItem(new ItemStack(Material.AIR));
						break;
					case WRITABLE_BOOK:						
						e.setCancelled(true);
						e.getWhoClicked().sendMessage("Speichern");
						e.setCurrentItem(new ItemStack(Material.AIR));
						break;
					case PAPER:
						e.setCancelled(true);
						e.getWhoClicked().sendMessage("Information");
						e.setCurrentItem(new ItemStack(Material.AIR));
						break;
					case GUNPOWDER:
						e.setCancelled(true);
						e.getWhoClicked().sendMessage("Sichtbar machen");
						e.setCurrentItem(new ItemStack(Material.AIR));
						break;
					case EGG:
						e.setCancelled(true);
						e.getWhoClicked().sendMessage("Adult/Small");
						e.setCurrentItem(new ItemStack(Material.AIR));
						break;
					case GOLD_INGOT:
						e.setCancelled(true);
						e.getWhoClicked().sendMessage("Auto Summon");
						e.setCurrentItem(new ItemStack(Material.AIR));
						break;
						default:
							//Item DIsplay abfragen
							e.setCancelled(true);
							e.getWhoClicked().sendMessage("Item Display ?");
							break;
					}
				}else e.setCancelled(true);
				e.getWhoClicked().sendMessage("Nö aber ja, aber nö");
			}else e.setCancelled(false);
		}else e.setCancelled(false);
	}
}
