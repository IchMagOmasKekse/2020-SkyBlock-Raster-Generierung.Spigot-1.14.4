package me.crafttale.de.commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.crafttale.de.Chat;
import me.crafttale.de.Chat.MessageType;
import me.crafttale.de.PlayerAtlas;
import me.crafttale.de.Prefixes;
import me.crafttale.de.Settings;
import me.crafttale.de.SkyBlock;
import me.crafttale.de.SkyBlockGenerator;
import me.crafttale.de.economy.EconomyManager;
import me.crafttale.de.economy.EconomyManager.MoneyType;
import me.crafttale.de.economy.PriceSetter;
import me.crafttale.de.filemanagement.SkyFileManager;
import me.crafttale.de.generators.CobbleGeneratorRenewed;
import me.crafttale.de.generators.CobbleGeneratorRenewed.CobblestoneGeneratorFileReader;
import me.crafttale.de.generators.CobbleGeneratorRenewed.GeneratorResultData;
import me.crafttale.de.gui.GUI;
import me.crafttale.de.gui.GUI.GUIType;
import me.crafttale.de.gui.GUIManager;
import me.crafttale.de.gui.defaults.ChestBigGUI;
import me.crafttale.de.gui.defaults.ChestNormalGUI;
import me.crafttale.de.gui.defaults.CraftingGUI;
import me.crafttale.de.gui.defaults.FurnaceGUI;
import me.crafttale.de.gui.defaults.LoadingAnimaGUI;
import me.crafttale.de.gui.island.IslandBanPlayerGUI;
import me.crafttale.de.gui.island.IslandInviteGUI;
import me.crafttale.de.gui.island.IslandPardonPlayerGUI;
import me.crafttale.de.gui.island.IslandSettingsGUI;
import me.crafttale.de.gui.island.IslandVisitGUI;
import me.crafttale.de.gui.island.VisitAcceptedGUI;
import me.crafttale.de.gui.island.VisitDeniedGUI;
import me.crafttale.de.gui.melody.JoinMelodyGUI;
import me.crafttale.de.gui.reward.RewardGUI;
import me.crafttale.de.profiles.IslandManager;
import me.crafttale.de.profiles.IslandManager.IslandData;
import me.crafttale.de.profiles.PlayerProfiler;
import me.crafttale.de.reward.DailyRewardManager;
import me.crafttale.de.tablist.TablistManager;

public class SkyBlockCommands implements CommandExecutor {
	
	public SkyBlockCommands() {
		//Registriere Commands
		SkyBlock.getSB().getCommand("simulate").setExecutor(this);
		SkyBlock.getSB().getCommand("undo").setExecutor(this);
		SkyBlock.getSB().getCommand("is").setExecutor(this);
		SkyBlock.getSB().getCommand("accept").setExecutor(this);
		SkyBlock.getSB().getCommand("deny").setExecutor(this);
		SkyBlock.getSB().getCommand("unclaimed").setExecutor(this);
		SkyBlock.getSB().getCommand("claimed").setExecutor(this);
		SkyBlock.getSB().getCommand("release").setExecutor(this);
		SkyBlock.getSB().getCommand("coins").setExecutor(this);
		SkyBlock.getSB().getCommand("dailyreward").setExecutor(this);
		SkyBlock.getSB().getCommand("gui").setExecutor(this);
		SkyBlock.getSB().getCommand("help").setExecutor(this);
	}
	
	/*
	 * TODO: Command-Execution
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("help")) {
				p.teleport(new Location(Bukkit.getWorld("skyblockworld"), 474.7, 73, 902.5, -90, 0));
//				//TODO: Help Command
//				if(SkyBlock.hasPermission(p, "skyblock.commandhelp")) {	//Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)
//					sendHelp(p);
//				}
			}else if(cmd.getName().equalsIgnoreCase("gui")) {
				//TODO: Gui Command
				if(SkyBlock.hasPermission(p, "skyblock.workbench")) {	//Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
					String s = "";
					for(GUIType type : GUIType.values()) {
						if(s.equals("")) s=type.toString();
						else s = s+"   "+type.toString();
					}
					switch(args.length) {
					case 0:
						SkyBlock.sendMessage(MessageType.INFO, p, "/gui <STOP   "+s+">");
						break;
					case 1:
						if(args[0].equalsIgnoreCase("stop")) {
							GUIManager.closeGUI(p);
						}else if(args[0].equalsIgnoreCase(GUIType.BLANK_WORKBENCH_GUI.toString())) {
							GUI gui = new CraftingGUI(p);
							GUIManager.openGUI(p, gui);
						}else if(args[0].equalsIgnoreCase(GUIType.BLANK_FURNACE_GUI.toString())) {
							GUI gui = new FurnaceGUI(p);
							GUIManager.openGUI(p, gui);
						}else if(args[0].equalsIgnoreCase(GUIType.BLANK_CHEST_BIG_GUI.toString())) {
							GUI gui = new ChestBigGUI(p);
							GUIManager.openGUI(p, gui);
						}else if(args[0].equalsIgnoreCase(GUIType.BLANK_CHEST_NORMAL_GUI.toString())) {
							GUI gui = new ChestNormalGUI(p);
							GUIManager.openGUI(p, gui);
						}else if(args[0].equalsIgnoreCase(GUIType.REWARD_GUI.toString())) {
							GUI gui = new RewardGUI(p);
							GUIManager.openGUI(p, gui);
						}else if(args[0].equalsIgnoreCase(GUIType.JOIN_MELODY.toString())) {
							GUI gui = new JoinMelodyGUI(p);
							GUIManager.openGUI(p, gui);
						}else if(args[0].equalsIgnoreCase(GUIType.ISLAND_VISIT_GUI.toString())) {
							GUI gui = new IslandVisitGUI(p, p);
							GUIManager.openGUI(p, gui);
						}else if(args[0].equalsIgnoreCase(GUIType.VISIT_ACCEPTED_MELODY.toString())) {
							GUI gui = new VisitAcceptedGUI(p);
							GUIManager.openGUI(p, gui);
						}else if(args[0].equalsIgnoreCase(GUIType.VISIT_DENIED_MELODY.toString())) {
							GUI gui = new VisitDeniedGUI(p);
							GUIManager.openGUI(p, gui);
						}else if(args[0].equalsIgnoreCase(GUIType.LOADING_ANIMA.toString())) {
							GUI gui = new LoadingAnimaGUI(p);
							GUIManager.openGUI(p, gui);
						}else if(args[0].equalsIgnoreCase(GUIType.ISLAND_SETTINGS_GUI.toString())) {
							GUI gui = new IslandSettingsGUI(p);
							GUIManager.openGUI(p, gui);
						}else SkyBlock.sendMessage(MessageType.ERROR, p, "GUI �f"+args[0]+" �cnicht gefunden");
						break;
						default:
							SkyBlock.sendMessage(MessageType.INFO, p, "/gui <"+s+">");
							break;
					}
				}
			}else if(cmd.getName().equalsIgnoreCase("dailyreward")) {
				//TODO: Dailyreward Command
				if(SkyBlock.hasPermission(p, "skyblock.dailyreward")) {	//Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
					switch(args.length) {
					case 0:
						if(DailyRewardManager.hasGotDailyReward(p) == false) p.performCommand("gui "+GUIType.REWARD_GUI.toString());
						else SkyBlock.sendMessage(MessageType.INFO, p, "Warte bis "+Settings.daily_Reward_reset_at_hours+
								":"+Settings.daily_Reward_reset_at_minutes+" Uhr, damit Du deine t�gliche Belohnung abholen kannst!");
//						SkyCoinHandler.addJoinCoins(p);
						break;
					}
				}
			}else if(cmd.getName().equalsIgnoreCase("coins")) {
				//TODO: Coins Command
				if(SkyBlock.hasPermission(p, "skyblock.coins")) {	//Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
					switch(args.length) {
					case 0:
						SkyBlock.sendMessage(MessageType.INFO, p, "Du hast "+EconomyManager.getMoney(PlayerProfiler.getUUID(p), MoneyType.MONEY1)+" SkyCoins");
						break;
					case 1:
						if(SkyBlock.hasPermission(p, "skyblock.coins.other")) {
							try {
								Player target = Bukkit.getPlayer(args[0]);
								SkyBlock.sendMessage(MessageType.INFO, p, target.getName()+" hat "+EconomyManager.getMoney(PlayerProfiler.getUUID(target), MoneyType.MONEY1)+" SkyCoins");								
							}catch(Exception ex) {
								UUID uuid = PlayerAtlas.getUUID(args[0]);
								if(uuid != null) {
									String uuid_s = uuid.toString();
									String name = PlayerAtlas.getPlayername(uuid_s);
									SkyBlock.sendMessage(MessageType.INFO, p, name+" hat "+EconomyManager.getMoney(uuid, MoneyType.MONEY1)+" SkyCoins");								
								}else SkyBlock.sendMessage(MessageType.ERROR, p, "Spieler nicht gefunden");
							}
						}
						break;
					}
				}
			}else if(cmd.getName().equalsIgnoreCase("release")) {
				//TODO: Release Command
				if(SkyBlock.hasPermission(p, "skyblock.release")) {	//Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
					switch(args.length) {
					case 0:
						p.sendMessage(" �7� �b/release <Island-ID> �fInsel freigeben");
						break;
					default:
						try { Integer.parseInt(args[0]); } catch(NumberFormatException ex) { SkyBlock.sendMessage(MessageType.ERROR, "Die Seitenanzahl muss kleines als "+Integer.MAX_VALUE+" sein"); return false; }
						try { Integer.parseInt(args[0]); } catch(ClassCastException e) { SkyBlock.sendMessage(MessageType.ERROR, "Die Seitenanzahl muss eine Ziffer/Zahl sein!"); return false; }
						int id = Integer.parseInt(args[0]);
						if(SkyFileManager.releaseIsland(id)) {
							SkyBlock.sendOperatorMessage(MessageType.NONE, p.getName()+
									" hat die Insel "+id+" freigegeben!");
						}else SkyBlock.sendMessage(MessageType.ERROR, p, "Die Insel �f"+id+MessageType.ERROR.getSuffix()+"konnte nicht freigegeben werden!");
							
						break;
					}
				}
			}else if(cmd.getName().equalsIgnoreCase("unclaimed")) {
				//TODO: Unclaimed Command
				if(SkyBlock.hasPermission(p, "skyblock.unclaimed")) {	//Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
					switch(args.length) {
					case 0:
						me.crafttale.de.commands.SkyBlockCommandFunction.sendAllUnclaimedIsland(p, 0);
						break;
					default:
						try { Integer.parseInt(args[0]); } catch(NumberFormatException ex) { SkyBlock.sendMessage(MessageType.ERROR, "Die Seitenanzahl muss kleines als "+Integer.MAX_VALUE+" sein"); return false; }
						try { Integer.parseInt(args[0]); } catch(ClassCastException e) { SkyBlock.sendMessage(MessageType.ERROR, "Die Seitenanzahl muss eine Ziffer/Zahl sein!"); return false; }
						me.crafttale.de.commands.SkyBlockCommandFunction.sendAllUnclaimedIsland(p, Integer.parseInt(args[0]));
						break;
					}
				}
			}else if(cmd.getName().equalsIgnoreCase("claimed")) {
				//TODO: Claimed Command
				if(SkyBlock.hasPermission(p, "skyblock.claimed")) {	//Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
					switch(args.length) {
					case 0:
						me.crafttale.de.commands.SkyBlockCommandFunction.sendAllClaimedIsland(p, 0);
						break;
					default:
						try { Integer.parseInt(args[0]); } catch(NumberFormatException ex) { SkyBlock.sendMessage(MessageType.ERROR, "Die Seitenanzahl muss kleines als "+Integer.MAX_VALUE+" sein"); return false;}
						try { Integer.parseInt(args[0]); } catch(ClassCastException e) { SkyBlock.sendMessage(MessageType.ERROR, "Die Seitenanzahl muss eine Ziffer/Zahl sein!"); }
						me.crafttale.de.commands.SkyBlockCommandFunction.sendAllClaimedIsland(p, Integer.parseInt(args[0]));
						break;
					}
				}
			}else if(cmd.getName().equalsIgnoreCase("simulate")) {
				//TODO: Simualte Command
				if(SkyBlock.hasPermission(p, "skyblock.simulate")) {	//Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
					
					switch(args.length) {
					case 0:
						//Starte den Simulation-Vorgang
						
						if(SkyBlockGenerator.generateIfReady()) {
							p.sendMessage(">>> �bDer Generier-Vorgang f�r die Simulation wird ausgef�hrt...");
						}else p.sendMessage(">>> �cDer Generator ist nicht bereit f�r die Simulation. Probiere es gleich erneut.");
						break;
					case 1:
						//Starte den Simulation-Vorgang
						
						if(SkyBlockGenerator.generateIfReady(Integer.valueOf(args[0]))) {
							p.sendMessage(">>> �bDer Generier-Vorgang f�r die Simulation wird ausgef�hrt...");
							p.sendMessage(">>> �eParameter: [�fAmountOfIsland=�7"+args[0]+"�f, IslandSize=�7Previous�f, SpaceBetweenIslands=�7Previous�e]");
						}else p.sendMessage(">>> �cDer Generator ist nicht bereit f�r die Simulation. Probiere es gleich erneut.");
						break;
					case 2:
						//Starte den Simulation-Vorgang
						
						if(SkyBlockGenerator.generateIfReady(Integer.valueOf(args[0]), Integer.valueOf(args[1]))) {
							p.sendMessage(">>> �bDer Generier-Vorgang f�r die Simulation wird ausgef�hrt...");
							p.sendMessage(">>> �eParameter: [�fAmountOfIsland=�7"+args[0]+"�f, IslandSize=�7"+args[1]+"�f, SpaceBetweenIslands=�7Previous�e]");
						}else p.sendMessage(">>> �cDer Generator ist nicht bereit f�r die Simulation. Probiere es gleich erneut.");
						break;
					case 3:
						//Starte den Simulation-Vorgang
						
						if(SkyBlockGenerator.generateIfReady(Integer.valueOf(args[0]), Integer.valueOf(args[1]), Integer.valueOf(args[2]))) {
							p.sendMessage(">>> �bDer Generier-Vorgang f�r die Simulation wird ausgef�hrt...");
							p.sendMessage(">>> �eParameter: [�fAmountOfIsland=�7"+args[0]+"�f, IslandSize=�7"+args[1]+"�f, SpaceBetweenIslands=�7"+args[2]+"�e]");
						}else p.sendMessage(">>> �cDer Generator ist nicht bereit f�r die Simulation. Probiere es gleich erneut.");
						break;
					}
				}
			}else if(cmd.getName().equalsIgnoreCase("undo")) {
				//TODO: Undo Command
				if(SkyBlock.hasPermission(p, "skyblock.undo")) {	//Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
					SkyBlockGenerator.undo();
					p.sendMessage(">>> �aDie Simulationen wurden r�ckg�ngig gemacht");
				}
			}else if(cmd.getName().equalsIgnoreCase("is")) {
				//TODO: Island Command
				int size = 0;
				int amount = 0;
				int space = 0;
				switch(args.length) {
				case 0:
					me.crafttale.de.commands.SkyBlockCommandFunction.teleportToIsland(p);
					break;

				case 1:
					if(args[0].equalsIgnoreCase("info") && SkyBlock.hasPermission(p, "skyblock.island.info")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						if(PlayerProfiler.getProfile(p).getStandort().getIslandData() != null) {
							IslandData data = PlayerProfiler.getProfile(p).getStandort().getIslandData();
							SkyBlock.sendMessage(MessageType.NONE, p, "�7Informationen �ber Insel-"+data.getIslandId());
							SkyBlock.sendMessage(MessageType.NONE, p, " �7� �fBesitzer: �3"+(data.hasOwner() ? PlayerAtlas.getPlayername(data.getOwnerUuid().toString()) : (SkyFileManager.getIslandStatus(data.getIslandId()).needRelease() ? "�oBraucht bereinigung" : "�oKeiner")));
							SkyBlock.sendMessage(MessageType.NONE, p, " �7� �fGebannte Spieler: �3"+(data.getSettings() == null ? "�oKeine" : (data.getSettings().getBannedPlayers().isEmpty() ? "�oKeine" : data.getSettings().getStringListOfBannedPlayer())));
							SkyBlock.sendMessage(MessageType.NONE, p, " �7� �fMember: �3"+(data.getSettings() == null ? "�oKeiner" : (SkyFileManager.getMembers(data.getOwnerUuid().toString()).isEmpty() ? "�oKeine" : SkyFileManager.getStringListOfMembers(data.getOwnerUuid().toString()))));
						}else {
							SkyBlock.sendMessage(MessageType.ERROR, p, "Du musst dich auf einer Insel befinden, um Informationen abrufen zu k�nnen.");
						}
					}else if(args[0].equalsIgnoreCase("properties") && SkyBlock.hasPermission(p, "skyblock.island.properties")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						if(SkyFileManager.hasIsland(p) == false) SkyBlock.sendMessage(MessageType.INFO, "Du musst eine eigene Insel besitzen, um Eigenschaften �ndern zu k�nnen.");
						else GUIManager.openGUI(p, new IslandSettingsGUI(p));
					}else if(args[0].equalsIgnoreCase("setspawn") && SkyBlock.hasPermission(p, "skyblock.island.setspawn")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						if(SkyFileManager.setPlayerDefinedIslandSpawn(p, p.getLocation())) SkyBlock.sendMessage(MessageType.INFO, "Neuer Insel-Spawn gesetzt!");
						else SkyBlock.sendMessage(MessageType.ERROR, "Neuer Insel-Spawn konnte nicht gesetzt werden!");
					}else if(args[0].equalsIgnoreCase("reloadcobble") && SkyBlock.hasPermission(p, "skyblock.island.reloadcobble")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						CobbleGeneratorRenewed.reload();
						SkyBlock.sendMessage(MessageType.INFO, "Cobblegenerator Daten neugeladen!");
					}else if(args[0].equalsIgnoreCase("reloadshop") && SkyBlock.hasPermission(p, "skyblock.island.reloadshop")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						PriceSetter.loadPrices();
						SkyBlock.sendMessage(MessageType.INFO, "Shop wird neugeladen..");
					}else  if(args[0].equalsIgnoreCase("reload") && SkyBlock.hasPermission(p, "skyblock.island.reload")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						Settings.reloadSettings();
						TablistManager.reloadAllPlayers();
						SkyBlock.sendMessage(MessageType.INFO, "Einstellungen werden teilweise neugeladen. Um alle Einstellungen neu zu laden, f�hre ein Reload/Neustart durch.");
					}else if(args[0].equalsIgnoreCase("help") && SkyBlock.hasPermission(p, "skyblock.island.help")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						sendCommandInfo(p, "is");
					}else if(args[0].equalsIgnoreCase("adminhelp") && SkyBlock.hasPermission(p, "skyblock.adminhelp")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						sendAdminHelp(p);
					}else if(args[0].equalsIgnoreCase("cown") && SkyBlock.hasPermission(p, "skyblock.island.cown")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)
						
						if(SkyFileManager.hasIsland(p) == false) {
							if(SkyFileManager.isMemberOfAnIsland(PlayerProfiler.getUUID(p))) {
								
								if(SkyFileManager.removeMember(PlayerProfiler.getUUID(p).toString(), PlayerProfiler.getUUID(p).toString())) {
									Chat.sendHoverableMessage(p, "Du wurdest nun von der Insel Entfernt.", "Deine Items kannst du nur noch erhalten indem du einen Member dieser Insel fragst.\nAnsonsten sind sie nun Eigentum des Besitzers der Insel.", false, true);
								}else Chat.sendHoverableMessage(p, "Du konntest nicht von deiner Insel entfernt werden.", "HOVERMESSAGE = EMPTY.", false, true);
							}
							
							p.performCommand("is create");
						}else p.performCommand("is");
						
						if(SkyFileManager.isMemberOfAnIsland(PlayerProfiler.getUUID(p))) {
							p.sendMessage("Du bist aber immernoch Member=!");
						}
					}else if(args[0].equalsIgnoreCase("create") && SkyBlock.hasPermission(p, "skyblock.island.create")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)
						if(SkyFileManager.isMemberOfAnIsland(PlayerProfiler.getUUID(p))) {
							Chat.sendHoverableMessage(p, "Du bist bereits Member einer Insel.", "Beachte: Du kannst entweder Member einer Insel sein,\noder eine eigene Insel haben.\nAber nicht beides.", false, true);
							Chat.sendClickableMessage(p, "Falls du dennoch eine eigene Insel m�chtest, klicke auf diese Nachricht.", "Insel verlassen und eine eigene erstellen.", "/is cown", false, true);
						}else me.crafttale.de.commands.SkyBlockCommandFunction.createIsland(p);
					}else if(args[0].equalsIgnoreCase("ban") && SkyBlock.hasPermission(p, "skyblock.island.ban")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)
						if(SkyFileManager.isMemberOfAnIsland(PlayerProfiler.getUUID(p))) {
							Chat.sendHoverableMessage(p, "Du kannst diesen Befehl nicht ausf�hren.", "Da du nur Member bist und nicht Besitzer dieser Insel,\ndarfst du niemanden von dieser Insel verbannen.", false, true);
							return false;
						}
						p.sendMessage(" �7� �b/is ban <Player> �fSpieler von der Insel verbannen");
					}else if(args[0].equalsIgnoreCase("pardon") && SkyBlock.hasPermission(p, "skyblock.island.pardon")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)
						if(SkyFileManager.isMemberOfAnIsland(PlayerProfiler.getUUID(p))) {
							Chat.sendHoverableMessage(p, "Du kannst diesen Befehl nicht ausf�hren.", "Da du nur Member bist und nicht Besitzer dieser Insel,\ndarfst du keine Verbannung annullieren.", false, true);
							return false;
						}
						p.sendMessage(" �7� �b/is pardon <Player> �fHebe eine Verbannung auf");
					}else if(args[0].equalsIgnoreCase("delworld") && SkyBlock.hasPermission(p, "skyblock.island.delworld")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						me.crafttale.de.commands.SkyBlockCommandFunction.deleteWorld(p);
					}else if(args[0].equalsIgnoreCase("delete") && SkyBlock.hasPermission(p, "skyblock.island.delete")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)		
						if(SkyFileManager.isMemberOfAnIsland(PlayerProfiler.getUUID(p))) {
							Chat.sendHoverableMessage(p, "Du kannst diesen Befehl nicht ausf�hren.", "Da du nur Member bist und nicht Besitzer dieser Insel,\ndarfst diese Insel nicht l�schen.", false, true);
							return false;
						}
						me.crafttale.de.commands.SkyBlockCommandFunction.deleteIsland(p);
					}else if(args[0].equalsIgnoreCase("kick") && SkyBlock.hasPermission(p, "skyblock.island.kick")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)	
						if(SkyFileManager.isMemberOfAnIsland(PlayerProfiler.getUUID(p))) {
							Chat.sendHoverableMessage(p, "Du kannst diesen Befehl nicht ausf�hren.", "Da du nur Member bist und nicht Besitzer dieser Insel,\ndarfst du keine Spieler von der Insel kicken.", false, true);
							return false;
						}
						sendCommandInfo(p, "is");
					}else if(args[0].equalsIgnoreCase("generatefile") && SkyBlock.hasPermission(p, "skyblock.*")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						p.sendMessage(Prefixes.SERVER.px()+"Generiere �eInsel-Index-File.yml �7mit folgenden Spezifikationen:");
						p.sendMessage(Prefixes.SERVER.px()+" �7� �fIsland-Size: �e"+SkyBlockGenerator.amountOfIslands);
						p.sendMessage(Prefixes.SERVER.px()+" �7� �dIsland-Amount: �e"+SkyBlockGenerator.issize);
						p.sendMessage(Prefixes.SERVER.px()+" �7� �fSpace-Between-Islands: �e"+SkyBlockGenerator.spaceBetweenIslands);
						SkyBlock.generateNewIndexFile(true);
					}else if(args[0].equalsIgnoreCase("addfriend") && SkyBlock.hasPermission(p, "skyblock.island.addfriend")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)		
						if(SkyFileManager.isMemberOfAnIsland(PlayerProfiler.getUUID(p))) {
							Chat.sendHoverableMessage(p, "Du kannst diesen Befehl nicht ausf�hren.", "Da du nur Member bist und nicht Besitzer dieser Insel,\ndarfst du keine weiteren member hinzuf�gen.", false, true);
							return false;
						}
						p.sendMessage(" �7� �b/is addfriend <Player> �fSpieler zur Insel einladen");
					}else if(args[0].equalsIgnoreCase("delfriend") && SkyBlock.hasPermission(p, "skyblock.island.delfriend")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)
						if(SkyFileManager.isMemberOfAnIsland(PlayerProfiler.getUUID(p))) {
							Chat.sendHoverableMessage(p, "Du kannst diesen Befehl nicht ausf�hren.", "Da du nur Member bist und nicht Besitzer dieser Insel,\ndarfst du keine Member entfernen.", false, true);
							return false;
						}
						p.sendMessage(" �7� �b/is delfriend <Player> �fSpieler von Insel entfernen");
					}else if(args[0].equalsIgnoreCase("friends") && SkyBlock.hasPermission(p, "skyblock.island.friends")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						
						if(IslandManager.getProfile(p).getMembers().isEmpty()) p.sendMessage(Prefixes.SERVER.px()+"Du hast keine Member auf deiner Insel");
						else {							
							p.sendMessage(Prefixes.SERVER.px()+"Alle Member deiner Insel:");
							for(String s : IslandManager.getProfile(p).getMembers()) Chat.sendHoverableMessage(p, " �7- �f"+PlayerAtlas.getPlayername(s), "�9"+s, false, true);
						}
						
					}else if(args[0].equalsIgnoreCase("tp") && SkyBlock.hasPermission(p, "skyblock.island.tp")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						p.sendMessage(" �7� �b/is tp <Island-ID> �fZu einer Insel teleportieren");
					}else if(args[0].equalsIgnoreCase("spy") && SkyBlock.hasPermission(p, "skyblock.island.spy")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						p.sendMessage(" �7� �b/is spy <Island-ID> �fEine Insel ausspionieren");
					}else sendCommandInfo(p, "is");
					break; 
					
					
					
					
					
					
					
				case 2:
					if(args[0].equalsIgnoreCase("adminhelp") && SkyBlock.hasPermission(p, "skyblock.adminhelp")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)	
						try { Integer.parseInt(args[1]); } catch(NumberFormatException ex) { SkyBlock.sendMessage(MessageType.ERROR, "Die Ablauf-Nummer muss kleines als "+Integer.MAX_VALUE+" sein"); return false; }
						try { Integer.parseInt(args[1]); } catch(ClassCastException e) { SkyBlock.sendMessage(MessageType.ERROR, "Die Ablauf-Nummer muss eine Ziffer/Zahl sein!"); return false; }
						int id = Integer.parseInt(args[1]);
						sendAdminHelp(p, id);
					}else if(args[0].equalsIgnoreCase("visit") && SkyBlock.hasPermission(p, "skyblock.island.visit")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)
						Player t = Bukkit.getPlayer(args[1]);
						if(t == null) { 
							SkyBlock.sendMessage(MessageType.INFO, p, "Der Spieler wurde nicht gefunden. Vielleicht ist er nicht online?");
						}else {							
							if(IslandManager.getIslandData(IslandManager.getProfile(t).getIslandID()).getSettings().isAllowVisiting()) me.crafttale.de.commands.SkyBlockCommandFunction.requestVisit(p, args);
							else SkyBlock.sendMessage(MessageType.INFO, p, PlayerProfiler.getCurrentPlayerName(t)+" m�chte momentan keine Besucher haben.");
						}
						
					}else if(args[0].equalsIgnoreCase("kick") && SkyBlock.hasPermission(p, "skyblock.island.kick")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)
						if(SkyFileManager.isMemberOfAnIsland(PlayerProfiler.getUUID(p))) {
							Chat.sendHoverableMessage(p, "Du kannst diesen Befehl nicht ausf�hren.", "Da du nur Member bist und nicht Besitzer dieser Insel,\ndarfst du keine Spieler von der Insel kicken.", false, true);
							return false;
						}
						me.crafttale.de.commands.SkyBlockCommandFunction.kickPlayer(p, args[1]);
					}else if(args[0].equalsIgnoreCase("generatefile") && SkyBlock.hasPermission(p, "skyblock.*")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						try{
							size = Integer.parseInt(args[1]);
							SkyBlock.generateNewIndexFile(true);
							p.sendMessage(Prefixes.SERVER.px()+"Generiere �eInsel-Index-File.yml �7mit folgenden Spezifikationen:");
							p.sendMessage(Prefixes.SERVER.px()+" �7� �fIsland-Size: �e"+size+" Bl�cken");
							p.sendMessage(Prefixes.SERVER.px()+" �7� �dIsland-Amount: �e"+SkyBlockGenerator.issize+" Bl�cken");
							p.sendMessage(Prefixes.SERVER.px()+" �7� �fSpace-Between-Islands: �e"+SkyBlockGenerator.spaceBetweenIslands+" Bl�cken");
						}catch(NumberFormatException ex) {
							p.sendMessage(Prefixes.SERVER.px()+"�cEs d�rfen nur reine Numerische Ziffern als Island-Size angegeben werden");
						}
					}else if(args[0].equalsIgnoreCase("addfriend") && SkyBlock.hasPermission(p, "skyblock.addfriend")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)	
						
						if(SkyFileManager.isMemberOfAnIsland(PlayerProfiler.getUUID(p))) {
							Chat.sendHoverableMessage(p, "Du kannst diesen Befehl nicht ausf�hren.", "Da du nur Member bist und nicht Besitzer dieser Insel,\ndarfst du keine weiteren member hinzuf�gen.", false, true);
							return false;
						}
						if(SkyFileManager.hasIsland(p) == false) {							
							Chat.sendHoverableMessage(p, "Du hast noch gar keine Insel. Nutze /is zum erstellen", "Du musst erst eine Insel erstellen, bevor du einen Member adden kannst. Logisch. Oder?", false, true);
							return false;
						}
						Player target = PlayerProfiler.getPlayer(PlayerAtlas.getUUID(args[1]));
						if(target != null) {
							if(target == p) {
								Chat.sendHoverableMessage(p, "Member adden nicht m�glich.", "Du kannst dich nicht selbst als Member adden,\n�3weil du der Besitzer dieser Insel bist.", false, true);
								return false;
							}
							GUIManager.openGUI(target, new IslandInviteGUI(target, p));
						}else if(PlayerAtlas.getUUID(args[1]) != null && Bukkit.getOfflinePlayer(PlayerAtlas.getUUID(args[1])) != null){
							SkyBlock.sendMessage(MessageType.INFO, "Der Spieler muss online sein, damit du ihn adden kannst");
						}else Chat.sendHoverableMessage(p, "�cDer Spieler wurde nicht gefunden!", "Versuche den Spielernamen korrekt zu schreiben. Ansonsten kann es sein, dass er nicht erkannt wird!", false, true);
						
					}else if(args[0].equalsIgnoreCase("ban") && SkyBlock.hasPermission(p, "skyblock.island.ban")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)
						if(SkyFileManager.isMemberOfAnIsland(PlayerProfiler.getUUID(p))) {
							Chat.sendHoverableMessage(p, "Du kannst diesen Befehl nicht ausf�hren.", "Da du nur Member bist und nicht Besitzer dieser Insel,\ndarfst du niemanden von dieser Insel verbannen.", false, true);
							return false;
						} else {
							if(PlayerAtlas.getPlayername(PlayerAtlas.getUUID(args[1]).toString()).equals(p.getName())) {
								SkyBlock.sendMessage(MessageType.INFO, p, "Du kannst dich nicht selbst verbannen.");
								return false;
							}
							if(PlayerAtlas.getUUID(args[1]) == null) SkyBlock.sendMessage(MessageType.ERROR, p, "Dieser Spieler ist nicht bekannt. Schreibe den Spielernamen bitte korrekt.");
							else if(IslandManager.getIslandData(SkyFileManager.getIslandID(PlayerProfiler.getUUID(p).toString())).getSettings().getBannedPlayers().contains(PlayerAtlas.getUUID(args[1]).toString()) == false) {
								if(GUIManager.openGUI(p, new IslandBanPlayerGUI(p, PlayerAtlas.getUUID(args[1])))) {
									
								}else SkyBlock.sendMessage(MessageType.INFO, p, "Du musst erst den ge�ffneten Dialog beenden um "+PlayerAtlas.getPlayername(PlayerAtlas.getUUID(args[1]).toString())+" verbannen zu k�nnen.");
							}else SkyBlock.sendMessage(MessageType.ERROR, p, "Du hast �f"+PlayerAtlas.getPlayername(PlayerAtlas.getUUID(args[1]).toString())+" �cbereits von deiner Insel verbannt.");
						}
					}else if(args[0].equalsIgnoreCase("pardon") && SkyBlock.hasPermission(p, "skyblock.island.pardon")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)
						if(SkyFileManager.isMemberOfAnIsland(PlayerProfiler.getUUID(p))) {
							Chat.sendHoverableMessage(p, "Du kannst diesen Befehl nicht ausf�hren.", "Da du nur Member bist und nicht Besitzer dieser Insel,\ndarfst du keine Verbannung annullieren.", false, true);
							return false;
						} else {
							if(PlayerAtlas.getPlayername(PlayerAtlas.getUUID(args[1]).toString()).equals(p.getName())) {
								SkyBlock.sendMessage(MessageType.INFO, p, "Da du dich nicht selbst verbannen kannst, kannst Du deine Verbannung auch nicht aufheben ;)");
								return false;
							}
							if(PlayerAtlas.getUUID(args[1]) == null) SkyBlock.sendMessage(MessageType.ERROR, p, "Dieser Spieler ist nicht bekannt. Schreibe den Spielernamen bitte korrekt.");
							else if(IslandManager.getIslandData(SkyFileManager.getIslandID(PlayerProfiler.getUUID(p).toString())).getSettings().getBannedPlayers().contains(PlayerAtlas.getUUID(args[1]).toString())) {
								if(GUIManager.openGUI(p, new IslandPardonPlayerGUI(p, PlayerAtlas.getUUID(args[1])))) {
									
								}else SkyBlock.sendMessage(MessageType.INFO, p, "Du musst erst den ge�ffneten Dialog beenden um "+PlayerAtlas.getPlayername(PlayerAtlas.getUUID(args[1]).toString())+" verbannen zu k�nnen.");
							}else SkyBlock.sendMessage(MessageType.ERROR, p, "�f"+PlayerAtlas.getPlayername(PlayerAtlas.getUUID(args[1]).toString())+" �cist nicht verbannt worden.");
						}
					}else if(args[0].equalsIgnoreCase("delfriend") && SkyBlock.hasPermission(p, "skyblock.delfriend")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						if(SkyFileManager.isMemberOfAnIsland(PlayerProfiler.getUUID(p))) {
							Chat.sendHoverableMessage(p, "Du kannst diesen Befehl nicht ausf�hren.", "Da du nur Member bist und nicht Besitzer dieser Insel,\ndarfst du keine Member entfernen.", false, true);
							return false;
						}
						
						if(IslandManager.getProfile(p).removeMember(PlayerProfiler.getPlayer(PlayerAtlas.getUUID(args[1])))) {
							Chat.sendHoverableMessage(p, "�fMember entfernt!", "�fEntfernt: �a"+args[1], false, true);
							Chat.sendHoverableMessage(PlayerProfiler.getPlayer(PlayerAtlas.getUUID(args[1])), "Du bist nun kein Member mehr auf "+p.getName()+"s Insel.", p.getName()+" hat sich dazu entschlossen, dich von der Insel zu entfernen :/", false, true);
						}else Chat.sendHoverableMessage(p, "�cSpieler ist kein Member!", "�fWom�glich ist dieser Spieler kein Member.", false, true);
						
					}else if(args[0].equalsIgnoreCase("tp") && SkyBlock.hasPermission(p, "skyblock.tp")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						int id = 0;
						try { id = Integer.parseInt(args[1]); } catch(NumberFormatException ex) { SkyBlock.sendMessage(MessageType.ERROR, "Die Seitenanzahl muss kleines als "+Integer.MAX_VALUE+" sein."); return false; }
						try { id = Integer.parseInt(args[1]); } catch(ClassCastException e) { SkyBlock.sendMessage(MessageType.ERROR, "Die Seitenanzahl muss eine Ziffer/Zahl sein!"); }
						
						Location loc = null;
						if(SkyFileManager.getPlayerDefinedIslandSpawn(id) != null) loc = SkyFileManager.getPlayerDefinedIslandSpawn(id);
						else loc = SkyFileManager.getLocationOfIsland(id);
						
						p.teleport(loc);
						Chat.sendHoverableMessage(p, "Du wurdest teleportiert", "Teleportiert zur Insel "+id, false, true);
						
					}else if(args[0].equalsIgnoreCase("spy") && SkyBlock.hasPermission(p, "skyblock.island.spy")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						p.setGameMode(GameMode.SPECTATOR);
						int id = 0;
						try { id = Integer.parseInt(args[1]); } catch(NumberFormatException ex) { SkyBlock.sendMessage(MessageType.ERROR, "Die Island-ID muss kleines als "+Integer.MAX_VALUE+" sein"); return false; }
						try { id = Integer.parseInt(args[1]); } catch(ClassCastException e) { SkyBlock.sendMessage(MessageType.ERROR, "Die Island-ID muss eine Ziffer/Zahl sein!"); }
						p.teleport(SkyFileManager.getLocationOfIsland(id));
						Chat.sendHoverableMessage(p, "�9Spionage-Modus aktiviert.", "Du bist im Spionage-Modus. Sobald du in den Chat schreibst, �ndert sich dein Spielmodus zu Creative und du bist sichtbar", false, true);
						
						
						
					}else sendCommandInfo(p, "is");
					break;
				case 3:
					if(args[0].equalsIgnoreCase("generatefile") && SkyBlock.hasPermission(p, "skyblock.*")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						try{
							size = Integer.parseInt(args[1]);
						}catch(NumberFormatException ex) {
							p.sendMessage(Prefixes.SERVER.px()+"�cEs d�rfen nur reine Numerische Ziffern als Island-Size angegeben werden");
							break;
						}
						try{
							amount = Integer.parseInt(args[2]);
							SkyBlock.generateNewIndexFile(true, size, amount);
							p.sendMessage(Prefixes.SERVER.px()+"Generiere �eInsel-Index-File.yml �7mit folgenden Spezifikationen:");
							p.sendMessage(Prefixes.SERVER.px()+" �7� �fIsland-Size: �e"+size+" Bl�cken");
							p.sendMessage(Prefixes.SERVER.px()+" �7� �dIsland-Amount: �e"+amount+" Bl�cken");
							p.sendMessage(Prefixes.SERVER.px()+" �7� �fSpace-Between-Islands: �e"+SkyBlockGenerator.spaceBetweenIslands+" Bl�cken");
						}catch(NumberFormatException ex) {
							p.sendMessage(Prefixes.SERVER.px()+"�cEs d�rfen nur reine Numerische Ziffern als Island-Amount angegeben werden");
						}
					}
					break;
				case 4:
					if(args[0].equalsIgnoreCase("generatefile") && SkyBlock.hasPermission(p, "skyblock.*")) { //Benutzerdifinierte Permissionabfrage(Siehe unteren Quellcode)				
						try{
							size = Integer.parseInt(args[1]);
						}catch(NumberFormatException ex) {
							p.sendMessage(Prefixes.SERVER.px()+"�cEs d�rfen nur reine Numerische Ziffern als Island-Size angegeben werden");
							break;
						}
						try{
							amount = Integer.parseInt(args[2]);
						}catch(NumberFormatException ex) {
							p.sendMessage(Prefixes.SERVER.px()+"�cEs d�rfen nur reine Numerische Ziffern als Island-Amount angegeben werden");
						}
						try{
							space = Integer.parseInt(args[3]);
							SkyBlock.generateNewIndexFile(true, size, amount, space);
							p.sendMessage(Prefixes.SERVER.px()+"Generiere �eInsel-Index-File.yml �7mit folgenden Spezifikationen:");
							p.sendMessage(Prefixes.SERVER.px()+" �7� �fIsland-Size: �e"+size+" Bl�cken");
							p.sendMessage(Prefixes.SERVER.px()+" �7� �dIsland-Amount: �e"+amount+" Bl�cken");
							p.sendMessage(Prefixes.SERVER.px()+" �7� �fSpace-Between-Islands: �e"+space+" Bl�cken");
						}catch(NumberFormatException ex) {
							p.sendMessage(Prefixes.SERVER.px()+"�cEs d�rfen nur reine Numerische Ziffern als Space-Between-Islands angegeben werden");
						}
					}
					break;
					default:
						sendCommandInfo(p, "is");
						break;
				}
			}else if(cmd.getName().equalsIgnoreCase("accept")) {
				//TODO: Accept Command
				if(SkyBlock.hasPermission(p, "skyblock.accept")) {
					me.crafttale.de.commands.SkyBlockCommandFunction.acceptRequest(p);
				}
			}else if(cmd.getName().equalsIgnoreCase("deny")) {
				//TODO: Deny Command
				if(SkyBlock.hasPermission(p, "skyblock.deny")) {
					me.crafttale.de.commands.SkyBlockCommandFunction.denyRequest(p);
				}
			}
		}else {
//			if(cmd.getName().equals("is")) {
//				switch(args.length) {
//				case 0:
//					readAndSendGeneratorResults(sender);
//					break;					
//				case 1:
//					System.out.println("Picked Material: "+CobbleGeneratorRenewed.pickRandomResult(2, null).toString());
//					break;
//				}
//			}
			sender.sendMessage("�cDer Befehl ist nur  Spieler!");
		}
		return false;
	}
	
	public void sendAdminHelp(Player p) {
		if(p.hasPermission("skyblock.adminhelp")) {
			SkyBlock.sendMessage(MessageType.NONE, p, "�fVerf�gbare Ablauf-Nummern:");
			Chat.sendClickableMessage(p, " - �e0", "Klicke um diesen Ablauf anzusehen", "/is adminhelp 0", false, true);
		}
	}
	public void sendAdminHelp(Player p, int ablauf) {
		//TODO:
		if(p.hasPermission("skyblock.adminhelp")) {
			switch(ablauf) {
			case 0:
				Chat.sendClearMessage(p);
				p.sendMessage("�aAblauf: �eInsel Freigeben");
				p.sendMessage("�e1. Kontrolle: �f/claimed �bKontrolliere, ob eine Insel eine Bereinigung braucht.");
				p.sendMessage(" �bFalls �fJa, �bdann schaue dir den Punkt �e1.1. Bereinigung: �ban.");
				p.sendMessage(" �bFalls �aNein, �bdann schaue dir den Punkt �e2. Freigeben: �ban.");
				p.sendMessage("�e1.1. Bereinigung: �bBeseitige alle Bl�cke auf diesem SkyGrundst�ck.");
				p.sendMessage(" �cACHTUNG �bDie Bereinigung soll lagg-frei passieren.");
				p.sendMessage(" �6A �bBeseitige also erst alle Fl�ssigkeiten und lasse diese auf keinem Fall von der Insel runter flie�en.");
				p.sendMessage(" �6B �bEntleere die Inventare aller Bl�cke wie Kisten, �fen, Dispenser, etc. und zerst�re diese danach.");
				p.sendMessage(" �6C �bBeseitige/t�te alle Entities(Mobs, ItemFrames, Leinen am Zaun, Armorstands, Herumliegende Items, etc.)");
				p.sendMessage(" �6D �bZerst�re jeden einzelnen Block der auf diesem SkyGrundst�ck existiert.");
				p.sendMessage("   �6D.A �bWorldEdit ist gestattet. �CABER �bnur, wenn kleine �nderungen mit WorldEdit get�tigt werden. Lieber kleine mehrere aber daf�r lagg-frei, als wenige gro�e die Laggs verursachen!");
				p.sendMessage("�e2. Freigeben: �b Wenn das SkyGrundst�ck nun nur noch auf Luft besteht, kannst du diesen Befehl ausf�hren:");
				p.sendMessage("   �f/release <Island-ID>");
				p.sendMessage("   �bDie Island-Id erh�lst du mit dem Command �f/is info");
				p.sendMessage("�e3. Freigabe abgeschlossen.");
				Chat.sendClearMessage(p);
				Chat.sendClickableMessage(p, "Ablaufliste �ffnen?", "Klicke um die Ablaufliste anzusehen", "/is adminhelp", false, true);
				break;
				default:
					SkyBlock.sendMessage(MessageType.NONE, p, "�fVerf�gbare Ablauf-Nummern:");
					Chat.sendClickableMessage(p, " - �e0", "Klicke um diesen Ablauf anzusehen", "/is adminhelp 0", false, true);
					break;
			}
		}
	}
	
	
	/*
	 * TODO: sendCommandInfo() sendet Information �ber einen command an einen Spieler
	 */
	public void sendCommandInfo(Player p, String cmd) {
		if(cmd.equalsIgnoreCase("is")) {
			p.sendMessage("");
			if(SkyBlock.hasPermission(p, "skyblock.island")) {
				p.sendMessage("�bSkyBlock Befehlen");
				Chat.sendHoverableCommandHelpMessage(p,                                                     " �7� �b/is �fTeleport zur Insel",                                    "�9Teleportiere dich zu Deiner eigenen Insel oder zu der, auf der Du als Freund hinzugef�gt wurdest.\n�ePermission: �fskyblock.island",                                                                                        true, false);
				if(p.hasPermission("skyblock.adminhelp")) Chat.sendHoverableCommandHelpMessage(p,           " �7� �b/is adminhelp �fHilfe und Abl�ufe f�r Admins",                "�9Hilfe f�r Admins, die einen bestimmten Ablauf nochmal nachlesen wollen, um alles richtig zu machen.\n�ePermission: �fskyblock.adminhelp",                                                                                   true, false);
				if(p.hasPermission("skyblock.island.help")) Chat.sendHoverableCommandHelpMessage(p,         " �7� �b/is help �fHilfe zu SkyBlock Commands",                       "�9Hilfe zu den �f/island �9Befehlen.\n�ePermission: �fskyblock.island.help",                                                                                                                                                  true, false);
				if(p.hasPermission("skyblock.island.info")) Chat.sendHoverableCommandHelpMessage(p,         " �7� �b/is info �fSiehe Informationen einer Insel",                  "�9Lasse dir Informationen �ber die Insel anzeige, auf der du dich gerade befindest.\nOder nutze �f/is info <Insel-ID> �9um die Informationen �ber eine spezifische Insel an zu sehen.\n�ePermission: �fskyblock.island.info", true, false);
				if(p.hasPermission("skyblock.island.create")) Chat.sendHoverableCommandHelpMessage(p,       " �7� �b/is create �fInsel erstellen",                                "�9Erstelle eine Insel, wenn Du noch keine hast und auch kein Member auf einer anderen bist.\n�ePermission: �fskyblock.island.create",                                                                                         true, false);
				if(p.hasPermission("skyblock.island.cown")) Chat.sendHoverableCommandHelpMessage(p,         " �7� �b/is cown �fEigene Insel erstellen",                           "�9Erstelle eine eigene Insel, wenn Du aber noch Member auf einer anderen bist.\n�ePermission: �fskyblock.island.cown",                                                                                                        true, false);
				if(p.hasPermission("skyblock.island.delete")) Chat.sendHoverableCommandHelpMessage(p,       " �7� �b/is delete �fInsel l�schen",                                  "�9L�sche deine eigene Insel\n�ePermission: �fskyblock.island.delete",                                                                                                                                                         true, false);
				if(p.hasPermission("skyblock.island.setspawn")) Chat.sendHoverableCommandHelpMessage(p,     " �7� �b/is setspawn �fInsel-Spawn setzen",                           "�9Setze den Spawn auf deiner Insel.\nDu wirst immer dort spawnen, wenn Du den Befehl �f/island �9nutzt\n�ePermission: �fskyblock.island.setspawn",                                                                            true, false);
				if(p.hasPermission("skyblock.island.visit")) Chat.sendHoverableCommandHelpMessage(p,        " �7� �b/is visit <Player> �fAndere Inseln besuchen",                 "�9Besuche die Insel eines anderen Spielers, sobald er die Anfrage angenommen hat.\n�ePermission: �fskyblock.island.visit",                                                                                                    true, false);
				if(p.hasPermission("skyblock.island.kick")) Chat.sendHoverableCommandHelpMessage(p,         " �7� �b/is kick <Player> �fSpieler von der Insel schmei�en",         "�9Kicke eine Spieler von Deiner Insel.\nDies ist kein Ban und f�hrt lediglich dazu, dass der unerw�nschte Spieler von Deiner Insel zum Spawn teleportiert wird\n�ePermission: �fskyblock.island.kick",                        true, false);
				if(p.hasPermission("skyblock.island.ban")) Chat.sendHoverableCommandHelpMessage(p,          " �7� �b/is ban <Player> �fSpieler von der Insel verbannen",          "�9Verbanne einen Spieler von Deiner Insel. Dieser kann Deine Insel nichtmehr betreten, bis Du die Verbannung mit �f/island pardon �9anulierst.\n�ePermission: �fskyblock.island.ban",                                         true, false);
				if(p.hasPermission("skyblock.island.pardon")) Chat.sendHoverableCommandHelpMessage(p,       " �7� �b/is pardon <Player> �fHebe eine Verbannung auf",              "�9Anuliere eine Verbannung eines Spielers von Deiner Insel.\nDieser Spieler kann danach wieder Deine Insel betreten.\n�ePermission: �fskyblock.island.pardon",                                                                true, false);
				if(p.hasPermission("skyblock.island.properties")) Chat.sendHoverableCommandHelpMessage(p,   " �7� �b/is properties �fBearbeite Insel Eigenschaften",              "�9Mit diesem Befehl kannst Du Eigenschaften deiner Insel bearbeitem.\nZum Beispiel kannst Du PVP auf deiner Insel erlauben oder verbieten.\n�ePermission: �fskyblock.island.properties",                                      true, false);
				if(p.hasPermission("skyblock.island.addfriend")) Chat.sendHoverableCommandHelpMessage(p,    " �7� �b/is addfriend <Player> �fSpieler zur Insel einladen",         "�9F�ge einen Spieler zu Deiner Insel hinzu.\nSo k�nnt Ihr beide zu zweit auf einer Insel spielen.\n�ePermission: �fskyblock.island.addfriend",                                                                                true, false);
				if(p.hasPermission("skyblock.island.delfriend")) Chat.sendHoverableCommandHelpMessage(p,    " �7� �b/is delfriend <Player> �fSpieler von Insel entfernen",        "�9L�sche einen Spieler von Deiner Insel.\nDieser Spieler kann danach nichtmehr bei Dir auf der Insel bauen.\n�ePermission: �fskyblock.island.delfriend",                                                                      true, false);
				if(p.hasPermission("skyblock.island.friends")) Chat.sendHoverableCommandHelpMessage(p,      " �7� �b/is friends <Player> �fSiehe Liste mit Freunden",             "�9Siehe eine Liste mit den Spieler, die Du als Freund zu Deiner Insel hinzugef�gt hast.\nJeder Spieler in dieser Liste hat Bau-Rechte auf Deiner Insel und k�nnte sie griefen.\n�ePermission: �fskyblock.island.friends",     true, false);
				if(p.hasPermission("skyblock.island.tp")) Chat.sendHoverableCommandHelpMessage(p,           " �7� �b/is tp <Island-ID> �fZu einer Insel teleportieren",           "�9Teleportiere dich zu einer Insel, ohne auf die Best�tigung des Insel-Besitzers zu warten.\n�ePermission: �fskyblock.island.tp",                                                                                             true, false);
				if(p.hasPermission("skyblock.island.spy")) Chat.sendHoverableCommandHelpMessage(p,          " �7� �c/is spy <Island-ID> �fEine Insel ausspionieren",              "�9Spioniere eine Insel aus, ohne dass dich der Insel-Besitzer bemerkt.\n�cDieser Befehl steht noch auf der Kippe und wird eventuell aus dem Spiel entfernt.\n�ePermission: �fskyblock.island.spy",                            true, false);
				if(p.hasPermission("skyblock.island.reloadcobble")) Chat.sendHoverableCommandHelpMessage(p, " �7� �b/is reloadcobble �fCobblestone Generator Daten neu laden",    "�9Lade die Daten des Cobblestone Generators neu, um �nderungen der im Generator resultierenden Bl�cke wirksam zu machen.\n�ePermission: �fskyblock.island.reloadcobble",                                                      true, false);
				if(p.hasPermission("skyblock.island.reloadshop")) Chat.sendHoverableCommandHelpMessage(p,   " �7� �b/is reloadshop �fShop Daten neu laden",                       "�9Lade die Daten des Shops Generators neu, um �nderungen der Shop Angebote wirksam zu machen.\n�ePermission: �fskyblock.island.reloadshop",                                                                                   true, false);
				if(p.hasPermission("skyblock.island.reload")) Chat.sendHoverableCommandHelpMessage(p,       " �7� �b/is reload �fEinstellungen neu laden",                        "�9Lade die Einstellungen aus der config.yml neu, um �nderungen des Plugins teilweise wirksam zu machen.\n�ePermission: �fskyblock.island.reload",                                                                             true, false);
			}
			
			if(PlayerProfiler.getUUID(p).toString().equals("e93f14bb-71c1-4379-bcf8-6dcc0a409ed9")) { // IchMagOmasKekse = e93f14bb-71c1-4379-bcf8-6dcc0a409ed9
				Chat.sendClearMessage(p);
				p.sendMessage(" �cDEV-ONLY �7� �b/is generatefile [�eIsland-Amount�b] [�eIsland-Size�b] [�eSpace-Between-Islands�b] �fGeneriere eine �eInsel-Index-File.yml �7Datei."
						+ "Aber Achtung: Das Generieren dieser File �ffnet ein Windows-Fenster welches nur sichtbar ist, wenn du dieses Plugin auf einem Localhost betreibst!");
			}
		}
	}
	public void sendHelp(Player p) {
		if(SkyBlock.hasPermission(p, "skyblock.commandhelp")) {
			p.sendMessage("");
			p.sendMessage("�2Commands -----------------------");
			if(SkyBlock.hasPermission(p, "skyblock.island")) {
				p.sendMessage("�bSkyBlock Befehlen");
				Chat.sendHoverableCommandHelpMessage(p,                                                     " �7� �b/is �fTeleport zur Insel",                                    "�9Teleportiere dich zu Deiner eigenen Insel oder zu der, auf der Du als Freund hinzugef�gt wurdest.\n�ePermission: �fskyblock.island",                                                                                        true, false);
				if(p.hasPermission("skyblock.adminhelp")) Chat.sendHoverableCommandHelpMessage(p,           " �7� �b/is adminhelp �fHilfe und Abl�ufe f�r Admins",                "�9Hilfe f�r Admins, die einen bestimmten Ablauf nochmal nachlesen wollen, um alles richtig zu machen.\n�ePermission: �fskyblock.adminhelp",                                                                                   true, false);
				if(p.hasPermission("skyblock.island.help")) Chat.sendHoverableCommandHelpMessage(p,         " �7� �b/is help �fHilfe zu SkyBlock Commands",                       "�9Hilfe zu den �f/island �9Befehlen.\n�ePermission: �fskyblock.island.help",                                                                                                                                                  true, false);
				if(p.hasPermission("skyblock.island.info")) Chat.sendHoverableCommandHelpMessage(p,         " �7� �b/is info �fSiehe Informationen einer Insel",                  "�9Lasse dir Informationen �ber die Insel anzeige, auf der du dich gerade befindest.\nOder nutze �f/is info <Insel-ID> �9um die Informationen �ber eine spezifische Insel an zu sehen.\n�ePermission: �fskyblock.island.info", true, false);
				if(p.hasPermission("skyblock.island.create")) Chat.sendHoverableCommandHelpMessage(p,       " �7� �b/is create �fInsel erstellen",                                "�9Erstelle eine Insel, wenn Du noch keine hast und auch kein Member auf einer anderen bist.\n�ePermission: �fskyblock.island.create",                                                                                         true, false);
				if(p.hasPermission("skyblock.island.cown")) Chat.sendHoverableCommandHelpMessage(p,         " �7� �b/is cown �fEigene Insel erstellen",                           "�9Erstelle eine eigene Insel, wenn Du aber noch Member auf einer anderen bist.\n�ePermission: �fskyblock.island.cown",                                                                                                        true, false);
				if(p.hasPermission("skyblock.island.delete")) Chat.sendHoverableCommandHelpMessage(p,       " �7� �b/is delete �fInsel l�schen",                                  "�9L�sche deine eigene Insel\n�ePermission: �fskyblock.island.delete",                                                                                                                                                         true, false);
				if(p.hasPermission("skyblock.island.setspawn")) Chat.sendHoverableCommandHelpMessage(p,     " �7� �b/is setspawn �fInsel-Spawn setzen",                           "�9Setze den Spawn auf deiner Insel.\nDu wirst immer dort spawnen, wenn Du den Befehl �f/island �9nutzt\n�ePermission: �fskyblock.island.setspawn",                                                                            true, false);
				if(p.hasPermission("skyblock.island.visit")) Chat.sendHoverableCommandHelpMessage(p,        " �7� �b/is visit <Player> �fAndere Inseln besuchen",                 "�9Besuche die Insel eines anderen Spielers, sobald er die Anfrage angenommen hat.\n�ePermission: �fskyblock.island.visit",                                                                                                    true, false);
				if(p.hasPermission("skyblock.island.kick")) Chat.sendHoverableCommandHelpMessage(p,         " �7� �b/is kick <Player> �fSpieler von der Insel schmei�en",         "�9Kicke eine Spieler von Deiner Insel.\nDies ist kein Ban und f�hrt lediglich dazu, dass der unerw�nschte Spieler von Deiner Insel zum Spawn teleportiert wird\n�ePermission: �fskyblock.island.kick",                        true, false);
				if(p.hasPermission("skyblock.island.ban")) Chat.sendHoverableCommandHelpMessage(p,          " �7� �b/is ban <Player> �fSpieler von der Insel verbannen",          "�9Verbanne einen Spieler von Deiner Insel. Dieser kann Deine Insel nichtmehr betreten, bis Du die Verbannung mit �f/island pardon �9anulierst.\n�ePermission: �fskyblock.island.ban",                                         true, false);
				if(p.hasPermission("skyblock.island.pardon")) Chat.sendHoverableCommandHelpMessage(p,       " �7� �b/is pardon <Player> �fHebe eine Verbannung auf",              "�9Anuliere eine Verbannung eines Spielers von Deiner Insel.\nDieser Spieler kann danach wieder Deine Insel betreten.\n�ePermission: �fskyblock.island.pardon",                                                                true, false);
				if(p.hasPermission("skyblock.island.properties")) Chat.sendHoverableCommandHelpMessage(p,   " �7� �b/is properties �fBearbeite Insel Eigenschaften",              "�9Mit diesem Befehl kannst Du Eigenschaften deiner Insel bearbeitem.\nZum Beispiel kannst Du PVP auf deiner Insel erlauben oder verbieten.\n�ePermission: �fskyblock.island.properties",                                      true, false);
				if(p.hasPermission("skyblock.island.addfriend")) Chat.sendHoverableCommandHelpMessage(p,    " �7� �b/is addfriend <Player> �fSpieler zur Insel einladen",         "�9F�ge einen Spieler zu Deiner Insel hinzu.\nSo k�nnt Ihr beide zu zweit auf einer Insel spielen.\n�ePermission: �fskyblock.island.addfriend",                                                                                true, false);
				if(p.hasPermission("skyblock.island.delfriend")) Chat.sendHoverableCommandHelpMessage(p,    " �7� �b/is delfriend <Player> �fSpieler von Insel entfernen",        "�9L�sche einen Spieler von Deiner Insel.\nDieser Spieler kann danach nichtmehr bei Dir auf der Insel bauen.\n�ePermission: �fskyblock.island.delfriend",                                                                      true, false);
				if(p.hasPermission("skyblock.island.friends")) Chat.sendHoverableCommandHelpMessage(p,      " �7� �b/is friends <Player> �fSiehe Liste mit Freunden",             "�9Siehe eine Liste mit den Spieler, die Du als Freund zu Deiner Insel hinzugef�gt hast.\nJeder Spieler in dieser Liste hat Bau-Rechte auf Deiner Insel und k�nnte sie griefen.\n�ePermission: �fskyblock.island.friends",     true, false);
				if(p.hasPermission("skyblock.island.tp")) Chat.sendHoverableCommandHelpMessage(p,           " �7� �b/is tp <Island-ID> �fZu einer Insel teleportieren",           "�9Teleportiere dich zu einer Insel, ohne auf die Best�tigung des Insel-Besitzers zu warten.\n�ePermission: �fskyblock.island.tp",                                                                                             true, false);
				if(p.hasPermission("skyblock.island.spy")) Chat.sendHoverableCommandHelpMessage(p,          " �7� �c/is spy <Island-ID> �fEine Insel ausspionieren",              "�9Spioniere eine Insel aus, ohne dass dich der Insel-Besitzer bemerkt.\n�cDieser Befehl steht noch auf der Kippe und wird eventuell aus dem Spiel entfernt.\n�ePermission: �fskyblock.island.spy",                            true, false);
				if(p.hasPermission("skyblock.island.reloadcobble")) Chat.sendHoverableCommandHelpMessage(p, " �7� �b/is reloadcobble �fCobblestone Generator Daten neu laden",    "�9Lade die Daten des Cobblestone Generators neu, um �nderungen der im Generator resultierenden Bl�cke wirksam zu machen.\n�ePermission: �fskyblock.island.reloadcobble",                                                      true, false);
				if(p.hasPermission("skyblock.island.reloadshop")) Chat.sendHoverableCommandHelpMessage(p,   " �7� �b/is reloadshop �fShop Daten neu laden",                       "�9Lade die Daten des Shops Generators neu, um �nderungen der Shop Angebote wirksam zu machen.\n�ePermission: �fskyblock.island.reloadshop",                                                                                   true, false);
				if(p.hasPermission("skyblock.island.reload")) Chat.sendHoverableCommandHelpMessage(p,       " �7� �b/is reload �fEinstellungen neu laden",                        "�9Lade die Einstellungen aus der config.yml neu, um �nderungen des Plugins teilweise wirksam zu machen.\n�ePermission: �fskyblock.island.reload",                                                                             true, false);
			}
			p.sendMessage("�eAdmin Befehlen");
			if(p.hasPermission("skyblock.claimed")) Chat.sendHoverableCommandHelpMessage(p,                 " �7� �e/claimed �fAlle geclaimten Inseln",                           "�9Siehe eine Liste der besetzten Insel.\nDabei findest Du dort auch noch den Besitzer und den Status, ob diese Insel eine Bereinigung braucht.\n�ePermission: �fskyblock.claimed",                                                                                                                            true, false);
			if(p.hasPermission("skyblock.unclaimed")) Chat.sendHoverableCommandHelpMessage(p,               " �7� �e/unclaimed �fAlle ungeclaimten Inseln",                       "�9Siehe eine Liste der noch nicht besetzten Insel.\n�ePermission: �fskyblock.unclaimed",                                                                                                                                                                                                                      true, false);
			if(p.hasPermission("skyblock.release")) Chat.sendHoverableCommandHelpMessage(p,                 " �7� �e/release <Island-ID> �fInsel freigeben",                      "�9Gebe eine Insel frei, damit sie ein anderer Spieler claimen kann.\n�cAchtung: �fEs werden alle Operatoren und Spieler mit der Berechtigung via Chat dar�ber informiert, dass Du eine Insel freigegeben hast.\n�4Diesen Vorgang kann man nicht r�ckg�ngig machen!\n�ePermission: �fskyblock.release",        true, false);
			if(p.hasPermission("skyblock.gui")) Chat.sendHoverableCommandHelpMessage(p,                     " �7� �e/gui �f�ffne ein GUI",                                        "�9�ffne einen GUI oder spiele eine Server-Eigene Melody ab.\n�ePermission: �fskyblock.gui",                                                                                                                                                                                                                   true, false);
			
			if(PlayerProfiler.getUUID(p).toString().equals("e93f14bb-71c1-4379-bcf8-6dcc0a409ed9")) { // IchMagOmasKekse = e93f14bb-71c1-4379-bcf8-6dcc0a409ed9
				Chat.sendClearMessage(p);
				p.sendMessage(" �cDEV-ONLY �7� �b/is generatefile [�eIsland-Amount�b] [�eIsland-Size�b] [�eSpace-Between-Islands�b] �fGeneriere eine �eInsel-Index-File.yml �7Datei."
						+ "Aber Achtung: Das Generieren dieser File �ffnet ein Windows-Fenster welches nur sichtbar ist, wenn du dieses Plugin auf einem Localhost betreibst!");
			}
		}
	}
	
	public void readAndSendGeneratorResults(CommandSender sender) {
		ArrayList<GeneratorResultData> results = CobblestoneGeneratorFileReader.getCobbleResultData();
		if(results.isEmpty()) sender.sendMessage("Is Empty");
		else {			
			for(GeneratorResultData d : results) {
				sender.sendMessage("Result: �a"+d.getResult().toString());
				sender.sendMessage("          - �b"+d.getEntries()+" Entries");
				sender.sendMessage("          - �e"+d.getPercentage()+"%");
			}
			String rs = CobbleGeneratorRenewed.results.poll().getResult().toString();
			for( GeneratorResultData data : CobbleGeneratorRenewed.results) {
				rs+= ", "+data.getResult().toString();
			}
			sender.sendMessage("Results registered; "+rs);
		}
	}

}
