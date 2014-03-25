package edu.cmu.cs.cs214.hw4.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestMove {
	private Move m;

	@Before
	public void setUp() throws Exception {
		NormalTile n = new NormalTile('b');
		m = new Move(0, 0, n);
	}

	@After
	public void tearDown() throws Exception {
		m = null;
	}

	@Test
	public void test() {
		System.out.print("START TESTING MOVE CLASS \n");
		assertEquals(0,m.getMoveLocation()[0]);
		assertEquals(0,m.getMoveLocation()[1]);
		NormalTile n = new NormalTile('b');
		Move k = new Move(0, 0, n);
		assertTrue(m.equals(k));
		System.out.print(m.getMoveNormalTile().getCharacter() + "\n");
		assertEquals(null,m.getMoveSpecialTile());
		
		SpecialTile st = new SpecialTile(10);
		Move another = new Move(1,2,st);
		System.out.print(another.getMoveSpecialTile().toString() + "\n");
		System.out.print("DONE TESTING MOVE CLASS \n");
	}

}
