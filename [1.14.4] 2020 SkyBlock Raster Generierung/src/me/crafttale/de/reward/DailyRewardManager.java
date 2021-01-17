package me.crafttale.de.reward;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.crafttale.de.Chat;
import me.crafttale.de.Chat.MessageType;
import me.crafttale.de.Settings;
import me.crafttale.de.SkyBlock;
import me.crafttale.de.gui.GUIManager;
import me.crafttale.de.gui.reward.RewardGUI;
import me.crafttale.de.profiles.PlayerProfiler;

public class DailyRewardManager {
	
	private static BukkitRunnable updater;
	private static int update_cooldown = 2; //Standart 100
	
	public DailyRewardManager() {
		updater = new BukkitRunnable() {
			
			@Override
			public void run() {
				update();
			}
		};
		updater.runTaskTimerAsynchronously(SkyBlock.getSB(), 0l, 10l);
	}
	
	public static void stop() {
		if(updater != null && updater.isCancelled() == false) updater.cancel();
	}
	
	public static void update() {
		if(new File("plugins/SkyBlock/daily_rewards.yml").exists() == false) createDailyRewardsFile();
		if(update_cooldown == 0) {
			String seconds, minutes, hours;
			seconds = SkyBlock.getCurrentSeconds();
			minutes = SkyBlock.getCurrentMinutes();
			hours = SkyBlock.getCurrentHours();
			
			if(Integer.parseInt(seconds) == Settings.daily_Reward_reset_at_seconds &&
					Integer.parseInt(minutes) == Settings.daily_Reward_reset_at_minutes &&
					Integer.parseInt(hours) == Settings.daily_Reward_reset_at_hours) {
				resetRewardingCooldown();
				SkyBlock.sendMessage(MessageType.INFO, "§dDie täglichen Belohnungen sind wieder verfügbar!");
				for(Player p : Bukkit.getOnlinePlayers())
					Chat.sendClickableMessage(p, "§6Klicke hier um deine Tägliche Belohnung abzuholen", "! FREE ITEMS ! YEAH !", "/dailyreward", true, true);
			}
			update_cooldown = 2;
		}else update_cooldown--;
	}
	
	private static void createDailyRewardsFile() {
		resetRewardingCooldown();
	}
	
	private static void resetRewardingCooldown() {
		File file = new File("plugins/SkyBlock/daily_rewards.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		ArrayList<String> users = new ArrayList<String>();
		cfg.set("Got A Daily Reward", users);
		
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void registerPlayerHasGotDailyReward(Player p) {
		File file = new File("plugins/SkyBlock/daily_rewards.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		List<String> users = cfg.getStringList("Got A Daily Reward");
		users.add(PlayerProfiler.getUUID(p).toString());
		cfg.set("Got A Daily Reward", users);
		
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean hasGotDailyReward(Player p) {
		File file = new File("plugins/SkyBlock/daily_rewards.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		List<String> users = cfg.getStringList("Got A Daily Reward");
		
		return users.contains(PlayerProfiler.getUUID(p).toString());
	}

	/**
	 * Behandelt einen Spieler, nachdem er gejoint ist.
	 * @param e
	 */
	public static void onJoin(PlayerJoinEvent e) {
		new BukkitRunnable() {
			@Override
			public void run() {					
				GUIManager.openGUI(e.getPlayer(), new RewardGUI(e.getPlayer()));
			}
		}.runTaskLater(SkyBlock.getSB(), 20l);
		
		Chat.sendClickableMessage(e.getPlayer(), "§6Klicke hier um deine Tägliche Belohnung abzuholen", "! FREE ITEMS ! YEAH !", "/dailyreward", true, true);
	}
	
}
