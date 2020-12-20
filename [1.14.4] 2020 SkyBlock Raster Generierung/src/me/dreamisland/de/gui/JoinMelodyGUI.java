package me.dreamisland.de.gui;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.dreamisland.de.SkyBlock;
import me.dreamisland.de.gui.GUIManager.GUI;

public class JoinMelodyGUI extends GUI {

	public JoinMelodyGUI(Player player) {
		super(player, GUIType.JOIN_MELODY);
		player.closeInventory();
		maxFrames = 4;
		loop = false;
	}

	@Override
	public void start() {
		if(animator != null) return;
		animator = new BukkitRunnable() {
			
			@Override
			public void run() {
				switch(frame) {
				case 0:
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 6f, 0f);
					nextFrame();
					break;
				case 1:
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 6f, 1f);
					nextFrame();
					break;
				case 2:
					nextFrame();
					break;
				case 3:
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 6f, 1.5f);
					nextFrame();
					break;
					default:
						if(loop) {
							reset();
							frame = 0;
						}else stop();
						break;
				}
			}
		};
		animator.runTaskTimer(SkyBlock.getSB(), (long)1.5*20l, 3l);
	}

	@Override
	public void stop() {
		if(animator == null) return;
		else animator.cancel();
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clickedItem(ItemStack item) {
		// TODO Auto-generated method stub
		
	}

}
