package me.crafttale.de.profiles;

import org.bukkit.entity.Player;

import me.crafttale.de.profiles.processing.ProcessSender;
import me.crafttale.de.profiles.processing.ProcessType;

public class PlayerSettings {
	
	
	public PlayerSettings() {
		
	}
	
	public static void processIfAllowed(ProcessType process, Object obj, Object value, ProcessSender pSender) {
		switch(process) {
		case SHOW_NEW_DAW_NOTIFICATION:
			if(obj instanceof Player) {
				
			}
			break;
		}
	}
	
}
