package org.ss12.wordprediction.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.ss12.wordprediction.TreeMapWordPredictor;
import org.ss12.wordprediction.WordReader;
import org.ss12.wordprediction.model.PredictionModel;

public class ConsolePredictionLauncher {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PredictionModel predictor = new TreeMapWordPredictor();
		WordReader wordReader = new WordReader(predictor);
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while(true){
			try {
				System.out.print("Enter a prediction query (e.g. 'experi'): ");
				String input = in.readLine();
				String[] results = predictor.getSuggestionsGramBased(predictor.processString(input), 5);
				// update unigram, bigram and trigram according to user input string
				wordReader.nextWords(predictor.processString(input));
				for(String r:results) System.out.println(r);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
