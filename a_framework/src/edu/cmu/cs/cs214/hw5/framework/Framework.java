package edu.cmu.cs.cs214.hw5.framework;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

import twitter4j.TwitterException;
import edu.cmu.cs.cs214.hw5.data.Data;
import edu.cmu.cs.cs214.hw5.plugin.AnalysisPlugin;
import edu.cmu.cs.cs214.hw5.plugin.DataPlugin;

/**
 * The Class Framework.
 */
public class Framework {

	private String userID;
	private DataPlugin currentDatabase;
	private Data currentData;
	private ArrayList<DataPlugin> allDatabases;
	private ArrayList<AnalysisPlugin> currentAnalyses;
	private ArrayList<AnalysisPlugin> allAnalyses;
	private int rateLimit;

	/**
	 * Instantiates a new framework.
	 */
	public Framework() {
		userID = "";
		currentDatabase = null;
		currentData = null;
		currentAnalyses = new ArrayList<AnalysisPlugin>();
		allDatabases = new ArrayList<DataPlugin>();
		allAnalyses = new ArrayList<AnalysisPlugin>();
		rateLimit = -1;
	}

	/**
	 * Register an analysis plugin.
	 * 
	 * @param a
	 *            the analysis plugin being registered
	 */
	public void registerAnalysisPlugin(AnalysisPlugin a) {
		a.onRegister();
		allAnalyses.add(a);
	}

	/**
	 * Register a data plugin.
	 * 
	 * @param d
	 *            the data plugin being registered
	 */
	public void registerDataPlugin(DataPlugin d) {
		d.onRegister();
		allDatabases.add(d);
	}

	/**
	 * Gets the all data plugins.
	 * 
	 * @return the all data plugins
	 */
	public ArrayList<DataPlugin> getAllDataPlugins() {
		return allDatabases;
	}

	/**
	 * Gets the all analysis plugin.
	 * 
	 * @return the all analysis plugins
	 */
	public ArrayList<AnalysisPlugin> getAllAnalysisPlugin() {
		return allAnalyses;
	}

	/**
	 * Sets the maximum rate limit.
	 * 
	 * @param limit
	 *            the new maximum rate limit
	 */
	public void setRateLimit(int limit) {
		rateLimit = limit;
	}

	/**
	 * Gets the maximum rate limit.
	 * 
	 * @return the maximum rate limit
	 */
	public int getRateLimit() {
		return rateLimit;
	}

	/**
	 * sets the data plugin.
	 * 
	 * @param d
	 *            the d
	 */
	public void setCurrentDataPlugin(DataPlugin d) {
		System.out.println("current data plugin set to " + d.getName());
		currentDatabase = d;
	}

	/**
	 * Removes the data plugin.
	 * 
	 * @param d
	 *            the d
	 * @return true, if successful
	 */
	public boolean removeDataPlugin(DataPlugin d) {
		if (currentDatabase.equals(d)) {
			d.onUnregister();
			currentDatabase = null;
			if (currentData != null) {
				currentData = null;
			}
			return true;
		}
		return false;
	}

	/**
	 * Removes the analysis plugin.
	 * 
	 * @param a
	 *            the a
	 * @return true, if successful
	 */
	public boolean removeAnalysisPlugin(AnalysisPlugin a) {
		if (allAnalyses.contains(a)) {
			a.onUnregister();
			allAnalyses.remove(a);
			return true;
		}
		return false;
	}

	/**
	 * Notify data plugin to retrieve data. After this function the data will
	 * have actual data
	 */
	public void notifyDataPlugin() {
		if (currentDatabase != null) {
			try {
				currentData = currentDatabase.retrieveData(userID);
			} catch (TwitterException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Notify analysis plugin.
	 */
	public void notifyAnalysisPlugins() {
		for (AnalysisPlugin a : currentAnalyses) {
			a.update(currentData);
		}
	}

	/**
	 * Sets the current user.
	 * 
	 * @param id
	 *            the new current user
	 */
	public void setCurrentUser(String id) {
		userID = id;
	}

	/**
	 * Gets the current user.
	 * 
	 * @return the current user
	 */
	public String getCurrentUser() {
		return userID;
	}

	/**
	 * Gets the display.
	 * 
	 * @return the display
	 */
	public ArrayList<JPanel> getDisplays() {
		ArrayList<JPanel> display = new ArrayList<JPanel>();
		for (AnalysisPlugin ap : currentAnalyses) {
			if (ap != null) {
				display.add(ap.getDisplay());
			}
		}
		return display;
	}

	/**
	 * Stop application.
	 */
	public void stopApplication() {
		System.exit(0);
	}

	/**
	 * changes the display when analysis plugins change.
	 * 
	 * @param ap
	 *            the analysis plugin that's changed
	 */
	public void changeDisplay(AnalysisPlugin ap) {
		System.out.println("changing display...");
		if (currentAnalyses.contains(ap)) {
			System.out.println("deleted");
			ap.getDisplay().removeAll();
			currentAnalyses.remove(ap);
		} else {
			System.out.println("added analysis");
			currentAnalyses.add(ap);
		}
	}
}
