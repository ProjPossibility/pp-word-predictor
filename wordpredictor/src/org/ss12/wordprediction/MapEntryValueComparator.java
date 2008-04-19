/**
 * 
 */
package org.ss12.wordprediction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

/**
 * @author Lily
 *
 */
public class MapEntryValueComparator implements Comparator<Entry<?, ? extends Comparable>> {
	public int compare(Entry<?,? extends Comparable> o1, Entry<?,? extends Comparable> o2)
	{
		Integer result = o1.getValue().compareTo(o2.getValue());
		return result>0? -1:result == 0? 0: 1;
	}
	/*
	public boolean equals(Entry<?, ? extends Comparable> o1, Entry<?, ? extends Comparable> o2){
		return o1.getValue().equals(o2.getValue());
	}
	*/
	public static void main(String[] args) {
		Map<String, Double> x = new TreeMap<String, Double>();
		x.put("a", 1.0);
		x.put("b", 2.0);
		x.put("c", 3.0);
		Set<Entry<String, Double>> entries = x.entrySet();
		List<Entry<String, Double>> y = new ArrayList<Entry<String, Double>>();
		y.addAll(entries);
		Collections.sort(y, new MapEntryValueComparator());
		System.out.println(y);
	}
}
