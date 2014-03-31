/**
 * 
 */
package edu.cmu.cs.cs214.hw4.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import edu.cmu.cs.cs214.hw4.core.Board;
import edu.cmu.cs.cs214.hw4.core.GameControl;
import edu.cmu.cs.cs214.hw4.core.Move;
import edu.cmu.cs.cs214.hw4.core.NormalTile;
import edu.cmu.cs.cs214.hw4.core.SpecialTile;

/**
 * @author bz Gui class. Display the GUI
 * 
 */
public class Gui extends JFrame {
	private static final long serialVersionUID = 2312321740014546618L;

	private static final String NAME = "Scrabble";

	// Game
	private GameControl game;

	// game separate panels
	private GameBoardPanel gameBoardp;
	private PlayerPanel playerp;
	private ActionPanel actionp;

	// game status
	private boolean selectedTile;
	private NormalTile normalTileSel;
	private SpecialTile specialTileSel;
	private Move m;
	private ArrayList<Move> play;
	private int num_passes;

	/**
	 * use this class solely for interactions between the game logic and the
	 * display.
	 * 
	 * @param g
	 *            the game input
	 */
	public Gui(GameControl g) {
		game = g;
		//game.getPlayer().addScore(100);
		gameBoardp = new GameBoardPanel(this);
		playerp = new PlayerPanel(this);
		actionp = new ActionPanel(this);
		selectedTile = false;
		normalTileSel = null;
		specialTileSel = null;
		m = null;
		play = new ArrayList<Move>();

		initGUI();
	}

	private void initGUI() {
		setTitle(NAME);
		setBackground(Color.white);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		panel.add(gameBoardp, BorderLayout.CENTER);
		panel.add(actionp, BorderLayout.EAST);
		panel.add(playerp, BorderLayout.SOUTH);

		add(panel);
	}

	/**
	 * get the gamecontrol for individual purposes
	 * 
	 * @return the game control being used now
	 */
	public GameControl getGameControl() {
		return game;
	}

	/**
	 * Notification for the square selected
	 * 
	 * @param x
	 * @param y
	 */
	public void notifySquareSelected(int x, int y) {
		if (selectedTile) {
			if (normalTileSel != null) {
				m = new Move(x, y, normalTileSel);
			} else if (specialTileSel != null) {
				m = new Move(x, y, specialTileSel);
			} else {
				showDialog(this, "fuck", "something wrong...");
			}
			if (!game.isValidMove(m)) {
				// fail
				showDialog(this, "invalid", "try agian");
			} else {
				// succeed
				selectedTile = false;
				play.add(m);
				updateBoardMove();
			}
		} else {
			showDialog(this, "select tile first",
					"You have to selected a tile to palce on board!");
		}
	}

	/**
	 * this updates the board look
	 */
	private void updateBoardMove() {
		gameBoardp.updateBoardMove(m);
	}

	/**
	 * this updates which tile has been selected.
	 * 
	 * @param nt
	 *            normal tile selected
	 * @param st
	 *            special tile selected.
	 */
	public void notifyTileSelected(NormalTile nt, SpecialTile st) {
		selectedTile = true;
		normalTileSel = nt;
		if (st != null) {
			if (game.getPlayer().purchaseTile(st) != 0) {
				showDialog(this, "Store", "You aint got enough money jezz");
				specialTileSel = null;
				selectedTile = false;
			} else {
				specialTileSel = st;
			}
		}
	}

	private static void showDialog(Component component, String title,
			String message) {
		JOptionPane.showMessageDialog(component, message, title,
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * this updates the play on both game control and gui
	 */
	public void updatePlay() {
		// printBoard(game.getPlayBoard());
		if (play == null || play.size() == 0) {
			// no move
			return;
		}
		num_passes = 0;
		// copy play, then re-initialize
		Move[] p = new Move[play.size()];
		for (int i = 0; i < play.size(); i++) {
			p[i] = play.get(i);
		}
		if (game.enablePlay(p) != 0) {
			updateRecall();
			play.clear();
			showDialog(this, "Play is not valid!", "Please try agian!");
		} else {
			gameBoardp.updateBoardAfterPlay();
			game.nextRound();
			playerp.updatePlayerPlay();
			printBoard(game.getPlayBoard());
		}
		play.clear();
		return;
	}

	/**
	 * this updates the exchange effect
	 */
	public void updateExchange() {
		num_passes = 0;
		gameBoardp.updateBoardAfterPlay();
		// For simplicity I'll just exchange all tiles..
		// null check is done in rack
		int[] e = new int[] { 0, 1, 2, 3, 4, 5, 6 };
		game.exchange(e);
		game.nextRound();
		playerp.updatePlayerPlay();
		// printBoard(game.getPlayBoard());
		play.clear();
		return;
	}

	/**
	 * the updates the recall
	 */
	public void updateRecall() {
		if (play.size() == 0) {
			return;
		}
		playerp.recall();
		gameBoardp.recall(play);
		play = new ArrayList<Move>();
	}

	/**
	 * this updates the pass
	 */
	public void updatePass() {
		if (num_passes >= 6 || game.playerTilesEmpty()) {
			showDialog(this, "GAME ENDED", "END with winner: plaer "
					+ game.getWinner().getID());
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
		num_passes++;
		gameBoardp.updateBoardAfterPlay();
		game.nextRound();
		playerp.updatePlayerPlay();
		return;
	}

	/**
	 * use this to print board to debug.
	 * 
	 * @param b
	 *            the board being printed
	 */
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

	/**
	 * gets the player id
	 * 
	 * @return the player ID
	 */
	public int getPlayerID() {
		return game.getPlayer().getID();
	}
}
