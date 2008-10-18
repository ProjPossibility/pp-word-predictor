package org.ss12.wordprediction.phonetic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.codec.language.DoubleMetaphone;
import org.ss12.wordprediction.reader.FileImporter;

public class Phonetic extends FileImporter{
	private TreeMap<String,ArrayList<String>> phoneticMap;
	private DoubleMetaphone metaphone;
	
	public Phonetic(){
		metaphone = new DoubleMetaphone();
		phoneticMap = new TreeMap<String, ArrayList<String>>();		
	}
	public void addPhonetic(String s){
		//change into double metaphone key
		String meta = metaphone.doubleMetaphone(s);
		//if contains key and not contains the string
		if (phoneticMap.containsKey(meta)){
			if (!phoneticMap.get(meta).contains(s)){
				phoneticMap.get(meta).add(s);
			}
		}
		else {
			ArrayList<String> alist = new ArrayList<String>();
			alist.add(s);
			phoneticMap.put(meta, alist);
		}
	}
	
	public void clearupPhonetic(){
		trimMapPhonetic(phoneticMap);
		String path = "resources/dictionaries/user/";
		try {
			saveMapPhonetic(new FileOutputStream(path+"phonetic.dat"));
		}catch (FileNotFoundException e) {
			System.out.println("can't save");
			e.printStackTrace();
		}
	}	
	
	public TreeMap<String,ArrayList<String>> getMap(){
		return phoneticMap;
	}
	
	public void displayPhonetic(String s){
		if (phoneticMap.containsKey(metaphone.doubleMetaphone(s))){
			for (String elem: phoneticMap.get(metaphone.doubleMetaphone(s))){
				if (elem != s){
					System.out.print(elem+' ');
				}
			}
		}
		System.out.println();
	}
	
	public void loadMapPhonetic(File f){
		try {
			FileInputStream objReader = new FileInputStream(f);
			ObjectInputStream in = new ObjectInputStream(objReader);
			phoneticMap = (TreeMap<String, ArrayList<String>>) in.readObject();
			in.close();
		} catch (IOException e) {
			System.out.println("File not found, creating empty Ngram entry instead");
			System.out.println(System.getProperty("user.dir"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
		
	private void saveMapPhonetic(OutputStream os){
		ObjectOutputStream out = null;
		try
		{
			out = new ObjectOutputStream(os);
			out.writeObject(phoneticMap);
			out.close();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	private void trimMapPhonetic(SortedMap<String, ArrayList<String>> sm){
		Set<Entry<String, ArrayList<String>>> s = sm.entrySet();
		Iterator it = s.iterator();
		while(it.hasNext()){
			if (((Entry<String, ArrayList<String>>) it.next()).getValue().size() == 1){
				it.remove();
			}
		}
	}
	private void remove(String key) {
		// TODO Auto-generated method stub
		
	}
}
