package edu.cmu.cs.cs214.hw4.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlListener implements ActionListener {

	private final Gui gui;
	private final int type;

	public ControlListener(int type, Gui GUI) {
		this.type = type;
		this.gui = GUI;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		switch (type) {
		case (0): // play
			gui.updatePlay();
		break;
		case(1) : // exchange
			gui.updateExchange();
		break;
		case(2) : // Purchase
			gui.updatePurcahse();
		break;
		case(3) : //recall
			gui.updateRecall();
		break;
		case(4):// pass
			gui.updatePass();
		default:
			break;
		}

	}
}
