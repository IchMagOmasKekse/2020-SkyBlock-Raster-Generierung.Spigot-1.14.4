package me.dreamisland.de;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.dreamisland.de.Chat.MessageType;
import me.dreamisland.de.application.game.SkyBlockAdminTool;
import me.dreamisland.de.area.IslandProtection;
import me.dreamisland.de.area.SpawnToIslandPortal;
import me.dreamisland.de.commands.SchematicCommands;
import me.dreamisland.de.commands.SkyBlockCommands;
import me.dreamisland.de.events.AsyncChatListener;
import me.dreamisland.de.events.BlockDetectorListener;
import me.dreamisland.de.events.JoinAndQuitListener;
import me.dreamisland.de.events.PlayerDamageListener;
import me.dreamisland.de.events.ServerListListener;
import me.dreamisland.de.filemanagement.SkyFileManager;
import me.dreamisland.de.generators.CobbleGeneratorRenewed;
import me.dreamisland.de.generators.SkyWorldGenerator;
import me.dreamisland.de.inventory.ChestContent;
import me.dreamisland.de.profiles.IslandManager;
import me.dreamisland.de.requests.Request.RequestManager;
import me.dreamisland.de.schematics.ChestGenerator;
import me.dreamisland.de.schematics.IslandPaster;
import me.dreamisland.de.schematics.SchematicManager;

public class SkyBlock extends JavaPlugin {
	
	private static SkyBlock instance = null;
	public static SkyBlock getInstance() { return instance; } //Gebe die einzigartige Instanz der Main-Klasse zurck
	public static Location spawn = null;
	public boolean generateNewWorld = false;
	public BukkitRunnable spawn_checker = null;
	public static SkyBlockAdminTool admintool = null;
	
	@Override
	public void onEnable() {
		instance = this;
		preInit(); //Benötigte Inhalte werden geladen
		init(); //Einstellungen werden getätigt
		postInit(); //Listener, Commands, Craftingrezepte und weitere Additionen werden hinzugefgt
		
		for(Player p : Bukkit.getOnlinePlayers()) sendRunningVersion(p);
	}
	
	@Override
	public void onLoad() {
	}
	
	@Override
	public void onDisable() {
		if(spawn_checker != null) spawn_checker.cancel();
	}
	
	/*
	 * TODO: Benötigte Inhalte werden geladen und zum Starten des Plugins breitgestellt
	 */
	public void preInit() {
//		spawn = Bukkit.getWorld("world").getSpawnLocation();
		spawn = new Location(Bukkit.getWorld("world"), 24, 94, 162, 0, 0);
	}
	
	/**
	 * TODO: Alle Einstellungen werdne getätigt
	 * @param
	 */
	public void init() {
		if(Bukkit.getWorld(SkyWorld.skyblockworld) == null) {
			generateSkyBlockWorld();
		}
		
		new SkyFileManager();
		new SkyWorld();//MUSS VOR new SkyBlockgenerator() stehen!!
		new SkyBlockGenerator();
		
		new PlayerAtlas(this);
		
		new ChestContent();
		ChestContent.createDefaultChestContent();
	}
	
	/**
	 * TODO: Listener, Commands, Craftingrezepte und weite Additionen wie das Laden von
	 * Spielerprofilen, Grundstcksprofilen oder Schematiken werden hinzugefgt bzw. durchgefhrt
	 * 
	 * @param
	 */
	public void postInit() {
		
		//Starte den RequestManager
		new RequestManager();
		
		//Registriere Commands
		new SkyBlockCommands();
		new SchematicCommands();
		
		//Registriere Events
		new SchematicManager();
		new BlockDetectorListener();
		new JoinAndQuitListener();
		new ServerListListener();
		new AsyncChatListener();
		new PlayerDamageListener();
		new ChestGenerator();
		new CobbleGeneratorRenewed();
		new IslandProtection();
		
		/* Starte Manager und Handler */
		new IslandManager();
		new IslandPaster();
		new SkyCoinHandler();
		
		//Erstelle Portale
		new SpawnToIslandPortal();
		
		//Erstelle SkyBock Welt
		if(generateNewWorld && new File("plugins/SkyBlock/Islands-Databank.yml").exists() == false) {
			for(Player p : Bukkit.getOnlinePlayers()) p.sendMessage("§eBenutze Algorithmus...");
			SkyBlockGenerator.generateIfReady(10, 10, 10);			
		}
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(p.isOp()) {
				PermissionAttachment att = p.addAttachment(SkyBlock.getInstance());
				att.setPermission("mv.bypass.gamemode.*", true);
			}
		}
		
		
		//Check ob jeder Spieler am Spawn im Adventure Mode ist
		spawn_checker = new BukkitRunnable() {
			
			@Override
			public void run() {
				for(Player p : Bukkit.getWorld("world").getPlayers()) {
					if(p.isOp() == false && p.hasPermission("skyblock.spawn.gamemode.bypass") == false) {						
						if(p.getGameMode() != GameMode.ADVENTURE) p.setGameMode(GameMode.ADVENTURE);
					}
				}
			}
		};
		spawn_checker.runTaskTimer(getInstance(), 0, 20l);
		
	}
	
	public static void sendRunningVersion(CommandSender sender) {
		sender.sendMessage("§aSkyBlock Version §f"+getInstance().getDescription().getVersion()+" §awird betrieben.");
	}
	
	/**
	 * Spawnt eine Feuerwerksrakete an einer Location.
	 * @param location
	 * @param amount
	 * @param trail
	 * @param flicker
	 * @param type
	 * @param color
	 */
    public static void spawnFireworks(Location location, int amount, boolean trail, boolean flicker, Type type, Color... color){
        Location loc = location.clone();
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
       
        Builder effect = null;
        if(color.length == 0) {
        	effect = FireworkEffect.builder()
            		.withColor(Color.AQUA)
            		.withColor(Color.BLACK)
            		.withColor(Color.BLUE)
            		.withColor(Color.FUCHSIA)
            		.withColor(Color.GRAY)
            		.withColor(Color.GREEN)
            		.withColor(Color.LIME)
            		.withColor(Color.MAROON)
            		.withColor(Color.NAVY)
            		.withColor(Color.OLIVE)
            		.withColor(Color.ORANGE)
            		.withColor(Color.PURPLE)
            		.withColor(Color.RED)
            		.withColor(Color.SILVER)
            		.withColor(Color.TEAL)
            		.withColor(Color.WHITE)
            		.withColor(Color.YELLOW);
        }else {        	
        	ArrayList<Color> colors = new ArrayList<Color>();
        	for(Color c : color) {
        		colors.add(c);
        	}
        	effect = FireworkEffect.builder().withColor(colors);
        }
        
        fwm.setPower(0);
        fwm.addEffect(effect.trail(trail).with(type).flicker(flicker).build());
        fwm.setDisplayName("SERVER");
        fw.setFireworkMeta(fwm);
        fw.detonate();
        fw.setCustomName("SERVER");
       
        for(int i = 1;i<amount; i++){
            Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
            fw2.setCustomName("SERVER");
            fw2.setFireworkMeta(fwm);
            fw2.detonate();
        }
    }
    
    /*
     * TODO: Generiert die SkyBlock Welt, in der die Inseln gespawnt werden
     */
    public void generateSkyBlockWorld() {
		SkyWorldGenerator generator = new SkyWorldGenerator();
		WorldCreator cr = new WorldCreator(SkyWorld.skyblockworld);
		cr.generator(generator);
		cr.generateStructures(false);
		for(Player p : Bukkit.getOnlinePlayers()) p.sendMessage("§eSkyWorld Wird generiert...");
		Bukkit.getConsoleSender().sendMessage("§eSkyWorld Wird generiert...");
		Bukkit.createWorld(cr);
		Bukkit.getConsoleSender().sendMessage("§eSkyWorld Wurde generiert!");
		for(Player p : Bukkit.getOnlinePlayers()) p.sendMessage("§eSkyWorld Wurde generiert!");
		generateNewWorld = true;
    }
    
    /**
     * Sendet einen Title an ein Spieler, wenn er keine Permission zur getätigten Handlung hat
     * @param p
     */
    public static void sendNoPermissionTitle(Player p) {
    	p.sendTitle("", "§4§l✖", 2, 10, 1);
    }
    /**
     * Sendet eine Nachricht mit Prefix an alle Operatoren
     * @param strings
     */
    public static void sendOperatorMessage(MessageType type, String... strings) {
    	for(String msg : strings) {
    		for(Player p : Bukkit.getOnlinePlayers()) {
    			if(p.isOp()) p.sendMessage(Prefixes.ALERT.px()+type.getPrefix()+type.getSuffix()+msg);
    		}
    	}
    }
    /**
     * Sendet eine Nachricht mit Prefix an alle Spieler(Broadcast)
     * @param strings
     */
    public static void sendMessage(MessageType type, String... strings) {
    	for(String msg : strings) {
    		for(Player p : Bukkit.getOnlinePlayers()) {
//    			if(p.isOp()) p.sendMessage(Prefixes.SERVER.px()+msg);
    			p.sendMessage(Prefixes.SERVER.px()+type.getPrefix()+type.getSuffix()+msg);
    		}
    	}
    }
    /**
     * Sendet eine Nachricht mit Prefix an die Console
     * @param strings
     */
    public static void sendConsoleMessage(MessageType type, String... strings) {
    	for(String msg : strings) {
//			if(p.isOp()) p.sendMessage(Prefixes.SERVER.px()+msg);
			Bukkit.getConsoleSender().sendMessage(Prefixes.SERVER.px()+type.getPrefix()+type.getSuffix()+msg);
    	}
    }
    /**
     * Sendet eine Nachricht mit Prefix an einen Spieler
     * @param p
     * @param strings
     */
    public static void sendMessage(MessageType type, Player p, String... strings) {
    	for(String msg : strings) {
    		if(p.isOp()) p.sendMessage(Prefixes.SERVER.px()+type.getPrefix()+type.getSuffix()+msg);
    	}
    }
    
    /**
     * Sendet eine Nachricht mit Prefix an Developer(Ariano)
     * @param strings
     */
    public static void sendDeveloperMessage(MessageType type, String... strings) {
    	for(String msg : strings) {
    		for(Player p : Bukkit.getOnlinePlayers()) {
    			if(p.getUniqueId().toString().equals("e93f14bb-71c1-4379-bcf8-6dcc0a409ed9")) p.sendMessage(Prefixes.DEVELOPER.px()+type.getPrefix()+type.getSuffix()+msg);
    		}
    	}
    }
    
	/*
	 * TODO: Benutzerdefinierte Permission-Abfrage
	 * 
	 * - Gibt eine benutzerdefinierte non-permission Message aus.
	 * - Diese Methode spart einige Zeilen an Code.
	 * 
	 */
	public static boolean hasPermission(Player p, String permission) {
		if(p.hasPermission(permission)) return true;
		else {
			sendNoPermissionTitle(p);
			return false;
		}
	}
	
	public static void sendChangelog(Player p) {
		p.sendMessage(" §bALLE NEUERUNGEN AUF EINEM BLICK");
		p.sendMessage(" §aAdded §7/s copy §bDer SchematicManager liest eine Region nun korrekt aus");
		p.sendMessage(" §aAdded §7/s paste §bSchematiken werden nun an einer vom Plugin festgelegten Position gepastet oder an der aktuellen Position des Spieler");
		p.sendMessage(" §aAdded §7/is create §bAnimation enthalten und automatische Überleitung vom Ausfhren des Commands bis zum TP zu deiner Insel.");
		p.sendMessage(" §aAdded §7/is kick §bSpieler, die sich auf deiner Insel befinden, kannst du nun kicken. Diese werden dann zum Spawn teleportiert");
		p.sendMessage(" §aAdded §7/is delete §bLöscht nun auch die gebauten Blöcke");
		p.sendMessage(" §aAdded §7Chest Loader und Chest Saver wurden eingeführt. Bennen eine Chest um zu 'Chest Saver:<Inventar Name>' und klicke damit eine Kiste an, um den Inhalt zu speichern. Bennene eine Kiste um nach 'Chest Loader:<Inventar Name>' und platziere diese Kiste, um ihr Inventar zu laden");
		p.sendMessage(" §aAdded §7Cobble-Generator generiert nicht nur Cobble");
		p.sendMessage(" §aAdded §7/vote §bDas Vote Menü wurde designed und anschaubar gemacht");
		p.sendMessage(" §fIncreased §eSpawn §bDer zukünftige Spawn wurde ein ganzes Stück weiter gebaut");
	}
	
	public static int randomInteger(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}
	
	public static boolean generateNewIndexFile(boolean generate) {
		if(admintool == null) {
			admintool = new SkyBlockAdminTool();
		}else if(SkyBlockAdminTool.isGenerated) {
			admintool = new SkyBlockAdminTool();
		}
		return SkyBlockAdminTool.isGenerated;
	}
	public static boolean generateNewIndexFile(boolean generate, int size) {
		if(admintool == null) {
			admintool = new SkyBlockAdminTool(size);
		}else if(SkyBlockAdminTool.isGenerated) {
			admintool = new SkyBlockAdminTool(size);
		}
		return SkyBlockAdminTool.isGenerated;
	}
	public static boolean generateNewIndexFile(boolean generate, int size, int amount) {
		if(admintool == null) {
			admintool = new SkyBlockAdminTool(size, amount);
		}else if(SkyBlockAdminTool.isGenerated) {
			admintool = new SkyBlockAdminTool(size, amount);
		}
		return SkyBlockAdminTool.isGenerated;
	}
	public static boolean generateNewIndexFile(boolean generate, int size, int amount, int space) {
		if(admintool == null) {
			admintool = new SkyBlockAdminTool(size, amount, space);
		}else if(SkyBlockAdminTool.isGenerated) {
			admintool = new SkyBlockAdminTool(size, amount, space);
		}
		return SkyBlockAdminTool.isGenerated;
	}
	
}
