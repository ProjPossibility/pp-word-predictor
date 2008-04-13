package org.ss12.wordprediction.customer;

import java.awt.List;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

/**
 * maintain an array with entries within time window or word number window
 * 
 * @author Lily
 */
public class EntryWindow {
	private SortedMap<Date, String> uniEntryWindow;
	private SortedMap<Date, String> biEntryWindow;
	private SortedMap<Date, String> triEntryWindow;
	private int Period;
	private int WordNumber;
	private int mode;
	
	/**
	 * constructor of EntryWindow
	 * @param mode 1: time window; 2: word number window 3: both
	 * @param WordNumber: must be larger than 500
	 * @param Period: must be larger than 10(min)
	 */
	EntryWindow(){
		uniEntryWindow = new TreeMap<Date, String>();
		uniEntryWindow = new TreeMap<Date, String>();
		uniEntryWindow = new TreeMap<Date, String>();
		mode = 1;
		Period  = 10;
		WordNumber = 1000;
	}
	
	public void setPeriod(int p){
		assert(p >= 10);
		Period = p;
	}
	public void setWordNumber(int n){
		assert(n > 500);
		WordNumber = n;
	}
	public void setMode(int m){
		assert(m>0 && m<4);
		mode = m;
	}
	
	public void update(SortedMap<String, Integer> unigram,SortedMap<String,Integer> bigram, 
			SortedMap<String, Integer> trigram, String s1, String s2, String s3){
		Date d = new Date();
		updateEntryWindow( uniEntryWindow, unigram, s1, d);
		updateEntryWindow( biEntryWindow, bigram, s2, d);
		updateEntryWindow( triEntryWindow, trigram, s3, d);
	}
	
	/**
	 * update EntryWindow according to {@link Period}/{@link WordNumber}
	 * dump old words(exceed time period) for mode 1, dump one oldest word for for 2, dump both 
	 * one oldest word and old words(exceed time period)
	 * @param map: the date-string map
	 * @param s: input string
	 * @param d: input time(date)
	 */
	private void updateEntryWindow(SortedMap<Date, String> map, 
			SortedMap<String, Integer> gram, String s, Date d){
		if (mode == 2){// word number frame
			//check if the map exceed maximum WordNumber
			if (map.size() == WordNumber){
				updateDown(gram, map.get(map.firstKey()));
				map.remove(map.firstKey());
			}
			map.put(d, s);
			updateUp(gram, s);
		}
		else {// both time and word number frame
			if (mode == 3 && map.size() == WordNumber){
				updateDown(gram, map.get(map.firstKey()));
				map.remove(map.firstKey());
			}
			map.put(d, s);
			updateUp(gram, s);
			Set<Entry<Date, String>> set = map.entrySet();
			Iterator<Entry<Date, String>> it = set.iterator();
			while (it.hasNext() && ((Entry<Date, String>)it.next()).getKey().getTime() < (d.getTime()- Period*60*1000)){
				updateDown(gram, it.next().getValue());
				it.remove();
			};
		}	
	}	

	// should be put in grams part
	/**
	 * add new string 
	 */
	private void updateUp(SortedMap<String,Integer> gram, String s){	
		if (gram.containsKey(s)){
			gram.put(s, gram.get(s)+1);
		}
		else gram.put(s, 1);
	}
	/**
	 * remove one string
	 */
	private void updateDown(SortedMap<String,Integer> gram, String s){
		gram.put(s, gram.get(s)-1);
		if (gram.get(s) == 0){
			gram.remove(s);
		}	
	}
}
