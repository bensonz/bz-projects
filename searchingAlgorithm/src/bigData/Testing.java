package bigData;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Testing {
	private SearchMatch s;

	@Before
	public void setUp() throws Exception {
		User u1 = new User(100000);
		User u2 = new User(200000);
		User u3 = new User(300000);
		u1.addTag("sports");
		u1.addTag("tattoo");
		u1.addTag("news");
		u2.addTag("sports");
		u2.addTag("news");
		u3.addTag("sports");
		u3.addTag("tattoo");
		User[] usr = new User[] { u1, u2, u3 };
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("sports");
		tags.add("news");
		tags.add("tattoo");

		s = new SearchMatch(usr, tags);
	}

	@After
	public void tearDown() throws Exception {
		s = null;
	}

	@Test
	public void test() {
		s.findMatchLinearly();
		s.printOutput();
	}

	@Test
	public void test2() {
		System.out.print("TESTING MATCH BY HASH \n");
		 s.findMatchByHash();
		 s.printOutput();
	}

}
