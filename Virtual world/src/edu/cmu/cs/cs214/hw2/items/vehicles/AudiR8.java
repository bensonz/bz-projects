package edu.cmu.cs.cs214.hw2.items.vehicles;

import javax.swing.ImageIcon;

import edu.cmu.cs.cs214.hw2.Direction;
import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.Util;

public class AudiR8 extends AbstractVehicles {

	private static final int OIL = 500;//energy for car lol
	private static final int STRENGTH = 200;
	private static final int DIR_CHANGE_MOMENTUM = 7;
	private static final int VIEW_RANGE = 5;
	private static final String NAME = "AudiR8";
	private static final ImageIcon myImage = Util.loadImage("AudiR8.jpg");
	private static final int INITIAL_COOLDOWN = 7;
	private static final Direction intitialDir = Direction.NORTH;

	public AudiR8(Location loc) {
		super(OIL,STRENGTH, INITIAL_COOLDOWN, DIR_CHANGE_MOMENTUM, VIEW_RANGE,
				NAME, myImage, loc,intitialDir );
	}

}