package me.dreamisland.de.gui.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import me.dreamisland.de.SkyBlock;

public class SkyBlockScoreboard {
	
	ScoreboardManager manager = Bukkit.getScoreboardManager();
	final Scoreboard board = manager.getNewScoreboard();
	final Objective objective = board.registerNewObjective("aaa", "bbb", "ccc");
	@EventHandler
	public void PlayerJoin(PlayerJoinEvent e) {
		final Player p = e.getPlayer();
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SkyBlock.getSB(), new Runnable() {
			public void run() {        
				ScoreboardManager manager = Bukkit.getScoreboardManager();
				final Scoreboard board = manager.getNewScoreboard();
				final Objective objective = board.registerNewObjective("aaa", "bbb", "ccc");        
				objective.setDisplaySlot(DisplaySlot.SIDEBAR);
				objective.setDisplayName(ChatColor.RED + "YourScoreboardTitle");
				Score score = objective.getScore("Score10");
				score.setScore(10);            
				Score score1 = objective.getScore("Score9");
				score1.setScore(9);        
				Score score2 = objective.getScore("Score8");
				score2.setScore(8);                        
				Score score3 = objective.getScore("§6Colors");
				score3.setScore(7);        
				p.setScoreboard(board);
			}
		},0, 20 * 10);
		
	}
	
}
