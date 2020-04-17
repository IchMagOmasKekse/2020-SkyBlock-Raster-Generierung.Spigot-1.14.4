package me.ichmagomaskekse.de.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.ichmagomaskekse.de.SkyBlock;

public class AsyncChatListener implements Listener {
	
	public AsyncChatListener() {
		SkyBlock.getInstance().getServer().getPluginManager().registerEvents(this, SkyBlock.getInstance());
	}
	
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if(e.getPlayer().isOp() || e.getPlayer().hasPermission("skyblock.chat.color")) {
			e.setMessage(e.getMessage().replace("&", "§"));
		}
	}
	
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block b = e.getClickedBlock();
			p.getInventory().addItem((ItemStack)b);
		}
	}
}
