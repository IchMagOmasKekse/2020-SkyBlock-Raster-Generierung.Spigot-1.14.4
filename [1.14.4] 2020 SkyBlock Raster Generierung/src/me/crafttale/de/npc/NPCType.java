package me.crafttale.de.npc;

public enum NPCType {
	
	NORMAL_CITIZEN("{RANDOM_NAME}"),
	DOLPHIN("{RANDOM_NAME}"),
	CASINO_SUCHTI("Spielsüchtiger");
	
	String name = "";
	
	NPCType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
