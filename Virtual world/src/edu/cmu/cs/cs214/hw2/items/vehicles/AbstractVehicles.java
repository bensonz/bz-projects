package edu.cmu.cs.cs214.hw2.items.vehicles;

import java.util.Set;

import javax.swing.ImageIcon;

import edu.cmu.cs.cs214.hw2.Actor;
import edu.cmu.cs.cs214.hw2.Direction;
import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.Util;
import edu.cmu.cs.cs214.hw2.World;
import edu.cmu.cs.cs214.hw2.commands.Command;
import edu.cmu.cs.cs214.hw2.commands.MoveCommand;
import edu.cmu.cs.cs214.hw2.commands.WaitCommand;
import edu.cmu.cs.cs214.hw2.items.Item;
import edu.cmu.cs.cs214.hw2.items.MoveableItem;

public abstract class AbstractVehicles implements MoveableItem, Actor {

	private final int strength;
	private final int dirChange;
	private final int viewRange;
	private final String name;
	private final ImageIcon image;

	private int oil;
	private Location location;
	private Direction curDir;
	private int coolDown;

	public AbstractVehicles(int oil, int strength, int initialCoolDown,
			int dirChangeMomentum, int viewRange, String name, ImageIcon image,
			Location loc, Direction curDir) {
		this.oil = oil;
		this.strength = strength;
		this.dirChange = dirChangeMomentum;
		this.viewRange = viewRange;
		this.name = name;
		this.image = image;
		this.location = loc;
		this.coolDown = initialCoolDown;
		this.curDir = curDir;
	}

	public Command getNextAction(World world) {
		oil--;
		if (coolDown > dirChange) {
			curDir = changeDir(curDir, curDir);
		}
		Location loc = new Location(location, curDir);
		if (!Util.isValidLocation(world, loc)) {
			this.oil = 0;
			return new WaitCommand();
		}
		Set<Item> around = world.searchSurroundings(location,
				this.getViewRange());
		Item it = getItemAtLoc(around, loc);
		if (it == null) {
			// nothing in my way, speed up and move!
			coolDown --;
			return new MoveCommand(this, loc);
		} else {
			if (coolDown < 6 || curDir.equals(Util.getRandomDirection())) {
				coolDown++;
			}
			// there's something there
			if (it.getStrength() > this.getStrength()) {
				this.oil = 0;
				return new WaitCommand();
			} else {
				it.loseEnergy(Integer.MAX_VALUE);
				return new MoveCommand(this, loc);
			}
		}
	}

	private Direction changeDir(Direction dir, Direction aintGoing) {
		if (dir == null || aintGoing == null) {
			throw new IllegalStateException("direction null");
		} else {
			Direction[] lol = new Direction[4];
			lol[0] = Direction.NORTH;
			lol[1] = Direction.EAST;
			lol[2] = Direction.SOUTH;
			lol[3] = Direction.WEST;
			for (Direction d : lol) {
				if (!d.equals(dir) && !d.equals(aintGoing)) {
					return d;
				}
			}
			throw new IllegalStateException("wtf?");
		}
	}

	private Item getItemAtLoc(Set<Item> around, Location check) {
		for (Item it : around) {
			if (it.getLocation().equals(check)) {
				return it;
			}
		}
		return null;
	}

	public ImageIcon getImage() {
		return image;
	}

	public String getName() {
		return name;
	}

	public Location getLocation() {
		return location;
	}

	public int getStrength() {
		return strength;
	}

	public void loseEnergy(int energyLoss) {
		oil -= energyLoss;
	}

	public boolean isDead() {
		return oil <= 0;
	}

	public int getCoolDownPeriod() {
		return coolDown;
	}

	public int getDirChangeMomentum() {
		return dirChange;
	}

	public int getViewRange() {
		return viewRange;
	}

	public void moveTo(Location targetLocation) {
		location = targetLocation;
	}

	public int getMovingRange() {
		return 1;
	}

	public int getPlantCalories() {
		return 0;
	}

	public int getMeatCalories() {
		return 0;
	}
}