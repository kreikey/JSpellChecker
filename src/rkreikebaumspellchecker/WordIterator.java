package rkreikebaumspellchecker;

import java.util.regex.*;

public class WordIterator {
	private String textData;
	private Pattern wordPattern;
	private Matcher wordMatcher;
	private boolean wordFound;
	private String curWord;
	private int wordStart;
	private int wordEnd;
	private int offset;

	public WordIterator() {
		super();
		offset = 0;
		curWord = "";
		wordFound = false;
		wordStart = 0;
		wordEnd = 0;
	}
	
	public WordIterator(String document) {
		super();
		loadDocument(document);
		offset = 0;
		curWord = "";
		wordFound = false;
		wordStart = 0;
		wordEnd = 0;
	}
	
	public void loadDocument(String document) {
		textData = document;
		wordPattern = Pattern.compile("[A-Za-z']+");		// I don't like having numbers spellchecked.
		if (textData != null)
			wordMatcher = wordPattern.matcher(textData);		
	}
	
	public boolean iterateNext() {		
		if (wordMatcher.find())
		{
			wordFound = true;
			curWord = wordMatcher.group();
			// Sanitize word, and set start and end position.
			wordStart = wordMatcher.start() + offset;
			wordEnd = wordMatcher.end() + offset;
			if ((curWord.length() > 1)) {		// Elegantly handle special cases word's and 'word'.
				if ((curWord.charAt(curWord.length()-1) == 's') && (curWord.charAt(curWord.length()-2) == '\'')) {
					wordEnd -= 2;
					curWord = curWord.substring(0, curWord.length() - 2);
				}
				else if ((curWord.charAt(curWord.length()-1) == '\'') && (curWord.charAt(0) == '\'')) {
					wordStart++;
					wordEnd--;
					curWord = curWord.substring(1, curWord.length() - 1);
				}
			}
		}
		else {
			wordFound = false;
			offset = 0;
		}
		return wordFound;
	}

	public String getWord() {
		return curWord;
	}

	public int getWordStart() {
		return wordStart;
	}
	
	public int getWordEnd() {
		return wordEnd;
	}
	
	public boolean wordFound() {
		return wordFound;
	}
	
	public void adjustOffset(int size) {
		offset += size;
	}
	
	public void adjustOffsetByWdLen(int wordLength) {
		offset += (wordLength - (wordEnd - wordStart));
	}
	
	public void reset() {
		wordMatcher.reset();
		offset = 0;
		curWord = "";
		wordFound = false;
		wordStart = 0;
		wordEnd = 0;
	}
}