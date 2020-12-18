package me.dreamisland.de.requests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.dreamisland.de.Prefixes;
import me.dreamisland.de.SkyBlock;

public abstract class Request {
	
	protected int request_id = 0;
	protected Request request = null;
	protected Player sender = null; //Sender der Anfrage
	protected Player receiver = null; //Empfänger der Anfrage
	protected RequestType type = RequestType.UNDEFINED; //Was für eine Anfrage ist das?
	protected boolean hasAccepted = false; //Wurde die Anfrage Akzeptiert?
	protected boolean hasDenied = false; //Wurde die Anfrage abgelehnt?
	protected boolean isExpired = false; //Ist die Anfrage abgelaufen?
	protected int timeLeft = 5*60*20; //In Sekunden angegeben
	private Random r = new Random();
	
	public Request(Player sender, Player receiver, RequestType type) {
		this.sender = sender;
		this.receiver = receiver;
		this.type = type;
		this.request_id = generateRequestid();
	}
	
	public abstract boolean accept();
	public abstract boolean deny();
	public boolean registerRequest(Request request) {
		this.request = request;
		return RequestManager.registerRequest(request);
	}
	public RequestType getRequestType() {
		return type;
	}
	public Player getSender() {
		return sender;
	}
	public Player getReceiver() {
		return receiver;
	}
	public void tick() {
		if(timeLeft == 0) {
			isExpired = true;
			sender.sendMessage("");
			sender.sendMessage(Prefixes.REQUEST.px()+"Deine Anfrage an Â§e"+receiver.getName()+" Â§bist abgelaufen");
			sender.sendMessage("");
			receiver.sendMessage("");
			receiver.sendMessage(Prefixes.REQUEST.px()+"Deine Anfrage von Â§e"+sender.getName()+" Â§bist abgelaufen");
			receiver.sendMessage("");
			RequestManager.removeRequest(request);
		}else {
			timeLeft -= 1;
		}
	}
	public int generateRequestid() {
		return r.nextInt(100000);
	}
	
	public static class RequestManager extends BukkitRunnable {
		
		private static RequestManager man = null;
		private static RequestManager getManager() {return man;}
		private static ArrayList<Request> requests = new ArrayList<Request>();
		private static HashMap<Player, Request> sender_requests = new HashMap<Player, Request>();
		private static HashMap<Player, Request> receiver_requests = new HashMap<Player, Request>();
		
		public RequestManager() {
			man = this;
			this.runTaskTimer(SkyBlock.getInstance(), 0,20);
		}
		
		/*
		 * TODO: Fährt den Manager herunter(Löscht alle Anfragen und stoppt den Timer)
		 */
		public static boolean shutdown() {
			requests.clear();
			getManager().cancel();
			return true;
		}
		
		
		/*
		 * TODO: Gibt die erhaltene Anfrage zurück
		 */
		public static Request getSendRequest(Player p) {
			return sender_requests.get(p);
		}
		
		/*
		 * TODO: Tickt alle Anfragen, damit diese ihre Ablauf Zeit aktualisieren
		 */
		public void tickRequests() {
			for(Request r : requests) {
				if(r!=null)r.tick();
			}
		}
		
		
		/*
		 * TODO: Gibt zurück, ob ein Spieler eine Anfrage gesendet hat
		 */
		public static boolean hasSendRequest(Player p) {
			if(sender_requests.containsKey(p)) return true;
			else return false;
		}
		
		/*
		 * TODO: Gibt die erhaltene Anfrage zurück
		 */
		public static Request getReceivedRequest(Player p) {
			return receiver_requests.get(p);
		}
		
		/*
		 * TODO: Gibt zurück, ob ein Spieler eine Anfrage erhalten hat
		 */
		public static boolean hasReceivedRequest(Player p) {
			if(receiver_requests.containsKey(p)) return true;
			else return false;
		}
		
		/*
		 * TODO: Fügt eine Anfrage zur Warteschlange hinzu
		 */
		public static boolean registerRequest(Request request) {
			if(requests.contains(request)) return false;
			else {
				requests.add(request);
				sender_requests.put(request.sender, request);
				receiver_requests.put(request.receiver, request);
			}
			return true;
		}
		
		/*
		 * TODO: Löscht eine Anfrage aus der Warteschlange
		 */
		public static boolean removeRequest(Request request) {
			if(requests.contains(request)) {
				requests.remove(request);
				sender_requests.remove(request.sender);
				receiver_requests.remove(request.receiver);
			}
			else return false;
			return true;
		}

		@Override
		public void run() {
			tickRequests();
		}
		
	}
	
	public static enum RequestType {
		
		UNDEFINED,
		VISITING,
		INVITING,
		JOINING;
		
	}
	
}
