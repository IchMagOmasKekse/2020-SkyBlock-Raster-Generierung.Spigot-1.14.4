package me.ichmagomaskekse.de.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.ichmagomaskekse.de.Prefixes;
import me.ichmagomaskekse.de.SkyBlock;

public class JoinAndQuitListener implements Listener {
	
	public JoinAndQuitListener() {
		//Registriere alle Envents in dieser Klasse
		SkyBlock.getInstance().getServer().getPluginManager().registerEvents(this, SkyBlock.getInstance());
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		e.setJoinMessage(Prefixes.JOIN.getPrefix()+"§e"+e.getPlayer().getName()+" §7ist dem Server beigetreten");
		SkyBlock.spawnFireworks(e.getPlayer().getLocation().clone(), 1);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		e.setQuitMessage(Prefixes.QUIT.getPrefix() + "§e"+e.getPlayer().getName()+" §7hat den Server verlassen");
	}

}
