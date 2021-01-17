package me.crafttale.de.casino;

import java.util.LinkedList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Lightable;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.crafttale.de.casino.CasinoAlgorithm.ContentType;
import me.crafttale.de.casino.CasinoAlgorithm.ItemProbability;
import me.crafttale.de.log.XLogger;
import me.crafttale.de.log.XLogger.LogType;
import me.crafttale.de.profiles.PlayerProfiler;

public abstract class Casino {
	
	protected CasinoAlgorithm cAlgo = null;
	protected ArmorStand armorDisplay1 = null; //ItemDisplay Links
	protected ArmorStand armorDisplay2 = null; //ItemDisplay Mitte
	protected ArmorStand armorDisplay3 = null; //ItemDisplay Rechts
	
	protected ItemFrame frameDisplay1 = null; //ItemDisplay Links
	protected ItemFrame frameDisplay2 = null; //ItemDisplay Mitte
	protected ItemFrame frameDisplay3 = null; //ItemDisplay Rechts
	
	protected Item rewardItem = null;
	protected Location rewardSpawnLocation = null;
	
	/* Variablen für das Behandeln des StartButton Entitys */
	protected LivingEntity startButton = null;
	protected Location rotationOfStartButton = null;
	protected Vector vec = null;
	protected final Vector staticVector = new Vector(0,0,0);
	protected float defaultYaw = 90, defaultPitch = -21;
	
	protected Player spinner = null;
	protected Block[] lamp = null;
	protected int lampsAmount = 0;
	protected int iterator1 = 0;
	protected String identifier = "Casino - Unnamed";
	protected ItemStack placeHolder = null;
	protected boolean isReadyToUse = false;
	
	public Casino(String identifier) {
		
		this.identifier = identifier;
		
		placeHolder = new ItemStack(Material.GLASS_PANE);
	}
	
	public boolean spin(Player p, ContentType type) {
		spinner = p;	
		XLogger.log(LogType.PluginInternProcess, "Spieler "+PlayerProfiler.getCurrentPlayerName(spinner)+" startet Casino mit der ID "+identifier);
		return spin(type);
	}
	public abstract boolean spin(ContentType type);
	public abstract void disable();
	public abstract void reset();
	
	
	private Player nearestPlayer = null;
	/**
	 * Dreht das StartButton Entity in die Richtung des nähersten oder zum Spieler, der gerade dreht.
	 * @return
	 */
	public void setRotationOfStartButtonEntity() {
		if(startButton != null) {
			if(spinner == null) {
				for(Entity ent : startButton.getNearbyEntities(4, 4, 4)) {
					if(ent instanceof Player) {
						nearestPlayer = (Player)ent;
						break;
					}
				}
			}else nearestPlayer = spinner;
			
			if(nearestPlayer == null) {
				startButton.setRotation(defaultYaw, defaultPitch);
			}else {				
				Vector vec = nearestPlayer.getLocation().toVector().subtract(startButton.getLocation().toVector());
				rotationOfStartButton = startButton.getEyeLocation().setDirection(vec);
				
				rotationOfStartButton.setX(startButton.getLocation().getX());
				rotationOfStartButton.setY(startButton.getLocation().getY());
				rotationOfStartButton.setZ(startButton.getLocation().getZ());
				
				startButton.teleport(rotationOfStartButton);
				nearestPlayer = null;
			}
		}
	}
	
	public void loadContent(ContentType type) {
		LinkedList<ItemProbability> items = CasinoManager.getContentByType(type);
		cAlgo = new CasinoAlgorithm(items);
	}
	
	/**
	 * Schaltet alle Lampen an.
	 * @return
	 */
	public void lightsON() {
		for(int i = 0; i != lampsAmount; i++) lightsON(lamp[i]);
	}
	/**
	 * Schaltet eine spezielle Lampen an.
	 * @return
	 */
	public void lightsON(Block lamp) {
		Lightable light = null;
		if(lamp.getBlockData() instanceof Lightable) {				
			light = (Lightable)lamp.getBlockData();
			light.setLit(true);
			lamp.setBlockData(light);
		}
	}
	/**
	 * Schaltet alle Lampen aus.
	 * @return
	 */
	public void lightsOFF() {
		for(int i = 0; i != lampsAmount; i++) lightsOFF(lamp[i]);
	}
	/**
	 * Schaltet eine spezielle Lampen aus.
	 * @return
	 */
	public void lightsOFF(Block lamp) {
		Lightable light = null;
		if(lamp.getBlockData() instanceof Lightable) {				
			light = (Lightable)lamp.getBlockData();
			light.setLit(false);
			lamp.setBlockData(light);
		}
	}
	
	public boolean isInUse() {
		return spinner == null ? false : true;
	}
	
	
}
