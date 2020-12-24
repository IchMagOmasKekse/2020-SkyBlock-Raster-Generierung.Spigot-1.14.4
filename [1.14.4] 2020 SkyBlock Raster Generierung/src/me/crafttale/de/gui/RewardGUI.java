package me.crafttale.de.gui;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.crafttale.de.SkyBlock;
import me.crafttale.de.economy.SkyCoinHandler;
import me.ichmagomaskekse.de.GUI;
import me.ichmagomaskekse.de.GUIManager;

public class RewardGUI extends GUI {
	
	public static String reward_displayname = "§6SKYCOINS";
	
	public RewardGUI(Player player) {
		super(player, GUIType.REWARD_GUI);
		maxFrames = 24;
		loop = true;
	}

	@Override
	public void start() {
		if(animator != null) return;
		
		item = new ItemStack(Material.GOLD_NUGGET);
		meta = item.getItemMeta();
		meta.setDisplayName("§eKrall dir deine Belohnung!");
		item.setItemMeta(meta);
		
		animator = new BukkitRunnable() {
			
			@Override
			public void run() {
				if(openedInv != null) {					
					switch(frame) {
					case 0:
						//Step 1
						openedInv.setItem(3, null);
						openedInv.setItem(5, null);
						openedInv.setItem(0, item);
						openedInv.setItem(8, item);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 6f, (float)(frame*0.3));
						nextFrame();
						break;
					case 1:
						openedInv.setItem(0, null);
						openedInv.setItem(8, null);
						openedInv.setItem(1, item);
						openedInv.setItem(7, item);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 6f, (float)(frame*0.3));
						nextFrame();
						break;
					case 2:
						openedInv.setItem(1, null);
						openedInv.setItem(7, null);
						openedInv.setItem(2, item);
						openedInv.setItem(6, item);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 6f, (float)(frame*0.3));
						nextFrame();
						break;
					case 3:
						openedInv.setItem(2, null);
						openedInv.setItem(6, null);
						openedInv.setItem(3, item);
						openedInv.setItem(5, item);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 6f, (float)(frame*0.3));
						nextFrame();
						break;
					case 4:
						//Step 1
						openedInv.setItem(0, item);
						openedInv.setItem(8, item);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 6f, (float)(frame*0.3));
						nextFrame();
						break;
					case 5:
						openedInv.setItem(0, null);
						openedInv.setItem(8, null);
						openedInv.setItem(1, item);
						openedInv.setItem(7, item);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 6f, (float)(frame*0.3));
						nextFrame();
						break;
					case 6:
						openedInv.setItem(1, null);
						openedInv.setItem(7, null);
						openedInv.setItem(2, item);
						openedInv.setItem(6, item);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 6f, (float)(frame*0.3));
						nextFrame();
						break;
					case 7:
						//Step 1
						openedInv.setItem(0, item);
						openedInv.setItem(8, item);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 6f, (float)(frame*0.3));
						nextFrame();
						break;
					case 8:
						openedInv.setItem(0, null);
						openedInv.setItem(8, null);
						openedInv.setItem(1, item);
						openedInv.setItem(7, item);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 6f, (float)(frame*0.3));
						nextFrame();
						break;
					case 9:
						//Step 1
						openedInv.setItem(2, item);
						openedInv.setItem(6, item);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 6f, (float)(frame*0.3));
						nextFrame();
						break;
					case 10:
						//Step 1
						openedInv.setItem(0, item);
						openedInv.setItem(8, item);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 6f, (float)(frame*0.3));
						nextFrame();
						break;
					case 11:
						nextFrame();
						break;
					case 12:
						nextFrame();
						break;
					case 13:
						nextFrame();
						break;
					case 14:
						//Reverse Step 1
						openedInv.setItem(0, null);
						openedInv.setItem(8, null);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 6f, (float)(frame*0.3));
						nextFrame();
						break;
					case 15:
						openedInv.setItem(1, null);
						openedInv.setItem(7, null);
						openedInv.setItem(0, item);
						openedInv.setItem(8, item);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 6f, (float)(frame*0.3));
						nextFrame();
						break;
					case 16:
						openedInv.setItem(0, null);
						openedInv.setItem(8, null);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 6f, (float)(frame*0.3));
						nextFrame();
						break;
					case 17:
						openedInv.setItem(2, null);
						openedInv.setItem(6, null);
						openedInv.setItem(1, item);
						openedInv.setItem(7, item);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 6f, (float)(frame*0.3));
						nextFrame();
						break;
					case 18:
						openedInv.setItem(1, null);
						openedInv.setItem(7, null);
						openedInv.setItem(0, item);
						openedInv.setItem(8, item);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 6f, (float)(frame*0.3));
						nextFrame();
						break;
					case 19:
						openedInv.setItem(0, null);
						openedInv.setItem(8, null);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 6f, (float)(frame*0.3));
						nextFrame();
						break;
					case 20:
						openedInv.setItem(3, null);
						openedInv.setItem(5, null);
						openedInv.setItem(2, item);
						openedInv.setItem(6, item);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 6f, (float)(frame*0.3));
						nextFrame();
						break;
					case 21:
						openedInv.setItem(2, null);
						openedInv.setItem(6, null);
						openedInv.setItem(1, item);
						openedInv.setItem(7, item);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 6f, (float)(frame*0.3));
						nextFrame();
						break;
					case 22:
						openedInv.setItem(1, null);
						openedInv.setItem(7, null);
						openedInv.setItem(0, item);
						openedInv.setItem(8, item);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 6f, (float)(frame*0.3));
						nextFrame();
						break;
					case 23:
						openedInv.setItem(0, null);
						openedInv.setItem(8, null);
						player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 6f, (float)(frame*0.3));
						nextFrame();
						break;
					case 24:
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
			}
		};
		animator.runTaskTimer(SkyBlock.getSB(), 0l, 1l);
	}

	@Override
	public void stop() {
		frame = -1;
		if(animator == null) return;
		else animator.cancel();
		player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 6f, -0.5f);
	}

	@Override
	public void reset() {
		openedInv.setItem(0, null);
		openedInv.setItem(1, null);
		openedInv.setItem(2, null);
		openedInv.setItem(3, null);
		openedInv.setItem(5, null);
		openedInv.setItem(6, null);
		openedInv.setItem(7, null);
		openedInv.setItem(8, null);
	}

	@Override
	public void clickedItem(InventoryClickEvent e, ItemStack item) {
		if(item != null &&
				item.getType() == Material.CHEST &&
				item.hasItemMeta() &&
				item.getItemMeta().hasDisplayName() &&
				item.getItemMeta().getDisplayName().equals(reward_displayname)) {
			SkyCoinHandler.addJoinCoins(player);
			GUIManager.getGUIManager().closeGUI(player);
			player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 6f, -0.5f);
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_COW_BELL, 6f, 1f);
		}
	}
	
}
