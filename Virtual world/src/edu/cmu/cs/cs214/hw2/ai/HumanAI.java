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
 * my human AI, moves around, gather woods, avoid Wolf, eat rabbits or fox
 */
public class HumanAI implements AI {

	private Location location;

	@Override
	public Command getNextAction(ArenaWorld world, ArenaAnimal animal) {
		location = animal.getLocation();
		Set<Item> around = world.searchSurroundings(animal);
		int[] distance = new int[4];
		for (int i = 0; i < 3; i++) {
			distance[i] = animal.getViewRange();
		}
		Item[] closest = new Item[4];
		for (Item it : around) {
			String name = it.getName();
			Location hi = it.getLocation();
			if (name.equals("Wolf")) {
				if (hi.getDistance(location) < distance[0]) {
					distance[0] = hi.getDistance(location);
					closest[0] = it;
				}
			} else if (name.equals("Wood")) {
				if (hi.getDistance(location) < distance[1]) {
					distance[1] = hi.getDistance(location);
					closest[1] = it;
				}
			} else if (name.equals("Rabbit") || name.equals("Fox")) {
				if (hi.getDistance(location) < distance[2]) {
					distance[2] = hi.getDistance(location);
					closest[2] = it;
				}
			}
		}
		if (closest[0] != null) {
			// wolf around! run!!
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
		if (animal.getEnergy() <= animal.getMaxEnergy() / 2
				&& (closest[1] != null || closest[2] != null || closest[3] != null)) {
			// wood!! go get it!, food! eat!
			Item w = closest[1];
			if (w == null) {
				w = closest[2];
			}
			if (w == null) {
				w = closest[3];
			}
			if (w == null) {
				throw new IllegalStateException("fml");
			}
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
			Direction dir = Direction.SOUTH;
			Location tl = new Location(location, dir);
			Command next = tryMoving(around, world, animal, tl);
			if (next != null) {
				return next;
			} else {
				Direction newDir = dir;
				for (int i = 0; i < 4; i++) {
					newDir = changeDir(newDir, dir);
					tl = new Location(location, newDir);
					next = tryMoving(around, world, animal, tl);
					if (next != null) {
						return next;
					}
				}
			}
		}
		if (animal.getEnergy() >= animal.getMinimumBreedingEnergy() + 20) {
			Direction dir = Direction.SOUTH;
			Location tl = new Location(location, dir);
			Command next = tryMoving(around, world, animal, tl);
			if (next != null) {
				return new BreedCommand(animal, tl);
			} else {
				Direction newDir = dir;
				for (int i = 0; i < 4; i++) {
					newDir = changeDir(newDir, dir);
					tl = new Location(location, newDir);
					next = tryMoving(around, world, animal, tl);
					if (next != null) {
						break;
					}
				}
				return new BreedCommand(animal, tl);
			}
		}
		return new WaitCommand();
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
		for (Item check : around) {
			if (check.getLocation().equals(tl)) {
				thingsInMyWay = true;
				if (check.getStrength() < animal.getStrength()) {
					return new EatCommand(animal, check);
				}
			}
		}
		if (!thingsInMyWay) {
			return new MoveCommand(animal, tl);
		}
		return null;
	}
}
