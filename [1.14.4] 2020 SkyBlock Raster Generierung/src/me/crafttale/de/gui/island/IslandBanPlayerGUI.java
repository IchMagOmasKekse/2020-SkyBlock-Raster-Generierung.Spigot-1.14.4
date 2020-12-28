package me.crafttale.de.gui.island;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import me.crafttale.de.Chat.MessageType;
import me.crafttale.de.PlayerAtlas;
import me.crafttale.de.SkyBlock;
import me.crafttale.de.filemanagement.SkyFileManager;
import me.crafttale.de.gui.GUI;
import me.crafttale.de.gui.GUIManager;
import me.crafttale.de.profiles.PlayerProfiler;

public class IslandBanPlayerGUI extends GUI {
	
	
	public IslandBanPlayerGUI(Player player, UUID uuid) {
		super(player, uuid, GUIType.ISLAND_BAN_PLAYER_GUI);
	}

	@Override
	public void start() {
		openedInv = Bukkit.createInventory(null,  InventoryType.HOPPER, "Verbannung: §c"+PlayerAtlas.getPlayername(targetUUID.toString()));
		processedTitle = type.getTitle();
		
		item = new ItemStack(Material.WHITE_BANNER);
		meta = item.getItemMeta();
		meta.setDisplayName("§aAbbrechen");
		item.setItemMeta(meta);
		openedInv.setItem(0, item);
		
		item = new ItemStack(Material.RED_BANNER);
		meta = item.getItemMeta();
		meta.setDisplayName("§cVerbannen!");
		item.setItemMeta(meta);
		openedInv.setItem(4, item);
		
		player.openInventory(openedInv);
	}

	@Override
	public void stop() {
		player.playSound(player.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 6f, -0.5f);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clickedItem(InventoryClickEvent e, ItemStack item) {
		if(item != null &&
				(item.getType() == Material.WHITE_BANNER || item.getType() == Material.RED_BANNER)) {
			
			if(item.getType() == Material.RED_BANNER) {
				if(SkyFileManager.banPlayerFromIsland(player, targetUUID)) {
					GUIManager.closeGUI(player);
					SkyFileManager.removeMember(PlayerAtlas.getUUID(PlayerAtlas.getPlayername(targetUUID.toString())).toString(),
							PlayerProfiler.getUUID(player).toString());
					me.crafttale.de.commands.SkyBlockCommandFunction.kickPlayer(player, PlayerAtlas.getPlayername(targetUUID.toString()));
					SkyBlock.sendMessage(MessageType.WARNING, player, "Du hast so eben einen Spieler von Deiner Insel verbannt: §e"+PlayerAtlas.getPlayername(targetUUID.toString()));
					SkyBlock.sendMessage(MessageType.WARNING, player, "Eine Verbannung kannst Du mit §f/is pardon §canulieren.");
				}else SkyBlock.sendMessage(MessageType.ERROR, player, "Die Verbannung ist fehlgeschlagen. Probiere es erneut.");
			}else if(item.getType() == Material.WHITE_BANNER) {
				GUIManager.closeGUI(player);
				SkyBlock.sendMessage(MessageType.INFO, player, "Du hast die Verbannung abgebrochen.");
			}
				
		}
	}
}
