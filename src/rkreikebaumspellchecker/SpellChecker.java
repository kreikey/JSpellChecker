package rkreikebaumspellchecker;

import java.io.*;
import java.util.*;

import org.apache.commons.io.*;

public class SpellChecker {
	private boolean problemFound;
	private Dictionary myDictionary;
	private WordIterator myWordIterator;

	public SpellChecker() {
		super();
		myWordIterator = new WordIterator();
		myDictionary = new Dictionary();
		problemFound = false;
	}
	
	public SpellChecker(String document) {
		super();
		myWordIterator = new WordIterator(document);
		myDictionary = new Dictionary();
		problemFound = false;
	}
	
	public String loadDictionary(File myFile) {
		String error = null;
		List<String> words = new ArrayList<String>();
		
		// Load a list from a file into an ArrayList
		// all transformations to lower case are done in this class.
		// Transforming to lower case has it's disadvantages. Proper names in the dictionary 
		// are also transformed to lower case, such that the presumably misspelled Greely 
		// cannot easily be replaced with Greeley, only greeley. It would help if we could use 
		// the compareToIgnoreCase() method, but our generic binary tree precludes this.
		// The only other option would be to keep the original case of all words as we load them
		// into the dictionary, but this would make comparisons problematic. "These" would not match "these."
		// To fix this, In our WordIterator, we would have to strategically use the toLowerCase() method
		// on the word at the beginning of the document, and any word following a period (possibly separated by whitespace)
		// This would be the "right" solution, but I do not know how to implement that at this point, and I think it would be 
		// unecessarily difficult for this assignment. Other than that, I generally like the way my program works.
		// Which bug is worse - false matches for capitalized words at the beginning of a sentence,
		// or proper nouns in the dictionary changed to lower case? I don't know.

		try {
			for (Object item : FileUtils.readLines(myFile)) {
				words.add(((String)item).toLowerCase());
			}
		} catch (IOException e) {
			e.printStackTrace();
			error = " IOException. Could Not Load File.";
		}
		
		// Load the list from the ArrayList into the dictionary
		myDictionary.loadList(words);
		return error;
	}

	public String saveDictionary(File myFile) {
		String error = null;
		try {
			FileUtils.writeLines(myFile, myDictionary.getList());
		} catch (IOException e) {
			e.printStackTrace();
			error = " IOException. Could not save file " + myFile.getName();
		}
		return error;
	}
	
	public void loadDocument(String document) {
		myWordIterator.loadDocument(document);
	}
	
	public boolean addToDictionary(String word) {
		return myDictionary.add(word.toLowerCase());
	}

	public String getPreceding() {
		return myDictionary.getPreceding();
	}

	public String getSucceeding() {
		return myDictionary.getSucceeding();
	}

	public boolean dictionaryIsEmpty() {
		return myDictionary.isEmpty();
	}
	
	public String getWord() {
		return myWordIterator.getWord();
	}

	public int getWordStart() {
		return myWordIterator.getWordStart();
	}

	public int getWordEnd() {
		return myWordIterator.getWordEnd();
	}

	// Must return false if no problem is found.
	// Currently, it does not do this. Why?
	// I *think* this boolean method is all I need to make it work.
	public boolean findProblem() {
		problemFound = false;
		while (myWordIterator.iterateNext()) {
			if (myDictionary.find(myWordIterator.getWord().toLowerCase()) == null) {	// If the word is not found in the dictionary
				problemFound = true;
				return true;
			}
		}
		return false;
	}

	public boolean problemFound() {
		return problemFound;
	}
	
	public void reset() {
		problemFound = false;
		myWordIterator.reset();
	}
	
	public boolean dictionaryHasChanged() {
		return myDictionary.hasChanged();
	}
	
	public void adjustOffsetByWdLen(int length) {
		myWordIterator.adjustOffsetByWdLen(length);
	}
}