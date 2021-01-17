package me.crafttale.de.gadgets;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.crafttale.de.SkyBlock;
import me.crafttale.de.SkyWorld;
import me.crafttale.de.profiles.PlayerProfiler;

public class JumpPads {
	
	LinkedList<JumpPad> pads = new LinkedList<JumpPad>();
	
	public JumpPads() {
		// TODO Auto-generated constructor stub
	}
	
	public static void checkJumpPad(Player p) {
		if(PlayerProfiler.isRegistered(p) && PlayerProfiler.getProfile(PlayerProfiler.getUUID(p)).getStandort().getName().toLowerCase().equals("spawn") == false) return;
		Location loc = p.getLocation();
		Block b = p.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY()-1, loc.getBlockZ());
		switch(b.getType()) {
		case EMERALD_BLOCK:
			/* Speed Effekt geben, wenn man auf Smaragdblöcken steht */
			new BukkitRunnable() { @Override public void run() { p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 30*20, 2)); } }.runTask(SkyBlock.getSB());
			break;
		case DIAMOND_BLOCK:
			/* Boost geben, wenn man auf Diamandblöcken steht */
			p.setVelocity(new Vector(6, 2, 0));
			break;
		case REDSTONE_BLOCK:
			/* Boost geben, wenn man auf Diamandblöcken steht */
			p.setVelocity(new Vector(p.getVelocity().getX(), p.getVelocity().getY()+2, p.getVelocity().getZ()));
			break;
		case GOLD_BLOCK:
			/* Boost geben, wenn man auf Diamandblöcken steht */
			p.setVelocity(p.getLocation().getDirection().add(new Vector(0,1,0)));
			break;
			default:
				break;
		}
	}
	
	public abstract class JumpPad {
		
		/*
		 * Diese Klasse wird  noch nicht verwendet
		 */
		
		private String name = "Unnamed";
		private Material material = Material.AIR;
		private Location loc = null;
		
		public JumpPad(String name, Material material, Location loc) {
			this.name = name;
			this.material = material;
			this.loc = loc.clone();
		}

		public abstract void react(Player p);
		
		public boolean canReact(Player p) {
			if(PlayerProfiler.getProfile(p).getStandort().getName().toLowerCase().equals("spawn")) {				
				int px = p.getLocation().getBlockX();
				int py = p.getLocation().getBlockY();
				int pz = p.getLocation().getBlockZ();
				
				if(Bukkit.getWorld(SkyWorld.skyblockworld).getBlockAt(px, py, pz).getType() == material) {
					react(p);
					return true;
				}else return false;
			}else return false;
		}
		
		public String getName() { return name; }
		public Material getMaterial() { return material; }
		public Location getLocation() { return loc.clone(); }
		
	}
	
}
