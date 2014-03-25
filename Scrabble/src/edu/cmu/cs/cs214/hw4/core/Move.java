package edu.cmu.cs.cs214.hw4.core;

public class Move {
	private int x, y;
	private NormalTile top;
	private SpecialTile special;

	/**
	 * A move is consisted of a location, and a normal/special tile
	 * 
	 * @param row
	 * @param col
	 * @param t
	 *            The normaltile
	 */
	public Move(int row, int col, NormalTile t) {
		x = row;
		y = col;
		top = t;
		special = null;
	}

	/**
	 * A move is consisted of a location, and a normal/special tile
	 * 
	 * @param row
	 * @param col
	 * @param t
	 *            The special tile
	 */
	public Move(int row, int col, SpecialTile st) {
		x = row;
		y = col;
		top = null;
		special = st;
	}

	public int[] getMoveLocation() {
		return new int[] { x, y };
	}

	public NormalTile getMoveNormalTile() {
		return top;
	}

	public SpecialTile getMoveSpecialTile() {
		return special;
	}
	
	@Override
	public int hashCode(){
		return x*33 + y*134 + top.getCharacter();
		
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Move)) {
			return false;
		} else {
			Move m = (Move) o;
			if (m.getMoveSpecialTile() != null) {
				return false;
			} else if (this.special != null) {
				return false;
			}
			int[] loc = m.getMoveLocation();
			if (this.x == loc[0]
					&& this.y == loc[1]
					&& this.top.getCharacter() == m.getMoveNormalTile()
							.getCharacter()) {
				return true;
			}
		}
		return false;
	}
}
