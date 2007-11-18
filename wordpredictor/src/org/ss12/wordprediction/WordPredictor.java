package org.ss12.wordprediction;
import java.util.*;

class WordPredictor implements PredictionModel
{
	private SortedMap<String, Integer> words;
	public WordPredictor(SortedMap<String,Integer> sm)
	{
		 this.words=sm;
	}
	public String getUpperBound(String s)
	{
		return null;
	}
	/* (non-Javadoc)
	 * @see org.ss12.wordprediction.PredictionModel#getSuggestions(java.lang.String, int)
	 */
	public String[] getSuggestions(String begin_seq, int numOfSuggestions)
	{
		SortedMap<String, Integer> suggestions=words.subMap(begin_seq, getUpperBound(begin_seq));
		return suggestions.keySet().toArray(new String[]{});
	}
}
