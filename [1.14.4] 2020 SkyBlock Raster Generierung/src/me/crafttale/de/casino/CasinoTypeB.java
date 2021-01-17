package me.crafttale.de.casino;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Villager.Type;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.crafttale.de.SkyBlock;
import me.crafttale.de.casino.CasinoAlgorithm.ContentType;
import me.crafttale.de.casino.CasinoManager.CasinoType;
import me.crafttale.de.gadgets.lobby.Spawn;
import me.crafttale.de.log.XLogger;
import me.crafttale.de.log.XLogger.LogType;

public class CasinoTypeB extends Casino {
	
	private HashMap<Player, BukkitRunnable> timer = new HashMap<Player, BukkitRunnable>();
	private long spawnDelay = 1*20;
	public static String startButtonName = "§0Viktor";
	
	public CasinoTypeB() {
		super(CasinoType.ItemFrameDisplay.id());
		this.defaultYaw = -130;
		this.defaultPitch = 0;
		lampsAmount = 3;
		lamp = new Block[lampsAmount];
		lamp[0] = new Location(Bukkit.getWorld(Spawn.spawn_world_name), 124, 121, 274).getBlock();
		lamp[1] = new Location(Bukkit.getWorld(Spawn.spawn_world_name), 123, 121, 274).getBlock();
		lamp[2] = new Location(Bukkit.getWorld(Spawn.spawn_world_name), 122, 121, 274).getBlock();
		
		startButton = Bukkit.getWorld(Spawn.spawn_world_name).spawn(new Location(Bukkit.getWorld(Spawn.spawn_world_name), 121.5, 119, 276.5, -45, 0), Villager.class);
		Villager vil = (Villager)startButton;
		vil.setAI(false);
		vil.setSilent(true);
		vil.setCollidable(false);
		vil.setCanPickupItems(false);
		vil.setProfession(Profession.CARTOGRAPHER);
		vil.setVillagerType(Type.SNOW);
		vil.setCustomName(startButtonName);
		vil.setCustomNameVisible(true);
		
		XLogger.log(LogType.PluginInternProcess, "Init CasinoTypeB. Spawning ItemFrames in "+spawnDelay+" ticks");
		new BukkitRunnable() {
			
			@Override
			public void run() {				
				if(frameDisplay1 == null || frameDisplay2 == null || frameDisplay3 == null) {
					frameDisplay1 = Bukkit.getWorld(Spawn.spawn_world_name).spawn(new Location(Bukkit.getWorld(Spawn.spawn_world_name), 122, 120, 275, -160, 50), ItemFrame.class);
					frameDisplay2 = Bukkit.getWorld(Spawn.spawn_world_name).spawn(new Location(Bukkit.getWorld(Spawn.spawn_world_name), 123, 120, 275, -135, 45), ItemFrame.class);
					frameDisplay3 = Bukkit.getWorld(Spawn.spawn_world_name).spawn(new Location(Bukkit.getWorld(Spawn.spawn_world_name), 124, 120, 275, -110, 48), ItemFrame.class);
					frameDisplay1.setSilent(true);
					frameDisplay2.setSilent(true);
					frameDisplay3.setSilent(true);
					frameDisplay1.setVisible(false);
					frameDisplay2.setVisible(false);
					frameDisplay3.setVisible(false);
					frameDisplay1.setGravity(false);
					frameDisplay2.setGravity(false);
					frameDisplay3.setGravity(false);
					frameDisplay1.setCustomName("CASINO");
					frameDisplay2.setCustomName("CASINO");
					frameDisplay3.setCustomName("CASINO");
					rewardSpawnLocation = new Location(frameDisplay1.getWorld(), 123, 120.1, 277);
					reset();
					isReadyToUse = true;
					XLogger.log(LogType.PluginInternProcess, "Die ItemFrames vom Casino CasinoTypeB wurden gespawnt.");
				}
			}
		}.runTaskLater(SkyBlock.getSB(), spawnDelay);
		
	}
	
	@Override
	public void disable() {
		startButton.remove();
		reset();
		if(frameDisplay1 != null) {
			frameDisplay1.setVisible(true);
			frameDisplay1.remove();
		}
		if(frameDisplay2 != null) {
			frameDisplay2.setVisible(true);
			frameDisplay2.remove();
		}
		if(frameDisplay3 != null) {
			frameDisplay3.setVisible(true);
			frameDisplay3.remove();
		}
	}
	
	@Override
	public void reset() {
		if(frameDisplay1 != null)  frameDisplay1.setItem(placeHolder);
		
		if(frameDisplay2 != null) frameDisplay2.setItem(placeHolder);
		
		if(frameDisplay3 != null) frameDisplay3.setItem(placeHolder);
		CasinoManager.remove(spinner);
		lightsOFF();
		if(timer.containsKey(spinner)) {			
			timer.get(spinner).cancel();
			timer.remove(spinner);
		}
		iterator1 = 0;
		spinner = null;
	}
	
	@Override
	public boolean spin(ContentType type) {
		if(isReadyToUse == false) return false;
		loadContent(type);
		lightsON();
		
		XLogger.log(LogType.PluginInternProcess, "me.crafttale.de.lootchest.LootChest.start(p)");
		timer.put(spinner, new BukkitRunnable() {
			int tick = 0;
			int frame = 0;
			int maxFrames = 20;
			int trigger_1 = 0;
			int trigger_2 = 0;
			int trigger_3 = 0;
			int trigger_4 = 0;
			int speed = 1;
			float pitch = 0f;
			@Override
			public void run() {
				if(frame == 0) {
//					maxFrames = SkyBlock.randomInteger(30, maxFrames);
					trigger_1 = (int)(maxFrames*0.60);
					trigger_2 = (int)(maxFrames*0.79);
					trigger_3 = (int)(maxFrames*0.87);
					trigger_4 = (int)(maxFrames*0.95);
					frame++;
				}else if(frame > maxFrames){
					lightsOFF();
					lightsON(lamp[1]);
					if(frame == (maxFrames+10)) {
						removeNieten();
						frame++;
					}else if(frame == (maxFrames+30)) {
						frame = 0;
						tick = 0;
						pitch = 0f;
						dropItem();
						spinner.getWorld().playSound(frameDisplay2.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 6f, 0f);
						reset();
					}else frame++;
				}else {
					if(tick == speed) {
						tick = 0;
						if(frame == trigger_1) {
							speed++;
							pitch+=0.5f;
						}else if(frame == trigger_2) {
							speed++;
							pitch+=0.5f;
						}else if(frame == trigger_3) {
							speed++;
							pitch+=0.5f;
						}else if(frame == trigger_4) {
							speed++;
							pitch+=0.5f;
						}
						
						spinner.getWorld().playSound(frameDisplay2.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 6f, pitch);
						frameDisplayItems(cAlgo.next());
						frame++;
					}else tick++;
				}
				
			}
			
		});
		timer.get(spinner).runTaskTimer(SkyBlock.getSB(), 0l, 1l);
		return false;
	}
	/**
	 * Droppt das gewonnene Item.
	 * @return
	 */
	public void dropItem() {
		if(frameDisplay2.getItem() != null && frameDisplay2.getItem().getType() != Material.AIR) {			
			rewardItem = frameDisplay2.getWorld().dropItem(new Location(frameDisplay2.getWorld(), 123.5, 120, 277.5), frameDisplay2.getItem());
			rewardItem.setVelocity(staticVector);
			rewardItem.setTicksLived(1);
			rewardItem.setOwner(spinner.getUniqueId()); //Hier MUSS die UUID über getUniqueId() gehen, ansonsten kann man das Item nicht aufheben
			rewardItem.setThrower(spinner.getUniqueId()); //Hier MUSS die UUID über getUniqueId() gehen, ansonsten kann man das Item nicht aufheben
			rewardItem.setCustomName("CASINO-B-REWARD"+rewardItem.getCustomName());
		}
	}
	
	/**
	 * Die Nieten werden entfernt
	 * @return
	 */
	public void removeNieten() {
		frameDisplay1.setItem(null);
		frameDisplay3.setItem(null);
	}

	public void frameDisplayItems(ItemStack item) {
		if(frameDisplay2.getItem() != null) frameDisplay1.setItem(frameDisplay2.getItem());
		if(frameDisplay3.getItem() != null) frameDisplay2.setItem(frameDisplay3.getItem());
		frameDisplay3.setItem(item.clone());
		
		lightsOFF();
		switch(iterator1) {
		case 0:
			lightsON(lamp[0]);
			iterator1 = 1;
			break;
		case 1:
			lightsON(lamp[1]);
			iterator1 = 2;
			break;
		case 2:
			lightsON(lamp[2]);
			iterator1 = 0;
			break;
		}
	}
	
}
