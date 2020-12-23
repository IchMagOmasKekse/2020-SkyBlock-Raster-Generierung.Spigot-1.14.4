package me.skytale.de.events;

import java.util.HashMap;

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
import org.bukkit.scheduler.BukkitRunnable;

import me.ichmagomaskekse.de.GUI.GUIType;
import me.skytale.de.Chat;
import me.skytale.de.PlayerAtlas;
import me.skytale.de.Prefixes;
import me.skytale.de.SkyBlock;
import me.skytale.de.SkyBlockGenerator;
import me.skytale.de.economy.EconomyManager;
import me.skytale.de.economy.SkyCoinHandler;
import me.skytale.de.filemanagement.SkyFileManager;
import me.skytale.de.profiles.PlayerProfiler;

public class JoinAndQuitListener implements Listener {
	
	private HashMap<Player, BukkitRunnable> runnables = new HashMap<Player, BukkitRunnable>();
	
	public JoinAndQuitListener() {
		//Registriere alle Envents in dieser Klasse
		SkyBlock.getSB().getServer().getPluginManager().registerEvents(this, SkyBlock.getSB());
	}
	
	@EventHandler
	public void asyncPreLogin(PlayerLoginEvent e) {
		
		runnables.put(e.getPlayer(), new BukkitRunnable() {
			
			@Override
			public void run() {				
				PlayerProfiler.registerPlayer(e.getPlayer());
				PlayerAtlas.savePlayer(e.getPlayer());
			}
		});
		runnables.get(e.getPlayer()).runTaskAsynchronously(SkyBlock.getSB());
		
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
		/*
		 * Neukömmlings Ablauf --------------------------------------------------------------------------------------------------------
		 * 
		 * */
//		PlayerProfiler.registerPlayer(e.getPlayer());
//		PlayerAtlas.savePlayer(e.getPlayer());
		if(EconomyManager.hasAccount(PlayerProfiler.getUUID(e.getPlayer())) == false) EconomyManager.createAccount(PlayerProfiler.getUUID(e.getPlayer()));
		SkyCoinHandler.listBoosters(e.getPlayer());		
		long time = (System.currentTimeMillis() - e.getPlayer().getLastPlayed());
		if(time >= 86400000l) { /*86400000l = 1 Tag(86400000 Millisekunden)*/
			Chat.sendClickableMessage(e.getPlayer(), "§6Klicke hier um deine Tägliche Belohnung abzuholen", "! FREE ITEMS ! YEAH !", "/dailyreward", true, true);
		}
		
		
		/* Join Effects */
//		SkyBlock.spawnFireworks(e.getPlayer().getLocation().clone(), 1, true, true, Type.BALL_LARGE);
		Bukkit.dispatchCommand(e.getPlayer(), "gui "+GUIType.JOIN_MELODY.toString());
//		GUIManager.openGUI(e.getPlayer(), new JoinMelodyGUI(e.getPlayer()));
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
		
		PlayerProfiler.unregisterPlayer(e.getPlayer());
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
