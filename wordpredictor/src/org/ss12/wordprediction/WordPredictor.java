package org.ss12.wordprediction;
import java.util.Arrays;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.Map.Entry;

import org.ss12.wordprediction.model.PredictionModel;

public class WordPredictor implements PredictionModel
{
	private SortedMap<String, Integer> words;
	private SortedMap<String, Integer> unigramMap;
	private SortedMap<String, Integer> bigramMap;
	private SortedMap<String, Integer> trigramMap;
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
		SortedMap<String, Integer> suggestions_candidates=words.subMap(begin_seq, getUpperBound(begin_seq));
		Entry<String,Integer>[] cnd_set= suggestions_candidates.entrySet().toArray(new Entry[]{});
		cmpSortedMap sortedMapComparator = new cmpSortedMap();
		Arrays.sort(cnd_set, 0, cnd_set.length, sortedMapComparator);
		String[] suggestions=new String[numOfSuggestions];
		numOfSuggestions = Math.min(numOfSuggestions, cnd_set.length);
		for (int rank=0; rank<numOfSuggestions;rank++)
		{
			suggestions[rank]=cnd_set[rank].getKey().toString();
		}
		return suggestions;
	}

    public String[] getSuggestionsGramBased(String[]tokens,int numOfSuggestions)
    {
    	String begin_seq,end_seq;
    	SortedMap<String, Integer> suggestions_candidates;
    	//test tokens first
        switch(tokens.length)
        {
        	case 3:
        		begin_seq=tokens[0]+" "+tokens[1];
        		end_seq=begin_seq+getUpperBound(tokens[2]);
        		suggestions_candidates=trigramMap.subMap(begin_seq, end_seq);
        		break;
        	case 2:
        		begin_seq=tokens[0];
        		end_seq=begin_seq+getUpperBound(tokens[1]);
        		suggestions_candidates=bigramMap.subMap(begin_seq, end_seq);
        		break;
        	case 1:
        		begin_seq=tokens[0];
        		end_seq=getUpperBound(tokens[0]);
        		suggestions_candidates=unigramMap.subMap(begin_seq, end_seq);
        	    break;
        	default:
        		begin_seq=null;
        	    end_seq=null;
        	    suggestions_candidates=null;
        }
        if(begin_seq==null && end_seq==null && suggestions_candidates==null)
        	return null;
        
    		
    		Entry<String,Integer>[] cnd_set= suggestions_candidates.entrySet().toArray(new Entry[]{});
    		cmpSortedMap sortedMapComparator = new cmpSortedMap();
    		for(int i=0;i<cnd_set.length;i++){
    			System.out.println(cnd_set[i].getKey());
    		}
    		Arrays.sort(cnd_set, 0, cnd_set.length, sortedMapComparator);
    		for(int i=0;i<cnd_set.length;i++){
    			System.out.println(cnd_set[i].getKey());
    		}
    		String[] suggestions=new String[numOfSuggestions];
    		numOfSuggestions = Math.min(numOfSuggestions, cnd_set.length);
    		for (int rank=0; rank<numOfSuggestions;rank++)
    		{
    			suggestions[rank]=cnd_set[rank].getKey().toString();
    		}
    		return suggestions;
    	
    }

	public void addBigram(String s1, String s2) {
		// TODO Auto-generated method stub
		
	}

	public void addTrigram(String s1, String s2, String s3) {
		// TODO Auto-generated method stub
		
	}

	public void addUnigram(String s1) {
		// TODO Auto-generated method stub
		
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