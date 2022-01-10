package test;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;
import java.util.Scanner;

public class ParallelIOSearcher extends IOSearcher {

	private static ConcurrentHashMap<String, Set<String>> hashMap = new ConcurrentHashMap<String, Set<String>>();

	public class ParallelResult extends RecursiveAction implements Result { // inner class 

		private static final long serialVersionUID = 1L;

		private String text;
		private File file;

		public ParallelResult(String text, String rootPath) {
			this.text = text;
			this.file = new File(rootPath);
		}

		@Override
		public String getQuery() {
			return this.text;
		}

		@Override
		public Map<String, Set<String>> getAnswer() {
			
			compute();

			// return a deep copy of the hash map

			Map<String, Set<String>> mapCopy = new HashMap<String, Set<String>>();
			
		    	for (Map.Entry<String, Set<String>> entry : hashMap.entrySet())
		    	{
		        	Set<String> hs = new HashSet<String>();
		        	hs.addAll(entry.getValue());
		    		mapCopy.put(entry.getKey(), hs);
		    	}
			
			// Map<String, Set<String>> mapCopy;
			// mapCopy = hashMap.entrySet().stream()
			//		.collect(Collectors.toMap(e -> e.getKey(), e -> Set.copyOf(e.getValue())));
			
			// Clear the hashmap for the following calculations
			hashMap.clear();
			
			return mapCopy;
		}

		@Override
		public void compute() { // a recursive task

			if (IOSearcher.isTextFile(this.file)) { // stop condition
				IOSearcher.stringInFile(this.file, this.text, hashMap);
			}

			if (file.isDirectory()) { // This method returns true if file is actually a directory, if path doesn’t
										// exist then it returns false.

				File[] directoryListing = file.listFiles(); // The function returns an array of Files denoting the
															// files in a given abstract pathname (Directory)

				ArrayList<ParallelResult> arrFork = new ArrayList<ParallelResult>();

				if (directoryListing != null) {

					for (int i = 0; i < directoryListing.length; i++) { // run through all the content of the directory
																		// except the first element

						File child = directoryListing[i];

						// ## fork join pool !! ##

						ParallelResult pr1 = new ParallelResult(this.text, child.getAbsolutePath());
						arrFork.add(pr1);
						pr1.fork();
					}

					for (ParallelResult prObj : arrFork) // make sure all threads terminate
						prObj.join(); // The join blocks until the thread completes
				}

			}
			// The file does not exists
		}

	}
	
	@Override
	public Result search(String text, String rootPath) {

		Result r = new ParallelResult(text, rootPath);
				
		return r;
	}
	
}