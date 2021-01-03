package me.crafttale.de.display;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import me.crafttale.de.SkyBlock;

public class UniversalDisplay extends Display {
	
	public UniversalDisplay(String text, Location loc) {
		super(SkyBlock.spawn.getWorld().spawn(loc.clone(), ArmorStand.class), text);
	}

	@Override
	public void update() {
		this.display.setGravity(false);
		this.display.teleport(location.clone());
		this.display.setVelocity(this.static_velocity);
	}
	
}
