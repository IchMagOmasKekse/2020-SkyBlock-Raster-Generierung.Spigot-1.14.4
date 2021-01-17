package me.crafttale.de.casino;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.crafttale.de.Chat.MessageType;
import me.crafttale.de.SkyBlock;
import me.crafttale.de.casino.CasinoAlgorithm.ContentType;
import me.crafttale.de.casino.CasinoManager.CasinoType;
import me.crafttale.de.gadgets.lobby.Spawn;
import me.crafttale.de.log.XLogger;
import me.crafttale.de.log.XLogger.LogType;

public class CasinoTypeA extends Casino {
	
	private HashMap<Player, BukkitRunnable> timer = new HashMap<Player, BukkitRunnable>();
	public static String startButtonName = "Sirius";
	
	public CasinoTypeA() {
		super(CasinoType.ArmorStandDisplay.id());
		this.defaultYaw = 90;
		this.defaultPitch = -21;
		lampsAmount = 7;
		lamp = new Block[lampsAmount];
		lamp[0] = new Location(Bukkit.getWorld(Spawn.spawn_world_name), 102, 118, 323).getBlock();
		lamp[1] = new Location(Bukkit.getWorld(Spawn.spawn_world_name), 100, 118, 323).getBlock();
		lamp[2] = new Location(Bukkit.getWorld(Spawn.spawn_world_name), 100, 117, 323).getBlock();
		lamp[3] = new Location(Bukkit.getWorld(Spawn.spawn_world_name), 100, 116, 323).getBlock();
		lamp[4] = new Location(Bukkit.getWorld(Spawn.spawn_world_name), 101, 115, 323).getBlock();
		lamp[5] = new Location(Bukkit.getWorld(Spawn.spawn_world_name), 100, 115, 322).getBlock();
		lamp[6] = new Location(Bukkit.getWorld(Spawn.spawn_world_name), 100, 118, 321).getBlock();
		
		if(armorDisplay1 == null || armorDisplay2 == null || armorDisplay3 == null) {			
			armorDisplay1 = Bukkit.getWorld(Spawn.spawn_world_name).spawn(new Location(Bukkit.getWorld(Spawn.spawn_world_name), 102.5, 115.5, 323.5, -160, 50), ArmorStand.class);
			armorDisplay2 = Bukkit.getWorld(Spawn.spawn_world_name).spawn(new Location(Bukkit.getWorld(Spawn.spawn_world_name), 101.5, 114.5, 322.5, -135, 45), ArmorStand.class);
			armorDisplay3 = Bukkit.getWorld(Spawn.spawn_world_name).spawn(new Location(Bukkit.getWorld(Spawn.spawn_world_name), 100.5, 115.5, 321.5, -110, 48), ArmorStand.class);
			armorDisplay1.setSilent(true);
			armorDisplay2.setSilent(true);
			armorDisplay3.setSilent(true);
			armorDisplay1.setVisible(false);
			armorDisplay2.setVisible(false);
			armorDisplay3.setVisible(false);
			armorDisplay1.setGravity(false);
			armorDisplay2.setGravity(false);
			armorDisplay3.setGravity(false);
			
			armorDisplay1.setCustomName("CASINO");
			armorDisplay2.setCustomName("CASINO");
			armorDisplay3.setCustomName("CASINO");
		}
		startButton = armorDisplay1.getWorld().spawn(new Location(armorDisplay1.getWorld(), 104.5, 115, 322.5), WanderingTrader.class);
		WanderingTrader trader = (WanderingTrader)startButton;
		trader.setAI(false);
		trader.setRemoveWhenFarAway(false);
		trader.setCustomName(startButtonName);
		trader.setCustomNameVisible(true);
		
		rewardSpawnLocation = new Location(armorDisplay1.getWorld(), 102, 115.6, 321);
	}
	
	@Override
	public void disable() {
		reset();
		if(armorDisplay1 != null) armorDisplay1.remove();
		if(armorDisplay2 != null) armorDisplay2.remove();
		if(armorDisplay3 != null) armorDisplay3.remove();
		if(startButton != null) startButton.remove();
	}
	
	@Override
	public void reset() {
		if(armorDisplay1 != null)  armorDisplay1.getEquipment().setHelmet(null);
		if(armorDisplay2 != null) armorDisplay2.getEquipment().setHelmet(null);
		if(armorDisplay3 != null) armorDisplay3.getEquipment().setHelmet(null);
		if(startButton != null) startButton.setRotation(115, 40);
		lightsOFF();
		CasinoManager.remove(spinner);
		if(timer.containsKey(spinner)) {			
			timer.get(spinner).cancel();
			timer.remove(spinner);
		}
		spinner = null;
	}
	
	@Override
	public boolean spin(ContentType type) {
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
						spinner.getWorld().playSound(armorDisplay2.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 6f, 0f);
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
						
						if(spinner == null) SkyBlock.sendMessage(MessageType.WARNING, "spinner == null in id: "+identifier);
						else spinner.getWorld().playSound(armorDisplay2.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 6f, pitch);
						armorDisplayItems(cAlgo.next());
						frame++;
					}else tick++;
				}
				
			}
			
		});
		timer.get(spinner).runTaskTimer(SkyBlock.getSB(), 0l, 1l);
		return false;
	}
	
	
	public void dropItem() {
		if(armorDisplay2.getEquipment().getHelmet() != null && armorDisplay2.getEquipment().getHelmet().getType() != Material.AIR) {			
			rewardItem = armorDisplay2.getWorld().dropItem(new Location(armorDisplay2.getWorld(), 102.5, 116, 321.5), armorDisplay2.getEquipment().getHelmet());
			rewardItem.setVelocity(staticVector);
			rewardItem.setTicksLived(1);
			rewardItem.setOwner(spinner.getUniqueId()); //Hier MUSS die UUID über getUniqueId() gehen, ansonsten kann man das Item nicht aufheben
			rewardItem.setThrower(spinner.getUniqueId()); //Hier MUSS die UUID über getUniqueId() gehen, ansonsten kann man das Item nicht aufheben
			rewardItem.setCustomName("CASINO-A-REWARD"+rewardItem.getCustomName());
		}
	}
	
	public void removeNieten() {
		armorDisplay1.getEquipment().setHelmet(null);
		armorDisplay3.getEquipment().setHelmet(null);
	}

	public void armorDisplayItems(ItemStack item) {
		if(armorDisplay2.getEquipment().getHelmet() != null) {
			armorDisplay1.getEquipment().setItem(EquipmentSlot.HEAD, armorDisplay2.getEquipment().getHelmet());
//			armorDisplay1.getEquipment().setHelmet(armorDisplay2.getEquipment().getHelmet());
		}
		if(armorDisplay3.getEquipment().getHelmet() != null) {
			armorDisplay2.getEquipment().setItem(EquipmentSlot.HEAD, armorDisplay3.getEquipment().getHelmet());
		}
		armorDisplay3.getEquipment().setItem(EquipmentSlot.HEAD, item.clone());
		
		
		armorDisplay1.getWorld().spawnParticle(Particle.FLAME, armorDisplay1.getEyeLocation().getX(),
				armorDisplay1.getEyeLocation().getY()-0.2,
				armorDisplay1.getEyeLocation().getZ(), 1, 0.3, 0.1, 0.3, 0);
		
		armorDisplay2.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, armorDisplay2.getEyeLocation().getX(),
				armorDisplay2.getEyeLocation().getY()-0.2,
				armorDisplay2.getEyeLocation().getZ(), 1, 0.3, 0.1, 0.3, 0);
		
		armorDisplay3.getWorld().spawnParticle(Particle.FLAME, armorDisplay3.getEyeLocation().getX(),
				armorDisplay3.getEyeLocation().getY()-0.2,
				armorDisplay3.getEyeLocation().getZ(), 1, 0.3, 0.1, 0.3, 0);
	}
	
}
