package edu.cmu.cs.cs214.hw4.gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import edu.cmu.cs.cs214.hw4.core.GameControl;

public class TEMP{
	
	private static Gui g;

	public static void main(String[] args) {
		g = new Gui(new GameControl(4));
		
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGameBoard();
            }
        });
	}
	
	private static void createAndShowGameBoard(){
		g.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		g.pack();
		g.setVisible(true);
	}
}
