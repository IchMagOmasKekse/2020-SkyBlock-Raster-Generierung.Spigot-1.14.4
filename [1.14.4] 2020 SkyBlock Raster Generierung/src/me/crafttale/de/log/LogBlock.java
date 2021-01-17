package me.crafttale.de.log;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.crafttale.de.Chat.MessageType;
import me.crafttale.de.SkyBlock;
import me.crafttale.de.log.XLogger.LogType;
import me.crafttale.de.profiles.PlayerProfiler;

public class LogBlock {
	 private static HashMap<String, UUID> changes = new HashMap<String, UUID>();
	 
	  public LogBlock() { }
	  
	  /**
	   * Registriert eine Blockveränderung mit Veränderer.
	   */
	  public static void log(Location loc, Player p) {
	    String logcode = getLogCode(loc);
	    if(changes.containsKey(logcode)) changes.remove(logcode);
	    changes.put(logcode, PlayerProfiler.getUUID(p));
	  }
	  
	  /**
	   * Konvertiert eine Location in ein LogCode.
	   * Der LogCode wird verwendet, um in der HashMap Daten mit weniger Performance belastung fest zu halten(Anstatt Location.class -> String.class).
	   * @return
	   */
	  public static String getLogCode(Location loc) {
	    /* LogCode Format: [W:world|X:0|Y:0|Z:0] */
	    return "[W:" + loc.getWorld().getName() + "|X:"
	    		+ loc.getX() + "|Y:" + loc.getY() + "|Z:" + loc.getZ() + "]";
	  }
	 
	  /**
	   * Gibt die UUID des Spielers zurück, der die Änderung an diesem Block vorgenommen hat.
	   * Gibt @null zurück, wenn bisher keine Änderungen vorgenommen wurden.
	   * @return
	   */
	  public static UUID getChanger(Location loc) {
	    String logcode = getLogCode(loc);
	    if(changes.containsKey(logcode)) {
	      return changes.get(logcode);
	    }else return null;
	  }
	  
	  /**
	   * Konvertiert die Changes HashMap in eine LinkedList<String>.
	   */
	  public static LinkedList<String> getChangesList() {
	    LinkedList<String> list = new LinkedList<String>();
	    for(String s : changes.keySet()) list.add(s + " -> " + changes.get(s).toString());
	    return list;
	  }
	 
	  /**
	   * Speichert alle Änderungen.
	   * @return
	   */
	  public static boolean saveChanges() {
	    File file = new File("plugins/" + SkyBlock.getSB().getDescription().getName() + "/LogBlock/changes.yml");
	    FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
	    
	    cfg.set("Changes: ", getChangesList());
	    try{
	      cfg.save(file);
	      XLogger.log(LogType.PluginInternProcess, "LogBlock Changes wurden gespeichert!");
	      return true;
	    } catch(Exception ex) {
	      SkyBlock.sendConsoleMessage(MessageType.ERROR, "LogBlock Changes konnten nicht gespeichert werden!");
	      XLogger.log(LogType.PluginInternProcess, "LogBlock Changes konnten nicht gespeichert werden!");
	      return false;
	    }
	  }
	  
	  
	  
	}
