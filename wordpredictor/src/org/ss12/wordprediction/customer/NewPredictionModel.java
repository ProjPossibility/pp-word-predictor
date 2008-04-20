package org.ss12.wordprediction.customer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

/**
 * prediction basing on frecency 
 * 
 * @author Lily
 */

import org.ss12.wordprediction.customer.EntryWindow;
import org.ss12.wordprediction.MapEntryValueComparator;

public class NewPredictionModel {
	private EntryWindow ew1;
	private EntryWindow ew2;
	private EntryWindow ew3;
	private static double[] Weights = {0.15, 0.35, 0.6};
	private SortedMap<String, Double> uniScoreMap;
	private SortedMap<String, Double> biScoreMap;
	private SortedMap<String, Double> triScoreMap;
	private Map<String, Double> uniScore;
	private Map<String, Double> biScore;
	private Map<String, Double> triScore;
	
	
	NewPredictionModel(){
		ew1 = new EntryWindow(1, 10, 1000);
		ew2 = new EntryWindow(1, 1*60, 1000);
		ew3 = new EntryWindow(1, 3*60, 1000);
		uniScoreMap = new TreeMap<String, Double>();
		biScoreMap = new TreeMap<String, Double>();
		triScoreMap = new TreeMap<String, Double>();
		/*
		uniScore = new TreeMap<String, Double>();
		biScore = new TreeMap<String, Double>();
		triScore = new TreeMap<String, Double>();
		*/
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
		Set<Entry<String, Integer>> set = ew3.getTrigram().entrySet();
		Iterator<Entry<String, Integer>> iter = set.iterator();
		while(iter.hasNext()){
			Entry<String, Integer> entry = (Entry<String,Integer>)iter.next();
			if (ew2.getTrigram().containsKey(entry.getKey())){
				if (ew1.getTrigram().containsKey(entry.getKey())){
					triScoreMap.put(entry.getKey(), Weights[0]*ew1.getTrigram().get(entry.getKey())+Weights[1]*ew2.getTrigram().get(entry.getKey())+Weights[2]*entry.getValue());
				}
				else 
					triScoreMap.put(entry.getKey(), Weights[1]*ew2.getTrigram().get(entry.getKey())+Weights[2]*entry.getValue());
			}
			else 
				triScoreMap.put(entry.getKey(), Weights[2]*entry.getValue());
		}
	}
	
	private void biScorer(){
		Set<Entry<String, Integer>> set = ew3.getBigram().entrySet();
		Iterator<Entry<String, Integer>> iter = set.iterator();
		while(iter.hasNext()){
			Entry<String, Integer> entry = (Entry<String, Integer>)iter.next();
			if (ew2.getBigram().containsKey(entry.getKey())){
				if (ew1.getBigram().containsKey(entry.getKey())){
					biScoreMap.put(entry.getKey(), Weights[0]*ew1.getBigram().get(entry.getKey())+Weights[1]*ew2.getBigram().get(entry.getKey())+Weights[2]*entry.getValue());
				}
				else 
					biScoreMap.put(entry.getKey(), Weights[1]*ew2.getBigram().get(entry.getKey())+Weights[2]*entry.getValue());
			}
			else 
				biScoreMap.put(entry.getKey(), Weights[2]*entry.getValue());
		}
	}
	
	private void uniScorer(){
		Set<Entry<String, Integer>> set = ew3.getUnigram().entrySet();
		Iterator<Entry<String, Integer>> iter = set.iterator();
		while(iter.hasNext()){
			Entry<String, Integer> entry = (Entry<String, Integer>)iter.next();
			if (ew2.getUnigram().containsKey(entry.getKey())){
				if (ew1.getUnigram().containsKey(entry.getKey())){
					uniScoreMap.put(entry.getKey(), Weights[0]*ew1.getUnigram().get(entry.getKey())+Weights[1]*ew2.getUnigram().get(entry.getKey())+Weights[2]*entry.getValue());
				}
				else 
					uniScoreMap.put(entry.getKey(), Weights[1]*ew2.getUnigram().get(entry.getKey())+Weights[2]*entry.getValue());
			}
			else 
				uniScoreMap.put(entry.getKey(), Weights[2]*entry.getValue());
		}
	}
	
	
	public void setWeights(double w1, double w2, double w3){
		assert(w1>0 && w1<1 && w2>0 && w2<1 && w3>0 && w3<1 && w1+w2+w3==1);
		Weights[0] = w1;
		Weights[1] = w2;
		Weights[2] = w3;
	}
	
	public SortedMap<String, Double> getuniScoreMap(){
		return uniScoreMap;
	}
	 
}
