package edu.cmu.cs.cs214.hw5.framework;

import javax.swing.SwingUtilities;

import edu.cmu.cs.cs214.hw5.plugin.*;

public class Main {

	private static final Framework PAGE = new Framework();
	private static final FrameworkGui GUI = new FrameworkGui(PAGE);

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowFramework();
			}
		});

	}

	protected static void createAndShowFramework() {
		/*
		 * register your plugins here use registerDataPlugin to register data
		 * plugins and registerAnalysisPlugin to register analysis plugins
		 */
		PAGE.registerDataPlugin(new TwitterPlugin());
		PAGE.registerDataPlugin(new GithubPlugin());

		PAGE.registerAnalysisPlugin(new TestAnalysisPlugin());
		PAGE.registerAnalysisPlugin(new UserPostsAnalysis());

		/*
		 * do not modify any line below
		 */
		GUI.setup();
		GUI.setVisible(true);
	}

}
