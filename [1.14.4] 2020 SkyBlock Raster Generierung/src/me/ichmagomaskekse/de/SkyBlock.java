package me.ichmagomaskekse.de;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.ichmagomaskekse.de.commands.SkyBlockCommands;
import me.ichmagomaskekse.de.events.JoinAndQuitListener;

public class SkyBlock extends JavaPlugin {
	
	private static SkyBlock instance = null;
	public static SkyBlock getInstance() { return instance; } //Gebe die einzigartige Instanz der Main-Klasse zurück
	
	@Override
	public void onEnable() {
		preInit(); //Benötigte Inhalte werden geladen
		init(); //Einstellungen werden getätigt
		postInit(); //Listener, Commands, Craftingrezepte und weitere Additionen werden hinzugefügt
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		SkyBlockGenerator.undo();
		super.onDisable();
	}
	
	/*
	 * TODO: Benötigte Inhalte werden geladen und zum Starten des Plugins breitgestellt
	 */
	public void preInit() {
		instance = this;
	}
	
	/*
	 * TODO: Alle Einstellungen werdne getätigt
	 */
	public void init() {
		
	}
	
	/*
	 * TODO: Listener, Commands, Craftingrezepte und weite Additionen wie das Laden von
	 * Spielerprofilen, Grundstücksprofilen oder Schematiken werden hinzugefügt bzw. durchgeführt
	 * 
	 * 
	 */
	public void postInit() {
		new JoinAndQuitListener();
		new SkyBlockCommands();
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.isOp()) p.sendMessage(">>> §eVerwende §7/simulate §eum an deiner Location auf Höhe 230 eine Simulation zu starten");
		}
	}
	
}
