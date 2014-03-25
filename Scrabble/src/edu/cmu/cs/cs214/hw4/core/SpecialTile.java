package edu.cmu.cs.cs214.hw4.core;

public class SpecialTile {
	private final int IDENTIFIER;

	/**
	 * creates a special tile.
	 * 
	 * @param id
	 *            the identifier of a special tile 0 = negative points, 1 =
	 *            reverse-player-order, 2 = boom, 3 = random-point-loss, 4 =
	 *            extra-turn, else = unknown;
	 */
	public SpecialTile(int id) {
		IDENTIFIER = id;
	}

	public int getPrice() {
		return (IDENTIFIER + 1) * 10;
	}

	public int getIDENTIFIER() {
		return IDENTIFIER;
	}

	public String toString() {
		switch (IDENTIFIER) {
		case (0):
			return "negative-points";
		case (1):
			return "reverse-player-order";
		case (2):
			return "BOOM";
		case (3):
			return "random-point-lose";
		case (4):
			return "extra-turn";
		default:
			return "unknown";
		}
	}
}
