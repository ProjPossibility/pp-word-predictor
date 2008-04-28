package org.ss12.wordprediction.newcore;

/**
 * An mutable collection of {@link AnnotatedWord} instances, which are updated
 * as we collect unigrams, bigrams, and trigrams from the user.
 * 
 * @author Michael Parker
 */
public interface CustomLexicon<T extends AnnotatedWord> {
  /**
   * Registers with the lexicon that the given word has just been used.
   * 
   * @param word the used word
   * @throws IllegalStateException if the lexicon is closed and cannot be
   *           written
   */
  public void addUnigram(String word) throws IllegalStateException;

  /**
   * Registers with the lexicon that the given bigram has just been used.
   * 
   * @param firstWord the first word used
   * @param secondWord the second word used
   * @throws IllegalStateException if the lexicon is closed and cannot be
   *           written
   */
  public void addBigram(String firstWord, String secondWord)
      throws IllegalStateException;

  /**
   * Registers with the lexicon that the given trigram has just been used.
   * 
   * @param firstWord the first word used
   * @param secondWord the second word used
   * @param thirdWord the third word used
   * @throws IllegalStateException if the lexicon is closed and cannot be
   *           written
   */
  public void addTrigram(String firstWord, String secondWord, String thirdWord)
      throws IllegalStateException;

  /**
   * Returns all completions of the given word without considering context.
   * 
   * @param incompleteWord the incomplete word
   * @return an iterable over all possible completions
   * @throws IllegalStateException if the lexicon is closed and cannot be read
   */
  public Iterable<T> getUnigrams(String incompleteWord)
      throws IllegalStateException;

  /**
   * Returns all completions of the given word, considering the preceding word.
   * 
   * @param prevWord the first word preceding the incomplete word
   * @param incompleteWord the incomplete word
   * @return an iterable over all possible completions
   * @throws IllegalStateException if the lexicon is closed and cannot be read
   */
  public Iterable<T> getBigrams(String prevWord, String incompleteWord)
      throws IllegalStateException;

  /**
   * Returns all completion of the given word, considering the two preceding
   * words.
   * 
   * @param prevPrevWord the second word preceding the incomplete word
   * @param prevWord the first word preceding the incomplete word
   * @param incompleteWord the incomplete word
   * @return an iterable over all possible completions
   * @throws IllegalStateException if the lexicon is closed and cannot be read
   */
  public Iterable<T> getTrigrams(String prevPrevWord, String prevWord,
      String incompleteWord) throws IllegalStateException;

  /**
   * Closes this lexicon, releasing all held resources and commiting all changes
   * to disk. After this method is invoked, it can no longer be read from or
   * written to.
   */
  public void close();
}
