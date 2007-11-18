package org.ss12.wordprediction;
import java.util.*;

class WordPredictor implements PredictionModel
{
	private SortedMap<String, Integer> words;
	public WordPredictor(SortedMap<String,Integer> sm)
	{
		 this.words=sm;
	}
	
	/**
	 * Gets the upper bound of a string s
	 * 
	 * @param s the string to get the upper bound of
	 */
	public String getUpperBound(String s)
	{
		String upperBound = null;
		char[] ch = s.toCharArray();
		int len = ch.length;
		StringBuilder sb = new StringBuilder();
		
		for(int i = len - 1; i >= 0; i--)
		{
			if(ch[i] != 'z')
			{
				ch[i]++;
				
				for(int j = 0; j <= i; j++)
				{
					sb.append(ch[j]);
				}
				
				upperBound = sb.toString();
				break;
			}
		}
		
		return upperBound;
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
