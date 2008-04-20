package org.ss12.wordprediction.newcore.annotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.ss12.wordprediction.newcore.AnnotatedWord;
import org.ss12.wordprediction.newcore.Scorer;
import org.ss12.wordprediction.newcore.WordFrequencyPair;

/**
 * An skeletal implementation of a {@link Scorer}.
 * 
 * @author Michael Parker
 */
public abstract class AbstractScorer<T extends AnnotatedWord> implements
    Scorer<T> {
  protected final Set<String> allWords;
  protected final Comparator<T> comparator;

  protected List<T> trigrams;
  protected List<T> bigrams;
  protected List<T> unigrams;
  protected List<WordFrequencyPair> additionalLexicons;
  protected List<WordFrequencyPair> defaultLexicon;

  public AbstractScorer(Comparator<T> comparator) {
    allWords = new HashSet<String>();
    this.comparator = comparator;

    trigrams = null;
    bigrams = null;
    unigrams = null;
    additionalLexicons = null;
    defaultLexicon = null;
  }

  /**
   * Appends each word in the iterable that has not yet been seen to the given
   * list.
   * 
   * @param annotatedWords the iterable over words and their metadata
   * @param list the list to append to
   */
  protected void addAnnotatedWords(Iterable<T> annotatedWords, List<T> list) {
    for (T annotatedWord : annotatedWords) {
      String word = annotatedWord.getWord();
      if (!allWords.contains(word)) {
        allWords.add(word);
        list.add(annotatedWord);
      }
    }
  }

  /**
   * Appends each word in the iterable that has not yet been seen to the given
   * list.
   * 
   * @param wordSignificances the iterable over words and their significance
   * @param list the list to append ot
   */
  protected void addWordSignificances(
      Iterable<WordFrequencyPair> wordSignificances, List<WordFrequencyPair> list) {
    for (WordFrequencyPair wordSignificance : wordSignificances) {
      if (!allWords.contains(wordSignificance.word)) {
        allWords.add(wordSignificance.word);
        list.add(wordSignificance);
      }
    }
  }

  /**
   * Sorts the given list of annotated words by the comparator passed into the
   * constructor, and then appends the words in order to the given list of
   * suggestions.
   * 
   * @param annotatedWords the list of annotated words
   * @param suggestions the list of suggestions to append to
   */
  protected void appendAnnotatedWords(List<T> annotatedWords,
      List<String> suggestions) {
    Collections.sort(annotatedWords, comparator);
    for (T annotatedWord : annotatedWords) {
      suggestions.add(annotatedWord.getWord());
    }
  }

  /**
   * Sorts the given list of words and their significance by significance, and
   * then appends the words in order to the given list of suggestions.
   * 
   * @param wordSignificances the list of words and their significance
   * @param suggestions the list of suggestions to append to
   */
  protected void appendWordSignificances(
      List<WordFrequencyPair> wordSignificances, List<String> suggestions) {
    Collections.sort(wordSignificances, WordFrequencyPair.COMPARATOR);
    for (WordFrequencyPair wordSignificance : wordSignificances) {
      suggestions.add(wordSignificance.word);
    }
  }

  public void addTrigrams(Iterable<T> customTrigrams) {
    this.trigrams = new ArrayList<T>();
    addAnnotatedWords(customTrigrams, this.trigrams);
  }

  public void addBigrams(Iterable<T> customBigrams) {
    this.bigrams = new ArrayList<T>();
    addAnnotatedWords(customBigrams, this.bigrams);
  }

  public void addUnigrams(Iterable<T> customUnigrams) {
    this.unigrams = new ArrayList<T>();
    addAnnotatedWords(customUnigrams, this.unigrams);
  }

  public void addAdditionalLexicon(Iterable<WordFrequencyPair> words) {
    additionalLexicons = new ArrayList<WordFrequencyPair>();
    addWordSignificances(words, additionalLexicons);
  }

  public void addDefaultLexicon(Iterable<WordFrequencyPair> words) {
    defaultLexicon = new ArrayList<WordFrequencyPair>();
    addWordSignificances(words, defaultLexicon);
  }

  public boolean hasNumSuggestions(int numSuggestions) {
    return (allWords.size() >= numSuggestions);
  }

  public List<String> getSuggestions() {
    List<String> suggestions = new ArrayList<String>();

    if (trigrams != null) {
      appendAnnotatedWords(trigrams, suggestions);
    }
    if (bigrams != null) {
      appendAnnotatedWords(bigrams, suggestions);
    }
    if (unigrams != null) {
      appendAnnotatedWords(unigrams, suggestions);
    }

    if (additionalLexicons != null) {
      appendWordSignificances(additionalLexicons, suggestions);
    }
    if (defaultLexicon != null) {
      appendWordSignificances(defaultLexicon, suggestions);
    }
    return suggestions;
  }
}
