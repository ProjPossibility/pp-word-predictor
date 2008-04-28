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
	private SortedMap<Date, String> UniEntryWindow;  
	private SortedMap<Date, String> BiEntryWindow;
	private SortedMap<Date, String> TriEntryWindow;
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
	public EntryWindow(){
		UniEntryWindow = new TreeMap<Date, String>();
		BiEntryWindow = new TreeMap<Date, String>();
		TriEntryWindow = new TreeMap<Date, String>();
		unigram = new TreeMap<String, Integer>();
		bigram = new TreeMap<String, Integer>();
		trigram = new TreeMap<String, Integer>();
		mode = 1;
		Period  = 10;
		WordNumber = 500;
	}
	public EntryWindow(int m, int p, int n){
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
	
	public void update(String t1, String t2, String t3){
		Date d = new Date();
		
		updateUniEntryWindow(t1);
		updateBiEntryWindow(t1, t2);
		updateTriEntryWindow(t1, t2, t3);
		
	}
	
	/**
	 * update UniEntryWindow according to {@link Period}/{@link WordNumber}
	 * mode 1: time frame constraint , mode 2: entry number constraint, 
	 * mode 3: time frame constraint and entry number constraint
	 * @param t: input string
	 */
	public void updateUniEntryWindow(String t){
		Date d = new Date();
		if (mode == 2){// word number frame
			//check if the map exceed maximum WordNumber
			if (UniEntryWindow.size() == WordNumber){
				decrement(unigram, UniEntryWindow.get(UniEntryWindow.firstKey()));
				//System.out.println(map.firstKey());
				UniEntryWindow.remove(UniEntryWindow.firstKey());
			}
			UniEntryWindow.put(d, t);
			increment(unigram, t);
		}
		else {// both time and word number frame
			if (mode == 3 && UniEntryWindow.size() == WordNumber){
				decrement(unigram, UniEntryWindow.get(UniEntryWindow.firstKey()));
				UniEntryWindow.remove(UniEntryWindow.firstKey());
			}
			UniEntryWindow.put(d, t);
			increment(unigram, t);
			Set<Entry<Date, String>> set = UniEntryWindow.entrySet();
			Iterator<Entry<Date, String>> it = set.iterator();
			while (it.hasNext()){
				System.out.println("entering period remove: ");
				Entry<Date, String> entry = (Entry<Date, String>)it.next();
				if (entry.getKey().getTime() <= (d.getTime()- Period*1000)){
					System.out.println(entry.getValue());
					decrement(unigram, entry.getValue());
					it.remove();
				}
			}
		}	
	}	
	
	/**
	 * update BiEntryWindow according to {@link Period}/{@link WordNumber}
	 * mode 1: time frame constraint , mode 2: entry number constraint, 
	 * mode 3: time frame constraint and entry number constraint
	 * @param t: input string
	 */
	public void updateBiEntryWindow(String s1, String s2){
		Date d = new Date();
		StringBuilder s = new StringBuilder(s1);
		s.append(" ").append(s2);
		String t = s.toString();
		
		if (mode == 2){// word number frame
			//check if the map exceed maximum WordNumber
			if (BiEntryWindow.size() == WordNumber){
				decrement(bigram, BiEntryWindow.get(BiEntryWindow.firstKey()));
				//System.out.println(map.firstKey());
				BiEntryWindow.remove(BiEntryWindow.firstKey());
			}
			BiEntryWindow.put(d, t);
			increment(bigram, t);
		}
		else {// both time and word number frame
			if (mode == 3 && BiEntryWindow.size() == WordNumber){
				decrement(bigram, BiEntryWindow.get(BiEntryWindow.firstKey()));
				BiEntryWindow.remove(BiEntryWindow.firstKey());
			}
			BiEntryWindow.put(d, t);
			increment(bigram, t);
			Set<Entry<Date, String>> set = BiEntryWindow.entrySet();
			Iterator<Entry<Date, String>> it = set.iterator();
			while (it.hasNext()){
				System.out.println("entering period remove: ");
				Entry<Date, String> entry = (Entry<Date, String>)it.next();
				if (entry.getKey().getTime() <= (d.getTime()- Period*1000)){
					System.out.println(entry.getValue());
					decrement(bigram, entry.getValue());
					it.remove();
				}
			}
		}	
	}	
	
	/**
	 * update TriEntryWindow according to {@link Period}/{@link WordNumber}
	 * mode 1: time frame constraint , mode 2: entry number constraint, 
	 * mode 3: time frame constraint and entry number constraint
	 * @param t: input string
	 */
	public void updateTriEntryWindow(String s1, String s2, String s3){
		Date d = new Date();
		StringBuilder s = new StringBuilder(s1);
		s.append(" ").append(s2).append(s3);
		String t = s.toString();
		
		if (mode == 2){// word number frame
			//check if the map exceed maximum WordNumber
			if (TriEntryWindow.size() == WordNumber){
				decrement(trigram, TriEntryWindow.get(TriEntryWindow.firstKey()));
				//System.out.println(map.firstKey());
				TriEntryWindow.remove(TriEntryWindow.firstKey());
			}
			TriEntryWindow.put(d, t);
			increment(trigram, t);
		}
		else {// both time and word number frame
			if (mode == 3 && TriEntryWindow.size() == WordNumber){
				decrement(trigram, TriEntryWindow.get(TriEntryWindow.firstKey()));
				TriEntryWindow.remove(TriEntryWindow.firstKey());
			}
			TriEntryWindow.put(d, t);
			increment(trigram, t);
			Set<Entry<Date, String>> set = TriEntryWindow.entrySet();
			Iterator<Entry<Date, String>> it = set.iterator();
			while (it.hasNext()){
				System.out.println("entering period remove: ");
				Entry<Date, String> entry = (Entry<Date, String>)it.next();
				if (entry.getKey().getTime() <= (d.getTime()- Period*1000)){
					System.out.println(entry.getValue());
					decrement(trigram, entry.getValue());
					it.remove();
				}
			}
		}	
	}	

	// should be put in grams part
	/**
	 * add new string from the gram
	 */
	private void increment(SortedMap<String,Integer> gram, String s){	
		if (gram.containsKey(s)){
			gram.put(s, gram.get(s)+1);
		}
		else gram.put(s, 1);
	}
	/**
	 * remove one string if its freq is 1; else decrement the freq by 1
	 */
	private void decrement(SortedMap<String,Integer> gram, String s){
		System.out.println("string to remove+:"+s);
		System.out.println("freq of the removed string is: "+gram.get(s));
		gram.put(s, gram.get(s)-1);
		if (gram.get(s) == 0){
			gram.remove(s);
		}	
	}
}
