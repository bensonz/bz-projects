package edu.cmu.cs.cs214.hw2.items.vehicles;

import javax.swing.ImageIcon;

import edu.cmu.cs.cs214.hw2.Direction;
import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.Util;

public class Maybach extends AbstractVehicles {

	private static final int OIL = 500;//energy for car lol
	private static final int STRENGTH = 350;
	private static final int DIR_CHANGE_MOMENTUM = 8;
	private static final int VIEW_RANGE = 10;
	private static final String NAME = "Maybach";
	private static final ImageIcon myImage = Util.loadImage("Maybach.jpg");
	private static final int INITIAL_COOLDOWN = 10;
	private static final Direction intitialDir = Direction.SOUTH;

	public Maybach(Location loc) {
		super(OIL,STRENGTH, INITIAL_COOLDOWN, DIR_CHANGE_MOMENTUM, VIEW_RANGE,
				NAME, myImage, loc,intitialDir );
	}

}