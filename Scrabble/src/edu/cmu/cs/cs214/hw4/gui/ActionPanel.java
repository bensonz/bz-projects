package edu.cmu.cs.cs214.hw4.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ActionPanel extends JPanel {
	private static final long serialVersionUID = 1287416458747119884L;

	private JButton play;
	private JButton exchange;
	private JButton purchase;
	private JButton recall;
	private JButton pass;
	
	private Gui gui;

	public ActionPanel(Gui GUI) {
		setLayout(new GridLayout(5, 1));
		setBackground(Color.yellow);
		this.gui = GUI;
		
		play = new JButton("Play");
		exchange = new JButton("Exchange");
		purchase = new JButton("Purchase");
		recall = new JButton("Recall");
		pass = new JButton("Pass");
		play.addActionListener(new ControlListener(0,gui));
		exchange.addActionListener(new ControlListener(1,gui));
		purchase.addActionListener(new ControlListener(2,gui));
		recall.addActionListener(new ControlListener(3,gui));
		pass.addActionListener(new ControlListener(4,gui));
		
		add(play);
		add(exchange);
		add(purchase);
		add(recall);
		add(pass);
	}
}
