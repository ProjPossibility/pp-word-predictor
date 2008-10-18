package org.ss12.wordprediction;

import junit.framework.TestCase;
import org.ss12.wordprediction.WordReader;
import org.ss12.wordprediction.model.WordPredictor;

public class WordLoaderTest extends TestCase {
	public void testWordReader(){
		WordPredictor wp = new TreeMapWordPredictor();
		WordReader wr = new WordReader(wp);
		System.out.print(wr.wordify("St-show."));
	}
	
	public void testnextWord(){
		WordPredictor wp = new TreeMapWordPredictor();
		WordReader wr = new WordReader(wp);
		String[] s = {"Hello wo", "Lily "};
		wr.nextWord(s);
	}
	public void testwordify(){
		WordPredictor wp = new TreeMapWordPredictor();
		WordReader wr = new WordReader(wp);
		System.out.println(wr.wordify("he llo "));
	}
}
