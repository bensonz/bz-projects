package edu.cmu.cs.cs214.hw5.plugin;

import java.io.IOException;

import twitter4j.TwitterException;
import edu.cmu.cs.cs214.hw5.data.Data;

/**
 * The Interface DataPlugin.
 */
public interface DataPlugin {

	/**
	 * On register.
	 */
	public void onRegister();

	/**
	 * On unregister.
	 */
	public void onUnregister();

	/**
	 * Retrieve data.
	 * 
	 * @param userID
	 *            the user id
	 * @return the data
	 * @throws TwitterException
	 *             the twitter exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public Data retrieveData(String userID) throws TwitterException,
			IOException;

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName();

}
