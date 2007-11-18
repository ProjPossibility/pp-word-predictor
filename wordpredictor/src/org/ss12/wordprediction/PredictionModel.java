package org.ss12.wordprediction;

interface PredictionModel {

	public abstract String[] getSuggestions(String begin_seq,
			int numOfSuggestions);

}