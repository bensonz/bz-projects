package edu.cmu.cs.cs214.hw5.data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Class Data.
 */
public class Data {

	private static String USERID;
	private ArrayList<String> friends;
	private HashMap<Long, String> timelineWithPosts;
	private HashMap<Long, String> friendsTimeline;

	/**
	 * Constructor of data class.
	 * 
	 * @param id
	 *            the user id; used to get information related to this specific
	 *            user
	 */
	public Data(String id) {
		USERID = id;
	}

	/**
	 * Gets the friends.
	 * 
	 * @return the friends
	 */
	public ArrayList<String> getFriends() {
		return friends;
	}

	/**
	 * Sets the friends.
	 * 
	 * @param friends
	 *            the new friends
	 */
	public void setFriends(ArrayList<String> friends) {
		this.friends = friends;
	}

	/**
	 * Gets the timelineWithPosts.
	 * 
	 * @return the timelineWithPosts
	 */
	public HashMap<Long, String> getTimelineWithPosts() {
		return timelineWithPosts;
	}

	/**
	 * Sets the timelineWithPosts.
	 * 
	 * @param timelineWithPosts
	 *            the timelineWithPosts
	 */
	public void setTimelineWithPosts(HashMap<Long, String> timelineWithPosts) {
		this.timelineWithPosts = timelineWithPosts;
	}

	/**
	 * Gets the friendsTimeline.
	 * 
	 * @return the friendsTimeline
	 */
	public HashMap<Long, String> getFriendsTimeline() {
		return friendsTimeline;
	}

	/**
	 * Sets the friendsTimeline.
	 * 
	 * @param friendsTimeline
	 *            the friends timeline
	 */
	public void setFriendstimeline(HashMap<Long, String> friendsTimeline) {
		this.friendsTimeline = friendsTimeline;
	}

	/**
	 * Gets the userid.
	 * 
	 * @return the userid
	 */
	public String getUserid() {
		return USERID;
	}

}
