package me.skytale.de.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.skytale.de.SkyBlock;

public class AsyncChatListener implements Listener {
	
	public AsyncChatListener() {
		SkyBlock.getSB().getServer().getPluginManager().registerEvents(this, SkyBlock.getSB());
	}
	
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if(e.getPlayer().isOp() || e.getPlayer().hasPermission("skyblock.chat.color")) {
			e.setMessage(e.getMessage().replace("&", "§"));
		}
	}
	
}
