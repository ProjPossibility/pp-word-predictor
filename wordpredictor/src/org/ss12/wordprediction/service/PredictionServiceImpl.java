/*
    This file is part of Word Predictor.

    Word Predictor is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Word Predictor is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Word Predictor.  If not, see <http://www.gnu.org/licenses/>. 
    
    This software was developed by members of Project:Possibility, a software 
    collaboration for the disabled.
    
    For more information, visit http://projectpossibility.org
*/

package org.ss12.wordprediction.service;

import org.ss12.wordprediction.client.PredictionService;

import org.ss12.wordprediction.TreeMapWordPredictor;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
/**
 * Returns suggestions from a dictionary and from what the user inputs.
 */
public class PredictionServiceImpl extends RemoteServiceServlet implements PredictionService {
	/**
	 * 
	 */
	private static final long serialVersionUID = -190827977040318307L;
	static TreeMapWordPredictor wp;
	
	public PredictionServiceImpl(){
		wp = new TreeMapWordPredictor();
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