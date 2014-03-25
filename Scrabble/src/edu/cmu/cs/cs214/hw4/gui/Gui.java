/**
 * 
 */
package edu.cmu.cs.cs214.hw4.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import edu.cmu.cs.cs214.hw4.core.GameControl;
import edu.cmu.cs.cs214.hw4.core.Move;
import edu.cmu.cs.cs214.hw4.core.NormalTile;
import edu.cmu.cs.cs214.hw4.core.SpecialTile;

/**
 * @author bz Gui class. Display the GUI, and get the player's interaction
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

	public Gui(GameControl g) {
		game = g;
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

	public void initGUI() {
		setTitle(NAME);
		setBackground(Color.white);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		panel.add(gameBoardp, BorderLayout.CENTER);
		panel.add(actionp, BorderLayout.EAST);
		panel.add(playerp, BorderLayout.SOUTH);

		add(panel);
	}
	
	public GameControl getGameControl() {
		return game;
	}

	/**
	 * Notifications
	 * 
	 * @param x
	 * @param y
	 */
	public void notifySquareSelected(int x, int y) {
		if (selectedTile) {
			if (normalTileSel != null) {
				m = new Move(x, y, normalTileSel);
			} else if (specialTileSel != null) {
				m = new Move(x, y, normalTileSel);
			} else {
				showDialog(this, "fuck", "something wrong...");
			}
			if (game.placeMove(m) != 0){
				//fail
				showDialog(this,"invalid", "try agian");
			}else{
				//succeed
				selectedTile = false;
				play.add(m);
				updateBoard();
			}
		} else {
			showDialog(this, "select tile first",
					"You have to selected a tile to palce on board!");
		}
	}

	private void updateBoard() {
		gameBoardp.updateBoard(m);
	}

	public void notifyTileSelected(NormalTile nt, SpecialTile st){
		selectedTile = true;
		normalTileSel = nt;
		specialTileSel = st;
	}
	
	private static void showDialog(Component component, String title,
			String message) {
		JOptionPane.showMessageDialog(component, message, title,
				JOptionPane.INFORMATION_MESSAGE);
	}

	public void updatePlay() {
		if (play == null || play.size() == 0){
			// no move
			return;
		}
		//copy play, then re-initialize
		Move[] p = new Move[play.size()];
		for (int i = 0; i < play.size(); i ++){
			p[i] = play.get(i);
		}
		play = new ArrayList<Move>();
		if (game.enablePlay(p) != 0){
			updateRecall();
			showDialog(this,"Play is not valid!","Please try agian!");
		}
		else{
			game.nextRound();
			playerp.updatePlayerPlay();
		}
		return;
	}

	public void updateExchange() {
		System.out.print("EXC \n");	
	}

	public void updatePurcahse() {
		System.out.print("PUR \n");		
	}

	public void updateRecall() {
		System.out.print("REC \n");
	}

	public void updatePass() {
		System.out.print("PAS \n");
	}

}
