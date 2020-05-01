package me.dreamisland.de;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.dreamisland.de.application.game.SkyBlockAdminTool;
import me.dreamisland.de.area.BuildProtection;
import me.dreamisland.de.area.SpawnToIslandPortal;
import me.dreamisland.de.commands.SchematicCommands;
import me.dreamisland.de.commands.SkyBlockCommands;
import me.dreamisland.de.events.AsyncChatListener;
import me.dreamisland.de.events.JoinAndQuitListener;
import me.dreamisland.de.events.PlayerDamageListener;
import me.dreamisland.de.events.PlayerRespawnAndDeathListener;
import me.dreamisland.de.events.ServerListListener;
import me.dreamisland.de.filemanagement.SkyFileManager;
import me.dreamisland.de.generators.CobbleGenerator;
import me.dreamisland.de.generators.SkyWorldGenerator;
import me.dreamisland.de.profiles.IslandManager;
import me.dreamisland.de.requests.Request.RequestManager;
import me.dreamisland.de.schematics.ChestGenerator;
import me.dreamisland.de.schematics.SchematicManager;

public class SkyBlock extends JavaPlugin {
	
	private static SkyBlock instance = null;
	public static SkyBlock getInstance() { return instance; } //Gebe die einzigartige Instanz der Main-Klasse zurück
	public static Location spawn = null;
	public boolean generateNewWorld = false;
	public BukkitRunnable spawn_checker = null;
	public static SkyBlockAdminTool admintool = null;
	
	@Override
	public void onEnable() {
		preInit(); //Benötigte Inhalte werden geladen
		init(); //Einstellungen werden getätigt
		postInit(); //Listener, Commands, Craftingrezepte und weitere Additionen werden hinzugefügt
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
		instance = this;
//		spawn = Bukkit.getWorld("world").getSpawnLocation();
		spawn = new Location(Bukkit.getWorld("world"), 79, 116, 313, -90, -32);
	}
	
	/*
	 * TODO: Alle Einstellungen werdne getätigt
	 */
	public void init() {
		if(Bukkit.getWorld("skyblockworld") == null) {
			generateSkyBlockWorld();
		}
		
		new SkyFileManager();
	}
	
	/*
	 * TODO: Listener, Commands, Craftingrezepte und weite Additionen wie das Laden von
	 * Spielerprofilen, Grundstücksprofilen oder Schematiken werden hinzugefügt bzw. durchgeführt
	 */
	public void postInit() {
		
		//Starte den RequestManager
		new RequestManager();
		
		//Registriere Commands
		new SkyBlockCommands();
		new SchematicCommands();
		
		//Registriere Events
		new SchematicManager();
		new JoinAndQuitListener();
		new ServerListListener();
		new PlayerRespawnAndDeathListener();
		new AsyncChatListener();
		new PlayerDamageListener();
		new ChestGenerator();
		new CobbleGenerator();
		new BuildProtection();
		
		//Starte IslandManager
		new IslandManager();
		
		//Erstelle Portale
		new SpawnToIslandPortal();
		
		//Erstelle SkyBock Welt
		if(generateNewWorld && new File("/plugins/SkyBlock/Islands-Databank.yml").exists() == false) {
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
	
	/*
	 * TODO: Spawne Feuerwerksrakete an einer Location
	 */
    public static void spawnFireworks(Location location, int amount, boolean trail, boolean flicker, Type type, Color... color){
        Location loc = location;
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
		WorldCreator cr = new WorldCreator("skyblockworld");
		cr.generator(generator);
		cr.generateStructures(false);
		for(Player p : Bukkit.getOnlinePlayers()) p.sendMessage("§eSkyWorld Wird generiert...");
		Bukkit.getConsoleSender().sendMessage("§eSkyWorld Wird generiert...");
		Bukkit.createWorld(cr);
		Bukkit.getConsoleSender().sendMessage("§eSkyWorld Wurde generiert!");
		for(Player p : Bukkit.getOnlinePlayers()) p.sendMessage("§eSkyWorld Wurde generiert!");
		generateNewWorld = true;
    }
    
    public static void sendOperatorMessage(String... strings) {
    	for(String msg : strings) {
    		for(Player p : Bukkit.getOnlinePlayers()) {
    			if(p.isOp()) p.sendMessage(Prefixes.ALERT.getPrefix()+msg);
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
			p.sendMessage(Prefixes.SERVER.getPrefix()+"§cDu hast kein Recht dazu!");
			return false;
		}
	}
	
	public static void sendChangelog(Player p) {
//		p.sendMessage(" §bALLE NEUERUNGEN AUF EINEM BLICK");
//		p.sendMessage(" §aAdded §7/s copy §bDer SchematicManager liest eine Region nun korrekt aus");
//		p.sendMessage(" §aAdded §7/s paste §bSchematiken werden nun an einer vom Plugin festgelegten Position gepastet oder an der aktuellen Position des Spieler");
//		p.sendMessage(" §aAdded §7/is create §bAnimation enthalten und automatische Überleitung vom Ausführen des Commands bis zum TP zu deiner Insel.");
//		p.sendMessage(" §aAdded §7/is kick §bSpieler, die sich auf deiner Insel befinden, kannst du nun kicken. Diese werden dann zum Spawn teleportiert");
//		p.sendMessage(" §aAdded §7/is delete §bLöscht nun auch die gebauten Blöcke");
//		p.sendMessage(" §aAdded §7Chest Loader und Chest Saver wurden eingeführt. Bennen eine Chest um zu 'Chest Saver:<Inventar Name>' und klicke damit eine Kiste an, um den Inhalt zu speichern. Bennene eine Kiste um nach 'Chest Loader:<Inventar Name>' und platziere diese Kiste, um ihr Inventar zu laden");
//		p.sendMessage(" §aAdded §7Cobble-Generator generiert nicht nur Cobble");
//		p.sendMessage(" §aAdded §7/vote §bDas Vote Menü wurde designed und anschaubar gemacht");
//		p.sendMessage(" §fIncreased §eSpawn §bDer zukünftige Spawn wurde ein ganzes Stück weiter gebaut");
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
