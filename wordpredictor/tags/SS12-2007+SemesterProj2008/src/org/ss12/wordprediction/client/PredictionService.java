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


package org.ss12.wordprediction.client;

import com.google.gwt.user.client.rpc.RemoteService;

public interface PredictionService extends RemoteService{
	/**
	 * Updates the frequency of the given unigram.
	 * 
	 * @param s1
	 *            the word
	 */
	public void addUnigram(String s1);

	/**
	 * Updates the frequency of the given bigram.
	 * 
	 * @param s1
	 *            first word
	 * @param s2
	 *            second word
	 */
	public void addBigram(String s1, String s2);

	/**
	 * Updates the frequency of the given trigram.
	 * 
	 * @param s1
	 *            the first word
	 * @param s2
	 *            the second word
	 * @param s3
	 *            the third word
	 */
	public void addTrigram(String s1, String s2, String s3);

	/**
	 * Gets an array of suggestions of length numOfSuggestions based on the
	 * dictionary.
	 * 
	 * @param begin_seq
	 *            starting sequence for suggestions
	 * @param numOfSuggestions
	 *            number of suggestions to return
	 * @return the suggestions
	 */
	public String[] getSuggestionsFromDic(String begin_seq,
			int numOfSuggestions);

	/**
	 * Gets an array of suggestions of length numOfSuggestions based on the
	 * user's inputs. If context is length 1, then this method returns
	 * completions of the provided string. If context is greater than 1, this
	 * method returns completions of the last string in the array, where
	 * previous elements in the array are the preceding words.
	 * 
	 * @param context
	 *            the starting sequence for suggestions, and possible preceding
	 *            words
	 * @param numOfSuggestions
	 *            number of suggestions to return
	 * @return the suggestions
	 */
	public String[] getSuggestionsGramBased(String[] context,
			int numOfSuggestions);

	/**
	 * Saves the internal data to disk.
	 */
	public void cleanup();
}