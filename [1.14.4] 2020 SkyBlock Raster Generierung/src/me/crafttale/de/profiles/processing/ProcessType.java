package me.crafttale.de.profiles.processing;

public enum ProcessType {
	
	SHOW_NEW_DAW_NOTIFICATION("new_day_notify"),
	PLAY_JOIN_MELODY("play_join_melody"),
	ALLOW_CHAT_MESSAGES("allow_chat_sending"),
	SHOW_PARTICLE("show_particles");
	
	ProcessType(String codename) {
		this.codename = codename;
	}
	
	private String codename = "";
	
	public String getCodename() {
		return codename;
	}
	
}