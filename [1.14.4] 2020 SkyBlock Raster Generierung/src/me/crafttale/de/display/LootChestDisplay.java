package me.crafttale.de.display;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import me.crafttale.de.SkyBlock;

public class LootChestDisplay extends Display {
	
	int anima = 0;
	
	public LootChestDisplay(Location loc) {
		super(SkyBlock.spawn.getWorld().spawn(loc.clone(), ArmorStand.class), "");
		display.setSmall(true);
	}
	
	@Override
	public void update() {
		if(frame == 0) randomAnimationEffect();
		if(anima == 0) {
			triggerAtTick = 3;
			if(tick < triggerAtTick) tick++;
			else {
				tick = 0;
				switch (frame) {
				case 0:
					maxFrames = 20;
					updateText("§5§lLOOT-CHEST");
					nextFrame();
					break;
				case 1:
					updateText("§f§lL§5§lOOT-CHEST");
					nextFrame();
					break;
				case 2:
					updateText("§5§lL§f§lO§5§lOT-CHEST");
					nextFrame();
					break;
				case 3:
					updateText("§5§lLO§f§lO§5§lT-CHEST");
					nextFrame();
					break;
				case 4:
					updateText("§5§lLOO§f§lT§5§l-CHEST");
					nextFrame();
					break;
				case 5:
					updateText("§5§lLOOT§f§l-§5§lCHEST");
					nextFrame();
					break;
				case 6:
					updateText("§5§lLOOT-§f§lC§5§lHEST");
					nextFrame();
					break;
				case 7:
					updateText("§5§lLOOT-C§f§lH§5§lEST");
					nextFrame();
					break;
				case 8:
					updateText("§5§lLOOT-CH§f§lE§5§lST");
					nextFrame();
					break;
				case 9:
					updateText("§5§lLOOT-CHE§f§lS§5§lT");
					nextFrame();
				case 10:
					updateText("§5§lLOOT-CHES§f§lT");
					nextFrame();
					break;
				case 11:
					updateText("§5§lLOOT-CHEST");
					nextFrame();
					break;
				case 12:
					nextFrame();
					break;
				case 13:
					nextFrame();
					break;
				case 14:
					nextFrame();
					break;
				case 15:
					nextFrame();
					break;
				case 16:
					nextFrame();
					break;
				case 17:
					nextFrame();
					break;
				case 18:
					nextFrame();
					break;
				case 19:
					nextFrame();
					break;
				case 20:
					nextFrame();
					break;
				default:
					break;
				}
			}
			
		}else if(anima == 1) {
			triggerAtTick = 3;
			if(tick < triggerAtTick) tick++;
			else {
				tick = 0;
				switch (frame) {
				case 0:
					maxFrames = 20;
					updateText("§5§lLOOT-CHEST");
					nextFrame();
					break;
				case 1:
					updateText("§f§lL§5§lOOT-CHEST");
					nextFrame();
					break;
				case 2:
					updateText("§f§lO§5§lOT-CHEST");
					nextFrame();
					break;
				case 3:
					updateText("§f§lO§5§lT-CHEST");
					nextFrame();
					break;
				case 4:
					updateText("§f§lT§5§l-CHEST");
					nextFrame();
					break;
				case 5:
					updateText("§f§l-§5§lCHEST");
					nextFrame();
					break;
				case 6:
					updateText("§f§lC§5§lHEST");
					nextFrame();
					break;
				case 7:
					updateText("§f§lH§5§lEST");
					nextFrame();
					break;
				case 8:
					updateText("§f§lE§5§lST");
					nextFrame();
					break;
				case 9:
					updateText("§f§lS§5§lT");
					nextFrame();
				case 10:
					updateText("§f§lT");
					nextFrame();
					break;
				case 11:
					updateText("");
					nextFrame();
					break;
				case 12:
					nextFrame();
					break;
				case 13:
					nextFrame();
					break;
				case 14:
					nextFrame();
					break;
				case 15:
					nextFrame();
					break;
				case 16:
					nextFrame();
					break;
				case 17:
					nextFrame();
					break;
				case 18:
					nextFrame();
					break;
				case 19:
					nextFrame();
					break;
				case 20:
					nextFrame();
					break;
				default:
					break;
				}
			}
		}else if(anima == 2) {
			triggerAtTick = 2;
			if(tick < triggerAtTick) tick++;
			else {
				tick = 0;
				switch (frame) {
				case 0:
					maxFrames = 22;
					updateText("§5§lLOOT-CHEST");
					nextFrame();
					break;
				case 1:
					updateText("");
					nextFrame();
					break;
				case 2:
					updateText("§f§lO§5§lOT-CHEST");
					nextFrame();
					break;
				case 3:
					updateText("");
					nextFrame();
					break;
				case 4:
					updateText("§f§lT§5§l-CHEST");
					nextFrame();
					break;
				case 5:
					updateText("");
					nextFrame();
					break;
				case 6:
					updateText("§f§lC§5§lHEST");
					nextFrame();
					break;
				case 7:
					updateText("");
					nextFrame();
					break;
				case 8:
					updateText("§f§lE§5§lST");
					nextFrame();
					break;
				case 9:
					updateText("");
					nextFrame();
				case 10:
					updateText("§f§lT");
					nextFrame();
					break;
				case 11:
					updateText("");
					nextFrame();
					break;
				case 12:
					updateText("§5§lLOOT-CHEST");
					nextFrame();
					break;
				case 13:
					updateText("");
					nextFrame();
					break;
				case 14:
					updateText("§9§lLOOT-CHEST");
					nextFrame();
					break;
				case 15:
					updateText("");
					nextFrame();
					break;
				case 16:
					updateText("§2§lLOOT-CHEST");
					nextFrame();
					break;
				case 17:
					updateText("");
					nextFrame();
					break;
				case 18:
					updateText("§e§lLOOT-CHEST");
					nextFrame();
					break;
				case 19:
					updateText("");
					nextFrame();
					break;
				case 20:
					updateText("§6§lLOOT-CHEST");
					nextFrame();
					break;
				case 21:
					updateText("");
					nextFrame();
					break;
				case 22:
					updateText("§4§lLOOT-CHEST");
					nextFrame();
					break;
				default:
					break;
				}
			}
		}else if(anima == 3) {
			triggerAtTick = 1;
			if(tick < triggerAtTick) tick++;
			else {
				tick = 0;
				switch (frame) {
				case 0:
					maxFrames = 20;
					updateText("§5§lLOOT-CHEST");
					nextFrame();
					break;
				case 1:
					updateText("§5§lOOT-CHEST L");
					nextFrame();
					break;
				case 2:
					updateText("§5§lOT-CHEST LO");
					nextFrame();
					break;
				case 3:
					updateText("§5§lT-CHEST LOO");
					nextFrame();
					break;
				case 4:
					updateText("§5§l-CHEST LOOT");
					nextFrame();
					break;
				case 5:
					updateText("§5§lCHEST LOOT-");
					nextFrame();
					break;
				case 6:
					updateText("§5§lHEST LOOT-C");
					nextFrame();
					break;
				case 7:
					updateText("§5§lEST LOOT-CH");
					nextFrame();
					break;
				case 8:
					updateText("§5§lST LOOT_CHE");
					nextFrame();
					break;
				case 9:
					updateText("§5§lT LOOT_CHES");
					nextFrame();
				case 10:
					updateText("§5§l LOOT-CHEST");
					nextFrame();
					break;
				case 11:
					updateText("§5§lLOOT-CHEST");
					nextFrame();
					break;
				case 12:
					updateText("§5§lLOOT-CHEST");
					nextFrame();
					break;
				case 13:
					updateText("§4§lLOOT-CHEST");
					nextFrame();
					break;
				case 14:
					updateText("§9§lLOOT-CHEST");
					nextFrame();
					break;
				case 15:
					updateText("§4§lLOOT-CHEST");
					nextFrame();
					break;
				case 16:
					updateText("§2§lLOOT-CHEST");
					nextFrame();
					break;
				case 17:
					updateText("§4§lLOOT-CHEST");
					nextFrame();
					break;
				case 18:
					updateText("§e§lLOOT-CHEST");
					nextFrame();
					break;
				case 19:
					updateText("§4§lLOOT-CHEST");
					nextFrame();
					break;
				case 20:
					updateText("§6§lLOOT-CHEST");
					nextFrame();
					break;
				default:
					break;
				}
			}
		}else if(anima == 4) {
			triggerAtTick = 1;
			if(tick < triggerAtTick) tick++;
			else {
				tick = 0;
				switch (frame) {
				case 0:
					updateText("§5§lLOOT-CHEST");
					nextFrame();
					break;
				case 1:
					maxFrames = 20;
					updateText("§5§lT LOOT-CHES");
					nextFrame();
					break;
				case 2:
					updateText("§5§lST LOOT-CHE");
					nextFrame();
					break;
				case 3:
					updateText("§5§lEST LOOT-CH");
					nextFrame();
					break;
				case 4:
					updateText("§5§lHEST LOOT-C");
					nextFrame();
					break;
				case 5:
					updateText("§5§lCHEST LOOT-");
					nextFrame();
					break;
				case 6:
					updateText("§5§l-CHEST LOOT");
					nextFrame();
					break;
				case 7:
					updateText("§5§lT-CHEST LOO");
					nextFrame();
					break;
				case 8:
					updateText("§5§lOT-CHEST LO");
					nextFrame();
					break;
				case 9:
					updateText("§5§lOOT-CHEST L");
					nextFrame();
					break;
				case 10:
					updateText("§5§lLOOT-CHEST ");
					nextFrame();
				case 11:
					updateText("§5§lLOOT-CHEST");
					nextFrame();
					break;
				case 12:
					updateText("§5§lLOOT-CHEST");
					nextFrame();
					break;
				case 13:
					updateText("§5§lLOOT-CHEST");
					nextFrame();
					break;
				case 14:
					updateText("§4§lLOOT-CHEST");
					nextFrame();
					break;
				case 15:
					updateText("§9§lLOOT-CHEST");
					nextFrame();
					break;
				case 16:
					updateText("§4§lLOOT-CHEST");
					nextFrame();
					break;
				case 17:
					updateText("§2§lLOOT-CHEST");
					nextFrame();
					break;
				case 18:
					updateText("§4§lLOOT-CHEST");
					nextFrame();
					break;
				case 19:
					updateText("§e§lLOOT-CHEST");
					nextFrame();
					break;
				case 20:
					updateText("§4§lLOOT-CHEST");
					nextFrame();
					break;
				default:
					break;
				}
			}
		}
	}
	
	
	public void randomAnimationEffect() {
		this.anima = SkyBlock.randomInteger(0, 4);
	}
}
