package me.crafttale.de.particle;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;

public class ParticleManager {
	
	/**
	 * Spawnt Particle, die für alle sichtbar sind, die Particle in den SkyBlock Settings aktiviert haben
	 * @param particle
	 * @param loc
	 */
	public static void sendParticleToAll(Particle particle, Location loc) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(particle == Particle.REDSTONE) {
				DustOptions dustOptions = new DustOptions(Color.fromRGB(0, 127, 255), 1);
				p.spawnParticle(Particle.REDSTONE, loc, 50, dustOptions);				
			}else {
				p.spawnParticle(particle, loc, 1, 0, 0, 0, 0);
			}
//			if((boolean)PlayerSettings.getConfirmation(ProcessType.SHOW_PARTICLE, p)) {				
//			}
		}
	}
	
	public static void sendParticle(Player p, Particle particle, Location loc) {
		if(particle == Particle.REDSTONE) {
			DustOptions dustOptions = new DustOptions(Color.fromRGB(0, 127, 255), 1);
			p.spawnParticle(Particle.REDSTONE, loc, 50, dustOptions);				
		}else {
			p.spawnParticle(particle, loc, 1, 0, 0, 0, 0);
		}
	}
	public static void sendParticle(Player p, Particle particle, Location loc, int r, int g, int b) {
		if(particle == Particle.REDSTONE) {
			DustOptions dustOptions = new DustOptions(Color.fromRGB(r, g, b), 1);
			p.spawnParticle(Particle.REDSTONE, loc, 50, dustOptions);				
		}else {
			p.spawnParticle(particle, loc, 1, 0, 0, 0, 0);
		}
	}
}
