package edu.cmu.cs.cs214.hw5.plugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;

import edu.cmu.cs.cs214.hw5.data.Data;

/**
 * The Class GithubPlugin.
 */
public class GithubPlugin implements DataPlugin {

	private static final String NAME = "GitHub Plugin";
	private GitHubClient client;
	private Data githubData;
	private String user;
	private HashMap<Long, String> home;
	private ArrayList<String> friends;
	private HashMap<Long, String> friendsTimeline;
	private String token;

	/**
	 * Instantiates a new github plugin.
	 */
	public GithubPlugin() {
	}

	@Override
	public void onRegister() {
		client = new GitHubClient();
		user = null;
		home = null;
		friends = null;
		friendsTimeline = null;
		token = null;
	}

	@Override
	public void onUnregister() {
		client = null;
	}

	@Override
	public Data retrieveData(String userID) throws IOException {
		LoginRead login = new LoginRead("githublogin.txt");
		token = login.oauthCode();
		client.setOAuth2Token(token);
		user = userID;
		githubData = new Data(user);
		UserService currentUser = new UserService(client);
		RepositoryService repoServ = new RepositoryService(client);
		CommitService commServ = new CommitService(client);

		friends = new ArrayList<String>();
		friendsTimeline = new HashMap<Long, String>();
		home = new HashMap<Long, String>();
		try {
			getOthers(currentUser, repoServ, commServ);
			getSelf(currentUser, repoServ, commServ);
		} catch (RequestException e) {
			System.out.println("Rate Limit exceeded somehow.");
		} catch (IOException e) {
			System.out.println("Reading exception.");
		}

		githubData.setFriends(friends);
		githubData.setFriendstimeline(friendsTimeline);
		githubData.setTimelineWithPosts(home);
		return githubData;
	}

	/**
	 * Gets the others(friends/friends timeline) info.
	 * 
	 * @param currentUser
	 *            the current user
	 * @param repoServ
	 *            the repo serv
	 * @param commServ
	 *            the comm serv
	 * @return the others
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void getOthers(UserService currentUser, RepositoryService repoServ,
			CommitService commServ) throws IOException {

		List<User> currentUserFriends = currentUser.getFollowing(user);
		Iterator<User> curUserFriIt = currentUserFriends.iterator();
		while (curUserFriIt.hasNext()) {
			User friend = curUserFriIt.next();
			String friendName = friend.getLogin();
			friends.add(friendName);
			List<Repository> friendRepo = repoServ.getRepositories(friendName);
			for (Repository f : friendRepo) {
				List<RepositoryCommit> fComm = commServ.getCommits(f);
				Iterator<RepositoryCommit> fCommIt = fComm.iterator();
				while (fCommIt.hasNext()) {
					RepositoryCommit g = fCommIt.next();
					long key = g.getCommit().getCommitter().getDate().getTime();
					String commit = g.getCommit().getMessage();
					friendsTimeline.put(key, commit);
				}
			}
		}
	}

	/**
	 * Gets the self info.
	 * 
	 * @param currentUser
	 *            the current user
	 * @param repoServ
	 *            the repo serv
	 * @param commServ
	 *            the comm serv
	 * @return the self
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void getSelf(UserService currentUser, RepositoryService repoServ,
			CommitService commServ) throws IOException {

		List<Repository> repo = repoServ.getRepositories(user);
		for (Repository r : repo) {
			List<RepositoryCommit> rComm = commServ.getCommits(r);
			Iterator<RepositoryCommit> rCommIt = rComm.iterator();
			while (rCommIt.hasNext()) {
				RepositoryCommit s = rCommIt.next();
				long key = s.getCommit().getCommitter().getDate().getTime();
				String commit = s.getCommit().getMessage();
				home.put(key, commit);
			}
		}
	}

	@Override
	public String getName() {
		return NAME;
	}

}
