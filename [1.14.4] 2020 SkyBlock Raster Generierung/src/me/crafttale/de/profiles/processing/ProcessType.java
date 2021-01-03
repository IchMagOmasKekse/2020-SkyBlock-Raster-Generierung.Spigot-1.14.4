package me.crafttale.de.profiles.processing;

public enum ProcessType {
	
	SHOW_NEW_DAW_NOTIFICATION("new_day_notify");
	
	ProcessType(String codename) {
		this.codename = codename;
	}
	
	private String codename = "";
	
	public String getCodename() {
		return codename;
	}
	
}