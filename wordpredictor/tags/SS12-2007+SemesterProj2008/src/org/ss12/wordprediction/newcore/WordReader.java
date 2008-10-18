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

package org.ss12.wordprediction.newcore;

import org.ss12.wordprediction.newcore.AnnotatedWord;
import org.ss12.wordprediction.newcore.CustomLexicon;
import org.ss12.wordprediction.newcore.SentenceReader;

/**
 * Accepts a sequence of words and adds all found unigrams, bigrams, and
 * trigrams to a backing {@link CustomLexicon}. If entering sentences, use
 * {@link SentenceReader}.
 */
public class WordReader {
  private final CustomLexicon<? extends AnnotatedWord> customLexicon;
  private String prevPrevWord;
  private String prevWord;

  /**
   * Creates a new word reader that adds to the given custom lexicon.
   * 
   * @param customLexicon the lexicon to add to
   */
  public WordReader(CustomLexicon<? extends AnnotatedWord> customLexicon) {
    this.customLexicon = customLexicon;
    prevPrevWord = null;
    prevWord = null;
  }

  /**
   * @param word
   */
  public void nextWord(String word) {
    String formattedWord = word.toLowerCase().trim();
    customLexicon.addUnigram(formattedWord);
    if (prevWord != null) {
      customLexicon.addBigram(prevWord, formattedWord);
    }
    if (prevPrevWord != null) {
      customLexicon.addTrigram(prevPrevWord, prevWord, formattedWord);
    }

    prevPrevWord = prevWord;
    prevWord = formattedWord;
  }

  /**
   * Extracts unigrams, bigrams, and trigrams from the given sequence of words
   * and updates the custom lexicon.
   * 
   * @param words the words
   */
  public void nextWords(String[] words) {
    for (String word : words) {
      nextWord(word);
    }
  }
}