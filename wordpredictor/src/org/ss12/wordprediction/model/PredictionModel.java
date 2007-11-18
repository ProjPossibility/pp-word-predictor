package org.ss12.wordprediction.model;

public interface PredictionModel {

	public abstract String[] getSuggestions(String begin_seq,
			int numOfSuggestions);

}