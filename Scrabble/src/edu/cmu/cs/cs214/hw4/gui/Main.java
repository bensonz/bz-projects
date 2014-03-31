package edu.cmu.cs.cs214.hw4.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import edu.cmu.cs.cs214.hw4.core.GameControl;

public class Main {

	private static Gui g;
	private static JFrame frame;
	private static int num_players;

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				askForNum_players();
				createAndShowGameBoard();
			}
		});
	}

	private static void askForNum_players() {
		// Custom button text
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Object[] options = { "2", "3", "4" };
		int n = JOptionPane.showOptionDialog(frame, "How many players?",
				"Scrabble", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		num_players = n + 2;
		frame.pack();
		frame.setVisible(true);
		return;
	}

	private static void createAndShowGameBoard() {
		frame.setVisible(false);
		g = new Gui(new GameControl(num_players));
		g.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		g.pack();
		g.setVisible(true);
	}
}
