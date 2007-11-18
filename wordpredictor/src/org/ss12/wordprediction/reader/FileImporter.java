package org.ss12.wordprediction.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.ss12.wordprediction.WordPredictor;
import org.ss12.wordprediction.WordReader;
import org.ss12.wordprediction.model.PredictionModel;

public class FileImporter {
	public boolean readFile(File f) throws FileNotFoundException{
		BufferedReader br = new BufferedReader(new FileReader(f));
		PredictionModel pm = new WordPredictor(null);
		WordReader wr = new WordReader(pm);
		try {
			while(br.ready()){
				wr.nextWords(br.readLine().split(" "));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	public boolean readFiles(List<File> files) throws FileNotFoundException{
		boolean retval=true;
		for(File f : files){
			retval = readFile(f) && retval;
		}
		return retval;
	}
}
