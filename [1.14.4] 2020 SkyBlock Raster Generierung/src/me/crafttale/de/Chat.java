package me.crafttale.de;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.crafttale.de.log.XLogger;
import me.crafttale.de.log.XLogger.LogType;
import me.crafttale.de.profiles.PlayerProfiler;
import me.crafttale.de.profiles.PlayerSettings;
import me.crafttale.de.profiles.processing.ProcessType;
import me.crafttale.de.tablist.TablistManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Chat {
	
	@SuppressWarnings("deprecation")
	public static boolean sendClickableVisitingMessage(Player target, String msg, String hovermsg, String command, boolean bold, boolean italic) {
		msg = prepareString(msg);
		hovermsg = prepareString(hovermsg);
		TextComponent message = new TextComponent(msg);
		
		message.setItalic(italic);
		message.setBold(bold);
		message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder(hovermsg).italic(italic).bold(bold).create()));
		message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
		
		target.spigot().sendMessage(message);
		return true;
	}
	@SuppressWarnings("deprecation")
	public static boolean sendClickableMessage(Player target, String msg, String hovermsg, String command, boolean bold, boolean italic) {
		msg = prepareString(msg);
		hovermsg = prepareString(hovermsg);
		TextComponent message = new TextComponent(Prefixes.CLICKABLE.px()+msg);
		
		message.setItalic(italic);
		message.setBold(bold);
		message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder(hovermsg).italic(italic).color(ChatColor.GREEN).bold(bold).create()));
		message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
		
		target.spigot().sendMessage(message);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public static boolean sendHoverableMessage(Player target, String msg, String hovermsg, boolean bold, boolean italic) {
		msg = prepareString(msg);
		hovermsg = prepareString(hovermsg);
		TextComponent message = new TextComponent(Prefixes.HOVERABLE.px()+msg);
		
		message.setItalic(italic);
		message.setBold(bold);
		message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder(hovermsg).italic(italic).bold(bold).color(ChatColor.DARK_AQUA).create()));
		
		target.spigot().sendMessage(message);
		return true;
	}
	@SuppressWarnings("deprecation")
	public static boolean sendHoverableCommandHelpMessage(Player target, String msg, String hovermsg, boolean bold, boolean italic) {
		msg = prepareString(msg);
		hovermsg = prepareString(hovermsg);
		TextComponent message = new TextComponent(msg);
		
		message.setItalic(italic);
		message.setBold(bold);
		message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder(hovermsg).italic(italic).bold(bold).color(ChatColor.DARK_AQUA).create()));
		
		target.spigot().sendMessage(message);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public static boolean sendCopyableMessage(Player target, String msg, String hovermsg, String copy, boolean bold, boolean italic) {
		msg = prepareString(msg);
		hovermsg = prepareString(hovermsg);
		TextComponent message = new TextComponent(Prefixes.COPYABLE.px()+msg);
		
		message.setItalic(italic);
		message.setBold(bold);
		message.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, copy));
		message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder(hovermsg).italic(italic).bold(bold).create()));
		
		target.spigot().sendMessage(message);
		return true;
	}
	
	/*
	 * Cleart den Chat für alle Spieler die nicht im Bypass drinnen sind.
	 */
	public static void clearChat(ArrayList<UUID> bypass) {
		TextComponent message = new TextComponent("");
		message.setColor(ChatColor.AQUA);
		for(int i = 0; i != 200; i++) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(bypass.contains(PlayerProfiler.getUUID(p)) == false) {
					p.spigot().sendMessage(message);
				}
			}
		}
	}
	/**
	 * Sendet einem Spieler eine komplett leere Nachricht.
	 * @param p
	 */
	public static void sendClearMessage(Player p) {
		TextComponent message = new TextComponent("");
		message.setColor(ChatColor.AQUA);
		p.spigot().sendMessage(message);
	}
	
	public static String prepareString(String s) {
		s = s.replace("{NEWLINE}", "\n");
		return s;
	}
	
	/**
	 * Behandelt Chat Nachrichten, wenn sie versendet werden.
	 * @param e
	 */
	public static void onChatting(AsyncPlayerChatEvent e) {
		if((boolean)PlayerSettings.getConfirmation(ProcessType.ALLOW_CHAT_MESSAGES, e.getPlayer()) == true) {
			if(e.getPlayer().isOp() || e.getPlayer().hasPermission("skyblock.chat.color")) {
				e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
			}
			e.setFormat(Settings.chat_format.replace("{USERNAME}", e.getPlayer().getName())
					.replace("{RANK_PREFIX}", TablistManager.getTeamPrefix(e.getPlayer()))
					.replace("{RANK_SUFFIX}", TablistManager.getTeamSuffix(e.getPlayer()))
					.replace("{CHAT_MESSAGE}", e.getMessage()));
			XLogger.log(LogType.ChatMessage, e.getFormat());
		}else {
			e.setCancelled(true);
			SkyBlock.sendMessage(MessageType.INFO, e.getPlayer(), "Du hast ausgestellt, dass Du Chat-Nachrichten senden kannst.");
			XLogger.log(LogType.ChatMessage, "CANCELLED BY PLAYERSETTINGS "+e.getFormat());
		}
	}
	
	public static enum MessageType {
		
		/*
		 * Diese Klasse wird bei sendMessage Methoden verwendet um die Wichtigkeit der Nachricht mitzuteilen
		 */
		
		INFO("§F[§a§lINFO§f]","§7 "), //Nice-To-Know Nachricht.
		WARNING("§f[§e§lWARNUNG§f]", "§c "), //ACHTUNG, könnte zu Problemen führen.
		ERROR("§f[§4FEHLER§f]", "§c "), //ggf. Handlungsbedarf.
		NONE("", "");//Falls kein Prefix/Suffic erforderlich ist.
		
		String prefix, suffix;
		
		MessageType(String prefix, String suffix) {
			this.prefix = prefix;
			this.suffix = suffix;
		}
		
		/**
		 * Gibt den Prefix der Wichtigkeit zurück
		 * @return
		 */
		public String px() { return prefix; }
		/**
		 * Gibt den Suffix der Wichtigkeit zurück.
		 * Dieser ist meistens ein Colorcode.
		 * @return
		 */
		public String sx() { return suffix; }
		
	}
	
}
