package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IOSearcher implements TextSearcher {

	// This method checks whether a given File object is a text file
	public static boolean isTextFile(File file) {

		if (file.isFile()) { // This method returns true if file exists and is a regular file, note that if
								// file doesn’t exist then it returns false.

			String name = file.getName();
			int len = name.length();

			// check txt file - get the last 4 characters and compare with ".txt"
			if (len >= 5) { // minimum 5 chars "_.txt"
				String str = name.substring(len - 4, len);
				if (str.equals(".txt"))
					return true;
			}
		}
		return false;
	}

	/*
	 * Given a VALID text file, the method: 1. Checks if the given string is in the
	 * text file 2. if it is in the text file: 2.1. insert the correspondence key
	 * and value to the MAP
	 */
	public static void stringInFile(File file, String text ,Map<String, Set<String>> hm) {

		boolean inTextFile = false; // flag - Check if the text is in the txt file
		int lineNumber = 1;

		String key = file.getAbsolutePath(); // full path
		Set<String> value = new HashSet<String>(); // the set of all the lines from the file in which the string
													// appeared

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String st;
		try {
			while ((st = br.readLine()) != null) {
				// check if the line contains the text string
				if (st.contains(text)) {
					inTextFile = true;
					value.add("" + lineNumber);
				}
				lineNumber++;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if (inTextFile)
			hm.put(key, value); // Insert the key and value to the map
	}

	@Override
	public Result search(String text, String rootPath) {

		Result r = new Result() { // anonymous class that implements Result (Is A Result)

			private Map<String, Set<String>> hashMap;
			
			@Override
			public String getQuery() {
				return text;
			}

			@Override
			public Map<String, Set<String>> getAnswer() {

				File f = new File(rootPath);

				this.hashMap = new HashMap<String, Set<String>>();

				recursiveSearch(f);

				return this.hashMap;
			}

			private void recursiveSearch(File file) {

				if (IOSearcher.isTextFile(file)) { // stop condition
					IOSearcher.stringInFile(file, text, this.hashMap);
					return;
				}
				if (file.isDirectory()) { // This method returns true if file is actually a directory, if path doesn’t
											// exist then it returns false.

					File[] directoryListing = file.listFiles(); // The function returns an array of Files denoting the
												 				// files in a given abstract pathname (Directory)

					if (directoryListing != null)
						for (File child : directoryListing)
							recursiveSearch(child);
				}
				return;
				// The file does not exists
			}
		};
		return r;
	}
}
