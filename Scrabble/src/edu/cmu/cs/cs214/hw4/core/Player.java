package edu.cmu.cs.cs214.hw4.core;

public class Player {
	private final int ID;
	private int score;
	private Rack rack;
	private int num_special;
	private static final int MAX_NUM = 7;
	/**
	 * player class, takes in an integer ID and initialized tiles for him
	 * @param id
	 * @param initTiles
	 */
	public Player(int id){
		ID = id;
		score = 0;
		rack = new Rack(MAX_NUM);
		num_special = 0;
	}
	
	public int getID(){
		return ID;
	}
	
	public int getScore(){
		return score;
	}
	
	public NormalTile[] getCurrentPlayingRack(){
		return rack.getArrayOfNormalTile();
	}
	
	public Rack getWholeRack(){
		return rack;
	}
	
	public int addScore(int add){
		score += add;
		return 0;
	}
	
	public int purchaseTile(SpecialTile st){
		if (score <= st.getPrice()){
			return 1;
		}
		score -= st.getPrice();
		rack.addSpecialTile(st);
		return 0;
	}

	public void refillRack() {
		rack.refillRack();
	}

	public void retire(int i) {
		rack.retire(i);
	}
	
}
