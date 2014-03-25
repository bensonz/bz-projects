package edu.cmu.cs.cs214.hw2.items.animals;

import java.util.Set;

import javax.swing.ImageIcon;

import edu.cmu.cs.cs214.hw2.Direction;
import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.Util;
import edu.cmu.cs.cs214.hw2.World;
import edu.cmu.cs.cs214.hw2.commands.Command;
import edu.cmu.cs.cs214.hw2.commands.MoveCommand;
import edu.cmu.cs.cs214.hw2.commands.WaitCommand;
import edu.cmu.cs.cs214.hw2.items.Item;
import edu.cmu.cs.cs214.hw2.items.LivingItem;

/**
 * this class of superman extends AbstractMoveActor and superman does go towards
 * fox and wolf but doesnt kill em more act like a blocking effect
 * 
 * @author bz
 * 
 */
public class Superman extends AbstractMoveActor {

	private static final int INITIAL_ENERGY = 200;
	private static final int MAX_ENERGY = 1000;
	private static final int STRENGTH = 500;
	private static final int VIEW_RANGE = 10;
	private static final int MIN_BREEDING_ENERGY = 10000;// cannot breed
	private static final int COOLDOWN = 1;
	private static final ImageIcon supermanImage = Util
			.loadImage("superman.gif");

	private Location location;
	private int energy;

	public Superman(Location loc) {
		super(INITIAL_ENERGY, MAX_ENERGY, STRENGTH, VIEW_RANGE,
				MIN_BREEDING_ENERGY, COOLDOWN, supermanImage, false, loc);
		this.location = loc;
		this.energy = INITIAL_ENERGY;
	}

	@Override
	public Command getNextAction(World world) {
		Set<Item> items = world.searchSurroundings(this);
		Direction dir = null;
		for (Item i : items) {
			if (i.getName().equals("Fox") || i.getName().equals("Wolf")) {
				dir = Util.getDirectionTowards(location, i.getLocation());
				break;
			}
		}
		if (dir != null) {
			Location tl = new Location(location, dir);
			if (Util.isValidLocation(world, tl)
					&& Util.isLocationEmpty(world, tl)) {
				return new MoveCommand(this, tl);
			}
		}
		return new WaitCommand();
	}

	@Override
	public void loseEnergy(int energy) {
		// superman doesn't die!
		return;
	}

	@Override
	public LivingItem breed() {
		return null;
	}

	@Override
	public String getName() {
		return "superMan";
	}

}