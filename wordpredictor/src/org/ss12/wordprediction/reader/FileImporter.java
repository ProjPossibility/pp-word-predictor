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

package org.ss12.wordprediction.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.ss12.wordprediction.SQLITEWordPredictor;
import org.ss12.wordprediction.TreeMapWordPredictor;
import org.ss12.wordprediction.WordLoader;
import org.ss12.wordprediction.WordReader;
import org.ss12.wordprediction.model.WordPredictor;

public class FileImporter {
	WordLoader wl;
	public WordPredictor pm;
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
		pm = new TreeMapWordPredictor(wl.getWords(),new HashMap<String,Integer>(),new HashMap<String,Integer>(),new HashMap<String,Integer>());
		wr = new WordReader(pm);
	}
	public FileImporter(WordLoader wl, boolean useSQL){
		this.wl = wl;
		if(useSQL)
			pm = new SQLITEWordPredictor();
		else
			pm = new TreeMapWordPredictor(wl.getWords(),new HashMap<String,Integer>(),new HashMap<String,Integer>(),new HashMap<String,Integer>());
		wr = new WordReader(pm);
	}
	public boolean readFile(File f) throws FileNotFoundException{
		boolean b = readFileAndNotCleanup(f);
		//pm.cleanup();
		return b;
	}
	private boolean readFileAndNotCleanup(File f) throws FileNotFoundException{
		BufferedReader br = new BufferedReader(new FileReader(f));
		try {
			while(br.ready()){
//				br.readLine().split(" ");
				wr.nextWords(br.readLine().split(" "));
			}
			br.close();
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
