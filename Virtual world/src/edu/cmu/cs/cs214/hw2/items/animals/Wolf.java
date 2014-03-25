package edu.cmu.cs.cs214.hw2.items.animals;

import java.util.Set;

import javax.swing.ImageIcon;

import edu.cmu.cs.cs214.hw2.Direction;
import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.Util;
import edu.cmu.cs.cs214.hw2.World;
import edu.cmu.cs.cs214.hw2.commands.Command;
import edu.cmu.cs.cs214.hw2.commands.EatCommand;
import edu.cmu.cs.cs214.hw2.commands.MoveCommand;
import edu.cmu.cs.cs214.hw2.commands.WaitCommand;
import edu.cmu.cs.cs214.hw2.items.Item;
import edu.cmu.cs.cs214.hw2.items.LivingItem;

/**
 * this is a wolf class. it moves randomly and eat whatever if can find(only
 * meat)
 * 
 * @author bz
 * 
 */
public class Wolf extends AbstractMoveActor {

	private static final int INITIAL_ENERGY = 100;
	private static final int MAX_ENERGY = 500;
	private static final int STRENGTH = 200;
	private static final int VIEW_RANGE = 8;
	private static final int MIN_BREEDING_ENERGY = 400;
	private static final int COOLDOWN = 2;
	private static final ImageIcon supermanImage = Util.loadImage("wolf.jpg");

	private Location location;
	private int energy;

	public Wolf(Location loc) {
		super(INITIAL_ENERGY, MAX_ENERGY, STRENGTH, VIEW_RANGE,
				MIN_BREEDING_ENERGY, COOLDOWN, supermanImage, false, loc);
		this.location = loc;
		this.energy = INITIAL_ENERGY;
	}

	@Override
	public Command getNextAction(World world) {
		Direction dir = Util.getRandomDirection();
		Location targetLocation = new Location(this.getLocation(), dir);

		if (Util.isValidLocation(world, targetLocation)) {
			if (Util.isLocationEmpty(world, targetLocation)) {
				return new MoveCommand(this, targetLocation);
			} else {
				Set<Item> around = world.searchSurroundings(this);
				for (Item it : around) {
					if (it.getLocation().equals(targetLocation)
							&& it.getStrength() < this.getStrength()) {
						return new EatCommand(this, it);
					}
				}
			}
		}
		return new WaitCommand();
	}

	@Override
	public LivingItem breed() {
		Wolf child = new Wolf(location);
		child.energy = energy / 2;
		this.energy = energy / 2;
		return child;
	}

	@Override
	public String getName() {
		return "Wolf";
	}

}