package edu.cmu.cs.cs214.hw5.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import edu.cmu.cs.cs214.hw5.data.Data;

public class TwitterPlugin implements DataPlugin {
	private static final String NAME = "Twitter Plugin";
	private Twitter twitter;
	private Data twitterData;
	private String user;

	private HashMap<Long, String> home;
	private ArrayList<String> friends;
	private HashMap<Long, String> friendsTimeline;

	public TwitterPlugin() {
	}

	/**
	 * Gets the self posts.
	 * 
	 * @return the self posts
	 * @throws TwitterException
	 *             the twitter exception
	 */
	private void getSelfPosts() throws TwitterException {
		ResponseList<Status> homeTime = twitter.getUserTimeline(user);
		Iterator<Status> homeTimeIt = homeTime.iterator();

		while (homeTimeIt.hasNext()) {
			Status next = homeTimeIt.next();
			String nextPost = next.getText();
			long key = next.getCreatedAt().getTime();
			home.put(key, nextPost);
		}
	}

	/**
	 * Gets the friends and posts.
	 * 
	 * @return the friends and posts
	 * @throws TwitterException
	 *             the twitter exception
	 */
	private void getFriendsAndPosts() throws TwitterException {
		User currentUser = twitter.showUser(user);
		long currentUserID = currentUser.getId();
		long[] friendsIDs = twitter.getFriendsIDs(currentUserID, -1).getIDs();

		ResponseList<User> friendUsers = twitter.lookupUsers(friendsIDs);
		Iterator<User> friendUsersIt = friendUsers.iterator();

		while (friendUsersIt.hasNext()) {
			User friendUser = friendUsersIt.next();

			long key = friendUser.getId();
			String friend = friendUser.getName();
			friends.add(friend);

			ResponseList<Status> friendsTime = twitter.getUserTimeline(friend);
			Iterator<Status> friendsTimeIt = friendsTime.iterator();
			while (friendsTimeIt.hasNext()) {
				String nextPost = friendsTimeIt.next().getText();
				friendsTimeline.put(key, nextPost);
			}
		}
	}

	@Override
	public void onRegister() {
		twitter = new TwitterFactory().getInstance();
		user = null;
		home = null;
		friends = null;
		friendsTimeline = null;
	}

	@Override
	public void onUnregister() {
		twitter = null;
	}

	@Override
	public Data retrieveData(String userID) {
		user = userID;
		twitterData = new Data(user);
		home = new HashMap<Long, String>();
		friends = new ArrayList<String>();
		friendsTimeline = new HashMap<Long, String>();
		try {
			getSelfPosts();
			getFriendsAndPosts();
		} catch (TwitterException e) {
			System.out.println("Twitter exception occured, supply auth.");
		}
		twitterData.setTimelineWithPosts(home);
		twitterData.setFriends(friends);
		twitterData.setFriendstimeline(friendsTimeline);
		return twitterData;
	}

	@Override
	public String getName() {
		return NAME;
	}

}
