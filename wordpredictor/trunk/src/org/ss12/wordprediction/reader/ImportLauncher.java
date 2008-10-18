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

import java.io.File;
import java.io.IOException;

import org.ss12.wordprediction.WordLoader;

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
		
		FileImporter fi = new FileImporter(wl,false);//true to use SQLITE, false to use HashMap
//		try{
//			File d = new File("C:/articles");
//			String[] files = d.list();
//			for(int j=0;j<files.length;j++){
//				if(files[j].charAt(0)=='.')
//					continue;
////				if(files[j].compareTo("article00575344.txt")<=0)
////					continue;
//				System.out.println(d.getAbsolutePath()+"/"+files[j]);
//				File f = new File(d.getAbsolutePath()+"/"+files[j]);
//				fi.readFile(f);
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		fi.pm.cleanup();
		System.out.println("Time elapsed: "+(float)(System.nanoTime()-t)/1000000000.0 + " seconds");
	}
}
