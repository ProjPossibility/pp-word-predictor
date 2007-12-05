package org.ss12.wordprediction.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

import org.ss12.wordprediction.WordLoader;
import org.ss12.wordprediction.WordPredictor;
import org.ss12.wordprediction.WordReader;
import org.ss12.wordprediction.model.PredictionModel;

public class FileImporter {
	WordLoader wl;
	public PredictionModel pm;
	WordReader wr;
	public FileImporter(){
		wl = new WordLoader(1);
		try {
			wl.loadDictionary(new File("resources/dictionaries/converted/plain.dat"));
			wl.loadFrequenciess(new File("resources/dictionaries/converted/freq.dat"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*File uni = new File("resources/dictionaries/user/uni.dat");
		File bi = new File("resources/dictionaries/user/bi.dat");
		File tri = new File("resources/dictionaries/user/tri.dat");
		*/
		pm = new WordPredictor(wl.getWords(),new TreeMap<String,Integer>(),new TreeMap<String,Integer>(),new TreeMap<String,Integer>());
	}
	public FileImporter(WordLoader wl){
		this.wl = wl;
		pm = new WordPredictor(wl.getWords(),new TreeMap<String,Integer>(),new TreeMap<String,Integer>(),new TreeMap<String,Integer>());
	}
	public boolean readFile(File f) throws FileNotFoundException{
		boolean b = readFileAndNotCleanup(f);
		//pm.cleanup();
		return b;
	}
	private boolean readFileAndNotCleanup(File f) throws FileNotFoundException{
		BufferedReader br = new BufferedReader(new FileReader(f));
		wr = new WordReader(pm);
		try {
			while(br.ready()){
				wr.nextWords(br.readLine().split(" "));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	public boolean readFiles(List<File> files) throws FileNotFoundException{
		boolean retval=true;
		for(File f : files){
			retval = readFile(f) && retval;
		}
		pm.cleanup();
		return retval;
	}
}
