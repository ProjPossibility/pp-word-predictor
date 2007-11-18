package org.ss12.wordprediction;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
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
	private int wordCount;
	private int bigramCount;
	private int trigramCount;
	private int unigramCount;
	public WordPredictor(SortedMap<String,Integer> sm,SortedMap<String, Integer> uni,SortedMap<String,Integer> bi,SortedMap<String,Integer> tri)
	{
		 this.words=sm;
		 this.bigrams = bi;
		 this.trigrams = tri;
		 this.unigrams = uni;
		 wordCount = sumValues(words);
		 bigramCount = sumValues(bigrams);
		 trigramCount = sumValues(trigrams);
		 unigramCount = sumValues(unigrams);
	}
	public int sumValues(SortedMap<String,Integer> sm){
		Collection<Integer> c = sm.values();
		int sum=0;
		for(Integer i:c){
			sum+=i;
		}
		return sum;
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
        		suggestions_candidates=trigrams.subMap(begin_seq, end_seq);
        		break;
        	case 2:
        		begin_seq=tokens[0];
        		end_seq=begin_seq+getUpperBound(tokens[1]);
        		suggestions_candidates=bigrams.subMap(begin_seq, end_seq);
        		break;
        	case 1:
        		begin_seq=tokens[0];
        		end_seq=getUpperBound(tokens[0]);
        		suggestions_candidates=unigrams.subMap(begin_seq, end_seq);
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
    public void cleanup(){
    	String path = "resources/dictionaries/user/";
		try {
			saveMap(unigrams,new FileOutputStream(path+"uni.dat"));
			saveMap(bigrams,new FileOutputStream(path+"bi.dat"));
			saveMap(trigrams,new FileOutputStream(path+"tri.dat"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }
	private void saveMap(SortedMap<String,Integer> sm,OutputStream os){
		ObjectOutputStream out = null;
		try
		{
			out = new ObjectOutputStream(os);
			out.writeObject(unigrams);
			out.writeObject(bigrams);
			out.writeObject(trigrams);
			out.close();
		}
		catch(IOException ex)
		{
		    ex.printStackTrace();
		}
	}
	@Override
	public void addTrigram(String s1, String s2, String s3) {
		String t = s1+" "+s2+" "+s3;
		addNgram(t,trigrams,"resources/dictionaries/user/tri.dat");
	}	
	public void addBigram(String s1, String s2) {
		String t = s1+" "+s2;
		addNgram(t,bigrams,"resources/dictionaries/user/bi.dat");
	}	
	public void addUnigram(String t) {
		addNgram(t,unigrams,"resources/dictionaries/user/uni.dat");
	}	

	private void addNgram(String t, SortedMap<String,Integer> sm,String filename) {
		if(sm.containsKey(t))
			sm.put(t, sm.get(t)+1);
		else
			sm.put(t,1);
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