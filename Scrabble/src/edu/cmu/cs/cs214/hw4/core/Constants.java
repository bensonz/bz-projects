package edu.cmu.cs.cs214.hw4.core;

import java.util.HashMap;

/**
 * The Constants class.
 */
public class Constants {

	public final static int[][] BOOM_TILE = { { -2, 0 }, { -1, -1 }, { -1, 0 },
			{ -1, 1 }, { 0, -2 }, { 0, -1 }, { 0, 0 }, { 0, 1 }, { 0, 2 },
			{ 1, -1 }, { 1, 0 }, { 1, 1 }, { 2, 0 } };

	/**
	 * The board scoring reference. credit to
	 * http://en.wikipedia.org/wiki/File:Blank_Scrabble_board_with_coordinates
	 * .svg 4 = triple word 3 = triple letter 2 = double word 1 = double letter
	 */
	public final static int[][] BOARDMAP_SCHEME = {
			{ 4, 0, 0, 1, 0, 0, 0, 4, 0, 0, 0, 1, 0, 0, 4 },
			{ 0, 2, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 2, 0 },
			{ 0, 0, 2, 0, 0, 0, 1, 0, 1, 0, 0, 0, 2, 0, 0 },
			{ 1, 0, 0, 2, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 1 },
			{ 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0 },
			{ 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0 },
			{ 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0 },
			{ 4, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 1, 0, 0, 4 },
			{ 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0 },
			{ 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 3, 0 },
			{ 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0 },
			{ 1, 0, 0, 2, 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 1 },
			{ 0, 0, 2, 0, 0, 0, 1, 0, 1, 0, 0, 0, 2, 0, 0 },
			{ 0, 2, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 2, 0 },
			{ 4, 0, 0, 1, 0, 0, 0, 4, 0, 0, 0, 1, 0, 0, 4 } };

	/**
	 * the number of letters a player can have credit to
	 * http://boardgames.about.com/od/ScrabbleFAQ/f/How-Many-Letter-Tiles.htm
	 */
	public final static HashMap<Character, Integer> NUMBER_OF_LETTERS = new HashMap<Character, Integer>();
	static {
		// no blank tile pls.
		NUMBER_OF_LETTERS.put('a', 9);
		NUMBER_OF_LETTERS.put('b', 2);
		NUMBER_OF_LETTERS.put('c', 2);
		NUMBER_OF_LETTERS.put('d', 4);
		NUMBER_OF_LETTERS.put('e', 12);
		NUMBER_OF_LETTERS.put('f', 2);
		NUMBER_OF_LETTERS.put('g', 3);
		NUMBER_OF_LETTERS.put('h', 2);
		NUMBER_OF_LETTERS.put('i', 9);
		NUMBER_OF_LETTERS.put('j', 1);
		NUMBER_OF_LETTERS.put('k', 1);
		NUMBER_OF_LETTERS.put('l', 4);
		NUMBER_OF_LETTERS.put('m', 2);
		NUMBER_OF_LETTERS.put('n', 6);
		NUMBER_OF_LETTERS.put('o', 8);
		NUMBER_OF_LETTERS.put('p', 2);
		NUMBER_OF_LETTERS.put('q', 1);
		NUMBER_OF_LETTERS.put('r', 6);
		NUMBER_OF_LETTERS.put('s', 4);
		NUMBER_OF_LETTERS.put('t', 6);
		NUMBER_OF_LETTERS.put('u', 4);
		NUMBER_OF_LETTERS.put('v', 2);
		NUMBER_OF_LETTERS.put('w', 2);
		NUMBER_OF_LETTERS.put('x', 1);
		NUMBER_OF_LETTERS.put('y', 2);
		NUMBER_OF_LETTERS.put('z', 1);
	}

	/**
	 * the letter values. credit to
	 * http://www.scrabblefinder.com/scrabble-letter-values/
	 */
	public final static HashMap<Character, Integer> LETTER_VALUES = new HashMap<Character, Integer>();
	static {
		LETTER_VALUES.put('a', 1);
		LETTER_VALUES.put('b', 3);
		LETTER_VALUES.put('c', 3);
		LETTER_VALUES.put('d', 2);
		LETTER_VALUES.put('e', 1);
		LETTER_VALUES.put('f', 4);
		LETTER_VALUES.put('g', 2);
		LETTER_VALUES.put('h', 4);
		LETTER_VALUES.put('i', 1);
		LETTER_VALUES.put('j', 8);
		LETTER_VALUES.put('k', 5);
		LETTER_VALUES.put('l', 1);
		LETTER_VALUES.put('m', 3);
		LETTER_VALUES.put('n', 1);
		LETTER_VALUES.put('o', 1);
		LETTER_VALUES.put('p', 3);
		LETTER_VALUES.put('q', 10);
		LETTER_VALUES.put('r', 1);
		LETTER_VALUES.put('s', 1);
		LETTER_VALUES.put('t', 1);
		LETTER_VALUES.put('u', 1);
		LETTER_VALUES.put('v', 4);
		LETTER_VALUES.put('w', 4);
		LETTER_VALUES.put('x', 8);
		LETTER_VALUES.put('y', 4);
		LETTER_VALUES.put('z', 10);
	}
}
