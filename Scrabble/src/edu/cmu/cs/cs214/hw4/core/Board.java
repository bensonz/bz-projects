package edu.cmu.cs.cs214.hw4.core;

import static edu.cmu.cs.cs214.hw4.core.Constants.*;

public class Board {

	private NormalTile[][] board_tiles;
	private SpecialTile[][] special_tiles;
	private int row, col;

	/**
	 * main board
	 */
	public Board() {
		row = 15;
		col = 15;
		board_tiles = new NormalTile[row][col];
		special_tiles = new SpecialTile[row][col];
	}

	public int getBoardRow() {
		return row;
	}

	public int getBoardCol() {
		return col;
	}

	/**
	 * gets scoring information at a location
	 * 
	 * @param i
	 *            row
	 * @param j
	 *            col
	 * @return the score multiple
	 */
	public int getBoardScoring(int i, int j) {
		return BOARDMAP_SCHEME[i][j];
	}

	/**
	 * gets the letter's score
	 * 
	 * @param c
	 *            the letter being checked
	 * @return the score of this letter
	 */
	public int getLetterVal(char c) {
		return LETTER_VALUES.get(c);
	}

	/**
	 * gets what's been placed on the board at the location
	 * 
	 * @param i
	 *            row
	 * @param j
	 *            col
	 * @return a normalTile or null
	 */
	public NormalTile getBoardNormalTile(int i, int j) {
		return board_tiles[i][j];
	}

	/**
	 * gets what's the special tile there
	 * 
	 * @param i
	 *            row
	 * @param j
	 *            col
	 * @return a special tile or null
	 */
	public SpecialTile getBoardSpecialTile(int i, int j) {
		return special_tiles[i][j];
	}

	public void placeNormalTileOnBoard(int row, int col, NormalTile t) {
		board_tiles[row][col] = t;
	}

	public void placeSpecialTileOnBoard(int row, int col, SpecialTile t) {
		special_tiles[row][col] = t;
	}

	public int[][] getBoomtile() {
		return BOOM_TILE;
	}
}
