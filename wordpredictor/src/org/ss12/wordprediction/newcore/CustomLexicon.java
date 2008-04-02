package org.ss12.wordprediction.newcore;

/**
 * TODO(mgp)
 * 
 * @author Michael Parker
 */
public interface CustomLexicon<T extends AnnotatedWord> {
  /**
   * Registers with the lexicon that the given word has just been used.
   * 
   * @param word the used word
   */
  public void addUnigram(String word);

  /**
   * Registers with the lexicon that the given bigram has just been used.
   * 
   * @param firstWord the first word used
   * @param secondWord the second word used
   */
  public void addBigram(String firstWord, String secondWord);

  /**
   * Registers with the lexicon that the given trigram has just been used.
   * 
   * @param firstWord the first word used
   * @param secondWord the second word used
   * @param thirdWord the third word used
   */
  public void addTrigram(String firstWord, String secondWord, String thirdWord);

  /**
   * Returns all completions of the given word without considering context.
   * 
   * @param incompleteWord the incomplete word
   * @return an iterable over all possible completions
   */
  public Iterable<T> getUnigrams(String incompleteWord);

  /**
   * Returns all completions of the given word, considering the preceding word.
   * 
   * @param incompleteWord the incomplete word
   * @param prevWord the first word preceding the incomplete word
   * @return an iterable over all possible completions
   */
  public Iterable<T> getBigrams(String incompleteWord, String prevWord);

  /**
   * Returns all completion of the given word, considering the two preceding
   * words.
   * 
   * @param incompleteWord the incomplete word
   * @param prevWord the first word preceding the incomplete word
   * @param prevPrevWord the second word preceding the incomplete word
   * @return an iterable over all possible completions
   */
  public Iterable<T> getTrigrams(String incompleteWord, String prevWord,
      String prevPrevWord);
}
