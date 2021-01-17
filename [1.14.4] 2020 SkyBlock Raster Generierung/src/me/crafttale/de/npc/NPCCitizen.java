package me.crafttale.de.npc;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;

import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.EntityVillager;
import net.minecraft.server.v1_16_R3.PathfinderGoalAvoidTarget;
import net.minecraft.server.v1_16_R3.PathfinderGoalPanic;
import net.minecraft.server.v1_16_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_16_R3.PathfinderGoalRandomStroll;

public class NPCCitizen extends EntityVillager {
	
	public final UUID uuid;
	
	public NPCCitizen(Location loc) {
		super(EntityTypes.VILLAGER, ((CraftWorld)loc.getWorld()).getHandle());
		
		this.setPosition(loc.getX(), loc.getY(), loc.getZ());
		if(NPCType.NORMAL_CITIZEN.getName().equals("{RANDOM_NAME}")) this.setCustomName(new ChatComponentText("§7[NPC] "+ChatColor.GOLD+NPCManager.getRandomCitizenName()));
		else this.setCustomName(new ChatComponentText(NPCType.NORMAL_CITIZEN.getName()));
		this.setCustomNameVisible(true);
		this.setHealth(1000f);
		this.uuid = this.getBukkitEntity().getUniqueId();
	}
	
	@Override
	protected void initPathfinder() {
		this.goalSelector.a(0, new PathfinderGoalAvoidTarget<EntityHuman>(this, EntityHuman.class, 1, 10.0d, 10.0d));
		this.goalSelector.a(1, new PathfinderGoalPanic(this, 10.0d));
		this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, 0.6D));
		this.goalSelector.a(3, new PathfinderGoalRandomLookaround(this));
		super.initPathfinder();
	}
	
}
