package edu.cmu.cs.cs214.hw2.items;

import javax.swing.ImageIcon;

/**
 * this is an abstract for all non moving Item.
 * I did not modify the grass to extend this class
 */

import edu.cmu.cs.cs214.hw2.Location;

public abstract class NonMovingItem implements Item{
	private final int energy;
	private final int strength;
	private final ImageIcon image;
	private final String name;
	
	private boolean isDead = false;
	private Location location;
	
	public NonMovingItem(int energy, int strength, ImageIcon image, String name, Location location){
		this.energy = energy;
		this.strength = strength;
		this.image = image;
		this.name = name;
		this.location = location;
	}
	
	public int getPlantCalories(){
		return energy;
	}
    public int getMeatCalories(){
    	return 0;
    }
	public int getEnergy(){
		return energy;
	}
	public ImageIcon getImage(){
		return image;
	}
	public String getName(){
		return name;
	}
	public Location getLocation(){
		return location;
	}
	public int getStrength(){
		return strength;
	}
	public void loseEnergy(int energy){
		this.isDead = true;
	}
	public boolean isDead(){
		return isDead;
	}
}