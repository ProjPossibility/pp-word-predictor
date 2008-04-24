package org.ss12.wordprediction.customer;

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
	//keep track of the words within a certain time frame or number of entries constraint
	private SortedMap<Date, String> uniEntryWindow;  
	private SortedMap<Date, String> biEntryWindow;
	private SortedMap<Date, String> triEntryWindow;
	private SortedMap<String, Integer> unigram;
	private SortedMap<String, Integer> bigram;
	private SortedMap<String, Integer> trigram;
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
		biEntryWindow = new TreeMap<Date, String>();
		triEntryWindow = new TreeMap<Date, String>();
		unigram = new TreeMap<String, Integer>();
		bigram = new TreeMap<String, Integer>();
		trigram = new TreeMap<String, Integer>();
		mode = 1;
		Period  = 10;
		WordNumber = 500;
	}
	EntryWindow(int m, int p, int n){
		setPeriod(p);
		setWordNumber(n);
		setMode(mode);
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
	public int getPeriod(){
		return Period;
	}
	public int getWordNumber(){
		return WordNumber;
	}
	public int getMode(){
		return mode;
	}
	public SortedMap<String, Integer> getUnigram(){
		return unigram;
	}
	public SortedMap<String, Integer> getBigram(){
		return bigram;
	}
	public SortedMap<String, Integer> getTrigram(){
		return trigram;
	}
	
	public void update(String s1, String s2, String s3){
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
		// just for testing
		//SortedMap<Date, String> map = uniEntryWindow;
		if (mode == 2){// word number frame
			//check if the map exceed maximum WordNumber
			if (map.size() == WordNumber){
				updateDown(gram, map.get(map.firstKey()));
				//System.out.println(map.firstKey());
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
			while (it.hasNext()){
				System.out.println("entering period remove: ");
				Entry<Date, String> entry = (Entry<Date, String>)it.next();
				if (entry.getKey().getTime() <= (d.getTime()- Period*1000)){
					System.out.println(entry.getValue());
					updateDown(gram, entry.getValue());
					it.remove();
				}
			};
		}	
	}	

	// should be put in grams part
	/**
	 * add new string from the gram
	 */
	private void updateUp(SortedMap<String,Integer> gram, String s){	
		if (gram.containsKey(s)){
			gram.put(s, gram.get(s)+1);
		}
		else gram.put(s, 1);
	}
	/**
	 * remove one string if its freq is 1; else decrement the freq by 1
	 */
	private void updateDown(SortedMap<String,Integer> gram, String s){
		System.out.println("string to remove+:"+s);
		System.out.println("freq of the removed string is: "+gram.get(s));
		gram.put(s, gram.get(s)-1);
		if (gram.get(s) == 0){
			gram.remove(s);
		}	
	}
}
