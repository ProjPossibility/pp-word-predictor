package org.ss12.wordprediction;
import java.util.Arrays;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.Map.Entry;

import org.ss12.wordprediction.model.PredictionModel;

public class WordPredictor implements PredictionModel
{
	private SortedMap<String, Integer> words;
	private SortedMap<String, Integer> unigrams;
	private SortedMap<String, Integer> bigrams;
	private SortedMap<String, Integer> trigrams;
	public WordPredictor(SortedMap<String,Integer> sm,SortedMap<String, Integer> uni,SortedMap<String,Integer> bi,SortedMap<String,Integer> tri)
	{
		 this.words=sm;
		 this.bigrams = bi;
		 this.trigrams = tri;
		 this.unigrams = uni;
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
		SortedMap<String, Integer> suggestions_candidates=words.subMap(begin_seq, getUpperBound(begin_seq));
		Entry<String,Integer>[] cnd_set= suggestions_candidates.entrySet().toArray(new Entry[]{});
		cmpSortedMap sortedMap = new cmpSortedMap();
		Arrays.sort(cnd_set, 0, cnd_set.length, sortedMap);
		String[] suggestions=new String[numOfSuggestions];
		numOfSuggestions = Math.min(numOfSuggestions, cnd_set.length);
		for (int rank=0; rank<numOfSuggestions;rank++)
		{
			suggestions[rank]=cnd_set[rank].getKey().toString();
		}
		return suggestions;
	}

	@Override
	public void addBigram(String s1, String s2) {
		String t = s1+" "+s2;
		if(bigrams.containsKey(t))
			bigrams.put(t, bigrams.get(t)+1);
		else
			bigrams.put(t, 1);
	}

	@Override
	public void addTrigram(String s1, String s2, String s3) {
		String t = s1+" "+s2+" "+s3;
		if(trigrams.containsKey(t))
			trigrams.put(t, trigrams.get(t)+1);
		else
			trigrams.put(t, 1);
	}	
	@Override
	public void addUnigram(String t) {
		if(unigrams.containsKey(t))
			unigrams.put(t, unigrams.get(t)+1);
		else
			unigrams.put(t,1);
	}
}

class cmpSortedMap implements Comparator<Entry<String,Integer>>
{
	public cmpSortedMap()
	{
		
	}
	
	public int compare(Entry<String,Integer> o1, Entry<String,Integer> o2)
	{
		int v1=o1.getValue();
		int v2=o2.getValue();
		if(v1<v2)
		  return 1;
		else if(v1>v2)
		  return -1;
		else
		  return 0;
	}
	
	public boolean equals()
	{
		return true;
	}
	
}