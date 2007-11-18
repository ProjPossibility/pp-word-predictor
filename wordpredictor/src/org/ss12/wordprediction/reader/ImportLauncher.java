package org.ss12.wordprediction.reader;

import java.io.File;
import java.io.IOException;

import org.ss12.wordprediction.WordLoader;
import org.ss12.wordprediction.WordPredictor;
import org.ss12.wordprediction.WordReader;
import org.ss12.wordprediction.model.PredictionModel;

public class ImportLauncher {
	public static void main(String args[]){
		WordLoader wl = new WordLoader(1);
		try {
			wl.loadDictionary(new File("resources/dictionaries/converted/plain.dat"));
			wl.loadFrequenciess(new File("resources/dictionaries/converted/freq.dat"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PredictionModel pm = new WordPredictor(wl.getWords());
		WordReader wr = new WordReader(pm);
		FileImporter fi = new FileImporter();
		String t = "this is a string of words.";
		wr.nextWords(t.split(" "));
	}
}
