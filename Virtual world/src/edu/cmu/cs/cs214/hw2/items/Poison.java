package edu.cmu.cs.cs214.hw2.items;

import javax.swing.ImageIcon;

import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.Util;

/**
 * This is wood, block animals' way
 */

public class Poison extends NonMovingItem{
	private final static ImageIcon poisonImage = Util.loadImage("poison.png");
	
	private static final int energy = -50;
	private static final int strength = 0;
	private static final String name = "Poison";

	public Poison(Location location){
		super(energy,strength,poisonImage,name,location);
	}
}