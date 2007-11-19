package org.ss12.wordprediction.model;

/**
 * Returns suggestions from a dictionary and from what the user inputs.
 */
public interface PredictionModel {
	/**
	 * Updates the frequency of the given unigram.
	 * 
	 * @param s1
	 *            the word
	 */
	public abstract void addUnigram(String s1);

	/**
	 * Updates the frequency of the given bigram.
	 * 
	 * @param s1
	 *            first word
	 * @param s2
	 *            second word
	 */
	public abstract void addBigram(String s1, String s2);

	/**
	 * Updates the frequency of the given trigram.
	 * 
	 * @param s1
	 *            the first word
	 * @param s2
	 *            the second word
	 * @param s3
	 *            the third word
	 */
	public abstract void addTrigram(String s1, String s2, String s3);

	/**
	 * Gets an array of suggestions of length numOfSuggestions based on the
	 * dictionary.
	 * 
	 * @param begin_seq
	 *            starting sequence for suggestions
	 * @param numOfSuggestions
	 *            number of suggestions to return
	 * @return the suggestions
	 */
	public abstract String[] getSuggestionsFromDic(String begin_seq,
			int numOfSuggestions);

	/**
	 * Gets an array of suggestions of length numOfSuggestions based on the
	 * user's inputs. If context is length 1, then this method returns
	 * completions of the provided string. If context is greater than 1, this
	 * method returns completions of the last string in the array, where
	 * previous elements in the array are the preceding words.
	 * 
	 * @param context
	 *            the starting sequence for suggestions, and possible preceding
	 *            words
	 * @param numOfSuggestions
	 *            number of suggestions to return
	 * @return the suggestions
	 */
	public abstract String[] getSuggestionsGramBased(String[] context,
			int numOfSuggestions);

	/**
	 * Saves the internal data to disk.
	 */
	public void cleanup();
}