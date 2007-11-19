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
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
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
	
	public WordPredictor(SortedMap<String, Integer> sm) {
		this(sm, new TreeMap<String, Integer>(), new TreeMap<String, Integer>(), new TreeMap<String, Integer>());
	}
	
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
		for(Integer f:c){
			sum+=f;
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
	public String[] getSuggestionsFromDic(String begin_seq, int numOfSuggestions)
	{
		String[] suggestions=new String[numOfSuggestions];
		Entry<String, Integer>[] cnd_set=foo(begin_seq, getUpperBound(begin_seq), numOfSuggestions,words);
		numOfSuggestions = Math.min(numOfSuggestions, cnd_set.length);
		for (int rank=0; rank<numOfSuggestions;rank++)
		{
			suggestions[rank]=cnd_set[rank].getKey().toString();
		}
		return suggestions;
	}
	/*****************************
	 * 
	 * @param begin_seq: the user input
	 * @param end_seq: the maximum range we will retrieve suggestions from
	 * @param numOfSuggestions
	 * @param map: words or unigrams or bigrams or trigrams
	 * @return: exactly "numOfSuggestions" items of suggestions in the form of Entry<String, Integer>
	 */
    public Entry<String, Integer>[] foo(String begin_seq, String end_seq, int numOfSuggestions, SortedMap<String,Integer> map)
    {
    	SortedMap<String, Integer> suggestions_candidates;
    	System.out.println(end_seq);
    	if(end_seq==null)
    		suggestions_candidates=map.tailMap(begin_seq);
    	else
    		suggestions_candidates=map.subMap(begin_seq, end_seq);
		Entry<String,Integer>[] cnd_set= suggestions_candidates.entrySet().toArray(new Entry[]{});
		cmpSortedMap sortedMapComparator = new cmpSortedMap();
		Arrays.sort(cnd_set, 0, cnd_set.length, sortedMapComparator);
		
		
		
		return cnd_set;
    }
    public String[] getSuggestionsGramBased(String[]tokens,int numOfSuggestions)
    {
    	System.out.println("getSuggestions started");
    	String begin_seq,end_seq;
    	String[] suggestions=new String[10];
    	//SortedMap<String, Integer> suggestions_candidates;
    	Entry<String, Integer>[] suggestions_candidates=null;
    	//these variables are designed specially for unigram case
    	Entry<String, Integer>[] unigram_suggestions, dictionary_suggestions;
    	int numOfHints_unigram, numOfHints_dictionary;
    	System.out.println("Tokens: "+tokens.length);
        switch(tokens.length)
        {
        	case 3:
        		String tmp_begin=tokens[0]+" "+tokens[1];
        		begin_seq=tokens[0]+" "+tokens[1]+" "+tokens[2];
        		System.out.println("the begin_seq is:"+begin_seq);
        		end_seq=tmp_begin+" "+getUpperBound(tokens[2]);
        		System.out.println("and the end_seq is:"+end_seq);
        		suggestions_candidates=foo(begin_seq, end_seq, numOfSuggestions, trigrams);
        		numOfSuggestions = Math.min(numOfSuggestions, suggestions_candidates.length);
        		break;
        	case 2:
        		
        		begin_seq=tokens[0]+" "+tokens[1];
        		end_seq=tokens[0]+" "+getUpperBound(tokens[1]);
        		suggestions_candidates=foo(begin_seq, end_seq, numOfSuggestions, bigrams);
        		numOfSuggestions = Math.min(numOfSuggestions, suggestions_candidates.length);
        		break;
        	case 1:
        		// m is used as a temporary structure to remove duplicate suggestions from two lists
        		Map<String,Integer> m=new HashMap<String,Integer>();
        		begin_seq=tokens[0];
        		end_seq=getUpperBound(begin_seq);
        		//built  suggestion list from unigram map
        		unigram_suggestions=foo(begin_seq, end_seq, numOfSuggestions,unigrams);
        		dictionary_suggestions = foo(begin_seq, end_seq, numOfSuggestions, words);
        		numOfHints_unigram = Math.min(numOfSuggestions, unigram_suggestions.length);
        		numOfHints_dictionary = Math.min(numOfSuggestions, dictionary_suggestions.length);
        		//for suggestions from dictionary map and from uni-gram map
        		//modify the entry value from counts to counts percentage
        		//so that their frequencies are comparable
        		for(int i=0;i<numOfHints_unigram;i++)
        		{
        			unigram_suggestions[i].setValue(unigram_suggestions[i].getValue()/unigramCount);
        			//after the modification, put that into Map
        			m.put(unigram_suggestions[i].getKey(), unigram_suggestions[i].getValue());
        			
        		}
        		//built list from dictionary map
        		
        		
        		for(int j=0; j<numOfHints_dictionary;j++)
        		{
        			//if unigram suggestions do not contain current item in dictionary suggestions
        			//insert current item as new hint
        			if(!m.containsKey(dictionary_suggestions[j].getKey()))
        				m.put(dictionary_suggestions[j].getKey(), dictionary_suggestions[j].getValue());
        			else 
        			{
        				int oldVal=m.get(dictionary_suggestions[j].getKey());
        				int newVal=dictionary_suggestions[j].getValue();
        				//unigram suggestions contain the current item, then it's a duplicate
            			//if the current item has a higher count (value), then update the 
            		    //count of the item in unigram suggestions
            				if(oldVal<newVal)
            				{
            				    //update
            					m.put(dictionary_suggestions[j].getKey(), newVal);
            				}
            				else
            				{
            					//just skip it!
            				}
        			}
        			
        			
        			
        		}
        		//convert the temporary map to our suggestions_candidates;
        		suggestions_candidates=m.entrySet().toArray(new Entry[0]);
        		//then I sort the results
        		cmpSortedMap sortedMapComparator = new cmpSortedMap();
        		//sort the merged suggestions (10 items), and we are gonna pick the top 5, 
        		Arrays.sort(suggestions_candidates, 0, suggestions_candidates.length, sortedMapComparator);
        		
        		//finally, update the numOfSuggestions before further processing
        		numOfSuggestions = Math.min(numOfSuggestions, suggestions_candidates.length);
        	    break;    
        }
        
        String[] str_arr;
        for(int rank=0;rank<numOfSuggestions;rank++)
		{
        	//System.out.println("Suggestion: "+suggestions_candidates[rank].toString());
			//suggestions[rank]=suggestions_candidates[rank].getKey().toString();
			str_arr=suggestions_candidates[rank].getKey().toString().split(" ");
			suggestions[rank]=str_arr[str_arr.length-1];
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
	
	public void addTrigram(String s1, String s2, String s3) {
		String t = s1+" "+s2+" "+s3;
		trigramCount++;
		addNgram(t,trigrams,"resources/dictionaries/user/tri.dat");
	}	
	public void addBigram(String s1, String s2) {
		String t = s1+" "+s2;
		bigramCount++;
		addNgram(t,bigrams,"resources/dictionaries/user/bi.dat");
	}	
	public void addUnigram(String t) {
		unigramCount++;
		addNgram(t,unigrams,"resources/dictionaries/user/uni.dat");
	}	

	private void addNgram(String t, SortedMap<String,Integer> sm,String filename) {
		if(sm.containsKey(t))
			sm.put(t, sm.get(t)+1);
		else{
			sm.put(t, 1);
		}
		//System.out.println("trigram: "+s1+" "+s2+" "+s3);
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