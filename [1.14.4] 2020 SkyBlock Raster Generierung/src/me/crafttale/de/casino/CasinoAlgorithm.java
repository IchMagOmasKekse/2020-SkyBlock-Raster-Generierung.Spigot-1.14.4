package me.crafttale.de.casino;

import java.util.LinkedList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.crafttale.de.SkyBlock;

public class CasinoAlgorithm {
	
	private ItemProbability[] content = null;
	private ItemStack[] preDrawn = null;
	
	public CasinoAlgorithm(LinkedList<ItemProbability> items) {
		content = new ItemProbability[items.size()];
		for(int i = 0; i != items.size(); i++) {
			content[i] = items.get(i);
		}
		
		preDrawn = new ItemStack[100];
		for(int i = 0; i != 100; i++) {
			preDrawn[i] = next();
		}
	}
	
	ItemStack item = null;
	ItemStack lastItem = null;
	ItemProbability iP = null;
	int doubled = 0; //Anzahl wie oft das letzte Item doppelt gezogen wurde
	int maxDoubles = -1; //Wie oft darf ein Item doppelt gezogen werden
	public ItemStack next() {
		
		if(item == null && lastItem == null) {			
			while(iP == null) {
				iP = content[SkyBlock.randomInteger(0, content.length-1)];
				
				double drawn = (SkyBlock.randomInteger(0, 1000) / 100);
				
				if((drawn) < iP.getProbability()) {
					item = iP.getItem();
					break;
				}else continue;
			}
		}else if(item != null && lastItem == null) {
			lastItem = item;
			while(iP == null) {
				iP = content[SkyBlock.randomInteger(0, content.length-1)];
				
				double drawn = (SkyBlock.randomInteger(0, 1000) / 100);
				
				if((drawn) < iP.getProbability()) {
					item = iP.getItem();
					break;
				}else continue;
			}
		}else if(item != null && lastItem != null) {
			lastItem = item;
			while(iP == null) {
				iP = content[SkyBlock.randomInteger(0, content.length-1)];
				
				double drawn = (SkyBlock.randomInteger(0, 1000) / 100);
				
				if((drawn) < iP.getProbability()) {
					maxDoubles = iP.maxDoubling;
					if(lastItem == iP.getItem()) {
						if(doubled <= maxDoubles) {
							item = iP.getItem();
							doubled++;
						}
					}else {
						item = iP.getItem();
						doubled = 0;
					}
					break;
				}else continue;
			}
		}
		iP = null;
		return item;
	}
	
	public ItemProbability clone(int index) {
		if(index >= content.length) return new ItemProbability(new ItemStack(Material.BEDROCK), 100.0d, true, 1000);
		return content[index].clone();
	}
	
	public static class ItemProbability {
		
		/**
		 * Diese Klasse beinhaltet Daten zu den Items, die in den Slots angezeigt werden.
		 */
		
		ItemStack item = null;
		double probability = 30.5d; //Wahrscheinlich in % Ausgelost zu werden
		boolean allowDoubling = true; //Darf dieses Item mehrfach gezogen werden?
		int maxDoubling = 1;
		
		public ItemProbability() { }
		
		public ItemProbability(ItemStack item, double probability, boolean allowDoubling, int maxDoubling) {
			this.item = item;
			this.probability = probability;
			this.allowDoubling = allowDoubling;
			this.maxDoubling = maxDoubling;
		}

		public ItemStack getItem() {
			return item;
		}

		public double getProbability() {
			return probability;
		}

		public boolean isAllowDoubling() {
			return allowDoubling;
		}
		
		public ItemProbability clone() {
			return new ItemProbability(item, probability, allowDoubling, maxDoubling);
		}
		
		
		
	}
	
	public static enum ContentType {
		DEFAULT("default_content"),
		DONOR1("donor1_content"),
		DONOR2("donor2_content"),
		DONOR3("donor3_content"),
		DONOR4("donor4_content"),
		VOTE("vote_content");
		
		String fileName = ""; //Dateiname
		String fileExtension = ".cas"; //Dateiendung
		
		ContentType(String fileName) {
			this.fileName = fileName;
		}
		
		public String getFileName() {
			return fileName+fileExtension;
		}
	}
	
}
