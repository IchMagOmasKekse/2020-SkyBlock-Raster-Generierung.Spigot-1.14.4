package me.crafttale.de.gui.island;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.crafttale.de.SkyBlock;
import me.crafttale.de.gui.GUI;
import me.crafttale.de.gui.GUIManager;

public class VisitDeniedGUI extends GUI {

	public VisitDeniedGUI(Player player) {
		super(player, GUIType.VISIT_ACCEPTED_MELODY);
		player.closeInventory();
		maxFrames = 2;
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
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 6f, 1f);
					nextFrame();
					break;
				case 1:
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 6f, 0f);
					nextFrame();
					break;
					default:
						if(loop) {
							reset();
							frame = 0;
						}else {
							stop();
							GUIManager.closeGUI(player);
						}
						break;
				}
			}
		};
		animator.runTaskTimer(SkyBlock.getSB(), 0l, 3l);
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
	public void clickedItem(InventoryClickEvent e, ItemStack item) {
		// TODO Auto-generated method stub
		
	}

}
