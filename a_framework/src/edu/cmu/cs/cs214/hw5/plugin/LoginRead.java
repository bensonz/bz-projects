package edu.cmu.cs.cs214.hw5.plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Reads the github authentication code.
 */
public class LoginRead {
	private List<String> data = new ArrayList<String>();

	/**
	 * Creates the login reader.
	 * 
	 * @param fileName
	 *            The file to read from.
	 * @throws FileNotFoundException
	 *             If the file is not found.
	 */
	public LoginRead(String fileName) throws FileNotFoundException {
		File file = new File(fileName);
		Scanner scanner = new Scanner(file);
		while (scanner.hasNextLine()) {
			data.add(scanner.nextLine());
		}
		scanner.close();
	}

	/**
	 * Gets the authentication code.
	 * 
	 * @return the string
	 */
	public String oauthCode() {
		if (data.size() != 1) {
			System.out.println("Auth code is input incorrectly");
			return "";
		}
		return data.get(0);
	}
}