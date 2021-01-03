package me.crafttale.de.gadgets.jumppads;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.crafttale.de.gadgets.JumpPads;
import me.crafttale.de.gadgets.JumpPads.JumpPad;

public class BoostPad extends JumpPad {
/*
 * Diese Klasse wird  noch nicht verwendet
 */
	public BoostPad(JumpPads jumpPads, String name, Material material, Location loc) {
		jumpPads.super(name, material, loc);
	}

	@Override
	public void react(Player p) {
		//Wird noch nicht verwendet
	}

}
