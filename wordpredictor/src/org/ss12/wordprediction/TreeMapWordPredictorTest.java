package org.ss12.wordprediction;

import junit.framework.TestCase;

import org.ss12.wordprediction.TreeMapWordPredictor;

public class TreeMapWordPredictorTest extends TestCase {
	public void testprocessString(){
		TreeMapWordPredictor tmwp = new TreeMapWordPredictor();
		String[] s = tmwp.processString("I am a cat named Lily");
		System.out.println(s);
	}
	
	public void testprocessStringWithWordNum(){
		TreeMapWordPredictor tmwp = new TreeMapWordPredictor();
		String[] s = tmwp.processString("I am a cat named Lily ", 3);
		System.out.print(s);
	}

}
