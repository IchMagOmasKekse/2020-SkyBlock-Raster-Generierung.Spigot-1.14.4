package me.crafttale.de.gui.island;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.crafttale.de.Chat.MessageType;
import me.crafttale.de.SkyBlock;
import me.crafttale.de.gui.GUI;
import me.crafttale.de.profiles.IslandManager;
import me.crafttale.de.profiles.IslandManager.IslandData;

public class IslandSettingsGUI extends GUI {

	public IslandSettingsGUI(Player player) {
		super(player, GUIType.ISLAND_SETTINGS_GUI);
		closeInventoryWhenGetClosed = false;
	}

	@Override
	public void start() {
		
	}

	@Override
	public void stop() {
		player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 6f, -0.5f);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clickedItem(InventoryClickEvent e, ItemStack item) {
		if(item != null &&
				(item.getType() == Material.YELLOW_STAINED_GLASS_PANE || item.getType() == Material.LIME_STAINED_GLASS_PANE || item.getType() == Material.RED_STAINED_GLASS_PANE)) {
			
			IslandData data = IslandManager.getIslandData(IslandManager.getProfile((Player)e.getWhoClicked()).getIslandID());
			
				switch(e.getRawSlot()) {
				case 9:
					item.setType((item.getType() == Material.LIME_STAINED_GLASS_PANE ? Material.RED_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE));
					data.getSettings().edit("allowVisiting", !data.getSettings().isAllowVisiting(), (Player)e.getWhoClicked());
					break;
				case 10:
					item.setType((item.getType() == Material.LIME_STAINED_GLASS_PANE ? Material.RED_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE));
					data.getSettings().edit("fireSpread", !data.getSettings().isFireSpread(), (Player)e.getWhoClicked());
					break;
				case 11:
					item.setType((item.getType() == Material.LIME_STAINED_GLASS_PANE ? Material.RED_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE));
					data.getSettings().edit("monsterSpawning", !data.getSettings().isMonsterSpawning(), (Player)e.getWhoClicked());
					break;
				case 12:
					item.setType((item.getType() == Material.LIME_STAINED_GLASS_PANE ? Material.RED_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE));
					data.getSettings().edit("animalSpawning", !data.getSettings().isAnimalSpawning(), (Player)e.getWhoClicked());
					break;
				case 13:
					item.setType((item.getType() == Material.LIME_STAINED_GLASS_PANE ? Material.RED_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE));
					data.getSettings().edit("tntDamage", !data.getSettings().isTntDamage(), (Player)e.getWhoClicked());
					break;
				case 14:
					item.setType((item.getType() == Material.LIME_STAINED_GLASS_PANE ? Material.RED_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE));
					data.getSettings().edit("mobGriefing", !data.getSettings().isMobGriefing(), (Player)e.getWhoClicked());
					break;
				case 15:
					item.setType((item.getType() == Material.LIME_STAINED_GLASS_PANE ? Material.RED_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE));
					data.getSettings().edit("pvp", !data.getSettings().isPvp(), (Player)e.getWhoClicked());
					break;
				case 27:
					new BukkitRunnable() {
						
						@Override
						public void run() {
//							SkyBlock.sendMessage(MessageType.INFO, player, "Es wurden §a"+IslandManager.clearAllEntitiesAtIsland(data.getIslandId(), new EntityType[] {EntityType.ZOMBIE,
//									EntityType.SKELETON, EntityType.CREEPER, EntityType.SPIDER, EntityType.PHANTOM, EntityType.CAVE_SPIDER, EntityType.BLAZE, EntityType.DROWNED,
//									EntityType.ELDER_GUARDIAN, EntityType.ENDERMITE, EntityType.ENDERMAN, EntityType.GHAST, EntityType.GIANT, EntityType.GUARDIAN, EntityType.HOGLIN,
//									EntityType.ILLUSIONER, EntityType.MAGMA_CUBE, EntityType.PIGLIN, EntityType.PIGLIN_BRUTE, EntityType.PILLAGER, EntityType.RAVAGER, EntityType.SHULKER,
//									EntityType.SHULKER_BULLET, EntityType.SILVERFISH, EntityType.SLIME, EntityType.STRIDER, EntityType.VEX, EntityType.VINDICATOR, EntityType.WITCH,
//									EntityType.ZOGLIN, EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIFIED_PIGLIN})+" §7Monster getötet.");
							SkyBlock.sendMessage(MessageType.INFO, player, "Es wurden §a"+IslandManager.clearAllEntitiesAtIsland(data.getIslandId(), Arrays.asList(EntityType.ZOMBIE,
									EntityType.SKELETON, EntityType.CREEPER, EntityType.SPIDER, EntityType.PHANTOM, EntityType.CAVE_SPIDER, EntityType.BLAZE, EntityType.DROWNED,
									EntityType.ELDER_GUARDIAN, EntityType.ENDERMITE, EntityType.ENDERMAN, EntityType.GHAST, EntityType.GIANT, EntityType.GUARDIAN, EntityType.HOGLIN,
									EntityType.ILLUSIONER, EntityType.MAGMA_CUBE, EntityType.PIGLIN, EntityType.PIGLIN_BRUTE, EntityType.PILLAGER, EntityType.RAVAGER, EntityType.SHULKER,
									EntityType.SHULKER_BULLET, EntityType.SILVERFISH, EntityType.SLIME, EntityType.STRIDER, EntityType.VEX, EntityType.VINDICATOR, EntityType.WITCH,
									EntityType.ZOGLIN, EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIFIED_PIGLIN))+" §7Monster getötet.");
						}
					}.runTaskAsynchronously(SkyBlock.getSB());
					break;
					default:
						player.sendMessage("RawSlot: "+e.getRawSlot());
						break;
				}
				if(item.getType() == Material.LIME_STAINED_GLASS_PANE) {
					meta = item.getItemMeta();
					meta.setDisplayName("§aErlaubt");
					item.setItemMeta(meta);
				}else if(item.getType() == Material.RED_STAINED_GLASS_PANE) {
					meta = item.getItemMeta();
					meta.setDisplayName("§cVerboten");
					item.setItemMeta(meta);
					
				}
		}
	}
}
