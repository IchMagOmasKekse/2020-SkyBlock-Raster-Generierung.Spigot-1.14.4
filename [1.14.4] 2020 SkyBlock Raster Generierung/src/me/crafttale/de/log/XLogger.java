package me.crafttale.de.log;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.crafttale.de.Chat.MessageType;
import me.crafttale.de.Settings;
import me.crafttale.de.SkyBlock;

public class XLogger {
	private static String log = "";
	private static String seperator = "#SEP#";
	private static String entryPrefix = "[0000]";
	private static int autoSaveAt = 5000;
	private static boolean autoSave = true;
	private static boolean logDate = true;
	private static boolean logCommandUsage = true;
	private static boolean logChatMessage = true;
	private static boolean logDebugMessage = true;
	private static boolean logPluginInternProcesses = true;
	private static int logged = 0;
	
	public XLogger() { }
	
	public static void loadSettings() {
		autoSave = Settings.xloggerAutoSave;
		autoSaveAt = Settings.xloggerAutoSaveAt;
		logDate = Settings.xloggerLogDate;
		logCommandUsage = Settings.xloggerLogCommandUsage;
		logCommandUsage = Settings.xloggerLogChatMessage;
		logCommandUsage = Settings.xloggerLogDebugMessage;
		logCommandUsage = Settings.xloggerLogPluginInternProcesses;
	}
	
	/**
	 * Fügt eine Message zum Log hinzu, nachdem die Nachricht Einstellungs-komform bestätigt wurde.
	 * @param type
	 * @param message
	 */
	public static void log(LogType type, String message) {
		switch(type) {
		case ChatMessage: if(logChatMessage == false) return;
		case CommandUsage: if(logCommandUsage == false) return;
		case DebugMessage: if(logDebugMessage == false) return;
		case PluginInternProcess: if(logPluginInternProcesses == false) return;
		default:
			break;
		}
		if(message.endsWith(".") == false && type != LogType.CommandUsage) message = message+".";
		if(logDate == false) log = log+seperator+entryPrefix+"["+type.px+"] "+message;
		else log = log+seperator+entryPrefix+"["+SkyBlock.getCurrentDate()+"/"+SkyBlock.getCurrentTime()+"]["+type.px+"] "+message;
		
		logged++;
		generateNewEntryPrefix();
		if(autoSave && logged == autoSaveAt) {
			if(logDate == false) log = log+seperator+entryPrefix+"["+LogType.PluginInternProcess.px+"] "+"Auto saving...";
			else log = log+seperator+entryPrefix+"["+SkyBlock.getCurrentDate()+"/"+SkyBlock.getCurrentTime()+"]["+LogType.PluginInternProcess.px+"] "+"Auto saving...";
			save(true);
		}
	}
	
	/**
	 * Gibt die Letzte Nachricht aus dem Log heraus
	 * @return
	 */
	public static String getLastLogged() {
		return "Noch nicht";
	}
	
	/**
	 * Gibt den Log in Form einer List zurück
	 * @return
	 */
	public static LinkedList<String> getLog() {
		LinkedList<String> list_rev = new LinkedList<String>(); //Reversed, sonst wird der Log falschherum gespeichert
		String[] sar = log.replace(seperator, System.lineSeparator()+"§f").split(seperator);
		for(int i = 0; i != sar.length; i++) list_rev.add(sar[i]);
		return list_rev;
	}
	
	/**
	 * Speichert den Log in eine Datei. Anschließend wird der Log gesäubert, wenn erwünscht.
	 * plugins/"+SkyBlock.getSB().getDescription().getName()+"/logs/Datum-log.yml
	 * @return
	 */
	public static String save() {
		return save(false);
	}
	/**
	 * Speichert den Log in eine Datei. Anschließend wird der Log gesäubert, wenn erwünscht.
	 * plugins/"+SkyBlock.getSB().getDescription().getName()+"/logs/Datum-log.yml
	 * @return
	 */
	public static String save(boolean clear) {
		String date = getCompressedDate();
		
		LinkedList<String> list_rev = new LinkedList<String>(); //Reversed, sonst wird der Log falschherum gespeichert
		for(String s : getLog()) {
			list_rev.add(s);
		}
		
	    try {
	        FileWriter writer = new FileWriter("plugins/"+SkyBlock.getSB().getDescription().getName()+"/logs/"+date+"-log.yml");
	        for(String s : list_rev) {
	        	writer.write(s);
	        	writer.write(System.lineSeparator());
	        }
        	writer.write("["+SkyBlock.getCurrentDate()+"/"+SkyBlock.getCurrentTime()+"]["+LogType.PluginInternProcess.px+"] Speichere Log...");
        	writer.write(System.lineSeparator());
        	writer.write("["+SkyBlock.getCurrentDate()+"/"+SkyBlock.getCurrentTime()+"]["+LogType.LastMessage.px+"] Online Spieler: " +getOnlinePlayers());
        	writer.write(System.lineSeparator());
        	writer.write("["+SkyBlock.getCurrentDate()+"/"+SkyBlock.getCurrentTime()+"]["+LogType.LastMessage.px+"] ------------------------------ Log vom "+SkyBlock.getCurrentDate()+" um "+SkyBlock.getCurrentTime()+" Uhr ------------------------------------");
	        writer.close();
	        
	        if(clear) {
	        	log = "";
	        	logged = 0;
	        }
	        return date;
	      } catch (IOException e) {
	    	  SkyBlock.sendConsoleMessage(MessageType.ERROR, "Log konnte nicht gespeichert werden");
	    	  SkyBlock.sendOperatorMessage(MessageType.ERROR, "Der Log konnte nicht gespeichert werden");
	        e.printStackTrace();
	        return "NOT SAVED";
	      }
	}
	
	
	/**
	 * Gibt ein String zurück, der die Uhrzeit und das Datum enthält.
	 * @return
	 */
	private static String getCompressedDate() {
		return SkyBlock.getCurrentDate().replace(".", "-").replace(":", "-")+"-"+SkyBlock.getCurrentTime().replace(".", "-").replace(":", "-");
	}
	
	private static String getOnlinePlayers() {
		String s = "";
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(s.equals("")) s = p.getName();
			else s = s+", "+p.getName();
		}
		return s;
	}
	
	private static int amountZero = new String(autoSaveAt+"").length()-1;
	public static void generateNewEntryPrefix() {
		for(int i = 0; i != autoSaveAt; i++) {
			
			if(logged < 10) {
				entryPrefix = "[";
				for(int a = 0; a != amountZero; a++) entryPrefix = entryPrefix + "0";
				
				entryPrefix = entryPrefix + logged + "]";
			}else if(logged < 100) {
				entryPrefix = "[";
				for(int a = 0; a != amountZero-1; a++) entryPrefix = entryPrefix + "0";
				
				entryPrefix = entryPrefix + logged + "]";
			}else if(logged < 1000) {
				entryPrefix = "[";
				for(int a = 0; a != amountZero-2; a++) entryPrefix = entryPrefix + "0";
				
				entryPrefix = entryPrefix + logged + "]";
			}else if(logged < 10000) {
				entryPrefix = "[";
				for(int a = 0; a != amountZero-3; a++) entryPrefix = entryPrefix + "0";
				
				entryPrefix = entryPrefix + logged + "]";
			}else if(logged < 100000) {
				entryPrefix = "[";
				for(int a = 0; a != amountZero-4; a++) entryPrefix = entryPrefix + "0";
				
				entryPrefix = entryPrefix + logged + "]";
			}
		}
	}
	
	public static enum LogType {
		ChatMessage("CHAT"),
		CommandUsage("COMMAND"),
		DebugMessage("DEBUG"),
		LastMessage("END"),
		PluginInternProcess("PROCESS");
		
		String px = "";
		
		private LogType(String px) {
			this.px = px;
		}
		
		public String getPrefix() {
			return px;
		}
	}
}
