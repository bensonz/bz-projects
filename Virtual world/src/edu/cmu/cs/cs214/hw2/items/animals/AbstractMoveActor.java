package edu.cmu.cs.cs214.hw2.items.animals;

import javax.swing.ImageIcon;

import edu.cmu.cs.cs214.hw2.Food;
import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.World;
import edu.cmu.cs.cs214.hw2.commands.Command;
import edu.cmu.cs.cs214.hw2.items.LivingItem;

/**
 * this is an abstract for all moving actors meaning this can be fox and rabbit
 * too but I chose not to modify the give files
 * 
 * @author bz
 * 
 */
public abstract class AbstractMoveActor implements ArenaAnimal {

	private final int maxEnergy;
	private final int strength;
	private final int viewRange;
	private final int minBreedingEnergy;
	private final int coolDown;
	private final ImageIcon image;
	private boolean isPlant;

	private Location location;
	private int energy;

	abstract public Command getNextAction(World world);

	abstract public LivingItem breed();

	abstract public String getName();

	public AbstractMoveActor(int initEnergy, int maxEnergy, int strength,
			int viewRange, int minBreedingEnergy, int coolDown,
			ImageIcon image, boolean isPlant, Location loc) {
		this.energy = initEnergy;
		this.maxEnergy = maxEnergy;
		this.strength = strength;
		this.viewRange = viewRange;
		this.minBreedingEnergy = minBreedingEnergy;
		this.coolDown = coolDown;
		this.image = image;
		this.isPlant = isPlant;
		this.location = loc;
	}

	public ImageIcon getImage() {
		return image;
	}

	public int getEnergy() {
		return energy;
	}

	public void eat(Food food) {
		energy = Math.min(maxEnergy, energy + food.getMeatCalories());
	}

	public void moveTo(Location targetLocation) {
		location = targetLocation;
	}

	public int getMovingRange() {
		return 1;
	}

	public Location getLocation() {
		return location;
	}

	public int getStrength() {
		return strength;
	}

	public void loseEnergy(int energy) {
		this.energy -= energy;
	}

	public boolean isDead() {
		return (energy <= 0);
	}

	public int getPlantCalories() {
		if (isPlant) {
			return energy;
		} else {
			return 0;
		}
	}

	public int getMeatCalories() {
		if (isPlant) {
			return 0;
		} else {
			return energy;
		}
	}

	public int getCoolDownPeriod() {
		return coolDown;
	}

	public int getMaxEnergy() {
		return maxEnergy;
	}

	public int getViewRange() {
		return viewRange;
	}

	public int getMinimumBreedingEnergy() {
		return minBreedingEnergy;
	}

}