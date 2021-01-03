package me.crafttale.de.gui.defaults;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.crafttale.de.SkyBlock;
import me.crafttale.de.gui.GUI;
import me.crafttale.de.gui.GUIManager;

public class LoadingAnimaGUI extends GUI {

	public String subtitle = "§oLädt";
	public String subtitleFinished = "100%";
	public String subtitleClosing = "";
	
	public LoadingAnimaGUI(Player player) {
		super(player, GUIType.LOADING_ANIMA);
		loop = true;
		maxFrames = 3;
	}
	
	public LoadingAnimaGUI(Player player, String subtitle, String subtitleFinished, String subtitleClosing) {
		super(player, GUIType.LOADING_ANIMA);
		this.subtitle= subtitle;
		this.subtitleFinished = subtitleFinished;
		this.subtitleClosing = subtitleClosing;
		loop = true;
		maxFrames = 3;
	}

	@Override
	public void start() {
		if(animator != null) return;
		animator = new BukkitRunnable() {
			
			@Override
			public void run() {
				switch(frame) {
				case 0:
					player.sendTitle("§fO§7oo", subtitle, 0, (int)speed+10, 0);
					nextFrame();
					break;
				case 1:
					player.sendTitle("§7o§fO§7o", subtitle, 0, (int)speed+10, 0);
					nextFrame();
					break;
				case 2:
					player.sendTitle("§7oo§fO", subtitle, 0, (int)speed+10, 0);
					nextFrame();
					break;
				case 3:
					player.sendTitle("§7o§fO§7o", subtitle, 0, (int)speed+10, 0);
					if(stopAnimation)nextFrame();
					else frame = 0;
					break;
					default:
						if(loop) {
							reset();
							frame = 0;
						}else {
							stop();
							player.sendTitle("", subtitleFinished, 0, (int)speed+10, 0);
							GUIManager.closeGUI(player);
						}
						break;
				}
			}
		};
		animator.runTaskTimer(SkyBlock.getSB(), 0l, speed);
	}
	
	public void loadingFinished() {
		player.sendTitle("", subtitleFinished, 0, (int)speed+10, 10);
		loop = false;
		frame = 4;
	}

	@Override
	public void stop() {
		this.stopAnimation = true;
		if(animator == null) return;
		else animator.cancel();
		player.sendTitle("", subtitleClosing, 0, (int)speed+10, 10);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clickedItem(InventoryClickEvent e, ItemStack item) {
		// TODO Auto-generated method stub
		
	}
}
