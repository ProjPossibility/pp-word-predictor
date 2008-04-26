package org.ss12.wordprediction.newcore;

import java.io.File;
import java.io.Serializable;
import java.util.Collections;
import java.util.SortedMap;

import org.ss12.wordprediction.newcore.annotations.FrequencyAnnotation;
import org.ss12.wordprediction.newcore.annotations.FrequencyAnnotationFactory;

import com.sleepycat.bind.serial.ClassCatalog;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.collections.TransactionRunner;
import com.sleepycat.collections.TransactionWorker;
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
	private ClassCatalog catalog;
	private Database uniDB;
	private Database biDB;
	private Database triDB;
	private SortedMap<WordSequence, T> unigrams;
	private SortedMap<WordSequence, T> bigrams;
	private SortedMap<WordSequence, T> trigrams;
	TransactionRunner runner;
	
	private final Class<T> dataClass;
	private final AnnotationFactory<T> annotationFactory;
	final static String dir = "./resources/dictionaries/bdb";
	
	
	private void add(SortedMap<WordSequence, T> sequenceMap,
			WordSequence wordSequence) throws Exception{
		TransactionWorker worker = new AddWord(sequenceMap, wordSequence);
		try{runner.run(worker);} 
		finally{}
	}
	
	public class AddWord implements TransactionWorker{
		SortedMap<WordSequence, T> sequenceMap;
		WordSequence wordSequence;
		
		public AddWord(SortedMap<WordSequence, T> map,
				WordSequence w){
			sequenceMap = map;
			wordSequence = w;
		}
		
		public void doWork(){
			T annotation = sequenceMap.get(wordSequence);
			if (annotation != null){
				annotation.update();
			} else {
				String lastWord = wordSequence.getLastWord();
				sequenceMap.put(wordSequence, 
						annotationFactory.newAnnotation(lastWord));
			}
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
		if (upperBound == null) {
			return Collections.unmodifiableCollection(sequenceMap
					.tailMap(wordSequence).values());
		}
		return Collections.unmodifiableCollection(sequenceMap.subMap(
				wordSequence, upperBound).values());
	}
	
	public Iterable<T> getUnigrams(String incompleteWord)
			throws IllegalStateException {
		return get(unigrams, new WordSequence(incompleteWord));
	}
	
	public Iterable<T> getBigrams(String incompleteWord, String prevWord)
			throws IllegalStateException {
		return get(bigrams, new WordSequence(prevWord, incompleteWord));
	}

	public Iterable<T> getTrigrams(String incompleteWord, String prevWord,
			String prevPrevWord) throws IllegalStateException {
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
		
		wp.tester();
		wp.check();
		wp.close();
	}
	
	public void tester(){
		addUnigram("cat");
//		addBigram("the", "cat");
	}
	
	public void check(){
//		Iterable<T> i = getUnigrams("a");
//		System.out.println(i);
		
		System.out.println(unigrams);
	}
	
	private BDBCustomLexicon(Environment e, AnnotationFactory<T> a, Class<T> c)
			throws Exception{
		runner = new TransactionRunner(e);
		env = e;
		open();
		
		annotationFactory = a;
		dataClass = c;
	}

	@SuppressWarnings("unchecked")
	private void open() throws Exception{
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setTransactional(true);
		dbConfig.setAllowCreate(true);
		
		Database catalogDb = env.openDatabase(null, "catalog", dbConfig);
		catalog = new StoredClassCatalog(catalogDb);
		
		// set the keys to be type WordSequence, and set the data to be the class 
		// set by the constructor
		SerialBinding keyBinding = new SerialBinding(catalog, WordSequence.class);
		SerialBinding dataBinding = new SerialBinding(catalog, dataClass);
		
//		db = env.openDatabase(null, envName, dbConfig);
		uniDB = env.openDatabase(null, "UnigramCustomLexicon", dbConfig);
		biDB = env.openDatabase(null, "BigramCustomLexicon", dbConfig);
		triDB = env.openDatabase(null, "TrigramCustomLexicon", dbConfig);
		
//		map = new StoredSortedMap(db, keyBinding, dataBinding, true);
		unigrams = new StoredSortedMap(uniDB, keyBinding, dataBinding, true);
		bigrams = new StoredSortedMap(biDB, keyBinding, dataBinding, true);
		trigrams = new StoredSortedMap(triDB, keyBinding, dataBinding, true);
		
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
