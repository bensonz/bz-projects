package bigData;

import java.util.ArrayList;

public class User {
	
	private static final int MAX_TAGS = 20;
	private int id;
	private ArrayList<String> tags;
	
	public User(int input_ID){
		id = input_ID;
		tags = new ArrayList<String>();
	}
	
	public int getID(){
		return id;
	}
	
	public ArrayList<String> getTags(){
		return tags;
	}
	
	public void addTag(String tag){
		if (tags.size() >= MAX_TAGS){
			return;
		}
		tags.add(tag);
	}

	@Override
	public boolean equals(Object o){
		if (!(o instanceof User)){
			return false;
		}
		User u = (User) o;
		return this.id == u.id;
	}
}
