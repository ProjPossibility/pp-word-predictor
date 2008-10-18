package org.ss12.wordprediction.newcore;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.Map.Entry;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.collections.TransactionRunner;
import com.sleepycat.collections.TransactionWorker;
import com.sleepycat.je.CheckpointConfig;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.EnvironmentMutableConfig;

/**
 * @author Brad Fol
 *
 */
public class BDBImmutableLexicon implements ImmutableLexicon {

	private Environment env;
	String envName = "ImmutableLexicon";
	private Database db;
	public SortedMap<String, Integer> map;
	TransactionRunner runner;
	
	final static String dir = "./resources/dictionaries/bdb/immutable";
	
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

	public void add(Map<String, Integer> map) {
		System.out.println("Words to save: " + map.entrySet().size());
		int count=0;
		for (Iterator<Entry<String,Integer>> it = map.entrySet().iterator(); it.hasNext();) {
//			if(count++<5000001){
//				it.next();
//				continue;
//			}
			if(count++%1000000==0){
				System.out.println("Read "+count+" words so far");
				System.gc();
				try {
					env.sync();
				} catch (DatabaseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			Entry<String, Integer> e = it.next();
//			if(e.getValue()>5)
			incrementWord(e.getKey(), e.getValue());
			it.remove();
		}
		System.out.println("All words saved");
		map.clear();
		try {
			env.sync();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<10;i++)
			System.out.println("gc: "+i);
			System.gc();
	}

	public void incrementWord(String word){
		incrementWord(word, 1);
	}
	
	public void incrementWord(String word, int toAdd) {
		Integer count = map.get(word);
		if (count == null){
			map.put(word, toAdd);
		} else {
			map.put(word, count + toAdd);
		}
		/*
		TransactionWorker worker = new IncrementWordCount(word);
		try{runner.run(worker);}
		catch(Exception e){
			e.printStackTrace();
		}
		*/
	}
	
	public void tester() throws Exception{
		for (int i = 0; i < 100000; ++i) {
			TransactionWorker worker =  new IncrementWordCount("cat");
			try{runner.run(worker);}
			
//		finally{}
//		worker= new IncrementWordCount("dog");
//		try {runner.run(worker);}
			
			finally {
				System.out.println("map = " + map);
			}
			
		}
	}

	public void check() throws Exception{
//		Iterable<WordFrequencyPair> i = getSignificance(null, null);
//		
//		System.out.println(i);
		
		System.out.println("size: "+map.entrySet().size());
		
//		System.out.println("the: "+ map.get("the"));
//		System.out.println("cat: "+ map.get("cat"));
//		System.out.println("upside: "+ map.get("upside"));
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
	
	public int compress() throws Exception{
		EnvironmentMutableConfig config = env.getMutableConfig();
		config.setConfigParam("je.cleaner.minUtilization", "90");
		
		env.setMutableConfig(config);
		System.out.println(env.getConfig());
		
		boolean anyCleaned = false;
		int i = env.cleanLog();
		int out=i;
		while (i > 0) {
//			System.out.println("i: "+i);
			anyCleaned = true;
			i = env.cleanLog();
			out+=i;
		}
		if (anyCleaned) {
			CheckpointConfig force = new CheckpointConfig();
			force.setForce(true);
			env.checkpoint(force);
		}
		
		return out;
	}
	
	public int trim(int n) throws Exception{
		int trimmed=0;
		for (Iterator<Entry<String,Integer>> it = this.map.entrySet().iterator(); it.hasNext();) {
			Entry<String,Integer> e = it.next();
			if(e.getValue()<n){
				trimmed++;
				it.remove();
			}
		}
		return trimmed;
	}

	public static void main(String[] args) throws Exception{
		// environment is transactional
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setTransactional(false);
		envConfig.setAllowCreate(true);
		Environment myEnv = new Environment(new File(dir), envConfig);
		BDBImmutableLexicon wp = new BDBImmutableLexicon(myEnv);

//		wp.tester();
		wp.check();

		System.out.println("compress out: "+wp.compress());
		wp.close();
	}
	
	public BDBImmutableLexicon(Environment e) throws Exception{
		runner = new TransactionRunner(e);
		env = e;
		open();
	}

	@SuppressWarnings("unchecked")
	private void open() throws Exception{
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setTransactional(false);
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
//				System.out.println("now close db");
				db.close();
				db = null;
			}
			if (env != null) {
//				System.out.println("now close env");
				env.close();
				env = null;
			}
		} catch (DatabaseException de){
			System.out.println("Closing a database with active cursors, closing them");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
