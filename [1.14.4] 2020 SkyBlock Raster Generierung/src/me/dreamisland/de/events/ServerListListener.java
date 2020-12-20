package me.dreamisland.de.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import me.dreamisland.de.SkyBlock;

public class ServerListListener implements Listener {
	
	public ServerListListener() {
		SkyBlock.getSB().getServer().getPluginManager().registerEvents(this, SkyBlock.getSB());
	}
	
	
	@EventHandler
	public void onPing(ServerListPingEvent e) {
		/* Ausgeschaltet, weil die MOTD vom Lobby System gesteuert werden soll */
//		e.setMotd("§aSkyblock §6§l950% RELEASE BOOSTER!");
	}
	
}
