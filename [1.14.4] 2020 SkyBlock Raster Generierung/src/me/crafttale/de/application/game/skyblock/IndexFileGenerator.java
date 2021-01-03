package me.crafttale.de.application.game.skyblock;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.crafttale.de.SkyBlock;
import me.crafttale.de.SkyBlockGenerator;
import me.crafttale.de.application.game.SkyBlockAdminTool;

public class IndexFileGenerator {
	
	private static int issize = SkyBlockGenerator.issize;
	private static int spaceBetweenIslands = SkyBlockGenerator.spaceBetweenIslands;
	public static int amountOfIslands = SkyBlockGenerator.amountOfIslands;
	private static ConcurrentLinkedQueue<Island> islands = new ConcurrentLinkedQueue<Island>();
	@SuppressWarnings("unused")
	private static ArrayList<String> emptylist = new ArrayList<String>();
	public static boolean pause = false;
	public static boolean generateDatabank = true;
	private static File file = null;
	private static FileConfiguration cfg;
	
	public IndexFileGenerator(boolean generateDatabank) {
		IndexFileGenerator.generateDatabank = generateDatabank;
	}
	
	public static void renderIslands(Graphics g) {
		for(Island i : islands) {
			if(pause == false && islands.isEmpty() == false) {
				i.render(g);
			}
		}
	}
	
	
	static int curx = 500;
	static int cury = 300;
	public static boolean generate(SkyBlock sb) {
		if(SkyBlockAdminTool.isGenerated == false) {
			System.out.println("Erstelle Insel-Index-File.yml...");
			if(generateDatabank) {
				file = new File("src/Insel-Index-File.yml");
				cfg = YamlConfiguration.loadConfiguration(file);
				cfg.set("Islands.Amount Generated", amountOfIslands);
				cfg.set("Islands.Amount Claimed", 0);
			}
			pause = false;
			islands.clear();
			curx = 500;
			cury = 300;
			int left = 1;
			int round = 0;
			int addx1 = 1; //â†�
			int addy1 = 1; //â†“
			int addx2 = 2; //â†’
			int addy2 = 0; //â†‘
			while(left < (amountOfIslands+1) || pause == true) {
				
				if(round==0) {
					addIsland(curx, cury, left);
					left++;
				}
				
				if(left < (amountOfIslands+1)) { //â†‘
					for(int a = 0; a != addy2; a++) {
						if(left < (amountOfIslands+1)) {
							cury-=(issize+spaceBetweenIslands);
							addIsland(curx, cury, left);
							left++;
						}else break;
					}
				}
				
				if(left < (amountOfIslands+1)) { //â†�
					for(int a = 0; a != addx1; a++) {
						if(left < (amountOfIslands+1)) {						
							curx-=(issize+spaceBetweenIslands);
							addIsland(curx, cury, left);
							left++;
						}else break;
					}			
				}
				
				if(left < (amountOfIslands+1)) { //â†“
					for(int a = 0; a != addy1; a++) {
						if(left < (amountOfIslands+1)) {						
							cury+=(issize+spaceBetweenIslands);
							addIsland(curx, cury, left);
							left++;
						}else break;
					}	
				}
				
				if(left < (amountOfIslands+1)) { //â†’
					for(int a = 0; a != addx2; a++) {
						if(left < (amountOfIslands+1)) {
							curx+=(issize+spaceBetweenIslands);
							addIsland(curx, cury, left);
							left++;
						}else break;
					}
				}
				addy2+=2; //â†‘
				addx1+=2; //â†�
				addy1+=2; //â†“
				addx2+=2; //â†’
				round++;
			}
			
			if(generateDatabank) {			
				/*
				 * TODO: Exportieren der Island-Databank-File fÃ¼r Ãœbernahme auf dem Server
				 */
				try {
					cfg.save(file);
					System.out.println("Datei Gespeichert!");
					return true;
				} catch (IOException e) { e.printStackTrace(); return false;}
			}else return true;
		}else return false;
	}
	
	
	public static void addIsland(int x, int y, int id) {
		Island island = new Island(x,y, id);
		if(generateDatabank) {
			cfg.set("Islands.ID-"+id+".Claimed", false);
			cfg.set("Islands.ID-"+id+".Owner UUID", "none");
			cfg.set("Islands.ID-"+id+".World", "skyblockworld");
			cfg.set("Islands.ID-"+id+".LocX", x);
			cfg.set("Islands.ID-"+id+".LocZ", y);
		}
		IndexFileGenerator.islands.add(island);
	}
	
	public static class Island {
		
		private int x,y,id;
		Random r = new Random();
		Color c = new Color(r.nextInt(255),r.nextInt(255),r.nextInt(255));
		
		public Island(int x, int y, int id) {
			this.x = x;
			this.y = y;
			this.id = id;
			
			if(id >= 1 && id <= 5) c = Color.LIGHT_GRAY;
			else if(id >= 6 && id <= 17) c = Color.CYAN;
			else if(id >= 18 && id <= 37) c = Color.pink;
			else if(id >= 38 && id <= 65) c = Color.BLUE;
			else if(id >= 66 && id <= 101) c = Color.GREEN;
		}
		
		public boolean render(Graphics g) {
			if((x+(int)(issize-SkyBlockAdminTool.getCamera().getX())) > 0 &&
					(x+(int)(issize-SkyBlockAdminTool.getCamera().getX())) < SkyBlockAdminTool.getInstance().getWindow().frame.getWidth() &&
					(y+(int)(issize-SkyBlockAdminTool.getCamera().getY())) > 0 &&
					(y+(int)(issize-SkyBlockAdminTool.getCamera().getY())) < SkyBlockAdminTool.getInstance().getWindow().frame.getHeight()) {
				g.setColor(c);
				g.drawRect(x, y, (int)(issize-SkyBlockAdminTool.getCamera().getOffset()), (int)(issize-SkyBlockAdminTool.getCamera().getOffset()));
				g.setColor(Color.BLACK);
				g.drawString(id+"", x+((int)((issize-SkyBlockAdminTool.getCamera().getOffset())/2)), y+10);
				g.setColor(Color.WHITE);
				g.drawString(id+"", x+((int)((issize-SkyBlockAdminTool.getCamera().getOffset())/2))+2, y+11);
				g.setColor(c);
				g.fillRect((int)(x+(issize/2)+1-SkyBlockAdminTool.getCamera().getOffset()), (int)(y+(issize/2)+1-SkyBlockAdminTool.getCamera().getOffset()), 1, 1);
				
//				g.setColor(Color.WHITE);
//				g.drawLine((int)(x+(issize/2)+1-SkyBlockAdminTool.getCamera().getOffset()),
//						(int)(y+(issize/2)+1-SkyBlockAdminTool.getCamera().getOffset()),
//						(int)(x+(issize/2)+1-SkyBlockAdminTool.getCamera().getOffset())-(issize/2),
//						(int)(y+(issize/2)+1-SkyBlockAdminTool.getCamera().getOffset()));
				return true;
			}
			return false;
		}
		
	}
	
}
