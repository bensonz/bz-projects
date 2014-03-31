package edu.cmu.cs.cs214.hw4.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import edu.cmu.cs.cs214.hw4.core.Board;
import edu.cmu.cs.cs214.hw4.core.Move;
import edu.cmu.cs.cs214.hw4.core.SpecialTile;

public class GameBoardPanel extends JPanel {

	private static final long serialVersionUID = -3101050523744631971L;

	private Board b;
	private Gui GUI;

	private final int columns;
	private final int rows;

	private JButton[][] boardDisplayArray;

	public GameBoardPanel(Gui gui) {
		b = gui.getGameControl().getPlayBoard();
		this.GUI = gui;
		columns = b.getBoardCol();
		rows = b.getBoardRow();
		// setLayout(new BorderLayout());
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		setBackground(Color.cyan);
		initBoard();
	}

	private void initBoard() {
		boardDisplayArray = new JButton[rows][columns];
		add(createBoardPanel(), BorderLayout.CENTER);
	}

	private JPanel createBoardPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(rows, columns, 0, 0));

		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < columns; y++) {
				boardDisplayArray[x][y] = new JButton();
				boardDisplayArray[x][y].setText(getBoardDisplayText(x, y));
				boardDisplayArray[x][y].addActionListener(new SquareListener(x,
						y, GUI));
				boardDisplayArray[x][y].putClientProperty("JButton.buttonType",
						"square");
				panel.add(boardDisplayArray[x][y]);
			}
		}
		return panel;
	}

	public void updateBoardMove(Move m) {
		int[] loc = m.getMoveLocation();
		if (m.getMoveNormalTile() != null) {
			char c = m.getMoveNormalTile().getCharacter();
			boardDisplayArray[loc[0]][loc[1]].setText(" "
					+ Character.toString(c) + " ");
			boardDisplayArray[loc[0]][loc[1]].repaint();
		} else {
			boardDisplayArray[loc[0]][loc[1]].setBackground(Color.red);
			boardDisplayArray[loc[0]][loc[1]].setOpaque(true);
			boardDisplayArray[loc[0]][loc[1]].repaint();
		}
	}

	private String getBoardDisplayText(int x, int y) {
		int info = b.getBoardScoring(x, y);
		if (x == 7 && y == 7) {
			return "  S ";
		}
		switch (info) {
		case (0):
			return "    ";
		case (1):
			return " DL ";
		case (2):
			return " DW ";
		case (3):
			return " TL ";
		case (4):
			return " TW ";
		default:
			return "FUCK";
		}
	}

	public void recall(ArrayList<Move> play) {
		for (Move m : play) {
			int[] loc = m.getMoveLocation();
			boardDisplayArray[loc[0]][loc[1]].setText(getBoardDisplayText(
					loc[0], loc[1]));
		}
		revalidate();
		repaint();
	}

	public void updateBoardAfterPlay() {
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < columns; y++) {
				if (b.getBoardNormalTile(x, y) == null) {
					if (b.getBoardSpecialTile(x, y) != null
							&& b.getBoardSpecialTile(x, y).getBelong() == GUI
									.getPlayerID()) {
						boardDisplayArray[x][y].setBackground(Color.red);
						boardDisplayArray[x][y].setOpaque(true);

					} else {
						boardDisplayArray[x][y].setText(getBoardDisplayText(x,
								y));
						boardDisplayArray[x][y].setOpaque(false);
					}
				} else {
					boardDisplayArray[x][y].setBackground(Color.green);
					boardDisplayArray[x][y].setOpaque(true);
				}
			}
		}
		revalidate();
		repaint();
	}
}
