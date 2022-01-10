package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Test {

	public static String path = "C:\\Users\\Ido\\eclipse-workspace\\DP_Lab1\\src\\test\\search_folder";

	// print Result object to I/O Console
	public static void printResult(Result r) {
		System.out.println("The searched text is: " + r.getQuery() + "\n");
		Map<String, Set<String>> hashMap = r.getAnswer();

		for (String key : hashMap.keySet()) {
			Set<String> value = hashMap.get(key);
			System.out.println(key);
			System.out.println(Arrays.toString(value.toArray()) + "\n");
		}

	}

	public static void testMissions1And2() { // ## Missions 1 & 2 - Tests ##
		ParallelIOSearcher pio = new ParallelIOSearcher();
		IOSearcher ios = new IOSearcher();

		// compare results of same query using ParallelIOSearcher and IOSearcher
		Result r1 = pio.search("Hello", path);
		Result r2 = ios.search("Hello", path);

		if (LRUCacheSearcher.compareResults(r1, r2) == false)
			System.out.println("ParallelIOSearcher and IOSearcher returned different Results to the same query (-40)");
		
		// search specific row in Frank Herbert-Dune.txt file
		// the searched row: -from "Manual of Muad'Dib" by the Princess Irulan, row
		// number 69

		Result r = pio.search("Paul looked up at his mother, puzzled.", path);
		
		String query = r.getQuery();
		if (query.equals("Paul looked up at his mother, puzzled.") == false)
			System.out.println("The returned query is wrong (-10)");

		Map<String, Set<String>> hmap = r.getAnswer();

		if (hmap.size() != 1)
			System.out.println("There should be only one search result (-10)");

		for (String key : hmap.keySet()) {
			Set<String> value = hmap.get(key);
			if (key.equals(
					"C:\\Users\\Ido\\eclipse-workspace\\DP_Lab1\\src\\test\\search_folder\\Frank Herbert - Dune.txt") == false)
				System.out.println("wrong key (-20)");

			if (value.toArray()[0].equals("69") == false)
				System.out.println("wrong line in the text file (-20)");

		}

		// More tests that helped me:
		// you can change the query and check manually if the printed Result on the
		// Console matches your expected answer using the auxiliary method
		// printResult(res)
		// Running the lines:
		// IOSearcher ios1 = new IOSearcher();
		// Result res = ios1.search("hey", path);
		// printResult(res);

		System.out.println("done");
	}

	public static void testMission3() { // ## Mission 3 - Test ##

		CacheIOSearcher cis = new CacheIOSearcher(new IOSearcher());
		Set<Result> resultSet1 = new HashSet<Result>();
		resultSet1.add(cis.search("popo", path));

		int num = CacheIOSearcher.searchCalls;

		for (int i = 0; i < 10; i++) // execute 10 searches same as the first search
			cis.search("popo", path);

		if (num != CacheIOSearcher.searchCalls)
			System.out.println("no cache (-40)");

		resultSet1.add(cis.search("boy", path));
		resultSet1.add(cis.search("lala", path));
		resultSet1.add(cis.search("papa", path));

		Set<Result> resultSet2 = cis.getCachedResults();

		if (!ObservableCacheSearcher.compareResultSet(resultSet1, resultSet2))
			System.out.println("the returned Cache results are incorrect (-20)");

		resultSet1.clear();
		cis.clear();

		if (cis.getCachedResults().size() != 0)
			System.out.println("make sure you clear the Cache correctly (-20)");

		Result r1 = cis.search("boy", path);
		Result r2 = cis.search("girl", path);
		Result r3 = cis.search("popo", path);

		resultSet1.add(r1);

		cis.remove(r2);
		cis.remove(r3);

		resultSet2 = cis.getCachedResults();

		if (!ObservableCacheSearcher.compareResultSet(resultSet1, resultSet2))
			System.out.println("removal from the Cache is incorrect (-20)");

		System.out.println("done");
	}

	public static void testMission4() { // ## Mission 4 - Test ##
		int threshold = 2;
		LRUCacheSearcher LRUcs = new LRUCacheSearcher(new CacheIOSearcher(new IOSearcher()), threshold);

		// test if LRUCacheSearcher implements CacheSearcher
		if (LRUcs instanceof CacheSearcher == false)
			System.out.println("LRUCacheSearcher is a Decorator of CacheSearcher (-10)");

		Result r1 = LRUcs.search("popo", path); // cache = {r1,}
		Result r2 = LRUcs.search("boy", path); // cache = {r1,r2}
		Result r3 = LRUcs.search("girl", path); // cache = {r2,r3} (r1 is evicted from the cache)
		r2 = LRUcs.search("boy", path); // cache = {r2,r3}
		Result r4 = LRUcs.search("lala", path); // cache = {r2,r4} (r3 is evicted from the cache)

		Set<Result> resultSet = LRUcs.getCachedResults();

		if (resultSet.size() > threshold)
			System.out.println("the Cache size should be less than or equal to the threshold (-10)");

		Set<Result> resultSet2 = new HashSet<Result>();
		resultSet2.add(r2);
		resultSet2.add(r4);

		if (!ObservableCacheSearcher.compareResultSet(resultSet, resultSet2))
			System.out.println("wrong implemantion of LRU algorithm (-40)");

		LRUcs.remove(r2); // cache = {r4}
		LRUcs.remove(r4); // cache = {}
		r3 = LRUcs.search("girl", path); // cache = {r3}

		resultSet = LRUcs.getCachedResults();

		resultSet2.clear();
		resultSet2.add(r3); // resultSet2 = {r3}

		if (!ObservableCacheSearcher.compareResultSet(resultSet, resultSet2))
			System.out.println("removal from the Cache is incorrect (-20)");

		LRUcs.clear();
		if (LRUcs.getCachedResults().size() != 0)
			System.out.println("make sure you clear the Cache correctly (-20)");

		System.out.println("done");
	}

	public static void testMission5() { // ## Mission 5 - Test ##
		int threshold = 3;
		LFUCacheSearcher LFUcs = new LFUCacheSearcher(new CacheIOSearcher(new IOSearcher()), threshold);

		// test if LFUCacheSearcher implements CacheSearcher
		if (LFUcs instanceof CacheSearcher == false)
			System.out.println("LFUCacheSearcher is a Decorator of CacheSearcher (-10)");

		Result r1 = LFUcs.search("popo", path); // cache = {r1}, searches: r1:1
		Result r2 = LFUcs.search("boy", path); // cache = {r1,r2}, searches: r1:1 r2:1 r3:
		Result r3 = LFUcs.search("girl", path); // cache = {r1,r2,r3}, searches: r1:1 r2:1 r3:1

		r1 = LFUcs.search("popo", path); // cache = {r1,r2,r3}, searches: r1:2 r2:1 r3:1
		r3 = LFUcs.search("girl", path); // cache = {r1,r2,r3}, searches: r1:2 r2:1 r3:2
		r3 = LFUcs.search("girl", path); // cache = {r1,r2,r3}, searches: r1:2 r2:1 r3:3

		Result r4 = LFUcs.search("lala", path); // cache = {r1,r3,r4} (r2 is evicted from the cache), searches: r1:2
												// r4:1
												// r3:3

		Set<Result> resultSet1 = LFUcs.getCachedResults();

		Set<Result> resultSet2 = new HashSet<Result>();
		resultSet2.add(r3);
		resultSet2.add(r4);
		resultSet2.add(r1);

		if (!ObservableCacheSearcher.compareResultSet(resultSet1, resultSet2))
			System.out.println("wrong implemantion of LFU algorithm (-50)");

		r4 = LFUcs.search("lala", path); // cache = {r1,r3,r4}, searches: r1:2 r4:2 r3:3
		r4 = LFUcs.search("lala", path); // cache = {r1,r3,r4}, searches: r1:2 r4:3 r3:3
		r1 = LFUcs.search("popo", path); // cache = {r1,r3,r4}, searches: r1:3 r4:3 r3:3

		// all the results have the same amount of searches, remove with LRU
		r2 = LFUcs.search("boy", path); // cache = {r1,r2,r4} (r3 is evicted from cache), searches: r1:3 r4:3 r2:1

		resultSet1 = LFUcs.getCachedResults();

		resultSet2.clear();
		resultSet2.add(r1);
		resultSet2.add(r2);
		resultSet2.add(r4);

		if (!ObservableCacheSearcher.compareResultSet(resultSet1, resultSet2))
			System.out.println("if all the results have the same amount of searches, evict from Cache with LRU (-40)");

		// remove and clear methods tests are exactly the same as in Mission4 test
		System.out.println("done");
	}

	public static void testMissions6And7() { // ## Missions 6 & 7 - Test ##
		int threshold = 3;
		ObservableCacheSearcher ocs = new ObservableCacheSearcher(
				new LFUCacheSearcher(new CacheIOSearcher(new IOSearcher()), threshold));
		Logger log = Logger.getInstance("file.txt", ocs);

		// test if ObservableCacheSearcher implements CacheSearcher
		if (ocs instanceof CacheSearcher == false)
			System.out.println("ObservableCacheSearcher is a Decorator of CacheSearcher (-20)");

		Result r1 = ocs.search("popo", path);
		Result r2 = ocs.search("boy", path);
		Result r3 = ocs.search("girl", path); // cache = {r1,r2,r3}
		Result r4 = ocs.search("lala", path); // cache = {r4,r2,r3}
		Result r5 = ocs.search("blabla", path); // cache = {r4,r5,r3}

		ocs.remove(r5);
		ocs.remove(r4); // cache = {r4,r5}

		ocs.clear(); // cache = {}

		// read the file content and compare it with the sequence of actions

		String actions = "popo added 1boy added 2girl added 3popo removed;lala added 3boy removed;blabla added 3blabla removed 2lala removed 1Cache cleared 0";

		String str = "";

		// pass the path to the file as a parameter
		File file = new File("C:\\Users\\ido\\eclipse-workspace\\DP_Lab1\\file.txt");
		Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (sc.hasNextLine())
			str += sc.nextLine();

		if (actions.equals(str) == false)
			System.out.println("wrong cache sequence of actions (-80)");

		System.out.println("done");

	}

	public static void main(String[] args) {
//		testMissions1And2();
//		testMission3();
//		testMission4();
//		testMission5();
//		testMissions6And7();
	}

}
