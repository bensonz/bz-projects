package edu.cmu.cs.cs214.hw2.items.vehicles;

import javax.swing.ImageIcon;

import edu.cmu.cs.cs214.hw2.Direction;
import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.Util;

/**
 * The AbstractVehicles is an abstract class that has AI and all the code for
 * all vehicles
 * 
 */
public class McLaren extends AbstractVehicles {

	private static final int OIL = 500;// energy for car lol
	private static final int STRENGTH = 300;
	private static final int DIR_CHANGE_MOMENTUM = 2;
	private static final int VIEW_RANGE = 3;
	private static final String NAME = "McLaren";
	private static final ImageIcon myImage = Util.loadImage("McLaren.jpg");
	private static final int INITIAL_COOLDOWN = 6;
	private static final Direction intitialDir = Direction.EAST;

	public McLaren(Location loc) {
		super(OIL, STRENGTH, INITIAL_COOLDOWN, DIR_CHANGE_MOMENTUM, VIEW_RANGE,
				NAME, myImage, loc, intitialDir);
	}

}