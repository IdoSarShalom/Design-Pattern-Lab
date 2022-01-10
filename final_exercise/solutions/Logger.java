package test;

import java.util.Observable;
import java.util.Observer;
import java.io.File; // Import the File class
import java.io.FileWriter;
import java.io.IOException; // Import the IOException class to handle errors
import java.io.PrintWriter;

public class Logger implements Observer {

	private static Logger instance = null;
	private String fileName;
	private File myFileObj;
	private int cacheSize;

	private Logger(String fileName, ObservableCacheSearcher ocs) {
		ocs.addObserver(this); // the Logger is an Observer of ocs
		this.fileName = fileName;
		try {
			this.myFileObj = new File(this.fileName);
			this.myFileObj.createNewFile();
			// delete the content of text file without deleting itself
			PrintWriter writer = new PrintWriter(this.myFileObj);
			writer.print("");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.cacheSize = 0;
	}

	synchronized public static Logger getInstance(String fileName, ObservableCacheSearcher ocs) {
		if (instance == null) {
			// if instance is null, initialize
			instance = new Logger(fileName, ocs);
		}
		return instance;
	}

	@Override
	public void update(Observable o, Object arg) {
		String msg = (String) arg;

		if (msg.contains("added"))
			this.cacheSize++;

		if (msg.contains("removed"))
			this.cacheSize--;

		if (msg.contains("cleared"))
			this.cacheSize = 0;

		msg = msg + " " + cacheSize + "\n";

		try {
			PrintWriter out = new PrintWriter(new FileWriter(this.myFileObj, true));
			out.append(msg);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
