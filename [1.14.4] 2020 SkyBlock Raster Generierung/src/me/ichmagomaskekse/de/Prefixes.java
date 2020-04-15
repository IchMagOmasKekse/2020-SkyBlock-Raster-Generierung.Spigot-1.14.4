package me.ichmagomaskekse.de;

public enum Prefixes {
	
	VOTE("§5§lVOTE §8× §7"),
	SERVER("§a§lSKYBLOCK §8× §7"),
	INFO("§e§lINFO §8× §7"),
	BROADCAST("\n§d§lRUNDRUF §8× §7{TEXT}\n "),
	SPACE("\n{TEXT}\n§4"),
	ALERT("§4§lWARNUNG §8× §c"),
	JOIN("§a§l+ §7"),
	QUIT("§c§l- §7"),
	DEATH("§c§lTOD §8× §e"),
	REQUEST("§3§lANFRAGE §8× §b");
	
	
	String prefix = "";
	
	Prefixes(String prefix) {
		this.prefix = prefix;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
}
