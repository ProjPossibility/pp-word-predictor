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
		long t = System.nanoTime();
		try {
			wl.loadDictionary(new File("resources/dictionaries/converted/plain.dat"));
			wl.loadFrequenciess(new File("resources/dictionaries/converted/freq.dat"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FileImporter fi = new FileImporter(wl);
		try{
			File d = new File("resources/sample");
			String[] files = d.list();
			for(int j=0;j<files.length;j++){
				if(files[j].charAt(0)=='.')
					continue;
				System.out.println(d.getAbsolutePath()+"/"+files[j]);
				fi.readFile(new File(d.getAbsolutePath()+"/"+files[j]));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		fi.pm.cleanup();
		System.out.println("Time elapsed: "+(float)(System.nanoTime()-t)/1000000000.0);
	}
}
