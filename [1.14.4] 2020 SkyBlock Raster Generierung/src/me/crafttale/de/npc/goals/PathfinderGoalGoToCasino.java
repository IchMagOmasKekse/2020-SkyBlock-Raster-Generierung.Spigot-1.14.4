package me.crafttale.de.npc.goals;

import java.util.EnumSet;

import net.minecraft.server.v1_16_R3.EntityCreature;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityLiving;
import net.minecraft.server.v1_16_R3.PathfinderGoal;
import net.minecraft.server.v1_16_R3.RandomPositionGenerator;
import net.minecraft.server.v1_16_R3.Vec3D;

public class PathfinderGoalGoToCasino extends PathfinderGoal {
	
	private EntityInsentient a; //Der Suchti
	private EntityLiving b; //Casino Personal
	
	private final double f; //Villager speed
	private final float g; //Distanz zwischen villager und casino
	
	private double c; //X
	private double d; //Y
	private double e; //Z
	
	public PathfinderGoalGoToCasino(EntityInsentient a, EntityLiving b, double speed, float distance) {
		this.a = a;
		this.b = b;
		this.f = speed;
		this.g = distance;
		this.a(EnumSet.of(Type.MOVE));
	}
	
	
	
	/**
	 * a() Gibt an, ob dieses PathfinderGoal ausgeführt werden soll oder nicht.
	 * a() wird jeden Tick ausgeführt, um zu schauen, ob dieser PathfinderGoal ausgeführt werden soll, oder nicht.
	 */
	@Override
	public boolean a() {
		/*
		 * Wenn a() true returnt, startet das PathfinderGoal
		 */
		this.b = this.a.getGoalTarget();
		if(this.b == null) return false;
		else if(this.a.getDisplayName() == null) return false;
		else if(!(this.a.getDisplayName().toString().contains(this.b.getDisplayName().toString()))) return false;
		else if(this.b.h(this.a) > (double) (this.g * this.g)) {
			// Läuft zum Casino Personal
			a.setPosition(this.b.locX(), this.b.locY(), this.b.locZ());
			return false;
		} else {
			// Casino Personal folgen
			
			Vec3D vec = RandomPositionGenerator.a((EntityCreature)this.a, 16, 7, this.b.getPositionVector());
			if(vec == null) return false;
			
			this.c = this.b.locX(); // X
			this.d = this.b.locY(); // Y
			this.e = this.b.locZ(); // Z
			return true; // Wenn true returnt wird, dann wird c() ausgeführt
		}
	}
	
	/**
	 * c() wird einmal aufgerufen, wenn a() true returnt.
	 * Danach wird c() solange aufgrerufen, wie b() true returnt.
	 */
	public void c() {
		//Updater                 X      Y      Z      Speed
		this.a.getNavigation().a(this.c,this.d,this.e,this.f);
	}
	
	/**
	 * b() wird ausgeführt, wenn c() einmal durch a() ausgeführt wurde.
	 */
	public boolean b() {
		// b() wird nach c() ausgeführt
		// c() wird so lange ausgeführt, wie b true returnt.
		return !this.a.getNavigation().m() && this.b.h(this.a) < (double) (this.g * this.g);
	}
	
	/**
	 * Wird ausgeführt, wenn b() false returnt.
	 */
	public void d() {
		this.b = null;
	}
	
}
