package edu.cmu.cs.cs214.hw2.items;

import javax.swing.ImageIcon;

import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.Util;

public class House extends NonMovingItem{
	private final static ImageIcon houseImage = Util.loadImage("house.png");
	
	private static final int initialEnergy = 1000;
	private static final int strength = 15;
	private static final String name = "House";
	
	private int energy;
	
	public House(Location loc){
		super(initialEnergy,strength,houseImage,name,loc);
		energy = initialEnergy;
	}
	
	@Override
	public void loseEnergy(int energyLoss){
		this.energy -= energyLoss;
	}
	@Override
	public boolean isDead(){
		return this.energy <= 0;
	}
}