package edu.cmu.cs.cs214.hw4.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestBoard {
	private Board board;

	@Before
	public void setUp() throws Exception {
		board = new Board();
	}

	@After
	public void tearDown() throws Exception {
		board = null;
	}

	@Test
	public void initTest() {
		System.out.print("START TESTING BOARD CLASS \n");
		assertEquals(15,board.getBoardCol());
		assertEquals(15,board.getBoardRow());
		assertEquals(4,board.getBoardScoring(0, 0));
		assertEquals(10,board.getLetterVal('q'));
	}
	
	@Test
	public void placingNormalTileTest(){
		assertEquals(null,board.getBoardNormalTile(1, 14));
		NormalTile n = new NormalTile('a');
		board.placeNormalTileOnBoard(1, 1, n);
		board.placeNormalTileOnBoard(2, 1, n);
	    board.placeNormalTileOnBoard(1, 1, n);
	    assertEquals(n,board.getBoardNormalTile(1, 1));
	}

	@Test
	public void placingSpecialTile(){
		assertEquals(null,board.getBoardSpecialTile(10, 4));
		SpecialTile s = new SpecialTile(0);
		board.placeSpecialTileOnBoard(1, 9, s);
		assertEquals(s,board.getBoardSpecialTile(1, 9));
		System.out.print("START TESTING BOARD CLASS \n");
	}
}
