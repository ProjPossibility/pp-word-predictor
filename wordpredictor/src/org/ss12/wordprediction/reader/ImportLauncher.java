package org.ss12.wordprediction.reader;

import java.io.File;
import java.io.FileNotFoundException;
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
		
		FileImporter fi = new FileImporter();
		try {
			fi.readFile(new File("resources/sample/test.txt"));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
