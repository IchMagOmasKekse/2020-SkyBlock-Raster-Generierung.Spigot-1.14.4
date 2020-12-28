package me.crafttale.de;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ActionBar {
	
	public static void send(boolean needOp, String message) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if(needOp && player.isOp())
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
			else if(needOp == false) player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
		} 
	}
	public static void send(boolean needOp, String message, Player target) {
		if(target.isOnline() && needOp && target.isOp())
			target.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
		else if(needOp == false) target.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
	}
}
