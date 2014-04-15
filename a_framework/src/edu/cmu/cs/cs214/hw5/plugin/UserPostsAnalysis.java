package edu.cmu.cs.cs214.hw5.plugin;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.cmu.cs.cs214.hw5.data.Data;

public class UserPostsAnalysis implements AnalysisPlugin {
	private static final String NAME = "User Posts Analysis";
	private static final int WIDTH = 400;
	private static final int HEIGHT = 300;
	private JPanel show;

	@Override
	public void onRegister() {
		show = new JPanel();
		show.setSize(WIDTH, HEIGHT);
	}

	@Override
	public void onUnregister() {
		show = null;
	}

	@Override
	public JPanel getDisplay() {
		return show;
	}

	@Override
	public void update(Data data) {
		HashMap<String, Integer> frequencymap = new HashMap<String, Integer>();
		HashMap<Long, String> timeline = data.getTimelineWithPosts();
		for (Long time : timeline.keySet()) {
			String post = timeline.get(time);
			String[] words = post.split("[ .,?!\"]+");
			for (String word : words) {
				if (word.contains("http://")) {
					// ignore website
					break;
				} else if (word == "I" || word == "i" || word == "is"
						|| word == "am" || word == "are") {
					// ignore common words
					continue;
				}
				if (frequencymap.containsKey(post)) {
					int old = frequencymap.get(post);
					frequencymap.remove(post);
					frequencymap.put(post, old + 1);
				} else {
					frequencymap.put(word, 1);
				}
			}
		}
		// now we have the frequency constructed
		drawPieChart(frequencymap);
	}

	/**
	 * Analyzes the data and draw a pie chart on a JPanel
	 * 
	 * @param frequencymap
	 *            the frequency hash map
	 */
	private void drawPieChart(HashMap<String, Integer> frequencymap) {
		ArrayList<Integer> values = new ArrayList<Integer>();
		ArrayList<String> mostFrequentwords = new ArrayList<String>();
		for (String word : frequencymap.keySet()) {
			if (mostFrequentwords.size() < 5) {
				mostFrequentwords.add(word);
			} else {
				for (String check : mostFrequentwords) {
					if (frequencymap.get(check) <= frequencymap.get(word)) {
						mostFrequentwords.remove(check);
						mostFrequentwords.add(word);
						break;
					}
				}
			}
		}
		for (String frequent : mostFrequentwords) {
			System.out.println("word most freq  = " + frequent);
			values.add(frequencymap.get(frequent));
		}
		show.setLayout(new GridLayout(6, 1));
		JLabel top = new JLabel("Most frequently used words:");
		show.add(top);
		JLabel[] all = new JLabel[mostFrequentwords.size()];
		for (int i = 0; i < mostFrequentwords.size(); i++) {
			all[i] = new JLabel(mostFrequentwords.get(i));
		}
		for (JLabel j : all) {
			show.add(j);
		}
		show.setBackground(Color.white);
	}

	@Override
	public int getWidth() {
		return WIDTH;
	}

	@Override
	public int getHeight() {
		return HEIGHT;
	}

	@Override
	public String getName() {
		return NAME;
	}

}
