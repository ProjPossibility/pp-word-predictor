package org.ss12.wordprediction.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

/*
 * Convert files of format 
 * 1.	The	69970	6.8872
 * (Rank) (Word) (Occurrences) (Frequency)
 * to...
 * (Word) (Occurrences)
 * Sorted lexicographically
 */
public class ProcessFrequencylessCorpus {
	public static void main(String args[]){
		try {
			TreeMap<String,Integer> tm = new TreeMap<String,Integer>();
			File d = new File("resources/dictionaries/plain");
			String[] files = d.list();
			for(int j=0;j<files.length;j++){
				if(files[j].charAt(0)=='.')
					continue;
				System.out.println(files[j]);
				BufferedReader in = new BufferedReader(new FileReader("resources/dictionaries/plain/"+files[j]));
				in.readLine();
				while(in.ready()){
					String s = in.readLine();
					String[] items = s.split("/");
					String word=items[0];
					tm.put(word.toLowerCase(), 0);
				}
			}
			Object[] keys = tm.keySet().toArray();
			FileWriter w = new FileWriter("resources/dictionaries/converted/plain.dat");
			for(int i=0;i<keys.length;i++){
				String t = ""+keys[i];
				w.write(t+"\n");
//				System.out.println(t);
			}
			w.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
