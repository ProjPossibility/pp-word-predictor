package org.ss12.wordprediction.service;

import org.ss12.wordprediction.client.PredictionService;

import org.ss12.wordprediction.WordPredictor;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
/**
 * Returns suggestions from a dictionary and from what the user inputs.
 */
public class PredictionServiceImpl extends RemoteServiceServlet implements PredictionService {
	/**
	 * 
	 */
	private static final long serialVersionUID = -190827977040318307L;
	static WordPredictor wp;
	
	public PredictionServiceImpl(){
		wp = new WordPredictor();
	}
	
	public void addUnigram(String s1){
		wp.addUnigram(s1);
	}
	public void addBigram(String s1, String s2){
		wp.addBigram(s1, s2);
	}
	public void addTrigram(String s1, String s2, String s3){
		wp.addTrigram(s1, s2, s3);
	}
	public String[] getSuggestionsFromDic(String begin_seq,
			int numOfSuggestions){
		return wp.getSuggestionsFromDic(begin_seq, numOfSuggestions);
	}

	public String[] getSuggestionsGramBased(String[] context,
			int numOfSuggestions){
		return wp.getSuggestionsGramBased(context, numOfSuggestions);
	}

	public void cleanup(){
		wp.cleanup();
	}
}