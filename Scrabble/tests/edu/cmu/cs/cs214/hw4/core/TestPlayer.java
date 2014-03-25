package edu.cmu.cs.cs214.hw4.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestPlayer {
	private Player p;

	@Before
	public void setUp() throws Exception {
		p = new Player(0);
	}

	@After
	public void tearDown() throws Exception {
		p = null;
	}

	@Test
	public void initTest() {
		System.out.print("TESTING PLAYER CLASS \n");
		assertEquals(0, p.getID());
		assertEquals(0, p.getScore());

		NormalTile[] ns = p.getCurrentPlayingRack();
		printNormal(ns);

		p.addScore(10);
		assertEquals(10, p.getScore());

		Rack r = p.getWholeRack();
		printNormal(r.getArrayOfNormalTile());

		SpecialTile st = new SpecialTile(3);
		p.purchaseTile(st);
		p.addScore(100);
		p.purchaseTile(st);

		p.retire(0);
		ns = p.getCurrentPlayingRack();
		printNormal(ns);
		p.refillRack();
		ns = p.getCurrentPlayingRack();
		printNormal(ns);
		System.out.print("DONE TESTIN PLAYER CLASS \n");
	}

	private void printNormal(NormalTile[] ns) {
		for (NormalTile n : ns) {
			if (n != null) {
				System.out.print(n.getCharacter() + " ");
			} else {
				System.out.print("  ");
			}
		}
		System.out.print("\n");
	}

}
