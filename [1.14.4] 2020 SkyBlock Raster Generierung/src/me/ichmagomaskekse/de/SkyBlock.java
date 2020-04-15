package me.ichmagomaskekse.de;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

import me.ichmagomaskekse.de.commands.SkyBlockCommands;
import me.ichmagomaskekse.de.events.AsyncChatListener;
import me.ichmagomaskekse.de.events.JoinAndQuitListener;
import me.ichmagomaskekse.de.events.PlayerDamageListener;
import me.ichmagomaskekse.de.events.PlayerRespawnAndDeathListener;
import me.ichmagomaskekse.de.events.ServerListListener;
import me.ichmagomaskekse.de.filemanagement.SkyFileManager;
import me.ichmagomaskekse.de.generators.SkyWorldGenerator;
import me.ichmagomaskekse.de.requests.Request.RequestManager;

public class SkyBlock extends JavaPlugin {
	
	private static SkyBlock instance = null;
	public static SkyBlock getInstance() { return instance; } //Gebe die einzigartige Instanz der Main-Klasse zurück
	public static Location spawn = null;
	public boolean generateNewWorld = false;
	
	@Override
	public void onEnable() {
		preInit(); //Benötigte Inhalte werden geladen
		init(); //Einstellungen werden getätigt
		postInit(); //Listener, Commands, Craftingrezepte und weitere Additionen werden hinzugefügt
		super.onEnable();
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
	
	/*
	 * TODO: Benötigte Inhalte werden geladen und zum Starten des Plugins breitgestellt
	 */
	public void preInit() {
		instance = this;
		spawn = Bukkit.getWorld("world").getSpawnLocation();
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
	 * 
	 * 
	 */
	public void postInit() {
		//Starte den RequestManager
		new RequestManager();
		
		//Registreire Events
		new JoinAndQuitListener();
		new SkyBlockCommands();
		new ServerListListener();
		new PlayerRespawnAndDeathListener();
		new AsyncChatListener();
		new PlayerDamageListener();
		
		//Erstelle SkyBock Welt
		if(generateNewWorld && new File("/Plugins/SkyBlock/Islands-Databank.yml").exists() == false) {
			for(Player p : Bukkit.getOnlinePlayers()) p.sendMessage("§eBenutze Algorithmus...");
			SkyBlockGenerator.generateIfReady(10, 10, 10);			
		}
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
	
}
