package me.crafttale.de.events;

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

import me.crafttale.de.Chat;
import me.crafttale.de.PlayerAtlas;
import me.crafttale.de.Prefixes;
import me.crafttale.de.SkyBlock;
import me.crafttale.de.SkyBlockGenerator;
import me.crafttale.de.economy.EconomyManager;
import me.crafttale.de.economy.SkyCoinHandler;
import me.crafttale.de.filemanagement.SkyFileManager;
import me.crafttale.de.gui.GUI.GUIType;
import me.crafttale.de.gui.GUIManager;
import me.crafttale.de.gui.reward.RewardGUI;
import me.crafttale.de.profiles.PlayerProfiler;
import me.crafttale.de.reward.DailyRewardManager;
import me.crafttale.de.tablist.TablistManager;

public class JoinAndQuitListener implements Listener {
	
	private HashMap<String, BukkitRunnable> runnables = new HashMap<String, BukkitRunnable>();
	
	public JoinAndQuitListener() {
		//Registriere alle Envents in dieser Klasse
		SkyBlock.getSB().getServer().getPluginManager().registerEvents(this, SkyBlock.getSB());
	}
	
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		if(e.getResult() == Result.KICK_WHITELIST) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(p.isOp()) p.sendMessage("§e"+e.getPlayer().getName()+" §fhat versucht zu joinen. Abgelehnt wegen Whitelist");
			}
		}		
		runnables.put(e.getPlayer().getName()+"1", new BukkitRunnable() {
			
			@Override
			public void run() {
				PlayerProfiler.registerPlayer(e.getPlayer());
				PlayerAtlas.savePlayer(e.getPlayer());
				runnables.remove(e.getPlayer().getName()+"1");
			}
		});
		runnables.get(e.getPlayer().getName()+"1").runTaskAsynchronously(SkyBlock.getSB());
		
	}
	
	@EventHandler
	public void onJoin(final PlayerJoinEvent e) {
		Player player = e.getPlayer();
		e.setJoinMessage(Prefixes.JOIN.px()+"§e"+player.getName()+" §7ist dem Server beigetreten");
		
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
//		PlayerProfiler.registerPlayer(player);
//		PlayerAtlas.savePlayer(player);
		if(EconomyManager.hasAccount(PlayerProfiler.getUUID(player)) == false) EconomyManager.createAccount(PlayerProfiler.getUUID(player));
		SkyCoinHandler.listBoosters(player);		
		if(DailyRewardManager.hasGotDailyReward(player) == false) {
			runnables.put(player.getName()+"2", new BukkitRunnable() {
				Player p = player;
				@Override
				public void run() {					
					GUIManager.openGUI(p, new RewardGUI(p));
					runnables.remove(p.getName()+"2");
				}
			});
			runnables.get(player.getName()+"2").runTaskLater(SkyBlock.getSB(), 20l);
			
			Chat.sendClickableMessage(player, "§6Klicke hier um deine Tägliche Belohnung abzuholen", "! FREE ITEMS ! YEAH !", "/dailyreward", true, true);
		}else {
			runnables.put(player.getName()+"2", new BukkitRunnable() {
				@Override
				public void run() {
					Bukkit.dispatchCommand(player, "gui "+GUIType.JOIN_MELODY.toString());
					runnables.remove(player.getName()+"2");
				}
			});
			runnables.get(player.getName()+"2").runTaskLater(SkyBlock.getSB(), 15l);
		}
		TablistManager.editTablist(player);
		
		
		
		/* Join Effects */
//		SkyBlock.spawnFireworks(player.getLocation().clone(), 1, true, true, Type.BALL_LARGE);
//		GUIManager.openGUI(player, new JoinMelodyGUI(player));
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

}
