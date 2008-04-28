package org.ss12.wordprediction.newcore;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.ss12.wordprediction.newcore.annotations.FrequencyAnnotation;
import org.ss12.wordprediction.newcore.annotations.FrequencyAnnotationFactory;

import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.collections.TransactionRunner;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

/**
 * @author Brad Fol
 *
 */
public class BDBCustomLexicon<T extends AnnotatedWord & Serializable> 
		implements CustomLexicon<T> {
	private Environment env;
	String envName = "CustomLexicon";
	private StoredClassCatalog catalog;
	private Database uniDB;
	private Database biDB;
	private Database triDB;
	private StoredSortedMap unigrams;
	private StoredSortedMap bigrams;
	private StoredSortedMap trigrams;
	TransactionRunner runner;
	
	private final Class<T> dataClass;
	private final AnnotationFactory<T> annotationFactory;
	final static String dir = "./resources/dictionaries/bdb";
	
	
	private void add(SortedMap<WordSequence, T> sequenceMap,
			WordSequence wordSequence){
		T annotation = sequenceMap.get(wordSequence);
		if (annotation != null){
			annotation.update();
			sequenceMap.put(wordSequence, annotation);
		} else {
			String lastWord = wordSequence.getLastWord();
			sequenceMap.put(wordSequence, 
					annotationFactory.newAnnotation(lastWord));
		}
	}
	
	public void addUnigram(String word){
		try {
			add(unigrams, new WordSequence(word));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addBigram(String firstWord, String secondWord)
			throws IllegalStateException {
		try {
			add(bigrams, new WordSequence(firstWord, secondWord));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addTrigram(String firstWord, String secondWord, String thirdWord)
			throws IllegalStateException {
		try {
			add(trigrams, new WordSequence(firstWord, secondWord, thirdWord));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Iterable<T> get(SortedMap<WordSequence, T> sequenceMap,
			WordSequence wordSequence) {
		WordSequence upperBound = WordSequence.getNextSequence(wordSequence);
		System.out.println("lower bound = " + wordSequence);
		System.out.println("upper bound = " + upperBound);
		if (upperBound == null) {
			return Collections.unmodifiableCollection(sequenceMap
					.tailMap(wordSequence).values());
		}
		/*
		List<WordSequence> keys = new ArrayList<WordSequence>(sequenceMap.subMap(
				wordSequence, upperBound).keySet());
		System.out.println("all keys = " + keys);
		for (WordSequence ws : keys) {
			System.out.println("lt: " + wordSequence.compareTo(ws));
		}
		for (WordSequence ws : keys) {
			System.out.println("gt: " + upperBound.compareTo(ws));
		}
		*/
		SortedMap<WordSequence, T> copy = new TreeMap<WordSequence, T>(sequenceMap);
		System.out.println("what it should be: " + copy.subMap(wordSequence, upperBound).keySet());
		System.out.println("what it is = " + sequenceMap.subMap(
				wordSequence, upperBound).keySet());
		return Collections.unmodifiableCollection(sequenceMap.subMap(
				wordSequence, upperBound).values());
	}
	
	public Iterable<T> getUnigrams(String incompleteWord)
			throws IllegalStateException {
		return get(unigrams, new WordSequence(incompleteWord));
	}
	
	public Iterable<T> getBigrams(String prevWord, String incompleteWord)
			throws IllegalStateException {
		return get(bigrams, new WordSequence(prevWord, incompleteWord));
	}

	public Iterable<T> getTrigrams(String prevPrevWord, String prevWord,
			String incompleteWord) throws IllegalStateException {
		return get(trigrams, new WordSequence(prevPrevWord, prevWord,
		        incompleteWord));
	}

	public static void main(String[] args) throws Exception{
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setTransactional(true);
		envConfig.setAllowCreate(true);
		Environment myEnv = new Environment(new File(dir), envConfig);
		
		// will use class AnnotatedWord for the data type 
		// in this test code
		BDBCustomLexicon<FrequencyAnnotation> wp = 
			new BDBCustomLexicon<FrequencyAnnotation>(myEnv, 
					new FrequencyAnnotationFactory(), 
					FrequencyAnnotation.class);
		
//		wp.tester();
		wp.check();
		
		wp.close();
	}
	
	public void tester(){
		addUnigram("cat");
//		addBigram("the", "cat");
//		addBigram("the", "bat");
	}
	
	public void check(){
//		Iterable<T> i = getUnigrams("a");
//		System.out.println(i);
		
		Iterable<T> b = getBigrams("the","c");
		System.out.println(b);
		
		System.out.println(unigrams);
		System.out.println(bigrams);
	}
	
	private BDBCustomLexicon(Environment e, AnnotationFactory<T> a, Class<T> c)
			throws Exception{
		env = e;
		
		annotationFactory = a;
		dataClass = c;
		
		open();
	}

	@SuppressWarnings("unchecked")
	private void open() throws Exception{
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setTransactional(true);
		dbConfig.setAllowCreate(true);
		
//		db = env.openDatabase(null, envName, dbConfig);
		uniDB = env.openDatabase(null, "UnigramCustomLexicon", dbConfig);
		biDB = env.openDatabase(null, "BigramCustomLexicon", dbConfig);
		triDB = env.openDatabase(null, "TrigramCustomLexicon", dbConfig);
		
		dbConfig = new DatabaseConfig();
		dbConfig.setSortedDuplicates(false);
		dbConfig.setAllowCreate(true);
		Database catalogDb = env.openDatabase(null, "classDb", dbConfig);
		catalog = new StoredClassCatalog(catalogDb);
		
		// set the keys to be type WordSequence, and set the data to be the class 
		// set by the constructor
//		EntryBinding uniKeyBinding = new UnigramTupleBinding();
//		TupleBinding uniKeyBinding = TupleBinding.getPrimitiveBinding(UnigramTupleBinding.class);
//		TupleBinding biKeyBinding = TupleBinding.getPrimitiveBinding(BigramTupleBinding.class);
//		TupleBinding triKeyBinding = TupleBinding.getPrimitiveBinding(TrigramTupleBinding.class);
		SerialBinding dataBinding = new SerialBinding(catalog, dataClass);
		
//		map = new StoredSortedMap(db, keyBinding, dataBinding, true);
		unigrams = new StoredSortedMap(uniDB, new UnigramTupleBinding(), dataBinding, true);
		bigrams = new StoredSortedMap(biDB, new BigramTupleBinding(), dataBinding, true);
		trigrams = new StoredSortedMap(triDB, new TrigramTupleBinding(), dataBinding, true);
		
	}
	
	
	private static class UnigramTupleBinding extends TupleBinding {

		public void objectToEntry(Object o, TupleOutput out) {
			WordSequence sequence = (WordSequence) o;
			List<String> words = sequence.getWords();
			
			out.writeString(words.get(0));
		}

		public Object entryToObject(TupleInput in) {
			WordSequence sequence = new WordSequence(in.readString());
			
			return sequence;
		}
		
	}
	
	
	private static class BigramTupleBinding extends TupleBinding {

		public void objectToEntry(Object o, TupleOutput out) {
			WordSequence sequence = (WordSequence) o;
			List<String> words = sequence.getWords();
			
			out.writeString(words.get(0));
			out.writeString(words.get(1));
		}

		public Object entryToObject(TupleInput in) {
			WordSequence sequence = new WordSequence(in.readString(), in.readString());
			
			return sequence;
		}
		
	}
	
	
	private static class TrigramTupleBinding extends TupleBinding {

		public void objectToEntry(Object o, TupleOutput out) {
			WordSequence sequence = (WordSequence) o;
			List<String> words = sequence.getWords();
			
			out.writeString(words.get(0));
			out.writeString(words.get(1));
			out.writeString(words.get(2));
		}

		public Object entryToObject(TupleInput in) {
			WordSequence sequence = new WordSequence(in.readString(), in.readString(), in.readString());
			
			return sequence;
		}
		
	}
	
	public void close() {
		try {
			if (catalog != null) {
				catalog.close();
				catalog = null;
			}
			if (uniDB != null) {
				uniDB.close();
				uniDB = null;
			}
			if (biDB != null) {
				biDB.close();
				biDB = null;
			}
			if (triDB != null) {
				triDB.close();
				triDB = null;
			}
			if (env != null) {
				env.close();
				env = null;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
