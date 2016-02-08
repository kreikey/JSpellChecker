package rkreikebaumspellchecker;

import java.util.*;

public class Dictionary {
	private BinTree<String> wordStore;
	private int loadCount;
	private boolean somethingAdded;
	private boolean changedWithLastLoad;

	public Dictionary() {
		super();
		wordStore = new BinTree<String>();
		loadCount = 0;
		somethingAdded = false;
		changedWithLastLoad = false;
	}
	
	public Dictionary(List<String> words) {
		changedWithLastLoad = loadList(words);
		loadCount = 1;
		somethingAdded = false;
	}
	
	// Load the list from the List into the back end storage
	public boolean loadList(List<String> words) {
		boolean changed = false;
		if (wordStore.loadList(words)) {
			changed = true;
			changedWithLastLoad = true;
		}
		loadCount++;
		return changed;
	}
	
	// Return a list of words from the back end storage
	public List<String> getList() {
		return wordStore.saveInOrder();
	}

	public boolean add(String word) {
		boolean added = false;
		added = wordStore.insert(word);
		if (added) {
			somethingAdded = true;
		}
		return added;
	}

	public String find(String word) {
		return wordStore.find(word);
	}

	public String getPreceding() {
		String word;
		word = wordStore.getPreceding();
		if (word == null)
			word = "";
		return word;
	}

	public String getSucceeding() {
		String word;
		word = wordStore.getSucceeding();
		if (word == null)
			word = "";
		return word;
	}
	
	public boolean isEmpty() {
		return wordStore.isEmpty();
	}
	
	public boolean hasChanged() {
		boolean hasChanged = false;
		if ((loadCount > 1 && changedWithLastLoad) || somethingAdded) {
			hasChanged = true;
		}
		return hasChanged;
	}
}