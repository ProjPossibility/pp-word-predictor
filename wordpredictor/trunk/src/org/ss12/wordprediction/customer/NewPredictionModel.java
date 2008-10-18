package org.ss12.wordprediction.customer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.ss12.wordprediction.MapEntryValueComparator;
import org.ss12.wordprediction.TreeMapWordPredictor;
import org.ss12.wordprediction.cmpSortedMap;

public class NewPredictionModel extends TreeMapWordPredictor {
	private EntryWindow entryWin1;
	private EntryWindow entryWin2;
	private EntryWindow entryWin3;
	private static double[] Weights = {0.15, 0.35, 0.6};
	private SortedMap<String, Double> uniScoreMap;
	private SortedMap<String, Double> biScoreMap;
	private SortedMap<String, Double> triScoreMap;
	
	NewPredictionModel(){
		entryWin1 = new EntryWindow(1, 10, 1000);
		entryWin2 = new EntryWindow(1, 1*60, 1000);
		entryWin3 = new EntryWindow(1, 3*60, 1000);
		uniScoreMap = new TreeMap<String, Double>();
		biScoreMap = new TreeMap<String, Double>();
		triScoreMap = new TreeMap<String, Double>();
	}
	
	public SortedMap<String, Double> getUniScoreMap(){
		return uniScoreMap;
	}
	public SortedMap<String, Double> getBiScoreMap(){
		return biScoreMap;
	}
	public SortedMap<String, Double> getTriScoreMap(){
		return triScoreMap;
	}
	
	public int learn(String input){
		String[] words = processString(input, 4);
		if(!words[words.length-1].equals(""))
			return 0;
		String[] smaller = Arrays.copyOfRange(words, 0, words.length-1);
		if(words.length>1)
			reader.nextWord(smaller);
		return words.length-1; 
	}

	public void addUnigram(String t){
		entryWin1.updateUniEntryWindow(t);
	}
	public void addBigram(String s1, String s2){
		entryWin2.updateBiEntryWindow(s1, s2);
	}
	public void addTrigram(String s1, String s2, String s3){
		entryWin3.updateTriEntryWindow(s1, s2, s3);
	}
	
	public List<Entry<String, Double>> sortList(Map<String, Double> map){
		Collection<Entry<String, Double>> c = map.entrySet();

		// Convert to list
	    List<Entry<String,Double>> list = new ArrayList(c);
	    //sort the list
		Collections.sort(list, new MapEntryValueComparator());
		//System.out.println(list);
		if (list.size() < 5)
			return list;
		else 
			return list.subList(0,5);
	}
	
	private void triScorer(){
		Set<Entry<String, Integer>> set = entryWin3.getTrigram().entrySet();
		Iterator<Entry<String, Integer>> iter = set.iterator();
		while(iter.hasNext()){
			Entry<String, Integer> entry = (Entry<String,Integer>)iter.next();
			if (entryWin2.getTrigram().containsKey(entry.getKey())){
				if (entryWin1.getTrigram().containsKey(entry.getKey())){
					triScoreMap.put(entry.getKey(), Weights[0]*entryWin1.getTrigram().get(entry.getKey())+Weights[1]*entryWin2.getTrigram().get(entry.getKey())+Weights[2]*entry.getValue());
				}
				else 
					triScoreMap.put(entry.getKey(), Weights[1]*entryWin2.getTrigram().get(entry.getKey())+Weights[2]*entry.getValue());
			}
			else 
				triScoreMap.put(entry.getKey(), Weights[2]*entry.getValue());
		}
	}
	
	private void biScorer(){
		Set<Entry<String, Integer>> set = entryWin3.getBigram().entrySet();
		Iterator<Entry<String, Integer>> iter = set.iterator();
		while(iter.hasNext()){
			Entry<String, Integer> entry = (Entry<String, Integer>)iter.next();
			if (entryWin2.getBigram().containsKey(entry.getKey())){
				if (entryWin1.getBigram().containsKey(entry.getKey())){
					biScoreMap.put(entry.getKey(), Weights[0]*entryWin1.getBigram().get(entry.getKey())+Weights[1]*entryWin2.getBigram().get(entry.getKey())+Weights[2]*entry.getValue());
				}
				else 
					biScoreMap.put(entry.getKey(), Weights[1]*entryWin2.getBigram().get(entry.getKey())+Weights[2]*entry.getValue());
			}
			else 
				biScoreMap.put(entry.getKey(), Weights[2]*entry.getValue());
		}
	}
	
	private void uniScorer(){
		Set<Entry<String, Integer>> set = entryWin3.getUnigram().entrySet();
		Iterator<Entry<String, Integer>> iter = set.iterator();
		while(iter.hasNext()){
			Entry<String, Integer> entry = (Entry<String, Integer>)iter.next();
			if (entryWin2.getUnigram().containsKey(entry.getKey())){
				if (entryWin1.getUnigram().containsKey(entry.getKey())){
					uniScoreMap.put(entry.getKey(), Weights[0]*entryWin1.getUnigram().get(entry.getKey())+Weights[1]*entryWin2.getUnigram().get(entry.getKey())+Weights[2]*entry.getValue());
				}
				else 
					uniScoreMap.put(entry.getKey(), Weights[1]*entryWin2.getUnigram().get(entry.getKey())+Weights[2]*entry.getValue());
			}
			else 
				uniScoreMap.put(entry.getKey(), Weights[2]*entry.getValue());
		}
	}
	
	private String[] getSuggestionsWithScoring(String[] tokens, int numberOfSuggestionsRequested) {
		if(tokens.length==0)
			return new String[0];
		
		int numOfSuggestionsFound = numberOfSuggestionsRequested;
		//System.out.println("getSuggestions started");
		String begin_seq,end_seq;
		//SortedMap<String, Integer> suggestions_candidates;
		Entry<String, Double>[] suggestions_candidates=null;
		//these variables are designed specially for unigram case
		Entry<String, Double>[] unigram_suggestions;
		int numOfHints_unigram;
		//System.out.println("Tokens: "+tokens.length);
		switch(tokens.length)
		{
		case 3:
			if(triScoreMap.size()<=0)
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
			suggestions_candidates=findSuggestions(begin_seq, end_seq, numOfSuggestionsFound, (SortedMap)triScoreMap);
			numOfSuggestionsFound = Math.min(numOfSuggestionsFound, suggestions_candidates.length);
			break;
		case 2:
			if(biScoreMap.size()<=0)
				return new String[0];

			begin_seq=tokens[0]+" "+tokens[1];
			upper = getUpperBound(tokens[1]);
			if(upper==null)
				end_seq=tokens[0]+"a";
			else
				end_seq=tokens[0]+" "+upper;
			suggestions_candidates=findSuggestions(begin_seq, end_seq, numOfSuggestionsFound, (SortedMap)biScoreMap);
			numOfSuggestionsFound = Math.min(numOfSuggestionsFound, suggestions_candidates.length);
			break;
		case 1:
			if(uniScoreMap.size()<=0)
				return new String[0];
			// m is used as a temporary structure to remove duplicate suggestions from two lists
			Map<String,Double> m=new HashMap<String,Double>();
			begin_seq=tokens[0];
			end_seq=getUpperBound(begin_seq);
			//built  suggestion list from unigram map
			unigram_suggestions=findSuggestions(begin_seq, end_seq, numOfSuggestionsFound,(SortedMap)uniScoreMap);
			//dictionary_suggestions = findSuggestions(begin_seq, end_seq, numOfSuggestionsFound, (SortedMap)words);
			numOfHints_unigram = Math.min(numOfSuggestionsFound, unigram_suggestions.length);
			//numOfHints_dictionary = Math.min(numOfSuggestionsFound, dictionary_suggestions.length);
			//for suggestions from dictionary map and from uni-gram map
			//modify the entry value from counts to counts percentage
			//so that their frequencies are comparable
			for(int i=0;i < numOfHints_unigram;i++)
			{
				unigram_suggestions[i].setValue(unigram_suggestions[i].getValue()/uniScoreMap.size());
				//after the modification, put that into Map
				m.put(unigram_suggestions[i].getKey(), unigram_suggestions[i].getValue());
			}
			//built list from dictionary map

//
//			for(int j=0; j<numOfHints_dictionary;j++)
//			{
//				//if unigram suggestions do not contain current item in dictionary suggestions
//				//insert current item as new hint
//				if(!m.containsKey(dictionary_suggestions[j].getKey()))
//					m.put(dictionary_suggestions[j].getKey(), dictionary_suggestions[j].getValue());
//				else 
//				{
//					int oldVal=m.get(dictionary_suggestions[j].getKey());
//					int newVal=dictionary_suggestions[j].getValue();
//					//unigram suggestions contain the current item, then it's a duplicate
//					//if the current item has a higher count (value), then update the 
//					//count of the item in unigram suggestions
//					if(oldVal<newVal)
//					{
//						//update
//						m.put(dictionary_suggestions[j].getKey(), newVal);
//					}  
//					else
//					{
//						//just skip it!
//					}
//				}
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
		for(int rank=0; rank < numOfSuggestionsFound;rank++)
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
			for(String s : suggestions){
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
	

	/*****************************
	 * 
	 * @param begin_seq: the user input
	 * @param end_seq: the maximum range we will retrieve suggestions from
	 * @param numOfSuggestions
	 * @param map: words or unigram or bigram or trigram
	 * @return: exactly "numOfSuggestions" items of suggestions in the form of Entry<String, Integer>
	 */
	private Entry<String, Double>[] findSuggestions(String begin_seq, String end_seq, int numOfSuggestions, SortedMap<String, Double> map)
	{
		SortedMap<String, Double> suggestions_candidates;
		//System.out.println(end_seq);
		if(end_seq==null || end_seq.compareTo(begin_seq)<0){
			suggestions_candidates=map.tailMap(begin_seq);
		}
		else
			suggestions_candidates=map.subMap(begin_seq, end_seq);
		Entry<String,Double>[] cnd_set= suggestions_candidates.entrySet().toArray(new Entry[]{});
		cmpSortedMap sortedMapComparator = new cmpSortedMap();
		Arrays.sort(cnd_set, 0, cnd_set.length, sortedMapComparator);

		return cnd_set;
	}
	
	public String[] getSuggestionsEntryWindowBased(String[]tokens,int numberOfSuggestionsRequested)
	{
		//String[] scoredSuggestions = getSuggestionsWithScoring(tokens, numberOfSuggestionsRequested);
		//if (scoredSuggestions.length < numberOfSuggestionsRequested) {
			String[] unscoredSuggestions = getSuggestionsWithScoring(tokens, numberOfSuggestionsRequested);
		//}
		return unscoredSuggestions;
	}
	public void setWeights(double w1, double w2, double w3){
		assert(w1>0 && w1<1 && w2>0 && w2<1 && w3>0 && w3<1 && w1+w2+w3==1);
		Weights[0] = w1;
		Weights[1] = w2;
		Weights[2] = w3;
	}
	
	
}
