package edu.cmu.cs.cs214.hw2;

import javax.swing.SwingUtilities;

import edu.cmu.cs.cs214.hw2.ai.HumanAI;
import edu.cmu.cs.cs214.hw2.ai.FoxAI;
import edu.cmu.cs.cs214.hw2.ai.RabbitAI;
import edu.cmu.cs.cs214.hw2.items.Gardener;
import edu.cmu.cs.cs214.hw2.items.Grass;
import edu.cmu.cs.cs214.hw2.items.House;
import edu.cmu.cs.cs214.hw2.items.Poison;
import edu.cmu.cs.cs214.hw2.items.Wood;
import edu.cmu.cs.cs214.hw2.items.animals.Fox;
import edu.cmu.cs.cs214.hw2.items.animals.Gnat;
import edu.cmu.cs.cs214.hw2.items.animals.Human;
import edu.cmu.cs.cs214.hw2.items.animals.Rabbit;
import edu.cmu.cs.cs214.hw2.items.animals.Superman;
import edu.cmu.cs.cs214.hw2.items.animals.Wolf;
import edu.cmu.cs.cs214.hw2.items.vehicles.AudiR8;
import edu.cmu.cs.cs214.hw2.items.vehicles.Maybach;
import edu.cmu.cs.cs214.hw2.items.vehicles.McLaren;
import edu.cmu.cs.cs214.hw2.staff.WorldImpl;
import edu.cmu.cs.cs214.hw2.staff.WorldUI;

/**
 * The Main class initialize a world with some {@link Grass}, {@link Rabbit}s,
 * {@link Fox}es, {@link Gnat}s, {@link Gardener}, etc.
 * 
 * You may modify or add Items/Actors to the World.
 * 
 */
public class Main {

	static final int X_DIM = 40;
	static final int Y_DIM = 40;
	static final int SPACES_PER_GRASS = 7;
	static final int INITIAL_GRASS = X_DIM * Y_DIM / SPACES_PER_GRASS;
	static final int INITIAL_GNATS = INITIAL_GRASS / 4;
	static final int INITIAL_RABBITS = INITIAL_GRASS / 4;
	static final int INITIAL_FOXES = INITIAL_GRASS / 32;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Main().createAndShowWorld();
			}
		});
	}

	public void createAndShowWorld() {
		World world = new WorldImpl(X_DIM, Y_DIM);
		initialize(world);
		new WorldUI(world).show();
	}

	public void initialize(World world) {
		addGrass(world);
		world.addActor(new Gardener());

		// addGnats(world);
		//(world);
		addFoxes(world);
		addRabbits(world);
		// TODO: You may add your own creatures here!
		// add livingItems
		addHuman(world);
		addWolf(world);
		addSuperman(world);
		// add items
		addPoison(world);
		addWood(world);
		addHouse(world);
		//add vehicles
		addMaybach(world);
		addAudi(world);
		addMcLaren(world);
	}

	private void addMcLaren(World world) {
		for (int i = 0; i < 4; i ++){
			Location loc = Util.getRandomEmptyLocation(world);
			McLaren m = new McLaren(loc);
			world.addItem(m);
			world.addActor(m);
		}
	}

	private void addAudi(World world) {
		for (int i = 0; i < 4; i ++){
			Location loc = Util.getRandomEmptyLocation(world);
			AudiR8 a = new AudiR8(loc);
			world.addItem(a);
			world.addActor(a);
		}
	}

	private void addMaybach(World world) {
		for (int i = 0; i < 4; i ++){
			Location loc = Util.getRandomEmptyLocation(world);
			Maybach m = new Maybach(loc);
			world.addItem(m);
			world.addActor(m);
		}
	}

	private void addHouse(World world) {
		for (int i = 0; i < 4; i++) {
			Location loc = Util.getRandomEmptyLocation(world);
			House h = new House(loc);
			world.addItem(h);
		}
	}

	private void addPoison(World world) {
		for (int i = 0; i < 16; i++) {
			Location loc = Util.getRandomEmptyLocation(world);
			Poison p = new Poison(loc);
			world.addItem(p);
		}
	}

	private void addSuperman(World world) {
		Location loc = Util.getRandomEmptyLocation(world);
		Superman sm = new Superman(loc);
		world.addItem(sm);
		world.addActor(sm);
	}

	private void addWolf(World world) {
		Location loc = Util.getRandomEmptyLocation(world);
		Wolf w = new Wolf(loc);
		world.addItem(w);
		world.addActor(w);
	}

	private void addWood(World world) {
		for (int i = 0; i < 30; i++) {
			Location loc = Util.getRandomEmptyLocation(world);
			Wood w = new Wood(loc);
			world.addItem(w);
		}
	}

	private void addHuman(World world) {
		HumanAI hi = new HumanAI();
		for (int i = 0; i < 5; i++) {
			Location loc = Util.getRandomEmptyLocation(world);
			Human hey = new Human(hi, loc);
			world.addItem(hey);
			world.addActor(hey);
		}
	}

	private void addGrass(World world) {
		for (int i = 0; i < INITIAL_GRASS; i++) {
			Location loc = Util.getRandomEmptyLocation(world);
			world.addItem(new Grass(loc));
		}
	}

	private void addGnats(World world) {
		for (int i = 0; i < INITIAL_GNATS; i++) {
			Location loc = Util.getRandomEmptyLocation(world);
			Gnat gnat = new Gnat(loc);
			world.addItem(gnat);
			world.addActor(gnat);
		}
	}

	private void addFoxes(World world) {
		FoxAI foxAI = new FoxAI();
		for (int i = 0; i < INITIAL_FOXES; i++) {
			Location loc = Util.getRandomEmptyLocation(world);
			Fox fox = new Fox(foxAI, loc);
			world.addItem(fox);
			world.addActor(fox);
		}
	}

	private void addRabbits(World world) {
		RabbitAI rabbitAI = new RabbitAI();
		for (int i = 0; i < INITIAL_RABBITS; i++) {
			Location loc = Util.getRandomEmptyLocation(world);
			Rabbit rabbit = new Rabbit(rabbitAI, loc);
			world.addItem(rabbit);
			world.addActor(rabbit);
		}
	}

}
