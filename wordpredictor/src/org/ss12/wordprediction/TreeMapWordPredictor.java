/*
    This file is part of Word Predictor.

    Word Predictor is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Word Predictor is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Word Predictor.  If not, see <http://www.gnu.org/licenses/>. 

    This software was developed by members of Project:Possibility, a software 
    collaboration for the disabled.

    For more information, visit http://projectpossibility.org
 */

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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.ss12.wordprediction.model.WordPredictor;

public class TreeMapWordPredictor implements WordPredictor
{
	private SortedMap<String, Integer> words;
	private SortedMap<String, Integer> unigrams;
	private SortedMap<String, Integer> bigrams;
	private SortedMap<String, Integer> trigrams;
	private int wordCount;
	private int bigramCount;
	private int trigramCount;
	private int unigramCount;
	LinkedList<String> lastWords;

	public TreeMapWordPredictor(){
		WordLoader wl = new WordLoader(1);		
		File uni,bi,tri,dict;
		uni = new File("resources/dictionaries/user/uni.dat");
		bi = new File("resources/dictionaries/user/bi.dat");
		tri = new File("resources/dictionaries/user/tri.dat");
		dict = new File("resources/dictionaries/user/dict.dat");
		this.words = wl.loadNgram(dict);
		this.bigrams = wl.loadNgram(bi);
		this.trigrams = wl.loadNgram(tri);
		this.unigrams = wl.loadNgram(uni);
		wordCount = sumValues(words);
		bigramCount = sumValues(bigrams);
		trigramCount = sumValues(trigrams);
		unigramCount = sumValues(unigrams);
		lastWords = new LinkedList<String>();
	}
	public TreeMapWordPredictor(SortedMap<String, Integer> sm) {
		this(sm, new TreeMap<String, Integer>(), new TreeMap<String, Integer>(), new TreeMap<String, Integer>());
	}

	public TreeMapWordPredictor(SortedMap<String,Integer> sm,SortedMap<String, Integer> uni,SortedMap<String,Integer> bi,SortedMap<String,Integer> tri)
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
		Entry<String, Integer>[] cnd_set=findSuggestions(begin_seq, getUpperBound(begin_seq), numOfSuggestions,words);
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
	private Entry<String, Integer>[] findSuggestions(String begin_seq, String end_seq, int numOfSuggestions, SortedMap<String,Integer> map)
	{
		SortedMap<String, Integer> suggestions_candidates;
		//System.out.println(end_seq);
		if(end_seq==null || end_seq.compareTo(begin_seq)<0){
			suggestions_candidates=map.tailMap(begin_seq);
		}
		else
			suggestions_candidates=map.subMap(begin_seq, end_seq);
		Entry<String,Integer>[] cnd_set= suggestions_candidates.entrySet().toArray(new Entry[]{});
		cmpSortedMap sortedMapComparator = new cmpSortedMap();
		Arrays.sort(cnd_set, 0, cnd_set.length, sortedMapComparator);

		return cnd_set;
	}
	public String[] getSuggestionsGramBased(String[]tokens,int numberOfSuggestionsRequested)
	{
		int numOfSuggestionsFound = numberOfSuggestionsRequested;
		//System.out.println("getSuggestions started");
		String begin_seq,end_seq;
		//SortedMap<String, Integer> suggestions_candidates;
		Entry<String, Integer>[] suggestions_candidates=null;
		//these variables are designed specially for unigram case
		Entry<String, Integer>[] unigram_suggestions, dictionary_suggestions;
		int numOfHints_unigram, numOfHints_dictionary;
		//System.out.println("Tokens: "+tokens.length);
		switch(tokens.length)
		{
		case 3:
			String tmp_begin=tokens[0]+" "+tokens[1];
			begin_seq=tmp_begin+" "+tokens[2];
			//System.out.println("the begin_seq is:"+begin_seq);
			String upper = getUpperBound(tokens[2]);
			if(upper==null)
				end_seq=tmp_begin+"a";
			else
				end_seq=tmp_begin+" "+upper;
			//System.out.println("and the end_seq is:"+end_seq);
			suggestions_candidates=findSuggestions(begin_seq, end_seq, numOfSuggestionsFound, trigrams);
			numOfSuggestionsFound = Math.min(numOfSuggestionsFound, suggestions_candidates.length);
			break;
		case 2:

			begin_seq=tokens[0]+" "+tokens[1];
			upper = getUpperBound(tokens[1]);
			if(upper==null)
				end_seq=tokens[0]+"a";
			else
				end_seq=tokens[0]+" "+upper;
			suggestions_candidates=findSuggestions(begin_seq, end_seq, numOfSuggestionsFound, bigrams);
			numOfSuggestionsFound = Math.min(numOfSuggestionsFound, suggestions_candidates.length);
			break;
		case 1:
			// m is used as a temporary structure to remove duplicate suggestions from two lists
			Map<String,Integer> m=new HashMap<String,Integer>();
			begin_seq=tokens[0];
			end_seq=getUpperBound(begin_seq);
			//built  suggestion list from unigram map
			unigram_suggestions=findSuggestions(begin_seq, end_seq, numOfSuggestionsFound,unigrams);
			dictionary_suggestions = findSuggestions(begin_seq, end_seq, numOfSuggestionsFound, words);
			numOfHints_unigram = Math.min(numOfSuggestionsFound, unigram_suggestions.length);
			numOfHints_dictionary = Math.min(numOfSuggestionsFound, dictionary_suggestions.length);
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
			numOfSuggestionsFound = Math.min(numOfSuggestionsFound, suggestions_candidates.length);
			break;    
		}

		String[] str_arr;
		String[] suggestions=new String[numOfSuggestionsFound];
		for(int rank=0;rank<numOfSuggestionsFound;rank++)
		{
			//System.out.println("Suggestion: "+suggestions_candidates[rank].toString());
			//suggestions[rank]=suggestions_candidates[rank].getKey().toString();
			str_arr=suggestions_candidates[rank].getKey().toString().split(" ");
			suggestions[rank]=str_arr[str_arr.length-1];
		}
		if(numOfSuggestionsFound<numberOfSuggestionsRequested && tokens.length>1){
			String[] smaller = new String[tokens.length-1];
			System.arraycopy(tokens, 1, smaller, 0, tokens.length-1);
			String[] temp = this.getSuggestionsGramBased(smaller, numberOfSuggestionsRequested);
			List<String> m=new LinkedList<String>();
			for(String s:suggestions){
				m.add(s);
			}
			for(int j=0; j<temp.length;j++)
			{
				if(!m.contains(temp[j]))
					m.add(temp[j]);
			}
			String[] results = m.toArray(new String[0]);
			String[] appendedResults = new String[Math.min(results.length, numberOfSuggestionsRequested)];

			//System.arraycopy(suggestions, 0, appendedResults, 0, numOfSuggestionsFound);
			System.arraycopy(results, 0, appendedResults, 0, Math.min(results.length, numberOfSuggestionsRequested));
			suggestions=appendedResults;
		}
		return suggestions;
	}    

	public void cleanup(){
		String path = "resources/dictionaries/user/";
		try {
			saveMap(unigrams,new FileOutputStream(path+"uni.dat"));
			saveMap(bigrams,new FileOutputStream(path+"bi.dat"));
			saveMap(trigrams,new FileOutputStream(path+"tri.dat"));
			saveMap(words,new FileOutputStream(path+"dict.dat"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	private void saveMap(SortedMap<String,Integer> sm,OutputStream os){
		ObjectOutputStream out = null;
		try
		{
			out = new ObjectOutputStream(os);
			out.writeObject(sm);
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
		addNgram(t,trigrams);
		//System.out.println("Trigrams: "+trigrams.size());;
	}	
	public void addBigram(String s1, String s2) {
		String t = s1+" "+s2;
		bigramCount++;
		addNgram(t,bigrams);
		//System.out.println("Bigrams: "+bigrams.size());;
	}	
	public void addUnigram(String t) {
		unigramCount++;
		addNgram(t,unigrams);
		//System.out.println("Unigrams: "+unigrams.size());;
	}	

	private void addNgram(String t, SortedMap<String,Integer> sm) {
		if(sm.containsKey(t))
			sm.put(t, sm.get(t)+1);
		else{
			sm.put(t, 1);
		}
		//System.out.println("trigram: "+s1+" "+s2+" "+s3);
	}
	public String[] processString(String input) {
		input=input.toLowerCase();
		String temp[] = new String[3];
		int[] ind = new int[3];
		ind[0] = input.lastIndexOf(' ');
		temp[0] = input.substring(ind[0]+1);
		int i;
		for(i=1;ind[i-1]>0 && i<3;i++){
			ind[i] = input.lastIndexOf(' ', ind[i-1]-1);
			temp[i] = input.substring(ind[i]+1,ind[i-1]);
		}
		String word[] = new String[i];
		for(int j=0;j<i;j++){
			word[j] = temp[i-j-1];
		}
//		for(i=0;i<word.length-1;i++){
//			if(!lastWords.get(i).equals(word[i])){
//				System.out.println("Learning " + word[i] + "...");
//				
//				lastWords.add(word[i]);
//			}
//		}
//		while(lastWords.size()>2){
//			lastWords.poll();
//		}
		//for(String w:word) System.out.println("'"+w+"'");
		return word;
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