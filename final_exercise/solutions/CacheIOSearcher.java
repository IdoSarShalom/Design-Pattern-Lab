package test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CacheIOSearcher implements CacheSearcher {
	
	public static int searchCalls = 0 ;
	
	private Map<String, Result> cacheMap;
	
	private IOSearcher ios;

	public CacheIOSearcher(IOSearcher ios) {
		this.ios = ios; 
		this.cacheMap = new HashMap<String, Result>();
	}

	// Override from TextSearcher
	@Override
	public Result search(String text, String rootPath) {

		String key = text + ", " + rootPath;

		if (this.cacheMap.containsKey(key)) // Check if the query is in the cache
			return this.cacheMap.get(key); // return the Result value (precalculated)
		
		// The result is not in the map and it needs to be calculated  
		Result r = this.ios.search(text, rootPath); // Caculate the result
		searchCalls++;
		
		// Insert to the map
		this.cacheMap.put(key, r);
		
		// Return
		return r;
	}

	// Override from CacheSearcher
	@Override
	public Set<Result> getCachedResults() {
		return new HashSet<Result>(this.cacheMap.values()); // create a new HashSet with map.values()
	}

	@Override
	public void clear() {
		this.cacheMap.clear();
	}

	@Override
	public void remove(Result r) {
		while (this.cacheMap.values().remove(r));
	}

}
