package org.ss12.wordprediction;

import java.util.Comparator;
import java.util.Map.Entry;

public class cmpSortedMap implements Comparator<Entry<String, ? extends Comparable>>
{
	public cmpSortedMap(){}

	public int compare(Entry<String,? extends Comparable> o1, Entry<String,? extends Comparable> o2)
	{
		Integer result = o1.getValue().compareTo(o2.getValue());
		return result>0? -1:result == 0? 0: 1;
	}
//		int v1=o1.getValue();
//		int v2=o2.getValue();
//		if(v1<v2)
//			return 1;
//		else if(v1>v2)
//			return -1;
//		else
//			return 0;

	public boolean equals()
	{
		return true;
	}
}

