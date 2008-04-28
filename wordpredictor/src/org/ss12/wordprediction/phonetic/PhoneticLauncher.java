//package org.ss12.wordprediction.phonetic;
//
//import java.io.File;
//import java.util.ArrayList;
//
//import org.ss12.wordprediction.WordLoader;
//import org.ss12.wordprediction.phonetic.Phonetic;
//import org.ss12.wordprediction.reader.FileImporter;
//
//public class PhoneticLauncher {
//	public static void main(String args[]){
//		Phonetic phonetic = new Phonetic();
//		WordLoader wl = new WordLoader(1);
//		FileImporter fi = new FileImporter(wl, phonetic);
//		System.out.println("Size of phoneticMap is: "+phonetic.getMap().size());
//		try {
//			File d = new File("resources/sample");
//			String [] files = d.list();
//			for (int j = 0; j < files.length; j++){
//				if (files[j].charAt(0) == '.')
//					continue;
//				System.out.println(d.getAbsolutePath()+'/'+files[j]);
//				fi.readFilePhonetic(new File(d.getAbsolutePath()+'/'+files[j]));
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		System.out.println("Size of phoneticMap is: "+phonetic.getMap().size());
//		phonetic.clearupPhonetic();
//		System.out.println("the last key is: "+phonetic.getMap().lastKey());
//		ArrayList<String> al = new ArrayList<String>();
//		al = phonetic.getMap().get(phonetic.getMap().lastKey());
//		for (String s: al){
//			System.out.println(s);
//		}
//		System.out.println("Size of phoneticMap is: "+phonetic.getMap().size());
//	}
//}
