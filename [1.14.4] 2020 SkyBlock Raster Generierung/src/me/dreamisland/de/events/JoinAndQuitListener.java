package me.dreamisland.de.events;

import org.bukkit.Bukkit;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;

import me.dreamisland.de.Prefixes;
import me.dreamisland.de.SkyBlock;
import me.dreamisland.de.SkyBlockGenerator;
import me.dreamisland.de.filemanagement.SkyFileManager;

public class JoinAndQuitListener implements Listener {
	
	public JoinAndQuitListener() {
		//Registriere alle Envents in dieser Klasse
		SkyBlock.getInstance().getServer().getPluginManager().registerEvents(this, SkyBlock.getInstance());
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		e.setJoinMessage(Prefixes.JOIN.getPrefix()+"§e"+e.getPlayer().getName()+" §7ist dem Server beigetreten");
		SkyBlock.spawnFireworks(e.getPlayer().getLocation().clone(), 1, true, true, Type.BALL_LARGE);
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.isOp()) {
				PermissionAttachment att = p.addAttachment(SkyBlock.getInstance());
				att.setPermission("mv.bypass.gamemode.*", true);
			}
		}
		SkyBlock.sendChangelog(e.getPlayer());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		e.setQuitMessage(Prefixes.QUIT.getPrefix() + "§e"+e.getPlayer().getName()+" §7hat den Server verlassen");
		if(e.getPlayer().getWorld().getName().equals(SkyBlockGenerator.skyworldName))e.getPlayer().teleport(SkyBlock.spawn);
		if(SkyFileManager.runs.containsKey(e.getPlayer())) {
			e.getPlayer().teleport(SkyBlock.spawn);
			e.getPlayer().setGameMode(GameMode.ADVENTURE);
		}
	}
	
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		if(e.getResult() == Result.KICK_WHITELIST) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(p.isOp()) p.sendMessage("§e"+e.getPlayer().getName()+" §fhat versucht zu joinen. Abgelehnt wegen Whitelist");
			}
		}
	}

}
