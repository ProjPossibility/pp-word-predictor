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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

/**
 * Allows loading the word-frequency pairs and the dictionary.
 */
public class WordLoader {
	Integer min_freq;
	Set<Entry<String, Integer>> set;
	TreeMap<String, Integer> tm = new TreeMap<String, Integer>();

	public WordLoader(Integer minimumFrequency) {
		min_freq = minimumFrequency; // Initialize the frequency to zero
	}

	/**
	 * Loads the given file containing word-frequency pairs.
	 * 
	 * @param file
	 *            the file of word-frequency pairs
	 */
	public void loadFrequenciess(File file) throws IOException {
		FileReader objReader = new FileReader(file);
		BufferedReader objBuffReader = new BufferedReader(objReader);

		while (true) {
			String line = objBuffReader.readLine();
			if (line != null) {

				String[] items = line.split(" ");
				if (items.length != 2) {
					continue;
				}

				String word = "";
				Integer value;
				for (int i = 0; i < items.length; i++) {
					if (items[i].length() > 0) {
						if (word.equals("")) {
							word = items[i];
						} else {
							value = Integer.parseInt(items[i]);
							tm.put(word.toLowerCase(), value);
							break;
						}
					}
				}
			} else {
				break;
			}
			set = tm.entrySet();
		}
	}

	/**
	 * Loads the given file containing only words.
	 * 
	 * @param file
	 *            the file of words
	 * @throws IOException
	 *             if the dictionary cannot be loaded
	 */
	public void loadDictionary(File file) throws IOException {
		FileReader objReader = new FileReader(file);
		BufferedReader objBuffReader = new BufferedReader(objReader);

		while (true) {
			String word = objBuffReader.readLine();
			if (word != null) {
				tm.put(word, new Integer(min_freq));
			} else {
				break;
			}
			set = tm.entrySet();
		}
	}

	/**
	 * Deserializes the map from n-grams to their associated frequencies.
	 * 
	 * @param file the file to deserialize
	 * @return the n-gram to frequency map
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String, Integer> loadNgram(File file) {
		HashMap<String, Integer> retval = new HashMap<String, Integer>();
		try {
			FileInputStream objReader = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(objReader);
			retval = (HashMap<String, Integer>) in.readObject();
			in.close();
		} catch (IOException e) {
			System.out
					.println("File not found, creating empty Ngram entry instead");
			System.out.println(System.getProperty("user.dir"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return retval;
	}
	public static void main(String args[]) throws Exception{
		TreeMapWordPredictor wp = new TreeMapWordPredictor();
		System.out.println("Trimmed: " + wp.biBD.trim(20));
		System.out.println("Compressed: " + wp.biBD.compress());
//		System.out.println("Loading file");
//		Map<String, Integer> m = loadNgram(new File("resources/dictionaries/user/tri6.dat"));
//		System.out.println("Adding file");
//		wp.triBD.add(m);
//		m = loadNgram(new File("resources/dictionaries/user/tri2.dat"));
//		System.out.println("Adding file");
//		wp.triBD.add(m);
//		m = loadNgram(new File("resources/dictionaries/user/tri3.dat"));
//		System.out.println("Adding file");
//		wp.triBD.add(m);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Integer> me : set) {
			sb.append(me.getKey() + ": ");
			sb.append(me.getValue() + ". ");
		}
		return sb.toString();
	}

	/**
	 * Returns all loaded words as an array of word-frequency pairs.
	 * 
	 * @return an array of word-frequency pairs
	 */
	public SortedMap<String, Integer> getWords() {
		return tm;
	}
}
