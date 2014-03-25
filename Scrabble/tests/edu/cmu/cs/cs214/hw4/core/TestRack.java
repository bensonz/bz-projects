package edu.cmu.cs.cs214.hw4.core;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestRack {

	private Rack r;

	@Before
	public void setUp() throws Exception {
		r = new Rack(7);
	}

	@After
	public void tearDown() throws Exception {
		r = null;
	}

	@Test
	public void test() {
		System.out.print("START TESTING RACK CLASS \n");
		Vector<SpecialTile> ts = r.getRackSpecialTile();
		assertTrue(ts.isEmpty());
		SpecialTile st = new SpecialTile(1);
		r.addSpecialTile(st);
		assertFalse(ts.isEmpty());
		System.out.print(ts.get(0).toString() + "\n");
		NormalTile[] ns = r.getArrayOfNormalTile();
		printNormal(ns);
		// I have to manually check this...
		r.getPocket();// don't want to print this out.
		assertEquals(0,r.retire(1));
		assertEquals(0,r.retire(3));
		assertEquals(1,r.retire(1));
		
		ns = r.getArrayOfNormalTile();
		printNormal(ns);
		r.refillRack();
		printNormal(ns);
		
		System.out.print("DONE TESTING RACK CLASS \n");
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
