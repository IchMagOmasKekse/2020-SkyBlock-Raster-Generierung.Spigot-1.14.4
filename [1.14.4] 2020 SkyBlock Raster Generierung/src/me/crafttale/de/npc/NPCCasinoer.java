package me.crafttale.de.npc;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import me.crafttale.de.npc.goals.PathfinderGoalGoToCasino;
import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.EntityLiving;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.EntityVillager;
import net.minecraft.server.v1_16_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_16_R3.PathfinderGoalLookAtPlayer;

public class NPCCasinoer extends EntityVillager {

	public final UUID uuid;
	
	public NPCCasinoer(Location loc, Player player) {
		super(EntityTypes.VILLAGER, ((CraftWorld)loc.getWorld()).getHandle())	;
		this.setPositionRaw(loc.getX(), loc.getY(), loc.getZ());
		this.setBaby(true);
		
		this.setInvulnerable(true);
		this.uuid = this.getBukkitEntity().getUniqueId();
		this.setGoalTarget((EntityLiving)((CraftPlayer)player).getHandle(), TargetReason.CUSTOM, true);
		this.setCustomName(new ChatComponentText(player.getName()));
		this.setCustomNameVisible(true);
	}
	
	@Override
	protected void initPathfinder() {
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(1, new PathfinderGoalGoToCasino(this, this.getGoalTarget(), 1.1d, 10f));
		this.goalSelector.a(2, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		
		super.initPathfinder();
	}

}
