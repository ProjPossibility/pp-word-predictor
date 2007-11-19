package org.ss12.wordprediction;

import org.ss12.wordprediction.model.PredictionModel;

/**
 * Accepts text and decomposes it into unigrams, bigrams, and tribrams.
 */
public class WordReader {
	private String prePreWord;
	private String preWord;
	private final PredictionModel wp;
	boolean endSentence;

	/**
	 * Creates a new word reader, and adds unigrams, bigrams, and trigrams to
	 * the given prediction model.
	 * 
	 * @param wp
	 *            prediction model to populate
	 */
	public WordReader(PredictionModel wp) {
		this.wp = wp;
		prePreWord = null;
		preWord = null;
		endSentence = false;
	}

	/**
	 * Extracts unigrams, bigrams, and trigrams from the given sequence of words
	 * and updates the prediction model.
	 * 
	 * @param words
	 *            the words
	 */
	public void nextWords(String[] words) {
		for (int i = 0; i < words.length; i++) {
			endSentence = false;
			words[i] = words[i].toLowerCase();
			if ((words[i] = wordify(words[i])).equals("")) {
				prePreWord = preWord = null;
				continue;
			}
			//System.out.print(words[i] + " ");
			// System.out.println(wp);
			wp.addUnigram(words[i]);

			if (preWord != null)
				wp.addBigram(preWord, words[i]);

			if (prePreWord != null && preWord != null)
				wp.addTrigram(prePreWord, preWord, words[i]);

			if (!endSentence) {
				prePreWord = preWord;
				preWord = words[i];
			} else {
				prePreWord = preWord = null;
				System.out.println();
			}
		}
	}

	private String wordify(String word) {
		word.trim();
		for (int i = 0; i < word.length() - 1; i++) {
			char c = word.charAt(i);
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c == '\'')
					|| (c == '-')) {
				continue;
			} else
				return "";
		}
		if (word.length() > 0) {
			char c = word.charAt(word.length() - 1);
			if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
					|| (c == '\'') || (c == '-'))) {
				word = word.substring(0, word.length() - 1);
				endSentence = true;
			}
		}
		return word;
	}
}