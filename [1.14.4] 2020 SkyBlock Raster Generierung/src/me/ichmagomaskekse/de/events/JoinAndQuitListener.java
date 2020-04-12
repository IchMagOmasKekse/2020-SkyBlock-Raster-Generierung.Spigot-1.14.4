package me.ichmagomaskekse.de.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.ichmagomaskekse.de.SkyBlock;

public class JoinAndQuitListener implements Listener {
	
	
	public JoinAndQuitListener() {
		//Registriere alle Envents in dieser Klasse
		SkyBlock.getInstance().getServer().getPluginManager().registerEvents(this, SkyBlock.getInstance());
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if(e.getPlayer().isOp()) e.getPlayer().sendMessage(">>> §eVerwende §7/simulate §eum an deiner Location auf Höhe 140 eine Simulation der Größe 10 zu starten");
		if(e.getPlayer().isOp()) e.getPlayer().sendMessage(">>> §cACHTUNG: §fDer Generation-Vorgang kann nicht abgebrochen oder rückgängig gemacht werden!!");
	}

}
