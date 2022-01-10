package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LFUCacheSearcher implements CacheSearcher {

	private CacheSearcher cs;
	private int threshold;

	private Map<String, ArrayList<Integer>> LFUmap;
	/*
	 * key = A concatenated String of "text, rootpath", value = An Arraylist
	 * containing two elements: First element is the LRU number, Second element is
	 * the number of searches
	 */

	public LFUCacheSearcher(CacheSearcher cs, int threshold) {
		this.cs = cs;
		this.threshold = threshold;
		this.LFUmap = new HashMap<String, ArrayList<Integer>>();
	}

	@Override
	public Result search(String text, String rootPath) {
		String key = text + ", " + rootPath;

		if (this.LFUmap.containsKey(key)) {

			ArrayList<Integer> list = this.LFUmap.get(key);
			list.set(1, list.get(1) + 1); // add one to the number of searches

			// iterate over the map's values set
			for (ArrayList<Integer> listVal : this.LFUmap.values())
				listVal.set(0, listVal.get(0) + 1); // add one to the LRU number

			list.set(0, 0); // current key LRU number is zero i.e. its the most recently used
		}

		else if (LFUmap.size() < this.threshold) { // map does not contain the key and the cache is not full

			// iterate over the map's values set
			for (ArrayList<Integer> listVal : this.LFUmap.values())
				listVal.set(0, listVal.get(0) + 1); // add one to the LRU number

			ArrayList<Integer> list = new ArrayList<Integer>();
			list.add(0); // 0 - the key is most recently used
			list.add(1); // 1- number of searches

			LFUmap.put(key, list);

		}

		else { // map does not contain the key and the cache is full

			Set<Integer> keySearches = new HashSet<Integer>();

			// iterate over the map's values set
			for (ArrayList<Integer> l : this.LFUmap.values())
				keySearches.add(l.get(1)); // add the number of searches of the key

			boolean isSearchEqual = keySearches.size() == 1;

			if (isSearchEqual) {// all the results have the same amount of searches

				// remove from the cache the least recently used key
				Map.Entry<String, ArrayList<Integer>> maxEntry = null;
				// iterate over the map's entry set
				for (Map.Entry<String, ArrayList<Integer>> entry : this.LFUmap.entrySet())
					if (maxEntry == null || entry.getValue().get(0) > maxEntry.getValue().get(0))
						maxEntry = entry;

				this.LFUmap.remove(maxEntry.getKey());

				// remove from the CacheSearcher cache
				String[] strVar = maxEntry.getKey().split(", ", 0);
				cs.remove(cs.search(strVar[0], strVar[1]));

				// add one to the LRU number of all the keys in the map
				for (ArrayList<Integer> listVal : this.LFUmap.values())
					listVal.set(0, listVal.get(0) + 1);

				// put in the new key in the map
				ArrayList<Integer> list = new ArrayList<Integer>();
				list.add(0); // 0 - most recently used
				list.add(1); // 1 - number of searches

				LFUmap.put(key, list); // the key has been searched only once

			} else { // there exists results with different amount of searches

				// remove from the cache the key with minimum search number value
				Map.Entry<String, ArrayList<Integer>> minEntry = null;
				// iterate over the map's entry set
				for (Map.Entry<String, ArrayList<Integer>> entry : this.LFUmap.entrySet())
					if (minEntry == null || entry.getValue().get(1) < minEntry.getValue().get(1))
						minEntry = entry;

				this.LFUmap.remove(minEntry.getKey());

				// remove from the CacheSearcher cache
				String[] strVar = minEntry.getKey().split(", ", 0);
				cs.remove(cs.search(strVar[0], strVar[1]));

				// add one to the LRU number of all the keys in the map
				for (ArrayList<Integer> listVal : this.LFUmap.values())
					listVal.set(0, listVal.get(0) + 1);

				// put in the new key in the map
				ArrayList<Integer> list = new ArrayList<Integer>();
				list.add(0); // 0 - most recently used
				list.add(1); // 1 - number of searches

				LFUmap.put(key, list); // the key has been searched only once
			}
		}
		return this.cs.search(text, rootPath);
	}

	@Override
	public Set<Result> getCachedResults() {
		return this.cs.getCachedResults();
	}

	@Override
	public void clear() {
		this.LFUmap.clear(); // clear LFU map cache
		this.cs.clear(); // clear CacheSearcher cache
	}

	@Override
	public void remove(Result r) {
		// remove the key with corresponding Result r from the LFU map cache
		String[] strVar;
		Result res;
		Set<String> valuesToRemove = new HashSet<>();

		for (String str : this.LFUmap.keySet()) {
			strVar = str.split(", ", 0);
			res = this.cs.search(strVar[0], strVar[1]);
			if (LRUCacheSearcher.compareResults(r, res))
				valuesToRemove.add(str); // avoid java.util.ConcurrentModificationException
		}
		this.LFUmap.keySet().removeAll(valuesToRemove);

		// remove the Result from the CacheSearcher cache
		this.cs.remove(r);
	}

}
