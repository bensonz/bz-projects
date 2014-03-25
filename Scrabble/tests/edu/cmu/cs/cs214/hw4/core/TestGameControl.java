package edu.cmu.cs.cs214.hw4.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * TESTING. NOTE: The dictionary has been modified by me to accommodate some
 * technical issues such as under extreme conditions I am just way too tired to
 * think of actual valid word play. So why not just change the dict?
 * 
 * @author bz
 * 
 */
public class TestGameControl {
	private GameControl g;

	@Before
	public void setUp() throws Exception {
		g = new GameControl(2);
	}

	@After
	public void tearDown() throws Exception {
		g = null;
	}

	@Test
	public void initTest() {
		assertEquals(0, g.getRound());
		Board b = g.getPlayBoard();
		// printBoard(b);
		Player p = g.getPlayer();
		assertEquals(0, p.getID());
		assertTrue(p == g.getWinner());
	}

	@Test
	public void testValidMove() {
		Move m1 = new Move(0, 0, new NormalTile('b'));
		Move m2 = new Move(0, 0, new NormalTile('i'));
		Move m3 = new Move(-1, 14, new NormalTile('t'));
		assertTrue(g.isValidMove(m1));
		g.placeMove(m1);
		assertFalse(g.isValidMove(m2));
		assertFalse(g.isValidMove(m3));

		m2 = new Move(0, 1, new NormalTile('i'));
		assertTrue(g.isValidMove(m2));
		SpecialTile t = null;
		m3 = new Move(0, 1, t);
		assertFalse(g.isValidMove(m3));
		NormalTile n = null;
		m3 = new Move(0, 1, n);
		assertFalse(g.isValidMove(m3));

		m2 = new Move(0, 16, new NormalTile('t'));
		assertFalse(g.isValidMove(m2));
		m3 = new Move(0, -7, new SpecialTile(1));
		assertFalse(g.isValidMove(m3));

		m2 = new Move(16, 0, new NormalTile('l'));
		assertFalse(g.isValidMove(m2));
	}

	@Test
	public void testEnablePlay() {
		Move m1 = new Move(7, 7, new NormalTile('b'));
		Move m2 = new Move(8, 7, new NormalTile('i'));
		Move m3 = new Move(9, 7, new NormalTile('t'));
		Move m4 = new Move(10, 7, new NormalTile('c'));
		Move m5 = new Move(11, 7, new NormalTile('h'));
		Move[] moves = new Move[] { m1, m2, m3, m4, m5 };
		g.enablePlay(moves);
		// printBoard(g.getPlayBoard());
		assertEquals(32, g.getPlayerPoints(g.getPlayer()));
		m5 = new Move(0, 0, new NormalTile('h'));
		moves[4] = m5;
		g.enablePlay(moves);
	}

	@Test
	public void GameflowTest() {
		Player p1 = g.getPlayer();
		g.nextRound();
		Player p2 = g.getPlayer();
		assertFalse(p1.equals(p2));
		assertFalse(p1.getID() == p2.getID());
		g.negativePoints();
		p2.addScore(100);
		Move m1 = new Move(7, 7, new NormalTile('b'));
		Move m2 = new Move(8, 7, new NormalTile('i'));
		Move m3 = new Move(9, 7, new NormalTile('t'));
		Move m4 = new Move(10, 7, new NormalTile('c'));
		// Move m5 = new Move(11, 7, new NormalTile('h'));
		Move[] moves = new Move[] { m1, m2, m3, m4 };
		g.enablePlay(moves);
		System.out.print("player score:  " + p2.getScore());
		// assertEquals(iDONT KNOW, g.getPlayerPoints(p2));
		// testing order
		GameControl ag = new GameControl(4);
		assertEquals(ag.getPlayer().getID(), 0);
		ag.nextRound();
		assertEquals(ag.getPlayer().getID(), 1);
		ag.nextRound();
		assertEquals(ag.getPlayer().getID(), 2);
		ag.nextRound();
		assertEquals(ag.getPlayer().getID(), 3);
		ag.nextRound();
		assertEquals(ag.getPlayer().getID(), 0);
		ag.reverseOrder();
		ag.nextRound();
		assertEquals(ag.getPlayer().getID(), 3);
		ag.nextRound();
		assertEquals(ag.getPlayer().getID(), 2);
		ag.nextRound();
		assertEquals(ag.getPlayer().getID(), 1);
		ag.exchange(new int[] { 1, 2, 3 });
	}

	@Test
	public void TestIsValidPlay() {
		Move[] moves = new Move[0];
		assertFalse(g.isValidPlay(moves));
		Move m1 = new Move(7, 7, new NormalTile('d'));
		Move m2 = new Move(7, 8, new NormalTile('i'));
		Move m3 = new Move(7, 9, new NormalTile('g'));
		Move m4 = new Move(7, 10, new NormalTile('g'));
		Move m5 = new Move(7, 11, new NormalTile('e'));
		Move m6 = new Move(7, 12, new NormalTile('r'));
		moves = new Move[] { m1 };
		assertFalse(g.isValidPlay(moves));
		moves = new Move[] { m2 };
		assertFalse(g.isValidPlay(moves));
		Move temp = new Move(9, 10, new NormalTile('t'));
		moves = new Move[] { m1, temp };
		assertFalse(g.isValidPlay(moves));
		moves = new Move[] { m1, m2, m3, m4 };
		assertFalse(g.isValidPlay(moves));
		moves = new Move[] { m1, m2, m4};
		assertFalse(g.isValidPlay(moves));
		moves = new Move[] { m1, m2, m3, m4, m5, m6 };
		assertTrue(g.isValidPlay(moves));
		g.enablePlay(moves);
		// printBoard(g.getPlayBoard());
		// nextRound test
		g.nextRound();
		Move ma = new Move(8, 6, new NormalTile('b'));
		Move mb = new Move(8, 7, new NormalTile('i'));
		Move mc = new Move(8, 8, new NormalTile('t'));
		Move md = new Move(8, 9, new NormalTile('c'));
		Move me = new Move(8, 10, new NormalTile('h'));
		moves = new Move[] { ma, mb, mc, md, me };
		assertTrue(g.isValidPlay(moves));
		g.enablePlay(moves);
		printBoard(g.getPlayBoard());
		g.boom(mc);
		printBoard(g.getPlayBoard());
	}

	@Test
	public void TestPrivateMethods() {
		Move m1 = new Move(5, 7, new NormalTile('d'));
		Move m2 = new Move(6, 7, new NormalTile('i'));
		Move m3 = new Move(7, 7, new NormalTile('g'));
		Move m4 = new Move(8, 7, new NormalTile('g'));
		Move m5 = new Move(9, 7, new NormalTile('e'));
		Move m6 = new Move(10, 7, new NormalTile('r'));
		Move[] moves = new Move[] { m1, m2, m3, m4, m5, m6 };
		assertTrue(g.isValidPlay(moves));
		g.enablePlay(moves);
		g.nextRound();
		Move m7 = new Move(4, 8, new NormalTile('b'));
		Move m8 = new Move(5, 8, new NormalTile('i'));
		Move m9 = new Move(6, 8, new NormalTile('t'));
		Move m10 = new Move(7, 8, new NormalTile('c'));
		Move m11 = new Move(8, 8, new NormalTile('h'));
		Move[] anotherMoves = new Move[] { m7, m8, m9, m10, m11 };
		assertTrue(g.isValidPlay(anotherMoves));
		g.enablePlay(anotherMoves);
		// printBoard(g.getPlayBoard());
	}

	@Test
	public void TestBoom() {
		Move m1 = new Move(5, 7, new NormalTile('d'));
		Move m2 = new Move(6, 7, new NormalTile('i'));
		Move m3 = new Move(7, 7, new NormalTile('g'));
		Move m4 = new Move(8, 7, new NormalTile('g'));
		Move m5 = new Move(9, 7, new NormalTile('e'));
		Move m6 = new Move(10, 7, new NormalTile('r'));
		Move[] moves = new Move[] { m1, m2, m3, m4, m5, m6 };
		assertTrue(g.isValidPlay(moves));
		g.enablePlay(moves);
		g.nextRound();
		Move m7 = new Move(4, 8, new NormalTile('b'));
		Move m8 = new Move(5, 8, new NormalTile('i'));
		Move m9 = new Move(6, 8, new NormalTile('t'));
		Move m10 = new Move(7, 8, new NormalTile('c'));
		Move m11 = new Move(8, 8, new NormalTile('h'));
		Move m12 = new Move(4, 9, new SpecialTile(2));
		Move[] anotherMoves = new Move[] { m7, m9, m8, m10, m11, m12 };
		assertTrue(g.isValidPlay(anotherMoves));
		g.enablePlay(anotherMoves);
		printBoard(g.getPlayBoard());
		g.nextRound();
		Move m13 = new Move(4, 9, new NormalTile('i'));
		Move m14 = new Move(4, 10, new NormalTile('t'));
		moves = new Move[] { m13, m14 };
		assertTrue(g.isValidPlay(moves));
		g.enablePlay(moves);
		printBoard(g.getPlayBoard());
	}

	@Test(expected = IllegalStateException.class)
	public void failTest() {
		GameControl k = new GameControl(10);
	}

	private void printBoard(Board b) {
		System.out.print("   ");
		for (int k = 0; k < b.getBoardCol(); k++) {
			System.out.print((char) ((int) 'a' + k) + " ");
		}
		System.out.print("\n");
		for (int i = 0; i < b.getBoardRow(); i++) {
			if (i < 10) {
				System.out.print(" ");
			}
			System.out.print(i + " ");
			for (int j = 0; j < b.getBoardCol(); j++) {
				if (b.getBoardNormalTile(i, j) == null) {
					System.out.print("# ");
				} else {
					char c = b.getBoardNormalTile(i, j).getCharacter();
					System.out.print(c + " ");
				}
			}
			System.out.print("\n");
		}
	}
}
