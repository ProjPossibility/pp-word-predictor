package org.ss12.wordprediction.newcore;

import java.util.List;

/**
 * This class, when used with {@link ScoringWordPredictor}, scores possible
 * suggestions. Changing the scorer implementation allows changing the
 * suggestions by the predictor at runtime.
 * 
 * @see ScoringWordPredictor
 * @see AnnotationFactory
 * @author Michael Parker
 */
public interface Scorer<T extends AnnotatedWord> {
  /**
   * Adds the given trigrams from the custom lexicon to the suggestions to
   * score.
   * 
   * @param trigrams the trigrams from the custom lexicon
   */
  public void addTrigrams(Iterable<T> trigrams);

  /**
   * Adds the given bigrams from the custom lexicon to the suggestions to score.
   * 
   * @param bigrams the bigrams from the custom lexicon
   */
  public void addBigrams(Iterable<T> bigrams);

  /**
   * Adds the given unigrams from the custom lexicon to the suggestions to
   * score.
   * 
   * @param unigrams the unigrams from the custom lexicon
   */
  public void addUnigrams(Iterable<T> unigrams);

  /**
   * Adds the given additional lexicon to the suggestions to score.
   * 
   * @param words the words from an additional lexicon
   */
  public void addAdditionalLexicon(Iterable<WordSignificance> words);

  /**
   * Adds the default lexicon to the suggestions to score.
   * 
   * @param words the words from the default lexicon
   */
  public void addDefaultLexicon(Iterable<WordSignificance> words);

  /**
   * Returns whether {@link #getSuggestions()} will return at least the given
   * number of suggestions.
   * 
   * @param numSuggestions the desired number of suggestions to return
   * @return {@code true} if the scorer has at least the given number of
   *         suggestions, {@code false} otherwise
   */
  public boolean hasNumSuggestions(int numSuggestions);

  /**
   * Returns the suggested predictions, in order from most likely to least
   * likely.
   */
  public List<String> getSuggestions();
}
