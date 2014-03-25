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

public class PlayerPanel extends JPanel{

	private static final long serialVersionUID = 4426639239907132467L;
	
	private final Gui gui;
	private NormalTile[] r;
	private JPanel rack;

	private ArrayList<JButton> all_normal_tile;

	public PlayerPanel(Gui GUI){
		this.gui = GUI;
		all_normal_tile = new ArrayList<JButton>();
		rack = new JPanel();
		GameControl g = gui.getGameControl();
		
		GridLayout layout = new GridLayout(3,2);
		setLayout(layout);
		//setBackground(Color.pink);
		
		JLabel name = new JLabel();
		name.setText("player " + Integer.toString(g.getPlayer().getID()) + " Turn");
		add(name);
		
		JLabel constant = new JLabel();
		constant.setText("Your Rack: ");
		add(constant);
		
		JLabel another = new JLabel();
		another.setText("Your SpecialTile: ");
		add(another);
		
		//rack panel	
		r = g.getPlayer().getCurrentPlayingRack();
		for (int i = 0; i < r.length; i ++){
			String s = Character.toUpperCase((r[i].getCharacter())) + "," + Integer.toString(r[i].getLetterScore());
			JButton t = new JButton(s);
			t.putClientProperty("JButton.buttonType", "gradient");
			TileListener tl = new TileListener(r[i], gui, this);
			t.addActionListener(tl);
			all_normal_tile.add(t);
			rack.add(t);
		}
		
		//special panel
		
		add(rack);
	}

	public void updatePlayerSelect(NormalTile nt, SpecialTile st){
		char c = nt.getCharacter();
		for (JButton b : all_normal_tile){
			if (b.getText().toCharArray()[0] == Character.toUpperCase(c)){
				for( ActionListener al : b.getActionListeners() ) {
			        b.removeActionListener( al );
			    }
				b.setText(" used ");
				break;
			}
		}
	}
	
	public void updatePlayerPlay(){
		rack.removeAll();
		
		r = gui.getGameControl().getPlayer().getCurrentPlayingRack();
		all_normal_tile.clear();
		
		for (int i = 0; i < r.length; i ++){
			String s = Character.toUpperCase((r[i].getCharacter())) + "," + Integer.toString(r[i].getLetterScore());
			JButton t = new JButton(s);
			t.putClientProperty("JButton.buttonType", "gradient");
			TileListener tl = new TileListener(r[i], gui, this);
			t.addActionListener(tl);
			all_normal_tile.add(t);;
			rack.add(t);
		}
		revalidate();
		repaint();
	}
}
