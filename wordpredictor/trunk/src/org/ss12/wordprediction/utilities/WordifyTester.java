package org.ss12.wordprediction.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.ss12.wordprediction.WordReader;

public class WordifyTester {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WordReader wordReader = new WordReader(null);
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while(true){
			try {
				System.out.print("Enter a word to wordify: ");
				String input = in.readLine();
				input = wordReader.wordify(input);
				System.out.println(input);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
} 