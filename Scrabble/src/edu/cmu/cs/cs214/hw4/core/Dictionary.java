package edu.cmu.cs.cs214.hw4.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Dictionary {
	private static HashMap<String, String> hmap_dict = new HashMap<String, String>();
	
	//Methods imported from class notes
	public Dictionary(String fileName){
		readDictionary(fileName);
	}
	
	public HashMap<String, String> getDict(){
		return hmap_dict;
	}
	
	/**
	 * searches the string in the dictionary
	 * @param word
	 * @return true if it's in the dictionary
	 */
	public boolean search(String word){
		return (hmap_dict.get(word) != null);
	}
	
	public void readDictionary(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				tempString = tempString.trim();
				tempString = tempString.toLowerCase();
				hmap_dict.put(tempString, tempString);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					System.out.println(e1.toString());
				}
			}
		}
	}
}
