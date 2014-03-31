package edu.cmu.cs.cs214.hw4.core;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class GameControl {
	private static final int NUM_OF_MOVES_PER_ROUND = 8;

	private Board playBoard;
	private int num_player;
	private Player[] players;
	private Move[] playerMoves;
	private int round;
	private Dictionary hmap_dict;

	// player's status
	private String moveWord;
	private int moveUsed;
	/**
	 * horizontal or vertical, 0 = horizontal 1 = vertical
	 */
	private int hORv;

	// Special effects
	private boolean isReverse;
	private int reverseRound;
	private int negative;
	private int extra_turn_round;
	private boolean extra_activate;
	private int comback;

	/**
	 * Game main control class.
	 * 
	 * @param num_player
	 *            is the number of players we are playing with(<=4)
	 */
	public GameControl(int num_players) {
		round = 0;
		extra_turn_round = -1;
		extra_activate = false;
		comback = -1;
		playBoard = new Board();
		hmap_dict = new Dictionary("assets/words.txt");
		playerMoves = new Move[NUM_OF_MOVES_PER_ROUND];

		// initialize
		isReverse = false;
		negative = 1;

		if (num_players > 4 || num_players < 2) {
			throw new IllegalStateException("Number of players must be 2~4");
		} else {
			num_player = num_players;
			players = new Player[num_player];
			for (int i = 0; i < num_player; i++) {
				Player p = new Player(i);
				players[i] = p;
			}
		}
	}

	/**
	 * checks if a move is valid
	 * 
	 * @param m
	 *            the move being checked
	 * @return true if a move is valid
	 */
	public boolean isValidMove(Move m) {
		// a move is valid as long as it's within bound, and no other tile
		// is at its place.
		if (moveUsed >= 8) {
			return false;
		}
		int[] loc = m.getMoveLocation();
		if (m.getMoveNormalTile() == null && m.getMoveSpecialTile() == null) {
			return false;
		} else if (loc[0] > playBoard.getBoardRow()
				|| loc[1] > playBoard.getBoardCol() || loc[0] < 0 || loc[1] < 0) {
			return false;
		} else if (playBoard.getBoardNormalTile(loc[0], loc[1]) != null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * enable the play that the player played. checks if valid inside as well.
	 * changes the board if the play is valid.
	 * 
	 * @param moves
	 * 
	 * @return 0 if move is successfully placed. 1 otherwise.
	 */
	public int enablePlay(Move[] moves) {
		if (!isValidPlay(moves)) {
			// play is not valid, cannot enable;
			System.out.println("play not enabled");
			return 1;
		} else {
			Move boomMove = null;
			for (Move m : moves) {
				int[] loc = m.getMoveLocation();
				if (m.getMoveSpecialTile() != null
						&& m.getMoveSpecialTile().getIDENTIFIER() == 4) {
					extraTurn();
				}
				if (playBoard.getBoardSpecialTile(loc[0], loc[1]) != null) {
					//we placed down a normal tile onto special
					int IDENTIFIER = playBoard.getBoardSpecialTile(loc[0],
							loc[1]).getIDENTIFIER();
					switch (IDENTIFIER) {
					case (0):
						negativePoints();
						break;
					case (1):
						reverseOrder();
						break;
					case (2):
						boomMove = m;
						break;
					case (3):
						randomPointLose();
						break;
					case (4):
						extra_activate = true;
						break;
					default:
						break;
					}
				}
				placeMove(m);
			}
			int score = getScore(moves);
			if (boomMove != null) {
				boom(boomMove);
				score -= deduction(moves, boomMove);
			}
			// System.out.print("TOTAL : " + score + "\n");
			addPoints(getPlayer(), score);
			return 0;
		}
	}

	private void extraTurn() {
		extra_turn_round = round; // record down the round
	}

	private int deduction(Move[] moves, Move center) {
		Move[] mMoves = new Move[moves.length];
		int i = 0;
		for (Move m : moves) {
			int[] loc = m.getMoveLocation();
			NormalTile nt = playBoard.getBoardNormalTile(loc[0], loc[1]);
			if (nt == null) {
				// got blown up
				mMoves[i] = m;
				i++;
			}
		}
		return getScore(mMoves);
	}

	private int getScore(Move[] moves) {
		int score = 0;
		moveWord = getMoveWordWithHead(moves);
		score += getPlayWordScore(moves);
		// System.out.print("Word score" + score + "\n");
		if (round != 0) {
			for (Move m : moves) {
				/*
				 * if (getMoveWordScore(m) != 0) {
				 * System.out.print(m.getMoveNormalTile().getCharacter());
				 * System.out.print(" got: " + getMoveWordScore(m) +
				 * " points \n"); }
				 */
				if (m.getMoveSpecialTile() != null) {
					continue;
				}
				score += getMoveWordScore(m);
			}
		}
		return score * (negative);
	}

	private int getMoveWordScore(Move m) {
		int score = 0;
		int wordMult = 1;
		int letterMult = 1;
		String word;
		if (m.getMoveSpecialTile() != null) {
			// it's a special tile! no score!
			return score;
		}
		if (hORv == 1) {
			word = findHorizontalWord(m);
		} else {
			word = findVerticalWord(m);
		}
		if (word == null) {
			return score;
		} else {
			for (char c : word.toCharArray()) {
				score += playBoard.getLetterVal(c);
			}
		}
		int[] loc = m.getMoveLocation();
		int boardscore = playBoard.getBoardScoring(loc[0], loc[1]);
		switch (boardscore) {
		case (0):
			break;
		case (1): // double letter
			letterMult *= 2;
			break;
		case (2): // double word
			wordMult *= 2;
			break;
		case (3): // triple letter
			letterMult *= 3;
			break;
		case (4): // triple word
			wordMult *= 3;
			break;
		default:
			break;
		}
		int letterScore = playBoard.getLetterVal(m.getMoveNormalTile()
				.getCharacter());
		score += letterScore * letterMult;
		return score * wordMult;
	}

	private int getPlayWordScore(Move[] moves) {
		int score = 0;
		int wordMult = 1;
		char[] mw = moveWord.toCharArray();
		for (Move m : moves) {
			if (m.getMoveNormalTile() != null) {
				// gets the score of the play's word
				int letterMult = 1;
				char c = m.getMoveNormalTile().getCharacter();
				int letterScore = playBoard.getLetterVal(c);
				int k = charSearch(mw, c);
				if (k == -1) {
					return 0;
				}
				mw[k] = 0;
				int[] loc = m.getMoveLocation();
				int boardscore = playBoard.getBoardScoring(loc[0], loc[1]);
				switch (boardscore) {
				case (0):
					break;
				case (1): // double letter
					letterMult *= 2;
					break;
				case (2): // double word
					wordMult *= 2;
					break;
				case (3): // triple letter
					letterMult *= 3;
					break;
				case (4): // triple word
					wordMult *= 3;
					break;
				default:
					break;
				}
				score += letterScore * letterMult;
			}
		}
		for (char c : mw) {
			if (c != 0) {
				score += playBoard.getLetterVal(c);
			}
		}
		score *= wordMult;
		return score;
	}

	public int placeMove(Move m) {
		if (!isValidMove(m)) {
			return 1;
		} else {
			int[] loc = m.getMoveLocation();
			playBoard.placeNormalTileOnBoard(loc[0], loc[1],
					m.getMoveNormalTile());
			playBoard.placeSpecialTileOnBoard(loc[0], loc[1],
					m.getMoveSpecialTile());
			playerMoves[moveUsed] = m;
			moveUsed++;
		}
		return 0;
	}

	public Board getPlayBoard() {
		return playBoard;
	}

	public void nextRound() {
		// every round player's moves are different.
		// at most 1 special tile can be placed.
		playerMoves = new Move[NUM_OF_MOVES_PER_ROUND];
		moveUsed = 0;
		negative = 1;
		moveWord = "";
		round++;
		getPlayer().getWholeRack().refillRack();
		if (isReverse) {
			reverseRound--;
		}
		if (extra_activate && extra_turn_round != -1) {
			System.out.println("extra turn activated ");
			comback = round;
			extra_activate = false;
			round = extra_turn_round;
		} else if (!extra_activate && comback != -1) {
			round = comback;
			comback = -1;
		}
		return;
	}

	public int getRound() {
		return round;
	}

	public int getPlayerPoints(Player p) {
		return p.getScore();
	}

	/**
	 * adds the point to the player
	 * 
	 * @param p
	 *            the current player.
	 * @param score
	 *            the score adding.
	 */
	private void addPoints(Player p, int score) {
		p.addScore(score);
	}

	/**
	 * takes in a bunch of moves and check if they are valid.
	 * 
	 * @return true if the play is valid
	 */
	public boolean isValidPlay(Move[] moves) {
		boolean valid = true;

		// no move? definitely false.
		if (moves.length < 1) {
			System.out.print("Move length not long enough");
			return false;
		}

		// first round
		// at least one move have to be at the center.
		if (round == 0 || playBoard.getBoardNormalTile(7, 7) == null) {
			boolean firstRoundMoves = false;
			for (Move m : moves) {
				if (m.getMoveLocation()[0] == 7 && m.getMoveLocation()[1] == 7) {
					firstRoundMoves = true;
				}
			}
			if (!firstRoundMoves || moves.length == 1) {
				System.out.print("first round move wrong");
				return false;
			}
		}
		/*
		 * if more than 1 move, the play if valid iff: 1. all moves besides
		 * special tiles are of same line 2. at least one move is connected to a
		 * tile on board 3. all connected moves are valid words
		 */
		moveWord = "";
		if (moves.length == 1) {
			if (moves[0].getMoveSpecialTile() == null) {
				moveWord += moves[0].getMoveNormalTile().getCharacter();
			}
		} else if (moves.length != 1) {
			int[] loc = new int[] { -1, -1 };
			int[] sloc = new int[] { -1, -1 };
			for (int i = 0; i < moves.length - 1; i++) {
				if (moves[i].getMoveSpecialTile() == null) {
					loc = moves[i].getMoveLocation();
				}
				if (moves[i + 1].getMoveSpecialTile() == null) {
					sloc = moves[i + 1].getMoveLocation();
				}
				if (sloc[0] >= 0 && sloc[1] >= 0 && loc[0] >= 0 & loc[0] >= 0) {
					break;
				}
			}
			if (loc[0] == sloc[0]) {
				hORv = 0;
			} else if (loc[1] == sloc[1]) {
				hORv = 1;
			} else {
				// System.out.print("loc = {" + loc[0] + "," + loc[1] + " }");
				// System.out.print("sloc = {" + sloc[0] + "," + sloc[1] +
				// " }");
				// not on the same line
				System.out.print("Not on the same line");
				return false;
			}
			moves = sortPlayMoves(moves);// sort it!
			for (int i = 0; i < moves.length; i++) {
				Move m = moves[i];
				int[] check = m.getMoveLocation();
				if (m.getMoveSpecialTile() != null) {
					// I am ignoring all special tiles
					continue;
				}
				if (check[hORv] != loc[hORv]) {
					// if moves are not all in one line,
					// then this play is not valid;
					System.out.print("Not in the same line");
					return false;
				}
				if (i + 1 != moves.length) {
					// not out of bound yet
					Move next = moves[i + 1];
					int[] nLoc = next.getMoveLocation();
					int cc = (hORv == 0) ? 1 : 0;
					if (Math.abs(check[cc] - nLoc[cc]) > 1) {
						System.out.println("diff " + check[cc] + ", "
								+ nLoc[cc]);
						// not connecting
						for (int idk = check[cc]; idk < nLoc[cc]; idk++) {
							idk++;
							if (hORv == 0) {
								// horizontal
								if (playBoard.getBoardNormalTile(nLoc[0], idk) == null) {
									System.out
											.println("horizontal word not connecting");
									return false;
								} else {
									moveWord += m.getMoveNormalTile()
											.getCharacter()
											+ playBoard.getBoardNormalTile(
													nLoc[0], idk)
													.getCharacter();
								}
							} else {
								// vertical
								if (playBoard.getBoardNormalTile(idk, nLoc[1]) == null) {

									System.out
											.println("vertical word not connecting");
									return false;
								} else {
									moveWord += m.getMoveNormalTile()
											.getCharacter()
											+ playBoard.getBoardNormalTile(idk,
													nLoc[1]).getCharacter();
								}
							}
						}
					} else {
						moveWord += m.getMoveNormalTile().getCharacter();
					}
				} else {
					moveWord += m.getMoveNormalTile().getCharacter();
				}
			}
		}

		/*
		 * now it's the same for only one move or more 1. check if connected to
		 * an existing board tile 2. check if all moves(or just one) forms a
		 * word
		 */
		for (Move m : moves) {
			if (m.getMoveSpecialTile() != null) {
				// if it's a special tile, ignore checking words on this
				if (!isValidMove(m)) {
					return false;
				} else {
					continue;
				}
			}
			int[] check = m.getMoveLocation();
			if (playBoard.getBoardNormalTile(check[0], check[1]) != null) {
				// a normal tile already there.
				System.out.print("Tried placing another move on board \n");
				return false;
			}
			int[] up = new int[] { check[0], check[1] - 1 };
			int[] left = new int[] { check[0] - 1, check[1] };
			int[] down = new int[] { check[0], check[1] + 1 };
			int[] right = new int[] { check[0] + 1, check[1] };
			if ((up[1] > 0 && playBoard.getBoardNormalTile(up[0], up[1]) != null)
					|| (left[0] > 0 && playBoard.getBoardNormalTile(left[0],
							left[1]) != null)
					|| (down[1] < playBoard.getBoardRow() && playBoard
							.getBoardNormalTile(down[0], down[1]) != null)
					|| (right[0] < playBoard.getBoardCol() && playBoard
							.getBoardNormalTile(right[0], right[1]) != null)) {
				// check if it is connecting to other tiles
				// the play not placed on board yet, so it's okay to just do
				// this check
				valid = true;
			}
		}
		if (round != 0 && !valid) {
			System.out.print("not connecting other tiles");
			return false;
		} else {
			for (Move m : moves) {
				// because at least one move is adjacent to a tile on board,
				// I do not need to consider if all words found are null.
				String word = findWords(m);
				if (word != null) {
					/*
					 * System.out.print("move : { " + m.getMoveLocation()[0] +
					 * "," + m.getMoveLocation()[1] + " } \n");
					 * System.out.print("searching : " + word + ": ");
					 */
					if (!hmap_dict.search(word)) {
						System.out.print("fail searching " + word + "\n");
						return false;
					}
					System.out.print("succeed searching " + word + "\n");
					if (moves.length == 1) {
						return true;
					}
				}
			}
			System.out.print(moveWord + "\n");
			moveWord = getMoveWordWithHead(moves);
			System.out.print(moveWord + "\n");
			if (!hmap_dict.search(moveWord)) {
				System.out.print("Move word Not found:" + moveWord + "\n");
				return false;
			}
		}
		return true;
	}

	private Move[] sortPlayMoves(Move[] moves) {
		if (hORv == 0) {
			// horizontal word
			Arrays.sort(moves, new Comparator<Move>() {
				@Override
				public int compare(Move m1, Move m2) {
					Integer numOfKeys1 = m1.getMoveLocation()[1];
					Integer numOfKeys2 = m2.getMoveLocation()[1];
					return numOfKeys1.compareTo(numOfKeys2);
				}
			});
		} else {
			// vertical word
			Arrays.sort(moves, new Comparator<Move>() {
				@Override
				public int compare(Move m1, Move m2) {
					Integer numOfKeys1 = m1.getMoveLocation()[0];
					Integer numOfKeys2 = m2.getMoveLocation()[0];
					return numOfKeys1.compareTo(numOfKeys2);
				}
			});
		}
		return moves;
	}

	private String getMoveWordWithHead(Move[] moves) {
		if (hORv == 1) {
			// Vertically placed, going to check the head
			int[] head = moves[0].getMoveLocation();
			head[0]--;
			int[] tail = moves[moves.length - 1].getMoveLocation();
			tail[0]++;
			String check = "";
			while (head[0] > 0
					&& playBoard.getBoardNormalTile(head[0], head[1]) != null) {
				char c = playBoard.getBoardNormalTile(head[0], head[1])
						.getCharacter();
				check = Character.toString(c) + check;
				head[0]--;
			}
			check += moveWord;
			while (tail[0] < playBoard.getBoardRow()
					&& playBoard.getBoardNormalTile(tail[0], tail[1]) != null) {
				char c = playBoard.getBoardNormalTile(tail[0], tail[1])
						.getCharacter();
				check += Character.toString(c);
				tail[0]++;
			}
			return check;
		} else {
			// horizontal word
			int[] head = moves[0].getMoveLocation();
			head[1]--;

			int[] tail = moves[moves.length - 1].getMoveLocation();
			tail[1]++;

			String check = "";
			while (head[1] > 0
					&& playBoard.getBoardNormalTile(head[0], head[1]) != null) {
				char c = playBoard.getBoardNormalTile(head[0], head[1])
						.getCharacter();
				check = Character.toString(c) + check;
				head[1]--;
			}
			check += moveWord;
			while (tail[1] < playBoard.getBoardCol()
					&& playBoard.getBoardNormalTile(tail[0], tail[1]) != null) {
				char c = playBoard.getBoardNormalTile(tail[0], tail[1])
						.getCharacter();
				check += Character.toString(c);
				tail[1]++;
			}
			return check;
		}
	}

	/**
	 * finds the words related to this move.
	 * 
	 * @param move
	 * @return
	 */
	private String findWords(Move move) {
		if (hORv == 0) {
			return findVerticalWord(move);
		} else {
			return findHorizontalWord(move);
		}
	}

	private String findVerticalWord(Move move) {
		int[] loc = move.getMoveLocation();
		String word = "";
		int start = loc[0];
		int end = loc[0];
		while (start >= 0
				&& playBoard.getBoardNormalTile(start - 1, loc[1]) != null) {
			start--;
		}
		while (end <= playBoard.getBoardCol()
				&& playBoard.getBoardNormalTile(end + 1, loc[1]) != null) {
			end++;
		}
		if (start >= end) {
			return null;
		}
		for (int i = start; i <= end; i++) {
			char c;
			if (i == loc[0]) {
				c = move.getMoveNormalTile().getCharacter();
			} else {
				c = playBoard.getBoardNormalTile(i, loc[1]).getCharacter();
			}
			word += c;
		}
		return word;
	}

	private String findHorizontalWord(Move move) {
		int[] loc = move.getMoveLocation();
		String word = "";
		int start = loc[1];
		int end = loc[1];
		while (start >= 0
				&& playBoard.getBoardNormalTile(loc[0], start - 1) != null) {
			start--;
		}
		while (end <= playBoard.getBoardRow()
				&& playBoard.getBoardNormalTile(loc[0], end + 1) != null) {
			end++;
		}
		if (start >= end) {
			return null;
		}
		for (int i = start; i <= end; i++) {
			char c;
			if (i == loc[1]) {
				c = move.getMoveNormalTile().getCharacter();
			} else {
				c = playBoard.getBoardNormalTile(loc[0], i).getCharacter();
			}
			word += c;
		}
		return word;
	}

	public void reverseOrder() {
		isReverse = !isReverse;
		reverseRound = round;
	}

	public void negativePoints() {
		negative = -1;
	}

	/**
	 * activate special tile boom. Takes in the move where the tile is
	 * activated.
	 * 
	 * @param m
	 */
	public void boom(Move m) {
		int[] loc = m.getMoveLocation();
		for (int[] target : playBoard.getBoomtile()) {
			int i = loc[0] + target[0];
			int j = loc[1] + target[1];
			if (i >= 0 && j >= 0) {
				NormalTile t = null;
				playBoard.placeNormalTileOnBoard(i, j, t);
			}
		}
	}

	/**
	 * cause random point lose in other players.
	 */
	private void randomPointLose() {
		Random rand = new Random();
		for (Player p : players) {
			if (!p.equals(getPlayer())) {
				p.addScore(-rand.nextInt(20));
			}
		}
	}

	/**
	 * gets the current player
	 * 
	 * @return the current player
	 */
	public Player getPlayer() {
		if (!isReverse) {
			return players[round % num_player];
		} else {
			if (reverseRound < 0) {
				reverseRound += 32;
			}
			return players[reverseRound % num_player];
		}
	}

	/**
	 * helps exchange the tiles.
	 * 
	 * @param e
	 *            the tile place, represented by integer
	 */
	public void exchange(int[] e) {
		for (int i : e) {
			getPlayer().retire(i);
		}
		getPlayer().refillRack();
	}

	public Player getWinner() {
		int high = players[0].getScore();
		Player winner = players[0];
		for (Player p : players) {
			if (p.getScore() > high) {
				winner = p;
				high = p.getScore();
			}
		}
		return winner;
	}

	private int charSearch(char[] array, char c) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == c) {
				return i;
			}
		}
		return -1;
	}

	public boolean playerTilesEmpty() {
		return (getPlayer().getPocketSize() == 0);
	}

}
