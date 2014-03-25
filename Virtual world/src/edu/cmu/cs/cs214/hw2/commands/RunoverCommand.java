package edu.cmu.cs.cs214.hw2.commands;

import java.util.Set;

import edu.cmu.cs.cs214.hw2.Direction;
import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.Util;
import edu.cmu.cs.cs214.hw2.World;
import edu.cmu.cs.cs214.hw2.items.Item;
import edu.cmu.cs.cs214.hw2.items.MoveableItem;

/**
 * IGNORE THIS FILE. A Runover command is a {@link Command} which represents a {@link MovableItem}
 * moving through a couple blocks. This Command moves that Item from one space
 * in the world to another, while killing everything or dying
 */
public final class RunoverCommand implements Command {

	private final MoveableItem item;
	private Location targetLocation;

	/**
	 * Construct a {@link RunoverCommand}, where <code>item</code> is the moving
	 * item and <code>targetLocation</code> is the location that
	 * <code> item </code> is moving to. The target location must be within
	 * <code>item</code>'s moving range and the target location must be empty
	 * and valid.
	 * 
	 * @param item
	 *            the Item that is moving
	 * @param targetLocation
	 *            the location that Item is moving to
	 */
	public RunoverCommand(MoveableItem item, Location targetLocation) {
		this.item = item;
		this.targetLocation = targetLocation;
	}

	@Override
	public void execute(World world) throws InvalidCommandException {
		// If the item is dead, then it will not move.
		if (item.isDead()) {
			return;
		}
		
		if (!Util.isValidLocation(world, targetLocation)
                || !Util.isLocationEmpty(world, targetLocation)) {
            throw new InvalidCommandException(
                    "Invalid MoveCommand: Invalid/non-empty target location");
        }
        if (item.getMovingRange() < targetLocation.getDistance(item.getLocation())) {
            throw new InvalidCommandException(
                    "Invalid MoveCommand: Target location farther than moving range");
        }

		Location now = item.getLocation();
		Direction dir = Util.getDirectionTowards(now, targetLocation);
		Item stuff = null;
		Location next = new Location(now, dir);
		for (int i = 0; i < now.getDistance(targetLocation); i++) {
			// check if anything on my way
			if (Util.isValidLocation(world, next)) {
				if (!Util.isLocationEmpty(world, next)) {
					// something on my way
					stuff = checkLoc(world, next);
					if (stuff != null
							&& stuff.getStrength() > item.getStrength()) {
						item.loseEnergy(1000000);
					} else if (stuff != null
							&& stuff.getStrength() < item.getStrength()) {
						stuff.loseEnergy(1000000);
					} else if (stuff != null
							&& stuff.getStrength() == item.getStrength()) {
						targetLocation = next;
						break;
					}
				}
			}
			next = new Location(next, dir);
		}
		if (stuff == null) {
			// seems like nothing on my way
			item.moveTo(targetLocation);
		}
	}

	private Item checkLoc(World world, Location next) {
		Set<Item> around = world.searchSurroundings(item.getLocation(),
				item.getMovingRange());
		for (Item it : around) {
			if (it.getLocation().equals(next)) {
				return it;
			}
		}
		return null;
	}
}