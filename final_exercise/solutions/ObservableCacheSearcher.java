package test;

import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

public class ObservableCacheSearcher extends Observable implements CacheSearcher {

	// return true if the two results sets are equal, otherwise false
	public static boolean compareResultSet(Set<Result> r1, Set<Result> r2) {

		if (r1.size() != r2.size())
			return false;

		boolean flag;
		for (Result res1 : r1) {
			flag = false;

			for (Result res2 : r2) {
				if (LRUCacheSearcher.compareResults(res2, res1) == true) {
					flag = true;
					break;
				}
			}
			if (flag == false)
				return false;
		}
		return true;
	}

	// returns an element that is in Set r1 and not in the Set r2, if one doesn't
	// exists return null
	public static Result findDifferences(Set<Result> r1, Set<Result> r2) {
		boolean flag;
		for (Result res1 : r1) {
			flag = false;

			for (Result res2 : r2) {
				if (LRUCacheSearcher.compareResults(res1, res2) == true) {
					flag = true;
					break;
				}
			}
			if (flag == false)
				return res1;
		}
		return null;
	}

	private CacheSearcher cs;

	public ObservableCacheSearcher(CacheSearcher cs) {
		this.cs = cs;
	}

	@Override
	public Result search(String text, String rootPath) {

		Set<Result> resultSet1 = this.cs.getCachedResults();

		Result res = this.cs.search(text, rootPath);

		Set<Result> resultSet2 = this.cs.getCachedResults();

		if (compareResultSet(resultSet1, resultSet2)) // no change in the cache
			return res;

		if (resultSet2.size() == resultSet1.size() + 1) { // the res element has been added to the cache
			String toObservers = res.getQuery() + " added";
			setChanged(); // notify the Observers on Cache change
			notifyObservers(toObservers);
		}
		// an element has been removed from the cache and another element has been added
		// to the cache
		else {
			Result r2 = findDifferences(resultSet1, resultSet2); // the removed element
			String toObservers = r2.getQuery() + " removed;" + res.getQuery() + " added";
			setChanged();
			notifyObservers(toObservers);
		}
		return res;
	}

	@Override
	public Set<Result> getCachedResults() {
		return this.cs.getCachedResults();
	}

	@Override
	public void clear() {
		this.cs.clear(); // clear CacheSearcher cache
		String toObservers = "Cache cleared"; // notify Observers on Cache clear
		setChanged();
		notifyObservers(toObservers);
	}

	@Override
	public void remove(Result r) {
		String toObservers = r.getQuery() + " removed";
		setChanged();
		notifyObservers(toObservers);
		// remove the Result from the CacheSearcher cache
		this.cs.remove(r);
	}

}
