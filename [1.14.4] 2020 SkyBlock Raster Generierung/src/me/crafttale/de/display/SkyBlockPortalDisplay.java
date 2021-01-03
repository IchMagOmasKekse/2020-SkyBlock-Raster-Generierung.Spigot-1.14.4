package me.crafttale.de.display;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import me.crafttale.de.SkyBlock;

public class SkyBlockPortalDisplay extends Display {

	public SkyBlockPortalDisplay(Location loc) {
		super(SkyBlock.spawn.getWorld().spawn(loc.clone(), ArmorStand.class), "§aZu deiner SkyBlock Insel");
	}

	@Override
	public void update() {
		display.teleport(location);
	}

}
