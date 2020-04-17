package me.ichmagomaskekse.de.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.ichmagomaskekse.de.SkyBlock;

public class AsyncChatListener implements Listener {
	
	public AsyncChatListener() {
		SkyBlock.getInstance().getServer().getPluginManager().registerEvents(this, SkyBlock.getInstance());
	}
	
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if(e.getPlayer().isOp() || e.getPlayer().hasPermission("skyblock.chat.color")) {
			e.setMessage(e.getMessage().replace("&", "§"));
		}
	}
	
}
