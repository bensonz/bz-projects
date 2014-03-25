package edu.cmu.cs.cs214.hw2.items.animals;

import javax.swing.ImageIcon;

import edu.cmu.cs.cs214.hw2.Food;
import edu.cmu.cs.cs214.hw2.Location;
import edu.cmu.cs.cs214.hw2.Util;
import edu.cmu.cs.cs214.hw2.World;
import edu.cmu.cs.cs214.hw2.ai.AI;
import edu.cmu.cs.cs214.hw2.commands.Command;
import edu.cmu.cs.cs214.hw2.items.LivingItem;

/**
 * the {@link Human} is an {@link ArenaAnimal} that would try to gather wood and
 * eat fox and rabbits
 */
public class Human extends AbstractMoveActor {

	private static final int INITIAL_ENERGY = 100;
	private static final int MAX_ENERGY = 250;
	private static final int STRENGTH = 150;
	private static final int VIEW_RANGE = 5;
	private static final int MIN_BREEDING_ENERGY = 100;
	private static final int COOLDOWN = 2;
	private static final ImageIcon humanImage = Util.loadImage("Human.png");

	private final AI ai;

	private int woodStock;
	private Location loc;
	private int energy;

	public Human(AI ai, Location loc) {
		super(INITIAL_ENERGY, MAX_ENERGY, STRENGTH, VIEW_RANGE,
				MIN_BREEDING_ENERGY, COOLDOWN, humanImage, false, loc);
		this.ai = ai;
		this.loc = loc;
		this.woodStock = 9;
		this.energy = INITIAL_ENERGY;
	}

	public int getWoodStock() {
		return woodStock;
	}

	public void setWoodStock(int woodStock) {
		this.woodStock += woodStock;
	}

	@Override
	public void eat(Food food) {
		if (food.toString().equals("Wood")) {
			setWoodStock(1);
		}
		energy += food.getMeatCalories();
		energy += food.getPlantCalories();
	}

	@Override
	public Command getNextAction(World world) {
		Command next = ai.getNextAction(world, this);
		this.energy--;
		return next;
	}

	@Override
	public LivingItem breed() {
		Human child = new Human(ai, loc);
		child.energy = energy / 2;
		this.energy = energy / 2;
		return child;
	}

	@Override
	public String getName() {
		return "Human";
	}
}