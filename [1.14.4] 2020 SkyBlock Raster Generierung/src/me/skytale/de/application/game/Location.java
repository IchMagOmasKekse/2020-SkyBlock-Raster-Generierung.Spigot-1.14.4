package me.skytale.de.application.game;

public class Location {
	
	public double x=0;
	public double y=0;
	public double z=0;
	
	public Location(double x, double y) {
		this.x=x;
		this.y=y;
	}
	public Location() {}
	public Location clone() {
		return new Location(getX(), getY());
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;
	}
	public void add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}
	
	
}
