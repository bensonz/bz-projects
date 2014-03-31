package edu.cmu.cs.cs214.hw4.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.cmu.cs.cs214.hw4.core.GameControl;
import edu.cmu.cs.cs214.hw4.core.Move;
import edu.cmu.cs.cs214.hw4.core.NormalTile;
import edu.cmu.cs.cs214.hw4.core.SpecialTile;

public class PlayerPanel extends JPanel {

	private static final long serialVersionUID = 4426639239907132467L;

	private final Gui gui;
	private final GameControl g;

	private JPanel rack;
	private JPanel store;
	private JLabel name;

	private ArrayList<JButton> all_normal_tile;
	private ArrayList<JButton> store_special_tile;

	public PlayerPanel(Gui GUI) {
		this.gui = GUI;
		this.g = gui.getGameControl();
		setLayout(new GridLayout(3, 2));

		all_normal_tile = new ArrayList<JButton>();
		store_special_tile = new ArrayList<JButton>();

		name = new JLabel();
		name.setText("player " + Integer.toString(g.getPlayer().getID())
				+ " Score:"
				+ Integer.toString(g.getPlayerPoints(g.getPlayer())));
		add(name);

		JLabel for_the_look = new JLabel();
		add(for_the_look);

		JLabel constant = new JLabel();
		constant.setText("Your Rack: ");
		add(constant);

		JLabel another = new JLabel();
		another.setText("SpecialTiles: ");
		add(another);

		// rack panel
		rack = new JPanel();
		NormalTile[] r = g.getPlayer().getCurrentPlayingRack();
		for (int i = 0; i < r.length; i++) {
			String s = Character.toUpperCase((r[i].getCharacter())) + ","
					+ Integer.toString(r[i].getLetterScore());
			JButton t = new JButton(s);
			t.putClientProperty("JButton.buttonType", "gradient");
			TileListener tl = new TileListener(r[i], gui, this);
			t.addActionListener(tl);

			all_normal_tile.add(t);
			rack.add(t);
		}
		add(rack);

		// special panel
		store = new JPanel();
		store.setLayout(new GridLayout(2, 3));
		for (int i = 0; i < 5; i++) {
			SpecialTile special = new SpecialTile(i);
			String text = special.toString() + special.getPrice();
			JButton st = new JButton(text);
			st.putClientProperty("JButton.buttonType", "gradient");
			TileListener tl = new TileListener(special, gui, this);
			st.addActionListener(tl);

			store_special_tile.add(st);
			store.add(st);
		}

		add(store);

	}

	public void updatePlayerSelect(NormalTile nt, SpecialTile st) {
		if (st != null || nt == null) {
			return;
		}
		char c = nt.getCharacter();
		for (JButton b : all_normal_tile) {
			if (b.getText().toCharArray()[0] == Character.toUpperCase(c)) {
				for (ActionListener al : b.getActionListeners()) {
					b.removeActionListener(al);
				}
				b.setText(" used ");
				break;
			}
		}
	}

	public void updatePlayerPlay() {
		name.setText("player " + Integer.toString(g.getPlayer().getID())
				+ " Score : " + Integer.toString(g.getPlayer().getScore()));

		rack.removeAll();

		NormalTile[] r = g.getPlayer().getCurrentPlayingRack();
		all_normal_tile.clear();

		for (int i = 0; i < r.length; i++) {
			String s = Character.toUpperCase((r[i].getCharacter())) + ","
					+ Integer.toString(r[i].getLetterScore());
			JButton t = new JButton(s);
			t.putClientProperty("JButton.buttonType", "gradient");
			TileListener tl = new TileListener(r[i], gui, this);
			t.addActionListener(tl);
			all_normal_tile.add(t);
			rack.add(t);
		}
		revalidate();
		repaint();
	}

	public void recall() {
		rack.removeAll();
		NormalTile[] r = g.getPlayer().getCurrentPlayingRack();
		all_normal_tile.clear();

		for (int i = 0; i < r.length; i++) {
			String s = Character.toUpperCase((r[i].getCharacter())) + ","
					+ Integer.toString(r[i].getLetterScore());
			JButton t = new JButton(s);
			t.putClientProperty("JButton.buttonType", "gradient");
			TileListener tl = new TileListener(r[i], gui, this);
			t.addActionListener(tl);
			all_normal_tile.add(t);
			rack.add(t);
		}
		revalidate();
		repaint();

	}

	public int getPlayerID() {
		return g.getPlayer().getID();
	}
}
