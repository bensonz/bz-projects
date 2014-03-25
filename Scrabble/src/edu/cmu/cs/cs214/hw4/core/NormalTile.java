package edu.cmu.cs.cs214.hw4.core;

import static edu.cmu.cs.cs214.hw4.core.Constants.*;

public class NormalTile{
	private int letterScore;
	private char character;

	/**
	 * normal tiles. Consists of a letter and its score.
	 */
	public NormalTile(char letter) {
		int a = (int) 'a';
		int l = (int) letter;
		if (l < a || l > (a+26)){
			throw new IllegalStateException("letter is not able to be created");
		}
		else{
			character = letter;
			letterScore = LETTER_VALUES.get(letter);
		}
	}
	
	public int getLetterScore() {
		return letterScore;
	}

	public char getCharacter() {
		return character;
	}
	@Override
	public boolean equals(Object o){
		if (!(o instanceof NormalTile)){
			return false;
		}
		NormalTile n = (NormalTile)o;
		return n.getCharacter() == this.getCharacter();
	}
	@Override
	public int hashCode(){
		return 33*character + letterScore;
		
	}
}
