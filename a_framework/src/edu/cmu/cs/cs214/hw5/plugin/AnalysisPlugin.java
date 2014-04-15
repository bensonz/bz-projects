package edu.cmu.cs.cs214.hw5.plugin;

import javax.swing.JPanel;

import edu.cmu.cs.cs214.hw5.data.Data;

/**
 * The Interface AnalysisPlugin.
 */
public interface AnalysisPlugin {

	/**
	 * On register.
	 * 
	 * @param framework
	 *            the framework
	 */
	public void onRegister();

	/**
	 * On unregister.
	 * 
	 * @param framework
	 *            the framework
	 */
	public void onUnregister();

	/**
	 * gets the display from analysis.
	 * 
	 * @return a JPanel ready for display
	 */
	public JPanel getDisplay();

	/**
	 * updates the JPanel according to the date.
	 * 
	 * @param data
	 *            the changed data
	 */
	public void update(Data data);

	/**
	 * width for display.
	 * 
	 * @return width
	 */
	public int getWidth();

	/**
	 * height for display.
	 * 
	 * @return height
	 */
	public int getHeight();

	/**
	 * the name for this plugin.
	 * 
	 * @return the name
	 */
	public String getName();

}
