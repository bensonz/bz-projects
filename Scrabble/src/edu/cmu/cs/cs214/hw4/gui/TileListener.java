package edu.cmu.cs.cs214.hw4.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.cmu.cs.cs214.hw4.core.NormalTile;
import edu.cmu.cs.cs214.hw4.core.SpecialTile;

public class TileListener implements ActionListener {

	private final NormalTile ntile;
	private final SpecialTile stile;
	private final Gui gui;
	private final PlayerPanel p;

	/**
	 * Creates a new tile listener
	 */
	public TileListener(NormalTile nt, Gui GUI, PlayerPanel p) {
		this.ntile = nt;
		this.stile = null;
		this.gui = GUI;
		this.p = p;
	}

	public TileListener(SpecialTile st, Gui GUI, PlayerPanel p) {
		this.ntile = null;
		this.stile = st;
		this.gui = GUI;
		this.p = p;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		if (stile != null) {
			stile.setBelong(p.getPlayerID());
		}
		gui.notifyTileSelected(ntile, stile);
		p.updatePlayerSelect(ntile, stile);
	}

}