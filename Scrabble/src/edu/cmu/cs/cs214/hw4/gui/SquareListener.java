package edu.cmu.cs.cs214.hw4.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.cmu.cs.cs214.hw4.core.GameControl;
import edu.cmu.cs.cs214.hw4.core.Move;
import edu.cmu.cs.cs214.hw4.core.NormalTile;

public class SquareListener implements ActionListener {

	private final int x;
	private final int y;
	private final Gui gui;
	/**
	 * Creates a new square listener to get click events at a specific game grid
	 * coordinate.
	 */
	public SquareListener(int x, int y,Gui GUI) {
		this.x = x;
		this.y = y;
		this.gui = GUI;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		gui.notifySquareSelected(x,y);
	}

}
