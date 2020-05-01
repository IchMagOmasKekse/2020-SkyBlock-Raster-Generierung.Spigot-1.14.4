package me.dreamisland.de.generators;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import me.dreamisland.de.SkyBlock;

public class CobbleGenerator implements Listener {
	
	private Random r = new Random();
	private ConcurrentHashMap<Material, Double> results = new ConcurrentHashMap<Material, Double>();
	
	public CobbleGenerator() {
		SkyBlock.getInstance().getServer().getPluginManager().registerEvents(this, SkyBlock.getInstance());
//		results.put(Material.STONE, 20d);
//		results.put(Material.COAL_ORE, (12d));
//		results.put(Material.SAND, 10d);
//		results.put(Material.GRAVEL, (5.5d * 9d));
//		results.put(Material.IRON_ORE, (5.5d * 6d));
//		results.put(Material.DIAMOND_ORE, (5.5d * 3d));
//		results.put(Material.DIAMOND_BLOCK, (5.5d * 2.5D));
		results.put(Material.STONE, 20d);
		results.put(Material.COAL_ORE, 12d);
		results.put(Material.SAND, 10d);
		results.put(Material.GRAVEL, 9d);
		results.put(Material.IRON_ORE, 6d);
		results.put(Material.DIAMOND_ORE, 3d);
		results.put(Material.DIAMOND_BLOCK, 2.5D);
	}
	
	
	@EventHandler
	public void onCobble(BlockFromToEvent e) {
	    Material type = e.getBlock().getType();
	    if(type == Material.WATER || type == Material.LAVA){
	        Block b = e.getToBlock();
	        if(b.getType() == Material.AIR){
	            if (generatesCobble(type, b)){
	            	e.setCancelled(true);
	            	e.getToBlock().getWorld().playSound(e.getToBlock().getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1f, 6f);
	            	pickRandomResult(e.getToBlock().getLocation());
	            }
	        }
	    }
	}
	
	private final BlockFace[] faces = new BlockFace[]{
	        BlockFace.SELF,
	        BlockFace.UP,
	        BlockFace.DOWN,
	        BlockFace.NORTH,
	        BlockFace.EAST,
	        BlockFace.SOUTH,
	        BlockFace.WEST
	        };
	
	public boolean generatesCobble(Material type, Block b){
	    Material mirrorID1 = (type == Material.WATER ? Material.LAVA : Material.WATER);
	    for (BlockFace face : faces){
	        Block r = b.getRelative(face, 1);
	        if (r.getType() == mirrorID1){
	            return true;
	        }
	    }
	    return false;
	}
	


	
	public void pickRandomResult(Location loc) {
		
		int p = r.nextInt(100);
		double pick = (p * 0.01d);
		Material mat = Material.AIR;
		double m_p = 0;
		for(Material m : results.keySet()) {
			m_p = (results.get(m) * 0.01d);
			if(pick >= (1-m_p)) {
//				Bukkit.broadcastMessage("Pick: "+pick+" m_p: "+m_p+" = "+m.toString());
				
				mat = m;
			}
			else break;
		}
		
		if(mat == Material.AIR) mat = Material.COBBLESTONE;
		
		loc.getBlock().setType(mat);
	}
	
}
