package org.ss12.wordprediction.newcore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import com.sleepycat.bind.tuple.TupleBinding;
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
public class BDBImmutableLexicon implements ImmutableLexicon {

	private Environment env;
	String envName = "ImmutableLexicon";
	private Database db;
	private SortedMap<String, Integer> map;
	TransactionRunner runner;
	
	final static String dir = "./resources/dictionaries/bdb";
	
	public Iterable<WordFrequencyPair> getSignificance(String lowBound,
			String highBound) {
		SortedMap<String, Integer> subMap;
		if ((lowBound != null) && (highBound != null)) {
			subMap = map.subMap(lowBound, highBound);
		} else if (lowBound != null) {
			subMap =  map.tailMap(lowBound);
		} else if (highBound != null) {
			subMap = map.headMap(highBound);
		}else{
			subMap=map;
		}
		List<WordFrequencyPair> l= new ArrayList<WordFrequencyPair>(subMap.size());
		for(Map.Entry<String,Integer> e: subMap.entrySet()){
			l.add(new WordFrequencyPair(e.getKey(),e.getValue()));
		}
		return l;
	}
	
	public void tester() throws Exception{
		TransactionWorker worker =  new IncrementWordCount("cat");
		try{runner.run(worker);}
		
//		finally{}
//		worker= new IncrementWordCount("dog");
//		try {runner.run(worker);}

		finally {
			System.out.println("map = " + map);
		}
	}

	public void check() throws Exception{
		Iterable<WordFrequencyPair> i = getSignificance(null, null);
		
		System.out.println(i);
	}
	
	public class IncrementWordCount implements TransactionWorker{
		private String word;
		
		public IncrementWordCount(String w){
			word = w;
		}
		
		public void doWork() {
			Integer count = map.get(word);
			if (count == null){
				map.put(word, 1);
			} else {
				map.put(word, count +1);
			}
		}
	}

	public static void main(String[] args) throws Exception{
		// environment is transactional
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setTransactional(true);
		envConfig.setAllowCreate(true);
		Environment myEnv = new Environment(new File(dir), envConfig);
		BDBImmutableLexicon wp = new BDBImmutableLexicon(myEnv);

		// wp.tester();
		wp.check();
		
		wp.close();
	}
	
	private BDBImmutableLexicon(Environment e) throws Exception{
		runner = new TransactionRunner(e);
		env = e;
		open();
	}

	@SuppressWarnings("unchecked")
	private void open() throws Exception{
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setTransactional(true);
		dbConfig.setAllowCreate(true);
		
		// sets the keys to be type String, and the data to be Integer
		TupleBinding keyBinding = TupleBinding.getPrimitiveBinding(String.class);
		TupleBinding dataBinding = TupleBinding.getPrimitiveBinding(Integer.class);
		
		db = env.openDatabase(null, envName, dbConfig);
		
		map = new StoredSortedMap(db, keyBinding, dataBinding, true);
	}
	
	public void close() {
		try {
			if (db != null) {
				db.close();
				db = null;
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
