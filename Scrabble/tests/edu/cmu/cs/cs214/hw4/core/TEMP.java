package edu.cmu.cs.cs214.hw4.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TEMP {
	private GameControl g;

	@Before
	public void setUp() throws Exception {
		g = new GameControl(4);
	}

	@After
	public void tearDown() throws Exception {
		g = null;
	}

	@Test
	public void TestBoom() {
		Move[] moves;
		Move m1 = new Move(7, 7, new NormalTile('d'));
		Move m2 = new Move(7, 8, new NormalTile('i'));
		Move m3 = new Move(7, 9, new NormalTile('g'));
		Move m4 = new Move(7, 10, new NormalTile('g'));
		Move m5 = new Move(7, 11, new NormalTile('e'));
		Move m6 = new Move(7, 12, new NormalTile('r'));
		moves = new Move[] { m1, m2, m4 };
		assertFalse(g.isValidPlay(moves));
		g.enablePlay(moves);
		printBoard(g.getPlayBoard());
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
