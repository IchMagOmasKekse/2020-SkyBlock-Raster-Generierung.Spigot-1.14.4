package me.crafttale.de.events;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scheduler.BukkitRunnable;

import me.crafttale.de.PlayerAtlas;
import me.crafttale.de.Prefixes;
import me.crafttale.de.SkyBlock;
import me.crafttale.de.SkyBlockGenerator;
import me.crafttale.de.economy.EconomyManager;
import me.crafttale.de.economy.SkyCoinHandler;
import me.crafttale.de.filemanagement.SkyFileManager;
import me.crafttale.de.gadgets.lobby.Spawn;
import me.crafttale.de.profiles.PlayerProfiler;
import me.crafttale.de.profiles.PlayerSettings;
import me.crafttale.de.profiles.processing.ProcessType;
import me.crafttale.de.reward.DailyRewardManager;
import me.crafttale.de.tablist.TablistManager;
import me.crafttale.de.waitlobby.WaitLobby;

public class JoinAndQuitListener implements Listener {
	
	private HashMap<String, BukkitRunnable> runnables = new HashMap<String, BukkitRunnable>();
	
	public JoinAndQuitListener() {
		//Registriere alle Envents in dieser Klasse
		SkyBlock.getSB().getServer().getPluginManager().registerEvents(this, SkyBlock.getSB());
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
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
				WaitLobby.hideExtraPlayer(e.getPlayer());
			}
		});
		runnables.get(e.getPlayer().getName()+"1").runTaskAsynchronously(SkyBlock.getSB());
		
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
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
		PlayerSettings.onJoin(e);
		if(EconomyManager.hasAccount(PlayerProfiler.getUUID(player)) == false) EconomyManager.createAccount(PlayerProfiler.getUUID(player));
		SkyCoinHandler.listBoosters(player);
		if(DailyRewardManager.hasGotDailyReward(player) == false) DailyRewardManager.onJoin(e);
		else if((boolean)PlayerSettings.getConfirmation(ProcessType.PLAY_JOIN_MELODY, e.getPlayer()) == true) Spawn.playJoinMelody(e);
		TablistManager.onJoin(e);
		Spawn.onJoin(e);
		
		
		/* Join Effects */
//		SkyBlock.spawnFireworks(player.getLocation().clone(), 1, true, true, Type.BALL_LARGE);
//		GUIManager.openGUI(player, new JoinMelodyGUI(player));
		if(player.getName().equals("IchMagOmasKekse")) player.sendMessage("Island Control Commands machen!");
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
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
