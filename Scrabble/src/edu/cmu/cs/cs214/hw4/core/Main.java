package edu.cmu.cs.cs214.hw4.core;

import static edu.cmu.cs.cs214.hw4.core.Constants.*;
import edu.cmu.cs.cs214.hw4.gui.Gui;

public class Main {

	// game status
	private boolean gameOver;
	private int num_passes;

	// game
	private GameControl g;

	// GUI
	private Gui gui;

	public void main(String[] args) {
		gameOver = false;
		num_passes = 0;
		g = new GameControl(2);
		runGame();
	}

	/**
	 * call this method to run the game
	 */
	public void runGame() {
		while (!gameOver) {
			Player p = g.getPlayer();
			if (p.getCurrentPlayingRack().length == 0) {
				gameOver();
				break;
			}
			Board b = g.getPlayBoard();
			// GUI.refreshBoard(b);
			// GUI.refreshPlayer(p);
			int command = PASS;// =GUI.getCommand();
			if (num_passes > 5) {
				gameOver();
				break;
			}
			if (useInput(command) != PASS || useInput(command) != PURCHASE) {
				g.nextRound();
			}
		}
		Player winner = g.getWinner();
		// GUI.refreshEndGame(winner);
		return;
	}

	/**
	 * reads input from GUI and then does different things
	 * 
	 * @param Command
	 *            the command returned from GUI
	 */
	public int useInput(int Command) {
		// gets input from GUI.
		switch (Command) {
		case (PURCHASE):
			SpecialTile st = null;// = GUI.getPurchase();
			g.getPlayer().purchaseTile(st);
			return PURCHASE;
		case (PASS):
			num_passes++;
			g.nextRound();
			return PASS;
		case (PLAY):
			num_passes = 0;
			// GUI.getPlay();
			Move[] m = null;
			g.enablePlay(m);
			return PLAY;
		case (EXCHANGE):
			// GUI.getExchangeTiles;
			g.exchange(new int[] { 0, 1, 4 });
			return EXCHANGE;
		}
		return 0;
	}

	public void gameOver() {
		gameOver = true;
		// Show some GUI stuff
	}

}
