package me.crafttale.de.application.game.objects;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameObject extends Rectangle {

	private static final long serialVersionUID = 1L;
	
	public GameObject() {
		// TODO Auto-generated constructor stub
	}
	public abstract void render(Graphics g);
	public abstract void tick();
	
	
}
