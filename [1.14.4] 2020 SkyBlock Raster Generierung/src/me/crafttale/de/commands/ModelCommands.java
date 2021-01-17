package me.crafttale.de.commands;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.crafttale.de.Chat;
import me.crafttale.de.Chat.MessageType;
import me.crafttale.de.Code;
import me.crafttale.de.SkyBlock;
import me.crafttale.de.log.XLogger;
import me.crafttale.de.log.XLogger.LogType;
import me.crafttale.de.models.LoadReason;
import me.crafttale.de.models.Model;
import me.crafttale.de.models.ModelManager;
import me.crafttale.de.models.SummonReason;

public class ModelCommands implements CommandExecutor {
	
	private String[] command = null; //Ein Array mit allen Commands, die in dieser Klasse verwendet werden.
	
	public ModelCommands() {
		//Initialisiere Commands
		command = new String[1];
		command[0] = "model";
		
		//Registriere Commands
		XLogger.log(LogType.PluginInternProcess, "§eRegistriere Model Commands");
		for(int i = 0; i != command.length; i++) {
			SkyBlock.getSB().getCommand(command[i]).setExecutor(this);
			XLogger.log(LogType.PluginInternProcess, "§aRegistriere Model Befehl: §2"+command[i]);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		
		if(cmd.getName().equals("model") && SkyBlock.hasPermission(sender, "skyblock.model")) {			
			if(sender instanceof Player) {
				Player p = (Player) sender;
				switch(args.length) {
				case 0:
					sendCommandInfo(sender, "model");
					break;
				case 1:
					if(args[0].equalsIgnoreCase("edit")) {
						if(ModelManager.hasSession(p)) {
							ModelManager.closeSession(p);
						} else {
							ModelManager.createNewSession(p);
						}
						
						p.sendMessage("§aEdit-Mode: "+(ModelManager.hasSession(p) ? "§7aktiviert" : "§cdeaktiviert"));
					}else if(args[0].equalsIgnoreCase("info")) {
						if(ModelManager.hasSession(p) && ModelManager.getModelSession(p).hasSelectedModel()) {							
							p.sendMessage("§aArmorstand Infos:");
							p.sendMessage("  §7Selektiertes Model: §f"+ModelManager.getModelSession(p).getModel().getModelName());
							p.sendMessage("  §7Location X: §f"+ModelManager.getModelSession(p).getModel().getArmorStand().getLocation().getX());
							p.sendMessage("  §7Location Y: §f"+ModelManager.getModelSession(p).getModel().getArmorStand().getLocation().getY());
							p.sendMessage("  §7Location Z: §f"+ModelManager.getModelSession(p).getModel().getArmorStand().getLocation().getZ());
							p.sendMessage("  §7Headpose X: §f"+ModelManager.getModelSession(p).getModel().getArmorStand().getHeadPose().getX());
							p.sendMessage("  §7Headpose Y: §f"+ModelManager.getModelSession(p).getModel().getArmorStand().getHeadPose().getY());
							p.sendMessage("  §7Headpose Z: §f"+ModelManager.getModelSession(p).getModel().getArmorStand().getHeadPose().getZ());
						}else if(ModelManager.hasSession(p) == false) p.sendMessage("§cDu hast keine ModelSession am Laufen");
						else if(ModelManager.getModelSession(p).hasSelectedModel() == false) p.sendMessage("§cDu hast kein Model selektiert.");
						
					}else if(args[0].equalsIgnoreCase("reload")) {
						int amount = ModelManager.reloadModels();
						p.sendMessage("§2Alle Models(§f"+amount+"§2) wurden neugeladen und neu erstellte wurden gesummoned.");
					}else if(args[0].equalsIgnoreCase("off")) {
						ModelManager.closeAllModels();
						p.sendMessage("§2Alle Models wurden deaktiviert.");
					}else if(args[0].equalsIgnoreCase("select")) {
						Chat.sendHoverableCommandHelpMessage((Player)sender, " §7» §2/model select <ModelName> §fModel auswählen", "§fWähle ein Model aus, welches Du bearbeiten möchtest.\n§ePermission: §fskyblock.model", true, false);
					
					}else if(args[0].equalsIgnoreCase("load")) {
						Chat.sendHoverableCommandHelpMessage((Player)sender, " §7» §2/model load <ModelName> §fModels einsehen", "§fListe alle registrierten Models auf.\n§ePermission: §fskyblock.model", true, false);
					
					}else if(args[0].equalsIgnoreCase("unload")) {
						Chat.sendHoverableCommandHelpMessage((Player)sender, " §7» §2/model unload <ModelName> §fModels einsehen", "§fDeaktiviere ein Model\n§ePermission: §fskyblock.model", true, false);
					
					}else if(args[0].equalsIgnoreCase("list")) {
						ConcurrentHashMap<String, Model> models = ModelManager.getModels();
						if(models.isEmpty()) {
							LinkedList<String> unloaded = ModelManager.getExistingModels();
							p.sendMessage("§2Liste alle §f" + unloaded.size() + " §2auf:");
							for(String s : unloaded) {
								Chat.sendHoverableCommandHelpMessage((Player)sender, " - §7"+s, "§7§oDieses Model wurde nicht geladen.", true, false);
							}
						}
						else {
							p.sendMessage("§2Liste alle §f" + models.size() + " §2auf:");
							for(String s : models.keySet()) {
								Chat.sendHoverableCommandHelpMessage((Player)sender, " - §a"+s, "§7Location §fWorld/X/Y/Z: §3" + models.get(s).getLocation().getWorld().getName() + "§f/§3" + models.get(s).getLocation().getY() + "§f/§3" + models.get(s).getLocation().getX() + "§f/§3" + models.get(s).getLocation().getZ() +
										"\n§7Euler §fX/Y/Z: §3" + models.get(s).getEulerAngle().getY() + "§f/§3" + models.get(s).getEulerAngle().getX() + "§f/§3" + models.get(s).getEulerAngle().getZ(), true, false);
							}
							for(String s : ModelManager.getExistingModels()) {
								if(ModelManager.getModels().keySet().contains(s) == false) Chat.sendHoverableCommandHelpMessage((Player)sender, " - §7"+s, "§7§oDieses Model wurde nicht geladen.", true, false);
							}
						}
					}else if(args[0].equalsIgnoreCase("new")) {
						String modelName = Code.getRandomCode(1, 3);
						while(ModelManager.modelExists(modelName) == true) modelName = Code.getRandomCode(1, 3);
						ModelManager.createModelFile(modelName, p.getLocation());
						ModelManager.loadModel(modelName, LoadReason.GOT_CREATED, SummonReason.GOT_CREATED);
						p.sendMessage("§7§oEin neues Model(Name: §f" + modelName + "§7§o) wurde erstellt.");
						if(ModelManager.hasSession(p) && ModelManager.getModelSession(p).hasSelectedModel() == false)p.performCommand("model select "+modelName);
					}else if(args[0].equalsIgnoreCase("set")) {
						Chat.sendHoverableCommandHelpMessage((Player)sender, " §7» §2/model set <display : AutoSummon : Small> §fEigenschaft ändern", "§fÄndere eine Eigenschaft eines Models.\n§7AutoSummon: Eigenschaft wird gegenteilig vom aktuellen Wert gesetzt.\n§7ItemDisplay: Das Item aus Deiner Main-Hand wirdverwendet.\n§7Small: Eigenschaft wird gegenteilig vom aktuellen Wert gesetzt.\n§ePermission: §fskyblock.model", true, false);
					}else if(args[0].equalsIgnoreCase("session")) {
						Chat.sendHoverableCommandHelpMessage((Player)sender, " §7» §2/model session <save:exit:close:unselect> §fSession verwalten", "§fVerwalte Deine ModelSession.\n§fsave: Speichern die Änderungen.\nexit: Beendet Deine Session, ohne zu speichert.\n§fclose: Speichert und schließt Deine Session.\n§funselect: Speichert und unselektiert Dein aktuell selektiertes Model\n§ePermission: §fskyblock.model", true, false);
					}else if(args[0].equalsIgnoreCase("rename")) {
						Chat.sendHoverableCommandHelpMessage((Player)sender, " §7» §2/model rename <New ModelName> §fModel umbenennen", "§fÄndere den Name vom Model.\n§ePermission: §fskyblock.model", true, false);
					}else if(args[0].equalsIgnoreCase("delete")) {
						
						if(ModelManager.hasSession(p)) {
							if(ModelManager.getModelSession(p).hasSelectedModel()) {
								String modelName = ModelManager.getModelSession(p).getModel().getModelName();
								ModelManager.deleteModel(modelName);
								p.sendMessage("§aDas Model §f" + modelName + " §awurde gelöscht.");
							}else p.sendMessage("§cDu hast kein Model selektiert.");
						}else p.sendMessage("§cDu hast keine ModelSession am Laufen.");
						
					}else sendCommandInfo(p, "model");
					break;
				case 2:
					if(args[0].equalsIgnoreCase("load")) {
						String modelName = args[1];
						if(ModelManager.modelExists(modelName) == true) {
							if(ModelManager.loadModel(modelName, LoadReason.LOAD_BY_PLAYER, SummonReason.SUMMON_BY_PLAYER)) {
								p.playSound(p.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 6f, 0f);
								Model model = new Model(modelName);
								ModelManager.registerModel(model, LoadReason.MODEL_RELOAD, SummonReason.SUMMON_BY_PLAYER);
								p.sendMessage("§2Model §f" + modelName + " §2wurde geladen.");
							}else p.sendMessage("§cDas Model konnte nicht geladen werden.");
							
						}else p.sendMessage("§cDieses Model gibt es nicht. §f/model list");
					}else if(args[0].equalsIgnoreCase("unload")) {
						String modelName = args[1];
						if(ModelManager.modelExists(modelName) == true) {
							if(ModelManager.unregisterModel(modelName)) {
								p.playSound(p.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 6f, 0f);
								p.sendMessage("§2Model §f" + modelName + " §2wurde deaktiviert.");
							}else p.sendMessage("§cDas Model konnte nicht deaktiviert werden.");
							
						}else p.sendMessage("§cDieses Model gibt es nicht. §f/model list");
					}else if(args[0].equalsIgnoreCase("set")) {
						if(ModelManager.hasSession(p)) {
							if(ModelManager.getModelSession(p).hasSelectedModel()) {								
								if(args[1].equalsIgnoreCase("autosummon")) {
									ModelManager.getModelSession(p).setAutoSummon(!ModelManager.getModelSession(p).getModel().getAutoSummon());
									p.sendMessage("§aEigenschaft geändert!");
									p.sendMessage("§aAutoSummon: §f"+ModelManager.getModelSession(p).getModel().getAutoSummon());
								}else if(args[1].equalsIgnoreCase("display")) {
									ModelManager.getModelSession(p).getModel().setDisplayItem(p.getInventory().getItemInMainHand());
								}else if(args[1].equalsIgnoreCase("small")) {
									ModelManager.getModelSession(p).getModel().setSmall(!ModelManager.getModelSession(p).getModel().getSmall());
								}
							}else p.sendMessage("§cDu hast kein Model selektiert.");
						}else p.sendMessage("§cDu hast keine ModelSession am Laufen.");
					}else if(args[0].equalsIgnoreCase("session")) {
						if(ModelManager.hasSession(p)) {
							if(ModelManager.getModelSession(p).hasSelectedModel()) {								
								if(args[1].equalsIgnoreCase("save")) {
									ModelManager.getModelSession(p).getModel().saveChanges();
									p.sendMessage("§aÄnderungen gespeichert.");
								}else if(args[1].equalsIgnoreCase("exit")) {
									ModelManager.closeSession(p, false);
									p.sendMessage("§aSession geschlossen.");
								}else if(args[1].equalsIgnoreCase("close")) {
									ModelManager.closeSession(p, true);
									p.sendMessage("§aSession gespeichert & geschlossen.");
								}else if(args[1].equalsIgnoreCase("unselect")) {
									ModelManager.getModelSession(p).getModel().saveChanges();
									p.sendMessage("§aModel wurde gespeichert.");
									ModelManager.getModelSession(p).unselectModel();
									p.sendMessage("§aDu hast nun kein Model mehr selektiert");
								}
							}else p.sendMessage("§cDu hast kein Model selektiert.");
						}else p.sendMessage("§cDu hast keine ModelSession am Laufen.");
					}else if(args[0].equalsIgnoreCase("rename")) {
						if(ModelManager.hasSession(p)) {
							if(ModelManager.getModelSession(p).hasSelectedModel()) {
								String oldName = ModelManager.getModelSession(p).getModel().getModelName();
								if(ModelManager.renameModel(ModelManager.getModelSession(p).getModel().getModelName(), args[1])) {
									p.sendMessage("§aName von §f" + oldName + " §azu §f" + args[1] + " §ageändert.");									
								}else p.sendMessage("§cDer Name konnte nicht geändert werden.");
							}else p.sendMessage("§cDu hast kein Model selektiert.");							
						}else p.sendMessage("§cDu hast keine ModelSession am Laufen.");
					}else if(args[0].equalsIgnoreCase("select")) {
						if(ModelManager.hasSession(p) == false) ModelManager.createNewSession(p);
						if((ModelManager.getModelSession(p).hasSelectedModel() && ModelManager.getModelSession(p).getModel().isSaved()) || ModelManager.getModelSession(p).hasSelectedModel() == false) {
							if(ModelManager.getModelSession(p).selectModel(args[1])) p.sendMessage("§aDu bearbeitest nun das Model §f" + args[1]);
							else if(ModelManager.getModel(args[1]) != null && ModelManager.getModel(args[1]).isInEditing()) p.sendMessage("§eDieses Model wird gerade von einem anderen Spieler bearbeitet !");
							else p.sendMessage("§cBeim Selektieren ist etwas schief gelaufen.");
						}else if(ModelManager.getModelSession(p).hasSelectedModel() && ModelManager.getModelSession(p).getModel().isSaved() == false) p.sendMessage("§cDu musst Dein aktuelles Model erst speichern mit §f/model session save");
					}else sendCommandInfo(p, "model");
					break;
					default:
						sendCommandInfo(p, "model");
						break;
				}
			}else SkyBlock.sendConsoleMessage(MessageType.ERROR, "Dieser Befehl ist nur für Spieler.");
		}
		
		return false;
	}
	
	public void sendCommandInfo(CommandSender sender, String cmd) {
		if(cmd.equals("model")) {
			if(SkyBlock.hasPermission(sender, "skyblock.model")) {
				sender.sendMessage(" §2Model-Befehle");
				Chat.sendHoverableCommandHelpMessage((Player)sender, " §7» §2/model edit §fBearbeite ein Model", "§fBearbeite ein Model in der Location, Headposition und dem Display-Item.\n§ePermission: §fskyblock.model", true, false);
				Chat.sendHoverableCommandHelpMessage((Player)sender, " §7» §2/model info §fErhalte Information", "§fErhalte Informationen über das Model, welches Du gerade bearbeitest.\n§ePermission: §fskyblock.model", true, false);
				Chat.sendHoverableCommandHelpMessage((Player)sender, " §7» §2/model reload §fAlle Models neu laden", "§fAlle Models neu laden und neu erstelle Models laden.\n§ePermission: §fskyblock.model", true, false);
				Chat.sendHoverableCommandHelpMessage((Player)sender, " §7» §2/model list §fModels einsehen", "§fListe alle registrierten Models auf.\n§ePermission: §fskyblock.model", true, false);
				Chat.sendHoverableCommandHelpMessage((Player)sender, " §7» §2/model new §fModel erstellen", "§fErstelle ein neues Model an der Position,\n§fwo Du dich gerade befindest.\n§ePermission: §fskyblock.model", true, false);
				Chat.sendHoverableCommandHelpMessage((Player)sender, " §7» §2/model select <ModelName> §fModel auswählen", "§fWähle ein Model aus, welches Du bearbeiten möchtest.\n§ePermission: §fskyblock.model", true, false);
				Chat.sendHoverableCommandHelpMessage((Player)sender, " §7» §2/model load <ModelName> §fModels einsehen", "§fAktiviere ein Model\n§ePermission: §fskyblock.model", true, false);
				Chat.sendHoverableCommandHelpMessage((Player)sender, " §7» §2/model unload <ModelName> §fModels einsehen", "§fDeaktiviere ein Model.\n§ePermission: §fskyblock.model", true, false);
				Chat.sendHoverableCommandHelpMessage((Player)sender, " §7» §2/model rename <New ModelName> §fModel umbenennen", "§fÄndere den Name vom Model.\n§ePermission: §fskyblock.model", true, false);
				Chat.sendHoverableCommandHelpMessage((Player)sender, " §7» §2/model set <Display : AutoSummon : Small> §fEigenschaft ändern", "§fÄndere eine Eigenschaft eines Models.\n§fAutoSummon: §7Eigenschaft wird gegenteilig vom aktuellen Wert gesetzt.\n§fItemDisplay: §7Das Item aus Deiner Main-Hand wirdverwendet.\n§fsmall: §7Eigenschaft wird gegenteilig vom aktuellen Wert gesetzt.\n§ePermission: §fskyblock.model", true, false);
				Chat.sendHoverableCommandHelpMessage((Player)sender, " §7» §2/model session <save:exit:close:unselect> §fSession verwalten", "§fVerwalte Deine ModelSession.\n§fsave:§7 Speichern die Änderungen.\n§fexit: §7Beendet Deine Session, ohne zu speichert.\n§fclose: §7Speichert und schließt Deine Session.\n§funselect: §7Speichert und unselektiert Dein aktuell selektiertes Model\n§ePermission: §fskyblock.model", true, false);
				Chat.sendHoverableCommandHelpMessage((Player)sender, " §7» §2/model delete §fModel löschen", "§fSelektiertes Model löschen.\n§cACHTUNG: §cEs wird nicht um Bestätigung gebeten. Ausführen dieses Befehls hat SOFORTIGE WIRKUNG!\n§ePermission: §fskyblock.model", true, false);
			}
		}
	}
}