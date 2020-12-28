package me.crafttale.de.infotainment;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.crafttale.de.Chat;
import me.crafttale.de.Chat.MessageType;
import me.crafttale.de.SkyBlock;
import net.md_5.bungee.api.ChatColor;

public class Broadcaster {
	
	private boolean enabled = false;
	private boolean pickRandom = false;
	private boolean isStarted = false;
	private int delaySeconds = 0;
	private int delayMinutes = 0;
	private int delayHours = 0;
	private int delayTicksTotal = 0;
	private ArrayList<BroadcastMessage> messages = new ArrayList<BroadcastMessage>();
	
	private BukkitRunnable timer = null; //Asynchron starten!
	
	public Broadcaster() {
		loadSettings();
		if(enabled) startTimer();
	}
	/**
	 * Starte den Broadcaster
	 *  @return
	 */
	public boolean startTimer() {
		if(isStarted == false) {
			isStarted = true;
			timer = new BukkitRunnable() {
				int picked = 0;
				@Override
				public void run() {
					BroadcastMessage bm = null;
					if(pickRandom) {
						bm = messages.get(SkyBlock.randomInteger(0, messages.size()-1));
					}else {
						if(picked >= messages.size()) picked = 0;
						bm = messages.get(picked);
						picked++;
					}
					switch(bm.getType()) {
					case HOVER:
						for(Player p : Bukkit.getOnlinePlayers()) Chat.sendHoverableMessage(p, bm.getText(), bm.getHoverText(), false, false);
						break;
					case CLICK:
						for(Player p : Bukkit.getOnlinePlayers()) Chat.sendClickableMessage(p, bm.getText(), bm.getHoverText(), bm.getCommand(), false, false);
						break;
					case COPY:
						for(Player p : Bukkit.getOnlinePlayers()) Chat.sendCopyableMessage(p, bm.getText(), bm.getHoverText(), bm.getCommand(), false, false);
						break;
					case NORMAL:
						SkyBlock.sendMessage(MessageType.INFO, bm.getText());
						break;
					}
				}
			};
			timer.runTaskTimerAsynchronously(SkyBlock.getSB(), 0l, (long)delayTicksTotal);
			return true;
		}else return false;
	}
	
	/**
	 * Stoppt den Broadcaster
	 *  @return
	 */
	public boolean stop() {
		if(isStarted) {
			isStarted = false;
			timer.cancel();
			return true;
		}
		return false;
	}
	
	/**
	 * L‰dt die Einstellungen in der broadcaster.yml Datei
	 *  @return
	 */
	public void loadSettings() {
		File file = new File("plugins/SkyBlock/broadcaster.yml");
		if(file.exists() == false)SkyBlock.getSB().saveResource("broadcaster.yml", true);
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		enabled = cfg.getBoolean("Broadcaster.enabled");
		pickRandom = cfg.getBoolean("Broadcaster.pick random message");
		delaySeconds = cfg.getInt("Broadcaster.delay.seconds");
		delayMinutes = cfg.getInt("Broadcaster.delay.minutes");
		delayHours = cfg.getInt("Broadcaster.delay.hours");
		messages = readMessages(cfg);
		
		delayTicksTotal = ((delaySeconds * 20)+(delayMinutes * 60 * 20)+(delayHours * 60 * 60 / 20));
	}
	
	/**
	 * Lieﬂt und wertet die Messages aus der broadcaster.yml Datei aus
	 * @param cfg
	 * @return
	 */
	public ArrayList<BroadcastMessage> readMessages(FileConfiguration cfg) {
		
		ArrayList<String> list = (ArrayList<String>) cfg.getStringList("Broadcaster.messages");
		ArrayList<BroadcastMessage> msgs = new ArrayList<BroadcastMessage>();
		
		
		String[] splitter = "".split("");
		BroadcastMessage bm = null;
		for(String s : list) {
			splitter = s.split(":;:");
			bm = new BroadcastMessage();
			if(splitter[0].equals("HOVER")) {
				bm.setType(BroadcastMessageType.HOVER);
				bm.setText(ChatColor.translateAlternateColorCodes('&', splitter[1]));
				bm.setHoverText(ChatColor.translateAlternateColorCodes('&', splitter[2]));
				msgs.add(bm.clone());
				
			}else if(splitter[0].equals("CLICK")) {
				bm.setType(BroadcastMessageType.CLICK);
				bm.setText(ChatColor.translateAlternateColorCodes('&', splitter[1]));
				bm.setHoverText(ChatColor.translateAlternateColorCodes('&', splitter[2]));
				bm.setCommand(splitter[3]);
				msgs.add(bm.clone());
				
			}else if(splitter[0].equals("COPY")) {
				bm.setType(BroadcastMessageType.COPY);
				bm.setText(ChatColor.translateAlternateColorCodes('&', splitter[1]));
				bm.setHoverText(ChatColor.translateAlternateColorCodes('&', splitter[2]));
				bm.setCommand(splitter[3]);
				msgs.add(bm.clone());
				
			}else if(splitter[0].equals("NORMAL")) {
				bm.setType(BroadcastMessageType.NORMAL);
				bm.setText(ChatColor.translateAlternateColorCodes('&', splitter[1]));
				msgs.add(bm.clone());
				
			}else {
				bm = new BroadcastMessage(BroadcastMessageType.NORMAL, "FEHLER BEIM FORMATIEREN", "FEHLER IN DER BROADCASTER.JAVA", "");
				msgs.add(bm.clone());
			}
		}
		
		return msgs;
	}
	
	public class BroadcastMessage {
		
		String msg, hovermsg, command;
		BroadcastMessageType type = BroadcastMessageType.NORMAL;
		
		public BroadcastMessage() {}
		public BroadcastMessage(BroadcastMessageType type, String msg, String hovermsg, String command) {
			this.type = type;
			this.msg = msg;
			this.hovermsg = hovermsg;
			this.command = command;
		}

		public String getText() {
			return msg;
		}

		public void setText(String msg) {
			this.msg = msg;
		}

		public String getHoverText() {
			return hovermsg;
		}

		public void setHoverText(String hovermsg) {
			this.hovermsg = hovermsg;
		}

		public String getCommand() {
			return command;
		}

		public void setCommand(String command) {
			this.command = command;
		}

		public BroadcastMessageType getType() {
			return type;
		}

		public void setType(BroadcastMessageType type) {
			this.type = type;
		}
		
		public BroadcastMessage clone() {
			return new BroadcastMessage(type, msg, hovermsg, command);
		}
		
	}
	
	public enum BroadcastMessageType {
		NORMAL,
		HOVER,
		CLICK,
		COPY;
	}
	
}
