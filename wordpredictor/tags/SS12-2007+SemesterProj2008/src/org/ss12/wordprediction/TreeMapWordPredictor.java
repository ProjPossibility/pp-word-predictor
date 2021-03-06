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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.ss12.wordprediction.model.WordPredictor;
import org.ss12.wordprediction.newcore.BDBImmutableLexicon;
import org.ss12.wordprediction.newcore.CachingImmutableLexicon;
import org.ss12.wordprediction.newcore.ImmutableLexicon;
import org.ss12.wordprediction.newcore.WordFrequencyPair;
import org.ss12.wordprediction.newcore.WordPredictorUtil;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

public class TreeMapWordPredictor implements WordPredictor
{
	private static final int CACHE_SIZE = 20000;

	private Map<String, Integer> words;
	private Map<String, Integer> unigrams;
	private Map<String, Integer> bigrams;
	private Map<String, Integer> trigrams;

	private int wordCount;
	private int bigramCount;
	private int trigramCount;
	private int unigramCount;
	LinkedList<String> lastWords;
	protected WordReader reader;
	BDBImmutableLexicon uniBD;
	ImmutableLexicon uniCache;
	BDBImmutableLexicon triBD;
	BDBImmutableLexicon biBD;
	BDBImmutableLexicon dictBD;
	ImmutableLexicon dictCache;
	private boolean buffered;

	final static String dir = "./resources/dictionaries/bdb/";

	public TreeMapWordPredictor(){
		buffered=false;
		WordLoader wl = new WordLoader(1);		
		reader = new WordReader(this);
		File uni,bi,tri,dict;
		uni = new File(dir+"uni");
		uni.mkdirs();
		bi = new File(dir+"bi");
		bi.mkdirs();
		tri = new File(dir+"tri");
		tri.mkdirs();
		dict = new File(dir+"dict");
		wordCount = sumValues(words);
		bigramCount = sumValues(bigrams);
		trigramCount = sumValues(trigrams);
		unigramCount = sumValues(unigrams);
		lastWords = new LinkedList<String>();
		// environment is transactional
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setTransactional(false);
		envConfig.setAllowCreate(true);
		envConfig.setLocking(false);
		envConfig.setCachePercent(20);
		try {
			dictBD = new BDBImmutableLexicon(new Environment(dict, envConfig));
			dictCache = CachingImmutableLexicon.createCache(dictBD, CACHE_SIZE);
			uniBD = new BDBImmutableLexicon(new Environment(uni, envConfig));
			uniCache = CachingImmutableLexicon.createCache(uniBD, CACHE_SIZE);
			Environment e = new Environment(bi, envConfig);
			biBD = new BDBImmutableLexicon(e);
			triBD = new BDBImmutableLexicon(new Environment(tri, envConfig));
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		wp.tester();
	}

	public TreeMapWordPredictor(SortedMap<String, Integer> sm) {
		this(sm, new HashMap<String, Integer>(), new HashMap<String, Integer>(), new HashMap<String, Integer>());
	}

	public TreeMapWordPredictor(Map<String,Integer> sm,Map<String, Integer> uni,Map<String, Integer> bi,Map<String, Integer> tri)
	{
		buffered=true;
		this.words=sm;
		this.bigrams = bi;
		this.trigrams = tri;
		this.unigrams = uni;
		File uniF,biF,triF,dictF;
		uniF = new File(dir+"uni");
		uniF.mkdir();
		biF = new File(dir+"bi");
		biF.mkdir();
		triF = new File(dir+"tri");
		triF.mkdir();
		dictF = new File(dir+"dict");
		dictF.mkdir();
		reader = new WordReader(this);
		wordCount = sumValues(words);
		bigramCount = sumValues(bigrams);
		trigramCount = sumValues(trigrams);
		unigramCount = sumValues(unigrams);
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setCachePercent(30);
		envConfig.setTransactional(false);
		envConfig.setAllowCreate(true);
		try {
			Environment myEnv = new Environment(dictF, envConfig);
			try{
			myEnv.removeDatabase(null, "ImmutableLexicon");
			}catch(DatabaseException e){}
			dictBD = new BDBImmutableLexicon(myEnv);
//			dictCache = CachingImmutableLexicon.createCache(dictBD, CACHE_SIZE);

//			Environment myEnv = new Environment(uniF, envConfig);
//			try{
//			myEnv.removeDatabase(null, "ImmutableLexicon");
//			}catch(DatabaseException e){}
//			uniBD = new BDBImmutableLexicon(myEnv);
//			uniCache = CachingImmutableLexicon.createCache(uniBD, CACHE_SIZE);

//			Environment myEnv = new Environment(biF, envConfig);
//			try{
//			myEnv.removeDatabase(null, "ImmutableLexicon");
//			}catch(DatabaseException e){}
//			biBD = new BDBImmutableLexicon(myEnv);
//			myEnv = new Environment(triF, envConfig);
//			try{
//			myEnv.removeDatabase(null, "ImmutableLexicon");
//			}catch(DatabaseException e){}
//			triBD = new BDBImmutableLexicon(myEnv);
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int sumValues(Map<String,Integer> sm){
//		Collection<Integer> c = sm.values();
//		int sum=0;
//		for(Integer f:c){
//		sum+=f;
//		}
//		return sum;
		return 1;
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
		Entry<String, Integer>[] cnd_set=findSuggestions(begin_seq, getUpperBound(begin_seq), numOfSuggestions,(SortedMap)words);
		numOfSuggestions = Math.min(numOfSuggestions, cnd_set.length);
		for (int rank=0; rank<numOfSuggestions;rank++)
		{
			suggestions[rank]=cnd_set[rank].getKey().toString();
		}
		return suggestions;
	}

	private Entry<String, Integer>[] findSuggestions(String beginSeq, String endSeq,
			int numSuggestions, ImmutableLexicon cacheLexicon) {
		// Build a list of all WordFrequencyPair instances from the cache.
		Iterable<WordFrequencyPair> freqs = cacheLexicon.getSignificance(beginSeq, endSeq);
		LinkedList<WordFrequencyPair> freqsList = new LinkedList<WordFrequencyPair>();
		for (WordFrequencyPair freq : freqs) {
			freqsList.add(freq);
		}
		if (freqsList.size() < numSuggestions) {
			// We can't return enough words from the cache, must go to the real lexicon.
			return new Entry[0];
		}

		WordFrequencyPair[] freqsArray = freqsList.toArray(new WordFrequencyPair[0]);
		TopElements.selectSmallest(freqsArray, numSuggestions,
				Collections.reverseOrder(WordFrequencyPair.COMPARATOR));
		Entry[] entries = new Entry[numSuggestions];
		for (int i = 0; i < numSuggestions; ++i) {
			entries[i] = new FrequencyEntry(freqsArray[i].word, freqsArray[i].significance);
		}
		return entries;
	}

	// SUCH AN UGLY HACK.
	private static final class FrequencyEntry implements Entry<String, Integer> {
		private final String word;
		private final Integer frequency;

		private FrequencyEntry(String word, int frequency) {
			this.word = word;
			this.frequency = frequency;
		}

		public String getKey() {
			return word;
		}

		public Integer getValue() {
			return frequency;
		}

		public Integer setValue(Integer value) {
			throw new UnsupportedOperationException();
		}

		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			} else if (obj instanceof Entry) {
				Entry e = (Entry) obj;
				return WordPredictorUtil.equals(word, e.getKey()) &&
				WordPredictorUtil.equals(frequency, e.getValue());
			}
			return false;
		}

		public int hashCode() {
			return WordPredictorUtil.hashCode(word, frequency);
		}
	}

	/*****************************
	 * 
	 * @param begin_seq: the user input
	 * @param end_seq: the maximum range we will retrieve suggestions from
	 * @param numOfSuggestions
	 * @param map: words or unigrams or bigrams or trigrams
	 * @return: exactly "numOfSuggestions" items of suggestions in the form of Entry<String, Integer>
	 */
	private Entry<String, Integer>[] findSuggestions(String begin_seq, String end_seq, int numOfSuggestions, SortedMap<String, Integer> map)
	{
		SortedMap<String, Integer> suggestions_candidates;
		if(end_seq==null || end_seq.compareTo(begin_seq)<0){
			suggestions_candidates=map.tailMap(begin_seq);
		}
		else
			suggestions_candidates=map.subMap(begin_seq, end_seq);
		//Don't remove, apparently this is required for it work when grabbing entries from BerkeleyDB
		SortedMap<String, Integer> copy = new TreeMap<String, Integer>(
				suggestions_candidates);
		Entry<String,Integer>[] cnd_set= copy.entrySet().toArray(new Entry[]{});
		cmpSortedMap sortedMapComparator = new cmpSortedMap();
		TopElements.selectSmallest(cnd_set, 5,sortedMapComparator);
		//Arrays.sort(cnd_set, sortedMapComparator);
		return cnd_set;
	}
	public String[] getSuggestionsGramBased(String[]tokens,int numberOfSuggestionsRequested)
	{
		//String[] scoredSuggestions = getSuggestionsWithScoring(tokens, numberOfSuggestionsRequested);
		//if (scoredSuggestions.length < numberOfSuggestionsRequested) {
		String[] unscoredSuggestions = getSuggestionsWithoutScoring(tokens, numberOfSuggestionsRequested);

		//}
		return unscoredSuggestions;
	}

	private String[] getSuggestionsWithoutScoring(String[] tokens, int numberOfSuggestionsRequested) {
		if(tokens.length==0)
			return new String[0];

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
			if(trigramCount<=0)
				return new String[0];

			String tmp_begin=tokens[0]+" "+tokens[1];
			begin_seq=tmp_begin+" "+tokens[2];
			//System.out.println("the begin_seq is:"+begin_seq);
			String upper = getUpperBound(tokens[2]);
			if(upper==null)
				end_seq=tmp_begin+"a";
			else
				end_seq=tmp_begin+" "+upper;
			//System.out.println("and the end_seq is:"+end_seq);
			suggestions_candidates=findSuggestions(begin_seq, end_seq, numOfSuggestionsFound, (SortedMap)triBD.map);
			numOfSuggestionsFound = Math.min(numOfSuggestionsFound, suggestions_candidates.length);
			break;
		case 2:
			if(bigramCount<=0)
				return new String[0];

			begin_seq=tokens[0]+" "+tokens[1];
			upper = getUpperBound(tokens[1]);
			if(upper==null)
				end_seq=tokens[0]+"a";
			else
				end_seq=tokens[0]+" "+upper;
			suggestions_candidates=findSuggestions(begin_seq, end_seq, numOfSuggestionsFound, (SortedMap)biBD.map);
			numOfSuggestionsFound = Math.min(numOfSuggestionsFound, suggestions_candidates.length);
			break;
		case 1:
			if(unigramCount<=0)
				return new String[0];
			// m is used as a temporary structure to remove duplicate suggestions from two lists
			Map<String,Integer> m=new HashMap<String,Integer>();
			begin_seq=tokens[0];
			end_seq=getUpperBound(begin_seq);

			// Try getting suggestions from the unigram cache first.
			unigram_suggestions= findSuggestions(begin_seq, end_seq, numOfSuggestionsFound, uniCache);
			if (unigram_suggestions.length < numOfSuggestionsFound) {
				// Did not find enough suggestions, go to the real unigram lexicon.
				unigram_suggestions = findSuggestions(begin_seq, end_seq, numOfSuggestionsFound,(SortedMap)uniBD.map);
			}

			// Try getting suggestions from the dictionary cache first.
			dictionary_suggestions = findSuggestions(begin_seq, end_seq, numOfSuggestionsFound, dictCache);
			if (dictionary_suggestions.length < numOfSuggestionsFound) {
				// Did not find enough suggestions, go to the real dictionary lexicon.
				dictionary_suggestions = findSuggestions(begin_seq, end_seq, numOfSuggestionsFound, (SortedMap)dictBD.map);
			}

			numOfHints_unigram = Math.min(numOfSuggestionsFound, unigram_suggestions.length);
			numOfHints_dictionary = Math.min(numOfSuggestionsFound, dictionary_suggestions.length);
			//for suggestions from dictionary map and from uni-gram map
			//modify the entry value from counts to counts percentage
			//so that their frequencies are comparable
			for(int i=0;i<numOfHints_unigram;i++)
			{
				//after the modification, put that into Map
				m.put(unigram_suggestions[i].getKey(), unigram_suggestions[i].getValue());
			}
			//built list from dictionary map

//			for(int j=0; j<numOfHints_dictionary;j++)
//			{
//			//if unigram suggestions do not contain current item in dictionary suggestions
//			//insert current item as new hint
//			if(!m.containsKey(dictionary_suggestions[j].getKey()))
//			m.put(dictionary_suggestions[j].getKey(), dictionary_suggestions[j].getValue());
//			else 
//			{
//			int oldVal=m.get(dictionary_suggestions[j].getKey());
//			int newVal=dictionary_suggestions[j].getValue();
//			//unigram suggestions contain the current item, then it's a duplicate
//			//if the current item has a higher count (value), then update the 
//			//count of the item in unigram suggestions
//			if(oldVal<newVal)
//			{
//			//update
//			m.put(dictionary_suggestions[j].getKey(), newVal);
//			}  
//			else
//			{
//			//just skip it!
//			}
//			}



//			}
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
			if(str_arr.length==0)
				return new String[0];
			suggestions[rank]=str_arr[str_arr.length-1];
		}
		if(numOfSuggestionsFound < numberOfSuggestionsRequested && tokens.length>1){
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
	String path = "resources/dictionaries/user/";
	public void cleanup(){
//		System.out.println("Saving...");
//		try {
////			saveMap(unigrams,new FileOutputStream(path+"uni.dat"));
////			saveMap(bigrams,new FileOutputStream(path+"bi.dat"));
////			saveMap(trigrams,new FileOutputStream(path+"tri.dat"));
////			saveMap(words,new FileOutputStream(path+"dict.dat"));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		System.out.println("Done saving!");
		System.out.println("cleaning up");

		if(buffered){
			flush();
		}
		uniBD.close();
		biBD.close();
		triBD.close();
		dictBD.close();
		System.out.println("cleaned up");
	}
	private void saveMap(Map<String,Integer> sm,OutputStream os){
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
		StringBuilder t = new StringBuilder(s1);
		t.append(" ").append(s2).append(" ").append(s3);
		trigramCount++;
		if(buffered)
			addNgram(t.toString(),trigrams);
		else
			triBD.incrementWord(t.toString());
		//System.out.println("Trigrams: "+trigrams.size());
	}	
	public void addBigram(String s1, String s2) {
		StringBuilder t = new StringBuilder(s1);
		t.append(" ").append(s2);
		bigramCount++;
		if(buffered)
			addNgram(t.toString(),bigrams);
		else
			biBD.incrementWord(t.toString());
		//System.out.println("Bigrams: "+bigrams.size());;
	}
	public void addUnigram(String t) {
		unigramCount++;
		if(buffered)
			addNgram(t.toString(),unigrams);
		else
			uniBD.incrementWord(t.toString());
		//System.out.println("Unigrams: "+unigrams.size());;
	}	

	private void addNgram(String t, Map<String,Integer> sm) {
//		wp.incrementWord(t);
//		if(sm.containsKey(t))
//		sm.put(t, sm.get(t)+1);
//		else{
//		sm.put(t, 1);
//		}
		Integer n;
		if((n=sm.get(t))==null) n=0;
		sm.put(t, n+1);
		if(buffered && Runtime.getRuntime().freeMemory()<300000000){
			flush();
		}
	}
	int count=0;
	private void flush() {
		System.out.println("FLUSH!!!!!");
//		try {
//			saveMap(trigrams,new FileOutputStream(path+"tri"+(count++)+".dat"));
//			trigrams.clear();
//			System.gc();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		System.out.println("Saving words");
		dictBD.add(words);
		System.out.println("Saving unigrams");
		uniBD.add(unigrams);
		System.out.println("Saving bigrams");
		biBD.add(bigrams);
		System.out.println("Saving trigrams");
		triBD.add(trigrams);
		System.out.println("Free memory:"+Runtime.getRuntime().freeMemory());
	}
	public int learn(String input){
		String[] words = processString(input, 4);
		if(!words[words.length-1].equals(""))
			return 0;
		String[] smaller = Arrays.asList(words).subList(0, words.length -1).toArray(new String[0]);
		if(words.length>1)
			reader.nextWord(smaller);
		return words.length-1;
	}
	public String[] processString(String input){
		return processString(input,3);
	}
	public String[] processString(String input, int numWords) {
		input=input.toLowerCase();
		int startPoint=Math.max(input.lastIndexOf('.'), Math.max(input.lastIndexOf('?'),input.lastIndexOf('!')))+1;
		if(startPoint>=input.length())
			return new String[]{""};
		else if(startPoint>0)
			input = input.substring(startPoint);
		String temp[] = new String[numWords];
		int[] ind = new int[numWords];
		ind[0] = input.lastIndexOf(' ');
		temp[0] = input.substring(ind[0]+1);
		int i;
		for(i=1; ind[i-1]>0 && i<numWords; i++){
			ind[i] = input.lastIndexOf(' ', ind[i-1]-1);
			temp[i] = input.substring(ind[i]+1, ind[i-1]);
		}
		String word[] = new String[i];
		for(int j=0;j<i;j++){
			temp[i-j-1] = reader.wordify(temp[i-j-1]);
			word[j] = temp[i-j-1].trim();
		}
//		for(i=0;i<word.length-1;i++){
//		if(!lastWords.get(i).equals(word[i])){
//		System.out.println("Learning " + word[i] + "...");

//		lastWords.add(word[i]);
//		}
//		}
//		while(lastWords.size()>2){
//		lastWords.poll();
//		}
//		System.out.println("ProcessString: ");
//		for(String w:word)
//		System.out.println(w);
		return word;
	}
}