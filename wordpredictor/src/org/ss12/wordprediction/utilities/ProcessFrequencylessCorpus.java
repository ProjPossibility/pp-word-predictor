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
