package me.dreamisland.de.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import me.dreamisland.de.SkyBlock;

public class ServerListListener implements Listener {
	
	public ServerListListener() {
		SkyBlock.getInstance().getServer().getPluginManager().registerEvents(this, SkyBlock.getInstance());
	}
	
	
	@EventHandler
	public void onPing(ServerListPingEvent e) {
		e.setMotd("§cTesting, Testing und noch mehr Testing");
	}
	
}
