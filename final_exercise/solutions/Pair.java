package test;

public class Pair {

	private Integer LRUNumber;
	private Integer NumberOfSearches;

	public Pair(int LRUNumber, int NumberOfSearches) {
		this.LRUNumber = LRUNumber;
		this.NumberOfSearches = NumberOfSearches;
	}

	public int getLRUNumber() {
		return this.LRUNumber;
	}

	public int getNumberOfSearches() {
		return this.NumberOfSearches;
	}

	public void setLRUNumber(int LRUNumber) {
		this.LRUNumber = LRUNumber;
	}

	public void setNumberOfSearches(int NumberOfSearches) {
		this.NumberOfSearches = NumberOfSearches;
	}

}
