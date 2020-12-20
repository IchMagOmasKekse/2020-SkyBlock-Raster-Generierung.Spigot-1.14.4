package me.dreamisland.de.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;

import me.dreamisland.de.Chat;
import me.dreamisland.de.PlayerAtlas;
import me.dreamisland.de.Prefixes;
import me.dreamisland.de.SkyBlock;
import me.dreamisland.de.SkyBlockGenerator;
import me.dreamisland.de.economy.EconomyManager;
import me.dreamisland.de.economy.SkyCoinHandler;
import me.dreamisland.de.filemanagement.SkyFileManager;
import me.dreamisland.de.gui.GUIManager;
import me.dreamisland.de.gui.JoinMelodyGUI;

public class JoinAndQuitListener implements Listener {
	
	public JoinAndQuitListener() {
		//Registriere alle Envents in dieser Klasse
		SkyBlock.getSB().getServer().getPluginManager().registerEvents(this, SkyBlock.getSB());
	}
	
	@EventHandler
	public void onJoin(final PlayerJoinEvent e) {
		
		e.setJoinMessage(Prefixes.JOIN.px()+"§e"+e.getPlayer().getName()+" §7ist dem Server beigetreten");
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.isOp()) {
				PermissionAttachment att = p.addAttachment(SkyBlock.getSB());
				att.setPermission("mv.bypass.gamemode.*", true);
			}
		}
		/* Neukömmlings Ablauf */
		PlayerAtlas.savePlayer(e.getPlayer());
		if(EconomyManager.hasAccount(e.getPlayer().getUniqueId()) == false) EconomyManager.createAccount(e.getPlayer().getUniqueId());
//		SkyCoinHandler.addJoinCoins(e.getPlayer());
		
		SkyCoinHandler.listBoosters(e.getPlayer());
//		SkyBlock.sendChangelog(e.getPlayer());
		
		long time = (System.currentTimeMillis() - e.getPlayer().getLastPlayed());
		if(time >= 2000) { /*86400000l = 1 Tag(86400000 Millisekunden)*/
			Chat.sendClickableMessage(e.getPlayer(), "§6Klicke hier um deine Tägliche Belohnung abzuholen", "! FREE ITEMS ! YEAH !", "/dailyreward", true, true);
		}
		
		
		/* Join Effects */
//		SkyBlock.spawnFireworks(e.getPlayer().getLocation().clone(), 1, true, true, Type.BALL_LARGE);
		GUIManager.openGUI(e.getPlayer(), new JoinMelodyGUI(e.getPlayer()));
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		PlayerAtlas.savePlayer(e.getPlayer());
		e.setQuitMessage(Prefixes.QUIT.px() + "§e"+e.getPlayer().getName()+" §7hat den Server verlassen");
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
