package edu.cmu.cs.cs214.hw4.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestTile {

	private NormalTile nt;
	private SpecialTile st;

	@Before
	public void setUp() throws Exception {
		nt = new NormalTile('a');
		st = new SpecialTile(1);
	}

	@After
	public void tearDown() throws Exception {
		nt = null;
		st = null;
	}

	@Test
	public void test() {
		System.out.print("START TESTING TILE CLASS \n");
		assertEquals('a',nt.getCharacter());
		assertEquals(1,nt.getLetterScore());
		NormalTile temp = new NormalTile('a');
		assertTrue(nt.equals(temp));
		temp = new NormalTile('s');
		assertFalse(nt.equals(temp));
		
		assertEquals(1,st.getIDENTIFIER());
		assertEquals("reverse-player-order",st.toString());
		assertEquals(st.getPrice(),20);
		for (int i = 0; i < 5; i++){
			SpecialTile spe = new SpecialTile(i);
			System.out.print(spe.toString() + "\n");
		}
		
		assertFalse(nt.equals(st));
		
		System.out.print("DONE TESTING TILE CLASS \n");
	}
	
	@Test(expected = IllegalStateException.class)
	public void fail(){
		nt = new NormalTile('9');
	}

}
