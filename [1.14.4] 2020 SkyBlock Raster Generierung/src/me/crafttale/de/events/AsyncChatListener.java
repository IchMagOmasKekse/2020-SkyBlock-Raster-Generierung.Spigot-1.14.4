package me.crafttale.de.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.crafttale.de.Chat;
import me.crafttale.de.SkyBlock;
import me.crafttale.de.log.XLogger;
import me.crafttale.de.log.XLogger.LogType;

public class AsyncChatListener implements Listener {
	
	public AsyncChatListener() {
		SkyBlock.getSB().getServer().getPluginManager().registerEvents(this, SkyBlock.getSB());
	}
	
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent e) {
		Chat.onChatting(e);
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onCommand(PlayerCommandPreprocessEvent e) {
		XLogger.log(LogType.CommandUsage, e.getPlayer().getName()+" -> "+e.getMessage());
	}
	
}
