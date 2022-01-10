package test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LRUCacheSearcher implements CacheSearcher {

	// return true if the two results are equal, otherwise false
	public static boolean compareResults(Result r1, Result r2) {
		if (!r1.getQuery().equals(r2.getQuery()))
			return false;

		Map<String, Set<String>> m1 = r1.getAnswer();
		Map<String, Set<String>> m2 = r2.getAnswer();

		if (!m1.equals(m2))
			return false;

		return true;
	}

	private CacheSearcher cs;
	private int threshold;

	private Map<String, Integer> LRUmap; // key = text+rootpath, value = LRU number

	public LRUCacheSearcher(CacheSearcher cs, int threshold) {
		this.cs = cs;
		this.threshold = threshold; // maximum cache size
		this.LRUmap = new HashMap<String, Integer>();
	}

	public LRUCacheSearcher(CacheSearcher cs, int threshold, Map<String, Integer> map) {
		this.cs = cs;
		this.threshold = threshold;
		this.LRUmap = map;
	}

	@Override
	public Result search(String text, String rootPath) {

		String key = text + ", " + rootPath;

		if (LRUmap.size() == this.threshold && LRUmap.containsKey(key) == false) {

			// remove the least recently used element
			// remove from the cache the key with max RLU number value
			Map.Entry<String, Integer> maxEntry = null;

			// iterate over the map's entry set
			for (Map.Entry<String, Integer> entry : LRUmap.entrySet())
				if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
					maxEntry = entry;

			// remove from the cache the maxEntry
			LRUmap.remove(maxEntry.getKey());

			// remove from the CacheSearcher cache the least recently used result
			String[] strVar = maxEntry.getKey().split(", ", 0);
			cs.remove(cs.search(strVar[0], strVar[1]));
		}

		// update the LRU value of each key-value pair in the map

		LRUmap.replaceAll((k, v) -> v + 1); // incement all the LRU-values in a the map

		LRUmap.put(key, 0); // the current key has the minimal value i.e. its the most recently used

		return this.cs.search(text, rootPath);
	}

	@Override
	public Set<Result> getCachedResults() {
		return this.cs.getCachedResults();
	}

	@Override
	public void clear() {
		this.LRUmap.clear(); // clear LRU map cache
		this.cs.clear(); // clear CacheSearcher cache
	}

	@Override
	public void remove(Result r) {
		// remove the key with corresponding Result r from the LRU map cache
		String[] strVar;
		Result res;
		Set<String> valuesToRemove = new HashSet<>();

		for (String str : this.LRUmap.keySet()) {
			strVar = str.split(", ", 0);
			res = this.cs.search(strVar[0], strVar[1]);
			if (compareResults(r, res))
				valuesToRemove.add(str); // avoid java.util.ConcurrentModificationException
		}

		this.LRUmap.keySet().removeAll(valuesToRemove);

		// remove the Result from the CacheSearcher cache
		this.cs.remove(r);
	}

}