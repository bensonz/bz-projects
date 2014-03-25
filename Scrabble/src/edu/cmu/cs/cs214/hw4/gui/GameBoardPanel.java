package edu.cmu.cs.cs214.hw4.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import edu.cmu.cs.cs214.hw4.core.Board;
import edu.cmu.cs.cs214.hw4.core.Move;

public class GameBoardPanel extends JPanel {

	private static final long serialVersionUID = -3101050523744631971L;

	private Board b;
	private Gui GUI;

	private final int columns;
	private final int rows;

	private JButton[][] boardDisplayArray;
	private JLabel currentPlayerLabel;

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
		currentPlayerLabel = new JLabel();
		boardDisplayArray = new JButton[rows][columns];
		add(currentPlayerLabel, BorderLayout.NORTH);
		add(createBoardPanel(), BorderLayout.CENTER);
	}

	private JPanel createBoardPanel() {
		JPanel panel = new JPanel();
		 panel.setLayout(new GridLayout(rows, columns,0,0));
		 
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < columns; y++) {
				boardDisplayArray[x][y] = new JButton();
				boardDisplayArray[x][y].setText(getBoardDisplayText(x, y));
				boardDisplayArray[x][y].addActionListener(new SquareListener(x,y, GUI));
				boardDisplayArray[x][y].putClientProperty("JButton.buttonType", "square");
				panel.add(boardDisplayArray[x][y]);
			}
		}
		return panel;
	}
	
	public void updateBoard(Move m){
		int[] loc = m.getMoveLocation();
		boardDisplayArray[loc[0]][loc[1]].setText(Character.toString(m.getMoveNormalTile().getCharacter()));		
	}

	private String getBoardDisplayText(int x, int y) {
		int info = b.getBoardScoring(x, y);
		if (x == 7 && y == 7) {
			return "STAR";
		}
		switch (info) {
		case (0):
			return "  ";
		case (1):
			return "DL";
		case (2):
			return "DW";
		case (3):
			return "TL";
		case (4):
			return "TW";
		default:
			return "FUCK";
		}
	}
}
