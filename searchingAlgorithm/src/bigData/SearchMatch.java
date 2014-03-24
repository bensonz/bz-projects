package bigData;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchMatch {

	// inputs
	private final User[] usr;
	private final ArrayList<String> tags;

	// outputs
	private ArrayList<User[]> out_users;
	private ArrayList<String[]> out_tags;

	// mapping
	private static HashMap<String, String> hmap_tag = new HashMap<String, String>();
	private static HashMap<Integer, User> hmap_id = new HashMap<Integer, User>();

	/**
	 * constructor of SearchMatch.
	 * 
	 * @param input_usr
	 *            the input users. I expect users to have a field tags that
	 *            belongs to it.
	 * @param input_tags
	 *            the input tags
	 */
	public SearchMatch(User[] input_usr, ArrayList<String> input_tags) {
		usr = input_usr;
		tags = input_tags;
		out_users = new ArrayList<User[]>();
		out_tags = new ArrayList<String[]>();
	}

	/**
	 * finds the matches by linearly looping through it
	 * 
	 * @return 0
	 */
	public int findMatchLinearly() {
		// first layer of loop
		for (int i = 0; i < usr.length; i++) {
			User current = usr[i];
			// second layer. only loops users after index i
			for (int j = i + 1; j < usr.length; j++) {
				User check = usr[j];
				// if they share more than two tags, put it into output
				if (getSameTag(current, check).length >= 2) {
					out_users.add(new User[] { current, check });
					out_tags.add(getSameTag(current, check));
				}
			}
		}
		return 0;
	}

	/**
	 * finds the matches by using a hash table
	 * 
	 * @return 0
	 */
	public int findMatchByHash() {

		// construct the hash map
		for (User u : usr) {
			int id = u.getID();
			ArrayList<String> user_tags = u.getTags();
			hmap_id.put(id, u);
			for (String tag : user_tags) {
				if (hmap_tag.get(tag) == null) {
					// tag not initialized
					hmap_tag.put(tag, Integer.toString(id) + " ");
					break;
				}
				// already initialized
				String temp = hmap_tag.get(tag);
				hmap_tag.put(tag, temp + Integer.toString(id) + " ");
			}
		}
		/*
		 * after above procedures, we'll have a hmap_tag with every tag mapping
		 * to a String containing all user ID separated by space. now we loop
		 * through the users again to check for multiple tags
		 */
		for (User u : usr) {
			int id = u.getID();
			ArrayList<String> user_tags = u.getTags();
			for (String tag : user_tags) {
				if (hmap_tag.get(tag) != null) {
					// check for sharing status
					String temp = hmap_tag.get(tag);
					ArrayList<Integer> IDs_sharing_tag = stringToID(temp);
					for (int ID : IDs_sharing_tag) {
						if (ID == id) {
							// it's himself...
							continue;
						}
						User second = hmap_id.get(ID);
						if (getSameTag(u, second).length >= 2) {
							// we found a match! horray!
							if (duplicateOutput(u, second)) {
								continue;
							}
							User[] new_couple = new User[] { u, second };
							out_users.add(new_couple);
							out_tags.add(getSameTag(u, second));
						}
					}
				}
			}
		}
		return 0;
	}

	private boolean duplicateOutput(User u1, User u2) {
		for (User[] ou : out_users) {
			if ((ou[0].equals(u1) && ou[1].equals(u2))
					|| (ou[1].equals(u1) && ou[0].equals(u2))) {
				return true;
			}
		}
		return false;
	}

	private ArrayList<Integer> stringToID(String temp) {
		ArrayList<Integer> read = new ArrayList<Integer>();
		char[] char_array = temp.toCharArray();
		String s = "";
		for (int i = 0; i < char_array.length; i++) {
			char c = char_array[i];
			if (c != ' ') {
				// an integer!
				s += Character.toString(c);
			} else {
				// integer reading done
				read.add(Integer.parseInt(s));
				s = "";
			}
		}
		return read;
	}

	/**
	 * gets the same tags that are shared between two users
	 * 
	 * @param current
	 *            user 1
	 * @param check
	 *            user 2
	 * @return a String[] containing all the same tags.
	 */
	private String[] getSameTag(User current, User check) {
		ArrayList<String> ref = current.getTags();
		int size = (ref.size() <= check.getTags().size()) ? ref.size() : check
				.getTags().size();
		String[] same = new String[size];
		int count = 0;
		for (String s : check.getTags()) {
			if (ref.contains(s)) {
				same[count] = s;
				count++;
			}
		}
		if (same.length != count) {
			// we have a mismatch
			String[] temp = new String[count];
			for (String s : same) {
				if (s != null) {
					temp[count - 1] = s;
					count--;
				}
			}
			same = temp;
		}
		return same;
	}

	public void printOutput() {
		for (int i = 0; i < out_users.size(); i++) {
			System.out.print(out_users.get(i)[0].getID() + " "
					+ out_users.get(i)[1].getID() + ", ");
			System.out.print(out_tags.get(i)[0] + " " + out_tags.get(i)[1]
					+ "\n");
		}
	}
}
