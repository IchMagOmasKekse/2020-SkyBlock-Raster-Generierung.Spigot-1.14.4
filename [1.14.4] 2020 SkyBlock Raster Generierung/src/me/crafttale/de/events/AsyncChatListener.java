package me.crafttale.de.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.crafttale.de.Settings;
import me.crafttale.de.SkyBlock;
import me.crafttale.de.profiles.log.XLogger;
import me.crafttale.de.profiles.log.XLogger.LogType;
import me.crafttale.de.tablist.TablistManager;
import net.md_5.bungee.api.ChatColor;

public class AsyncChatListener implements Listener {
	
	public AsyncChatListener() {
		SkyBlock.getSB().getServer().getPluginManager().registerEvents(this, SkyBlock.getSB());
	}
	
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent e) {
		if(e.getPlayer().isOp() || e.getPlayer().hasPermission("skyblock.chat.color")) {
			e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
		}
		e.setFormat(Settings.chat_format.replace("{USERNAME}", e.getPlayer().getName())
				.replace("{RANK_PREFIX}", TablistManager.getTeamPrefix(e.getPlayer()))
				.replace("{RANK_SUFFIX}", TablistManager.getTeamSuffix(e.getPlayer()))
				.replace("{CHAT_MESSAGE}", e.getMessage()));
		
		XLogger.log(LogType.ChatMessage, e.getFormat());
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onCommand(PlayerCommandPreprocessEvent e) {
		XLogger.log(LogType.CommandUsage, e.getPlayer().getName()+" -> "+e.getMessage());
	}
	
}
