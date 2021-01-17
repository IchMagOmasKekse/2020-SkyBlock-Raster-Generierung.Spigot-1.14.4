package me.crafttale.de.npc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Player;

import me.crafttale.de.SkyBlock;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.WorldServer;

public class NPCManager {
	
	private static HashMap<UUID, Entity> registeredNPCs = new HashMap<UUID, Entity>();
	private static ArrayList<String> citizenNames = new ArrayList<String>();
	
	public NPCManager() {
		citizenNames.add("Frank");
		citizenNames.add("Uwe");
		citizenNames.add("Christian");
		citizenNames.add("Ariano");
		citizenNames.add("Nektarios");
		citizenNames.add("Justin");
		citizenNames.add("Tom");
		citizenNames.add("Tim");
		citizenNames.add("Timo");
		citizenNames.add("Timothy");
		citizenNames.add("Anton");
		citizenNames.add("Antoine");
		citizenNames.add("Enton");
	}
	
	public static void unregisterAll() {
		if(registeredNPCs.isEmpty()) return;
		for(Entity ent : registeredNPCs.values()) ent.getBukkitEntity().remove();
	}
	
	public static void registerNewNPC(NPCType type, Location loc, Player p) {
		WorldServer world = null;
		switch(type) {
		case NORMAL_CITIZEN:
			world = ((CraftWorld)loc.getWorld()).getHandle();
			NPCCitizen npc = new NPCCitizen(loc.clone());
			registeredNPCs.put(npc.uuid, npc);
			world.addEntity(npc);
			break;
		case CASINO_SUCHTI:
			world = ((CraftWorld)loc.getWorld()).getHandle();
			NPCCasinoer npc2 = new NPCCasinoer(loc.clone(), p);
			registeredNPCs.put(npc2.uuid, npc2);
			world.addEntity(npc2);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Gibt einen zufälligen Bürgernamen aus einer Liste zurück.
	 * @return
	 */
	public static String getRandomCitizenName() {
		if(citizenNames.isEmpty()) return "No Names Registered";
		else return citizenNames.get(SkyBlock.randomInteger(0, citizenNames.size()-1));
	}
	
}
