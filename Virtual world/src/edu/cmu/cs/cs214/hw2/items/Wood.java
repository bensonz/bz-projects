package edu.cmu.cs.cs214.hw2.items;

import javax.swing.ImageIcon;

import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.Util;

/**
 * This is wood, block animals' way
 */

public class Wood extends NonMovingItem{
	private final static ImageIcon woodImage = Util.loadImage("wood.gif");
	
	private static final int energy = 10;
	private static final int strength = 15;
	private static final String name = "Wood";

	public Wood(Location location){
		super(energy,strength,woodImage,name,location);
	}
}