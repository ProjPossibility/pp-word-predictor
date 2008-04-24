package org.ss12.wordprediction.customer;

import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import junit.framework.TestCase;

public class EntryWindowTest extends TestCase {
	public void testSetPeriod(){
		EntryWindow ew = new EntryWindow();
		ew.setMode(3);
		assertEquals(3, ew.getMode());
		//assertEquals(, ew.getPeriod());
		//assertEquals(2, ew.getWordNumber());
	}
	
	public void testUpdate(){
		EntryWindow ew = new EntryWindow();
		ew.update("a", "a b", "a b c");
		ew.update("a", "a b", "a b d");
		System.out.println(ew.getUnigram());
		System.out.println(ew.getBigram());
		System.out.println(ew.getTrigram());
	}
	
	/*
	// change updateUp and down to public before test this part
	public void testUpdateDown(){
		EntryWindow ew = new EntryWindow();
		SortedMap<String, Integer> unigram = new TreeMap<String, Integer>();
		assertEquals(0, unigram.size());
		unigram.put("a", 1);
		unigram.put("b", 2);
		ew.updateDown(unigram, "a");
		ew.updateUp(unigram, "c");
		System.out.println(unigram.get("a"));
		//System.out.println(unigram.get("c"));
		assertEquals((Integer)1, unigram.get("c"));
	}
	*/
	/*
	// change updateEntryWindow to public and add map = unigram; comment parameter map to test
	public void testupdateEntryWindow(){
		EntryWindow ew = new EntryWindow();
		ew.setMode(2);
		System.out.println("mode:" + ew.getMode());
		System.out.println("WordNumber:" + ew.getWordNumber());
		SortedMap<String, Integer> gram = new TreeMap<String, Integer>();
		Date d1 = new Date();
		ew.updateEntryWindow(gram,"a",d1);
		System.out.println(gram.size());
		Date d2 = new Date();
		ew.updateEntryWindow(gram,"b",d2);
		System.out.println(gram.size());
		Date d3 = new Date();
		ew.updateEntryWindow(gram,"c",d3);
		System.out.println(gram.size());
		System.out.println(gram.firstKey());
		System.out.println(gram.lastKey());
	}
	*/
	/*
	// change updateEntryWindow to public and add map = unigram; comment parameter map to test
	public void testupdateEntryWindow(){
		EntryWindow ew = new EntryWindow();
		ew.setMode(1);
		System.out.println("mode:" + ew.getMode());
		System.out.println("WordNumber:" + ew.getWordNumber());
		System.out.println("Period:" + ew.getPeriod());
		SortedMap<String, Integer> gram = new TreeMap<String, Integer>();
		Date d1 = new Date();
		ew.updateEntryWindow(gram,"a",d1);
		System.out.println(gram.size());
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Date d2 = new Date();
		ew.updateEntryWindow(gram,"b",d2);
		System.out.println(gram.size());
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Date d3 = new Date();
		ew.updateEntryWindow(gram,"c",d3);
		System.out.println(gram.size());
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Date d4 = new Date();
		ew.updateEntryWindow(gram,"d",d4);
		System.out.println(gram.size());
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Date d5 = new Date();
		ew.updateEntryWindow(gram,"e",d5);
		System.out.println(gram.size());
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(gram.firstKey());
		System.out.println(gram.lastKey());
		
	}
	*/
}
