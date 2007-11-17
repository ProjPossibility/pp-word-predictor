package org.ss12.wordprediction;
/**
 * 
 */

/**
 * A pairing of a word and a frequency.
 */
public class WordFrequencyPair {
	private final String word;
	private final float frequency;
	
	public WordFrequencyPair(String word, float frequency) {
		this.word = word;
		this.frequency = frequency;
	}
	
	public String getWord() {
		return word;
	}
	
	public float getFrequency() {
		return frequency;
	}
}
