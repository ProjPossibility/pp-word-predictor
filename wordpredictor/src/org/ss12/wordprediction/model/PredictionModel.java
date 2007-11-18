package org.ss12.wordprediction.model;

public interface PredictionModel {
	public abstract String[] getSuggestions(String begin_seq,
			int numOfSuggestions);
	public abstract void addUnigram(String s1);
	public abstract void addBigram(String s1,String s2);
	public abstract void addTrigram(String s1, String s2, String s3);
}