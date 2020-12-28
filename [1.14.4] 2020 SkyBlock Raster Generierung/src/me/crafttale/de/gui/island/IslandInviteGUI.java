package me.crafttale.de.gui.island;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import me.crafttale.de.Chat;
import me.crafttale.de.Chat.MessageType;
import me.crafttale.de.SkyBlock;
import me.crafttale.de.filemanagement.SkyFileManager;
import me.crafttale.de.gui.GUI;
import me.crafttale.de.gui.GUIManager;
import me.crafttale.de.profiles.IslandManager;
import me.crafttale.de.profiles.PlayerProfiler;

public class IslandInviteGUI extends GUI {

	public IslandInviteGUI(Player player, Player player2) {
		super(player, player2, GUIType.ISLAND_INVITE__GUI);
	}

	@Override
	public void start() {
		openedInv = Bukkit.createInventory(null,  InventoryType.HOPPER, "Einladung von: "+PlayerProfiler.getCurrentPlayerName(player2));
		processedTitle = type.getTitle();
		
		item = new ItemStack(Material.LIME_BANNER);
		meta = item.getItemMeta();
		meta.setDisplayName("§aAnnehmen");
		item.setItemMeta(meta);
		openedInv.setItem(0, item);
		
		item = new ItemStack(Material.RED_BANNER);
		meta = item.getItemMeta();
		meta.setDisplayName("§cAblehnen");
		item.setItemMeta(meta);
		openedInv.setItem(4, item);
		
		player.openInventory(openedInv);
	}

	@Override
	public void stop() {
		SkyBlock.sendMessage(MessageType.INFO, player2, "§e"+PlayerProfiler.getCurrentPlayerName(player)+" §7hat deine Einladung abgelehnt.");
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clickedItem(InventoryClickEvent e, ItemStack item) {
		if(item != null &&
				(item.getType() == Material.LIME_BANNER || item.getType() == Material.RED_BANNER)) {
			
			if(item.getType() == Material.RED_BANNER) {
				SkyBlock.sendMessage(MessageType.INFO, player2, "§e"+PlayerProfiler.getCurrentPlayerName(player)+" §7hat deine Einladung abgelehnt.");
				GUIManager.closeGUI(player);
			}else if(item.getType() == Material.LIME_BANNER) {
				GUIManager.closeGUI(player);
				player.playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_LEVELUP, 6f, 1f);
				SkyBlock.sendMessage(MessageType.INFO, player2, "§e"+PlayerProfiler.getCurrentPlayerName(player)+" §7hat deine Einladung angenommen");


				if(IslandManager.getProfile(player2).addMember(player)) {
					SkyFileManager.setPlayerDefinedIslandSpawn(player, SkyFileManager.getPlayerDefinedIslandSpawn(player2));
					Chat.sendClickableMessage(player, "Du wurdest soeben zu "+PlayerProfiler.getCurrentPlayerName(player2)+"s Insel hinzugefügt.", "Klicke um dich dort hin zu teleportieren.", "/is", false, true);
					Chat.sendHoverableMessage(player2, "§fNeuer Member hinzugefügt!", "§fHinzugefügt: §a"+PlayerProfiler.getCurrentPlayerName(player), false, true);								
				}else Chat.sendHoverableMessage(player2, "§cSpieler ist bereits Member!", "Dieser Spieler ist bereits Member deiner Insel.", false, true);
				
			}
				
		}
	}

}
