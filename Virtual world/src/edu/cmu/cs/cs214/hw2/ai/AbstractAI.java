package edu.cmu.cs.cs214.hw2.ai;

import java.util.Set;

import edu.cmu.cs.cs214.hw2.ArenaWorld;
import edu.cmu.cs.cs214.hw2.Direction;
import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.Util;
import edu.cmu.cs.cs214.hw2.commands.BreedCommand;
import edu.cmu.cs.cs214.hw2.commands.Command;
import edu.cmu.cs.cs214.hw2.commands.EatCommand;
import edu.cmu.cs.cs214.hw2.commands.MoveCommand;
import edu.cmu.cs.cs214.hw2.commands.WaitCommand;
import edu.cmu.cs.cs214.hw2.items.Item;
import edu.cmu.cs.cs214.hw2.items.animals.ArenaAnimal;

/**
 * This is the abstract AI, incorporated both FoxAI and RabbitAI
 */
public abstract class AbstractAI implements AI {
	private final boolean eatPlant;
	private Location location;

	public AbstractAI(boolean eatPlant) {
		this.eatPlant = eatPlant;
	}

	@Override
	public Command getNextAction(ArenaWorld world, ArenaAnimal animal) {
		// initializing stuff
		location = animal.getLocation();
		Set<Item> around = world.searchSurroundings(animal);
		int[] distance = new int[2];
		for (int i = 0; i < 2; i++) {
			distance[i] = animal.getViewRange();
		}
		Item[] closest = new Item[2];
		for (Item it : around) {
			int strength = it.getStrength();
			Location hi = it.getLocation();
			int d = location.getDistance(hi);
			if (eatPlant) {
				if (strength < animal.getStrength()
						&& it.getPlantCalories() != 0 && d < distance[0]) {
					distance[1] = d;
					closest[1] = it;
				} else if (strength > animal.getStrength() && d < distance[1]) {
					distance[0] = d;
					closest[0] = it;
				}
			} else {
				if (strength < animal.getStrength()
						&& it.getPlantCalories() == 0 && d < distance[0]) {
					distance[1] = d;
					closest[1] = it;
				} else if (strength > animal.getStrength() && d < distance[1]) {
					distance[0] = d;
					closest[0] = it;
				}
			}
		}
		// done initializing, surronding confirmed
		if (closest[0] != null) {
			// danger around!!! omg go run!
			Item w = closest[0];
			Direction dir = Util.getDirectionTowards(location, w.getLocation());
			assert (dir != null);
			Direction newDir = changeDir(dir, dir);
			Location tl = new Location(location, newDir);
			Command next = tryMoving(around, world, animal, tl);
			if (next != null) {
				return next;
			} else {
				for (int i = 0; i < 3; i++) {
					// only 4 directions we can try at most
					newDir = changeDir(newDir, dir);
					tl = new Location(location, newDir);
					next = tryMoving(around, world, animal, tl);
					if (next != null) {
						return next;
					}
				}
			}
			// can't go anywhere, wait..
			return new WaitCommand();
		}
		if (closest[1] != null) {
			// I see food! time for crave
			Item w = closest[1];
			Direction dir = Util.getDirectionTowards(location, w.getLocation());
			assert (dir != null);
			Location tl = new Location(location, dir);
			Command next = tryMoving(around, world, animal, tl);
			if (next != null) {
				return next;
			} else {
				Direction newDir = dir;
				for (int i = 0; i < 4; i++) {
					// only 4 directions we can try at most
					newDir = changeDir(newDir, dir);
					tl = new Location(location, newDir);
					next = tryMoving(around, world, animal, tl);
					if (next != null) {
						return next;
					}
				}
			}
			// can't go anywhere, wait..
			return new WaitCommand();
		}
		if (animal.getEnergy() <= 50) {
			// almost dying letmme just move randomly
			Location tl = getEmptyNearbyLoc(world, around);
			if (tl != null) {
				return new MoveCommand(animal, tl);
			}
		}
		if (animal.getEnergy() >= animal.getMinimumBreedingEnergy() + 20) {
			// make sure after breeding we don't have too little energy so that
			// we died
			Location tl = getEmptyNearbyLoc(world, around);
			if (tl != null) {
				return new BreedCommand(animal, tl);
			}
		}
		return new WaitCommand();
	}

	private Location getEmptyNearbyLoc(ArenaWorld world, Set<Item> around) {
		Direction[] dir = Direction.values();
		Location tl = null;
		for (Direction heyhey : dir) {
			tl = new Location(location, heyhey);
			if (Util.isValidLocation(world, tl) && isEmpty(around, tl)) {
				return tl;
			}
		}
		return null;
	}

	private boolean isEmpty(Set<Item> around, Location toGo) {
		for (Item it : around) {
			if (it.getLocation().equals(toGo)) {
				return false;
			}
		}
		return true;
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

	private Command tryMoving(Set<Item> around, ArenaWorld world,
			ArenaAnimal animal, Location tl) {
		if (!Util.isValidLocation(world, tl)) {
			return null;
		}
		boolean thingsInMyWay = false;
		for (Item it : around) {
			if (it.getLocation().equals(tl)) {
				thingsInMyWay = true;
				if (eatPlant) {
					if (animal.getStrength() > it.getStrength()
							&& it.getPlantCalories() != 0) {
						// hell yeah I can eat this shit
						return new EatCommand(animal, it);
					}
				} else {
					if (animal.getStrength() > it.getStrength()
							&& it.getPlantCalories() == 0) {
						// hell yeah I can eat this shit
						return new EatCommand(animal, it);
					}
				}
			}
		}
		if (!thingsInMyWay) {
			// i gotta move
			return new MoveCommand(animal, tl);
		}
		return null;
	}
}