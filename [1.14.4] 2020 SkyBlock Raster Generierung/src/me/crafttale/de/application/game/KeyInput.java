package me.crafttale.de.application.game;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import me.crafttale.de.application.game.skyblock.IndexFileGenerator;


public class KeyInput extends KeyAdapter {
	
	public List<String> cooldown_bypass = new LinkedList<String>();
	public ConcurrentLinkedQueue<Integer> pressed_keys = new ConcurrentLinkedQueue<Integer>();
	
	public static String text = "";
	
	public float move_speed = 3f;
	
	public KeyInput() {
		addBypass("W", "A", "S", "D");
//		addBypass("Oben", "Links", "Unten", "Rechts");
		addBypass("Leertaste", "Umschalt", "Steuerung", "Escape");
	}
	public void addBypass(String... letter) {
		for(String s : letter) if(cooldown_bypass.contains(s) == false) cooldown_bypass.add(s);
	}
	
	private int ticked = 0;
	public void tick() {
		ticked++;
		if(ticked == 1) ticked = 0;
		else return;
		for(int key : pressed_keys) {
			switch(key) {
			case KeyEvent.VK_W:
				break;
			case KeyEvent.VK_A:
				break;
			case KeyEvent.VK_S:
				break;
			case KeyEvent.VK_D:
				break;
			case KeyEvent.VK_P:
				SkyBlockAdminTool.getCamera().biggerOffset();
				break;
			case KeyEvent.VK_O:
				SkyBlockAdminTool.getCamera().smallerOffset();
				break;
			case KeyEvent.VK_RIGHT:
				SkyBlockAdminTool.getCamera().addX(1);
				break;
			case KeyEvent.VK_LEFT:
				SkyBlockAdminTool.getCamera().addX(-1);
				break;
			case KeyEvent.VK_UP:
				SkyBlockAdminTool.getCamera().addY(-1);
				break;
			case KeyEvent.VK_DOWN:
				SkyBlockAdminTool.getCamera().addY(1);
				break;
			case KeyEvent.VK_PLUS:
				SkyBlockAdminTool.fps_maximal++;
				break;
			case KeyEvent.VK_MINUS:
				SkyBlockAdminTool.fps_maximal--;
				break;
			case KeyEvent.VK_L:
				release(key);
				break;
			case KeyEvent.VK_0:
				text+="0";
				pressed_keys.remove(key);
				break;
			case KeyEvent.VK_1:
				text+="1";
				pressed_keys.remove(key);
				break;
			case KeyEvent.VK_2:
				text+="2";
				pressed_keys.remove(key);
				break;
			case KeyEvent.VK_3:
				text+="3";
				pressed_keys.remove(key);
				break;
			case KeyEvent.VK_4:
				text+="4";
				pressed_keys.remove(key);
				break;
			case KeyEvent.VK_5:
				text+="5";
				pressed_keys.remove(key);
				break;
			case KeyEvent.VK_6:
				text+="6";
				pressed_keys.remove(key);
				break;
			case KeyEvent.VK_7:
				text+="7";
				pressed_keys.remove(key);
				break;
			case KeyEvent.VK_8:
				text+="8";
				pressed_keys.remove(key);
				break;
			case KeyEvent.VK_9:
				text+="9";
				pressed_keys.remove(key);
				break;
			case KeyEvent.VK_BACK_SPACE:
				text = text.substring(0, text.length()-1);
				pressed_keys.remove(key);
				break;
			case KeyEvent.VK_ENTER:
				if(text.equals("")) break;
				IndexFileGenerator.pause = true;
				IndexFileGenerator.amountOfIslands = Integer.valueOf(text);
				IndexFileGenerator.generate();
				text="";
				pressed_keys.remove(key);
				break;
			}
		}
	}
	int height = 20;
	int space = 2;
	int amount = 1;
	public void render(Graphics g) {
		if(pressed_keys.isEmpty()) {
			g.drawString("Kein Cooldown registriert", 10, 10+space+height);
		}else {
			for(int key : pressed_keys) {
				g.drawString("Key: "+KeyEvent.getKeyText(key), 10, 10+((amount+1)*space)+(amount*height));
				amount++;
			}
		}
		amount=1;
	}
	public void press(int key) {
		if(pressed_keys.contains(key) == false) {
			pressed_keys.add(key);
		}
	}
	public void release(int key) {
		pressed_keys.remove(key);
	}
	
	//TODO: Get Interaction
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		press(key);
	}
	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		release(key);
	}
	
	
}
