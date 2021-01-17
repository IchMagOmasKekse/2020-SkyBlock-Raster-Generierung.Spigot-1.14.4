package me.crafttale.de.gui;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.crafttale.de.Chat.MessageType;
import me.crafttale.de.SkyBlock;

public class TravellerGUI extends GUI {

	Endermite particler = null;
	Evoker evo = null;
	Player target = null;
	Item displayItem = null;
	double searchRadiusX = 3, searchRadiusY = 2, searchRadiusZ = 3; //Such Radius für die Spieler Erkunnung in der Nähe des Wächters
	int updateItem = 0; //Cooldown, wann das Item wieder refreshed wird
	String nickname = "Portal Wächter";
	List<String> messages = new LinkedList<String>();
	Vector static_velo = new Vector(0,0,0);
	Location rotationOfEvoker = null;
	
	public TravellerGUI() {
		super(null, GUIType.OWN_CREATION);
		
		messages.add("§f"+nickname+" §9§l> §7Möchtest Du, dass ich dich in's Dorf bringe?");
		messages.add("§f"+nickname+" §9§l> §7Schlage mich, wenn ich Dir das Portal öffnen soll.");
		messages.add("§f"+nickname+" §9§l> §7Pass auf, den meisten Leuten wird während der Teleportation übel...");
		messages.add("§f"+nickname+" §9§l> §7Was darf's sein? Eine Reise zum Dorf etwa?");
		messages.add("§f"+nickname+" §9§l> §7Ich liebe meinen Job..");
	}

	@Override
	public void start() {
		if(loc == null) loc = new Location(Bukkit.getWorld("world"), 107.5, 115, 328.5, 0, 0);
		animator = new BukkitRunnable() {
			
			@Override
			public void run() {				
				if(evo == null && loc != null) {
					if(loc == null) SkyBlock.sendConsoleMessage(MessageType.ERROR,"loc == null");
					if(loc.getWorld() == null) SkyBlock.sendConsoleMessage(MessageType.ERROR,"getWorld == null");
					evo = loc.getWorld().spawn(loc, Evoker.class);
					evo.setAI(false);
					evo.setAware(false);
					evo.setSilent(true);
					evo.setCanJoinRaid(false);
					evo.setArrowCooldown(Integer.MAX_VALUE);
					evo.setArrowsInBody(0);
					evo.setCollidable(false);
					evo.setPatrolLeader(false);
					evo.setCustomName(nickname);
					evo.setCustomNameVisible(true);
					evo.setRemoveWhenFarAway(false);
					evo.getEquipment().setHelmet(null);
					evo.getEquipment().setItemInMainHand(new ItemStack(Material.FEATHER));
					evo.getEquipment().setItemInOffHand(new ItemStack(Material.BOOK));
				}
				if(particler == null) {
					particler = evo.getWorld().spawn(evo.getLocation(), Endermite.class);
					particler.setSilent(true);
					particler.setGravity(false);
					particler.setAI(false);
					particler.setInvisible(true);
					particler.setRemoveWhenFarAway(false);
				}
				if(displayItem == null) {
					displayItem = evo.getWorld().dropItem(evo.getLocation().add(0,2.37,0), new ItemStack(Material.COMPASS));
					displayItem.setVelocity(static_velo);
					displayItem.setGravity(false);
					displayItem.setSilent(true);
					displayItem.setPickupDelay(Integer.MAX_VALUE);
				}
				if(evo != null) {
					if(evo.getNearbyEntities(searchRadiusX, searchRadiusY, searchRadiusZ).contains(target) == false) {
						target = null;
						for(Entity e : evo.getNearbyEntities(searchRadiusX, searchRadiusY, searchRadiusZ)) {
							if(e instanceof Player) {
								target = ((Player)e);
								if(SkyBlock.randomInteger(0, 10) < 5) {
									target.playSound(evo.getEyeLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 6f, -0.5f);
									target.sendMessage(messages.get(SkyBlock.randomInteger(0, messages.size()-1)));
								}
								break;
							}
						}
					}
					if(target != null) setRotationOfEvokerEntity();
					else evo.setRotation((float)(180), (float)(54));
					evo.setTicksLived(1);
				}
				if(displayItem != null && updateItem == 0) {
					displayItem.teleport(evo.getLocation().add(0,2.37,0));
					displayItem.setVelocity(static_velo);
					displayItem.setPickupDelay(Integer.MAX_VALUE);
					displayItem.setTicksLived(1);
					particler.setTicksLived(1);
					updateItem = 40;
				}else if(displayItem != null) updateItem-=1;
			}
		};
		animator.runTaskTimer(SkyBlock.getSB(), 0l, 1l);
	}

	@Override
	public void stop() {
		if(animator != null) animator.cancel();
		if(evo != null) evo.remove();
		if(particler != null) particler.remove();
		if(displayItem != null) displayItem.remove();
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clickedItem(InventoryClickEvent e, ItemStack item) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Dreht das StartButton Entity in die Richtung des nähersten oder zum Spieler, der gerade dreht.
	 * @return
	 */
	public void setRotationOfEvokerEntity() {
		if(evo != null) {
			if(target != null) {			
				Vector vec = target.getLocation().toVector().subtract(evo.getLocation().toVector());
				rotationOfEvoker = evo.getEyeLocation().setDirection(vec);
				
				rotationOfEvoker.setX(evo.getLocation().getX());
				rotationOfEvoker.setY(evo.getLocation().getY());
				rotationOfEvoker.setZ(evo.getLocation().getZ());
				
				evo.teleport(rotationOfEvoker);
			}
		}
	}
	
}
