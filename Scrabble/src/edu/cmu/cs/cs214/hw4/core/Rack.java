package edu.cmu.cs.cs214.hw4.core;

import java.util.Random;
import java.util.Vector;
import static edu.cmu.cs.cs214.hw4.core.Constants.*;

public class Rack {

	private Vector<NormalTile> pocket = new Vector<NormalTile>();
	private Random rand = new Random();
	private Vector<SpecialTile> specialTiles = new Vector<SpecialTile>();
	private NormalTile[] nTiles;

	/**
	 * creates a rack containing not only current available letters but also the
	 * whole pocket of normalTiles, initialize special tiles to be empty(null)
	 * 
	 * @param num
	 *            the max number of a player's tiles (normal)
	 */
	public Rack(int num) {
		init();
		nTiles = new NormalTile[num];
		for (int i = 0; i < num; i++) {
			nTiles[i] = createANormalTile();
		}
	}

	private void init() {
		int a = (int) 'a';
		for (int i = a; i < a + 26; i++) {
			int numberOfLetters = NUMBER_OF_LETTERS.get((char) i);
			for (int j = 0; j < numberOfLetters; j++) {
				pocket.add(new NormalTile((char) i));
			}
		}
		// specialTiles = null;
	}

	public NormalTile[] getArrayOfNormalTile() {
		return nTiles;
	}

	public Vector<NormalTile> getPocket() {
		return pocket;
	}

	public Vector<SpecialTile> getRackSpecialTile() {
		return specialTiles;
	}

	public void addSpecialTile(SpecialTile st) {
		specialTiles.add(st);
	}

	/**
	 * creates a normal tile from our pocket
	 * 
	 * @return a normalTile
	 */
	public NormalTile createANormalTile() {
		int key = rand.nextInt(pocket.size());
		return pocket.remove(key);
	}

	/**
	 * changes the player's rack accordingly
	 * 
	 * @param p
	 *            the char being removed
	 * @return 0 if remove successfully 1 otherwise
	 */
	public int retire(int p) {
		if (nTiles[p] != null) {
			nTiles[p] = null;
			return 0;
		}
		return 1;
	}

	public void refillRack() {
		for (int i = 0; i < nTiles.length; i++) {
			if (nTiles[i] == null) {
				nTiles[i] = createANormalTile();
			}
		}
	}
}
